package io;

import static app.Settings.STAR_HEADERS;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import app.Settings;
import domain.galaxy.Coordinate;
import domain.galaxy.LuminosityClass;
import domain.galaxy.Star;
import domain.galaxy.StarType;

public class CsvStarHandler {

    private static CSVRecord badRecord;

    public static Set<Star> readStarsfromCsv(String path) throws IOException {

        Path p = java.nio.file.Paths.get(path);


        char delimiter = detectDelimiter(p);

        if (delimiter == ';') {

            return readSemicolonStarsAsSimple(p);
        }


        Set<Star> stars = new HashSet<>();
        boolean firstLine = true;

        try (Reader reader = Files.newBufferedReader(p, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(STAR_HEADERS))) {

            for (CSVRecord record : csvParser) {
                badRecord = record;

                if (firstLine) { 
                    firstLine = false;
                    continue;
                }

                try {
                    String designation = record.get(0);
                    String name = record.get(1);
                    String starType = record.get(2);
                    String sequence = record.get(3);
                    String luminosity = record.get(4);
                    String magnitude = record.get(5);
                    String distance = record.get(6);
                    String longitude = record.get(7);
                    String latitude = record.get(8);
                    String goldilockStart = record.get(9);
                    String goldilockEnd = record.get(10);
                    String sisterDesignation = record.get(11);

                    StarType type = StarType.parse(starType);
                    if (type == null) {

                        continue;
                    }


                    Star dummySister = new Star();
                    if (sisterDesignation == null || sisterDesignation.trim().isEmpty()) {
                        dummySister.setDesignation(null);
                    } else {
                        dummySister.setDesignation(sisterDesignation.trim());
                    }

                    Star star = new Star(
                            designation,
                            name,
                            type,
                            Integer.valueOf(sequence),
                            LuminosityClass.parse(luminosity),
                            Float.valueOf(magnitude),
                            Float.valueOf(distance),
                            Float.valueOf(longitude),
                            Float.valueOf(latitude),
                            Double.valueOf(goldilockStart),
                            Double.valueOf(goldilockEnd),
                            dummySister
                    );

                    stars.add(star);

                } catch (Exception ex) {
                    System.out.println("Bad record found: " + badRecord);
                    ex.printStackTrace();
                }
            }
        }

        return stars;
    }


    public static Set<Star> readInitStarList() {
        Set<Star> stars = new HashSet<>();

        File existingStars = Settings.instance().getExistingStarsFile();

        try (Reader reader = Files.newBufferedReader(existingStars.toPath(), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.newFormat(';'))) {

            for (CSVRecord csvRecord : csvParser) {
                try {
                    String name = null;

                    Double longitude = Double.valueOf(csvRecord.get(2).replace(',', '.'));
                    Double latitude  = Double.valueOf(csvRecord.get(3).replace(',', '.'));
                    Double distance  = Double.valueOf(csvRecord.get(4).replace(',', '.'));
                    Float magnitude  = Float.valueOf(csvRecord.get(8).replace(',', '.'));

                    Coordinate coordinate = correctCoordinateFromSolToBlackHole(
                            new Coordinate(distance, longitude, latitude)
                    );

                    String spectrum = csvRecord.get(6);
                    Star result = null;


                    if (spectrum.contains(":") || spectrum.contains("+") || spectrum.contains("/")) {


                        if (distance <= Settings.instance().getGalaxySize()) {
                            String designation = Star.randomDesignation(StarType.W, coordinate);
                            result = new Star(designation, name, StarType.W, 0, LuminosityClass.O, magnitude, coordinate);
                        }

 
                        String[] elements = spectrum.split(":");
                        if (elements.length < 2) {
                            elements = spectrum.split("/");
                            if (elements.length < 2) {
                                elements = spectrum.split("\\+");
                            }
                        }

                        if (elements.length >= 2) {
                            StarType starType = StarType.parse(elements[1]);
                            if (starType != null && distance <= Settings.instance().getGalaxySize()) {

                                int temperatureSequence;
                                if (elements[1].length() > 1 && Character.isDigit(elements[1].charAt(1))) {
                                    temperatureSequence = Integer.valueOf(elements[1].substring(1, 2));
                                } else {
                                    temperatureSequence = 0;
                                }

                                LuminosityClass luminosityClass = LuminosityClass.parse(elements[1]);

                                String designation = Star.randomDesignation(starType, coordinate);
                                Star twin = new Star(designation, null, starType, temperatureSequence, luminosityClass, magnitude, coordinate);


                                if (result != null && twin != null) {
                                    Coordinate sc = result.getCoordinate();

                                    double variation =
                                            ((Settings.instance().getRandom().nextFloat() * 30f) + 15f) *
                                            (float) Math.pow(((result.getType().getMinMass() + result.getType().getMaxMass()) / 2f), (1d / 3d));

                                    double newDistance = sc.getDistance() * 206264d + variation;
                                    Coordinate newCoordinate = new Coordinate((newDistance / 206264f), sc.getLongitude(), sc.getLatitude());

                                    twin.setCoordinate(newCoordinate);

                                    StringBuilder sisterDesignation = new StringBuilder(result.getDesignation().substring(0, 5));
                                    sisterDesignation.append(Long.valueOf(result.getDesignation().substring(5)) + 1);

                                    twin.setDesignation(new String(sisterDesignation));

                                    result.setSister(twin);
                                    twin.setSister(result);

                                    stars.add(twin);
                                    stars.add(result);
                                } else if (twin != null) {
                                    stars.add(twin);
                                }
                            } else if (result != null) {
                                stars.add(result);
                            }
                        } else if (result != null) {
                            stars.add(result);
                        }
                    }


                    else {
                        StarType starType = StarType.parse(spectrum);
                        if (starType == null) continue;

                        int temperatureSequence;
                        if (spectrum.length() > 2 && spectrum.substring(1, 2).equals(" ")) {
                            temperatureSequence = Integer.valueOf(spectrum.substring(2, 3));
                        } else {
                            temperatureSequence = 0;
                        }

                        String luminosity = csvRecord.get(7);
                        LuminosityClass luminosityClass =
                                (luminosity == null || luminosity.equals(""))
                                        ? LuminosityClass.I
                                        : LuminosityClass.parse(luminosity);

                        if (distance <= Settings.instance().getGalaxySize()) {
                            String designation = Star.randomDesignation(starType, coordinate);
                            Star s = new Star(designation, null, starType, temperatureSequence, luminosityClass, magnitude, coordinate);
                            stars.add(s);
                        }
                    }

                } catch (NumberFormatException e) {

                } catch (Exception e) {

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stars;
    }


    public static Set<Star> readCustomStars(Set<Star> initStars) throws IOException {
        Set<Star> stars = new HashSet<>();

        File customStars = Settings.instance().getCustomStarsFile();
        if (customStars == null || !customStars.exists()) {

            stars.addAll(readBaseStars());
            return stars;
        }

        try (Reader reader = Files.newBufferedReader(customStars.toPath(), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.newFormat(';'))) {

            for (CSVRecord csvRecord : csvParser) {
                try {
                    if (csvRecord.size() < 7) continue;

                    String name = csvRecord.get(0);
                    StarType type = StarType.parse(csvRecord.get(1));
                    if (type == null) continue;

                    int temperatureSequence = Integer.parseInt(csvRecord.get(2).trim());
                    LuminosityClass luminosity = LuminosityClass.parse(csvRecord.get(3));

                    Double distance  = Double.valueOf(csvRecord.get(6).replace(',', '.'));
                    Double longitude = Double.valueOf(csvRecord.get(4).replace(',', '.'));


                    Double latitude = (Double.valueOf(csvRecord.get(5).replace(',', '.'))) * 360d / 24d;

                    Coordinate coordinate = correctCoordinateFromSolToBlackHole(
                            new Coordinate(distance, longitude, latitude)
                    );

                    float magnitude = 0;


                    Star target = null;
                    double minDist = Double.MAX_VALUE;

                    for (Star s : initStars) {
                        if (s.getType() == type) {
                            double d = s.getCoordinate().calculateDistance(coordinate);
                            if (d < minDist) {
                                minDist = d;
                                target = s;
                            }
                        }
                    }

                    if (target != null) {
                        magnitude = target.getAbsoluteMagnitude();
                    }

                    String designation = Star.randomDesignation(type, coordinate);
                    Star result = new Star(designation, name, type, temperatureSequence, luminosity, magnitude, coordinate);
                    stars.add(result);

                } catch (NumberFormatException e) {

                } catch (Exception e) {

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        stars.addAll(readBaseStars());
        return stars;
    }



    private static char detectDelimiter(Path file) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                return line.contains(";") ? ';' : ',';
            }
        }
        return ','; 
    }


    private static Set<Star> readSemicolonStarsAsSimple(Path file) throws IOException {
        Set<Star> stars = new HashSet<>();

        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.newFormat(';'))) {

            for (CSVRecord csvRecord : csvParser) {
                try {
                    if (csvRecord.size() < 7) continue;

                    String name = csvRecord.get(0);
                    StarType type = StarType.parse(csvRecord.get(1));
                    if (type == null) continue;

                    int temperatureSequence = Integer.parseInt(csvRecord.get(2).trim());
                    LuminosityClass luminosity = LuminosityClass.parse(csvRecord.get(3));

                    Double longitude = Double.valueOf(csvRecord.get(4).replace(',', '.'));
                    Double latitudeDeg = (Double.valueOf(csvRecord.get(5).replace(',', '.'))) * 360d / 24d;
                    Double distance = Double.valueOf(csvRecord.get(6).replace(',', '.'));

                    Coordinate coordinate = correctCoordinateFromSolToBlackHole(
                            new Coordinate(distance, longitude, latitudeDeg)
                    );

                    float magnitude = 0f; 
                    String designation = Star.randomDesignation(type, coordinate);

                    Star s = new Star(designation, name, type, temperatureSequence, luminosity, magnitude, coordinate);
                    stars.add(s);

                } catch (Exception ignored) {

                }
            }
        }


        return stars;
    }


    private static Coordinate correctCoordinateFromSolToBlackHole(Coordinate coordinate) {
        double distance = coordinate.getDistance();
        double longitude = coordinate.getLongitude();
        double latitude = coordinate.getLatitude();

        latitude = latitude * (1d - ((11000 - distance) / 11000));

        if ((longitude > 90) && (longitude < 270)) {
            longitude = longitude + (Math.asin(Math.sin((180 - longitude) * Math.PI / 180) * ((distance - 11000) / 11000))) * 180 / Math.PI;
        } else {
            if (distance != 11000) {
                longitude = (Math.asin((Math.sin(longitude * Math.PI / 180) / (distance - 11000)))) * 180 / Math.PI;
            }
        }

        distance = Math.abs(11000 - distance);
        return new Coordinate(distance, longitude, latitude);
    }


    public static Set<Star> readBaseStars() throws IOException {
        Set<Star> result = new HashSet<>();

        Star newStar = new Star("SAGI-1000000000000000001", "Sagittarius A*", StarType.H, 5, LuminosityClass.VIII, 1000.0f, 0d, 0d, 0d);
        result.add(newStar);

        newStar = new Star("SAGI-1000000000000000002", "S1", StarType.B, 0, LuminosityClass.V, -1.1f, 0.0006d, 246.62607015d, 0d);
        result.add(newStar);

        newStar = new Star("SAGI-1000000000000000003", "S2", StarType.B, 0, LuminosityClass.V, -1.1f, 0.0006d, 3.14159265d, 0d);
        result.add(newStar);

        newStar = new Star("SAGI-1000000000000000004", "S3", StarType.B, 0, LuminosityClass.V, -1.1f, 0.0006d, 122.71828182, 0d);
        result.add(newStar);

        newStar = new Star("SOLA-6592602058205295101", "Sol", StarType.G, 5, LuminosityClass.I, 5.0f, 10981.917d, 0.314159265d, 0.000712d);
        result.add(newStar);

        return result;
    }
}

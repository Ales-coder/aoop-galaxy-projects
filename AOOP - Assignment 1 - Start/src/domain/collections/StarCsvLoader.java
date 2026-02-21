package domain.collections;

import domain.Star;
import domain.types.LuminosityClass;
import domain.types.StarType;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

final class StarCsvLoader {

    private StarCsvLoader() {}

    static List<Star> load(String csvPath) {
        return load(csvPath, -1);
    }

    static List<Star> load(String csvPath, int limit) {

        List<Star> stars = new ArrayList<>();

        try {
            char delimiter = detectDelimiter(csvPath);

            try (Reader reader = new FileReader(csvPath);
                 CSVParser parser = new CSVParser(reader,
                         CSVFormat.DEFAULT
                                 .withFirstRecordAsHeader()
                                 .withTrim()
                                 .withDelimiter(delimiter))) {

                int n = 0;

                for (CSVRecord rec : parser) {

                    Star s = map(rec);
                    if (s != null) stars.add(s);

                    n++;


                    if (limit > 0 && n % 5000 == 0) {
                        System.out.println("Loaded sample records: " + n + "/" + limit);
                    }

                    if (limit > 0 && n >= limit) break;
                }

                if (limit > 0) {
                    System.out.println("Finished loading sample: " + n + " records.");
                }
            }

        } catch (Exception ex) {
            throw new RuntimeException("Cannot load stars from CSV: " + csvPath, ex);
        }

        return stars;
    }

    private static char detectDelimiter(String csvPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String headerLine = br.readLine();
            if (headerLine == null) return ',';


            int commas = count(headerLine, ',');
            int semis  = count(headerLine, ';');
            int tabs   = count(headerLine, '\t');


            if (tabs > commas && tabs > semis) return '\t';
            if (semis > commas) return ';';
            return ',';
        } catch (Exception e) {
            return ',';
        }
    }

    private static int count(String s, char c) {
        int k = 0;
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) == c) k++;
        return k;
    }

    private static Star map(CSVRecord rec) {
        try {

            String designation = rec.get(0).trim();

            String name = rec.get(1);
            if (name != null) {
                name = name.trim();
                if (name.isBlank()) name = null;
            }

            StarType type = StarType.parse(rec.get(2));
            int temperatureSequence = Integer.parseInt(rec.get(3).trim());
            LuminosityClass lum = LuminosityClass.parse(rec.get(4));

            float absoluteMagnitude = parseFloat(rec.get(5));
            float distance = parseFloat(rec.get(6));
            float longitude = parseFloat(rec.get(7));
            float latitude = parseFloat(rec.get(8));


            return new Star(
                    designation, name, type, temperatureSequence, lum,
                    absoluteMagnitude, distance, longitude, latitude
            );

        } catch (Exception e) {

            return null;
        }
    }

    private static float parseFloat(String s) {
        if (s == null) return 0f;
        s = s.trim();
        if (s.isEmpty()) return 0f;


        s = s.replace(',', '.');

        return Float.parseFloat(s);
    }
}
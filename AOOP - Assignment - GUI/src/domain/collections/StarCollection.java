package domain.collections;

import app.Settings;
import domain.galaxy.Star;
import io.CsvStarHandler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class StarCollection implements Set<Star> {

    private final Set<Star> stars;
    private static StarCollection internalObject = null;

    private StarCollection() throws IOException {
        this.stars = new HashSet<>();
        loadFromSettings();
    }

    public static StarCollection instance() throws IOException {
        if (internalObject == null) {
            internalObject = new StarCollection();
        }
        return internalObject;
    }

    public static void resetInstance() {
        internalObject = null;
    }


    public void loadFromSettings() throws IOException {
        stars.clear();

        File dataDir = Settings.instance().getDataDirectory();

        String mainCsvName = Settings.instance().getStarCSVFile();
        File mainCsv = new File(dataDir, mainCsvName);


        if (!mainCsv.exists()) {
            File f1 = new File(dataDir, "newdata.csv");
            File f2 = new File(dataDir, "teststars.csv");
            File f3 = new File(dataDir, "00-allstars.csv");
            File f4 = new File(dataDir, "allstars.csv");

            if (f1.exists()) mainCsv = f1;
            else if (f2.exists()) mainCsv = f2;
            else if (f3.exists()) mainCsv = f3;
            else if (f4.exists()) mainCsv = f4;
        }

        if (!mainCsv.exists()) {
            throw new IOException("Star CSV not found in: " + dataDir.getAbsolutePath()
                    + " (tried Settings StarCSV + newdata/teststars/00-allstars/allstars)");
        }


        System.out.println("Loading stars from: " + mainCsv.getAbsolutePath());
        Set<Star> mainStars = CsvStarHandler.readStarsfromCsv(mainCsv.getAbsolutePath());
        stars.addAll(mainStars);


        File customFile = Settings.instance().getCustomStarsFile();
        if (customFile != null && customFile.exists()) {
            System.out.println("Loading custom stars from: " + customFile.getAbsolutePath());
            stars.addAll(CsvStarHandler.readCustomStars(mainStars));
        }

        System.out.println("Total stars loaded: " + stars.size());
    }


    public int addFromCsv(File extraCsv) throws IOException {
        if (extraCsv == null) return 0;
        if (!extraCsv.exists()) throw new IOException("Selected file does not exist: " + extraCsv.getAbsolutePath());

        int before = stars.size();
        Set<Star> extraStars = CsvStarHandler.readStarsfromCsv(extraCsv.getAbsolutePath());
        stars.addAll(extraStars);

        int after = stars.size();

        System.out.println("Added stars from: " + extraCsv.getAbsolutePath());
        System.out.println("Total stars loaded: " + after);

        return after - before;
    }


    public void addAndPersist(Star star) throws IOException {
        if (star == null) return;


        if (this.stars.contains(star)) {
            return;
        }

        this.stars.add(star);

        File customFile = Settings.instance().getCustomStarsFile();
        if (customFile == null) {
            throw new IOException("Custom stars file is not configured in Settings.");
        }


        if (customFile.getParentFile() != null) {
            Files.createDirectories(customFile.getParentFile().toPath());
        }

        appendCustomStarSemicolon(customFile, star);
    }

    private void appendCustomStarSemicolon(File file, Star star) throws IOException {

        double latitudeHours = star.getCoordinate().getLatitude() * 24d / 360d;

        String line =
                safe(star.getName()) + ";" +
                        star.getType() + ";" +
                        star.getTemperatureSequence() + ";" +
                        star.getLuminosity() + ";" +
                        String.format(Locale.US, "%.6f", star.getCoordinate().getLongitude()) + ";" +
                        String.format(Locale.US, "%.6f", latitudeHours) + ";" +
                        String.format(Locale.US, "%.6f", star.getCoordinate().getDistance());

        Files.writeString(
                file.toPath(),
                line + System.lineSeparator(),
                StandardCharsets.UTF_8,
                CREATE, APPEND
        );
    }

    private String safe(String s) {
        return (s == null) ? "" : s.replace(";", " ");
    }


    @Override public int size() { return stars.size(); }
    @Override public boolean isEmpty() { return stars.isEmpty(); }
    @Override public boolean contains(Object o) { return stars.contains(o); }
    @Override public Iterator<Star> iterator() { return stars.iterator(); }
    @Override public Object[] toArray() { return stars.toArray(); }
    @Override public <T> T[] toArray(T[] a) { return stars.toArray(a); }
    @Override public boolean add(Star e) { return stars.add(e); }
    @Override public boolean remove(Object o) { return stars.remove(o); }
    @Override public boolean containsAll(Collection<?> c) { return stars.containsAll(c); }
    @Override public boolean addAll(Collection<? extends Star> c) { return stars.addAll(c); }
    @Override public boolean retainAll(Collection<?> c) { return stars.retainAll(c); }
    @Override public boolean removeAll(Collection<?> c) { return stars.removeAll(c); }
    @Override public void clear() { stars.clear(); }
}
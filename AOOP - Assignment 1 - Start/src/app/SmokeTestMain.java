package app;

import domain.Star;
import domain.collections.StarCollection;
import domain.types.StarType;

public class SmokeTestMain {


    private static final boolean USE_BIG_FILE = false;

    public static void main(String[] args) {

        String csvPath;

        if (USE_BIG_FILE) {
            csvPath = "C:\\Users\\User\\Desktop\\AOOP_Final_Project\\00-allstars.csv";
        } else {
            csvPath = "C:\\Users\\User\\Desktop\\AOOP_Final_Project\\AOOP - Assignment 1 - Start\\data\\teststars.csv";
        }

        System.out.println("Loading CSV: " + csvPath);

        StarCollection sc = new StarCollection(csvPath);

        System.out.println("Loaded stars: " + sc.size());


        System.out.println("Find ZHNU-1376454841: " + (sc.find("ZHNU-1376454841") != null));
        System.out.println("K stars: " + sc.getStarList(StarType.K).size());
        System.out.println("M stars: " + sc.getStarList(StarType.M).size());

        int doubles = 0;
        for (Star s : sc) {
            if (s.getSister() != null) doubles++;
        }
        System.out.println("Stars in double systems: " + doubles);

  
        int i = 0;
        for (Star s : sc) {
            System.out.println(s.getDesignation() + " " + s.getType()
                    + " sister=" + (s.getSister() != null ? s.getSister().getDesignation() : "no"));
            if (++i == 5) break;
        }
    }
}

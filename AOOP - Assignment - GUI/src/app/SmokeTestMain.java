package app;

import domain.collections.StarCollection;

public class SmokeTestMain {
    public static void main(String[] args) throws Exception {
        StarCollection stars = StarCollection.instance();
        System.out.println("Stars loaded: " + stars.size());
    }
}

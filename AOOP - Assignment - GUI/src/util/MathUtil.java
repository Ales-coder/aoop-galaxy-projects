package util;

import java.util.Collection;

import domain.galaxy.Body;

public class MathUtil {
    public static double calculateSD(Collection<Double> input) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = input.size();

        for(double num : input) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: input) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }
    
    public static double calculateDistance(double distance1, double longitude1, double latitude1, double distance2, double longitude2, double latitude2) {
    	double x1 = distance1 * Math.sin(longitude1) * Math.cos(latitude1);
    	double x2 = distance2 * Math.sin(longitude2) * Math.cos(latitude2);
    	
    	double y1 = distance1 * Math.sin(longitude1) * Math.sin(latitude1);
    	double y2 = distance2 * Math.sin(longitude2) * Math.sin(latitude2);
    	
    	double z1 = distance1 * Math.cos(longitude1);
    	double z2 = distance2 * Math.cos(longitude2);
    	
    	double result = (Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2) + Math.pow((z1-z2),2)));
    	
    	return result;
    }
    
    public static <T extends Body> double calculateDistance(T body1, T body2) {
    	return calculateDistance(body1.getCoordinate().getDistance(),
    							body1.getCoordinate().getLongitude(), 
    							body1.getCoordinate().getLatitude(), 
    							body2.getCoordinate().getDistance(),
    							body2.getCoordinate().getLongitude(), 
    							body2.getCoordinate().getLatitude());
    							
    }
}

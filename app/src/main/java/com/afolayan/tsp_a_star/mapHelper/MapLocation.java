package com.afolayan.tsp_a_star.mapHelper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Oluwaseyi AFOLAYAN on 6/6/2018.
 */
public class MapLocation {

    private int id;
    private double latitude, longitude;
    private String name;
    private static final double EARTH_RADIUS = 6371;

    public MapLocation(int id, String name, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "{id: " + id + ", name: " + name + ", latitude: " + latitude + ", longitude: " + longitude + "}";
    }

    public double distanceTo(MapLocation anotherLocation) {
        return getDistanceBetween(latitude, longitude,
                anotherLocation.getLatitude(), anotherLocation.getLongitude());
    }

    public static double getDistanceBetween(double lat1, double lon1, double lat2, double lon2) {
        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = EARTH_RADIUS * c * 1000; //multiply by 1000 to convert to meters

        return d;
    }

    public static double toRadians(double degrees) {
        return degrees * (Math.PI / 180);
    }


    public static MapLocation[] loadMapLocations() {
        return new MapLocation[]{
                new MapLocation(0, "Adeniran Ogunsanya College of Education", 6.5001372, 3.1086725),
                new MapLocation(1, "Lagos State University", 6.4639375, 3.1988882),
                new MapLocation(2, "Ronik Polytechnic", 6.5372312, 3.2929776),
                new MapLocation(3, "Christopher University", 6.8273478, 3.4622118),

                new MapLocation(4, "Anchor University", 6.6057617, 3.2392411),
                new MapLocation(5, "Caleb University", 6.6194184, 3.508265),
                new MapLocation(6, "Lagos State Polytechnic", 6.6462946, 3.5156813),
                new MapLocation(7, "Yaba College of Technology", 6.5187345, 3.3722376),

                new MapLocation(8, "University of Lagos", 6.5193116, 3.3971378),
                new MapLocation(9, "Federal College of Education, Akoka", 6.5225924, 3.3819078),
                new MapLocation(10, "St. Augustine College of Education", 6.5238066, 3.3850396),
                new MapLocation(11, "Grace Polytechnic", 6.5131549, 3.3581842),

                new MapLocation(12, "National Open University of Nigeria", 6.4341691, 3.4069982),
                new MapLocation(13, "Pan-Atlantic University", 6.4872606, 3.8531619),
                new MapLocation(14, "Augustine University", 6.6490121, 3.9912698)
        };
    }

    public static ArrayList<MapLocation> loadMapLocationsArray() {
        return new ArrayList<>(Arrays.asList(loadMapLocations()));
    }

    public static double[][] locationDistanceMatrix(ArrayList<MapLocation> mapLocations) {
        int numOfLocations = mapLocations.size();
        double[][] distanceCost = new double[numOfLocations][numOfLocations];
        for (MapLocation refLocation : mapLocations) {
            for (MapLocation anotherLocation : mapLocations) {

                double distanceTo = refLocation.distanceTo(anotherLocation);
                int refLocationId = refLocation.getId();
                int anotherLocationId = anotherLocation.getId();
                if (distanceTo < 0) {
                    distanceCost[refLocationId][anotherLocationId] = Double.MAX_VALUE;
                } else {
                    distanceCost[refLocationId][anotherLocationId] = distanceTo;
                }
            }
        }
        return distanceCost;
    }

    static double[][] locationDistanceMatrix(MapLocation[] mapLocations) {
        //MapLocation[] mapLocations = MapLocation.loadMapLocations();
        int numOfLocations = mapLocations.length;
        double[][] distanceCost = new double[numOfLocations][numOfLocations];
        for (int i = 0; i < numOfLocations; i++) {
            for (int j = 0; j < numOfLocations; j++) {
                MapLocation refLocation = mapLocations[i];
                MapLocation anotherLocation = mapLocations[j];

                double distanceTo = refLocation.distanceTo(anotherLocation);
                if (distanceTo < 0) {
                    distanceCost[i][j] = Double.MAX_VALUE;
                } else {
                    distanceCost[i][j] = distanceTo;
                }
            }
        }
        return distanceCost;
    }

    static String printPath(int[] path) {
        MapLocation[] locations = loadMapLocations();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            MapLocation location = locations[path[i]];

            builder.append(location).append("\n");
        }
        return builder.toString();
    }

    public static double[][] loadMatrix() {
        return new double[][]{
                {0.0, 10749.49, 20774.87, 53370.48, 18600.71, 46091.37, 47807.0, 29191.5, 31940.31, 30289.48, 30645.58, 27603.7, 33767.57, 82264.78, 98890.32},
                {10749.49, 0.0, 13208.96, 49787.2, 16388.1, 38301.12, 40445.62, 20097.9, 22752.02, 21246.15, 21616.88, 18430.71, 23231.37, 72334.26, 89919.14},
                {20774.87, 13208.96, 0.0, 37282.65, 9659.39, 25476.77, 27426.67, 8994.48, 11678.23, 9958.37, 10279.37, 7685.06, 17030.12, 62136.67, 78128.06},
                {53370.48, 49787.2, 37282.65, 0.0, 34833.7, 23673.41, 20980.19, 35725.98, 34997.93, 35028.6, 34811.7, 36777.27, 44142.76, 57397.12, 61695.89},
                {18600.71, 16388.1, 9659.39, 34833.7, 0.0, 29753.87, 30864.28, 17592.28, 19915.8, 18272.86, 18505.19, 16693.64, 26599.44, 69087.95, 83202.09},
                {46091.37, 38301.12, 25476.77, 23673.41, 29753.87, 0.0, 3098.73, 18738.42, 16571.02, 17627.93, 17271.84, 20358.58, 23440.71, 40835.96, 53449.44},
                {47807.0, 40445.62, 27426.67, 20980.19, 30864.28, 3098.73, 0.0, 21266.24, 19257.14, 20188.01, 19843.27, 22843.95, 26467.19, 41261.46, 52528.35},
                {29191.5, 20097.9, 8994.48, 35725.98, 17592.28, 18738.42, 21266.24, 0.0, 2751.62, 1151.23, 1522.61, 1671.95, 10157.3, 53247.41, 69896.89},
                {31940.31, 22752.02, 11678.23, 34997.93, 19915.8, 16571.02, 19257.14, 2751.62, 0.0, 1721.64, 1426.95, 4357.57, 9529.89, 50507.18, 67194.67},
                {30289.48, 21246.15, 9958.37, 35028.6, 18272.86, 17627.93, 20188.01, 1151.23, 1721.64, 0.0, 371.4, 2823.18, 10215.54, 52211.73, 68763.02},
                {30645.58, 21616.88, 10279.37, 34811.7, 18505.19, 17271.84, 19843.27, 1522.61, 1426.95, 371.4, 0.0, 3194.56, 10258.25, 51877.06, 68396.73},
                {27603.7, 18430.71, 7685.06, 36777.27, 16693.64, 20358.58, 22843.95, 1671.95, 4357.57, 2823.18, 3194.56, 0.0, 10306.56, 54760.93, 71545.08},
                {33767.57, 23231.37, 17030.12, 44142.76, 26599.44, 23440.71, 26467.19, 10157.3, 9529.89, 10215.54, 10258.25, 10306.56, 0.0, 49648.3, 68824.16},
                {82264.78, 72334.26, 62136.67, 57397.12, 69087.95, 40835.96, 41261.46, 53247.41, 50507.18, 52211.73, 51877.06, 54760.93, 49648.3, 0.0, 23584.8},
                {98890.32, 89919.14, 78128.06, 61695.89, 83202.09, 53449.44, 52528.35, 69896.89, 67194.67, 68763.02, 68396.73, 71545.08, 68824.16, 23584.8, 0.0}
        };
    }
}

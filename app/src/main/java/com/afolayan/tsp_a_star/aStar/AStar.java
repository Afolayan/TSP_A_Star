package com.afolayan.tsp_a_star.aStar;

import com.afolayan.tsp_a_star.mapHelper.MapLocation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AStar {
    private static ArrayList<MapLocation> mapLocations = new ArrayList<>();
    private static String bestSequence = "";

    public static void main(String[] args) {

        /*try {
            File file = new File("input.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {

                // Get the data from each line.
                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);
                String name = data[1];
                double latitude = Double.parseDouble(data[2]);
                double longitude = Double.parseDouble(data[3]);

                MapLocation location = new MapLocation(id, name, latitude, longitude);
                mapLocations.add(location);
            }
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        mapLocations = MapLocation.loadMapLocationsArray();
        double[][] distances = MapLocation.locationDistanceMatrix(mapLocations);

        /* Calculate all the possible solutions for the shortest route. */
        double bestDistance = 0.0;

        // For selecting starting point. Try everything as a starting point.
        for (MapLocation location : mapLocations) {
            ArrayList<MapLocation> toVisit = new ArrayList<>(mapLocations);
            AStarHelper.removeMapLocation(toVisit, location.getId()); //remove the starting locations

            /* Nearest neighbour algorithm */
            double totalDistance = 0.0;
            StringBuilder visitSequence = new StringBuilder(String.valueOf(location.getId() + 1)); //Save the visiting sequence as a string

            int currentTownId = location.getId();
            while (!toVisit.isEmpty()) { //till we have visited all the locations
                    int nearestTownId = AStarHelper.nearestLocation(toVisit, distances, currentTownId);
                    visitSequence.append(",").append(nearestTownId + 1); //add to sequence. plus i to get actual ID
                totalDistance += distances[currentTownId][nearestTownId]; // add the distance visited
                System.out.println(visitSequence);

                AStarHelper.removeMapLocation(toVisit, nearestTownId);
                currentTownId = nearestTownId; //we are now in that location
            }
            //if first time OR if current result is better
            if (bestDistance == 0.0 || totalDistance < bestDistance) {
                bestDistance = totalDistance;
                bestSequence = visitSequence.toString();
            }

        }
        printOutput();
        System.out.println("\n\nBest sequence with total distance of " + bestDistance + ":\n\n" + bestSequence);
    }

    private static void printOutput() {
        String[] sequenceSplit = bestSequence.split(",");
        int[] path = new int[sequenceSplit.length];
        for (int i = 0; i < sequenceSplit.length; i++) {
            String id = sequenceSplit[i];
            int index = Integer.parseInt(id) - 1;
            path[i] = index;
        }

        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("output.txt");
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(printPath(path));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();
                if (fileWriter != null)
                    fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    private static String printPath(int[] path) {
        StringBuilder builder = new StringBuilder();
        for (int aPath : path) {
            MapLocation location = mapLocations.get(aPath);
            builder.append(location).append("\n");
        }
        return builder.toString();
    }

}

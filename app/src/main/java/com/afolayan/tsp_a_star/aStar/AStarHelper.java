package com.afolayan.tsp_a_star.aStar;

import com.afolayan.tsp_a_star.mapHelper.MapLocation;

import java.util.ArrayList;
import java.util.Arrays;

public class AStarHelper {

    /* Method to remove a town from ArrayList<MapLocation>. */
    public static void removeMapLocation(ArrayList<MapLocation> towns, int locationId) {
        // Go through each item in the list till we find the town and remove.
        for (int i = 0; i < towns.size(); i++) {
            MapLocation current = towns.get(i);
            if (current.getId() == locationId) { // Found the town.
                towns.remove(i); // Remove from the same array that was passed.
                break;
            }

        }
    }


    /* Method to find the nearest town from a list of towns that are available to visit. */
    public static int nearestLocation(ArrayList<MapLocation> canVisit, double[][] distances, int locationId) {
        int nearestMapLocationID = locationId;
        double nearestDistance = 0.0;

        for (int i = 0; i < distances[locationId].length; i++) {
            if (distances[locationId][i] == 0.0) continue; // Skip comparing with same town.

            // Skip if town ID is not in the list of town that we can visit.
            if (!hasMapLocation(canVisit, i)) continue; // Cannot visit this town.

            // If no value for nearest distance, set this town to be the nearest. (Runs first time).
            if (nearestDistance == 0.0) {
                nearestMapLocationID = i;
                nearestDistance = distances[locationId][i];
                continue;
            }

            // Check if current town's distance is shorter.
            if (distances[locationId][i] < nearestDistance) {
                nearestMapLocationID = i;
                nearestDistance = distances[locationId][i];
            }
        }

        return nearestMapLocationID;
    }


    /* Method to check if a town is in ArrayList<MapLocation>. */
    public static boolean hasMapLocation(ArrayList<MapLocation> locations, int locationId) {
        // Go through each item in the list till we find the town.
        for (MapLocation location : locations) {
            if (location.getId() == locationId) { // Found the town.
                return true; // Return true.
            }
        }
        return false; // Assume that we don't have the town.
    }


    /* Method to print out 2D array of doubles. */
    public static void printDistances(double[][] array) {
        for (double[] arr : array) {
            System.out.println(Arrays.toString(arr));
        }
    }
}

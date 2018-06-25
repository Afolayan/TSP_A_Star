package com.afolayan.tsp_a_star;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.afolayan.tsp_a_star.aStar.AStarHelper;
import com.afolayan.tsp_a_star.mapHelper.MapLocation;
import com.cs.googlemaproute.DrawRoute;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, DrawRoute.onDrawRoute {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private static ArrayList<MapLocation> mapLocations = new ArrayList<>();
    private static String bestSequence = "";
    public static final String TAG = "MainActivity";
    private String googleMapsKey = "AIzaSyD4xotn_Tpkt9bryNg4bM4B23K6AZxHkZ4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mFusedLocationClient = getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });
    }

    private void doAStarSearch() {
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

                AStarHelper.removeMapLocation(toVisit, nearestTownId);
                if(toVisit.size() == 1){
                    /*just at the last item, add the distance to the starting point*/
                    totalDistance += distances[nearestTownId][location.getId()];
                }
                currentTownId = nearestTownId; //we are now in that location
            }
            //if first time OR if current result is better
            if (bestDistance == 0.0 || totalDistance < bestDistance) {
                bestDistance = totalDistance;
                bestSequence = visitSequence.toString();
            }

        }
        printOutput();
        Log.e(TAG, "\n\nBest sequence with total distance of " + bestDistance + ":\n\n" + bestSequence);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        doAStarSearch();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void printOutput() {
        String[] sequenceSplit = bestSequence.split(",");
        int length = sequenceSplit.length + 1;
        int[] path = new int[length];
        for (int i = 0; i < length - 1; i++) {
            String id = sequenceSplit[i];
            int index = Integer.parseInt(id) - 1;
            path[i] = index;
        }
        path[length-1] = Integer.parseInt(sequenceSplit[0])-1;
        printPath(path);

    }

    private void printPath(int[] path) {
        List<LatLng> tspPath = new ArrayList<>();
        mMap.clear();
        Log.e(TAG, "printPath: path-> "+ Arrays.toString(path));
        for (int i = 0; i < path.length; i++) {
            int aPath = path[i];
            MapLocation location = mapLocations.get(aPath);

            LatLng locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions;
            if (i == 0) {
                markerOptions = new MarkerOptions()
                        .position(locationLatLng)
                        .title(location.getName())
                        .snippet("Starting point");
                mMap.addMarker(markerOptions).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 10f));
            } else {
                markerOptions = new MarkerOptions()
                        .position(locationLatLng)
                        .title(location.getName()).snippet("Point: " + i);
                mMap.addMarker(markerOptions);
            }

            tspPath.add(locationLatLng);
        }
        //draw map points
        PolylineOptions pathOptions = new PolylineOptions().addAll(tspPath);
        mMap.addPolyline(pathOptions);
    }

    @Override
    public void afterDraw(String result) {

    }
}

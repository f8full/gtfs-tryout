package com.f8full;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String targetStop = "Grand Central";
        String targetDirectory = "gtfs\\";
        String stopFile = targetDirectory + "stops.txt";
        String stopTimeFile = targetDirectory + "stop_times.txt";
        String tripFile = targetDirectory + "trips.txt";
        String routeFile = targetDirectory + "routes.txt";

        BufferedReader br;
        String line;
        String separator = ",";

        long timeBeforeMillis = System.currentTimeMillis();

        try {
            br = new BufferedReader(new FileReader(stopFile));

            List<String> stopIdList = new ArrayList<>();

            //Let's find stops matching our criteria in stops.txt
            while ((line = br.readLine()) != null){
                if (line.contains(targetStop)){
                    String[] stop = line.split(separator);
                    //System.out.println("one Grand central stop -- id: " + stop[0] + " name: " + stop[2]);
                    stopIdList.add(stop[0]);
                }
            }

            //some stop matched
            if (!stopIdList.isEmpty()){

                //let's retrieve all unique trip id for those stops in stop_times.txt
                br = new BufferedReader(new FileReader(stopTimeFile));

                Set<String> uniqueTripIdSet = new HashSet<>();

                while ((line = br.readLine()) != null){
                    String[] stopTimeRow = line.split(separator);

                    //trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type,shape_dist_traveled
                    if (stopIdList.contains(stopTimeRow[3])){
                        uniqueTripIdSet.add(stopTimeRow[0]);
                    }
                }

                //from the trip id set, let's retrieve route id unique set in trips.txt
                br = new BufferedReader(new FileReader(tripFile));
                Set<String> uniqueRouteIdSet = new HashSet<>();

                while ((line = br.readLine()) != null){
                    String[] tripRow = line.split(separator);

                    //route_id,service_id,trip_id,trip_headsign,direction_id,block_id,shape_id
                    if (uniqueTripIdSet.contains(tripRow[2])){
                        uniqueRouteIdSet.add(tripRow[0]);
                    }
                }

                //finally, let's retrieve routes short names from routes.txt for display
                br = new BufferedReader(new FileReader(routeFile));

                while ((line = br.readLine()) != null){
                    String[] routeRow = line.split(separator);

                    //route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url,route_color,route_text_color
                    if (uniqueRouteIdSet.contains(routeRow[0])){
                        System.out.println("Route '" + routeRow[2] + "' passe par " + targetStop);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Took " + (System.currentTimeMillis() - timeBeforeMillis) + "ms");
    }
}

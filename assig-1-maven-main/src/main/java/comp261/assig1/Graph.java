package comp261.assig1;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.util.Locale; 

public class Graph {

    //Todo add your data structures here
    private ArrayList<Stop> stops; 
    public ArrayList<Trip> trips; 
    private ArrayList<Edge> edges; 

    public HashMap<String, Stop> stopMap;
    Trie trie = new Trie(); 

    
// constructor post parsing
    public Graph(ArrayList<Stop> stops, ArrayList<Trip> trips) {
        this.stops = stops;
        this.trips = trips; 
        edges = new ArrayList<Edge>(); 

        buildStopList();
        buildTripData();
    }

    // constructor with parsing
    public Graph(File stopFile, File tripFile) {
        //Todo: instantiate your data structures here
        stops = new ArrayList<Stop>(); 
        trips = new ArrayList<Trip>(); 
        edges = new ArrayList<Edge>(); 

        //Then you could parse them using the Parser
        stops = Parser.parseStops(stopFile);
        trips = Parser.parseTrips(tripFile);

        buildStopList();
        buildTripData();
    }

    // buildStoplist from other data structures
    private void buildStopList() {
       // Todo: you could use this sort of method to create additional data structures
       stopMap = new HashMap<String, Stop>();
       for(Stop s: stops){
           String stopId = s.getId(); 
           stopMap.put(stopId, s); 
       }


       for(Stop s: stops){
           trie.add(s); 
       }
    }

    // buildTripData into stops
    private void buildTripData(){
        // Add edges to the trips
        for(Trip t: trips){
            for(int i = 1; i < t.stops.size(); i++){
                Stop toStop = stopMap.get(t.getStops().get(i));
                Stop fromStop = stopMap.get(t.getStops().get(i-1));

                Edge e = new Edge(fromStop, toStop, t.getStopPatternId());
                t.addEdge(e); 
            }
        } 

        for(Trip t: trips){
            for(String stopId: t.stops){
                stopMap.get(stopId).addTrip(t); 
            }
        }

    }

    


    // Todo: getters and setters
    public ArrayList<Stop> getStops(){
        return stops; 
    }

    public ArrayList<Trip> getTrip(){
        return trips; 
    }




 

}

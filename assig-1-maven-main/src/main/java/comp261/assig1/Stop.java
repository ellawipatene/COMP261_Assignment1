package comp261.assig1;

import java.util.ArrayList;


// decide the data structure for stops
public class Stop {
    //probably always have these three    
    private GisPoint loc;
    private String name;
    private String id;
    public String stopDesc; 

    //Todo: add additional data structures
    public ArrayList<Trip> trips; 


        
    // Constructor
    public Stop(GisPoint loc, String name, String id, String stopDesc) {
        this.loc = loc;
        this.name = name;
        this.id = id;
        this.stopDesc = stopDesc; 
        this.trips = new ArrayList<Trip>(); 
    }

    // add getters and setters etc
    public GisPoint getLoc() {
        return loc;
    }

    public void setLoc(GisPoint loc) {
        this.loc = loc;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addTrip(Trip t){
        this.trips.add(t); 
    }

        
    }

package comp261.assig1;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import java.util.Random;

public class Trip {
    private String stopPatternId;
    private String stopId; 
    public Color color; 
    ArrayList<String> stops;
    ArrayList<Edge> edges; 
    private Random rand =  new Random();

    public Trip(String stopPatternId){
        this.stopPatternId = stopPatternId; 

        this.stops = new ArrayList<String>(); 
        this.edges = new ArrayList<Edge>(); 
        this.color = Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    public Trip(String stopPatternId, String stopId){
        this.stopPatternId = stopPatternId; 

        this.stops = new ArrayList<String>(); 
        this.edges = new ArrayList<Edge>(); 
        stops.add(stopId); 
    }

    // Add a new stop to the trip
    public void addStop(String stopId){
        this.stops.add(stopId); 
    }

    public ArrayList<String> getStops(){
        return stops; 
    }

    public void addEdge(Edge edge){
        this.edges.add(edge); 
    }

    public ArrayList<Edge> getEdges(){
        return edges; 
    }

    public String getStopPatternId(){
        return stopPatternId; 
    }


}

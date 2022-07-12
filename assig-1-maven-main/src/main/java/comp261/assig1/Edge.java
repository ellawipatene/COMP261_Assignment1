package comp261.assig1;

// The edge class represents an edge in the graph.
// This is directional, make sure that fromStop and toStop are in the right order.

public class Edge {
    private Stop fromStop; 
    private Stop toStop; 
    private String tripId;

    //todo: add a constructor
    public Edge(Stop fromStop, Stop toStop, String tripId){
        this.fromStop = fromStop; 
        this.toStop = toStop; 
        this.tripId = tripId; 
    }


    //todo: add getters and setters
    public Stop getFrom(){
        return fromStop;
    }

    public Stop getTo(){
        return toStop;
    }

    public String getTripId(){
        return tripId; 
    }

  

}

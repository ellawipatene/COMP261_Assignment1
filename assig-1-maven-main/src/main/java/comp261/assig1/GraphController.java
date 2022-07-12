package comp261.assig1;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;  
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Locale; 

import javafx.event.*;

public class GraphController {

    // names from the items defined in the FXML file
    @FXML private TextField searchText; 
    @FXML private Button load;
    @FXML private Button quit;
    @FXML private Button up;
    @FXML private Button down;
    @FXML private Button left;
    @FXML private Button right;
    @FXML private Canvas mapCanvas;
    @FXML private Label nodeDisplay;
    @FXML private TextArea tripText;

    // These are use to map the nodes to the location on screen
    private Double scale = 5000.0; //5000 gives 1 pixel ~ 2 meter
    private static final double ratioLatLon = 0.73; // in Wellington ratio of latitude to longitude
    private GisPoint mapOrigin = new GisPoint(174.77, -41.3); // Lon Lat for Wellington
 
    private static int stopSize = 5; // drawing size of stops
    private static int moveDistance = 100; // 100 pixels
    private static double zoomFactor = 1.1; // zoom in/out factor

    private boolean enterPressed = false; // if enter has been pressed for the search functions

    private ArrayList<Stop> highlightNodes = new ArrayList<Stop>();
    private Random rand = new Random(); // for generating random colours
    
    // for draging/moving the graoh around
    public double dragStartX = 0;
    public double dragStartY = 0; 

    // map model to screen using scale and origin
    private Point2D model2Screen (GisPoint modelPoint) {
        return new Point2D(model2ScreenX(modelPoint.lon), model2ScreenY(modelPoint.lat));
    }
    
    private double model2ScreenX (double modelLon) {
      return (modelLon - mapOrigin.lon) * (scale*ratioLatLon) + mapCanvas.getWidth()/2; 
    }
    // the getHeight at the start is to flip the Y axis for drawing as JavaFX draws from the top left with Y down.
    private double model2ScreenY (double modelLat) {
      return mapCanvas.getHeight()-((modelLat - mapOrigin.lat)* scale + mapCanvas.getHeight()/2);
    }

    // map screen to model using scale and origin
    private double getScreen2ModelX(Point2D screenPoint) {
        return (((screenPoint.getX()-mapCanvas.getWidth()/2)/(scale*ratioLatLon)) + mapOrigin.lon);
    }
    private double getScreen2ModelY(Point2D screenPoint) {
        return ((((mapCanvas.getHeight()-screenPoint.getY())-mapCanvas.getHeight()/2)/scale) + mapOrigin.lat);
    }
    
    private GisPoint getScreen2Model(Point2D screenPoint) {
        return new GisPoint(getScreen2ModelX(screenPoint), getScreen2ModelY(screenPoint));
    }   

    

    public void initialize() {
       // currently blank
    }

    /* handle the load button being pressed connected using FXML*/
    public void handleLoad(ActionEvent event) {
        Stage stage = (Stage) mapCanvas.getScene().getWindow();
        System.out.println("Handling event " + event.getEventType());
        FileChooser fileChooser = new FileChooser();
        //Set to user directory or go to default if cannot access

        
        File defaultNodePath = new File("data");
        if(!defaultNodePath.canRead()) {
            defaultNodePath = new File("C:/");
        }
        fileChooser.setInitialDirectory(defaultNodePath);
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extentionFilter);

        fileChooser.setTitle("Open Stop File");
        File stopFile = fileChooser.showOpenDialog(stage);

        fileChooser.setTitle("Open Stop Pattern File");
        File tripFile = fileChooser.showOpenDialog(stage);

        Main.graph=new Graph(stopFile,tripFile);
        drawGraph();
        event.consume(); // this prevents other handlers from being called
    }

    public void handleQuit(ActionEvent event) {
        System.out.println("Quitting with event " + event.getEventType()); 
        event.consume();  
        System.exit(0); 
    }

    /**
     * Increase the scale to zoom in
     * @param event
     */
    public void handleZoomin(ActionEvent event) {
        System.out.println("Zoom in event " + event.getEventType()); 

        scale *= zoomFactor;
        drawGraph();

        event.consume();  
    }

    /**
     * Decrese the scale size to zoom out
     * @param event
     */
    public void handleZoomout(ActionEvent event) {
        System.out.println("Zoom out event " + event.getEventType()); 

        scale *= 1.0/zoomFactor;
        drawGraph();

        event.consume();  
    }

    /**
     * Move the origin to the up when the up arrow is clicked
     * @param event
     */
    public void handleUp(ActionEvent event) {
        System.out.println("Move up event " + event.getEventType()); 
        GisPoint gp = new GisPoint(0, moveDistance/scale);
        mapOrigin.add(gp);
        drawGraph();
        event.consume();  
    }

    /**
     * Move the origin to the down when the down arrow is clicked
     * @param event
     */
    public void handleDown(ActionEvent event) {
        System.out.println("Move Down event " + event.getEventType()); 
        GisPoint gp = new GisPoint(0, moveDistance/scale);
        mapOrigin.subtract(gp);
        drawGraph();
        event.consume();  
    }

    /**
     * Move the origin to the left when left arrow is clicked
     * @param event
     */
    public void handleLeft(ActionEvent event) {
        System.out.println("Move Left event " + event.getEventType()); 
        GisPoint gp = new GisPoint(moveDistance/scale, 0);
        mapOrigin.subtract(gp);
        drawGraph();
        event.consume();  
    }

    /**
     * Move the origin to the right when right arrow is clicked
     * @param event
     */
    public void handleRight(ActionEvent event) {
        System.out.println("Move Right event " + event.getEventType()); 
        GisPoint gp = new GisPoint(moveDistance/scale, 0);
        mapOrigin.add(gp);
        drawGraph();
        event.consume();  
    }

    /**
     * Search up stops using the Trie structure 
     * Display trips when the user prints enter 
     * @param event
     */
    public void handleSearch(ActionEvent event) {
        System.out.println("Look up event " + event.getEventType()+ "  "+searchText.getText()); 
        enterPressed = true; 
        String search = searchText.getText();
        ArrayList<Stop> searchedStops = Main.graph.trie.getAll(search); 
        highlightNodes = searchedStops;
        if(highlightNodes != null) {
            drawGraph(); 
        }
        event.consume();  
    }  
    /**
     * When searching for a stop, the prefixed stops 
     * will be highlighted on the map. 
     * @param event
     */
    public void handleSearchKey(KeyEvent event) {
        if(!enterPressed){ //Only do this function if handleSearch hasn't also run
            System.out.println("Look up event " + event.getEventType()+ "  "+searchText.getText()); 
            System.out.println(event.getText()); 
            String search = searchText.getText();
            if(highlightNodes != null){
                highlightNodes.clear();
                highlightNodes = Main.graph.trie.getAll(search); 
                
                // Add the stops and the trip descriptions to the string variable
                ArrayList<Trip> tempTrips = new ArrayList<Trip>(); 
                String tripDescription = "Stops: ";
                for(Stop s: highlightNodes){
                    tripDescription = tripDescription + s.getName() + ", "; 
                    for(Trip t: s.trips){
                        if(!tempTrips.contains(t)){ // Ensure trip descriptions arn't being added twice
                            tempTrips.add(t); 
                        }
                    }      
                }
                tripDescription = tripDescription +  "\n Trips: "; 
                for(Trip t: tempTrips){
                    tripDescription = tripDescription + t.getStopPatternId() + ", "; 
                }

                tripText.setText(tripDescription);

                drawGraph(); 
                event.consume(); 
            }
            
        }else{
            enterPressed = false; // If handleSearch has already run, reset the boolean
        }
    }  

    
/* handle mouse clicks on the canvas
   select the node closest to the click */
    public void handleMouseClick(MouseEvent event) {
        System.out.println("Mouse click event " + event.getEventType());
        Point2D screenPoint = new Point2D(event.getX(), event.getY());
        double x = getScreen2ModelX(screenPoint);
        double y = getScreen2ModelY(screenPoint);
        highlightClosestStop(x,y);
       
        event.consume();
    }

    /**
     * Zoom in and out of the graph using the mouse scroller
     * @param event
     */
    public void mouseScroll(ScrollEvent event){
        System.out.println("Mouse scroll event " + event.getEventType());
        if(scale > 0){
            if(scale + event.getDeltaY() * 10.0 < 0){scale = 1.0;}
            else{scale += event.getDeltaY() * 10.0; }
        }
        tripText.setText("Scale  " + scale);
        drawGraph();
        event.consume(); 
    }

    /**
     * Record the current x and y cords of the initial mouse click
     * @param event
     */
    public void handleMousePressed(MouseEvent event){
        System.out.println("Mouse pressed event " + event.getEventType());
        dragStartX = event.getX();
        dragStartY = event.getY(); 
        event.consume(); 
    }

    /**
     * Change the origin of the map once the mouse has been released 
     * @param event
     */
    public void handleMouseDrag(MouseEvent event){
        System.out.println("Mouse drag event " + event.getEventType());
        double dx = dragStartX - event.getX(); 
        double dy = event.getY() - dragStartY; 
        dragStartX = event.getX(); 
        dragStartY = event.getY(); 
        mapOrigin.lon += dx/(scale * ratioLatLon);
        mapOrigin.lat += dy/scale;  
        drawGraph();
        event.consume();
 
    }

    /**
     * Find the Closest stop to the lon,lat postion entered
     * @param lon
     * @param lat
     */
    public void highlightClosestStop(double lon, double lat) {
        double minDist = Double.MAX_VALUE;
        Stop closestStop = null;
        // Find the closest stop
        for(Stop stop: Main.graph.getStops()){
            double dist = stop.getLoc().distance(lon, lat); 
            if(dist < minDist){
                minDist = dist;
                closestStop = stop;
            }
        }

        // Set the highlighted stops
        if(closestStop != null){
            if(highlightNodes != null){ highlightNodes.clear();}

            highlightNodes.add(closestStop); 
            nodeDisplay.setText(closestStop.toString());

            // Add the description text
            String stopDescription = closestStop.getName() + " " + closestStop.stopDesc + '\n' + "Trips: ";
            for(Trip t: closestStop.trips){
                stopDescription = stopDescription + t.getStopPatternId() + ", "; 
            }

            nodeDisplay.setText(closestStop.getName());
            tripText.setText(stopDescription);
            
            drawGraph();
        }
    }

    /**
     * Switch the language on the buttons to English (NOT FINISHED)
     * @param event
     */
    public void handleEnglish(ActionEvent event){
        System.out.println("English event " + event.getEventType());
        Locale.setDefault(new Locale("es", "NZ"));
        
        drawGraph();
        event.consume();
    }

    /** 
     * Switch the language on the buttons to Māori (NOT FINISHED)
     * @param event
     */
    public void handleMaori(ActionEvent event){
        System.out.println("Māori event " + event.getEventType());
        Locale.setDefault(new Locale("mi", "NZ"));
        drawGraph();
        event.consume();
    }



/*
Drawing the graph on the canvas
*/
    public void drawCircle(double x, double y, int radius) {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.fillOval(x-radius/2, y-radius/2, radius, radius);
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        mapCanvas.getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
    }
    

    // This will draw the graph in the graphics context of the mapcanvas
    public void drawGraph() {
        Graph graph = Main.graph;
        if(graph == null) {
            return;
        }
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        ArrayList<Stop> stopList = graph.getStops();
        
        // Draw all of the stops on the canvas
        stopList.forEach(stop ->{
            int size = stopSize; 
            if (highlightNodes!= null && highlightNodes.contains(stop)) { // if it is a 'highlighted stop' draw it green & larger
                gc.setFill(Color.GREEN);
                size = size * 2;
            } else {
                gc.setFill(Color.BLUE);
            }

            Point2D screenPoint = model2Screen(stop.getLoc());
            drawCircle(screenPoint.getX(), screenPoint.getY(), size);
        });
        
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);   

        //draw edges onto canvas
        for(Trip t: graph.trips){
            for(Edge e: t.getEdges()){
                Stop fromStop = e.getFrom(); 
                Stop toStop = e.getTo(); 

                Point2D startPoint = model2Screen(fromStop.getLoc());
                Point2D endPoint = model2Screen(toStop.getLoc());
                drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());

            }
        }   
        
        // draw the highlighed trips
        if(highlightNodes != null) {
            for(Stop stop: highlightNodes){
                for(Trip trip: stop.trips){
                    drawTrip(trip, gc, trip.color);
                }
             }
        }
        
    }

    /** Highlight the selected trips */
    private void drawTrip(Trip trip, GraphicsContext gc, Color color) {
        gc.setStroke(color);
        gc.setLineWidth(2);

        for(Edge e: trip.getEdges()){
            Stop fromStop = e.getFrom();
            Stop toStop = e.getTo();

            Point2D startPoint = model2Screen(fromStop.getLoc());
            Point2D endPoint = model2Screen(toStop.getLoc());
            drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
        }
                

}
}


    

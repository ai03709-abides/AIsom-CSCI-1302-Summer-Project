package sum26projects;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.scene.control.*;

public class SummerProject extends Application{
	
	@Override
	public void start(Stage primaryStage) {
		MeasurementPage measurementPage = new MeasurementPage();
		primaryStage.setScene(measurementPage.measurementScene);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}


class MeasurementPage{
	
	protected Scene measurementScene;
	protected VBox pointList;
	protected Text point1Txt= new Text("Point 1: ");
	protected Text point2Txt = new Text("Point 2: ");
	protected Text distanceDisplay = new Text("Distance: ");
	protected TextField point1Input = new TextField();
	protected TextField point2Input = new TextField();
	protected Button submit = new Button("Calculate");
	protected Point point1;
	protected Point point2;
	protected Connection line;
	
	MeasurementPage(){
		pointList = new VBox();
		point1Txt.setFont(new Font(24));
		point2Txt.setFont(new Font(24));
		point1Input.setFont(new Font (24));
		point2Input.setFont(new Font (24));
		distanceDisplay.setFont(new Font (24));
		point1Input.setPromptText("[input coords: lat, long]");
		point2Input.setPromptText("[input coords: lat, long]");
		submit.setPrefSize(150, 50);
		submit.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent arg0) {
				boolean point1Valid=false;
				boolean point2Valid=false;
				point2Txt.setText("Point 2: ");
				point1Txt.setText("Point 1: ");
				try {
					point1 = useInput(point1Input.getText());
					point1Txt.setText("Point 1: "+point1.getInfo());
					point1Valid=true;
				}
				catch (InvalidInputException e) {
					point1Txt.setText("Point 1: Please input valid coordinate");
				}
				try {
					point2 = useInput(point2Input.getText());
					point2Txt.setText("Point 2: "+point2.getInfo());
					point2Valid=true;
				}
				catch (InvalidInputException e) {
					point2Txt.setText("Point 2: Please input valid coordinate");
				}
				if(point1Valid && point2Valid) {
					line = new Connection (point1, point2);
					distanceDisplay.setText("Distance: "+line.getDistance()+" miles");
				}
			}
		});
		

		pointList.getChildren().add(point1Txt);
		pointList.getChildren().add(point1Input);
		pointList.getChildren().add(point2Txt);
		pointList.getChildren().add(point2Input);
		pointList.getChildren().add(submit);
		pointList.getChildren().add(distanceDisplay);
		measurementScene = new Scene(pointList, 500, 500);
		
		
	}
	
	
	public Scene getMeasurementScene() {
		return measurementScene;
	}
	
	public Point useInput(String input) {
		try {
			int commaIndex = input.indexOf(",");
			try {
				double latitude = Double.parseDouble(input.substring(0, commaIndex));
				double longitude = Double.parseDouble(input.substring(commaIndex+2));
				Point point = new Point(latitude, longitude);
				return point;
			}
			catch (StringIndexOutOfBoundsException e) {
				throw new InvalidInputException("IIE Thrown");
			}
		}
		catch (Exception e) {
			throw new InvalidInputException("IIE Thrown");
		}
		
	}
	
}

class InvalidInputException extends RuntimeException {

	public InvalidInputException(String message) {
    	super(message);
    }
}

class Point{
	private double latitude;
	private double longitude;
	
	Point(double latitude, double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getInfo() {
		return ("(Latitude: "+latitude+" Longitude: "+longitude+")");
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
}

class Connection{
	private double distance;
	private Point point1;
	private Point point2;
	
	Connection(Point point1, Point point2){
		this.point1=point1;
		this.point2=point2;
		distance = this.calculateDistance();
		
	}
	
	private double calculateDistance() {
		double latChange = (this.point1.getLatitude()-this.point2.getLatitude());
		double longChange = (this.point1.getLongitude()-this.point2.getLongitude());
		double term1 = Math.pow(Math.sin(Math.toRadians(latChange)/2), 2);
		double term2 = (Math.cos(this.point1.getLatitude())) * (Math.cos(this.point2.getLatitude())) * Math.pow(Math.sin(Math.toRadians(longChange)/2), 2);
		double radResult = Math.asin(Math.sqrt(term1+term2));
		return (Math.abs((2*3959)*radResult));
	}
	
	public double getDistance() {
		return distance;
	}
}
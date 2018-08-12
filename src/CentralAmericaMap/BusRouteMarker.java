package CentralAmericaMap;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import processing.core.PApplet;
import processing.core.PGraphics;

public class BusRouteMarker extends SimpleLinesMarker implements MarkerTypes {
	
	public MarkerType type = MarkerType.BUS_ROUTE;
	
	public UnfoldingMap map;
	
	private Location start, end;
	
	private int titleSize;
	
	private boolean showRoute = false;
	private boolean showTitle = false;
	private boolean showInfo = false;
	
	public void setShowRoute(boolean b){
		if (b)
			showRoute = true;
		else
			showRoute = false;
	}
	
	public boolean getShowRoute(){
		return showRoute;
	}
	
	public void setShowTitle(boolean b){
		if (b)
			showTitle = true;
		else
			showTitle = false;
	}
	
	public boolean getShowTitle(){
		return showTitle;
	}
	
	public void setShowInfo(boolean b){
		if (b)
			showInfo = true;
		else
			showInfo = false;
	}
	
	public boolean getShowInfo(){
		return showInfo;
	}
	
	public BusRouteMarker(Feature busRoute, UnfoldingMap m) {
		super(((ShapeFeature) busRoute).getLocations(), busRoute.getProperties());
		map = m;
		titleSize = (busRoute.getStringProperty("name")).length();
		start = this.getLocation(0);
		end = this.getLocation(1);
	}

	public Location getStart(){
		return start;
	}
	
	public Location getEnd(){
		return end;
	}
	
	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions){
		float x = ((mapPositions.get(0)).x + (mapPositions.get(1)).x)/2;
		float y = ((mapPositions.get(0)).y + (mapPositions.get(1)).y)/2;
		if (showRoute){
			pg.pushStyle();
			pg.strokeWeight((3f/8f)*map.getZoomLevel());
			pg.stroke(186, 180, 180);
			pg.line((mapPositions.get(0)).x, (mapPositions.get(0)).y, (mapPositions.get(1)).x, (mapPositions.get(1)).y);
			pg.popStyle();
		}
		
		if (showTitle)
			showTitle(pg, x, y, map.getZoomLevel());
	}
	
	private void showTitle(PGraphics pg, float x, float y, int zoomed){
		pg.pushStyle();
		pg.fill(255, 255, 255);
		pg.rect(x+25, y, titleSize*zoomed*0.94f, (20/6)*zoomed);
		pg.textSize(1.66f*zoomed);
		pg.fill(0,0,0);
		pg.text(this.getStringProperty("name"), x+25, y+((14/6)*zoomed));
		pg.popStyle();
	}
	
	public void drawInfo(PApplet pa){
		pa.fill(255, 255, 255);
		pa.textSize(30);
		pa.text(this.getStringProperty("name"), 925, 230, 325, 125);
		
		pa.textSize(18);
		pa.text("Price: " + this.getStringProperty("cost") + this.getStringProperty("currency"), 950, 450);
		pa.text("Journey Duration: " + this.getStringProperty("duration"), 950, 527);
		pa.text("Bus Frequency: " + this.getStringProperty("frequency"), 950, 610);
	}
}

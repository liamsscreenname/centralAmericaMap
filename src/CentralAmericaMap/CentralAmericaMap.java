package CentralAmericaMap;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import processing.core.PApplet;

public class CentralAmericaMap extends PApplet {
	
	private UnfoldingMap map;
	
	private boolean showAllRoutes = false;
	
	AbstractMapProvider microsoftAerial = new Microsoft.AerialProvider();
	//AbstractMapProvider google = new Google.GoogleSimplifiedProvider();
	
	AbstractMapProvider provider = microsoftAerial;
	
	private String hostelFile = "hostels.json";
	private String busRouteFile = "bus-routes.json";
	private String airportFile = "airports.json";
	private String busRouteTerminalFile = "bus-terminals.json";

	private List<Marker> hostelMarkers;
	
	private List<Marker> airportMarkers;
	
	private List<Marker> busRouteMarkers;
	private List<Marker> busRouteTerminalMarkers;
	
	private List<Marker> activityMarkers;
	
	private List<List<Marker>> allMarkers;

	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	private int[] buttonX = new int[] {495, 530, 565, 600, 635, 670, 705, 740, 775, 810, 845};
	
	
	Location center = new Location(15.55f, -84.07f);
	
	public void setup(){
		size (1300, 700, OPENGL);
		
		map = new UnfoldingMap(this, 50, 50, 800, 600, microsoftAerial);
				
		MapUtils.createDefaultEventDispatcher(this, map);
		
		map.zoomAndPanTo(6, center);
		map.setZoomRange(6, 15);
		map.setPanningRestriction(center, 800);

		List<Feature> hostels = GeoJSONReader.loadData(this, hostelFile);
		hostelMarkers = new ArrayList<Marker>();
		for (Feature h : hostels){
			hostelMarkers.add(new HostelMarker(h, map, this));
		}
		
		List<Feature> busRoutes = GeoJSONReader.loadData(this, busRouteFile);
		busRouteMarkers = new ArrayList<Marker>();
		for (Feature f : busRoutes){
			SimpleLinesMarker m = new BusRouteMarker(f, map);
			busRouteMarkers.add(m);
		}
		
		List<Feature> busTerminals = GeoJSONReader.loadData(this, busRouteTerminalFile);
		busRouteTerminalMarkers = new ArrayList<Marker>();
		for (Feature m : busTerminals){
			busRouteTerminalMarkers.add(new BusRouteTerminalMarker(m, map));
		}
		//Add end of last bus route too
		
		List<Feature> airports = GeoJSONReader.loadData(this, airportFile);
		airportMarkers = new ArrayList<Marker>();
		for (Feature a : airports){
			airportMarkers.add(new AirportMarker(a, map));
		}
		
		allMarkers = new ArrayList<List<Marker>>();
		allMarkers.add(hostelMarkers);
		allMarkers.add(airportMarkers);
		allMarkers.add(busRouteMarkers);
		allMarkers.add(busRouteTerminalMarkers);
			
		for (List<Marker> l : allMarkers){
			map.addMarkers(l);
		}
	}
	
	public void draw(){
		background(0);
		map.draw();
		drawKey();
		drawButtons();
		hoverAction();
		clickAction();
	}
	
	public void hoverAction(){
		
	}
	
	public void clickAction(){
		if (!showAllRoutes){
			for (Marker m: busRouteMarkers){
				((BusRouteMarker)m).setShowRoute(false);
			}
		}
		if(lastClicked != null){
			switch (lastClicked.type){
				case HOSTEL:
					((HostelMarker) lastClicked).drawInfo(this);
					((HostelMarker) lastClicked).showPicture(this);
					break;
				case BUS_ROUTE:
					if(!showAllRoutes){
						for (Marker r : busRouteMarkers){
							Location locEnd = ((BusRouteMarker) r).getEnd();
							Location locStart = ((BusRouteMarker)r).getStart();
							Location loc = lastClicked.getLocation();
							if (loc.equals(locEnd) || loc.equals(locStart))
								((BusRouteMarker) r).setShowRoute(true);
							else
								((BusRouteMarker)r).setShowRoute(false);
						}
					}
					break;
				default:
					break;				
			}
		}
	}
	
	
	public void mouseMoved(){
		if (lastSelected != null){
			lastSelected.setSelected(false);
			lastSelected = null;
		}
			
		selectMarkerIfHover(hostelMarkers);
		selectMarkerIfHover(airportMarkers);
		selectMarkerIfHover(busRouteTerminalMarkers);
	}
	
	public void mouseClicked(){
		if (lastClicked != null){
			lastClicked.setClicked(false);
			lastClicked = null;
		}
		
		selectMarkerIfClick(hostelMarkers);
		selectMarkerIfClick(busRouteTerminalMarkers);
		
		if (mouseX > 50 && mouseX < 298 && mouseY > 15 && mouseY < 35){
			showAllRoutes ^= true;
			if (showAllRoutes){
				for (Marker m: busRouteMarkers)
					((BusRouteMarker)m).setShowRoute(true);			
			}
			else
				for (Marker m: busRouteMarkers)
					((BusRouteMarker)m).setShowRoute(false);
			}
		
		for (int i = 0; i < 11; i++){
			if (mouseX > buttonX[i] && mouseX < buttonX[i+1] && mouseY > 15 && mouseY < 35){
				map.zoomToLevel(i+6);
			}
		}
	}
	
	public void selectMarkerIfHover(List<Marker> markers){
		for (Marker m : markers){
			if (m.isInside(map, mouseX, mouseY)){
				if (lastSelected != null) {
					lastSelected.setSelected(false);
					lastSelected = null;			
				}
				m.setSelected(true);
				lastSelected = (CommonMarker) m;
				break;
			}
		}
	}
	
	public void selectMarkerIfClick(List<Marker> markers){
		for (Marker m : markers){
			if (m.isInside(map, mouseX, mouseY)){
				if (lastClicked != null) {
					lastClicked.setClicked(false);
					lastClicked = null;			
				}
				((CommonMarker) m).setClicked(true);
				lastClicked = (CommonMarker) m;
				break;
			}
		}
	}
	
	private void drawKey(){
		stroke(255);
		strokeWeight(5);
		line(900, 50, 1250, 50);
		line(1250, 50, 1250, 150);
		line(1250, 150, 900, 150);
		line(900, 150, 900, 50);

		line(900, 200, 1250, 200);
		line(1250, 200, 1250, 350);
		line(1250, 350, 900, 350);
		line(900, 350, 900, 200);
		
		line(900, 400, 1250, 400);
		line(1250, 400, 1250, 650);
		line(1250, 650, 900, 650);
		line(900, 650, 900, 400);
		
		strokeWeight(0);
		fill(0, 43, 255);
		rect(925, 65, 9, 9, 7);
		fill(243, 157, 82);
		rect(925, 95, 9, 9);	
		fill(255, 255, 255);
		ellipse(930, 130, 9, 9);
		
		textSize(17);
		text("Hostel (Click for info)", 965, 75);
		text("Airport", 965, 105);
		text("Bus Terminal (Click for routes)", 965, 135);
	}	
	
	private void drawButtons(){
		stroke(255);
		strokeWeight(2);
		fill(0);
		rect(50, 15, 248, 20);
		fill(255);
		textSize(19);
		text("Show/Hide all Bus Routes", 55, 31);
		
		fill(100);
		rect(buttonX[map.getZoomLevel()-6], 15, 35, 20);
		
		fill(255);
		text("Zoom Level:", 375, 31);
		fill(0);
		strokeWeight(1);
		rect(495, 15, 35, 20);
		rect(530, 15, 35, 20);
		rect(565, 15, 35, 20);
		rect(600, 15, 35, 20);
		rect(635, 15, 35, 20);
		rect(670, 15, 35, 20);
		rect(705, 15, 35, 20);
		rect(740, 15, 35, 20);
		rect(775, 15, 35, 20);
		rect(810, 15, 35, 20);
		fill(255);
		text ("1", 506, 31);
		text ("2", 541, 31);
		text ("3", 576, 31);
		text ("4", 611, 31);
		text ("5", 646, 31);
		text ("6", 681, 31);
		text ("7", 716, 31);
		text ("8", 751, 31);
		text ("9", 786, 31);
		text ("10", 814, 31);
	}
	
	public Marker findMarkerByName (String s){
		for (List<Marker> markList : allMarkers){
			for (Marker m : markList){
				if (m.getStringProperty("name").equals(s))
					return m;
			}
		}
		return null;
	}
}

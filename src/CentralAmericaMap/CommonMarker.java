package CentralAmericaMap;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

public abstract class CommonMarker extends SimplePointMarker implements MarkerTypes {
	
	
	
	public static int LOGO_SIZE = 2;
	public static int BOX_HEIGHT = 20;
	
	public UnfoldingMap map;
	
	public MarkerType type;
	
	private int chars;
	protected float titleSize;
	
	public float xCoord, yCoord;
	
	private boolean clicked = false;
	
	public void setClicked(boolean b){
		if (b)
			clicked = true;
		else
			clicked = false;
	}
	
	public boolean isClicked(){
		return clicked;
	}
	//PUT IN HOSTEL/ACTIVITY MARKER ONLY public PImage picture;
	
	public CommonMarker(Location location, java.util.HashMap<java.lang.String, java.lang.Object> properties, UnfoldingMap m){
		super(location, properties);
		map = m;
		chars = ((this.getProperty("name")).toString()).length();
		titleSize = chars*5.5f;
			//picture = p.loadImage((String)this.getProperty("image"));
	}
	
	public CommonMarker(Location location, UnfoldingMap m){
		super (location);
		map = m;
	}


	public void draw(PGraphics pg, float x, float y){
		xCoord = x;
		yCoord = y;
		if (!hidden){
			drawMarker(pg, x, y, map.getZoomLevel());
			if (selected)
				showTitle(pg, x, y);
		}
	}
	
	public abstract void drawMarker(PGraphics pg, float x, float y, int zoom);
	public abstract void showTitle(PGraphics pg, float x, float y); //Show title on hover
	//PUT IN HOSTEL/ACTIVITY MARKER ONLY public void showPicture(){
		//Display picture when marker clicked

}

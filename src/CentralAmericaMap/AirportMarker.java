package CentralAmericaMap;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;


public class AirportMarker extends CommonMarker {

	private int zoomed;
			
	public AirportMarker(Feature airport, UnfoldingMap m) {
		super(((PointFeature)airport).getLocation(), airport.getProperties(), m);
	}

	@Override
	public void drawMarker(PGraphics pg, float x, float y, int zoom) {
		pg.fill(243, 157, 82);
		pg.rect(x, y, zoom*LOGO_SIZE, zoom*LOGO_SIZE);	
		zoomed = zoom;
		type = MarkerType.AIRPORT;
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.fill(230, 193, 160);
		pg.rect(x+23, y, (titleSize/6)*zoomed, (BOX_HEIGHT/6)*zoomed);
		pg.textSize(1.66f*zoomed);
		pg.fill(0,0,0);
		pg.text(this.getStringProperty("name"), x+25, y+((14/6)*zoomed));
		
		pg.fill(0,0,0);
		pg.textSize(20);
		pg.text(this.getStringProperty("name"), 925, 225);
		pg.popStyle();
		
	}

	

}

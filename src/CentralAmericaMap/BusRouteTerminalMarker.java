package CentralAmericaMap;

import java.util.HashMap;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import processing.core.PGraphics;

public class BusRouteTerminalMarker extends CommonMarker {

	private int zoomed;
	
	public BusRouteTerminalMarker(Feature terminal, UnfoldingMap m) {
		super(((PointFeature) terminal).getLocation(), terminal.getProperties(), m);
		type = MarkerType.BUS_ROUTE;
	}

	@Override
	public void drawMarker(PGraphics pg, float x, float y, int zoom) {
		pg.fill(255, 255, 255);
		pg.ellipse(x, y, zoom*LOGO_SIZE, zoom*LOGO_SIZE);
		zoomed = zoom;
		
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.fill(255, 255, 255);
		pg.rect(x+23, y, (titleSize/5)*zoomed, (BOX_HEIGHT/6)*zoomed);
		pg.textSize(1.66f*zoomed);
		pg.fill(0,0,0);
		pg.text(this.getStringProperty("name"), x+25, y+((14/6)*zoomed));
	}


}

package CentralAmericaMap;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class HostelMarker extends CommonMarker {
	
	public PImage picture;
	private int zoomed;	

	public HostelMarker(Feature hostel, UnfoldingMap m, PApplet p) {
		super( ((PointFeature) hostel).getLocation(), hostel.getProperties(), m);
		picture = p.loadImage((String)this.getProperty("image"));
		type = MarkerType.HOSTEL;
	}

	@Override
	public void drawMarker(PGraphics pg, float x, float y, int zoom) {
		pg.fill(0, 43, 255);
		pg.rect(x, y, zoom*LOGO_SIZE, zoom*LOGO_SIZE, 7);
		zoomed = zoom;
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.fill(182, 246, 246);
		pg.rect(x+23, y, (titleSize/6)*zoomed, (BOX_HEIGHT/6)*zoomed);
		pg.textSize(1.66f*zoomed);
		pg.fill(0,0,0);
		pg.text(this.getStringProperty("name"), x+25, y+((14/6)*zoomed));
	}
	
	public void drawInfo(PApplet pa){
		pa.fill(255, 255, 255);
		pa.textSize(30);
		pa.text(this.getStringProperty("name"), 925, 245);
		pa.text(this.getStringProperty("price") + this.getStringProperty("currency") + "/night", 925, 310);
	}

	public void showPicture(PApplet pa){
		picture.resize(350, 250);
		pa.image(picture, 900, 400);
	}
		
}

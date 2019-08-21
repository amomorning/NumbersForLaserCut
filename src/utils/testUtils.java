/**
 * 
 */
package utils;

import processing.core.*;
import wblut.geom.*;
import wblut.hemesh.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import igeo.IG;
import igeo.IVecI;
import igeo.ICurve;
/**
 * @author amo Sep 14, 2018 
 * 
 */
public class testUtils extends PApplet{
	
	WB_Polygon ply;
	WB_Point[] pts;
	Tools tools;
	public static int len = 100;
	HE_Mesh M;
	List<WB_Segment> segs;
	
	
	List<WB_Polygon> result;


	public void setup() {
		Random rnd = new Random(233);
		pts = new WB_Point[3];
		for(int i = 0; i < 3; ++ i) {
			int x = rnd.nextInt()%len;
			int y = rnd.nextInt()%len;
			int z = rnd.nextInt()%len;
			pts[i] = new WB_Point(x, y, z);
		}

		ply = new WB_Polygon(pts);
		
		WB_Point position = ply.getCenter();
		WB_Vector normal = ply.getNormal();
		WB_Vector right = (pts[0].add(pts[2]).mul(0.5)).sub(position);
		right.normalizeSelf();
		normal.normalizeSelf();
		
		segs = new ArrayList<>();
		segs.add(new WB_Segment(position, position.add(right.mul(10))));
		segs.add(new WB_Segment(position, position.add(normal.mul(10))));
		
		
		PNumber pnumber = new PNumber();

		result = pnumber.getNumberOnFace(569, position, normal, right);
		size(1000, 1000, P3D);
		tools = new Tools(this, len);


	}
	
	public void draw() {
		background(0);
		tools.cam.openLight();
		tools.cam.drawSystem(len);
		stroke(255);
		tools.render.drawPolygonEdges(ply);
		stroke(255, 0, 0);
		tools.render.drawPolygonEdges(result);
		
		stroke(0, 0, 255);
		tools.render.drawSegment(segs);
	}
	


	

}

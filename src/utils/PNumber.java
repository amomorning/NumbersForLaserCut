/**
 * 
 */
package utils;

import java.util.ArrayList;
import java.util.List;

import igeo.IG;
import igeo.ICurve;
import igeo.IVecI;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordinateSystem3D;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Transform;
import wblut.geom.WB_Vector;
/**
 * @author amo Aug 21, 2019
 * 
 */
public class PNumber {

	/**
	 * 
	 */

	List<List<WB_Polygon>> plys;
	private WB_GeometryFactory gf = new WB_GeometryFactory();
	private double FontWidth = 25;

	public PNumber() {
		IG.init();
		IG.open("../models/font.3dm");
		plys = new ArrayList<>();
		for (int i = 0; i < 10; ++i) {
			System.out.println("now is " + i);
			plys.add(IcurvestoWB_Polys3D(
					IG.layer(Integer.toString(i)).curves()));
		}
	}

	public List<WB_Polygon> getNumberOnFace(int id, WB_Point position,
			WB_Plane plane) {
		WB_CoordinateSystem3D CS = new WB_CoordinateSystem3D(plane);
		return getNumberOnFace(id, CS);
	}
	public List<WB_Polygon> getNumberOnFace(int id, WB_Point position,
			WB_Vector normal) {
		WB_Plane plane = gf.createPlane(position, normal);
		return getNumberOnFace(id, position, plane);
	}

	public List<WB_Polygon> getNumberOnFace(int id, WB_Point position,
			WB_Vector normal, WB_Vector right) {
		WB_CoordinateSystem3D CS = new WB_CoordinateSystem3D();
		CS.setOrigin(position);
		CS.setX(right);
		CS.setZ(normal);
		return getNumberOnFace(id, CS);

	}
	public List<WB_Polygon> getNumberOnFace(int id, WB_CoordinateSystem3D CS) {
		List<WB_Polygon> ret = new ArrayList<>();
		List<WB_Polygon> digits = new ArrayList<>();
		int k = Integer.toString(id).length();
		int i = 0;
		while (id > 0) {
			digits.addAll(getOneNumber(id%10, k, i++));
			id /= 10;
		}

		WB_Transform T = CS.getTransformToWorld();
		for (WB_Polygon tmp : digits) {
			ret.add(new WB_Polygon(tmp.apply(T).getPoints()));
		}
		return ret;
	}

	public List<WB_Polygon> getOneNumber(int digit, int x, int i) {
		List<WB_Polygon> ret = new ArrayList<>();
		WB_Transform T = new WB_Transform();
		T.addTranslate(new WB_Vector(FontWidth*(x-1)/2.0 - FontWidth*i, 0, 0));
		
		for(WB_Polygon tmp:plys.get(digit)) {
			ret.add(new WB_Polygon(tmp.apply(T).getPoints()));
		}
		return ret;
	}

	public static WB_Polygon IcurvetoWB_Poly3D(ICurve curve) {
		ArrayList<WB_Point> points = new ArrayList<>();
		IVecI[] ps = curve.cps();
		for (int i = 0; i < ps.length; i++) {
			points.add(new WB_Point(ps[i].x(), ps[i].y(), ps[i].z()));
		}
		return new WB_Polygon(points);
	}

	public static List<WB_Polygon> IcurvestoWB_Polys3D(ICurve[] curves) {
		System.out.println("okkk" + curves.length);
		List<WB_Polygon> polys = new ArrayList<>();
		for (int i = 0; i < curves.length; i++) {
			polys.add(IcurvetoWB_Poly3D(curves[i]));
		}
		return polys;
	}

}

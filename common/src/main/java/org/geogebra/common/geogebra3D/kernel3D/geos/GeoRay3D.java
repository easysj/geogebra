package org.geogebra.common.geogebra3D.kernel3D.geos;

import org.geogebra.common.geogebra3D.kernel3D.algos.AlgoJoinPoints3D;
import org.geogebra.common.geogebra3D.kernel3D.algos.AlgoRayPointVector3D;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.Transform;
import org.geogebra.common.kernel.Matrix.Coords;
import org.geogebra.common.kernel.algos.AlgoElement;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoLine;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.geos.LimitedPath;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.kernel.kernelND.GeoRayND;
import org.geogebra.common.plugin.GeoClass;

public class GeoRay3D extends GeoLine3D implements GeoRayND, LimitedPath {

	public GeoRay3D(Construction c, GeoPointND O, GeoPointND Q) {
		super(c, O, Q);
		setStartPoint(O);
	}

	public GeoRay3D(Construction c, GeoPointND O) {
		super(c);
		setStartPoint(O);
	}

	public GeoRay3D(Construction construction) {
		super(construction);
	}

	@Override
	public GeoClass getGeoClassType() {
		return GeoClass.RAY3D;
	}

	@Override
	protected GeoCoordSys1D create(Construction cons) {
		return new GeoRay3D(cons);
	}

	// Path3D interface
	@Override
	public double getMinParameter() {
		return 0;
	}

	@Override
	public boolean isValidCoord(double x) {
		return (x >= 0);
	}

	@Override
	public boolean isOnPath(Coords p, double eps) {
		// first check global line
		if (!super.isOnPath(p, eps)) {
			return false;
		}

		// then check position on segment
		return respectLimitedPath(p, eps);

	}

	@Override
	public boolean respectLimitedPath(Coords p, double eps) {
		if (Kernel.isEqual(p.getW(), 0, eps)) {
			return false;
		}
		double d = p.sub(getStartInhomCoords()).dotproduct(getDirectionInD3());
		if (d < -eps) {
			return false;
		}

		return true;
	}

	// ///////////////////////////////////////
	// LIMITED PATH
	// ///////////////////////////////////////

	private boolean allowOutlyingIntersections = false;
	private boolean keepTypeOnGeometricTransform = true; // for mirroring,
															// rotation, ...

	@Override
	final public boolean isLimitedPath() {
		return true;
	}

	@Override
	public boolean allowOutlyingIntersections() {
		return allowOutlyingIntersections;
	}

	@Override
	public void setAllowOutlyingIntersections(boolean flag) {
		allowOutlyingIntersections = flag;
	}

	@Override
	public boolean keepsTypeOnGeometricTransform() {
		return keepTypeOnGeometricTransform;
	}

	@Override
	public void setKeepTypeOnGeometricTransform(boolean flag) {
		keepTypeOnGeometricTransform = flag;
	}

	@Override
	public GeoElement[] createTransformedObject(Transform t, String label) {
		AlgoElement algoParent1 = keepTypeOnGeometricTransform
				? getParentAlgorithm() : null;

		// CREATE RAY
		if (algoParent1 instanceof AlgoJoinPoints3D && t.isAffine()) {
			// transform points
			AlgoJoinPoints3D algo = (AlgoJoinPoints3D) algoParent1;
			GeoPointND[] points = { algo.getP(), algo.getQ() };
			points = t.transformPoints(points);
			// if(t.isAffine()){
			GeoElement ray = (GeoElement) kernel.getManager3D().Ray3D(label,
					points[0], points[1]);
			ray.setVisualStyleForTransformations(this);
			GeoElement[] geos = { ray, (GeoElement) points[0],
					(GeoElement) points[1] };
			return geos;
			// }
			/*
			 * else { GeoPoint inf = new GeoPoint(cons);
			 * inf.setCoords(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
			 * 1); inf = (GeoPoint)t.doTransform(inf); AlgoConicPartCircumcircle
			 * ae = new AlgoConicPartCircumcircle(cons,
			 * Transform.transformedGeoLabel(this), points[0],
			 * points[1],inf,GeoConicPart.CONIC_PART_ARC);
			 * cons.removeFromAlgorithmList(ae); GeoElement arc =
			 * ae.getConicPart(); arc.setVisualStyleForTransformations(this);
			 * GeoElement [] geos = {arc, points[0], points[1]}; return geos; }
			 */
		}
		// create LINE
		GeoElement transformedLine = t.getTransformedLine(this);
		transformedLine.setLabel(label);
		GeoElement[] ret = { transformedLine };
		return ret;
	}

	@Override
	public boolean isAllEndpointsLabelsSet() {
		return startPoint.isLabelSet();
	}

	@Override
	public boolean isIntersectionPointIncident(GeoPoint p, double eps) {
		if (allowOutlyingIntersections) {
			return isOnFullLine(p.getCoordsInD3(), eps);
		}
		return isOnPath(p, eps);
	}

	@Override
	public GeoElement copyInternal(Construction cons) {
		GeoRay3D ray = new GeoRay3D(cons,
				(GeoPointND) startPoint.copyInternal(cons));
		ray.set(this);
		return ray;
	}

	@Override
	public void set(GeoElementND geo) {
		super.set(geo);
		if (!geo.isGeoRay()) {
			return;
		}

		if (!geo.isDefined()) {
			setUndefined();
		}

		GeoRayND ray = (GeoRayND) geo;

		setKeepTypeOnGeometricTransform(ray.keepsTypeOnGeometricTransform());

		startPoint = GeoLine.updatePoint(cons, startPoint, ray.getStartPoint());
	}

	@Override
	protected void getXMLtags(StringBuilder sb) {
		super.getXMLtags(sb);

		// allowOutlyingIntersections
		sb.append("\t<outlyingIntersections val=\"");
		sb.append(allowOutlyingIntersections);
		sb.append("\"/>\n");

		// keepTypeOnGeometricTransform
		sb.append("\t<keepTypeOnTransform val=\"");
		sb.append(keepTypeOnGeometricTransform);
		sb.append("\"/>\n");

	}

	@Override
	public boolean isGeoRay() {
		return true;
	}

	@Override
	public boolean respectLimitedPath(double parameter) {
		return Kernel.isGreaterEqual(parameter, 0);
	}

	@Override
	final protected void getCoordsXML(StringBuilder sb) {
		// not needed here
	}

	@Override
	public GeoElement copyFreeRay() {
		GeoPointND startPoint1 = (GeoPointND) getStartPoint()
				.copyInternal(cons);

		Coords direction = getDirectionInD3();

		GeoVector3D directionVec = new GeoVector3D(cons);
		directionVec.setCoords(direction);

		AlgoRayPointVector3D algo = new AlgoRayPointVector3D(cons, null,
				startPoint1, directionVec);

		return algo.getOutput(0);
	}

}

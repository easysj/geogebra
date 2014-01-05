package geogebra.common.kernel.algos;

import geogebra.common.kernel.Kernel;
import geogebra.common.kernel.arithmetic.Command;
import geogebra.common.kernel.commands.CommandProcessor;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.kernelND.GeoLineND;
import geogebra.common.kernel.kernelND.GeoVectorND;
import geogebra.common.main.MyError;

/**
 * UnitVector[ <GeoLine> ] UnitVector[ <GeoVector> ]
 */
public class CmdUnitVector extends CommandProcessor {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdUnitVector(Kernel kernel) {
		super(kernel);
	}

	@Override
	final public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		
		GeoElement[] arg;

		switch (n) {
		case 1:
			arg = resArgs(c);
			if (arg[0].isGeoLine()) {
				
				AlgoUnitVector algo = algo(c.getLabel(), (GeoLineND) arg[0]);

				GeoElement[] ret = { (GeoElement) algo.getVector() };
				return ret;
			} else if (arg[0].isGeoVector()) {
				
				AlgoUnitVector algo = algo(c.getLabel(), (GeoVectorND) arg[0]);

				GeoElement[] ret = { (GeoElement) algo.getVector() };
				return ret;
			} else {
				throw argErr(app, c.getName(), arg[0]);
			}

		default:
			throw argNumErr(app, c.getName(), n);
		}
	}
	
	/**
	 * 
	 * @param label vector name
	 * @param line line
	 * @return algo for this line
	 */
	protected AlgoUnitVector algo(String label, GeoLineND line){
		return new AlgoUnitVectorLine(cons, label, line);
	}
	
	
	/**
	 * 
	 * @param label vector name
	 * @param v vector
	 * @return algo for this vector
	 */
	protected AlgoUnitVector algo(String label, GeoVectorND v){
		return new AlgoUnitVectorVector(cons, label, v);
	}
}

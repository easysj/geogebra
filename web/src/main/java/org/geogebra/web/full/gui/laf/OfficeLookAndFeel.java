package org.geogebra.web.full.gui.laf;

import org.geogebra.common.GeoGebraConstants.Versions;
import org.geogebra.common.main.App;
import org.geogebra.web.shared.SignInController;

/**
 * @author geogebra
 * Look and Feel for SMART
 *
 */
public class OfficeLookAndFeel extends SmartLookAndFeel {
	
	@Override
    public boolean undoRedoSupported() {
	    return true;
    }
	
	@Override
    public boolean isSmart() {
		return false;
	}

	@Override
    public String getType() {
	    return "office";
    }

	@Override
	public SignInController getSignInController(App app) {
		return new SignInController(app, 2000, null);
    }

	@Override
	public Versions getVersion(int dim, String appName) {
		return Versions.POWERPOINT;
	}
}

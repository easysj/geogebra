package org.geogebra.desktop.main.settings.updater;

import org.geogebra.common.main.settings.updater.FontSettingsUpdater;
import org.geogebra.desktop.main.AppD;

public class FontSettingsUpdaterD extends FontSettingsUpdater {

	FontSettingsUpdaterD(AppD app) {
		super(app);
	}

	@Override
	public void setAppFontSize(int fontSize) {
		if (getFontSettings().getGuiFontSize() == -1) {
			getApp().setMaxIconSize(fontSize);
		}
		super.setAppFontSize(fontSize);
	}

	@Override
	protected AppD getApp() {
		return (AppD) super.getApp();
	}
}

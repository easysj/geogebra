package org.geogebra.desktop.main.settings;

import org.geogebra.common.main.settings.DefaultSettings;

public class DefaultSettingsD implements DefaultSettings {

	@Override
	public int getAppFontSize() {
		return 12;
	}

	@Override
	public int getGuiFontSize() {
		return -1;
	}
}

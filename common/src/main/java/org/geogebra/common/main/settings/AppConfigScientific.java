package org.geogebra.common.main.settings;

import org.geogebra.common.io.layout.Perspective;
import org.geogebra.common.kernel.commands.selector.CommandSelector;
import org.geogebra.common.kernel.commands.selector.SciCalcCommandSelectorFactory;

/**
 * Config for Scientific Calculator app
 */
public class AppConfigScientific extends AppConfigGraphing {

    @Override
    public String getAppTitle() {
        return "ScientificCalculator";
    }

    @Override
    public String getAppName() {
        return "GeoGebraScientificCalculator";
    }

    @Override
    public String getAppNameShort() {
        return "ScientificCalculator.short";
    }

    @Override
    public String getTutorialKey() {
        return "TutorialScientific";
    }

	@Override
	public boolean allowsSuggestions() {
		return false;
	}

    @Override
	public boolean isGreekAngleLabels() {
		return false;
    }

	@Override
	public String getForcedPerspective() {
		return Perspective.SCIENTIFIC + "";
	}

	@Override
	public boolean hasScientificKeyboard() {
		return true;
	}

	@Override
	public boolean isEnableStructures() {
		return false;
	}

	@Override
	public boolean hasSlidersInAV() {
		return false;
	}

	@Override
	public boolean hasAutomaticLabels() {
		return false;
	}
	
	@Override
	public CommandSelector getCommandSelector() {
		return new SciCalcCommandSelectorFactory().createCommandSelector();
	}
}

package net.zatrit.tunneler;

import com.github.alexdlaird.ngrok.protocol.Region;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui;

import static me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON;

@Config(name = "tunneler")
public class TunnelerConfig implements ConfigData {
    @Gui.Excluded
    public String token = "ENTER YOU TOKEN HERE";
    @Gui.EnumHandler(option = BUTTON)
    public Region region = Region.EU;
}

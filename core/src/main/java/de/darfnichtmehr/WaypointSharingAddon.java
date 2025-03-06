package de.darfnichtmehr;

import de.darfnichtmehr.commands.WaypointSharingCommand;
import de.darfnichtmehr.integration.WaypointsIntegration;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class WaypointSharingAddon extends LabyAddon<WaypointSharingConfiguration> {

  @Override
  protected void enable() {
    this.registerSettingCategory();

    Laby.references().addonIntegrationService()
        .registerIntegration("labyswaypoints", WaypointsIntegration.class);

    this.registerCommand(new WaypointSharingCommand(this));

    this.logger().info("Addon Enabled");
  }

  @Override
  protected Class<WaypointSharingConfiguration> configurationClass() {
    return WaypointSharingConfiguration.class;
  }

}

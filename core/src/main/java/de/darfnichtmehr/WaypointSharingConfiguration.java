package de.darfnichtmehr;

import de.darfnichtmehr.activity.ShareWaypointActivity;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.ActivitySettingWidget.ActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.MultiKeybindWidget.MultiKeyBindSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.MethodOrder;

public class WaypointSharingConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private ConfigProperty<Boolean> addWithoutConfirmation = new ConfigProperty<>(true);

  @MultiKeyBindSetting
  private final ConfigProperty<Key[]> openMenuKeys = new ConfigProperty<>(new Key[0]);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> addWithoutConfirmation() {
    return this.addWithoutConfirmation;
  }

  public ConfigProperty<Key[]> openMenuKeys() {
    return this.openMenuKeys;
  }

  @SettingSection("waypointsPlaceholder")
  @MethodOrder(after = "openMenuKeys")
  @ActivitySetting
  public Activity getWaypoints() {
    return new ShareWaypointActivity();
  }

}

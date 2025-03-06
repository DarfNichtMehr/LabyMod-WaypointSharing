package de.darfnichtmehr.integration;

import de.darfnichtmehr.WaypointSharingConfiguration;
import de.darfnichtmehr.util.MessageTransformer;
import net.labymod.api.Laby;
import net.labymod.api.addon.integration.AddonIntegration;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.input.KeyEvent.State;
import net.labymod.core.addon.DefaultAddonService;

public class WaypointsIntegration implements AddonIntegration {

  @Override
  public void load() {
  }

  @Override
  public void onIntegratedAddonEnable() {
    Laby.labyAPI().eventBus().registerListener(this);

  }

  @Override
  public void onIntegratedAddonDisable() {
    Laby.labyAPI().eventBus().unregisterListener(this);
  }

  @Subscribe
  public void onRecieveWaypoint(ChatReceiveEvent event) {
    MessageTransformer transformer = new MessageTransformer(event.chatMessage());
    if (!transformer.hasWaypoint()) {
      return;
    }

    event.setMessage(transformer.getTransformedMessage());
  }

  @Subscribe
  public void onOpenMenuHotkey(KeyEvent event) {
    if (event.state() != State.PRESS
        || Laby.labyAPI().minecraft().minecraftWindow().isScreenOpened()) {
      return;
    }

    WaypointSharingConfiguration config = (
        (WaypointSharingConfiguration) DefaultAddonService.getInstance().getMainConfiguration("waypointsharing")
    );

    Key[] keys = config.openMenuKeys().get();

    if (keys.length == 0) {
      return;
    }

    for (Key key : keys) {
      if (!key.isPressed()) {
        return;
      }
    }

    Laby.labyAPI().showSetting(
        Laby.labyAPI().coreSettingRegistry().getById("waypointsharing").getById("getWaypoints")
    );
  }
}

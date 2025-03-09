package de.darfnichtmehr.waypointsharing.activity;

import net.labymod.addons.waypoints.activity.WaypointsActivity;
import net.labymod.addons.waypoints.activity.widgets.WaypointListItemWidget;
import net.labymod.addons.waypoints.waypoint.WaypointMeta;
import net.labymod.api.Laby;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.key.MouseButton;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;

@AutoActivity
public class ShareWaypointActivity extends WaypointsActivity {

  private ButtonWidget shareButton;
  private WaypointListItemWidget selectedWaypoint;
  private VerticalListWidget<WaypointListItemWidget> waypointList;

  public ShareWaypointActivity() {
    super(true);
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = (FlexibleContentWidget) super.document.getChild(
        "waypoints-container");

    HorizontalListWidget menu = ((HorizontalListWidget) (container).getChild("overview-button-menu")
        .childWidget());

    this.waypointList = (VerticalListWidget<WaypointListItemWidget>)
        ((ScrollWidget) container.getChildren().get(1).childWidget())
            .contentWidget();

    this.selectedWaypoint = waypointList.listSession().getSelectedEntry();

    this.shareButton = ButtonWidget.i18n(
        "waypointsharing.button.share",
        () -> {
          WaypointMeta meta = selectedWaypoint.getWaypointMeta();

          Laby.labyAPI().minecraft().minecraftWindow().displayScreenRaw(null);

          Laby.labyAPI().minecraft().executeNextTick(() -> {
            Laby.labyAPI().minecraft().openChat(
                String.format("LabyWP@%s|%s,%s,%s|%s",
                    meta.getDimension(),
                    (int) meta.getLocation().getX(),
                    (int) meta.getLocation().getY(),
                    (int) meta.getLocation().getZ(),
                    ((TextComponent) meta.getTitle()).getText()
                ));
          });
        }
    );

    this.shareButton.setEnabled(this.selectedWaypoint != null);
    menu.addEntry(shareButton);

    waypointList.setSelectCallback(waypointListItemWidget -> {
      if (Laby.labyAPI().minecraft().isIngame()) {
        shareButton.setEnabled(true);
      }
    });
  }

  @Override
  public boolean mouseClicked(MutableMouse mouse, MouseButton mouseButton) {
    this.selectedWaypoint = this.waypointList.listSession().getSelectedEntry();
    this.shareButton.setEnabled(this.selectedWaypoint != null);

    return super.mouseClicked(mouse, mouseButton);
  }
}
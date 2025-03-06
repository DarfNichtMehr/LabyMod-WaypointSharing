package de.darfnichtmehr.activity;

import net.labymod.addons.waypoints.activity.WaypointsActivity;
import net.labymod.addons.waypoints.activity.container.ManageContainer;
import net.labymod.addons.waypoints.activity.widgets.WaypointListItemWidget;
import net.labymod.addons.waypoints.waypoint.WaypointMeta;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;

@AutoActivity
public class AddWaypointActivity extends WaypointsActivity {
  private final WaypointMeta waypoint;

  public AddWaypointActivity(WaypointMeta waypoint) {
    super(false);

    this.waypoint = waypoint;
  }

  @Override
  public void initialize(Parent parent) {
    DivWidget manageContainer = new DivWidget();
    manageContainer.addId("manage-container");

    FlexibleContentWidget inputWidget = new FlexibleContentWidget();
    WaypointListItemWidget waypointListItemWidget = new WaypointListItemWidget(this.waypoint);
    ManageContainer manageContainerAdd = new ManageContainer(
        waypointListItemWidget,
        Component.translatable("labyswaypoints.gui.create.permanent"),
        null,
        inputWidget,
        this
    );

    Widget overlayWidget = manageContainerAdd.initializeManageContainer();
    manageContainer.addChild(overlayWidget);
    this.document.addChild(manageContainer);
  }

}

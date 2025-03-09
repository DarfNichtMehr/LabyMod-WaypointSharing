package de.darfnichtmehr.waypointsharing.commands;

import de.darfnichtmehr.waypointsharing.WaypointSharingAddon;
import de.darfnichtmehr.waypointsharing.activity.AddWaypointActivity;
import net.labymod.addons.waypoints.Waypoints;
import net.labymod.addons.waypoints.waypoint.WaypointBuilder;
import net.labymod.addons.waypoints.waypoint.WaypointMeta;
import net.labymod.addons.waypoints.waypoint.WaypointType;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.Color;
import net.labymod.api.util.math.vector.FloatVector3;

import java.util.Arrays;

public class WaypointSharingCommand extends Command {
private final WaypointSharingAddon ADDON;
  public WaypointSharingCommand(WaypointSharingAddon addon) {
    super("waypointsharing");
    this.ADDON = addon;
  }

  @Override
  public boolean execute(String prefix, String[] args) {
    if (args.length < 5) {
      displayMessage(
          Component.translatable("waypointsharing.command.usage", NamedTextColor.RED));
      return true;
    }

    // Verify valid coordinates again because user can execute command manually
    FloatVector3 location;
    try {
      location = new FloatVector3(
          Integer.parseInt(args[1]),
          Integer.parseInt(args[2]),
          Integer.parseInt(args[3])
      );
    } catch (NumberFormatException e) {
      displayMessage(Component.translatable("waypointsharing.command.usage",
          NamedTextColor.RED));
      return true;
    }

    String title = String.join(" ", Arrays.copyOfRange(args, 4, args.length));

    // Limit length again, because user can execute command manually
    title = title.length() <= 50 ? title : title.substring(0, 50);

    WaypointMeta waypoint = WaypointBuilder.newBuilder()
        .title(Component.text(title))
        .color(Color.WHITE)
        .type(WaypointType.PERMANENT)
        .location(location)
        .visible(true)
        .world(Waypoints.getReferences().waypointService().actualWorld())
        .server(Waypoints.getReferences().waypointService().actualServer())
        .dimension(args[0])
        .build();

    if (ADDON.configuration().addWithoutConfirmation().get()) {
      Waypoints.getReferences().waypointService().addWaypoint(waypoint);
      Waypoints.refresh();
      displayMessage(Component.translatable("waypointsharing.command.waypointAdded", NamedTextColor.GREEN));
    }
    else {
      Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new AddWaypointActivity(waypoint));
    }

    return true;
  }
}

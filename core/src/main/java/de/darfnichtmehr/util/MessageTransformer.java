package de.darfnichtmehr.util;

import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;

public class MessageTransformer {

  private final boolean hasWaypoint;
  private int x, y, z;
  private String dimension, name, command, formattedText, plainText;

  public MessageTransformer(ChatMessage message) {
    this.plainText = message.getPlainText();

    this.hasWaypoint = extractWaypoint();

    if (!this.hasWaypoint) {
      return;
    }

    this.formattedText = message.getFormattedText();

    this.command = String.format("/waypointsharing %s %s %s %s %s",
        this.dimension,
        this.x,
        this.y,
        this.z,
        this.name
    );
  }

  public boolean hasWaypoint() {
    return this.hasWaypoint;
  }

  public Component getTransformedMessage() {

    Component hoverText = Component.
        translatable("waypointsharing.chat.clickToAdd", NamedTextColor.GREEN)
        .append(Component.newline())
        .append(Component.translatable("waypointsharing.chat.name", NamedTextColor.GOLD,
            Component.text(this.name, NamedTextColor.GRAY))
        )
        .append(Component.newline())
        .append(Component.translatable("waypointsharing.chat.position", NamedTextColor.GOLD,
            Component.text(this.x, NamedTextColor.GRAY),
            Component.text(this.y, NamedTextColor.GRAY),
            Component.text(this.z, NamedTextColor.GRAY)
        ))
        .append(Component.newline())
        .append(Component.translatable("waypointsharing.chat.dimension", NamedTextColor.GOLD,
            Component.text(this.dimension, NamedTextColor.GRAY)));

    int end = formattedText.indexOf("LabyWP@");

    return Component
        .text(formattedText.substring(0, end))
        .append(Component.text("[WAYPOINT]", NamedTextColor.GREEN)
            .append(Component.text(" » ", NamedTextColor.GRAY))
            .append(Component.text(this.name, NamedTextColor.GREEN))
            .clickEvent(ClickEvent.runCommand(this.command))
            .hoverEvent(HoverEvent.showText(hoverText)
            )
        );
  }

  private boolean extractWaypoint() {
    if (!this.plainText.contains("LabyWP@")) {
      return false;
    }

    int start = this.plainText.indexOf("LabyWP@");
    String[] parts = this.plainText.substring(start + 7).split("\\|", 3);

    // Verify valid format
    if (parts.length != 3) {
      return false;
    }

    String[] coordinates = parts[1].split(",");

    // Verify valid name and dimension
    if (parts[0].isEmpty() || parts[2].isEmpty()) {
      return false;
    }

    // Verify valid coordinates
    if (coordinates.length != 3) {
      return false;
    }

    try {
      this.x = Integer.parseInt(coordinates[0]);
      this.y = Integer.parseInt(coordinates[1]);
      this.z = Integer.parseInt(coordinates[2]);
    } catch (NumberFormatException e) {
      return false;
    }

    this.dimension = parts[0];

    // Limit name-length to 50 because Laby's Waypoints does it
    this.name = parts[2].length() <= 50 ? parts[2] : parts[2].substring(0, 50);

    return true;
  }
}

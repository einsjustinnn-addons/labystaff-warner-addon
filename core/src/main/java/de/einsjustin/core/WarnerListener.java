package de.einsjustin.core;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoAddEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoRemoveEvent;
import net.labymod.api.event.client.world.WorldLeaveEvent;
import net.labymod.api.mojang.GameProfile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WarnerListener {

  private final Set<GameProfile> staffInWorld = new HashSet<>();
  private final Set<GameProfile> staffInNear = new HashSet<>();

  private final Component PREFIX = Component.text("[LabyStaff Warner] ").color(TextColor.color(255, 106, 13));

  @Subscribe
  public void playerInfoAdd(PlayerInfoAddEvent event) {
    GameProfile profile = event.playerInfo().profile();
    UUID uniqueId = profile.getUniqueId();

    if (staffInWorld.contains(profile)) return;

    if (StaffUtil.getStaffMembers().contains(uniqueId)) {
      staffInWorld.add(profile);

      if (WarnerAddon.INSTANCE.configuration().worldNotification().get()) {
        if (WarnerAddon.INSTANCE.configuration().soundNotification().get()) {
          Laby.labyAPI().minecraft().sounds().playSound(ResourceLocation.create("minecraft", "block.note_block.bell"), 1F, 1F);
        }
        Component component = PREFIX.copy();
        component.append(Component.translatable("labystaffwarner.messages.joinWorld")
            .argument(Component.text(profile.getUsername()))
            .color(TextColor.color(235, 55, 0)));
        Laby.labyAPI().minecraft().chatExecutor().displayClientMessage(component);
      }
    }
  }

  @Subscribe
  public void playerInfoRemove(PlayerInfoRemoveEvent event) {
    GameProfile profile = event.playerInfo().profile();

    if (staffInWorld.remove(profile) && WarnerAddon.INSTANCE.configuration().worldNotification().get()) {
      if (WarnerAddon.INSTANCE.configuration().soundNotification().get()) {
        Laby.labyAPI().minecraft().sounds().playSound(ResourceLocation.create("minecraft", "block.note_block.bell"), 1F, 1F);
      }
      Component component = PREFIX.copy();
      component.append(Component.translatable("labystaffwarner.messages.leftWorld")
          .argument(Component.text(profile.getUsername()))
          .color(TextColor.color(125, 214, 0)));
      Laby.labyAPI().minecraft().chatExecutor().displayClientMessage(component);
    }
  }

  @Subscribe
  public void worldLeave(WorldLeaveEvent event) {
    staffInWorld.clear();
    staffInNear.clear();
  }

  @Subscribe
  public void gameTick(GameTickEvent event) {
    List<Player> players = Laby.labyAPI().minecraft().clientWorld().getPlayers();
    Set<GameProfile> currentlyNearStaff = new HashSet<>();

    for (Player player : players) {
      GameProfile profile = player.profile();

      if (staffInWorld.contains(profile)) {
        currentlyNearStaff.add(profile);

        if (staffInNear.add(profile) && WarnerAddon.INSTANCE.configuration().nearNotification().get()) {
          if (WarnerAddon.INSTANCE.configuration().soundNotification().get()) {
            Laby.labyAPI().minecraft().sounds().playSound(ResourceLocation.create("minecraft", "block.note_block.bell"), 1F, 1F);
          }
          Component component = PREFIX.copy();
          component.append(Component.translatable("labystaffwarner.messages.joinNear")
              .argument(Component.text(profile.getUsername()))
              .color(TextColor.color(235, 55, 0)));
          Laby.labyAPI().minecraft().chatExecutor().displayClientMessage(component);
        }
      }
    }

    staffInNear.removeIf(profile -> {
      if (!currentlyNearStaff.contains(profile)) {
        if (WarnerAddon.INSTANCE.configuration().soundNotification().get()) {
          Laby.labyAPI().minecraft().sounds().playSound(ResourceLocation.create("minecraft", "block.note_block.bell"), 1F, 1F);
        }
        if (WarnerAddon.INSTANCE.configuration().nearNotification().get()) {
          Component component = PREFIX.copy();
          component.append(Component.translatable("labystaffwarner.messages.leftNear")
              .argument(Component.text(profile.getUsername()))
              .color(TextColor.color(125, 214, 0)));
          Laby.labyAPI().minecraft().chatExecutor().displayClientMessage(component);
        }
        return true;
      }
      return false;
    });
  }
}

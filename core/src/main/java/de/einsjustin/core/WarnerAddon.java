package de.einsjustin.core;

import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class WarnerAddon extends LabyAddon<WarnerConfiguration> {

  public static WarnerAddon INSTANCE;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    INSTANCE = this;

    StaffUtil.fetchStaffMembers();

    this.registerListener(new WarnerListener());
  }

  @Override
  protected Class<WarnerConfiguration> configurationClass() {
    return WarnerConfiguration.class;
  }

}

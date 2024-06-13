package de.einsjustin.core;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
public class WarnerConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> worldNotification = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> nearNotification = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> soundNotification = new ConfigProperty<>(true);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> worldNotification() {
    return worldNotification;
  }

  public ConfigProperty<Boolean> nearNotification() {
    return nearNotification;
  }

  public ConfigProperty<Boolean> soundNotification() {
    return soundNotification;
  }
}

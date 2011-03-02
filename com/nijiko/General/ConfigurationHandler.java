package com.nijiko.cjcfork.General;

import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

/**
 * Handles default configuration and loads data.
 * 
 * @author Nijiko
 */
public class ConfigurationHandler extends DefaultConfiguration {
	private Configuration config;

	public ConfigurationHandler(Configuration config) {
		this.config = config;
	}

	public void load() throws NullPointerException {
		// if one of them is null, all of them are null. If one isn't, none are.
		try {
				this.health = config.getBoolean("playerlist.show-health", this.health);
				this.coords = config.getBoolean("playerlist.show-coords", this.coords);
				this.commands = config.getBoolean("help.inject-commands", this.commands);
		} catch (NullPointerException npe) {
			ConfigurationNode newConf = Configuration.getEmptyNode();
			newConf.setProperty("playerlist.show-health", true);
			newConf.setProperty("playerlist.show-coords", true);
			newConf.setProperty("help.inject-commands", true);
		}
	}
}

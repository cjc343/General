package com.nijikokun.cjcfork.bukkit.General;

import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.event.server.PluginEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.nijikokun.bukkit.Permissions.Permissions;

public class CommandChecker extends ServerListener {
	public static General plugin;

	public CommandChecker(General instance) {
		plugin = instance;
	}
	
	@Override
	public void onPluginEnabled(PluginEvent pl) {
		Plugin p = pl.getPlugin();
		if (p != null && !p.getDescription().getName().equalsIgnoreCase(General.name)) {

			if (p.getDescription().getName().equals("Permissions")) {
				General.Permissions = (Permissions) p;
				System.out.println("[" + General.name + "] hooked into Permissions.");
			} else if (p.getDescription().getCommands() != null) {
				for (Command c : PluginCommandYamlParser.parse(p)) {
					if (iListen.cmds.containsKey(c.getName())) {
						System.out.println(General.name + " is giving " + c.getName() + " to " + p.getDescription().getName());
						iListen.cmds.put(c.getName(), false);
						// compare command to hashtable with commands
						// General uses...
						// command exists in general. Need to....
					}
					// System.out.println(c.getName());
				}
				// System.out.println(p.getDescription().getCommands().toString());
			}
		}

	}
}

package com.nijikokun.cjcfork.bukkit.General;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.iConomy.iConomy;
import com.nijikokun.bukkit.Permissions.Permissions;

public class CommandChecker extends ServerListener {
	public static General plugin;

	public CommandChecker(General instance) {
		plugin = instance;
	}

	@Override
	public void onPluginEnable(PluginEnableEvent pl) {
		Plugin p = pl.getPlugin();
		if (p != null && !p.getDescription().getName().equalsIgnoreCase(General.name)) {
			if (p.getDescription().getName().equals("Permissions")) {
				General.Permissions = (Permissions) p;
				System.out.println("[" + General.name + "] hooked into Permissions.");
			} else if (p.getDescription().getName().equals("iConomy")) {
				General.iConomy = (iConomy) p;
				System.out.println("[" + General.name + "] hooked into iConomy.");				
			} else {
				iListen.checkPluginCommands(p);
			}
		}
	}
}

package com.nijikokun.cjcfork.bukkit.General;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import net.minecraft.server.WorldServer;
//import org.bukkit.World;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
//import org.bukkit.event.server.PluginEvent;
//import org.bukkit.event.server.ServerEvent;
import org.bukkit.Location;
//import org.bukkit.command.Command;
//import org.bukkit.command.PluginCommandYamlParser;
//import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;

/**
 * General 1.1 & Code from iConomy 2.x
 * Coded while listening to Avenged Sevenfold - A little piece of heaven <3
 * Copyright (C) 2011  Nijikokun <nijikokun@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * iListen.java <br />
 * <br />
 * Listens for calls from hMod, and reacts accordingly.
 * 
 * @author Nijikokun <nijikokun@gmail.com>
 */
public class iListen extends PlayerListener {

	private static final Logger log = Logger.getLogger("Minecraft");
	private ArrayList<String> lines = new ArrayList<String>();

	/*
	 * Miscellaneous things required.
	 */
	public Misc Misc = new Misc();
	public HashMap<Player, String> AFK = new HashMap<Player, String>();
	public List<String> Commands = new ArrayList<String>();
	public static General plugin;
	public WorldServer server;

	//common strings
	final String sl = "/";

	final String corUse = "&cCorrect usage is: ";
	final String incUse = "&cIncorrect usage of ";
	// all commands checked for by general.
	
	public String[] cmdArray = { "afk", "away", "compass", "getpos", "ghelp", "give", "help", "i", "item", "motd", "msg", "online", "playerlist", "reloaditems", "rlidb", "s", "setspawn", "spawn", "teleport", "tell", "time", "tp", "tphere", "who" };

	// 0 afk
	// 1 away
	// 2 compass
	// 3 getpos
	// 4 ghelp
	// 5 give
	// 6 help
	// 7 i
	// 8 item
	// 9 motd
	// 10 msg
	// 11 online
	// 12 playerlist
	// 13 reloaditems
	// 14 rlidb
	// 15 s
	// 16 setspawn
	// 17 spawn
	// 18 teleport
	// 19 tell
	// 20 time
	// 21 tp
	// 22 tphere
	// 23 who

	public static HashMap<String, Boolean> cmds = new HashMap<String, Boolean>();

	public iListen(General instance) {
		plugin = instance;
	}

	
	public void setupCmds() {
		for (String s : cmdArray) {
			cmds.put(s, true);
		}
	}
//	@Override
//	public void onPluginEnabled(PluginEvent p) {
//		
//	
//		// input commands into hash table
//		for (String s : cmdArray) {
//			cmds.put(s, true);
//		}
//		Plugin[] pArray = plugin.getServer().getPluginManager().getPlugins();
//		for (Plugin p : pArray) {
//			if (p != null && !p.getDescription().getName().equalsIgnoreCase(General.name)) {
//				//plugin.getServer().getPluginManager().enablePlugin(p);
//				if (p.getDescription().getCommands() != null) {
//					for (Command c : PluginCommandYamlParser.parse(p)) {
//						if (cmds.containsKey(c.getName())) {
//							System.out.println(General.name + " is giving " + c.getName() + " to " + p.getDescription().getName());
//							cmds.put(c.getName(), false);
//							// compare command to hashtable with commands
//							// General uses...
//							// command exists in general. Need to....
//						}
//						// System.out.println(c.getName());
//					}
//					// System.out.println(p.getDescription().getCommands().toString());
//				}
//			}
//		}
//	}

	private Location spawn(Player player) {
		// lol, duh. Courtesy of browsing haruArc's (github commit
		// 827cc074dc1c79486ead) General fork. Realized I had probably
		// overcomplicated things. And I had.
		return player.getWorld().getSpawnLocation();
	}

	public long getTime(Player player) {
		return player.getWorld().getTime();
	}

	public long getRelativeTime(Player player) {
		return (getTime(player) % 24000);
	}

	public long getStartTime(Player player) {
		return (getTime(player) - getRelativeTime(player));
	}

	public void setTime(long time, Player player) {
		player.getWorld().setTime(time);
	}

	// private void setRelativeTime(long time, Player player) {
	// long margin = (time - getTime(player)) % 24000;
	//
	// if (margin < 0) {
	// margin += 24000;
	// }
	//
	// player.getWorld().setTime(getTime(player) + margin);
	// }

	protected boolean teleport(String who, String to) {
		Player destination = Misc.playerMatch(to);

		if (who.equalsIgnoreCase("*")) {
			Player[] players = plugin.getServer().getOnlinePlayers();

			for (Player player : players) {
				if (!player.equals(destination)) {
					player.teleportTo(destination.getLocation());
				}
			}

			return true;
		} else if (who.contains(",")) {
			String[] players = who.split(",");

			for (String name : players) {
				Player player = Misc.playerMatch(name);

				if ((player == null) || (destination == null)) {
					continue;
				} else {
					if (!player.equals(destination)) {
						player.teleportTo(destination.getLocation());
					}
				}
			}

			return true;
		} else {
			Player player = Misc.playerMatch(who);

			if ((player == null) || (destination == null)) {
				return false;
			} else {
				player.teleportTo(destination.getLocation());
				return true;
			}
		}
	}

	private String getDirection(double degrees) {
		if (0 <= degrees && degrees < 22.5) {
			return "N";
		} else if (22.5 <= degrees && degrees < 67.5) {
			return "NE";
		} else if (67.5 <= degrees && degrees < 112.5) {
			return "E";
		} else if (112.5 <= degrees && degrees < 157.5) {
			return "SE";
		} else if (157.5 <= degrees && degrees < 202.5) {
			return "S";
		} else if (202.5 <= degrees && degrees < 247.5) {
			return "SW";
		} else if (247.5 <= degrees && degrees < 292.5) {
			return "W";
		} else if (292.5 <= degrees && degrees < 337.5) {
			return "NW";
		} else if (337.5 <= degrees && degrees < 360.0) {
			return "N";
		} else {
			return "ERR";
		}
	}

	public boolean isAFK(Player player) {
		return AFK.containsKey(player);
	}

	public void AFK(Player player, String message) {
		AFK.put(player, message);
	}

	public void unAFK(Player player) {
		AFK.remove(player);
	}

	public String[] readMotd() {
		ArrayList<String> motd = new ArrayList<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(plugin.getDataFolder() + File.separator + plugin.pBase + "motd"));
			String str;
			while ((str = in.readLine()) != null) {
				motd.add(str);
			}
			in.close();
		} catch (IOException e) {
		}

		return motd.toArray(new String[] {});
	}

	public String[] read_commands() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(plugin.getDataFolder() + File.separator + plugin.pBase + "help"));
			String str;
			while ((str = in.readLine()) != null) {
				if (!lines.contains(str)) {
					lines.add(str);
				} else {
					continue;
				}
			}
			in.close();
		} catch (IOException e) {
		}

		return lines.toArray(new String[] {});
	}

	public void print_commands(int page) {
		String[] commands = read_commands();
		int amount = 0;

		if (page > 0) {
			amount = (page - 1) * 7;
		} else {
			amount = 0;
		}

		Messaging.send("&dHelp &f(&dPage &f" + (page != 0 ? page : "1") + "&d of&f " + (int) Math.ceil((double) commands.length / 7D) + "&d) [] = required, () = optional:");

		try {
			for (int i = amount; i < amount + 7; i++) {
				if (commands.length > i) {
					Messaging.send(commands[i]);
				}
			}
		} catch (NumberFormatException ex) {
			Messaging.send("&cNot a valid page number.");
		}
	}

	public void register_command(String command, String help) {
		if (!Commands.contains(command.replace("|", "&5|&f") + help)) {
			Commands.add(command.replace("|", "&5|&f") + help);
		}
	}

	public void register_custom_command(String command) {
		if (!Commands.contains(command)) {
			Commands.add(command);
		}
	}

	public void save_command(String command, String help) {
		if (!Commands.contains(command + " &5-&3 " + help)) {
			Commands.add(command + " &5-&3 " + help);
		}
	}

	public void save_custom_command(String command) {
		if (!Commands.contains(command)) {
			Commands.add(command);
		}
	}

	public void remove_command(String command, String help) {
		if (Commands.contains(command.replace("|", "&5|&f") + " &5-&3 " + help)) {
			Commands.remove(command.replace("|", "&5|&f") + " &5-&3 " + help);
		} else {
			// General.log.info("Help command registry does not contain "+command+" to remove!");
		}
	}

	public void remove_custom_command(String command_line) {
		if (Commands.contains(command_line)) {
			Commands.remove(command_line);
		} else {
			// General.log.info("Help command registry does not contain "+command_line+" to remove!");
		}
	}
	
	private void sendMotd(Player player) {
		String[] motd = readMotd();

		if (motd == null || motd.length < 1) {
			return;
		}

		String location = (int) player.getLocation().getX() + "x, " + (int) player.getLocation().getY() + "y, " + (int) player.getLocation().getZ() + "z";
		String ip = player.getAddress().getAddress().getHostAddress();
		String balance = "";

		Plugin test = plugin.getServer().getPluginManager().getPlugin("iConomy");
		if (test != null) {
			Account pAct = iConomy.getBank().getAccount(player.getName());
			if (pAct != null) {
				balance = pAct + " " + iConomy.getBank().getCurrency();
				for (String line : motd) {
					Messaging.send(player, Messaging.argument(line, new String[] { "+dname,+d", "+name,+n", "+location,+l", "+health,+h", "+ip", "+balance", "+online" }, new String[] { player.getDisplayName(), player.getName(), location, Misc.string(player.getHealth()), ip, balance, Misc.string(plugin.getServer().getOnlinePlayers().length) }));
				}
			} else {
				final Player pp = player;
				plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
					public void run() {
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							public void run() {
								PlayerChatEvent newEvent = new PlayerChatEvent(Type.PLAYER_CHAT, pp, sl + cmdArray[9]);
								onPlayerCommandPreprocess(newEvent);
							}
						});
					}
				}, 10);
			}
		} else {
			for (String line : motd) {
				Messaging.send(player, Messaging.argument(line, new String[] { "+dname,+d", "+name,+n", "+location,+l", "+health,+h", "+ip", "+balance", "+online" }, new String[] { player.getDisplayName(), player.getName(), location, Misc.string(player.getHealth()), ip, balance, Misc.string(plugin.getServer().getOnlinePlayers().length) }));
			}
		}
	}

	@Override
	public void onPlayerJoin(PlayerEvent event) {
		if (cmds.get(cmdArray[9])) { // motd
			sendMotd(event.getPlayer());
		}
	}

	/**
	 * Commands sent from in game to us.
	 * 
	 * @param player
	 *            The player who sent the command.
	 * @param split
	 *            The input line split by spaces.
	 * @return <code>boolean</code> - True denotes that the command existed,
	 *         false the command doesn't.
	 */
	@Override
	public void onPlayerCommandPreprocess(PlayerChatEvent event) {

		String[] split = event.getMessage().split(" ");
		Player player = event.getPlayer();
		//World world = player.getWorld();
		//server = ((CraftWorld) world).getHandle();
		Messaging.save(player);
		String base = split[0];
		////// help ////////////// help /////////// ? ////////// ? /////////// ghelp ///////////// ghelp //////////////
		if ((!event.isCancelled()) && (Misc.isEither(base, sl + cmdArray[6], sl + "?") && cmds.get(cmdArray[6])) || (Misc.is(base, sl + cmdArray[4])) && cmds.get(cmdArray[4])) {
			// 6: help 4: ghelp
			int page = 0;

			if (split.length >= 2) {
				try {
					page = Integer.parseInt(split[1]);
				} catch (NumberFormatException ex) {
					Messaging.send("&cNot a valid page number.");
					event.setCancelled(true);
					return;
				}
			}

			print_commands(page);
			event.setCancelled(true);
		}
		/////////////////// setspawn //////////////////////////////// setspawn
		if ((!event.isCancelled()) && Misc.is(base, sl + cmdArray[16]) && cmds.get(cmdArray[16])) {
			//16: setspawn
			if (!General.Permissions.getHandler().permission(player, plugin.pBase + cmdArray[17] + ".set")) {//general.spawn.set
				return;
			}
			player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
			Messaging.send("&eSpawn position changed to where you are standing.");
		}
		/////////////////////// reloaditems ///////////////////////// reloaditems
		if (Misc.isEither(base, sl+ "rlidb", sl+cmdArray[13]) && cmds.get(cmdArray[13])) {
			//13: reloaditems
			if (!General.Permissions.getHandler().permission(player, plugin.pBase + cmdArray[13])) {//general.reloaditems
				return;
			}
			plugin.setupItems();
			Messaging.send("&eItems.db reloaded.");
		}
		/////////////////////////// spawn //////////////////////////// spawn //////////////
		if (Misc.is(base, sl + cmdArray[17]) && cmds.get(cmdArray[17])) {
			//17: spawn
			if (!General.Permissions.getHandler().permission(player, plugin.pBase + cmdArray[17])) {//general.spawn
				return;
			}

			player.teleportTo(spawn(player));
		}
		///////////////////// motd //////////////////////// motd //////////////////
		if ((!event.isCancelled()) && Misc.is(base, sl + cmdArray[9]) && cmds.get(cmdArray[9])) {
			//9: motd
			sendMotd(player);
		}
		///////////// tp /////////////// teleport ////////////// tp ///////////////// teleport //////////////
		if ((Misc.is(base, sl+ cmdArray[21]) && cmds.get(cmdArray[21])) || (Misc.is(base, sl+ cmdArray[18]) && cmds.get(cmdArray[18]))) {
			// 21: tp 18: teleport
			if (!General.Permissions.getHandler().permission(player, plugin.pBase + cmdArray[18])) {//general.teleport
				return;
			}

			if (split.length == 2) {
				String to = split[1];

				if (to.equalsIgnoreCase("*")) {
					Messaging.send(incUse + "wildchar *");
				} else if (to.contains(",")) {
					Messaging.send(incUse + "multiple players.");
				} else {
					if (!teleport(player.getName(), to)) {
						Messaging.send("&cCannot find destination player: &f" + to);
					}
				}
			} else if (split.length == 3) {
				String who = split[1];
				String to = split[2];

				if (to.equalsIgnoreCase("*")) {
					Messaging.send(incUse + "wildchar *");
				} else if (to.contains(",")) {
					Messaging.send(incUse + "multiple players.");
				} else {
					if (!teleport(who, to)) {
						Messaging.send("&cCould not teleport " + who + " to " + to + ".");
					}
				}
			} else {
				Messaging.send("&c------ &f/tp help&c ------");
				Messaging.send("&c/tp [player] &f-&c Teleport to a player");
				Messaging.send("&c/tp [player] [to] &f-&c Teleport player to another player");
				Messaging.send("&c/tp [player,...] [to] &f-&c Teleport players to another player");
				Messaging.send("&c/tp * [to] &f-&c Teleport everyone to another player");
			}
		}
		////////// s /////////// tphere ////////////// s //////////////// tphere //////////////////
		if ((Misc.is(base, sl + cmdArray[15]) && cmds.get(cmdArray[15])) || (Misc.is(base, sl + cmdArray[22]) && cmds.get(cmdArray[22]))) {
			//15: s 22: tphere
			if (!General.Permissions.getHandler().permission(player, plugin.pBase + cmdArray[18] + ".here")) { //general.teleport.here
				return;
			}

			if (split.length < 2) {
				Messaging.send(corUse + "&f" + sl + cmdArray[15] + " [player] &cor&f " + sl + cmdArray[22] + " [player]");
				return;
			}

			Player who = Misc.playerMatch(split[1]);

			if (who != null) {
				if (who.getName().equalsIgnoreCase(player.getName())) {
					Messaging.send("&cWow look at that! You teleported yourself to yourself!");
					return;
				}

				log.info(player.getName() + " teleported " + who.getName() + " to their self.");
				who.teleportTo(player.getLocation());
			} else {
				Messaging.send("&cCan't find user " + split[1] + ".");
			}
		}
		//////////////// getpos //////////////////////// getpos /////////////////////
		if ((!event.isCancelled()) && Misc.is(base, sl + cmdArray[3]) && cmds.get(cmdArray[3])) {
			//3: getpos
			Messaging.send("Pos X: " + player.getLocation().getX() + " Y: " + player.getLocation().getY() + " Z: " + player.getLocation().getZ());
			Messaging.send("Rotation: " + player.getLocation().getYaw() + " Pitch: " + player.getLocation().getPitch());

			double degreeRotation = ((player.getLocation().getYaw() - 90) % 360);

			if (degreeRotation < 0) {
				degreeRotation += 360.0;
			}

			Messaging.send("Compass: " + getDirection(degreeRotation) + " (" + (Math.round(degreeRotation * 10) / 10.0) + ")");
		}
		////////////////// compass ////////////// compass ////////////
		if (Misc.is(base, sl + cmdArray[2]) && cmds.get(cmdArray[2])) {
			//2: compass
			double degreeRotation = ((player.getLocation().getYaw() - 90) % 360);

			if (degreeRotation < 0) {
				degreeRotation += 360.0;
			}

			Messaging.send("&cCompass: " + getDirection(degreeRotation));
		}
		/////////////// afk ////////////// away ////////////// afk ///////////////// away ///////////////
		if ((Misc.is(base, sl + cmdArray[0]) && cmds.get(cmdArray[0])) || (Misc.is(base, sl + cmdArray[1]) && cmds.get(cmdArray[1]))) {
			//0: afk 1: away
			if ((AFK != null || !AFK.isEmpty()) && isAFK(player)) {
				Messaging.send("&7You have been marked as back.");
				unAFK(player);
			} else {
				Messaging.send("&7You are now currently marked as away.");
				String reason = "AFK";

				if (split.length >= 2) {
					reason = Misc.combineSplit(1, split, " ");
				}

				AFK(player, reason);
			}
		}
		////////////////// msg ///////////////// tell /////////////// msg ////////////////// tell //////////////////
		if ((Misc.is(base, sl + cmdArray[10]) && cmds.get(cmdArray[10])) || (Misc.is(base, sl + cmdArray[19]) && cmds.get(cmdArray[19]))) {
			// 10: msg 19: tell
			if (split.length < 3) {
				Messaging.send("&cCorrect usage is: " + sl + cmdArray[10] + " [player] [message]");
				event.setCancelled(true);
				return;
			}

			Player who = Misc.playerMatch(split[1]);

			if (who != null) {
				if (who.getName().equals(player.getName())) {
					Messaging.send("&cYou can't message yourself!");
					event.setCancelled(true);
					return;
				}

				Messaging.send("(MSG) <" + player.getName() + "> " + Misc.combineSplit(2, split, " "));
				Messaging.send(who, "(MSG) <" + player.getName() + "> " + Misc.combineSplit(2, split, " "));

				if (isAFK(who)) {
					Messaging.send("&7This player is currently away.");
					Messaging.send("&7Reason: " + AFK.get(player));
				}
			} else {
				Messaging.send("&cCouldn't find player " + split[1]);
			}
		}
		///////////// i //////////// give //////////// item ///////// i /////////// give ////////// item ///////////
		if ((Misc.is(base, sl + cmdArray[7]) && cmds.get(cmdArray[7])) || (Misc.is(base, sl + cmdArray[5]) && cmds.get(cmdArray[5])) || (Misc.is(base, sl + cmdArray[8]) && cmds.get(cmdArray[8]))) {
			//7: i 5: give 8: item
			if (!General.Permissions.getHandler().permission(player, plugin.pBase + cmdArray[8] + "s")) { //general.items
				return;
			}

			if (split.length < 2) {
				Messaging.send(corUse + sl + cmdArray[7] +" [" + cmdArray[8] +"(:type)|player] [" + cmdArray[8] +"(:type)|amount] (amount)");
				return;
			}

			int itemId = 0;
			int[] tmp;
			int amount = 1;
			int dataType = -1;
			Player who = null;

			try {
				if (split[1].contains(":")) {
					String[] data = split[1].split(":");

					try {
						dataType = Integer.valueOf(data[1]);
					} catch (NumberFormatException e) {
						dataType = -1;
					}

					tmp = Items.validate(data[0]);
					itemId = tmp[0];
				} else {
					tmp = Items.validate(split[1]);
					itemId = tmp[0];
					dataType = tmp[1];
				}

				if (itemId == -1) {
					who = Misc.playerMatch(split[1]);
				}
			} catch (NumberFormatException e) {
				who = Misc.playerMatch(split[1]);
			}

			if ((itemId == 0 || itemId == -1) && who != null) {
				String i = split[2];

				if (i.contains(":")) {
					String[] data = i.split(":");

					try {
						dataType = Integer.valueOf(data[1]);
					} catch (NumberFormatException e) {
						dataType = -1;
					}

					i = data[0];
				}

				tmp = Items.validate(i);
				itemId = tmp[0];

				if (dataType == -1) {
					dataType = Items.validateGrabType(i);
				}
			}

			if (itemId == -1 || itemId == 0) {
				Messaging.send("&cInvalid " + cmdArray[8] +".");
				return;
			}

			if (dataType != -1) {
				if (!Items.validateType(itemId, dataType)) {
					Messaging.send("&f" + dataType + "&c is not a valid data type for &f" + Items.name(itemId, -1) + "&c.");
					return;
				}
			}

			if (split.length >= 3 && who == null) {
				try {
					amount = Integer.valueOf(split[2]);
				} catch (NumberFormatException e) {
					amount = 1;
				}
			} else if (split.length >= 4) {
				if (who != null) {
					try {
						amount = Integer.valueOf(split[3]);
					} catch (NumberFormatException e) {
						amount = 1;
					}
				} else {
					who = Misc.playerMatch(split[3]);
				}
			}

			if (amount == 0) { // give one stack
				if (itemId == 332 || itemId == 344) {
					amount = 16; // eggs and snowballs
				} else if (Items.isStackable(itemId)) {
					amount = 64;
				} else {
					amount = 1;
				}
			}

			if (who == null) {
				who = player;
			}

			int slot = who.getInventory().firstEmpty();

			if (dataType != -1) {
				if (slot < 0) {
					who.getWorld().dropItem(who.getLocation(), new ItemStack(itemId, amount, ((byte) dataType)));
				} else {
					who.getInventory().addItem(new ItemStack(itemId, amount, ((byte) dataType)));
				}
			} else {
				if (slot < 0) {
					who.getWorld().dropItem(who.getLocation(), new ItemStack(itemId, amount));
				} else {
					who.getInventory().addItem(new ItemStack(itemId, amount));
				}
			}

			if (who.getName().equals(player.getName())) {
				Messaging.send(who, "&2Enjoy! Giving &f" + amount + "&2 of &f" + Items.name(itemId, dataType) + "&2.");
			} else {
				Messaging.send(who, "&2Enjoy the gift! &f" + amount + "&2 of &f" + Items.name(itemId, dataType) + "&2. c:!");
			}

			event.setCancelled(true);
		}
		/////////////////////////////// time //////////////////////////// time ///////////////////////////////////
		if (Misc.is(base, sl + cmdArray[20]) && cmds.get(cmdArray[20])) {
			//20: time
			if (!General.Permissions.getHandler().permission(player, plugin.pBase + cmdArray[20])) { //general.time
				return;
			}

			long time = getTime(player);
			long timeRelative = getRelativeTime(player);
			long timeStart = getStartTime(player);

			if (split.length < 2) {
				int hours = (int) ((time / 1000 + 8) % 24);
				int minutes = (((int) (time % 1000)) / 1000) * 60;
				Messaging.send("&cTime: " + hours + ":" + minutes);
			} else if (split.length == 2) {
				String command = split[1];
				if (Misc.is(command, "help")) {
					Messaging.send("&c-------- " + sl + cmdArray[20] + " help --------");
					Messaging.send("&c" + sl + cmdArray[20] + " &f-&c Shows relative " + cmdArray[20]);
					Messaging.send("&c" + sl + cmdArray[20] + " day &f-&c Turns " + cmdArray[20] + " to day");
					Messaging.send("&c" + sl + cmdArray[20] + " night &f-&c Turns " + cmdArray[20] + " to night");
					Messaging.send("&c" + sl + cmdArray[20] + " raw &f-&c Shows raw " + cmdArray[20]);
					Messaging.send("&c" + sl + cmdArray[20] + " =13000 &f-&c Sets raw " + cmdArray[20]);
					Messaging.send("&c" + sl + cmdArray[20] + " +500 &f-&c Adds to raw " + cmdArray[20]);
					Messaging.send("&c" + sl + cmdArray[20] + " -500 &f-&c Subtracts from raw " + cmdArray[20]);
					Messaging.send("&c/" + sl + cmdArray[20] + " 12 &f-&c Set relative " + cmdArray[20]);
				} else if (Misc.is(command, "day")) {
					setTime(timeStart, player);
				} else if (Misc.is(command, "night")) {
					setTime(timeStart + 13000, player);
				} else if (Misc.is(command, "raw")) {
					Messaging.send("&cRaw:  " + time);
				} else if (command.startsWith("=")) {
					try {
						setTime(Long.parseLong(command.substring(1)), player);
					} catch (NumberFormatException ex) {
					}
				} else if (command.startsWith("+")) {
					try {
						setTime(time + Long.parseLong(command.substring(1)), player);
					} catch (NumberFormatException ex) {
					}
				} else if (command.startsWith("-")) {
					try {
						setTime(time - Long.parseLong(command.substring(1)), player);
					} catch (NumberFormatException ex) {
					}
				} else {
					try {
						timeRelative = (Integer.parseInt(command) * 1000 - 8000 + 24000) % 24000;
						setTime(timeStart + timeRelative, player);
					} catch (NumberFormatException ex) {
					}
				}
			} else {
				Messaging.send(corUse + sl + cmdArray[20] + " [day|night|raw|([=|+|-]" + cmdArray[20] + ")] (raw" + cmdArray[20] + ")");
				Messaging.send("&c" + sl + cmdArray[20] + " &f-&c Shows relative " + cmdArray[20]);
				Messaging.send("&c" + sl + cmdArray[20] + " day &f-&c Turns " + cmdArray[20] + " to day");
				Messaging.send("&c" + sl + cmdArray[20] + " night &f-&c Turns " + cmdArray[20] + " to night");
				Messaging.send("&c" + sl + cmdArray[20] + " raw &f-&c Shows raw " + cmdArray[20]);
				Messaging.send("&c" + sl + cmdArray[20] + " =13000 &f-&c Sets raw " + cmdArray[20]);
				Messaging.send("&c" + sl + cmdArray[20] + " +500 &f-&c Adds to raw " + cmdArray[20]);
				Messaging.send("&c" + sl + cmdArray[20] + " -500 &f-&c Subtracts from raw " + cmdArray[20]);
				Messaging.send("&c/" + sl + cmdArray[20] + " 12 &f-&c Set relative " + cmdArray[20]);
			}

			return;
		}
		///////////// playerlist ////////////////// online ///////////// who ///////// playerlist //////// online ///////// who ////////
		if ((Misc.is(base, sl + cmdArray[12]) && cmds.get(cmdArray[12])) || (Misc.is(base, sl + cmdArray[11]) && cmds.get(cmdArray[11])) || (Misc.is(base, sl + cmdArray[23]) && cmds.get(cmdArray[23]))) {
			//12: playerlist 11: online 23: who
			if (split.length == 2) {
				if (!General.Permissions.getHandler().permission(player, plugin.pBase + "player-info")) { //general.player-info
					return;
				}

				Player lookup = Misc.playerMatch(split[1]);
				String name = lookup.getName();
				String displayName = lookup.getDisplayName();
				String bar = "";
				String location = "";

				if (General.health) {
					int health = lookup.getHealth();
					int length = 10;
					int bars = Math.round(health / 2);
					int remainder = length - bars;
					String hb_color = ((bars >= 7) ? "&2" : ((bars < 7 && bars >= 3) ? "&e" : ((bars < 3) ? "&c" : "&2")));
					bar = " &f[" + hb_color + Misc.repeat('|', bars) + "&7" + Misc.repeat('|', remainder) + "&f]";
				}

				if (General.coords) {
					int x = (int) lookup.getLocation().getX();
					int y = (int) lookup.getLocation().getY();
					int z = (int) lookup.getLocation().getZ();
					location = x + "x, " + y + "y, " + z + "z";
				}

				Messaging.send("&f------------------------------------------------");
				Messaging.send("&e Player &f[" + name + "/" + displayName + "]&e Info:");
				Messaging.send("&f------------------------------------------------");
				Messaging.send("&6 Username: &f" + name + ((General.health) ? bar : ""));

				if (General.coords) {
					Messaging.send("&6 -&e Location: &f" + location);
				}

				Messaging.send("&6 -&e Status: &f" + ((isAFK(lookup)) ? "AFK (" + AFK.get(lookup) + ")" : "Around."));

				Messaging.send("&f------------------------------------------------");
			} else {
				ArrayList<Player> olist = new ArrayList<Player>();
				Player[] players = new Player[] {};

				for (Player p : plugin.getServer().getOnlinePlayers()) {
					if (p == null || !p.isOnline()) {
						continue;
					} else {
						olist.add(p);
					}
				}

				// Cast it to something empty to prevent nulls / empties
				players = olist.toArray(players);

				if (players.length <= 1 || olist.isEmpty()) {
					Messaging.send("&ePlayer list (1):");
					Messaging.send("&f - Just you.");
					Messaging.send(" ");
				} else {
					int online = players.length;
					ArrayList<String> list = new ArrayList<String>();
					String currently = "";
					int on = 0, perLine = 5, i = 0;

					for (Player current : players) {
						if (current == null) {
							++on;
							continue;
						}
						if (i == perLine) {
							list.add(currently);
							currently = "";
							i = 0;
						}

						currently += (++on >= online) ? current.getName() : current.getName() + ", ";
						++i;
					}

					// Always append the line to the list
					// because there may be extra that didn't get added
					// when i == perLine
					list.add(currently);

					Messaging.send("&ePlayers list (" + on + "):");

					for (String line : list) {
						Messaging.send(line);
					}

					Messaging.send(" ");
				}
			}
		}
	}
}

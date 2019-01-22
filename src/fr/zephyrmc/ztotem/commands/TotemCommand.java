package fr.zephyrmc.ztotem.commands;

import java.util.*;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import fr.zephyrmc.ztotem.*;
import fr.zephyrmc.ztotem.factionenvents.*;

public class TotemCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length > 0) {

			if (args.length == 1 && args[0].toLowerCase().equals("help")) {

				sender.sendMessage("§6-------------+[ §cZTotem command §2[<] 1/1 [>] §6]+-------------\n"
						+ "§b/totem create <event> §eCreate new Totem event.\n"
						+ "§b/totem start <event> §eStart totem event.\n"
						+ "§b/totem setspawn <event> here §eSet totem spawn on the player.\n"
						+ "§b/totem setspawn <event> X Y Z §eSet totem spawn on X Y Z coordinates.\n"
						+ "§b/totem stop <event> §eStop the totem event.\n"
						+ "§b/totem list §eList all existing event.\n"
						+ "§b/totem remove <event> §eRemove the totem event.\n"
						+ "§b/totem join <event> §eJoin the event.\n"
						+ "§b/totem quit <event> §eQuit the event.");
				return true;
			} else if (args.length == 2 && args[0].toLowerCase().equals("create")) {

				if (!args[1].equals("allEvents")) {

					ZTotem.mainPlugin.getConfig().createSection(args[1]);
					ZTotem.mainPlugin.getConfig().createSection(args[1] + "_fac");
					ZTotem.mainPlugin.saveConfig();
					sender.sendMessage("§2The totem event \"§e" + args[1] + "§2\" is create, now set the spawn.");
					return true;
				} else {

					sender.sendMessage("§4The name \"allEvents\" is used by the system.");
					return true;
				}

			} else if (args.length == 3 && args[0].toLowerCase().equals("setspawn")
					&& args[2].toLowerCase().equals("here") && sender instanceof Player) {
				Player player = (Player) sender;

				if (ZTotem.mainPlugin.getConfig().contains(args[1])) {

					ZTotem.mainPlugin.getConfig().set(args[1],
							player.getLocation().getBlockX() + "/" + player.getLocation().getBlockY() + "/"
									+ player.getLocation().getBlockZ() + "/" + player.getWorld().getName() + "/false");

					List<String> allEvents = ZTotem.mainPlugin.getConfig().getStringList("allEvents");
					allEvents.add(args[1]);
					ZTotem.mainPlugin.getConfig().set("allEvents", allEvents);
					ZTotem.mainPlugin.saveConfig();

					sender.sendMessage("§2The totem event \"§e" + args[1] + "§2\" sapwn is now set.");
					return true;
				} else {

					sender.sendMessage("§4The §e" + args[1] + " §4event doesn't exist.");
					return true;
				}
			} else if (args.length == 1 && args[0].toLowerCase().equals("list")) {

				for (String event : ZTotem.mainPlugin.getConfig().getStringList("allEvents")) {

					sender.sendMessage("§e" + event);
				}
				return true;
			} else if (args.length == 2 && args[0].toLowerCase().equals("remove")) {

				List<String> allEvents = ZTotem.mainPlugin.getConfig().getStringList("allEvents");
				if (allEvents.remove(args[1])) {

					ZTotem.mainPlugin.getConfig().set("allEvents", allEvents);
					ZTotem.mainPlugin.getConfig().set(args[1], null);
					ZTotem.mainPlugin.saveConfig();
					sender.sendMessage("§2The totem event \"§e" + args[1] + "§2\" spawn is removed.");
					return true;
				} else {

					sender.sendMessage("§4The §e" + args[1] + " §4event doesn't exist.");
					return true;
				}
			} else if (args.length == 2 && args[0].toLowerCase().equals("start")) {

				if (ZTotem.mainPlugin.getConfig().contains(args[1])) {

					String infos = ZTotem.mainPlugin.getConfig().get(args[1]).toString();
					if (infos.contains("false")) {

						infos = infos.replace("false", "true");
						ZTotem.mainPlugin.getConfig().set(args[1], infos);
						ZTotem.mainPlugin.saveConfig();

						String[] strinfos = infos.split("/");
						ArrayList<Integer> coords = new ArrayList<Integer>();
						for (int i = 0; i < strinfos.length - 2; i++)
							coords.add(Integer.parseInt(strinfos[i]));

						for (int i = 0; i <= 5; i++) {
							Location blockLoc = new Location(Bukkit.getWorld(strinfos[3]), coords.get(0),
									coords.get(1) + i, coords.get(2));

							Bukkit.getWorld(strinfos[3]).getBlockAt(blockLoc).setType(Material.QUARTZ_BLOCK);
							blockLoc.getWorld().playEffect(blockLoc, Effect.EXPLOSION_HUGE, 400);
						}

						Location soundLoc = new Location(Bukkit.getWorld(strinfos[3]), coords.get(0), coords.get(1),
								coords.get(2));
						soundLoc.getWorld().playSound(soundLoc, Sound.EXPLODE, 3.0F, 0.533F);

						Bukkit.broadcastMessage("§aThe event \"§e" + args[1] + "§a\" is started.");
						return true;
					} else {

						sender.sendMessage("§4The §e" + args[1] + " §4event is already started.");
						return true;
					}
				} else {

					sender.sendMessage("§4The §e" + args[1] + " §4event doesn't exist.");
					return true;
				}
			} else if (args.length == 2 && args[0].toLowerCase().equals("stop")) {

				if (ZTotem.mainPlugin.getConfig().contains(args[1])) {

					String infos = ZTotem.mainPlugin.getConfig().get(args[1]).toString();
					if (infos.contains("true")) {

						infos = infos.replace("true", "false");
						ZTotem.mainPlugin.getConfig().set(args[1], infos);
						ZTotem.mainPlugin.saveConfig();

						String[] strinfos = infos.split("/");
						ArrayList<Integer> coords = new ArrayList<Integer>();
						for (int i = 0; i < strinfos.length - 2; i++)
							coords.add(Integer.parseInt(strinfos[i]));

						for (int i = 0; i <= 5; i++) {

							Location blockLoc = new Location(Bukkit.getWorld(strinfos[3]), coords.get(0),
									coords.get(1) + i, coords.get(2));

							Bukkit.getWorld(strinfos[3]).getBlockAt(blockLoc).setType(Material.AIR);
							blockLoc.getWorld().playEffect(blockLoc, Effect.EXPLOSION_HUGE, 400);
						}

						Location soundLoc = new Location(Bukkit.getWorld(strinfos[3]), coords.get(0), coords.get(1),
								coords.get(2));
						soundLoc.getWorld().playSound(soundLoc, Sound.EXPLODE, 3.0F, 0.533F);

						Bukkit.broadcastMessage("§aThe event \"§e" + args[1] + "§a\" is stopped.");
						return true;
					} else {

						sender.sendMessage("§4The §e" + args[1] + " §4event is already stoped.");
						return true;
					}
				} else {

					sender.sendMessage("§4The §e" + args[1] + " §4event doesn't exist.");
					return true;
				}
			} else if (args.length == 2 && args[0].toLowerCase().equals("join")) {

				if (ZTotem.mainPlugin.getConfig().contains(args[1]) && sender instanceof Player) {

					Player player = (Player) sender;

					if (!ZTotem.mainPlugin.getConfig().getString(args[1] + "_fac").equals(null)) {

						ZTotem.mainPlugin.getConfig().set(args[1] + "_fac",
								new FactionPlayer(player).getFactionName() + ":" + player.getDisplayName());
						ZTotem.mainPlugin.saveConfig();
						return true;
					} else {

						String infos = ZTotem.mainPlugin.getConfig().getString(args[1] + "_fac");
						if (infos.contains(player.getDisplayName())) {

							infos = infos + "/" + player.getDisplayName();
							ZTotem.mainPlugin.getConfig().set(args[1] + "_fac", infos);
							return true;
						} else {

							sender.sendMessage("§4Already join this event.");
							return true;
						}
					}
				} else {

					sender.sendMessage("§4The §e" + args[1] + " §4event doesn't exist.");
					return true;
				}
			} else if (args.length == 2 && args[0].toLowerCase().equals("quit")) {
				
				if(ZTotem.mainPlugin.)
			} else {

				return false;
			}
		} else {

			return false;
		}
	}

}
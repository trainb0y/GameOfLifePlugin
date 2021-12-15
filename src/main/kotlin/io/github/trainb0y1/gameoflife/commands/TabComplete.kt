package io.github.trainb0y1.gameoflife.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class TabComplete : TabCompleter {
	override fun onTabComplete(
		sender: CommandSender,
		cmd: Command,
		alias: String,
		args: Array<String>
	): MutableList<String> {
		val subcommands = mutableListOf<String>()
		when (args.size) {
			1 -> {
				// /gol <x>
				if (sender.hasPermission("gameoflife.create")) {
					subcommands.add("create")
				}
			}
		}
		return subcommands
	}
}
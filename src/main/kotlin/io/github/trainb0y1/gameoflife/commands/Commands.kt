package io.github.trainb0y1.gameoflife.commands

import io.github.trainb0y1.gameoflife.GameBoard
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Commands : CommandExecutor {
	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
		if (args.isEmpty()) {
			sender.sendMessage("Please input at least one argument!")
			return false
		}
		when (args[0].lowercase()) {
			"create" -> {
				if (args.size < 2) {
					sender.sendMessage("Please input a valid integer!")
					return false
				}
				try {
					GameBoard((sender as Player).location, args[1].toInt())
					sender.sendMessage("Create game board at ${sender.location} with size ${args[1]}")
					return true
				} catch (e: ClassCastException) {
					sender.sendMessage("Only players can use this command!")
				} catch (e: NumberFormatException) {
					sender.sendMessage("Please input a valid integer!")
				}
				return false
			}
			else -> {
				sender.sendMessage("Unknown command!")
				return false
			}
		}
	}
}
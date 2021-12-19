package io.github.trainb0y1.gameoflife.commands

import io.github.trainb0y1.gameoflife.GameBoard
import io.github.trainb0y1.gameoflife.GameOfLife.Companion.plugin
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
				if (!sender.hasPermission("gameoflife.create")) {
					sender.sendMessage("You don't have permission to create a game of life board!!")
					return false
				}
				if (args.size < 2) {
					sender.sendMessage("Please input a valid integer!")
					return false
				}
				try {
					GameBoard((sender as Player).location.toBlockLocation(), args[1].toInt(), sender)
					sender.sendMessage("Create game board at ${sender.location} with size ${args[1]}")
					return true
				} catch (e: ClassCastException) {
					sender.sendMessage("Only players can use this command!")
				} catch (e: NumberFormatException) {
					sender.sendMessage("Please input a valid integer!")
				}
				return false
			}
			"destroy" -> {
				if (!sender.hasPermission("gameoflife.destroy")) {
					sender.sendMessage("You don't have permission to use this command!")
					return false
				}
				return try {
					val board: GameBoard? = plugin.boards.remove((sender as Player).uniqueId)
					if (board == null) {
						sender.sendMessage("You don't have an active board!")
						return true
					}
					board.onDestroy()
					sender.sendMessage("Destroyed game board at ${board.origin.toString()} with size ${board.size}")
					true
				}
				catch (e: ClassCastException) {
					sender.sendMessage("Only players can use this command!")
					false
				}
			}
			"translate" -> {
				return try {
					sender.sendMessage(plugin.boards[(sender as Player).uniqueId]?.toCoordinate(sender.location).toString())
					true
				} catch (e: ClassCastException) {
					sender.sendMessage("Only players can use this command!")
					false
				}
			}
			else -> {
				sender.sendMessage("Unknown command!")
				return false
			}
		}
	}
}
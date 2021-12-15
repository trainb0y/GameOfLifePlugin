package io.github.trainb0y1.gameoflife

import io.github.trainb0y1.gameoflife.commands.Commands
import io.github.trainb0y1.gameoflife.commands.TabComplete
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class GameOfLife : JavaPlugin() {
	companion object {
		lateinit var plugin: GameOfLife
			private set
	}

	override fun onEnable() {
		plugin = this
		plugin.getCommand("gol")!!.setExecutor(Commands())
		plugin.getCommand("gol")!!.tabCompleter = TabComplete()

	}
}
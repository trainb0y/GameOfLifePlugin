package io.github.trainb0y1.gameoflife

import org.bukkit.scheduler.BukkitRunnable

class GameRunnable(private val board: GameBoard): BukkitRunnable() {
	override fun run() {
		if (board.autoAdvance){
			board.updateCellsFromWorld()
			board.nextGeneration()
			board.updateWorld()
		}
	}
}
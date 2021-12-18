package io.github.trainb0y1.gameoflife

import io.github.trainb0y1.gameoflife.GameOfLife.Companion.plugin
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class GameBoard(val origin: Location, val size: Int, player: Player): Listener {
	var cells = mutableMapOf<Coordinate, Boolean>()
	val liveBlock = Material.BLACK_WOOL
	var autoAdvance = false
	var controller = Location(origin.world,origin.x - 1, origin.y, origin.z - 1)


	init {
		controller.block.type = Material.LAPIS_BLOCK
		GameRunnable(this).runTaskTimer(GameOfLife.plugin, 0, 5)
		plugin.server.pluginManager.registerEvents(this, plugin)
	}

	private fun place(coordinate: Coordinate, block: Material) {
		Location(origin.world, origin.x + coordinate.x, origin.y, origin.z + coordinate.y).block.type = block
	}
	fun get (coordinate: Coordinate): Material {
		return Location(origin.world, origin.x + coordinate.x, origin.y, origin.z + coordinate.y).block.type
	}

	fun updateCellsFromWorld() {
		for (x in 0..size){
			for (y in 0..size) {
				cells[Coordinate(x,y)] = get(Coordinate(x,y)) == liveBlock
			}
		}
	}

	fun updateWorld() {
		// Update the board with the current cells
		for (x in 0..size){
			for (y in 0..size) {
				place(Coordinate(x,y), if (cells[Coordinate(x,y)] == true) liveBlock else Material.AIR)
			}
		}
	}

	@EventHandler
	fun onClickController(event: PlayerInteractEvent) {
		if (event.action == Action.LEFT_CLICK_BLOCK) {
			if (event.clickedBlock!!.location == controller) {
				event.player.sendMessage("One generation forward")
				updateCellsFromWorld()
				nextGeneration()
				updateWorld()
				event.isCancelled = true
			}
		}
		if (event.action == Action.RIGHT_CLICK_BLOCK) {
			if (event.clickedBlock!!.location == controller) {
				autoAdvance = !autoAdvance
				event.player.sendMessage("Auto-advance: ${if (autoAdvance) "on" else "off"}")
				event.isCancelled = true
			}
		}
	}

	fun getAliveNeighborCount(coordinate: Coordinate): Int {
		var count = 0
		for (x in coordinate.x - 1..coordinate.x + 1) {
			for (y in coordinate.y - 1..coordinate.y + 1) {
				if (cells[Coordinate(x,y)] == true && Coordinate(x,y) != coordinate) {
					count++
				}
			}
		}
		return count
	}

	fun nextGeneration() {
		// Calculate the next generation
		cells.forEach{ (coord, state) ->
			when (state) {
				false -> {
					when (getAliveNeighborCount(coord)) {
						3 -> cells[coord] = true
						else -> cells[coord] = false
					}
				}
				true -> {
					when (getAliveNeighborCount(coord)) {
						2, 3 -> cells[coord] = true
						else -> cells[coord] = false
					}
				}
			}
		}
	}
}
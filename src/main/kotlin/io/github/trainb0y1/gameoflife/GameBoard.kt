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
	private val liveBlock = Material.BLACK_WOOL
	var autoAdvance = false
	var controller = Location(origin.world,origin.x - 1, origin.y, origin.z - 1).block.location // rounds?
	val runnable = GameRunnable(this)


	init {
		controller.block.type = Material.LAPIS_BLOCK
		runnable.runTaskTimer(GameOfLife.plugin, 0, 5)
		plugin.server.pluginManager.registerEvents(this, plugin)
		plugin.boards[player.uniqueId] = this
	}

	fun toCoordinate(location: Location): Coordinate {
		return Coordinate(location.blockX - origin.blockX, location.blockZ - origin.blockZ)
	}

	fun toLocation(coordinate: Coordinate): Location {
		return Location(origin.world, origin.x + coordinate.x, origin.y, origin.z + coordinate.y)
	}

	private fun place(coordinate: Coordinate, block: Material) {
		toLocation(coordinate).block.type = block
	}
	fun get (coordinate: Coordinate): Material {
		return toLocation(coordinate).block.type
	}

	fun updateCellsFromWorld() {
		for (x in 0..size){
			for (y in 0..size) {
				cells[Coordinate(x,y)] = (get(Coordinate(x,y)) == liveBlock)
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
		val newCells = mutableMapOf<Coordinate, Boolean>()
		cells.forEach{ (coord, state) ->
			when (state) {
				false -> {
					newCells[coord] = getAliveNeighborCount(coord) == 3
				}
				true -> {
					when (getAliveNeighborCount(coord)) {
						2, 3 -> newCells[coord] = true
						else -> newCells[coord] = false
					}
				}
			}
		}
		cells = newCells
	}

	fun onDestroy() {
		// Called when the player destroys the board
		controller.block.type = Material.AIR
		runnable.cancel()
	}
}
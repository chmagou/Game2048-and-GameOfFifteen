package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer): Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        board.addValues(initializer)
    }

    override fun canMove(): Boolean {
        return true
    }

    override fun hasWon(): Boolean {

        var counter = 1

        for (i in 1..board.width) {
            for (j in 1..board.width) {
                var tileValue = board[board.getCell(i, j)]
                if (counter <= 15) {
                    if (tileValue != counter) {
                        return false
                    }
                    counter++
                }
            }
        }

        return true
    }

    override fun processMove(direction: Direction) {
        board.moveTiles(direction)
    }

    override fun get(i: Int, j: Int): Int? {
       return board.run { get(getCell(i,j)) }
    }


}

private fun GameBoard<Int?>.moveTiles(direction: Direction) {

    val emptyCell   = this.getAllCells().filter { it -> this[it] == null }

    when (direction) {

        Direction.UP -> {
            val tile = this.getCell(emptyCell.get(0).i+1, emptyCell.get(0).j)
            swapTiles(tile, emptyCell)
        }
        Direction.DOWN -> {
            val tile = this.getCell(emptyCell.get(0).i-1, emptyCell.get(0).j)
            swapTiles(tile, emptyCell)
        }
        Direction.RIGHT -> {
            val tile = this.getCell(emptyCell.get(0).i, emptyCell.get(0).j-1)
            swapTiles(tile, emptyCell)
        }
        Direction.LEFT -> {
            val tile = this.getCell(emptyCell.get(0).i, emptyCell.get(0).j+1)
            swapTiles(tile, emptyCell)
        }

    }

}

private fun GameBoard<Int?>.swapTiles(tile: Cell, empty: List<Cell>) {

    val temp = this[tile]
    this[tile] = this[empty.get(0)]
    this[empty.get(0)] = temp

}

private fun GameBoard<Int?>.addValues(initializer: GameOfFifteenInitializer) {

    val data = initializer.initialPermutation
    var counter = 0

    for (i in 1..width) {
        for ( j in 1..width) {
            var value: Int?
            if (counter < 15) {
                value = data[counter]
                counter++
            } else {
                value = null
            }
            this[this.getCell(i,j)] = value
        }
    }

}
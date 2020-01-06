package board

import board.Direction.*
import java.lang.IllegalArgumentException


open class SquareBoardImpl(override val width: Int) : SquareBoard {

    var cellArray : Array<Array<Cell>> = arrayOf(arrayOf())

    override fun getCellOrNull(i: Int, j: Int): Cell? {

        return when {
            (i > width || j > width || i == 0 || j == 0) -> null
            else -> getCell(i,j)
        }

    }

    override fun getCell(i: Int, j: Int): Cell {

        return when {
            (i > width || j > width || i == 0 || j == 0) -> throw IllegalArgumentException("Incorrect values")
            else -> cellArray[i - 1][j - 1]
        }

    }

    override fun getAllCells(): Collection<Cell> = (1..width).flatMap { i -> (1..width).map { j -> getCell(i,j) } }.toList()

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {

        return when {
            jRange.last > width -> (jRange.first..width).map { j -> getCell(i,j) }.toList()
            else -> jRange.map { j -> getCell(i,j) }.toList()
        }

    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {

        return when {
            iRange.last > width -> (iRange.first..width).map { i -> getCell(i,j) }.toList()
            else -> iRange.map { i -> getCell(i,j) }.toList()
        }

    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {

        return when (direction) {
            UP -> getCellOrNull(i-1, j)
            DOWN -> getCellOrNull(i+1, j)
            RIGHT -> getCellOrNull(i, j+1)
            LEFT -> getCellOrNull(i, j-1)
        }

    }
}

fun createSquareBoard(width: Int): SquareBoard {

    val squareBoard = SquareBoardImpl(width)
    squareBoard.cellArray = createEmptyBoard(width)
    //print("the square board ${squareBoard.getAllCells()} ")
    return squareBoard

}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {

    var cellMap : MutableMap<Cell, T?> = mutableMapOf<Cell, T?>()


    override operator fun get(cell: Cell): T? = cellMap[cell]

    override operator fun set(cell: Cell, value: T?)  {
        cellMap[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {

        return cellMap.filterValues { predicate.invoke(it) }.keys

    }

    override fun find(predicate: (T?) -> Boolean): Cell? {

        return cellMap.filter { predicate.invoke(it.value) }.keys.first()

    }

    override fun any(predicate: (T?) -> Boolean): Boolean {

        return cellMap.values.any(predicate)

    }

    override fun all(predicate: (T?) -> Boolean): Boolean {

        return cellMap.values.all(predicate)

    }
}

fun <T> createGameBoard(width: Int): GameBoard<T> {

    val gameBoard = GameBoardImpl<T>(width)
    gameBoard.cellArray = createEmptyBoard(width)
    gameBoard.cellArray.forEach { it.forEach { cell: Cell -> gameBoard.cellMap[cell] = null } }

    return gameBoard
}


fun createEmptyBoard(width: Int): Array<Array<Cell>> {
    var board = arrayOf<Array<Cell>>()

    for (i in 1..width) {
        var array = arrayOf<Cell>()
        for (j in 1..width) {
            array += Cell(i, j)
        }
        board += array
    }
    return board
}

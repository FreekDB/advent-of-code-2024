package com.github.freekdb.aoc.xxiv


fun main() {
    val verbose = false
    val part = 2
    val descriptions = listOf(
        "The number of unique locations within the bounds of the map that contain an antinode is",
        "The number of unique locations within the bounds of the map that contain an antinode is"
    )

//    Day08(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
    Day08(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Part 1 -- The number of unique locations within the bounds of the map that contain an antinode is: 364.
    // Part 2 -- The number of unique locations within the bounds of the map that contain an antinode is: 1231.
}


class Day08(verbose: Boolean) : BasePuzzleSolver(verbose) {
    private lateinit var map: Array<Array<Char>>
    private lateinit var antiNodes: Array<Array<Char>>

    override fun solvePart1(inputLines: List<String>): Long {
        map = initializeMap(inputLines)
        antiNodes = Array(map.size) { Array(map[0].size) { ' ' } }

        if (verbose) {
            val uniqueCharacters = map.flatten().toSet().filter { it != '.' }
            println("uniqueCharacters: $uniqueCharacters")
        }

        val characterMaps = map.mapIndexed { rowIndex, mapRow ->
            mapRow.mapIndexedNotNull { columnIndex, cellCharacter ->
                if (cellCharacter != '.') cellCharacter to Cell(rowIndex, columnIndex) else null
            }
        }.flatten()
            .groupBy { it.first }
            .map { entry -> entry.key to entry.value.map { it.second } }
            .toMap()

        if (verbose) {
            println("characterMaps: {")
            characterMaps.forEach { (character, cellList) -> println("$character -> $cellList") }
            println("}")
        }

        val mapRowRange = map.indices
        val mapColumnRange = map[0].indices

        characterMaps.forEach { (_, cellList) ->
            cellList.indices.forEach { startIndex ->
                val startCell = cellList[startIndex]

                ((startIndex + 1)..<cellList.size).forEach { endIndex ->
                    val endCell = cellList[endIndex]
                    val stepRows = endCell.rowIndex - startCell.rowIndex
                    val stepColumns = endCell.columnIndex - startCell.columnIndex

                    val beforeRowIndex = startCell.rowIndex - stepRows
                    val beforeColumnIndex = startCell.columnIndex - stepColumns
                    if (beforeRowIndex in mapRowRange && beforeColumnIndex in mapColumnRange) {
                        antiNodes[beforeRowIndex][beforeColumnIndex] = '#'
                    }

                    val afterRowIndex = endCell.rowIndex + stepRows
                    val afterColumnIndex = endCell.columnIndex + stepColumns
                    if (afterRowIndex in mapRowRange && afterColumnIndex in mapColumnRange) {
                        antiNodes[afterRowIndex][afterColumnIndex] = '#'
                    }
                }
            }
        }

        return antiNodes.sumOf { antiNodesRow -> antiNodesRow.count { it == '#' } }.toLong()
    }

    override fun solvePart2(inputLines: List<String>): Long {
        map = initializeMap(inputLines)
        antiNodes = Array(map.size) { Array(map[0].size) { ' ' } }

        val characterMaps = map.mapIndexed { rowIndex, mapRow ->
            mapRow.mapIndexedNotNull { columnIndex, cellCharacter ->
                if (cellCharacter != '.') cellCharacter to Cell(rowIndex, columnIndex) else null
            }
        }.flatten()
            .groupBy { it.first }
            .map { entry -> entry.key to entry.value.map { it.second } }
            .toMap()

        if (verbose) {
            println("characterMaps: {")
            characterMaps.forEach { (character, cellList) -> println("$character -> $cellList") }
            println("}")
        }

        val mapRowRange = map.indices
        val mapColumnRange = map[0].indices

        characterMaps.forEach { (_, cellList) ->
            cellList.indices.forEach { startIndex ->
                val startCell = cellList[startIndex]

                ((startIndex + 1)..<cellList.size).forEach { endIndex ->
                    val endCell = cellList[endIndex]
                    val stepRows = endCell.rowIndex - startCell.rowIndex
                    val stepColumns = endCell.columnIndex - startCell.columnIndex

                    var beforeRowIndex = startCell.rowIndex
                    var beforeColumnIndex = startCell.columnIndex
                    while (beforeRowIndex in mapRowRange && beforeColumnIndex in mapColumnRange) {
                        antiNodes[beforeRowIndex][beforeColumnIndex] = '#'
                        beforeRowIndex -= stepRows
                        beforeColumnIndex -= stepColumns
                    }

                    var afterRowIndex = endCell.rowIndex
                    var afterColumnIndex = endCell.columnIndex
                    while (afterRowIndex in mapRowRange && afterColumnIndex in mapColumnRange) {
                        antiNodes[afterRowIndex][afterColumnIndex] = '#'
                        afterRowIndex += stepRows
                        afterColumnIndex += stepColumns
                    }
                }
            }
        }

        return antiNodes.sumOf { antiNodesRow -> antiNodesRow.count { it == '#' } }.toLong()
    }

    private fun initializeMap(inputLines: List<String>): Array<Array<Char>> {
        val mapHeight = inputLines.size
        val mapWidth = (inputLines.maxOfOrNull { line -> line.length } ?: 0)

        val map = Array(mapHeight) { Array(mapWidth) { ' ' } }

        inputLines.forEachIndexed { rowIndex, inputLine ->
            inputLine.forEachIndexed { columnIndex, cellCharacter ->
                map[rowIndex][columnIndex] = cellCharacter
            }
        }

        return map
    }

    override val inputLinesExamples = listOf(
        """
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
"""
    )


    data class Cell(val rowIndex: Int, val columnIndex: Int)
}

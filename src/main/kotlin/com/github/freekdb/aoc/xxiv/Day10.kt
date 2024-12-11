package com.github.freekdb.aoc.xxiv


fun main() {
    val verbose = false
    val part = 2
    val descriptions = listOf(
        "The sum of the scores of all trailheads on the topographic map is",
        "The sum of the ratings of all trailheads is"
    )

//    Day10(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
//    Day10(verbose).solvePuzzle(part, inputId = "example 2", descriptions)
//    Day10(verbose).solvePuzzle(part, inputId = "example 3", descriptions)
//    Day10(verbose).solvePuzzle(part, inputId = "example 4", descriptions)
    Day10(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Part 1 -- The sum of the scores of all trailheads on the topographic map is: 733.
    // Part 2 -- The sum of the ratings of all trailheads is: 1514.
}


class Day10(verbose: Boolean) : BasePuzzleSolver(verbose) {
    override fun solvePart1(inputLines: List<String>): Long {
        val map = initializeMap(inputLines)

        val trailheads = map.mapIndexed { rowIndex, mapRow ->
            mapRow.mapIndexedNotNull { columnIndex, cellValue ->
                if (cellValue == 0) Cell(rowIndex, columnIndex) else null
            }
        }.flatten()

        if (verbose) {
            println("trailheads: $trailheads")
            println()
        }

        val sumOfScores = trailheads.sumOf { trailhead ->
            var currentSet = setOf(trailhead)

            if (verbose) {
                println("Level 0 -> currentSet: $currentSet")
            }

            val nextSet = mutableSetOf<Cell>()
            var level = 1
            while (level <= 9) {
                val nextSetsPerCell = currentSet.map { currentCell -> findNextCells(map, currentCell, level) }
                nextSetsPerCell.forEach { nextSetPerCell -> nextSet.addAll(nextSetPerCell) }

                level += 1
                currentSet = nextSet.toSet()
                nextSet.clear()

                if (verbose) {
                    println("Level ${level - 1} -> currentSet: $currentSet")
                }
            }

            if (verbose) {
                println()
            }

            currentSet.size
        }

        return sumOfScores.toLong()
    }

    override fun solvePart2(inputLines: List<String>): Long {
        val map = initializeMap(inputLines)

        val trailheads = map.mapIndexed { rowIndex, mapRow ->
            mapRow.mapIndexedNotNull { columnIndex, cellValue ->
                if (cellValue == 0) Cell(rowIndex, columnIndex) else null
            }
        }.flatten()

        val sumOfRatings = trailheads.sumOf { trailhead ->
            var currentSet = setOf(trailhead)
            val distinctTrails = mutableListOf(mutableListOf(trailhead))

            if (verbose) {
                println("Level 0 -> currentSet: $currentSet")
                println("           distinctTrails: $distinctTrails")
                println()
            }

            val nextSet = mutableSetOf<Cell>()
            var level = 1
            while (level <= 9) {
                val nextSetsPerCell = currentSet.map { currentCell ->
                    val nextCells = findNextCells(map, currentCell, level)

                    // Update the distinct trails.
                    val currentCellTrails = distinctTrails.filter { it.last() == currentCell }
                    distinctTrails.removeAll(currentCellTrails)
                    currentCellTrails.forEach { currentCellTrail ->
                        nextCells.forEach { nextCell ->
                            distinctTrails.add(currentCellTrail.toMutableList().apply { add(nextCell) })
                        }
                    }

                    nextCells
                }

                nextSetsPerCell.forEach { nextSetPerCell -> nextSet.addAll(nextSetPerCell) }

                level += 1
                currentSet = nextSet.toSet()
                nextSet.clear()

                if (verbose) {
                    println("Level ${level - 1} -> currentSet: $currentSet")
                    println("           distinctTrails: $distinctTrails")
                    println()
                }
            }

            distinctTrails.size
        }

        return sumOfRatings.toLong()
    }

    private fun initializeMap(inputLines: List<String>): Array<Array<Int>> {
        // Use a border around the actual map to prevent index out of range errors.
        val borderSize = 1

        val mapHeight = 2 * borderSize + inputLines.size
        val mapWidth = 2 * borderSize + (inputLines.maxOfOrNull { line -> line.length } ?: 0)

        val map = Array(mapHeight) { Array(mapWidth) { -1 } }

        inputLines.forEachIndexed { rowIndex, inputLine ->
            inputLine.forEachIndexed { columnIndex, cellCharacter ->
                map[borderSize + rowIndex][borderSize + columnIndex] = cellCharacter - '0'
            }
        }

        return map
    }

    private fun findNextCells(map: Array<Array<Int>>, currentCell: Cell, level: Int): Set<Cell> {
        val directions = listOf(Pair(-1, 0), Pair(0, -1), Pair(0, 1), Pair(1, 0))

        return directions.mapNotNull { direction ->
            if (map[currentCell.rowIndex + direction.first][currentCell.columnIndex + direction.second] == level) {
                Cell(rowIndex = currentCell.rowIndex + direction.first, columnIndex = currentCell.columnIndex + direction.second)
            } else {
                null
            }
        }.toSet()
    }

    override val inputLinesExamples = listOf(
        """
0123
1234
8765
9876
""",
        """
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
""",
        """
.....0.
..4321.
..5..2.
..6543.
..7..4.
..8765.
..9....
""",
        """
012345
123456
234567
345678
4.6789
56789.
"""
    )


    data class Cell(val rowIndex: Int, val columnIndex: Int)
}

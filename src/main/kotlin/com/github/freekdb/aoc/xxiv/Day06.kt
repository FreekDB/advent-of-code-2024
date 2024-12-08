package com.github.freekdb.aoc.xxiv


fun main() {
    val verbose = false
    val part = 2
    val descriptions = listOf(
        "The number of distinct positions that the guard will visit before leaving the mapped area is",
        "The number of different positions that you can choose for a loop causing obstruction is"
    )

//    Day06(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
    Day06(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Copy the results here to register the (in)correct answers that you submitted...
    // Part 1 -- The number of distinct positions that the guard will visit before leaving the mapped area is: 4778.
    // Part 2 -- The number of different positions that you can choose for a loop causing obstruction is: 333.
    // That's not the right answer; your answer is too low.
    // Part 2 -- The number of different positions that you can choose for a loop causing obstruction is: 1585.
    // That's not the right answer; your answer is too low.
    // Part 2 -- The number of different positions that you can choose for a loop causing obstruction is: 1618.
}


class Day06(verbose: Boolean) : BasePuzzleSolver(verbose) {
    private lateinit var puzzle: Array<Array<Char>>
    private val visited = mutableSetOf<Pair<Position, Char>>()
    private lateinit var loopDirectionsHorizontal: Array<Array<Char>>
    private lateinit var loopDirectionsVertical: Array<Array<Char>>

    override fun solvePart1(inputLines: List<String>): Long {
        puzzle = initializePuzzle(inputLines)

        val startPosition = findGuard()

        if (verbose) {
            printPuzzle()
            println("startPosition: $startPosition")
        }

        var position = startPosition
        while (puzzle[position.rowIndex][position.columnIndex] != ' ') {
            position = moveToNextPosition(position)

            if (verbose) {
                printPuzzle()
                println("position: $position")
            }
        }

        return countDistinctPositions()
    }

    // Brute force attempt.
    // Inspired by https://www.youtube.com/live/f7hsZgRakDU?si=_UQ1xpjhPOXA4pO8, reddit and
    // https://github.com/Jadarma/advent-of-code-kotlin-solutions/blob/main/solutions/aockt/y2024/Y2024D06.kt
    override fun solvePart2(inputLines: List<String>): Long {
        var loopCount = 0L
        val obstacles = mutableListOf<Pair<Long, Long>>()
        val initialPuzzle = initializePuzzle(inputLines)

        initialPuzzle.forEachIndexed { rowIndex, puzzleRow ->
            puzzleRow.forEachIndexed { columnIndex, cellCharacter ->
                if (cellCharacter == '.') {
                    puzzle = initializePuzzle(inputLines)
                    puzzle[rowIndex][columnIndex] = '#'

                    visited.clear()

                    val startPosition = findGuard()

                    if (verbose) {
                        printPuzzle()
                        println("startPosition: $startPosition")
                    }

                    var loopDetected = false
                    var position = startPosition
                    var direction = puzzle[position.rowIndex][position.columnIndex]
                    visited.add(Pair(position, direction))

                    while (!loopDetected && puzzle[position.rowIndex][position.columnIndex] != ' ') {
                        position = moveToNextPosition(position)
                        direction = puzzle[position.rowIndex][position.columnIndex]
                        if (!visited.contains(Pair(position, direction))) {
                            visited.add(Pair(position, direction))
                        } else {
                            // Loop detected!
                            loopDetected = true
                            loopCount++

                            // Remove initializePuzzle | borderSize from the coordinates and convert rowIndex to y.
                            obstacles.add(Pair(columnIndex - 1L, (puzzle.size - 2 * 1L) - rowIndex))
                        }

                        if (verbose) {
                            printPuzzle()
                            println("position: $position")
                        }
                    }
                }
            }
        }

        obstacles.sortBy { 1000000 * it.second + it.first }
        obstacles.forEach { println(it) }

        return loopCount
    }

    fun solvePart2IncorrectFirstAttempt(inputLines: List<String>): Long {
        puzzle = initializePuzzle(inputLines)
        loopDirectionsHorizontal = Array(puzzle.size) { Array(puzzle[0].size) { ' ' } }
        loopDirectionsVertical = Array(puzzle.size) { Array(puzzle[0].size) { ' ' } }

        val startPosition = findGuard()

        if (verbose) {
            printPuzzle()
            println("startPosition: $startPosition")
        }

        var position = startPosition
        var direction = puzzle[position.rowIndex][position.columnIndex]
        val obstructions = mutableListOf<Position>()
        while (puzzle[position.rowIndex][position.columnIndex] != ' ') {
            val result = moveToNextPositionPart2(position, direction, obstructions)
            position = result.first
            direction = result.second

            if (verbose) {
                printPuzzle()
                println("position: $position")
                println("direction: $direction")
                println("obstructions: $obstructions")
                println()
            }
        }

        return obstructions.size.toLong()
    }

    private fun findGuard(): Position {
        puzzle.forEachIndexed { rowIndex, puzzleRow ->
            puzzleRow.forEachIndexed { columnIndex, cellCharacter ->
                if (cellCharacter !in listOf(' ', '#', '.')) {
                    return Position(rowIndex, columnIndex)
                }
            }
        }

        return Position(0, 0)
    }

    private fun moveToNextPosition(currentPosition: Position): Position {
        var direction = puzzle[currentPosition.rowIndex][currentPosition.columnIndex]
        var nextPosition = determineNextPosition(currentPosition, direction)

        if (puzzle[nextPosition.rowIndex][nextPosition.columnIndex] == ' ') {
            puzzle[currentPosition.rowIndex][currentPosition.columnIndex] = 'X'

            return nextPosition
        } else if (puzzle[nextPosition.rowIndex][nextPosition.columnIndex] == '#') {
            puzzle[currentPosition.rowIndex][currentPosition.columnIndex] =
                when (puzzle[currentPosition.rowIndex][currentPosition.columnIndex]) {
                    '^' -> '>'
                    '<' -> '^'
                    '>' -> 'v'
                    else -> '<'
                }

            direction = puzzle[currentPosition.rowIndex][currentPosition.columnIndex]
            nextPosition = determineNextPosition(currentPosition, direction)

            // Check for 180 degrees turn.
            if (puzzle[nextPosition.rowIndex][nextPosition.columnIndex] == '#') {
                puzzle[currentPosition.rowIndex][currentPosition.columnIndex] =
                    when (puzzle[currentPosition.rowIndex][currentPosition.columnIndex]) {
                        '^' -> '>'
                        '<' -> '^'
                        '>' -> 'v'
                        else -> '<'
                    }

                direction = puzzle[currentPosition.rowIndex][currentPosition.columnIndex]
                nextPosition = determineNextPosition(currentPosition, direction)
            }
        }

        puzzle[currentPosition.rowIndex][currentPosition.columnIndex] = 'X'
        puzzle[nextPosition.rowIndex][nextPosition.columnIndex] = direction

        return nextPosition
    }

    private fun moveToNextPositionPart2(currentPosition: Position, direction: Char, obstructions: MutableList<Position>): Pair<Position, Char> {
        val currentCharacter = puzzle[currentPosition.rowIndex][currentPosition.columnIndex]
        var nextDirection = direction
        var nextCharacter: Char
        var nextPosition = determineNextPosition(currentPosition, nextDirection)

        if (puzzle[nextPosition.rowIndex][nextPosition.columnIndex] == ' ') {
            return Pair(nextPosition, nextDirection)
        } else if (puzzle[nextPosition.rowIndex][nextPosition.columnIndex] == '#') {
            nextDirection = when (nextDirection) {
                '^' -> '>'
                '<' -> '^'
                '>' -> 'v'
                else -> '<'
            }

            nextCharacter = '+'
            nextPosition = determineNextPosition(currentPosition, nextDirection)
        } else {
            nextCharacter = when (nextDirection) {
                '^' -> '|'
                '<' -> '-'
                '>' -> '-'
                else -> '|'
            }
        }

        if (currentCharacter == '.') {
            puzzle[currentPosition.rowIndex][currentPosition.columnIndex] = nextCharacter
            if (nextCharacter == '+') {
                if (direction == '^' || direction == 'v') {
                    loopDirectionsVertical[currentPosition.rowIndex][currentPosition.columnIndex] = direction
                    loopDirectionsHorizontal[currentPosition.rowIndex][currentPosition.columnIndex] = nextDirection
                }
                else {
                    loopDirectionsHorizontal[currentPosition.rowIndex][currentPosition.columnIndex] = direction
                    loopDirectionsVertical[currentPosition.rowIndex][currentPosition.columnIndex] = nextDirection
                }
            }
            else if (nextCharacter == '-') {
                loopDirectionsHorizontal[currentPosition.rowIndex][currentPosition.columnIndex] = nextDirection
            } else {
                loopDirectionsVertical[currentPosition.rowIndex][currentPosition.columnIndex] = nextDirection
            }
        } else if (currentCharacter in listOf('-', '|')) {
            if ((currentCharacter == '-') && nextDirection in listOf('^', 'v')) {
                nextCharacter = '+'
            }
            if ((currentCharacter == '|') && nextDirection in listOf('<', '>')) {
                nextCharacter = '+'
            }

            puzzle[currentPosition.rowIndex][currentPosition.columnIndex] = nextCharacter
            if (currentCharacter == '-') {
                loopDirectionsHorizontal[currentPosition.rowIndex][currentPosition.columnIndex] = nextDirection
            } else {
                loopDirectionsVertical[currentPosition.rowIndex][currentPosition.columnIndex] = nextDirection
            }
        }

        // Check for loops.
        if (direction == nextDirection && nextPosition !in obstructions && nextCharacter !in listOf('^', '<', '>', 'v')) {
            val loopDirection = when (direction) {
                '^' -> '>'
                '<' -> '^'
                '>' -> 'v'
                else -> '<'
            }

            var loopPosition = determineNextPosition(currentPosition, loopDirection)
            var loopCharacter = puzzle[loopPosition.rowIndex][loopPosition.columnIndex]

            while (loopCharacter in listOf('.')) {
                loopPosition = determineNextPosition(loopPosition, loopDirection)
                loopCharacter = puzzle[loopPosition.rowIndex][loopPosition.columnIndex]
            }

            // Check that the current loop direction is not opposite to the original direction.
            if (((loopCharacter in listOf('-', '+')) && loopDirection == loopDirectionsHorizontal[loopPosition.rowIndex][loopPosition.columnIndex]) ||
                ((loopCharacter in listOf('|', '+')) && loopDirection == loopDirectionsVertical[loopPosition.rowIndex][loopPosition.columnIndex]) ||
                ((loopCharacter in listOf('^', '<', '>', 'v')) && loopDirection == loopCharacter)
            ) {
                obstructions.add(nextPosition)
            }
        }

        return Pair(nextPosition, nextDirection)
    }

    private fun determineNextPosition(currentPosition: Position, direction: Char): Position =
        when (direction) {
            '^' -> Position(currentPosition.rowIndex - 1, currentPosition.columnIndex)
            '<' -> Position(currentPosition.rowIndex, currentPosition.columnIndex - 1)
            '>' -> Position(currentPosition.rowIndex, currentPosition.columnIndex + 1)
            else -> Position(currentPosition.rowIndex + 1, currentPosition.columnIndex)
        }

    private fun countDistinctPositions(): Long =
        puzzle.sumOf { puzzleRow -> puzzleRow.sumOf { cellCharacter -> if (cellCharacter == 'X') 1L else 0L } }

    private fun initializePuzzle(inputLines: List<String>): Array<Array<Char>> {
        // Use a border around the actual puzzle to prevent index out of range errors.
        val borderSize = 1

        val puzzleHeight = 2 * borderSize + inputLines.size
        val puzzleWidth = 2 * borderSize + (inputLines.maxOfOrNull { line -> line.length } ?: 0)

        val puzzle = Array(puzzleHeight) { Array(puzzleWidth) { ' ' } }

        inputLines.forEachIndexed { rowIndex, inputLine ->
            inputLine.forEachIndexed { columnIndex, cellCharacter ->
                puzzle[borderSize + rowIndex][borderSize + columnIndex] = cellCharacter
            }
        }

        return puzzle
    }

    private fun printPuzzle() {
        println("Puzzle:")

        puzzle.forEach { puzzleRow ->
            puzzleRow.forEach { puzzleCell -> print(puzzleCell) }
            println()
        }

        println()
    }

    override val inputLinesExamples = listOf(
        """
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...
""",
        """
Example 2 input lines...
""",
    )


    data class Position(val rowIndex: Int, val columnIndex: Int)
}

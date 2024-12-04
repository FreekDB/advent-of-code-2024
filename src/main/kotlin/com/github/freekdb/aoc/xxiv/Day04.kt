package com.github.freekdb.aoc.xxiv


fun main() {
    val verbose = false
    val part = 2
    val descriptions = listOf("The amount of times XMAS appears is", "The amount of times X-MAS appears is")

//    Day04(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
//    Day04(verbose).solvePuzzle(part, inputId = "example 2", descriptions)
    Day04(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Part 1 -- The amount of times XMAS appears is: 2569.
    // Part 2 -- The amount of times X-MAS appears is: 1998.
}


class Day04(verbose: Boolean) : BasePuzzleSolver(verbose) {
    private val searchWord = "XMAS"

    private lateinit var puzzle: Array<Array<Char>>

    override fun solvePart1(inputLines: List<String>): Long {
        puzzle = initializePuzzle(inputLines)

        if (verbose) {
            printPuzzle()
        }

        return puzzle.mapIndexed { rowIndex, puzzleRow ->
            puzzleRow.indices.sumOf { columnIndex ->
                if (puzzle[rowIndex][columnIndex] == searchWord[0]) {
                    countMatchesPart1(rowIndex, columnIndex)
                } else {
                    0L
                }
            }
        }.sum()
    }

    private fun countMatchesPart1(startRowIndex: Int, startColumnIndex: Int): Long {
        // Determine matches in eight directions (the center value will always be false).
        val matches = Array(3) { Array(3) { true } }

        (1..<searchWord.length).map { letterIndex ->
            (-1..1).map { rowDirection ->
                (-1..1).map { columnDirection ->
                    val cellRowIndex = startRowIndex + letterIndex * rowDirection
                    val cellColumnIndex = startColumnIndex + letterIndex * columnDirection

                    if (puzzle[cellRowIndex][cellColumnIndex] != searchWord[letterIndex]) {
                        // Convert row and column directions from range -1..1 to array indices 0..2.
                        matches[rowDirection + 1][columnDirection + 1] = false
                    }
                }
            }
        }

        return matches.sumOf { matchesRow -> matchesRow.count { it } }.toLong()
    }

    override fun solvePart2(inputLines: List<String>): Long {
        puzzle = initializePuzzle(inputLines)

        if (verbose) {
            printPuzzle()
        }

        val centerLetter = 'A'

        return puzzle.mapIndexed { rowIndex, puzzleRow ->
            puzzleRow.indices.sumOf { columnIndex ->
                if (puzzle[rowIndex][columnIndex] == centerLetter) {
                    countMatchesPart2(rowIndex, columnIndex)
                } else {
                    0L
                }
            }
        }.sum()
    }

    private fun countMatchesPart2(rowIndex: Int, columnIndex: Int): Long {
        val masSet = setOf('M', 'A', 'S')

        val diagonal1 = setOf(puzzle[rowIndex - 1][columnIndex - 1], puzzle[rowIndex][columnIndex], puzzle[rowIndex + 1][columnIndex + 1])
        val diagonal2 = setOf(puzzle[rowIndex + 1][columnIndex - 1], puzzle[rowIndex][columnIndex], puzzle[rowIndex - 1][columnIndex + 1])

        return if (diagonal1 == masSet && diagonal2 == masSet) 1L else 0L
    }

    private fun initializePuzzle(inputLines: List<String>): Array<Array<Char>> {
        // Use a border around the actual puzzle to prevent index out of range errors.
        val borderSize = searchWord.length - 1

        val puzzleHeight = 2 * borderSize + inputLines.size
        val puzzleWidth = 2 * borderSize + (inputLines.maxOfOrNull { line -> line.length } ?: 0)

        val puzzle = Array(puzzleHeight) { Array(puzzleWidth) { '.' } }

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

    @Suppress("SpellCheckingInspection")
    override val inputLinesExamples = listOf(
        """
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
""",
        """
.M.S......
..A..MSMS.
.M.S.MAA..
..A.ASMSM.
.M.S.M....
..........
S.S.S.S.S.
.A.A.A.A..
M.M.M.M.M.
..........
""",
    )
}

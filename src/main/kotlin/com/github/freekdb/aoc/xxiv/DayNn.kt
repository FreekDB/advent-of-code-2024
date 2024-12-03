package com.github.freekdb.aoc.xxiv


fun main() {
    val verbose = true
    val part = 1
    val descriptions = listOf("Description part 1...", "Description part 2...")

    DayNn(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
//    DayNn(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Copy the results here to register the (in)correct answers that you submitted...
}


class DayNn(verbose: Boolean) : BasePuzzleSolver(verbose) {
    override fun solvePart1(inputLines: List<String>): Long {

        // Create your solution for part 1 here...

        return inputLines.size.toLong()
    }

    override fun solvePart2(inputLines: List<String>): Long {

        // Create your solution for part 2 here...

        return inputLines.size.toLong()
    }

    override val inputLinesExamples = listOf(
        """
Example 1 input lines...
""",
        """
Example 2 input lines...
""",
    )
}

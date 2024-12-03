package com.github.freekdb.aoc.xxiv

import java.io.File


abstract class BasePuzzleSolver(protected val verbose: Boolean) {
    // Download the input file for day <n> from https://adventofcode.com/2024/day/<n>/input to the input directory.
    private val puzzleInputDirectory = "input/"

    fun solvePuzzle(part: Int, inputId: String, descriptions: List<String>) {
        val startTime = System.currentTimeMillis()

        val inputLines = getInputLines(inputId)

        if (verbose) {
            println()
            println("Input lines:")
            inputLines.forEach { println(it) }
            println()
        }

        val result = if (part == 1) { solvePart1(inputLines) } else { solvePart2(inputLines) }

        println()
        println("Part $part -- ${descriptions[part - 1]}: $result.")

        println()
        val duration = (System.currentTimeMillis() - startTime) / 1000
        println("The program ran for $duration seconds.")
    }

    internal abstract fun solvePart1(inputLines: List<String>): Long
    internal abstract fun solvePart2(inputLines: List<String>): Long

    internal abstract val inputLinesExamples: List<String>

    private fun getInputLines(inputId: String): List<String> {
        val validInputIds = (1..inputLinesExamples.size).map { "example $it" } + listOf("puzzle input")
        if (inputId !in validInputIds)
            throw RuntimeException("Unexpected input ID '$inputId'.")

        return if (inputId == "puzzle input") {
            val dayNumber = javaClass.name.takeLast(2)
            val filepath = "${puzzleInputDirectory}day-$dayNumber.txt"

            File(filepath).readLines()
        } else {
            inputLinesExamples[inputId.substringAfter(" ").toInt() - 1]
                .split("\n")
                .dropWhile { it.isBlank() }
                .dropLastWhile { it.isBlank() }
        }
    }
}

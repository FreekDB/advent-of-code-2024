package com.github.freekdb.aoc.xxiv

import kotlin.math.abs


fun main() {
    val verbose = false
    val part = 2
    val descriptions = listOf(
        "The total distance between the left and right lists is",
        "The similarity score of the left and right lists is"
    )

//    Day01(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
    Day01(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Part 1 -- The total distance between the left and right lists is: 2164381.
    // Part 2 -- The similarity score of the left and right lists is: 20719933.
}


class Day01(verbose: Boolean) : BasePuzzleSolver(verbose) {
    override fun solvePart1(inputLines: List<String>): Long {
        val splitLists = inputLines.map { line -> line.split(Regex(" +")) }

        val leftList = splitLists.map { items -> items[0].toLong() }.sorted()
        val rightList = splitLists.map { items -> items[1].toLong() }.sorted()

        return leftList.zip(rightList).sumOf { values -> abs(values.first - values.second) }
    }

    override fun solvePart2(inputLines: List<String>): Long {
        val splitLists = inputLines.map { line -> line.split(Regex(" +")) }

        val leftList = splitLists.map { items -> items[0].toLong() }.sorted()
        val rightList = splitLists.map { items -> items[1].toLong() }.sorted()

        return leftList.sumOf { leftNumber -> leftNumber * rightList.count { rightNumber -> leftNumber == rightNumber } }
    }

    override val inputLinesExamples = listOf(
        """
3   4
4   3
2   5
1   3
3   9
3   3
""")
}

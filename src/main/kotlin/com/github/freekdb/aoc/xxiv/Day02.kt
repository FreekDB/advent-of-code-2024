package com.github.freekdb.aoc.xxiv


fun main() {
    val verbose = false
    val part = 2
    val descriptions = listOf("The number of safe reports is", "The number of safe reports is")

//    Day02().solvePuzzle(part, inputId = "example 1", descriptions)
    Day02(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Part 1 -- The number of safe reports is: 486.
    // Part 2 -- The number of safe reports is: 522.
    // Part 2 -- The number of safe reports is: 540.
}


class Day02(verbose: Boolean) : BasePuzzleSolver(verbose) {
    override fun solvePart1(inputLines: List<String>): Long {
        val reports = inputLines.map { line -> line.split(" ").map { value -> value.toLong() } }

        return reports.count { numbers -> stepsAreValid(numbers, singleBadLevelAllowed = false) }.toLong()
    }

    override fun solvePart2(inputLines: List<String>): Long {
        val reports = inputLines.map { line -> line.split(" ").map { value -> value.toLong() } }

        return reports.count { levels ->
            if (verbose) {
                stepsAreValidVerbose(levels, singleBadLevelAllowed = true)
            } else {
                stepsAreValid(levels, singleBadLevelAllowed = true)
            }
        }.toLong()
    }

    @Suppress("SameParameterValue")
    private fun stepsAreValidVerbose(levels: List<Long>, singleBadLevelAllowed: Boolean): Boolean {
        val steps = levels
            .zipWithNext()
            .map { levelValues -> levelValues.second - levelValues.first }

        println("Levels: $levels => steps: $steps.")

        val valid = stepsAreValid(levels, singleBadLevelAllowed)

        println("Valid: $valid.")
        println()

        return valid
    }

    private fun stepsAreValid(levels: List<Long>, singleBadLevelAllowed: Boolean): Boolean {
        val steps = levels
            .zipWithNext()
            .map { levelValues -> levelValues.second - levelValues.first }

        val maximumStep = 3L
        val validSteps = if (steps[0] < 0) -maximumStep..-1L else 1L..maximumStep

        val validCount = steps.count { step -> step in validSteps }

        return validCount == steps.size ||
                (singleBadLevelAllowed && // validCount == steps.size - 1 &&
                        (levels.indices)
                            .any { levelIndex ->
                                val modifiedLevels = levels.toMutableList()
                                modifiedLevels.removeAt(levelIndex)

                                if (verbose) {
                                    printModifiedSituation(modifiedLevels, levels, levelIndex)
                                }

                                stepsAreValid(modifiedLevels, singleBadLevelAllowed = false)
                            })
    }

    private fun printModifiedSituation(modifiedLevels: MutableList<Long>, levels: List<Long>, levelIndex: Int) {
        val valid = stepsAreValid(modifiedLevels, singleBadLevelAllowed = false)

        if (valid) {
            val modifiedSteps = modifiedLevels
                .zipWithNext()
                .map { levelValues -> levelValues.second - levelValues.first }

            println("- Modified levels are valid: $levels => removed level index: $levelIndex => $modifiedLevels => steps: $modifiedSteps.")
        }
    }

    override val inputLinesExamples = listOf(
        """
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
"""
    )
}

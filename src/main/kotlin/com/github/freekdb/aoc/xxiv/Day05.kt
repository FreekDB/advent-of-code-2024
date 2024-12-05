package com.github.freekdb.aoc.xxiv


fun main() {
    val verbose = false
    val part = 2
    val descriptions = listOf(
        "The sum of the middle page number from the correctly-ordered updates is",
        "The sum of the middle page number from the incorrectly-ordered updates after correctly ordering them is"
    )

//    Day05(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
    Day05(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Part 1 -- The sum of the middle page number from the correctly-ordered updates is: 5064.
    // Part 2 -- The sum of the middle page number from the incorrectly-ordered updates after correctly ordering them is: 5152.
}


class Day05(verbose: Boolean) : BasePuzzleSolver(verbose) {
    override fun solvePart1(inputLines: List<String>): Long {
        val (orderingRules, updates) = readInput(inputLines)

        return updates.mapNotNull { pageUpdates ->
            if (determineOrderCorrect(pageUpdates, orderingRules)) pageUpdates[pageUpdates.size / 2] else null
        }.sum().toLong()
    }

    override fun solvePart2(inputLines: List<String>): Long {
        val (orderingRules, updates) = readInput(inputLines)

        return updates.mapNotNull { pageUpdates ->
            val orderCorrect = determineOrderCorrect(pageUpdates, orderingRules)

            if (orderCorrect) {
                null
            } else {
                val orderedPageNumbers = reorderPageNumbers(pageUpdates, orderingRules)

                orderedPageNumbers[orderedPageNumbers.size / 2]
            }
        }.sum().toLong()
    }

    private fun readInput(inputLines: List<String>): Pair<Map<Int, List<Int>>, List<List<Int>>> {
        val orderingRules = inputLines
            .takeWhile { line -> line.isNotEmpty() }
            .map { line -> line.split("|").map { it.toInt() } }
            .groupBy({ pageNumbers -> pageNumbers[0] }, { pageNumbers -> pageNumbers[1] })

        if (verbose) {
            println("orderingRules: $orderingRules")
        }

        val updates = inputLines
            .dropWhile { line -> line.isNotEmpty() }
            .drop(1)
            .map { line -> line.split(",").map { it.toInt() } }

        if (verbose) {
            println("updates: $updates")
            println()
        }

        return Pair(orderingRules, updates)
    }

    private fun determineOrderCorrect(pageUpdates: List<Int>, orderingRules: Map<Int, List<Int>>): Boolean {
        val orderCorrect = ((pageUpdates.size - 1) downTo 1).all { pageIndex ->
            val earlierPageNumbers = pageUpdates.subList(0, pageIndex)
            val rulePageNumbers = orderingRules[pageUpdates[pageIndex]] ?: emptyList()

            earlierPageNumbers.all { earlierPageNumber -> earlierPageNumber !in rulePageNumbers }
        }

        if (verbose) {
            println("pageUpdates: $pageUpdates => $orderCorrect, middle: ${pageUpdates[pageUpdates.size / 2]}.")
        }

        return orderCorrect
    }

    private fun reorderPageNumbers(pageUpdates: List<Int>, orderingRules: Map<Int, List<Int>>): MutableList<Int> {
        val remainingPageNumbers = pageUpdates.toMutableList()
        val orderedPageNumbers = mutableListOf<Int>()

        while (remainingPageNumbers.isNotEmpty()) {
            val pageNumber = remainingPageNumbers.removeFirst()
            val rulePageNumbers = orderingRules[pageNumber] ?: emptyList()
            val beforePageNumber = orderedPageNumbers.firstOrNull { orderedPageNumber -> orderedPageNumber in rulePageNumbers }
            val insertIndex = if (beforePageNumber == null) orderedPageNumbers.size else orderedPageNumbers.indexOf(beforePageNumber)

            orderedPageNumbers.add(insertIndex, pageNumber)
        }

        if (verbose) {
            println("pageUpdates: $pageUpdates => $orderedPageNumbers, middle: ${orderedPageNumbers[orderedPageNumbers.size / 2]}.")
        }

        return orderedPageNumbers
    }

    override val inputLinesExamples = listOf(
        """
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
"""
    )
}

package com.github.freekdb.aoc.xxiv


fun main() {
    val verbose = false
    val part = 2
    val descriptions = listOf(
        "The stone count after blinking 25 times is",
        "The stone count after blinking 75 times is"
    )

//    Day11(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
    Day11(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Part 1 -- The stone count after blinking 25 times is: 193269.
    // Part 2 -- The stone count after blinking 75 times is: 228449040027793.
}


class Day11(verbose: Boolean) : BasePuzzleSolver(verbose) {
    private val nextNumbersCache = mutableMapOf(0L to listOf(1L))

    override fun solvePart1(inputLines: List<String>): Long {
        if (verbose) {
            // Overflow test.

            listOf(500, 5_000, 50_000, 500_000, Long.MAX_VALUE / 4000, Long.MAX_VALUE / 2000, Long.MAX_VALUE / 1000)
                .forEach { number ->
                    val overflow = number > (Long.MAX_VALUE / 2024)

                    println("$number * 2024 = ${number * 2024} (overflow: $overflow).")
                }

            println()
        }

        var numbers = inputLines[0].split(" ").map { it.toLong() }

        if (verbose) {
            println("Numbers: $numbers.")
        }

        repeat(times = 25) {
            numbers = numbers.map { number -> nextNumbers(number) }.flatten()

            if (verbose) {
                println("Generation $it -- numbers size: ${numbers.size}.")
            }
        }

        return numbers.size.toLong()
    }

    override fun solvePart2(inputLines: List<String>): Long {
        var numberFrequencies = inputLines[0].split(" ").map { NumberFrequency(it.toLong(), 1L) }

        if (verbose) {
            println("Number frequencies: $numberFrequencies.")
        }

        repeat(times = 75) {
            val nextNumberFrequencies = numberFrequencies.map { numberFrequency ->
                nextNumbers(numberFrequency.number).map { nextNumber -> NumberFrequency(nextNumber, numberFrequency.frequency) }
            }.flatten()

            numberFrequencies = nextNumberFrequencies
                .groupBy { numberFrequency -> numberFrequency.number }
                .map { (number, frequencies) -> NumberFrequency(number, frequencies.sumOf { it.frequency }) }

            if (verbose) {
                println("Generation $it -- numbers frequencies sum: ${numberFrequencies.sumOf { it.frequency }}.")
            }
        }

        return numberFrequencies.sumOf { it.frequency }
    }

    private fun nextNumbers(number: Long): List<Long> {
        val result = when {
            nextNumbersCache.containsKey(number) -> nextNumbersCache[number] ?: emptyList()

            (number.toString().length % 2) == 0 -> {
                val numberString = number.toString()
                val length = numberString.length

                listOf(numberString.substring(0, length / 2).toLong(), numberString.substring(startIndex = length / 2).toLong())
            }

            else -> {
                if (number > (Long.MAX_VALUE / 2024)) {
                    System.err.println("Overflow with number $number!")
                }

                listOf(number * 2024L)
            }
        }

        nextNumbersCache[number] = result

        return result
    }

    override val inputLinesExamples = listOf(
        """
125 17
"""
    )


    data class NumberFrequency(val number: Long, val frequency: Long)
}

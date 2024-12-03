package com.github.freekdb.aoc.xxiv


fun main() {
    val verbose = false
    val part = 2
    val descriptions = listOf("The sum of all multiplications is", "The sum of all enabled multiplications is")

//    Day03(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
//    Day03(verbose).solvePuzzle(part, inputId = "example 2", descriptions)
    Day03(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Part 1 -- The sum of all multiplications is: 192767529.
    // Part 2 -- The sum of all enabled multiplications is: 104083373.
}


class Day03(verbose: Boolean) : BasePuzzleSolver(verbose) {
    override fun solvePart1(inputLines: List<String>): Long {
        // Match "mul(x,y)" commands.
        val regex = Regex(pattern = """(mul\(\d{1,3},\d{1,3}\))""")

        return inputLines.sumOf { inputLine ->
            val matchGroupValues = regex.findAll(inputLine).mapNotNull { matchResult -> matchResult.groups[1]?.value }

            matchGroupValues.map { matchGroupValue ->
                val number1 = matchGroupValue.substringAfter(delimiter = "mul(").substringBefore(delimiter = ",").toLong()
                val number2 = matchGroupValue.substringAfter(delimiter = ",").substringBefore(delimiter = ")").toLong()

                if (verbose) {
                    println(matchGroupValue)
                    println("$number1 * $number2 = ${number1 * number2}.")
                    println()
                }

                number1 * number2
            }.sum()
        }
    }

    override fun solvePart2(inputLines: List<String>): Long {
        // Match "mul(x,y)", "do()" and "don't()" commands.
        val regex = Regex(pattern = """((mul\(\d{1,3},\d{1,3}\))|do\(\)|don't\(\))""")
        var multiplicationsEnabled = true

        return inputLines.sumOf { inputLine ->
            val matchGroupValues = regex.findAll(inputLine).mapNotNull { matchResult -> matchResult.groups[1]?.value }

            matchGroupValues.map { matchGroupValue ->
                if (matchGroupValue in listOf("do()", "don't()")) {
                    multiplicationsEnabled = matchGroupValue == "do()"

                    if (verbose) {
                        println(matchGroupValue)
                        println("multiplicationsEnabled: $multiplicationsEnabled.")
                        println()
                    }

                    0
                } else if (multiplicationsEnabled) {
                    val number1 = matchGroupValue.substringAfter(delimiter = "mul(").substringBefore(delimiter = ",").toLong()
                    val number2 = matchGroupValue.substringAfter(delimiter = ",").substringBefore(delimiter = ")").toLong()

                    if (verbose) {
                        println(matchGroupValue)
                        println("$number1 * $number2 = ${number1 * number2}.")
                        println()
                    }

                    number1 * number2
                } else {
                    0
                }
            }.sum()
        }
    }

    override val inputLinesExamples = listOf(
        """
xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
""",
        """
xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))            
"""
    )
}

package com.github.freekdb.aoc.xxiv

import kotlin.math.pow


fun main() {
    val verbose = false
    val part = 2
    val descriptions = listOf(
        "The total sum of the calibration results of the equations that can be true is",
        "The total sum of the calibration results of the equations that can be true is"
    )

//    Day07(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
    Day07(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Part 1 -- The total sum of the calibration results of the equations that can be true is: 882304362421.
    // Part 2 -- The total sum of the calibration results of the equations that can be true is: 145149066755184.
}


class Day07(verbose: Boolean) : BasePuzzleSolver(verbose) {
    override fun solvePart1(inputLines: List<String>): Long {
        val equations = inputLines.map { line ->
            val parts = line.split(":")

            check(parts[0].toLong().toString() == parts[0])

            Equation(testValue = parts[0].toLong(), numbers = parts[1].trim().split(" ").map{ it.toLong() })
        }

        return equations.sumOf { equation ->
            val operatorCount = equation.numbers.size - 1
            val operatorPermutationCount = 2f.pow(operatorCount).toInt()

            if (verbose) {
                println("equation: $equation")
                println("operatorCount: $operatorCount")
                println("operatorPermutationCount: $operatorPermutationCount")
            }

            val equationPossible = (0..<operatorPermutationCount).any { operatorsValue ->
                // Start with the value zero and the plus operator.
                var value = 0L
                val operators = "0${operatorsValue.toString(radix = 2).padStart(operatorCount, '0')}"

                if (verbose) {
                    println("operatorsValue: $operatorsValue")
                    println("operators: '$operators'")
                }

                equation.numbers.forEachIndexed { numberIndex, number ->
                    val oldValue = value
                    value = if (operators[numberIndex] == '0') value + number else value * number

                    if (verbose) {
                        val operator = if (operators[numberIndex] == '0') "+" else "*"
                        println("value = old value $operator number == $oldValue $operator $number == $value")
                    }
                }

                value == equation.testValue
            }

            if (verbose) {
                println()
            }

            if (equationPossible) equation.testValue else 0L
        }
    }

    override fun solvePart2(inputLines: List<String>): Long {
        val equations = inputLines.map { line ->
            val parts = line.split(":")

            check(parts[0].toLong().toString() == parts[0])

            Equation(testValue = parts[0].toLong(), numbers = parts[1].trim().split(" ").map{ it.toLong() })
        }

        return equations.sumOf { equation ->
            val operatorCount = equation.numbers.size - 1
            val operatorPermutationCount = 3f.pow(operatorCount).toInt()

            if (verbose) {
                println("equation: $equation")
                println("operatorCount: $operatorCount")
                println("operatorPermutationCount: $operatorPermutationCount")
            }

            val equationPossible = (0..<operatorPermutationCount).any { operatorsValue ->
                // Start with the value zero and the plus operator.
                var value = 0L
                val operators = "0${operatorsValue.toString(radix = 3).padStart(operatorCount, '0')}"

                if (verbose) {
                    println("operatorsValue: $operatorsValue")
                    println("operators: '$operators'")
                }

                equation.numbers.forEachIndexed { numberIndex, number ->
                    val oldValue = value

                    value = when(operators[numberIndex]) {
                        '0' -> value + number
                        '1' -> value * number
                        else -> "$value$number".toLong()
                    }

                    if (verbose) {
                        val operator = when(operators[numberIndex]) {
                            '0' -> "+"
                            '1' -> "*"
                            else -> "||"
                        }
                        println("value = old value $operator number == $oldValue $operator $number == $value")
                    }
                }

                value == equation.testValue
            }

            if (verbose) {
                println()
            }

            if (equationPossible) equation.testValue else 0L
        }
    }

    override val inputLinesExamples = listOf(
        """
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20
""",
        """
Example 2 input lines...
""",
    )


    data class Equation(val testValue: Long, val numbers: List<Long>)
}

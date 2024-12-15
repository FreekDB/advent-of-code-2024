package com.github.freekdb.aoc.xxiv

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToLong


fun main() {
    val verbose = true
    val part = 2
    val descriptions = listOf(
        "The fewest tokens you would have to spend to win all possible prizes is",
        "The fewest tokens you would have to spend to win all possible prizes is"
    )

//    Day13(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
    Day13(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Part 1 -- The fewest tokens you would have to spend to win all possible prizes is: 28059.
    // Part 2 -- The fewest tokens you would have to spend to win all possible prizes is: 160896070903797.
    // That's not the right answer; your answer is too high.
    // Part 2 -- The fewest tokens you would have to spend to win all possible prizes is: 102255878088512.
}


class Day13(verbose: Boolean) : BasePuzzleSolver(verbose) {
    override fun solvePart1(inputLines: List<String>): Long {
        val configurationCount = (inputLines.size + 1) / 4

        val configurations = (0..<configurationCount).map { inputBlockIndex ->
            val buttonA = inputLines[inputBlockIndex * 4]
            val buttonB = inputLines[inputBlockIndex * 4 + 1]
            val prize = inputLines[inputBlockIndex * 4 + 2]

            val aFactorX = buttonA.substringAfter(delimiter = "Button A: X+").substringBefore(delimiter = ", Y+").toLong()
            val bFactorX = buttonB.substringAfter(delimiter = "Button B: X+").substringBefore(delimiter = ", Y+").toLong()
            val sumX = prize.substringAfter(delimiter = "Prize: X=").substringBefore(delimiter = ", Y=").toLong()

            val aFactorY = buttonA.substringAfter(delimiter = ", Y+").toLong()
            val bFactorY = buttonB.substringAfter(delimiter = ", Y+").toLong()
            val sumY = prize.substringAfter(delimiter = ", Y=").toLong()

            Configuration(aFactorX, bFactorX, sumX, aFactorY, bFactorY, sumY)
        }

        val totalTokenCount = configurations.sumOf { config ->
            val a = 1.0 * (config.bFactorY * config.sumX - config.bFactorX * config.sumY) / (config.bFactorY * config.aFactorX - config.bFactorX * config.aFactorY)
            val b = 1.0 * (config.sumX - config.aFactorX * a) / config.bFactorX

            if (verbose) {
                println("config: $config ->")
                println("a: $a, b: $b =>")
                println("$a * ${config.aFactorX} + $b * ${config.bFactorX} = ${a * config.aFactorX + b * config.bFactorX}.")
                println("$a * ${config.aFactorY} + $b * ${config.bFactorY} = ${a * config.aFactorY + b * config.bFactorY}.")
                println()
            }

            if (a <= 100.0 && b <= 100.0 && a - a.roundToLong() == 0.0 && b - b.roundToLong() == 0.0) {
                (3 * a + b).toLong()
            } else {
                0L
            }
        }

        return totalTokenCount
    }

    override fun solvePart2(inputLines: List<String>): Long {
        val configurationCount = (inputLines.size + 1) / 4
        val scaleFactor = BigDecimal("1.000000000000")
        val sumIncrease = scaleFactor * BigDecimal(10000000000000L)
        // val sumIncrease = scaleFactor * BigDecimal.ZERO
        val almostZero = BigDecimal("0.0000001")

        val configurations = (0..<configurationCount).map { inputBlockIndex ->
            val buttonA = inputLines[inputBlockIndex * 4]
            val buttonB = inputLines[inputBlockIndex * 4 + 1]
            val prize = inputLines[inputBlockIndex * 4 + 2]

            val aFactorX = scaleFactor * buttonA.substringAfter(delimiter = "Button A: X+").substringBefore(delimiter = ", Y+").toBigDecimal()
            val bFactorX = scaleFactor * buttonB.substringAfter(delimiter = "Button B: X+").substringBefore(delimiter = ", Y+").toBigDecimal()
            val sumX = sumIncrease + prize.substringAfter(delimiter = "Prize: X=").substringBefore(delimiter = ", Y=").toBigDecimal()

            val aFactorY = scaleFactor * buttonA.substringAfter(delimiter = ", Y+").toBigDecimal()
            val bFactorY = scaleFactor * buttonB.substringAfter(delimiter = ", Y+").toBigDecimal()
            val sumY = sumIncrease + prize.substringAfter(delimiter = ", Y=").toBigDecimal()

            ConfigurationBigDecimal(aFactorX, bFactorX, sumX, aFactorY, bFactorY, sumY)
        }

        val totalTokenCount = configurations.sumOf { config ->
            val a = (config.bFactorY * config.sumX - config.bFactorX * config.sumY) / (config.bFactorY * config.aFactorX - config.bFactorX * config.aFactorY)
            val b = (config.sumX - config.aFactorX * a) / config.bFactorX

            if (verbose) {
                println("config: $config ->")
                println("a: $a, b: $b =>")
                println("$a * ${config.aFactorX} + $b * ${config.bFactorX} = ${a * config.aFactorX + b * config.bFactorX}.")
                println("$a * ${config.aFactorY} + $b * ${config.bFactorY} = ${a * config.aFactorY + b * config.bFactorY}.")
                println()
            }

            val aRounded = a.setScale(0, RoundingMode.DOWN)
            val bRounded = b.setScale(0, RoundingMode.DOWN)
            if (/*a <= 100.0.toBigDecimal() && b <= 100.0.toBigDecimal() &&*/
                (a - aRounded) < almostZero &&
                (b - bRounded) < almostZero) {
                (3.toBigDecimal() * a + b)
            } else {
                0L.toBigDecimal()
            }
        }

        println("totalTokenCount: $totalTokenCount")

        return totalTokenCount.toLong()
    }

    override val inputLinesExamples = listOf(
        """
Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279
""",
        """
Example 2 input lines...
""",
    )


    data class Configuration(val aFactorX: Long, val bFactorX: Long, val sumX: Long, val aFactorY: Long, val bFactorY: Long, val sumY: Long)
    data class ConfigurationBigDecimal(val aFactorX: BigDecimal, val bFactorX: BigDecimal, val sumX: BigDecimal, val aFactorY: BigDecimal, val bFactorY: BigDecimal, val sumY: BigDecimal)
}

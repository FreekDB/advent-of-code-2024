package com.github.freekdb.aoc.xxiv


fun main() {
    val verbose = true
    val part = 2
    val descriptions = listOf(
        "The safety factor after exactly 100 seconds have elapsed is",
        "The fewest number of seconds that must elapse for the robots to display the Easter egg is"
    )

//    Day14(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
    Day14(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Part 1 -- The safety factor after exactly 100 seconds have elapsed is: 229069152.
    // I had no idea how to solve part 2. Tip from reddit:
    // "cptncolloo • 1d ago • You can use part one. The frame with the tree has the minimal safety factor."
    // https://www.reddit.com/r/adventofcode/comments/1hdw2m1/2024_day_14_part_2/
    // Part 2 -- The fewest number of seconds that must elapse for the robots to display the Easter egg is: 7383.
}


class Day14(verbose: Boolean) : BasePuzzleSolver(verbose) {
    override fun solvePart1(inputLines: List<String>): Long {
        val robots = inputLines.map { inputLine ->
            val fields = inputLine
                .split("p=", ",", " v=")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }

            Robot(Vector(fields[0], fields[1]), Vector(fields[2], fields[3]))
        }

        if (verbose) {
            robots.forEach { robot -> println(robot) }
            println()
        }

        val spaceWidth = 101
        val spaceHeight = 103

        var movedRobots = robots

        repeat(times = 100) {
            movedRobots = movedRobots.map { robot ->
                val movedPosition = robot.position + robot.velocity
                val newPosition = Vector(
                    x = (movedPosition.x + spaceWidth) % spaceWidth,
                    y = (movedPosition.y + spaceHeight) % spaceHeight
                )
                Robot(newPosition, robot.velocity)
            }

            if (verbose) {
                movedRobots.forEach { robot -> println(robot) }
                println()

                (0..<spaceHeight).forEach { y ->
                    (0..<spaceWidth).forEach { x ->
                        val robotCount = movedRobots.count { robot -> robot.position == Vector(x, y) }
                        print(if (robotCount == 0) "." else robotCount)
                    }
                    println()
                }
                println()
            }
        }

        val quadrantCounts = listOf(
            movedRobots.count { robot -> robot.position.x < spaceWidth / 2 && robot.position.y < spaceHeight / 2 },
            movedRobots.count { robot -> robot.position.x > spaceWidth / 2 && robot.position.y < spaceHeight / 2 },
            movedRobots.count { robot -> robot.position.x > spaceWidth / 2 && robot.position.y > spaceHeight / 2 },
            movedRobots.count { robot -> robot.position.x < spaceWidth / 2 && robot.position.y > spaceHeight / 2 },
        )

        val safetyFactor = quadrantCounts.fold(initial = 1) { product, count -> product * count }.toLong()

        return safetyFactor
    }

    override fun solvePart2(inputLines: List<String>): Long {
        val robots = inputLines.map { inputLine ->
            val fields = inputLine
                .split("p=", ",", " v=")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }

            Robot(Vector(fields[0], fields[1]), Vector(fields[2], fields[3]))
        }

        if (verbose) {
            robots.forEach { robot -> println(robot) }
            println()
        }

        val spaceWidth = 101
        val spaceHeight = 103

        var movedRobots = robots
        var lowestSafetyFactor = Long.MAX_VALUE
        var moveIndexAtLowestSafetyFactor = 0
        var moveIndex = 1

        while(moveIndex < 2 * spaceWidth * spaceHeight) {
            movedRobots = movedRobots.map { robot ->
                val movedPosition = robot.position + robot.velocity
                val newPosition = Vector(
                    x = (movedPosition.x + spaceWidth) % spaceWidth,
                    y = (movedPosition.y + spaceHeight) % spaceHeight
                )
                Robot(newPosition, robot.velocity)
            }

            val quadrantCounts = listOf(
                movedRobots.count { robot -> robot.position.x < spaceWidth / 2 && robot.position.y < spaceHeight / 2 },
                movedRobots.count { robot -> robot.position.x > spaceWidth / 2 && robot.position.y < spaceHeight / 2 },
                movedRobots.count { robot -> robot.position.x > spaceWidth / 2 && robot.position.y > spaceHeight / 2 },
                movedRobots.count { robot -> robot.position.x < spaceWidth / 2 && robot.position.y > spaceHeight / 2 },
            )

            val safetyFactor = quadrantCounts.fold(initial = 1) { product, count -> product * count }.toLong()

            if (safetyFactor < lowestSafetyFactor) {
                if (verbose) {
                    println("safetyFactor: $safetyFactor")
                    println("moveIndex: $moveIndex")
                    println()

                    (0..<spaceHeight).forEach { y ->
                        (0..<spaceWidth).forEach { x ->
                            val robotCount = movedRobots.count { robot -> robot.position == Vector(x, y) }
                            print(if (robotCount == 0) "." else robotCount)
                        }
                        println()
                    }
                    println()
                }

                lowestSafetyFactor = safetyFactor
                moveIndexAtLowestSafetyFactor = moveIndex
            }

            moveIndex++
        }

        return moveIndexAtLowestSafetyFactor.toLong()
    }

    override val inputLinesExamples = listOf(
        """
p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3
""",
        """
Example 2 input lines...
""",
    )

    data class Vector(val x: Int, val y: Int) {
        operator fun plus(vector: Vector): Vector =
            Vector(x = this.x + vector.x, y = this.y + vector.y)
    }

    data class Robot(val position: Vector, val velocity: Vector)
}

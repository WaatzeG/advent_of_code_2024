package Day06

import kotlinx.coroutines.*
import kotlinx.coroutines.awaitAll
import println
import readInput
import java.time.Duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

enum class Orientation(val moveX: Int, val moveY: Int) {
    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);

    fun turnRight(): Orientation {
        return entries[(this.ordinal + 1) % entries.size]
    }
}

fun main() {

    fun findStartingLocation(input: List<String>): Pair<Int, Int> {
        return input.mapIndexedNotNull { index, line ->
            val charIndex = line.indexOf('^')
            if (charIndex >= 0) {
                Pair(charIndex, index)
            } else {
                null
            }
        }.single()
    }

    fun peek(input: List<String>, x: Int, y: Int): Char? {
        return if (x >= 0 && x <= input[0].lastIndex && y >= 0 && y <= input.lastIndex) {
            input[y][x]
        } else null
    }

    fun isObstruction(input: List<String>, nextPosition: Pair<Int, Int>): Boolean {
        return when (peek(input, nextPosition.first, nextPosition.second)) {
            '#' -> true
            else -> false
        }
    }

    tailrec fun walk(
        input: List<String>,
        currentPosition: Pair<Int, Int>,
        orientation: Orientation = Orientation.NORTH,
        positionsCovered: Set<Pair<Int, Int>> = setOf(currentPosition)
    ): Set<Pair<Int, Int>> {
        return if (currentPosition.first < 0 || currentPosition.first > input[0].lastIndex || currentPosition.second < 0 || currentPosition.second > input.lastIndex) {
            //made it out of the grid
            positionsCovered
        } else {
            //not there yet
            //can we move to the next position? If not, keep turning right until we have a path forward.
            var nextOrientation = orientation
            var nextPosition =
                Pair(currentPosition.first + nextOrientation.moveX, currentPosition.second + nextOrientation.moveY)
            while (isObstruction(input, nextPosition)) {
                nextOrientation = nextOrientation.turnRight()
                nextPosition =
                    Pair(currentPosition.first + nextOrientation.moveX, currentPosition.second + nextOrientation.moveY)
            }
            //found valid path forward
            walk(input, nextPosition, nextOrientation, positionsCovered + currentPosition)
        }
    }

    tailrec fun testPath(
        input: List<String>,
        currentPosition: Pair<Int, Int>,
        orientation: Orientation = Orientation.NORTH,
        positionsCovered: Set<Pair<Pair<Int, Int>, Orientation>> = setOf()
    ): Boolean {
        return if (Pair(currentPosition, orientation) in positionsCovered) {
            //we are at the same position going in the same direction. We're in a loop.
            false
        } else if (currentPosition.first < 0 || currentPosition.first > input[0].lastIndex || currentPosition.second < 0 || currentPosition.second > input.lastIndex) {
            //made it out of the grid
            true
        } else {
            //not there yet
            //can we move to the next position? If not, keep turning right until we have a path forward.
            var nextOrientation = orientation
            var nextPosition =
                Pair(currentPosition.first + nextOrientation.moveX, currentPosition.second + nextOrientation.moveY)
            while (isObstruction(input, nextPosition)) {
                nextOrientation = nextOrientation.turnRight()
                nextPosition =
                    Pair(currentPosition.first + nextOrientation.moveX, currentPosition.second + nextOrientation.moveY)
            }
            //found valid path forward
            testPath(input, nextPosition, nextOrientation, positionsCovered + Pair(currentPosition, orientation))
        }
    }

    fun part1(input: List<String>): Int {
        //find the ^ start coordinates
        val startingCoords = findStartingLocation(input)
        return walk(input, startingCoords).size
    }

    fun part2(input: List<String>): Int = runBlocking {
        //foreach position that is part of the route, add an obstruction and try to run the route
        //if it succeeds, it's not endless. If the guards finds itself on the same point in the same orientation,
        //he is in a loop.
        val startingCoords = findStartingLocation(input)
        val atomicInteger = AtomicInteger(0)
        val route = walk(input, startingCoords)
        (route - startingCoords).map { (x, y) ->
            //start coroutine to check concurrently to speed things up.
            async(Dispatchers.Default) {
                val newInput = input.toTypedArray()
                newInput[y] = newInput[y].replaceRange(x until x + 1, "#")
                if (testPath(newInput.toList(), startingCoords)) {
                    0 //escaped the grid
                } else {
                    1 //in a loop
                }.also {
                    println("Completed ${atomicInteger.incrementAndGet()} positions of ${route.size - 1}")
                }
            }
        }.awaitAll().sum()
    }

// Or read a large test input from the `src/Day06_test.txt` file:
    readInput("Day06_test").also { input ->
        check(part1(input) == 41)
    }

    readInput("Day06_test").also { input ->
        check(part2(input) == 6)
    }

// Read the input from the `src/Day06.txt` file.
    readInput("Day06").also { input ->
        part1(input).println()
        part2(input).println()
    }
}

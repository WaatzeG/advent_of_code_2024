package Day10

import println
import readInput

fun main() {

    fun toTopoMap(input: List<String>): List<List<Int>> {
        return input.map { it.map { c -> c.digitToInt() } }
    }

    fun value(map: List<List<Int>>, x: Int, y: Int): Int {
        return map[y][x]
    }

    fun isValid(map: List<List<Int>>, x: Int, y: Int): Boolean {
        return x >= 0 && y >= 0 && y <= map.lastIndex && x <= map[y].lastIndex
    }

    fun getNeighbors(map: List<List<Int>>, x: Int, y: Int): List<Pair<Int, Int>> {
        return listOf(
            x + 1 to y, //right
            x - 1 to y, //left
            x to y - 1, //up
            x to y + 1, //down
        ).filter { (x, y) -> isValid(map, x, y) }
    }

    fun peaksReachable(map: List<List<Int>>, x: Int, y: Int): Set<Pair<Int, Int>> {
        val currentValue = value(map, x, y)
        return if (currentValue == 9) {
            setOf(x to y)
        } else {
            getNeighbors(map, x, y)
                .filter { (x, y) -> value(map, x, y) == currentValue + 1 }
                .fold(setOf()) { acc, (x, y) -> acc + peaksReachable(map, x, y) }
        }
    }

    fun numberOfTrails(map: List<List<Int>>, x: Int, y: Int): Int {
        val currentValue = value(map, x, y)
        return if (currentValue == 9) {
            1
        } else {
            getNeighbors(map, x, y)
                .filter { (x, y) -> value(map, x, y) == currentValue + 1 }
                .fold(0) { acc, (x, y) -> acc + numberOfTrails(map, x, y) }
        }
    }

    fun getTrailheads(map: List<List<Int>>) =
        map.flatMapIndexed { y: Int, heights: List<Int> ->
            heights.mapIndexedNotNull { x, height ->
                if (height == 0) {
                    x to y
                } else {
                    null
                }
            }
        }

    fun part1(input: List<String>): Long {
        val map = toTopoMap(input)
        val trailheads = getTrailheads(map)
        return trailheads.sumOf { (x, y) -> peaksReachable(map, x, y).size }.toLong()
    }

    fun part2(input: List<String>): Long {
        val map = toTopoMap(input)
        val trailheads = getTrailheads(map)
        //collect the number of trails for each trailhead
        return trailheads.sumOf { (x, y) -> numberOfTrails(map, x, y) }.toLong()
    }

// Or read a large test input from the `src/Day07_test.txt` file:
    readInput("Day10_test").also { input ->
        println(part1(input))
        check(part1(input) == 36L)
    }

    readInput("Day10_test").also { input ->
        println(part2(input))
        check(part2(input) == 81L)
    }

// Read the input from the `src/Day07.txt` file.
    readInput("Day10").also { input ->
        part1(input).println()
        part2(input).println()
    }
}

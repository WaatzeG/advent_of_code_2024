package Day11

import println
import readInput

fun main() {

    fun blink(stones: List<Long>): List<Long> {
        //we shouldn't modify the list we're going to iterate, so create a mutable copy
        val transformed = stones.toMutableList()
        var offsetFromIndex = 0
        stones.forEachIndexed { index, number ->
            when {
                number == 0L -> {
                    transformed[index + offsetFromIndex] = 1
                }

                number.toString().length % 2 == 0 -> {
                    val stringNumber = number.toString()
                    val left = stringNumber.substring(0 until stringNumber.length / 2).toLong()
                    val right = stringNumber.substring(stringNumber.length / 2 until stringNumber.length).toLong()
                    //insert right after left
                    transformed[index + offsetFromIndex] = left
                    transformed.add(index + 1 + offsetFromIndex, right)
                    offsetFromIndex += 1 //we increased the length off the list
                }

                else -> {
                    // multiply by 2024
                    transformed[index + offsetFromIndex] = number * 2024
                }
            }
        }
        return transformed
    }


    fun part1(input: List<String>): Int {
        var stones = getStones(input)
        repeat(25) {
            stones = blink(stones)
        }
        return stones.size
    }

    fun optimizedBlink(stoneCount: Map<Long, Long>): Map<Long,Long> {
        val newStoneCount = mutableMapOf<Long,Long>()
        stoneCount.keys.forEach { stoneNumber ->
            val numberOfStones = stoneCount[stoneNumber] ?: 0
            when {
                stoneNumber == 0L -> {
                    //0 becomes 1. Add
                    newStoneCount[1] = newStoneCount.computeIfAbsent(1) { 0 } + numberOfStones
                }

                stoneNumber.toString().length % 2 == 0 -> {
                    val stringNumber = stoneNumber.toString()
                    val left = stringNumber.substring(0 until stringNumber.length / 2).toLong()
                    val right = stringNumber.substring(stringNumber.length / 2 until stringNumber.length).toLong()
                    //insert right after left
                    newStoneCount[left] = newStoneCount.computeIfAbsent(left) { 0 } + numberOfStones
                    newStoneCount[right] = newStoneCount.computeIfAbsent(right) { 0 } + numberOfStones
                }

                else -> {
                    // multiply by 2024
                    val multiplicationResult = stoneNumber * 2024
                    newStoneCount[multiplicationResult] =
                        newStoneCount.computeIfAbsent(multiplicationResult) { 0 } + numberOfStones
                }
            }
        }
        //return and remove numbers for which are no stones
        return newStoneCount
            .filter { it.value > 0 }
    }

    fun part2(input: List<String>): Long {
        var stoneCount = getStones(input).associateWith { 1L }
        repeat(75) { blinkCount ->
            stoneCount = optimizedBlink(stoneCount)
            println("Blinked $blinkCount times.")
        }
        return stoneCount.entries.sumOf { it.value }
    }

// Or read a large test input from the `src/Day07_test.txt` file:
    readInput("Day11_test").also { input ->
        check(part1(input) == 55312)
    }

// Read the input from the `src/Day07.txt` file.
    readInput("Day11").also { input ->
        part1(input).println()
        part2(input).println()
    }
}

private fun getStones(input: List<String>) = input.single().split(" ").map { it.toLong() }

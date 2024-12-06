package Day03

import println
import readInput
import kotlin.math.absoluteValue

fun main() {

    val validMultiplicaton = Regex("mul\\((\\d+),(\\d+)\\)")
    val validMultiplicationWithOperators = Regex("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)")

    fun part1(input: List<String>): Int {
        return input.sumOf {
            validMultiplicaton.findAll(it).sumOf {
                it.groups[1]!!.value.toInt() * it.groups[2]!!.value.toInt()
            }
        }
    }

    fun part2(input: List<String>): Int {
        var enabledState = true //start enabled by default
        return input.sumOf {
            validMultiplicationWithOperators.findAll(it).sumOf {
                if (it.groups[0]!!.value == "do()") {
                    enabledState = true
                    0
                } else if (it.groups[0]!!.value == "don't()") {
                    enabledState = false
                    0
                } else if (enabledState) {
                    it.groups[1]!!.value.toInt() * it.groups[2]!!.value.toInt()
                } else {
                    0
                }
            }
        }
    }

// Or read a large test input from the `src/Day03_test.txt` file:
    readInput("Day03_test").also { input ->
        check(part1(input) == 161)
    }

    readInput("Day03_test").also { input ->
        check(part2(input) == 48)
    }

// Read the input from the `src/Day03.txt` file.
    readInput("Day03").also { input ->
        part1(input).println()
        part2(input).println()
    }
}

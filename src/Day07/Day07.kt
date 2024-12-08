package Day07

import println
import readInput

fun main() {
    val MULTIPLY = { a: Long, b: Int -> a.coerceAtLeast(1) * b }
    val PLUS = { a: Long, b: Int -> a + b }
    val CONCATENATE = { a: Long, b: Int -> (a.toString() + b.toString()).toLong() }

    fun splitInput(input: List<String>): List<Pair<Long, List<Int>>> {
        return input.map { line ->
            val targetValue = line.substringBefore(":").toLong()
            val components = line.substringAfter(": ").split(" ").map { it.toInt() }
            targetValue to components
        }
    }

    fun calculate(
        targetValue: Long,
        operators: List<(Long, Int) -> Long>,
        currentValue: Long,
        componentsRemaining: List<Int>
    ): Boolean {
        return operators.any { operator ->
            val nextValue = operator(currentValue, componentsRemaining.first())
            if (nextValue == targetValue && componentsRemaining.size == 1) {
                //found valid combination of operators and components
                true
            } else if (nextValue > targetValue) {
                //overshot
                false
            } else if (componentsRemaining.size == 1) {
                //not a valid combination of operators and components
                false
            } else {
                calculate(targetValue, operators, nextValue, componentsRemaining.subList(1, componentsRemaining.size))
            }
        }
    }

    fun part1(input: List<String>): Long {
        val splitInput = splitInput(input)
        return splitInput.sumOf { (targetValue, components) ->
            if (calculate(targetValue, listOf(MULTIPLY, PLUS), 0, components)) {
                targetValue
            } else {
                0
            }
        }
    }

    fun part2(input: List<String>): Long {
        val splitInput = splitInput(input)
        return splitInput.sumOf { (targetValue, components) ->
            if (calculate(targetValue, listOf(CONCATENATE, MULTIPLY, PLUS), 0, components)) {
                targetValue
            } else {
                0
            }
        }
    }

// Or read a large test input from the `src/Day07_test.txt` file:
    readInput("Day07_test").also { input ->
        check(part1(input) == 3749L)
    }

    readInput("Day07_test").also { input ->
        check(part2(input) == 11387L)
    }

// Read the input from the `src/Day07.txt` file.
    readInput("Day07").also { input ->
        part1(input).println()
        part2(input).println()
    }
}

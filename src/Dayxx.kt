import println
import readInput

fun main() {

    fun part1(input: List<String>): Long {
        return 0
    }

    fun part2(input: List<String>): Long {
       return 0
    }

// Or read a large test input from the `src/Day07_test.txt` file:
    readInput("Dayxx_test").also { input ->
        println(part1(input))
        check(part1(input) == 3749L)
    }

    readInput("Dayxx_test").also { input ->
        println(part2(input))
        check(part2(input) == 11387L)
    }

// Read the input from the `src/Day07.txt` file.
    readInput("Dayxx").also { input ->
        part1(input).println()
        part2(input).println()
    }
}

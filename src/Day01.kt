import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val lists = input.flatMap { line ->
            line.split("   ")
                .map { it.toInt() }
        }
        val firstList = lists.filterIndexed { index, _ -> index % 2 == 0 }.sorted()
        val secondList = lists.filterIndexed { index, _ -> index % 2 != 0 }.sorted()

        return firstList.foldIndexed(0) { index: Int, acc: Int, i: Int -> acc + (i - secondList[index]).absoluteValue }
    }

    fun part2(input: List<String>): Int {
        val lists = input.flatMap { line ->
            line.split("   ")
                .map { it.toInt() }
        }
        val firstList = lists.filterIndexed { index, _ -> index % 2 == 0 }.sorted()
        val secondList = lists.filterIndexed { index, _ -> index % 2 != 0 }.sorted()

        return firstList.foldIndexed(0) { index: Int, acc: Int, i: Int -> acc + (i * secondList.count { it == i }) }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    readInput("Day01_test").also { input ->
        check(part1(input) == 11)
    }

    readInput("Day01_test").also { input ->
        check(part2(input) == 31)
    }

    // Read the input from the `src/Day01.txt` file.
    readInput("Day01").also { input ->
        part1(input).println()
        part2(input).println()
    }
}

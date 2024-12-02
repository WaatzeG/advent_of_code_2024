import kotlin.math.absoluteValue

fun main() {

    fun getReports(input: List<String>) = input.map { line ->
        line.split(" ").map { it.toInt() }
    }

    fun isAscending(report: List<Int>) = report.toSet().sorted() == report
    fun isDescending(report: List<Int>) = report.toSet().sortedDescending() == report

    /**
     * Tests for safe distance between elements.
     */
    fun hasSafeLevels(report: List<Int>): Boolean {
        return report.foldIndexed(true) { index: Int, currentValue: Boolean, value: Int ->
            if (!currentValue) {
                false //don't bother checking anymore for this report
            } else if (index == 0) {
                true //no distance on first element
            } else {
                val distance = (report[index - 1] - value).absoluteValue
                distance in 1..3
            }
        }
    }

    /**
     * Return a sequence removing one element in each iteration from the original [report]. Elements are removed in
     * order, starting at the first.
     */
    fun applyDampening(report: List<Int>) = sequence<List<Int>> {
        var index = 0
        do {
            val reportToValidate = report.toMutableList().also { it.removeAt(index) }
            yield(reportToValidate)
            index++
        } while (index <= report.lastIndex)
    }

    /**
     * Tests all requirements for a safe report without dampening
     */
    fun reportIsSafe(report: List<Int>): Boolean {
        return (isAscending(report) || isDescending(report)) && hasSafeLevels(report)
    }

    /**
     * Tests all requirements for a safe report with dampening
     */
    fun reportIsSafeWithDampening(report: List<Int>): Boolean {
        return reportIsSafe(report) || applyDampening(report).any { reportIsSafe(it) }
    }

    fun part1(input: List<String>): Int {
        return getReports(input).count { report ->
            reportIsSafe(report)
        }
    }

    fun part2(input: List<String>): Int {
        return getReports(input).count { report ->
            reportIsSafeWithDampening(report)
        }
    }

// Or read a large test input from the `src/Day02_test.txt` file:
    readInput("Day02_test").also { input ->
        check(part1(input) == 2)
    }

    readInput("Day02_test").also { input ->
        check(part2(input) == 4)
    }

// Read the input from the `src/Day02.txt` file.
    readInput("Day02").also { input ->
        part1(input).println()
        part2(input).println()
    }

}

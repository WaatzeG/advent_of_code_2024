package Day05

import println
import readInput

fun main() {

    fun getRules(input: List<String>): List<Pair<Int, Int>> {
        return input.takeWhile { line -> line != "" }
            .map { rule -> rule.split("|").let { pair -> pair[0].toInt() to pair[1].toInt() } }
    }

    fun getUpdates(input: List<String>): List<List<Int>> {
        return input.dropWhile { it != "" }
            .filterNot { it == "" }
            .map { update -> update.split(",").map { it.toInt() } }
    }

    fun isValidUpdate(update: List<Int>, rules: List<Pair<Int, Int>>): Boolean {
        return update.all { page ->
            //must match all the rules
            rules.all { rule ->
                if (rule.first == page) {
                    update.indexOf(page) < update.indexOf(rule.second) || update.indexOf(rule.second) == -1
                } else {
                    true
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val rules = getRules(input)
        val updates = getUpdates(input)

        return updates.filter { update ->
            //collect all valid updates
            isValidUpdate(update, rules)
        }.sumOf { update ->
            update[update.lastIndex / 2]
        }
    }

    /**
     * Recursively call until the [update] is valid and return a valid update
     */
    fun correctUpdate(update: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
        return update.firstNotNullOfOrNull { page ->
            //find first rule violation, if any
            rules.filter { rule -> rule.first == page }
                .firstOrNull { rule -> update.indexOf(page) > update.indexOf(rule.second) && update.indexOf(rule.second) != -1 }
        }?.let { ruleViolation ->
            val correctedUpdate = update.toMutableList()
            val value = correctedUpdate[correctedUpdate.indexOf(ruleViolation.first)]
            correctedUpdate[correctedUpdate.indexOf(ruleViolation.first)] =
                correctedUpdate[correctedUpdate.indexOf(ruleViolation.second)]
            correctedUpdate[correctedUpdate.indexOf(ruleViolation.second)] = value

            //call recursively until valid
            correctUpdate(correctedUpdate, rules)
        } ?: update //no violations left
    }

    fun part2(input: List<String>): Int {
        val rules = getRules(input)
        val updates = getUpdates(input)

        val incorrectMiddles = updates.filter { update ->
            //collect all invalid updates
            !isValidUpdate(update, rules)
        }.map { update ->
            correctUpdate(update, rules)
        }.sumOf { update ->
            update[update.lastIndex / 2]
        }
        return incorrectMiddles
    }

// Or read a large test input from the `src/Day05_test.txt` file:
    readInput("Day05_test").also { input ->
        check(part1(input) == 143)
    }

    readInput("Day05_test").also { input ->
        check(part2(input) == 123)
    }

// Read the input from the `src/Day05.txt` file.
    readInput("Day05").also { input ->
        part1(input).println()
        part2(input).println()
    }
}

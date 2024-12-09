package Day08

import println
import readInput

fun main() {

    data class Position(val x: Int, val y: Int)

    fun getAntennas(input: List<String>): List<Pair<Char, Position>> {
        return input.flatMapIndexed { lineIndex: Int, line: String ->
            line.mapIndexedNotNull { charIndex, char ->
                if (char == '.') {
                    null
                } else {
                    Pair(char, Position(charIndex, lineIndex))
                }
            }
        }
    }

    fun isLegalPosition(position: Position, input: List<String>): Boolean {
        val max_X = input[0].lastIndex
        val max_Y = input.lastIndex
        return position.x >= 0 && position.y >= 0 && position.x <= max_X && position.y <= max_Y
    }

    fun doubleDistancePattern(
        input: List<String>,
        sourceAntenna: Pair<Char, Position>,
        targetAntenna: Pair<Char, Position>
    ): List<Position> {
        val distanceX = sourceAntenna.second.x - targetAntenna.second.x
        val distanceY = sourceAntenna.second.y - targetAntenna.second.y
        val antinodeX = sourceAntenna.second.x + distanceX
        val antinodeY = sourceAntenna.second.y + distanceY
        val antinodePosition = Position(antinodeX, antinodeY)

        return if (isLegalPosition(antinodePosition, input)) {
            listOf(antinodePosition)
        } else {
            emptyList()
        }
    }

    fun endlessPattern(
        input: List<String>,
        sourceAntenna: Pair<Char, Position>,
        targetAntenna: Pair<Char, Position>
    ): List<Position> {
        val distanceX = sourceAntenna.second.x - targetAntenna.second.x
        val distanceY = sourceAntenna.second.y - targetAntenna.second.y
        var iteration = 0 //since we start at ANY distance (also 0)
        return generateSequence {
            val antinodeX = sourceAntenna.second.x + (distanceX * iteration)
            val antinodeY = sourceAntenna.second.y + (distanceY * iteration)
            val antinodePosition = Position(antinodeX, antinodeY)
            if (isLegalPosition(antinodePosition, input)) {
                iteration += 1
                antinodePosition
            } else {
                null
            }
        }.toList()
    }

    /**
     * Get all antinodes for antennas in [antennaPositions], based on [antinodePatternCalculator]
     */
    fun getAntinodes(
        input: List<String>,
        antennaPositions: List<Pair<Char, Position>>,
        antinodePatternCalculator: (List<String>, Pair<Char, Position>, Pair<Char, Position>) -> List<Position>,
        antennaIndex: Int = 0
    ): Set<Position> {
        val sourceAntenna = antennaPositions[antennaIndex]
        //for each antenna, calculate antinodes created with all other antennas
        val antinodesForAntennas = (antennaPositions - sourceAntenna).flatMap { targetAntenna ->
            antinodePatternCalculator(input, sourceAntenna, targetAntenna)
        }.toSet()

        return if (antennaIndex < antennaPositions.lastIndex) {
            antinodesForAntennas + getAntinodes(input, antennaPositions, antinodePatternCalculator, antennaIndex + 1)
        } else {
            antinodesForAntennas
        }
    }

    fun part1(input: List<String>): Int {
        val positions = getAntennas(input)
        val groups = positions.groupBy { it.first }
        return groups.map { (_, antennaPositions) ->
            getAntinodes(input, antennaPositions, ::doubleDistancePattern)
        }.reduce(Set<Position>::plus)
            .count()
    }

    fun part2(input: List<String>): Int {
        val positions = getAntennas(input)
        val groups = positions.groupBy { it.first }
        return groups.map { (_, antennaPositions) ->
            getAntinodes(input, antennaPositions, ::endlessPattern)
        }.reduce(Set<Position>::plus)
            .count()
    }

// Or read a large test input from the `src/Day08_test.txt` file:
    readInput("Day08_test").also { input ->
        check(part1(input) == 14)
    }

    readInput("Day08_test").also { input ->
        check(part2(input) == 34)
    }

// Read the input from the `src/Day08.txt` file.
    readInput("Day08").also { input ->
        part1(input).println()
        part2(input).println()
    }
}

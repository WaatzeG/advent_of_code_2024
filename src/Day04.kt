private enum class Orientation(private val x: Int, private val y: Int) {
    Horizontal(1, 0), 
    Vertical(0, 1), 
    DownRight(1, 1), 
    DownLeft(-1, 1);

    fun nextChar(current: Int) = current + x
    fun nextLine(current: Int) = current + y
}

fun main() {

    fun findWordFromPosition(
        grid: List<String>,
        currentLine: Int,
        currentChar: Int,
        targetWord: String,
        orientation: Orientation,
        targetWordIndex: Int = 0,
    ): Boolean {
        return if (currentChar < 0 || currentLine > grid.lastIndex || currentChar > grid[currentLine].lastIndex) {
            false //out of bounds
        } else if (grid[currentLine][currentChar] == targetWord[targetWordIndex]) {
            //The current character matches the one we look for.
            //If this was the last letter in the word, we've found it.
            if (targetWordIndex == targetWord.lastIndex) {
                true
            } else {
                //The current character matches the one we look for, but we're not done yet. Traverse the grid further.
                val nextLine = orientation.nextLine(currentLine)
                val nextChar = orientation.nextChar(currentChar)
                val nextTargetWordIndex = targetWordIndex + 1
                findWordFromPosition(grid, nextLine, nextChar, targetWord, orientation, nextTargetWordIndex)
            }
        } else {
            false
        }
    }

    fun part1(input: List<String>): Int {
        val XMAS = "XMAS"
        //go over each character in each line, collecting the number of XMAS starting from that position
        //bi-directionality is achieved by reversing the target word
        return input.foldIndexed(0) { lineIndex: Int, prevValue: Int, line: String ->
            line.foldIndexed(prevValue) { charIndex: Int, prevValue: Int, _: Char ->
                listOf(
                    findWordFromPosition(input, lineIndex, charIndex, XMAS, Orientation.Horizontal),
                    findWordFromPosition(input, lineIndex, charIndex, XMAS, Orientation.Vertical),
                    findWordFromPosition(input, lineIndex, charIndex, XMAS, Orientation.DownLeft),
                    findWordFromPosition(input, lineIndex, charIndex, XMAS, Orientation.DownRight),
                    findWordFromPosition(input, lineIndex, charIndex, XMAS.reversed(), Orientation.Horizontal),
                    findWordFromPosition(input, lineIndex, charIndex, XMAS.reversed(), Orientation.Vertical),
                    findWordFromPosition(input, lineIndex, charIndex, XMAS.reversed(), Orientation.DownLeft),
                    findWordFromPosition(input, lineIndex, charIndex, XMAS.reversed(), Orientation.DownRight),
                ).count { found -> found }.let { timesFound -> prevValue + timesFound }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val MAS = "MAS"
        return input.foldIndexed(0) { lineIndex: Int, prevValue: Int, line: String ->
            line.foldIndexed(prevValue) { charIndex: Int, prevValue: Int, _: Char ->
                if ((
                    //first check if MAS/SAM is started from this position in down-right orientation
                    findWordFromPosition(input, lineIndex, charIndex, MAS, Orientation.DownRight) ||
                    findWordFromPosition(input, lineIndex, charIndex, MAS.reversed(), Orientation.DownRight)
                ) && (
                    //found it, check if there is also a down-left MAS/SAM starting 2 chars ahead
                    findWordFromPosition(input, lineIndex, charIndex + 2, MAS, Orientation.DownLeft) ||
                    findWordFromPosition(input, lineIndex, charIndex + 2, MAS.reversed(), Orientation.DownLeft))
                ) {
                    //found an X-MAS
                    prevValue + 1
                } else {
                    //no X-MAS on this position
                    prevValue
                }
            }
        }
    }

// Or read a large test input from the `src/Day04_test.txt` file:
    readInput("Day04_test").also { input ->
        check(part1(input) == 18)
    }

    readInput("Day04_test").also { input ->
        check(part2(input) == 9)
    }

// Read the input from the `src/Day04.txt` file.
    readInput("Day04").also { input ->
        part1(input).println()
        part2(input).println()
    }
}

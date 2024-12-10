package Day09

import println
import readInput

fun main() {

    fun transformToDiskLayout(input: String): List<Int?> {
        var numberIndex = 0
        return input.flatMapIndexed { index, char ->
            val number = char.digitToInt()
            val range = (0 until number)
            if (index % 2 == 0) {
                //used space mode
                range.map { numberIndex }.also {
                    numberIndex += 1
                }
            } else {
                //free space mode
                range.map { null }
            }
        }
    }

    /**
     * Checks if defragmentation is complete by checking if the end of the disklayout is a continuous block of
     * empty space
     */
    fun emptySpaceOptimized(diskLayout: List<Int?>): Boolean {
        val firstFreeSpaceBlock = diskLayout.indexOfFirst { it == null }
        return diskLayout.subList(firstFreeSpaceBlock, diskLayout.size).all { it == null }
    }

    tailrec fun optimizeFreeSpace(diskLayout: List<Int?>): List<Int?> {
        return if (!emptySpaceOptimized(diskLayout)) {
            val indexOfLastFileBlock = diskLayout.indexOfLast { it != null }
            val valueOfLastFileBlock = diskLayout[indexOfLastFileBlock]
            val indexOfFirstEmptyBlock = diskLayout.indexOfFirst { it == null }
            val mutable = diskLayout.toMutableList()
            mutable[indexOfFirstEmptyBlock] = valueOfLastFileBlock
            mutable[indexOfLastFileBlock] = null
            optimizeFreeSpace(mutable)
        } else {
            diskLayout
        }
    }

    /**
     * Find first empty block of at least [size]
     */
    fun firstEmptyBlock(diskLayout: MutableList<Int?>, size: Int): IntRange? {
        val occupationString = diskLayout.joinToString("") { if (it != null) "X" else "." }
        val emptyRegex = Regex((0 until size).joinToString("") { "\\." })
        val firstEmptySpace = emptyRegex.findAll(occupationString).firstOrNull()?.range
        return firstEmptySpace
    }

    fun defragmentBlocks(diskLayout: List<Int?>): List<Int?> {
        val mutatedDisklayout = diskLayout.toMutableList()
        //go over each file in reverse order
        val fileGroups = diskLayout.filterNotNull()
            .groupBy { it }
            .entries
            .reversed()
        fileGroups.forEach { (file, blocks) ->
            val fileSize = blocks.size
            val firstBlockOfFile = diskLayout.indexOf(file)
            val emptyBlock = firstEmptyBlock(mutatedDisklayout, fileSize)
            if (emptyBlock != null && emptyBlock.first < firstBlockOfFile) {
                //move file ID
                emptyBlock.forEach {
                    mutatedDisklayout[it] = file
                }
                (firstBlockOfFile until firstBlockOfFile + fileSize).forEach {
                    mutatedDisklayout[it] = null
                }
            }
        }
        return mutatedDisklayout
    }

    fun part1(input: List<String>): Long {
        val diskLayout = transformToDiskLayout(input[0])
        val optimized = optimizeFreeSpace(diskLayout)
        return optimized.filterNotNull().foldIndexed(0) { index, acc, value -> acc + (index * value) }
    }

    fun part2(input: List<String>): Long {
        val diskLayout = transformToDiskLayout(input[0])
        val defragmented = defragmentBlocks(diskLayout)
        return defragmented.foldIndexed(0L) { index, acc, value ->
            if (value != null) {
                acc + (value * index)
            } else {
                acc
            }
        }
    }

// Or read a large test input from the `src/Day07_test.txt` file:
    readInput("Day09_test").also { input ->
        println(part1(input))
        check(part1(input) == 1928L)
    }

    readInput("Day09_test").also { input ->
        println(part2(input))
        check(part2(input) == 2858L)
    }

// Read the input from the `src/Day07.txt` file.
    readInput("Day09").also { input ->
        part1(input).println()
        part2(input).println()
    }
}

package com.github.freekdb.aoc.xxiv


fun main() {
    val verbose = false
    val part = 2
    val descriptions = listOf("The resulting filesystem checksum is", "The resulting filesystem checksum is")

//    Day09(verbose).solvePuzzle(part, inputId = "example 1", descriptions)
    Day09(verbose).solvePuzzle(part, inputId = "puzzle input", descriptions)
    // Part 1 -- The resulting filesystem checksum is: 89312744865.
    // That's not the right answer; your answer is too low.
    // reddit: "ID can be a multi-digit number"
    // Part 1 -- The resulting filesystem checksum is: 6283170117911.
    // Part 2 -- The resulting filesystem checksum is: 6307653242596.
}


class Day09(verbose: Boolean) : BasePuzzleSolver(verbose) {
    override fun solvePart1(inputLines: List<String>): Long {
        val defragmentedDiskMap = initializeDiskMap(inputLines)

        var finished = false
        while (!finished) {
            val firstEmptySpaceIndex = defragmentedDiskMap.indexOfFirst { it is DiskBlock.EmptySpace }
            val lastFileBlocksIndex = defragmentedDiskMap.indexOfLast { it is DiskBlock.FileBlock }

            if (firstEmptySpaceIndex < lastFileBlocksIndex) {
                defragmentedDiskMap[firstEmptySpaceIndex] = defragmentedDiskMap[lastFileBlocksIndex]
                defragmentedDiskMap[lastFileBlocksIndex] = DiskBlock.EmptySpace
            } else {
                finished = true
            }

            if (verbose) {
                println(defragmentedDiskMap)
                println()
            }
        }

        val filesystemChecksum = defragmentedDiskMap.mapIndexed { objectIndex, diskBlock ->
            if (diskBlock is DiskBlock.FileBlock) objectIndex * diskBlock.id else 0L
        }.sum()

        return filesystemChecksum
    }

    override fun solvePart2(inputLines: List<String>): Long {
        val defragmentedDiskMap = initializeDiskMap(inputLines)

        val sortedFileIds = defragmentedDiskMap
            .mapNotNull { diskBlock -> if (diskBlock is DiskBlock.FileBlock) diskBlock.id else null }
            .distinct()
            .sortedDescending()

        sortedFileIds.forEach { fileId ->
            val diskBlockIndices = defragmentedDiskMap
                .mapIndexedNotNull { blockIndex, diskBlock ->
                    if (diskBlock is DiskBlock.FileBlock && diskBlock.id == fileId) blockIndex else null
                }

            val firstDiskBlockIndex = diskBlockIndices.first()

            val firstFreeBlockIndex = (0..<firstDiskBlockIndex).firstOrNull { firstBlockIndex ->
                (firstBlockIndex..<(firstBlockIndex + diskBlockIndices.size)).all { blockIndex ->
                    defragmentedDiskMap[blockIndex] is DiskBlock.EmptySpace
                }
            }

            if (firstFreeBlockIndex != null) {
                diskBlockIndices.forEach { diskBlockIndex ->
                    val freeBlockIndex = firstFreeBlockIndex + diskBlockIndex - firstDiskBlockIndex
                    defragmentedDiskMap[freeBlockIndex] = defragmentedDiskMap[diskBlockIndex]
                    defragmentedDiskMap[diskBlockIndex] = DiskBlock.EmptySpace
                }
            }

            if (verbose) {
                println(defragmentedDiskMap)
                println()
            }
        }

        val filesystemChecksum = defragmentedDiskMap.mapIndexed { objectIndex, diskBlock ->
            if (diskBlock is DiskBlock.FileBlock) objectIndex * diskBlock.id else 0L
        }.sum()

        return filesystemChecksum
    }

    private fun initializeDiskMap(inputLines: List<String>): MutableList<DiskBlock> {
        val diskMap = inputLines.first().mapIndexed { characterIndex, inputCharacter ->
            val length = (inputCharacter - '0').toLong()

            if (characterIndex % 2 == 0) {
                (0..<length).map { DiskBlock.FileBlock(id = characterIndex / 2L) }
            } else {
                (0..<length).map { DiskBlock.EmptySpace }
            }
        }.flatten()

        if (verbose) {
            println(diskMap)
            println()
        }

        return diskMap.toMutableList()
    }

    override val inputLinesExamples = listOf(
        """
2333133121414131402
"""
    )


    sealed interface DiskBlock {
        data class FileBlock(val id: Long) : DiskBlock
        data object EmptySpace : DiskBlock
    }
}

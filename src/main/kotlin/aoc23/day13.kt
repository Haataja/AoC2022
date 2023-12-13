package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day13.txt"

    val patterns = File(path).readText().split(Regex("(\\r?\\n){2,}")).map {
        val pattern = mutableListOf<MutableList<Char>>()
        it.split(Regex("(\\r?\\n)+")).forEachIndexed { row, s ->
            pattern.add(row, s.toMutableList())
        }
        pattern
    }

    fun checkColumnsBackwards(index: Int, breakOut: Int, pattern: MutableList<MutableList<Char>>): Int {
        return if (breakOut - index < 0 || breakOut + 1 + index > pattern[0].lastIndex) {
            1
        } else {
            if (List(pattern.size) { i -> pattern[i][breakOut - index] } ==
                List(pattern.size) { i -> pattern[i][breakOut + 1 + index] }) {
                1 + checkColumnsBackwards(index + 1, breakOut, pattern)
            } else {
                0
            }
        }
    }

    fun checkColumnsForwards(index: Int, max: Int, pattern: MutableList<MutableList<Char>>): Int {
        return if (index < pattern[0].lastIndex) {
            if (List(pattern.size) { i -> pattern[i][index] } == List(pattern.size) { i -> pattern[i][index + 1] }) {
                if (index > 0) {
                    val sameCol = checkColumnsBackwards(1, index, pattern)
                    if (sameCol < index && sameCol + index < pattern[0].lastIndex) {
                        checkColumnsForwards(index + 1, max, pattern)
                    } else {
                        if (index + 1 > max) {
                            checkColumnsForwards(index + 1, index + 1, pattern)
                        } else {
                            checkColumnsForwards(index + 1, max, pattern)
                        }
                    }
                } else {
                    checkColumnsForwards(1, 1, pattern)
                }
            } else {
                checkColumnsForwards(index + 1, max, pattern)
            }
        } else {
            max
        }
    }

    fun checkRowsBackwards(index: Int, breakOut: Int, pattern: MutableList<MutableList<Char>>): Int {
        return if (breakOut - index < 0 || breakOut + 1 + index > pattern.lastIndex) {
            1
        } else {
            if (pattern[breakOut - index] == pattern[breakOut + 1 + index]) {
                1 + checkRowsBackwards(index + 1, breakOut, pattern)
            } else {
                0
            }
        }
    }

    fun checkRowsForwards(index: Int, max: Int, pattern: MutableList<MutableList<Char>>): Int {
        return if (index < pattern.lastIndex) {
            if (pattern[index] == pattern[index + 1]) {
                if (index > 0) {
                    val sameRow = checkRowsBackwards(1, index, pattern)
                    if (sameRow < index && sameRow + index < pattern.lastIndex) {
                        checkRowsForwards(index + 1, max, pattern)
                    } else {
                        if (index + 1 > max) {
                            checkRowsForwards(index + 1, index + 1, pattern)
                        } else {
                            checkRowsForwards(index + 1, max, pattern)
                        }
                    }
                } else {
                    checkRowsForwards(1, 1, pattern)
                }
            } else {
                checkRowsForwards(index + 1, max, pattern)
            }
        } else {
            max
        }
    }

    fun part1(patterns: List<MutableList<MutableList<Char>>>): Int {
        return patterns.sumOf {
            // check for vertical and horizontal
            checkColumnsForwards(0, 0, it) + 100 * checkRowsForwards(0, 0, it)
        }
    }

    fun differs(first: List<Char>, second: List<Char>): Int {
        var different = 0
        for (i in first.indices) {
            if (first[i] != second[i]) {
                different++
            }
        }
        return different
    }

    fun checkColumnsBackwardsWithSmudge(
        index: Int,
        breakOut: Int,
        allMatch: Boolean,
        pattern: MutableList<MutableList<Char>>
    ): Int {
        return if (breakOut - index < 0 || breakOut + 1 + index > pattern[0].lastIndex) {
            if (allMatch) -1 else 1
        } else {
            if (List(pattern.size) { i -> pattern[i][breakOut - index] } ==
                List(pattern.size) { i -> pattern[i][breakOut + 1 + index] }) {
                1 + checkColumnsBackwardsWithSmudge(index + 1, breakOut, allMatch, pattern)
            } else if (differs(List(pattern.size) { i -> pattern[i][breakOut - index] },
                    List(pattern.size) { i -> pattern[i][breakOut + 1 + index] }) == 1
            ) {
                1 + checkColumnsBackwardsWithSmudge(index + 1, breakOut, false, pattern)
            } else {
                0
            }
        }
    }

    fun checkColumnsForwardsWithSmudge(
        index: Int,
        max: Int,
        allMatch: Boolean,
        pattern: MutableList<MutableList<Char>>
    ): Int {
        return if (index < pattern[0].lastIndex) {
            if (List(pattern.size) { i -> pattern[i][index] } == List(pattern.size) { i -> pattern[i][index + 1] }) {
                if (index > 0) {
                    val sameCol = checkColumnsBackwardsWithSmudge(1, index, allMatch, pattern)
                    if (sameCol < index && sameCol + index < pattern[0].lastIndex) {
                        checkColumnsForwardsWithSmudge(index + 1, max, allMatch, pattern)
                    } else {
                        if (index + 1 > max) {
                            checkColumnsForwardsWithSmudge(index + 1, index + 1, allMatch, pattern)
                        } else {
                            checkColumnsForwardsWithSmudge(index + 1, max, allMatch, pattern)
                        }
                    }
                } else {
                    checkColumnsForwardsWithSmudge(1, 0, allMatch, pattern)
                }
            } else if (differs(List(pattern.size) { i -> pattern[i][index] },
                    List(pattern.size) { i -> pattern[i][index + 1] }) == 1 && allMatch
            ) {
                if (index > 0) {
                    val sameCol = checkColumnsBackwardsWithSmudge(1, index, false, pattern)
                    //println("same col $sameCol ja index $index ${pattern[0].lastIndex}")
                    if (sameCol < index && sameCol + index < pattern[0].lastIndex) {
                        checkColumnsForwardsWithSmudge(index + 1, max, true, pattern)
                    } else {
                        if (index + 1 > max) {
                            checkColumnsForwardsWithSmudge(index + 1, index + 1, true, pattern)
                        } else {
                            checkColumnsForwardsWithSmudge(index + 1, max, true, pattern)
                        }
                    }
                } else {
                    checkColumnsForwardsWithSmudge(1, 1, true, pattern)
                }
            } else {
                checkColumnsForwardsWithSmudge(index + 1, max, allMatch, pattern)
            }
        } else {
            max
        }
    }

    fun checkRowsBackwardsWithSmudge(
        index: Int,
        breakOut: Int,
        allMatch: Boolean,
        pattern: MutableList<MutableList<Char>>
    ): Int {
        return if (breakOut - index < 0 || breakOut + 1 + index > pattern.lastIndex) {
            if (allMatch) -1 else 1
        } else {
            if (pattern[breakOut - index] == pattern[breakOut + 1 + index]) {
                1 + checkRowsBackwardsWithSmudge(index + 1, breakOut, allMatch, pattern)
            } else if (allMatch && differs(pattern[breakOut - index], pattern[breakOut + 1 + index]) == 1) {
                1 + checkRowsBackwardsWithSmudge(index + 1, breakOut, false, pattern)
            } else {
                0
            }
        }
    }

    fun checkRowsForwardsWithSmudge(
        index: Int,
        max: Int,
        allMatch: Boolean,
        pattern: MutableList<MutableList<Char>>
    ): Int {
        return if (index < pattern.lastIndex) {
            if (pattern[index] == pattern[index + 1]) {
                if (index > 0) {
                    val sameRow = checkRowsBackwardsWithSmudge(1, index, allMatch, pattern)
                    if (sameRow < index && sameRow + index < pattern.lastIndex) {
                        checkRowsForwardsWithSmudge(index + 1, max, allMatch, pattern)
                    } else {
                        if (index + 1 > max) {
                            checkRowsForwardsWithSmudge(index + 1, index + 1, allMatch, pattern)
                        } else {
                            checkRowsForwardsWithSmudge(index + 1, max, allMatch, pattern)
                        }
                    }
                } else {
                    checkRowsForwardsWithSmudge(1, 0, allMatch, pattern)
                }
            } else if (differs(pattern[index], pattern[index + 1]) == 1 && allMatch) {
                if (index > 0) {
                    val sameRow = checkRowsBackwardsWithSmudge(1, index, false, pattern)
                    if (sameRow < index && sameRow + index < pattern.lastIndex) {
                        checkRowsForwardsWithSmudge(index + 1, max, true, pattern)
                    } else {
                        if (index + 1 > max) {
                            checkRowsForwardsWithSmudge(index + 1, index + 1, true, pattern)
                        } else {
                            checkRowsForwardsWithSmudge(index + 1, max, true, pattern)
                        }
                    }
                } else {
                    checkRowsForwardsWithSmudge(1, 1, true, pattern)
                }
            } else {
                checkRowsForwardsWithSmudge(index + 1, max, allMatch, pattern)
            }
        } else {
            max
        }
    }

    fun part2(patterns: List<MutableList<MutableList<Char>>>): Int {
        return patterns.sumOf {
            100 * checkRowsForwardsWithSmudge(0, 0, true, it) + checkColumnsForwardsWithSmudge(0, 0, true, it)
        }
    }

    println(part1(patterns))
    println(part2(patterns))
}

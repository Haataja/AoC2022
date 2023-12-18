package me.haataja.aoc23

import Direction
import print2DWitObjects
import java.io.File
import java.lang.Long.parseLong

fun main() {

    val path = "src/main/input/aoc23/day18.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n)+"))

    fun part1(rows: List<String>): Int {
        var current = 0 to 0 // start from low enough
        val edges = mutableListOf<Pair<Direction, Pair<Int, Int>>>()
        rows.mapIndexed { index, row ->
            val split = row.split(Regex("\\s+"))
            var dir = when (split[0]) {
                "R" -> Direction.RIGHT
                "L" -> Direction.LEFT
                "U" -> Direction.UP
                else -> Direction.DOWN
            }
            repeat(split[1].toInt()) { r ->
                current = current.first + dir.dy to current.second + dir.dx
                if ((dir == Direction.RIGHT || dir == Direction.LEFT) && r == split[1].toInt() - 1) {
                    dir = if (rows[index + 1][0] == 'U') {
                        Direction.UP
                    } else {
                        Direction.DOWN
                    }
                }
                edges.add(dir to current)
            }
        }
        val points = edges.map { it.second }
        val colSize = points.maxOf { it.second + 1 }
        val rowSize = points.maxOf { it.first + 1 }
        var sum = points.size
        for (row in 0 until rowSize) {
            var inside = false
            for (col in 0 until colSize) {
                if (!inside &&
                    points.contains(row to col) &&
                    edges.find { it.second.first == row && it.second.second == col }!!.first == Direction.UP
                ) {
                    inside = true
                } else if (inside && points.contains(row to col) && edges.find { it.second.first == row && it.second.second == col }!!.first == Direction.DOWN) {
                    inside = false
                } else {
                    if (inside && !points.contains(row to col)) {
                        sum++
                    }
                }
            }
        }
        return sum
    }

    fun part2(rows: List<String>): Int {
        val newDirections = rows.map { row ->
            val hex = row.split(Regex("\\s+"))[2].replace("#", "").replace(")", "").replace("(", "")
            val dir = when (hex.last()) {
                '0' -> Direction.RIGHT
                '1' -> Direction.DOWN
                '2' -> Direction.LEFT
                else -> Direction.UP
            }
            val hexNumber = parseLong(hex.subSequence(0 until hex.lastIndex).toString(), 16)
            dir to hexNumber
        }
        println(newDirections)
        var current = 0L to 0L
        val corner = mutableListOf<Pair<Long, Long>>()
        newDirections.forEach {
            current = current.first + it.second * it.first.dy to current.second + it.second * it.first.dx
            corner.add(current)
        }
        /*println(corner)

        val downsize = corner.map{((it.first - corner.minOf { y -> y.first } )/ 10000).toInt() to (it.second / 1000).toInt()}
        println(downsize)
        val array = Array(downsize.maxOf { it.first } + 1) { Array(downsize.maxOf { it.second } + 1) { '-' }.toMutableList() }.toMutableList()
        print2DWitObjects(array, downsize, 'x')*/
        return 0
    }

    println(part1(rows))
    //println(part2(rows))
}
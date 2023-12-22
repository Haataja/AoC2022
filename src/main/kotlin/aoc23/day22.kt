package me.haataja.aoc23

import java.io.File

fun main() {

    data class Point(
        var x: Int,
        var y: Int,
        var z: Int
    )

    data class Brick(
        val points: MutableList<Point>
    ){
        fun maxOfX(): Int{
            return points.maxOf { it.x }
        }
        fun maxOfY(): Int{
            return points.maxOf { it.y }
        }

        fun maxOfZ(): Int{
            return points.maxOf { it.z }
        }

        fun minOfZ(): Int{
            return points.minOf { it.z }
        }
    }

    val path = "src/main/input/aoc23/day22.txt"
// val grid = mutableListOf<MutableList<MutableList<>>>
    val rows = File(path).readText().split(Regex("(\\r?\\n)+")).map {
        val brick = mutableListOf<Point>()
        val split = it.split("~")
        val start = split[0].trim().split(",")
        val end = split[1].trim().split(",")
        val startPoint = Point(start[0].toInt(), start[1].toInt(), start[2].toInt())
        val endPoint = Point(end[0].toInt(), end[1].toInt(), end[2].toInt())
        brick.add(startPoint)
        if (endPoint.x - startPoint.x > 1) {
            val xDifference = endPoint.x - startPoint.x
            repeat(xDifference) { index ->
                brick.add(
                    Point(
                        startPoint.x + xDifference - index,
                        startPoint.y,
                        startPoint.z
                    )
                )
            }
        }
        if (endPoint.y - startPoint.y > 1) {
            val yDifference = endPoint.y - startPoint.y
            repeat(yDifference) { index ->
                brick.add(
                    Point(
                        startPoint.x,
                        startPoint.y + yDifference - index,
                        startPoint.z
                    )
                )
            }
        }
        if (endPoint.z - startPoint.z > 1) {
            val zDifference = endPoint.z - startPoint.z
            repeat(zDifference) { index ->
                brick.add(
                    Point(
                        startPoint.x,
                        startPoint.y,
                        startPoint.z + zDifference - index
                    )
                )
            }
        }
        brick.add(endPoint)
        Brick(brick.distinct().toMutableList())
    }

    fun part1(): Int {
        //println(rows)
        val empty = mutableListOf<Int>()
        for (z in 0..rows.maxOf { brick -> brick.maxOfZ()}) {
            var isEmpty = true
            for (y in 0..rows.maxOf { brick -> brick.maxOfY() }) {
                for (x in 0..rows.maxOf { brick -> brick.maxOfX() }) {
                    if (rows.flatMap { it.points }.contains(Point(x, y, z))) {
                        print("B")
                        isEmpty = false
                    } else {
                        print(".")
                    }
                }
                println()
            }
            println()
            if (isEmpty && z > 0) {
                empty.add(z)
            }
        }
        rows.sortedBy { brick -> brick.minOfZ() }
        println("${rows[0].minOfZ()} ${rows[1].minOfZ()}")
        for (brick in rows) {
            var brickCanBeMoved = true
            while (brickCanBeMoved) {
                val original = brick.points.map { point -> Point(point.x, point.y, point.z) }
                for (point in brick.points) {
                    if (point.z < 1 || rows.flatMap { it.points }.contains(Point(point.x, point.y, point.z - 1))) {
                        brickCanBeMoved = false
                    } else {
                        point.z -= 1
                    }
                }
                if (!brickCanBeMoved) {
                    brick.points.forEachIndexed { index, _ -> brick.points[index] = original[index] }
                }
            }
        }

        println("---------------------")
        for (z in 0..rows.maxOf { brick -> brick.maxOfZ() }) {
            for (y in 0..rows.maxOf { brick -> brick.maxOfY() }) {
                for (x in 0..rows.maxOf { brick -> brick.maxOfX() }) {
                    if (rows.flatMap { it.points }.contains(Point(x, y, z))) {
                        print("B")
                    } else {
                        print(".")
                    }
                }
                println()
            }
            println()
        }


        return 0
    }

    fun part2(): Int {
        return 0
    }

    println(part1())
    println(part2())
}
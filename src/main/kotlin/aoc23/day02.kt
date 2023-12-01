package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day02.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n)+"))

    fun part1(rows: List<String>): Int {
      return 0
    }

    fun part2(rows: List<String>): Int {
        return 0
    }

    println(part1(rows))
    println(part2(rows))
}
package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day09.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n)+")).map { it.trim().split(Regex("\\s+")).map { number -> number.toLong() } }

    fun part1(rows: List<List<Long>>, backwards: Boolean): Long {
        return rows.sumOf {
            val sumRow = mutableListOf<List<Long>>()
            sumRow.add(it)
            while (!sumRow[sumRow.lastIndex].all { number -> number == sumRow[sumRow.lastIndex][0] }) {
                sumRow.add(sumRow[sumRow.lastIndex].windowed(2).map { (a, b) -> b - a })
            }
            var newNumber = 0L
            for (i in sumRow.indices) {
                if (backwards){
                    newNumber = sumRow[sumRow.lastIndex - i].first() - newNumber
                } else {
                    newNumber += sumRow[sumRow.lastIndex - i].last()
                }
            }
            newNumber
        }
    }


    println(part1(rows, false))
    println(part1(rows, true))

    // this is recursion, so trying to solve with recursion
    fun recursiveFun(list: List<Long>, backwards: Boolean): Long {
        return if (list.all { it == 0L }){
            0L // last thing at the end
        } else { // how to get there
            val nexRow = list.windowed(2){(a, b) -> b - a}
            if (backwards){
                recursiveFun(nexRow, true) + list.last()
            } else {
                list.first() - recursiveFun(nexRow, false)
            }
        }
    }
    println("recursive: ${rows.sumOf { recursiveFun(it, true) }}")
    println("recursive: ${rows.sumOf { recursiveFun(it, false) }}")
}

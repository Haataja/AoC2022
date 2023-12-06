package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day06.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n)+"))

    fun part1(rows: List<String>): Long {
        val races = rows.map {
            it.substring(it.indexOf(":") + 1).trim().split(Regex("\\s+"))
        }
        var multiply = 1.toLong()
        for(raceIndex in races[0].indices){
            val duration = races[0][raceIndex].toLong()
            val distanceToBeat = races[1][raceIndex].toLong()
            var holdButton = 0
            var waysToWin = 0.toLong()
            while(holdButton < duration) {
                holdButton++
                val distance = (duration - holdButton) * holdButton
                if(distance > distanceToBeat){
                    // println("duration: $duration , distance: $distanceToBeat, travellded: $distance")
                    waysToWin++
                }
            }
            multiply *= waysToWin
        }
        return multiply
    }

    fun part2(rows: List<String>): Long {
        val races = rows.map { it.replace(Regex("\\s"), "") }
        return part1(races)
    }

    println(part1(rows))
    println(part2(rows))
}
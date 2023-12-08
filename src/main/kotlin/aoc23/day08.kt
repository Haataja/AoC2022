package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day08.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n)+"))
    val instructions = rows[0].trim()
    val maps = rows.subList(1, rows.size).map {
        val split = it.split("=")
        val key = split[0].trim()
        val leftAndRight = split[1].trim().replace(")","").replace("(","").split(",")
        key to (Pair(leftAndRight[0].trim(), leftAndRight[1].trim()))
    }.toMap()

    fun part1(rows: Map<String, Pair<String, String>>): Int {
        println(rows)
        var current = "AAA"
        var steps = 0
        var index = 0
        while (current != "ZZZ"){
            if(index > instructions.lastIndex){
                index = 0
            }
            current = if (instructions[index] == 'L'){rows[current]!!.first} else {rows[current]!!.second}
            steps++
            index++
        }
        return steps
    }

    fun part2(rows: Map<String, Pair<String, String>>): Long {
        val end = rows.keys.filter { it[2] == 'A' }.map {
            var current = it
            var steps = 0
            var index = 0
            while (current[2] != 'Z'){
                if(index > instructions.lastIndex){
                    index = 0
                }
                current = if (instructions[index] == 'L'){rows[current]!!.first} else {rows[current]!!.second}
                steps++
                index++
            }
            steps.toLong()
        }
        // result is found as least common multiple of all route steps
        var result = end[0]
        for (i in 1..end.lastIndex) {
            result = leastCommonMultiple(result, end[i])
        }
        return result
    }

    println(part1(maps))
    println(part2(maps))

}

fun leastCommonMultiple(a: Long, b: Long): Long { // lcm = |a*b|/ greatest common divisor
    var i = 1
    while (i <= a && i <= b) {
        i++
        if (a % i == 0.toLong() && b % i == 0.toLong()) {
            return (a * b) / i
        }
    }
    return (a * b)
}
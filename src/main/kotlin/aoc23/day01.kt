package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day01.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n)+"))
    val numbers = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )

    fun part1(rows: List<String>): Int {
        return rows.sumOf {
            val digits = it.filter { c -> c.isDigit() }
            "${digits[0]}${digits[digits.length - 1]}".toInt()
        }
    }

    fun part2(rows: List<String>): Int {
        return rows.sumOf {
            var first = ""
            var second = ""
            for (i in it.indices) {
                val s = it.substring(0, i + 1)
                if (s.any { c -> c.isDigit() }) {
                    first = s[i].toString()
                    break
                } else if (numbers.keys.any { key -> s.contains(key) }) {
                    val key1 = numbers.keys.filter { key -> s.contains(key) }
                    first = numbers[key1[0]].toString()
                    break
                }
            }
            val anotherWay = it.reversed()
            for (i in anotherWay.indices) {
                var s = anotherWay.substring(0, i + 1)
                println(s)
                if (s.any { c -> c.isDigit() }) {
                    second = s[i].toString()
                    break
                } else if (numbers.keys.any { key -> s.reversed().contains(key) }) {
                    val key1 = numbers.keys.filter { key -> s.reversed().contains(key) }
                    second = numbers[key1[0]].toString()
                    break
                }
            }
            println("$first$second".toInt())
            "$first$second".toInt()
        }
    }

    println(part1(rows))
    println(part2(rows))
}
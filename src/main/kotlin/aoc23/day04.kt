package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day04.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n)+"))

    fun part1(rows: List<String>): Int {
        return rows.sumOf {
            val numbers = it.substring(it.indexOf(":") + 1).split("|")
            val winningNumbers = numbers[0].trim().split(Regex("\\s+"))
            val myNumbers = numbers[1].trim().split(Regex("\\s+"))

            var winningValue = 0
            myNumbers.forEach { myNumber ->
                if (winningNumbers.contains(myNumber.trim())) {
                    winningValue = if (winningValue == 0) {
                        1
                    } else {
                        winningValue * 2
                    }
                }
            }
            // println(winningValue)
            winningValue
        }
    }

    fun part2(rows: List<String>): Int {
        val cardMap = mutableMapOf<Int, Int>()
        rows.forEach {
            val card = it.substring(0, it.indexOf(":")).filter { c -> c.isDigit() }.toInt()
            val numbers = it.substring(it.indexOf(":") + 1).split("|")
            val winningNumbers = numbers[0].trim().split(Regex("\\s+"))
            val myNumbers = numbers[1].trim().split(Regex("\\s+"))

            cardMap[card] = cardMap[card]?.plus(1) ?: 1
            myNumbers.filter { myNumber -> winningNumbers.contains(myNumber) }
                .forEachIndexed { index, _ ->
                    cardMap[card + index + 1] = cardMap[card + index + 1]?.plus(cardMap[card]!!) ?: cardMap[card]!!
                }
            //println("card map: $cardMap")
        }
        return cardMap.values.sum()
    }

    println(part1(rows))
    println(part2(rows))
}
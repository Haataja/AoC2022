package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day02.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n)+"))
    val limit = mapOf("red" to 12, "green" to 13, "blue" to 14)

    fun part1(rows: List<String>): Int {
        return rows.sumOf {
            val game = it.substring(0, it.indexOf(":")).filter { c -> c.isDigit() }.toInt()
            val rounds = it.substring(it.indexOf(":") + 1).split(";")

            var isPossible = true
            for(round in rounds){
                val cubes = round.split(",")
                for (cube in cubes){
                    val amount = cube.filter { c -> c.isDigit() }.toInt()
                    val colour = cube.filter { c -> !c.isDigit() }.trim()
                    if(limit[colour]!! < amount){
                        isPossible = false
                        break
                    }
                }
                // println("$round : $isPossible")
                if(!isPossible){
                    break
                }
            }

            if(isPossible){
                game
            } else {
                0
            }
        }
    }

    fun part2(rows: List<String>): Int {
        return rows.sumOf {
            val rounds = it.substring(it.indexOf(":") + 1).split(";")

            val minCubes = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
            for(round in rounds){
                val cubes = round.split(",")
                for (cube in cubes){
                    val amount = cube.filter { c -> c.isDigit() }.toInt()
                    val colour = cube.filter { c -> !c.isDigit() }.trim()
                    if(minCubes[colour]!! < amount){
                        minCubes[colour] = amount
                    }
                }
            }

            minCubes.values.reduce { accumulator, element ->
                accumulator * element
            }
        }
    }

    println(part1(rows))
    println(part2(rows))
}
package me.haataja.aoc23

import java.io.File
import kotlin.math.abs
import kotlin.math.pow

fun main() {

    val path = "src/main/input/aoc23/day11.txt"

    val universe = mutableListOf<MutableList<Char>>()
    File(path).readText().split(Regex("(\\r?\\n)+")).forEachIndexed { index, s ->
        val row = mutableListOf<Char>()
        s.trim().forEachIndexed { ci, c ->
            row.add(c)
        }

        if (row.isNotEmpty()){
            universe.add(row)
        }
    }
    val initPlanets = mutableListOf<Pair<Int, Int>>()
    val planets = mutableListOf<Pair<Int, Int>>()

    fun printUniverse(rows: List<List<Char>>) {
        for (row in rows.indices) {
            for (column in rows[row].indices) {
                print(rows[row][column])
            }
            println()
        }
    }

    fun expandTheSpace() {
        val xIndices = mutableListOf<Int>()
        for (col in universe[0].indices) {
            val spaceY = List(universe.size) { index -> universe[index][col] }
            if (spaceY.all { it == '.' }) {
                xIndices.add(col)
            }
        }
        xIndices.forEachIndexed { index, i -> universe.forEach {row -> row.add(i + 1 + index, '.') } }
        val yIndices = mutableListOf<Int>()
        for (row in universe.indices) {
            if (universe[row].all { it == '.' }) {
                yIndices.add(row)
            }
        }
        val space = mutableListOf<Char>()
        repeat(universe[0].size) { space.add('.') }
        yIndices.forEachIndexed { index, i ->  universe.add(i + index , space) }
    }

    fun part1(rows: List<List<Char>>): Int {
        // printUniverse(rows)
        for (row in rows.indices) {
            for (column in rows[row].indices) {
                if(universe[row][column] == '#'){
                    initPlanets.add(Pair(row, column))
                }
            }
        }

        expandTheSpace()

        for (row in rows.indices) {
            for (column in rows[row].indices) {
                if(universe[row][column] == '#'){
                    planets.add(Pair(row, column))
                }
            }
        }

        //printUniverse(rows)
        var sum = 0
        for (planetIndex in planets.indices){
            for (secondPlanetIndex in planetIndex.. planets.lastIndex) {
                val dist = abs(planets[planetIndex].first - planets[secondPlanetIndex].first) +
                    abs(planets[planetIndex].second - planets[secondPlanetIndex].second)
                // println("$planetIndex $secondPlanetIndex $dist ")
                sum += dist
            }
        }
        return sum
    }

    fun part2(): Long {
        println(initPlanets) // initial planets
        println(planets) // after one expansion

        val newNewPlanets = mutableListOf<Pair<Long, Long>>() // after 1000000 expansion
        for (planetIndex in initPlanets.indices){
            val x = initPlanets[planetIndex].first +
                999999 * (planets[planetIndex].first - initPlanets[planetIndex].first)
            val y = initPlanets[planetIndex].second +
                999999* (planets[planetIndex].second - initPlanets[planetIndex].second)
            newNewPlanets.add(Pair(x.toLong(),y.toLong()))
        }
        println(newNewPlanets)
        var sum = 0L
        for (planetIndex in newNewPlanets.indices){
            for (secondPlanetIndex in planetIndex..newNewPlanets.lastIndex) {
                val dist = abs(newNewPlanets[planetIndex].first - newNewPlanets[secondPlanetIndex].first) +
                    abs(newNewPlanets[planetIndex].second - newNewPlanets[secondPlanetIndex].second)
                sum += dist
            }
        }
        return  sum
    }

    println(part1(universe))
    println(part2())
}
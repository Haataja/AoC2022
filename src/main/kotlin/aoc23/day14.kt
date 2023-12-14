package me.haataja.aoc23

import java.io.File
import java.util.*

fun main() {

    val path = "src/main/input/aoc23/day14.txt"

    val array = File(path).readText().split(Regex("(\\r?\\n)+")).map {
        it.toMutableList()
    }.toMutableList()

    fun getStones(
        array: MutableList<MutableList<Char>>
    ): MutableList<Pair<Int, Int>> {
        val stones = mutableListOf<Pair<Int, Int>>()
        for (row in array.indices) {
            for (column in array[row].indices) {
                if (array[row][column] == 'O') {
                    stones.add(Pair(row, column))
                }
            }
        }
        return stones
    }

    fun replaceRollingStones(array: MutableList<MutableList<Char>>): MutableList<MutableList<Char>> {
        val part1Change = mutableListOf<MutableList<Char>>()
        array.forEachIndexed { row, chars ->
            val tempRow = mutableListOf<Char>()
            chars.forEach { c ->
                if (c == 'O') {
                    tempRow.add('.')
                } else {
                    tempRow.add(c)
                }
            }
            part1Change.add(row, tempRow)
        }
        return part1Change
    }

    fun moveToNorth(
        stones: MutableList<Pair<Int, Int>>,
        withoutRollingStones: MutableList<MutableList<Char>>
    ): MutableList<Pair<Int, Int>>{
        val newStones = mutableListOf<Pair<Int, Int>>()
        for (stone in stones) {
            var canMove = true
            var currentPlace = stone
            while (canMove) {
                if (currentPlace.first > 0
                    && withoutRollingStones[currentPlace.first - 1][currentPlace.second] == '.'
                    && newStones.none { pair -> pair.first == currentPlace.first - 1 && pair.second == currentPlace.second }
                ) {
                    currentPlace = Pair(currentPlace.first - 1, currentPlace.second)
                } else {
                    canMove = false
                }
            }
            newStones.add(currentPlace)
        }
        return newStones
    }
    fun moveToSouth(
        stones: MutableList<Pair<Int, Int>>,
        withoutRollingStones: MutableList<MutableList<Char>>
    ) : MutableList<Pair<Int, Int>> {
        val newStones = mutableListOf<Pair<Int, Int>>()
        for (stone in stones) {
            var canMove = true
            var currentPlace = stone
            while (canMove) {
                if (currentPlace.first < withoutRollingStones.lastIndex
                    && withoutRollingStones[currentPlace.first + 1][currentPlace.second] == '.'
                    && newStones.none { pair -> pair.first == currentPlace.first + 1 && pair.second == currentPlace.second }
                ) {
                    currentPlace = Pair(currentPlace.first + 1, currentPlace.second)
                } else {
                    canMove = false
                }
            }
            newStones.add(currentPlace)
        }
        return newStones
    }

    fun moveToEast(
        stones: MutableList<Pair<Int, Int>>,
        withoutRollingStones: MutableList<MutableList<Char>>
    ): MutableList<Pair<Int,Int>>{
        val newStones = mutableListOf<Pair<Int, Int>>()
        for (stone in stones) {
            var canMove = true
            var currentPlace = stone
            while (canMove) {
                if (currentPlace.second < withoutRollingStones[0].lastIndex
                    && withoutRollingStones[currentPlace.first][currentPlace.second + 1] == '.'
                    && newStones.none { pair -> pair.first == currentPlace.first && pair.second == currentPlace.second + 1 }
                ) {
                    currentPlace = Pair(currentPlace.first, currentPlace.second + 1)
                } else {
                    canMove = false
                }
            }
            newStones.add(currentPlace)
        }
        return newStones
    }
    fun moveToWest(
        stones: MutableList<Pair<Int, Int>>,
        withoutRollingStones: MutableList<MutableList<Char>>,
    ): MutableList<Pair<Int,Int>> {
        val newStones = mutableListOf<Pair<Int, Int>>()
        for (stone in stones) {
            var canMove = true
            var currentPlace = stone
            while (canMove) {
                if (currentPlace.second > 0
                    && withoutRollingStones[currentPlace.first][currentPlace.second - 1] == '.'
                    && newStones.none { pair -> pair.first == currentPlace.first && pair.second == currentPlace.second - 1 }
                ) {
                    currentPlace = Pair(currentPlace.first, currentPlace.second - 1)
                } else {
                    canMove = false
                }
            }
            newStones.add(currentPlace)
        }
        return newStones
    }

    fun part1(array: MutableList<MutableList<Char>>): Int {
        val stones = getStones(array)
        val withoutRollingStones = replaceRollingStones(array)
        val newStones = moveToNorth(stones, withoutRollingStones)
        return newStones.sumOf {
            withoutRollingStones.size - it.first
        }
    }

    fun part2(): Int {
        val stones = getStones(array)
        val withoutRollingStones = replaceRollingStones(array)

        var newStones = mutableListOf<Pair<Int,Int>>()
        for (i in 1..1000000000){
            println("running ${String.format(Locale.GERMANY, "%,d", i)}/1000000000")
            newStones = if(newStones.isEmpty()){
                moveToNorth(stones, withoutRollingStones)
            } else {
                moveToNorth(newStones, withoutRollingStones)
            }
            newStones = moveToWest(newStones, withoutRollingStones)
            newStones = moveToSouth(newStones, withoutRollingStones)
            newStones = moveToEast(newStones, withoutRollingStones)
        }

       return newStones.sumOf {
            withoutRollingStones.size - it.first
        }
    }

    println(part1(array))
    println(part2())
}
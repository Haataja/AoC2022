package me.haataja.aoc23

import java.io.File
import java.lang.RuntimeException
import kotlin.math.roundToInt

fun main() {

    val path = "src/main/input/aoc23/day10.txt"
    val size = 140
    val array = Array(size) { Array(size) { '.' } }
    var start = Pair(0,0)
    var dir = 0
    File(path).readText().split(Regex("(\\r?\\n)+")).forEachIndexed { index, s ->
        s.trim().forEachIndexed { ci, c ->
            array[index][ci] = c
            if(c == 'S'){
                start = Pair(index, ci)
            }
        }
    }

    fun move(current: Pair<Int, Int>, map:Array<Array<Char>>): Pair<Int,Int>{
        // from left = - J 7
        return when (dir) {
            0 -> {
                when (map[current.first][current.second]){
                    '-' -> {
                        dir = 0
                        map[current.first][current.second] = '0'
                        Pair(current.first, current.second + 1)
                    }

                    'J' -> {
                        dir = 1
                        map[current.first][current.second] = '1'
                        Pair(current.first - 1, current.second)
                    }

                    '7' -> {
                        dir = 3
                        map[current.first][current.second] = '3'
                        Pair(current.first + 1, current.second)
                    }

                    else -> {
                        throw RuntimeException("Not valid char ${map[current.first][current.second]} at $current ")
                    }
                }
            }
            1 -> { // from down | F 7

                when (map[current.first][current.second]){
                    '|' -> {
                        dir = 1
                        map[current.first][current.second] = '1'
                        Pair(current.first - 1, current.second )
                    }
                    'F' -> {
                        dir = 0
                        map[current.first][current.second] = '1'
                        Pair(current.first, current.second + 1)
                    }
                    '7' -> {
                        dir = 2
                        map[current.first][current.second] = '1'
                        Pair(current.first, current.second - 1)
                    }
                    else -> {
                        throw RuntimeException("Not valid char ${map[current.first][current.second]} at $current ")
                    }
                }
            }
            2 -> { // from right - L F
                when (map[current.first][current.second]){
                    '-' -> {
                        dir = 2
                        map[current.first][current.second] = '2'
                        Pair(current.first, current.second - 1)
                    }
                    'L' -> {
                        dir = 1
                        map[current.first][current.second] = '1'
                        Pair(current.first - 1, current.second)
                    }
                    'F' -> {
                        dir = 3
                        map[current.first][current.second] = '3'
                        Pair(current.first + 1, current.second)
                    }
                    else -> {
                        throw RuntimeException("Not valid char ${map[current.first][current.second]} at $current ")
                    }
                }
            }
            else -> { // from up | L J
                when (map[current.first][current.second]){
                    '|' -> {
                        dir = 3
                        map[current.first][current.second] = '3'
                        Pair(current.first + 1, current.second)
                    }
                    'L' -> {
                        dir = 0
                        map[current.first][current.second] = '3'
                        Pair(current.first, current.second + 1)
                    }
                    'J' -> {
                        dir = 2
                        map[current.first][current.second] = '3'
                        Pair(current.first, current.second - 1)
                    }
                    else -> {
                        throw RuntimeException("Not valid char ${map[current.first][current.second]} at $current ")
                    }
                }
            }
        }
    }

    fun part1(array: Array<Array<Char>>): Int {
        var currentStep = if(array[start.first][start.second + 1] == '-' || array[start.first][start.second + 1] == 'J' || array[start.first][start.second + 1] == '7'){
            dir = 0
            Pair(start.first, start.second + 1)
        } else if(array[start.first - 1][start.second] == '|' || array[start.first - 1][start.second] == 'F' || array[start.first - 1][start.second] == '7'){
            dir = 1
            Pair(start.first - 1 , start.second)
        } else if(array[start.first][start.second - 1] == '-' || array[start.first][start.second - 1] == 'L' || array[start.first][start.second - 1] == 'F'){
            dir = 2
            Pair(start.first, start.second - 1)
        } else if(array[start.first + 1][start.second] == '|' || array[start.first + 1][start.second] == 'F' || array[start.first + 1][start.second] == '7') {
            dir = 3
            Pair(start.first + 1, start.second)
        } else {
            throw RuntimeException("Cannot figure out starting direction!")
        }

        var numberOfSteps = 0
        while (array[currentStep.first][currentStep.second] != 'S'){
            numberOfSteps++
            currentStep = move(currentStep, array)
        }

        return(numberOfSteps / 2.0).roundToInt()
    }

    fun part2(array: Array<Array<Char>>): Int {
        println("start: $start")
        var currentStep = if(array[start.first + 1][start.second] == '|' || array[start.first + 1][start.second] == 'F' || array[start.first + 1][start.second] == '7'){
            dir = 3
            Pair(start.first + 1, start.second)
        } else if(array[start.first - 1][start.second] == '|' || array[start.first - 1][start.second] == 'F' || array[start.first - 1][start.second] == '7'){
            dir = 1
            Pair(start.first - 1 , start.second)
        } else if(array[start.first][start.second - 1] == '-' || array[start.first][start.second - 1] == 'L' || array[start.first][start.second - 1] == 'F'){
            dir = 2
            Pair(start.first, start.second - 1)
        } else if(array[start.first][start.second + 1] == '-' || array[start.first][start.second + 1] == 'J' || array[start.first][start.second + 1] == '7') {
            dir = 0
            Pair(start.first, start.second + 1)
        } else {
            throw RuntimeException("Cannot figure out starting direction!")
        }
        println("first dir: $dir")

        val pipeline = mutableListOf<Pair<Int,Int>>()
        while (array[currentStep.first][currentStep.second] != 'S'){
            pipeline.add(currentStep)
            currentStep = move(currentStep, array)
        }
        array[start.first][start.second] = dir.toChar()

        var count = false
        var enclosed = 0
        for (row in array.indices){
            for (column in array[row].indices){
                if(array[row][column] == '1'){
                    count = true
                } else if (count && array[row][column] == '3') {
                    count = false
                } else {
                    if (count && !pipeline.contains(Pair(row, column))){
                        enclosed++
                    }
                }
                // only for visualisation to check the logic
               if(pipeline.contains(Pair(row, column))) {
                    print(array[row][column])
                } else {
                    if (count){print("*")} else {print(".")}
                }
            }
            println()
        }

        return enclosed
    }

    // do not run at the same time, as move function manipulates the input data.
    // println(part1(array))
    println(part2(array))
}
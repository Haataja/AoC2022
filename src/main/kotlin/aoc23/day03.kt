package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day03.txt"
    val size = 140
    val array = Array(size) { Array(size) { '-' } }
    File(path).readText().split(Regex("(\\r?\\n)+")).forEachIndexed { index, s ->
        s.trim().forEachIndexed { ci, c -> array[index][ci] = c }
    }

    fun checkAdjacentSymbols(row: Int, col: Int): Boolean{
        return row > 0 && (!array[row - 1][col].isDigit() && array[row - 1][col] != '.' ||
                col > 0 && !array[row - 1][col - 1].isDigit() && array[row - 1][col - 1] != '.' ||
                col < size - 1 && !array[row - 1][col + 1].isDigit() && array[row - 1][col + 1] != '.' ) || // symbols up from the number
                (col > 0 && !array[row][col - 1].isDigit() && array[row][col - 1] != '.') ||  // symbol in front of number
                (col < size - 1 && !array[row][col + 1].isDigit() && array[row][col + 1] != '.') ||  // symbol after number
                row < size - 1 && (!array[row + 1][col].isDigit() && array[row + 1][col] != '.' ||
                col > 0 && !array[row + 1][col - 1].isDigit() && array[row + 1][col - 1] != '.' ||
                col < size - 1 && !array[row + 1][col + 1].isDigit() && array[row + 1][col + 1] != '.' ) // symbols down from the number
    }

    fun part1(array: Array<Array<Char>>): Int {
        var sum = 0
        for (row in array.indices) {
            var currentNumber = ""
            var isPartNumber = false
            for (col in array[row].indices) {
                if(array[row][col] == '.' || !array[row][col].isDigit()){
                    if(isPartNumber){
                        // println("Adding part number: $currentNumber")
                        sum += currentNumber.toInt()
                    }

                    currentNumber = ""
                    isPartNumber = false
                } else if (array[row][col].isDigit()){
                    currentNumber += array[row][col]
                    if (checkAdjacentSymbols(row, col)){
                        isPartNumber = true
                    }
                }
            }
            // if the number is last thing at the row
            if(isPartNumber){
                // println("Adding part number: $currentNumber")
                sum += currentNumber.toInt()
            }
        }
        return sum
    }

    fun addToFront(
        row: Int,
        col: Int
    ) : String {
        var number = ""
        var column = col - 1
        var char = array[row][column]
        while (char.isDigit()) {
            number = char + number
            column--
            if (column >= 0) {
                char = array[row][column]
            } else {
                break
            }
        }
        return number
    }

    fun addToBack(
        row: Int,
        col: Int
    ) : String {
        var number = ""
        var column = col + 1
        var char = array[row][column]
        while (char.isDigit()) {
            number += char
            column++
            if(column < size - 1){
                char = array[row][column]
            } else {
                break
            }
        }
        return number
    }

    fun numberOfNumbers(row: Int, col: Int): List<Int>{
        val numbers = mutableListOf<Int>()
        // same row before the *
        if(col > 0 && array[row][col - 1].isDigit()){
            numbers.add(addToFront(row, col).toInt())
        }

        // same row after the *
        if(col < size - 1 && array[row][col + 1].isDigit()){
            numbers.add(addToBack(row, col).toInt())
        }

        // upper row, straight up
        if(row > 0 && array[row - 1][col].isDigit()){
            numbers.add( (addToFront(row - 1, col) + addToBack(row - 1, col - 1)).toInt())
        } else if (row > 0){ // upper row right and left
            val numberRight = addToBack(row - 1, col)
            if(numberRight.isNotBlank()) {
                numbers.add(numberRight.toInt())
            }

            val numberLeft = addToFront(row - 1, col)
            if(numberLeft.isNotBlank()) {
                numbers.add(numberLeft.toInt())
            }
        }

        // lower row, straight down
        if(row < size - 1 && array[row + 1][col].isDigit()){
            numbers.add((addToFront(row + 1, col) + addToBack(row + 1, col - 1)).toInt())
        } else if (row < size - 1){ // lower row, left and right
            val numberRight = addToBack(row + 1, col)
            if(numberRight.isNotBlank()) {
                numbers.add(numberRight.toInt())
            }

            val numberLeft = addToFront(row + 1, col)
            if(numberLeft.isNotBlank()) {
                numbers.add(numberLeft.toInt())
            }
        }
        return numbers
    }

    fun part2(array: Array<Array<Char>>): Int {
        var sum = 0
        for (row in array.indices) {
            for (col in array[row].indices) {
                if(array[row][col] == '*' ){
                    val numberOfNumbers =  numberOfNumbers(row, col)
                    if(numberOfNumbers.size == 2){
                        sum += numberOfNumbers[0] * numberOfNumbers[1]
                    }

                }
            }
        }
        return sum
    }

    println(part1(array))
    println(part2(array))
}

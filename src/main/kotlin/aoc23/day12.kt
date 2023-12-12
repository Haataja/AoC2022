package me.haataja.aoc23

import java.io.File
import java.time.LocalDateTime
import java.time.LocalTime

fun main() {

    val path = "src/main/input/aoc23/day12.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n)+"))

    fun checkMach(permutation: String, groups: List<Int>) : Boolean {
        var groupIndex = 0
        var charIndex = 0
        while(charIndex < permutation.length){
            if(permutation[charIndex] == '#'){
                if (charIndex + groups[groupIndex] - 1 < permutation.length
                    && permutation.subSequence(charIndex,charIndex + groups[groupIndex]).all { it == '#' }
                    && (charIndex + groups[groupIndex] < permutation.length
                    && permutation[charIndex + groups[groupIndex] ]  == '.' || charIndex + groups[groupIndex] >= permutation.length)){
                    //println("checking $groupIndex ${permutation.subSequence(charIndex,charIndex + groups[groupIndex])}")
                    if(groupIndex == groups.lastIndex){
                        return if(permutation.subSequence(charIndex + groups[groupIndex],permutation.length).all { char -> char == '.' } ){
                            //println("checking $permutation true $groups")
                            true
                        } else {
                            //println("checking $permutation false $groups")
                            false
                        }
                    } else {
                        charIndex += groups[groupIndex]
                        groupIndex++
                    }
                } else {
                    //println("checking $permutation false $groups")
                    return false
                }
            }
            charIndex++
        }
        return false
    }

    fun checkPartial(initPermutation: String, groups: List<Int>) : Boolean {
        var groupIndex = 0
        var charIndex = 0
        val permutation = initPermutation.substring(0, initPermutation.indexOf('?'))
        while(charIndex < permutation.length){
            if(permutation[charIndex] == '#'){
                if (charIndex + groups[groupIndex] - 1 < permutation.length
                    && permutation.subSequence(charIndex,charIndex + groups[groupIndex]).all { it == '#' }
                    && (charIndex + groups[groupIndex] < permutation.length
                        && permutation[charIndex + groups[groupIndex] ]  == '.' || charIndex + groups[groupIndex] >= permutation.length)){
                    //println("checking $groupIndex ${permutation.subSequence(charIndex,charIndex + groups[groupIndex])}"
                    if(groupIndex == groups.lastIndex){
                        return permutation.subSequence(charIndex + groups[groupIndex],permutation.length).all { char -> char == '.' }
                    } else {
                        charIndex += groups[groupIndex]
                        groupIndex++
                    }
                } else if(charIndex + groups[groupIndex] - 1 >= permutation.length) {
                    return true
                } else {
                    return false
                }
            }
            charIndex++
        }
        return true
    }

    fun part1(rows: List<String>): Int {
        val sum = rows.sumOf{
            val split = it.split(Regex("\\s"))
            val pre = mutableListOf<String>()
            val permutations = mutableListOf<String>()
            val groups = split[1].split(",").map { number -> number.toInt() }
            permutations.add(split[0])
            while (permutations.last().contains("?")){
                pre.addAll(permutations)
                permutations.clear()
                for (thing in pre){
                    permutations.add(thing.replaceFirst('?','.'))
                    permutations.add(thing.replaceFirst('?','#'))
                }
                pre.clear()
            }
            //println(permutations)
            //println(groups)
            permutations.sumOf { permutation ->
                if (checkMach(permutation, groups)){
                    //println("permutation: $permutation")
                    1.toInt()
                } else {
                    0
                }
            }
        }
        return sum
    }

    fun recursiveFunction(permutation: String, group: List<Int>): Long{
        println(permutation)
        return if(!permutation.contains("?")){
            if (checkMach(permutation,group)){
                1
            } else {
                0
            }
        } else {
            if(checkPartial(permutation, group)){
                recursiveFunction(permutation.replaceFirst('?','.'), group) +
                    recursiveFunction(permutation.replaceFirst('?','#'), group)
            } else {
                0
            }
        }

    }

    fun part2(rows: List<String>): Long {
        val start = LocalTime.now().toSecondOfDay()
        val sum = rows.sumOf{
            val split = it.split(Regex("\\s"))
            val groups = split[1].split(",").map { number -> number.toInt() }
            val allGroups = mutableListOf<Int>()
            repeat(5){allGroups.addAll(groups)}
            val first = "${split[0]}?${split[0]}?${split[0]}?${split[0]}?${split[0]}"
            val somethin = recursiveFunction(first, allGroups)
            println("$it $somethin")
            somethin

        }
        val stop = LocalTime.now().toSecondOfDay()
        println("${LocalTime.ofSecondOfDay((stop - start).toLong())}")
        return sum
    }

    //println(part1(rows))
    println(part2(rows))
}
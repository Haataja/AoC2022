package me.haataja.aoc23

import java.io.File

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
    fun recursiveFunction(permutation: String, group: List<Int>): Long{
        //println(permutation)
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

    // had to look this map solution from internet, because without it completion took too much time
    val map = mutableMapOf<String,MutableMap<List<Int>, Long>>()
    fun tryHard(permutation: String, groups: List<Int>): Long{
        if(map.keys.contains(permutation)){
            if(map[permutation]!!.keys.contains(groups)){
                return map[permutation]!![groups]!!
            }
        }

        if(permutation.isBlank()){
            return if (groups.isEmpty()){
                1
            } else {
                0
            }
        }

        val p = if (permutation[0] == '.'){
            tryHard(permutation.substring(1), groups)
        } else if (permutation[0] == '?') {
            tryHard("#${permutation.substring(1)}", groups) + tryHard(".${permutation.substring(1)}", groups)
        } else {
            if(groups.isNotEmpty()){
                val group = groups[0]
                if (group == permutation.length && permutation.all { it == '?' || it == '#' }){
                    if (groups.size > 1){
                        0
                    } else {
                        1
                    }
                }else if(permutation.length > group  && permutation.substring(0, group).all { it == '?' || it == '#' }){
                    val newGroups = groups.subList(1, groups.size)
                    if(permutation[group] == '.'){
                        tryHard(permutation.substring(group + 1), newGroups)
                    } else if(permutation[group] == '?'){
                        tryHard(".${permutation.substring(group + 1)}", newGroups)
                    } else {
                        0
                    }
                } else {
                    0
                }
            } else {
                0
            }
        }
        map.put(permutation, mutableMapOf(groups to p))
        return p
    }

    fun part1(rows: List<String>): Long {
        val sum = rows.sumOf{
            val split = it.split(Regex("\\s"))
            val groups = split[1].split(",").map { number -> number.toInt() }
            /* First brute force
            val pre = mutableListOf<String>()
            val permutations = mutableListOf<String>()
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

             */
            // after working on part two did the recursive way
            recursiveFunction(split[0], groups)
        }
        return sum
    }

    fun part2(rows: List<String>): Long {
        val sum = rows.sumOf{
            val split = it.split(Regex("\\s"))
            val groups = split[1].split(",").map { number -> number.toInt() }
            val allGroups = mutableListOf<Int>()
            repeat(5){allGroups.addAll(groups)}
            val first = "${split[0]}?${split[0]}?${split[0]}?${split[0]}?${split[0]}"
            val somethin = tryHard(first, allGroups)
            println("$it $somethin")
            somethin

        }
        return sum
    }

    println(part1(rows))
    println(part2(rows))
}
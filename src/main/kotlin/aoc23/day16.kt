package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day16.txt"
    val array = File(path).readText().split(Regex("(\\r?\\n)+")).map {
        it.toMutableList()
    }.toMutableList()

    data class Light(
        var direction: Int,
        val steps: Pair<Int, Int>
    )

    fun moveLight(
        light: MutableList<Light>,
        map: MutableList<MutableList<Char>>,
        addLights: MutableList<MutableList<Light>>,
        removeLights: MutableList<MutableList<Light>>,
        usedLights: MutableList<Pair<Int, Pair<Int, Int>>>
    ) {
        val currentStep = light.last().steps
        val currentDirection = light.last().direction
        when (currentDirection) {
            0 -> {
                if (currentStep.second < map[0].lastIndex) {
                    val next = currentStep.first to currentStep.second + 1
                    if (map[next.first][next.second] == '\\') {
                        if(!light.contains(Light(3, next))){
                            light.add(Light(3, next))
                        } else {
                            removeLights.add(light)
                        }
                    } else if (map[next.first][next.second] == '/') {
                        if(!light.contains(Light(1, next))){
                            light.add(Light(1, next))
                        } else {
                            removeLights.add(light)
                        }
                    } else if (map[next.first][next.second] == '|') {
                        if (next.first > 0) {
                            if(!usedLights.contains(1 to next)){
                                usedLights.add(1 to next)
                                addLights.add(mutableListOf(Light(1, next)))
                            }
                        }
                        if(!light.contains(Light(3, next))){
                            light.add(Light(3, next))
                        } else {
                            removeLights.add(light)
                        }
                    } else {
                        if(!light.contains(Light(0, next))){
                            light.add(Light(0, next))
                        } else {
                            removeLights.add(light)
                        }
                    }
                } else {
                    removeLights.add(light)
                }
            }

            1 -> {
                if (currentStep.first > 0) {
                    val next = currentStep.first - 1 to currentStep.second
                    if (map[next.first][next.second] == '\\') {
                        if(!light.contains(Light(2, next))){
                            light.add(Light(2, next))
                        } else {
                            removeLights.add(light)
                        }

                    } else if (map[next.first][next.second] == '/') {
                        if(!light.contains(Light(0, next))){
                            light.add(Light(0, next))
                        } else {
                            removeLights.add(light)
                        }

                    } else if (map[next.first][next.second] == '-') {
                        if (next.second > 0) {
                            if(!usedLights.contains(2 to next)){
                                usedLights.add(2 to next)
                                addLights.add(mutableListOf(Light(2, next)))
                            }
                        }
                        if(!light.contains(Light(0, next))){
                            light.add(Light(0, next))
                        } else {
                            removeLights.add(light)
                        }
                    } else {
                        if(!light.contains(Light(1, next))){
                            light.add(Light(1, next))
                        } else {
                            removeLights.add(light)
                        }
                    }
                } else {
                    removeLights.add(light)
                }
            }

            2 -> {
                if (currentStep.second > 0) {
                    val next = currentStep.first to currentStep.second - 1
                    if (map[next.first][next.second] == '\\') {
                        if(!light.contains(Light(1, next))){
                            light.add(Light(1, next))
                        } else {
                            removeLights.add(light)
                        }

                    } else if (map[next.first][next.second] == '/') {
                        if(!light.contains(Light(3, next))){
                            light.add(Light(3, next))
                        } else {
                            removeLights.add(light)
                        }

                    } else if (map[next.first][next.second] == '|') {
                        if (next.first > 0) {
                            if(!usedLights.contains(1 to next)){
                                usedLights.add(1 to next)
                                addLights.add(mutableListOf(Light(1, next)))
                            }
                        }
                        if(!light.contains(Light(3, next))){
                            light.add(Light(3, next))
                        } else {
                            removeLights.add(light)
                        }
                    } else {
                        if(!light.contains(Light(2, next))){
                            light.add(Light(2, next))
                        } else {
                            removeLights.add(light)
                        }
                    }
                } else {
                    removeLights.add(light)
                }
            }

            else -> {
                if (currentStep.first < map.lastIndex) {
                    val next = currentStep.first + 1 to currentStep.second
                    if (map[next.first][next.second] == '\\') {
                        if(!light.contains(Light(0, next))){
                            light.add(Light(0, next))
                        } else {
                            removeLights.add(light)
                        }

                    } else if (map[next.first][next.second] == '/') {
                        if(!light.contains(Light(2, next))){
                            light.add(Light(2, next))
                        } else {
                            removeLights.add(light)
                        }

                    } else if (map[next.first][next.second] == '-') {
                        if (next.second > 0) {
                            if (!usedLights.contains(2 to next)){
                                usedLights.add(2 to next)
                                addLights.add( mutableListOf(Light(2,next)))
                            }
                        }
                        if(!light.contains(Light(0, next))){
                            light.add(Light(0, next))
                        } else {
                            removeLights.add(light)
                        }

                    } else {
                        if(!light.contains(Light(3, next))){
                            light.add(Light(3, next))
                        } else {
                            removeLights.add(light)
                        }
                    }
                } else {
                    removeLights.add(light)
                }
            }

        }
    }

    fun part1(array: MutableList<MutableList<Char>>, start: Light): Int {
        val lights = mutableListOf(mutableListOf(start) )
        val energized = mutableListOf<Pair<Int, Int>>()
        val usedLights = mutableListOf<Pair<Int, Pair<Int, Int>>>()
        var i = 0
        while (lights.size> 0 && i < 5000) {
            // println("$i ${lights.size}")
            val addLight = mutableListOf<MutableList<Light>>()
            val removeLight = mutableListOf<MutableList<Light>>()
            lights.forEach {
                moveLight(it, array, addLight, removeLight, usedLights)
            }
            lights.addAll(addLight)
            energized.addAll(lights.flatMap { it.map { light: Light -> light.steps }}.filter { !energized.contains(it) })
            lights.removeAll(removeLight)
            i++
        }
        //println(energized.distinct())
        // energized.addAll(lights.flatMap { it.map { light: Light -> light.steps } }.filter { !energized.contains(it) })
        // print2DWitObjects(array, energized, '#')
        return energized.filter { it.first >= 0 && it.second >= 0 }.distinct().size
    }

    fun part2(array: MutableList<MutableList<Char>>): Int {
        val options = mutableListOf<Light>()
        // left and rigth edge
        for (i in array.indices){
            options.add(Light(0, (i to -1)))
            options.add(Light(2, (i to array[0].lastIndex + 1)))
        }
        // up and down
        for (i in array[0].indices){
            options.add(Light(1, (array.lastIndex + 1 to i)))
            options.add(Light(3, (-1 to i)))
        }
        return options.maxOf {
            println("${options.indexOf(it)} / ${options.lastIndex}")
            part1(array, it)
        }
    }

    println(part1(array, Light(0, (0 to -1))))
    // produces 7316 real value is 7315 and runs about 3h should be redone
    // println(part2(array))
}
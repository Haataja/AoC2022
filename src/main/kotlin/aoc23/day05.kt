package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day05.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n){2,}"))
    val seedsToSoil = mapToMap(rows, 1)
    val soilToFertilizer = mapToMap(rows, 2)
    val fertilizerToWater = mapToMap(rows, 3)
    val waterToLight = mapToMap(rows, 4)
    val lightToTemp = mapToMap(rows, 5)
    val tempToHumidity = mapToMap(rows, 6)
    val humidityToLocation = mapToMap(rows, 7)
    val seeds = rows[0].substring(rows[0].indexOf(":") + 1).trim().split(Regex("\\s+")).map { it.toLong() }

    fun fromSeedToLocation(it: Long): Long {
        val soil = findDestinationOrNull(seedsToSoil, it) ?: it
        val fert = findDestinationOrNull(soilToFertilizer, soil) ?: soil
        val water = findDestinationOrNull(fertilizerToWater, fert) ?: fert
        val light = findDestinationOrNull(waterToLight, water) ?: water
        val temp = findDestinationOrNull(lightToTemp, light) ?: light
        val humidity = findDestinationOrNull(tempToHumidity, temp) ?: temp
        return findDestinationOrNull(humidityToLocation, humidity) ?: humidity
    }

    fun part1(seeds: List<Long>): Long? {
        return seeds.minOfOrNull {
            fromSeedToLocation(it)
        }
    }

    fun part2(rows: List<Long>): Long {
        var min = 0 as Long
         rows.withIndex().groupBy { it.index / 2 }.values.forEach { range ->
            println(range)
            for (index in  0 until  range[1].value ) {
                val location = fromSeedToLocation(range[0].value + index)
                min = if (min == 0.toLong() || location < min){
                    location
                } else {
                    min
                }
           }
        }
        return min
    }

    println(part1(seeds))
    // slow as f*ck run at your own risk.
    // println(part2(seeds))
}

fun mapToMap(
    rows: List<String>,
    index: Int
) : List<List<Long>> {
    return rows[index].split(Regex("(\\r?\\n)+")).filter { !it.contains("-") }.map {
        it.split(Regex("\\s+")).map { number -> number.toLong() }
    }
}

fun findDestinationOrNull(map: List<List<Long>>, source: Long): Long? {
    val dsc = map.find { dsc ->  (dsc[1] until dsc[1] + dsc[2]).contains(source) }
    return dsc?.get(0)?.plus (source.minus (dsc[1]))
}
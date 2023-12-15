package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day15.txt"
    val rows = File(path).readText().split(",")

    fun hash(it: String): Int {
        var value = 0
        it.forEach { c ->
            value = (value + c.code.toByte().toInt()) * 17 % 256
        }
        return value
    }

    fun part1(rows: List<String>): Int {
        return rows.sumOf {
            hash(it)
        }
    }

    fun part2(rows: List<String>): Int {
        val boxes = mutableMapOf<Int, MutableList<Pair<String, Int>>>()
        rows.forEach {
            if(it.contains("=")){
                val split = it.split("=")
                val box = hash(split[0])
                if(boxes.keys.contains(box) && boxes[box]!!.any { pair -> pair.first == split[0] }){
                    val index = boxes[box]!!.indexOfFirst { pair -> pair.first == split[0] }
                    boxes[box]!!.removeAt(index)
                    boxes[box]!!.add(index, Pair(split[0], split[1].toInt()))
                } else {
                    if (boxes[box].isNullOrEmpty()){
                        boxes[box] = mutableListOf(Pair(split[0], split[1].toInt()))
                    } else {
                        boxes[box]!!.add(Pair(split[0], split[1].toInt()))
                    }
                }
            } else {
                val label = it.substring(0 until it.lastIndex)
                val box = hash(label)
                if(!boxes[box].isNullOrEmpty() && boxes[box]!!.any { pair -> pair.first == label }){
                    //println("$label $box ${boxes[box]!!.any { pair -> pair.first == label }}")
                    boxes[box]!!.removeAt(boxes[box]!!.indexOfFirst { pair -> pair.first == label })
                }
            }
        }
        var sum = 0
        boxes.forEach { it.value.forEachIndexed { index, pair -> sum += (it.key + 1) * (index + 1) * pair.second  }}
        return sum
    }

    println(part1(rows))
    println(part2(rows))
}
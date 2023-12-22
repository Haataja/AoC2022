package me.haataja.aoc23

import Direction
import java.io.File
import java.util.*
import kotlin.math.abs

fun main() {

    val path = "src/main/input/aoc23/day17.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n)+")).map { s ->
        s.map { c -> c.digitToInt() }.toMutableList()
    }.toMutableList()

    data class State(val direction: Direction, val turnCount: Int, val position: Pair<Int, Int>, val distance: Int)

    fun canTakeStep(
        current: State,
        step: Pair<Direction, Pair<Int, Int>>
    ): Boolean {
        return when {
            current.direction == step.first -> {
                current.turnCount < 2
            }

            else -> current.direction.opposite() != step.first
        }
    }

    fun available(
        current: State,
        map: MutableList<MutableList<Int>>
    ): List<Pair<Direction, Pair<Int, Int>>> {
        val steps = Direction.entries.toTypedArray().map { direction ->
            direction to (current.position.first + direction.dy to current.position.second + direction.dx)
        }.filter { step -> step.second.first in 0..map.lastIndex && step.second.second in 0..map[0].lastIndex }
            .filter { step -> canTakeStep(current, step) }
        return steps
    }

    fun canTakeStep2(
        current: State,
        step: Pair<Direction, Pair<Int, Int>>
    ): Boolean {
        return when {
            current.direction == step.first -> {
                current.turnCount < 9
            }

            current.direction != step.first -> {
                current.turnCount > 3
            }

            else -> current.direction.opposite() != step.first
        }
    }

    fun available2(
        current: State,
        map: MutableList<MutableList<Int>>
    ): List<Pair<Direction, Pair<Int, Int>>> {
        val steps = Direction.entries.toTypedArray().map { direction ->
            if (current.turnCount > 3) {
                direction to (current.position.first + direction.dy to current.position.second + direction.dx)
            } else {
                direction to (current.position.first + 4 * direction.dy to current.position.second + 4 * direction.dx)
            }
        }.filter { step -> step.second.first in 0..map.lastIndex && step.second.second in 0..map[0].lastIndex }
            .filter { step -> canTakeStep2(current, step) }
        println(steps)
        return steps
    }


    fun part1(rows: MutableList<MutableList<Int>>): Int {
        val start = 0 to 0
        val end = rows.lastIndex to rows[0].lastIndex
        val visited = mutableSetOf<State>()
        val pq = PriorityQueue(compareBy<State> { it.distance })
        pq.offer(State(Direction.RIGHT, 0, start, 0))
        while (pq.isNotEmpty()) {
            val current = pq.poll()
            println("Currently working on: ${current.distance}")
            if (current.position == end) {
                return current.distance
            }

            // part of the Dijkstra algorithm is to keep track of the seen states
            // which include everything but the tracked variable
            if (visited.any { it.position == current.position && it.turnCount == current.turnCount && it.direction == current.direction }) {
                continue
            }
            visited.add(current)

            available(current, rows).forEach {
                if (it.first == current.direction) {
                    val next = if (current.position == start && current.distance == 0) {
                        State(it.first, 0, it.second, rows[it.second.first][it.second.second])
                    } else {
                        State(
                            it.first,
                            current.turnCount + 1,
                            it.second,
                            current.distance + rows[it.second.first][it.second.second]
                        )
                    }
                    pq.offer(next)
                } else {
                    val next = State(it.first, 0, it.second, current.distance + rows[it.second.first][it.second.second])
                    pq.offer(next)
                }
            }
        }

        return -1
    }

    fun part2(rows: MutableList<MutableList<Int>>): Int {
        return -1
    }

    // this is slow, but got it done
    println(part1(rows))
    println(part2(rows))
}
import java.io.File

fun main() {
    var start1 = Pair(0, 0)
    var end = Pair(0, 0)
    val startingPoints = mutableListOf<Pair<Int,Int>>()
    val value = File("src/main/input/day12.txt").readText().split("\n").mapIndexed { row, s ->
        s.trim().mapIndexed { col, c ->
            when (c) {
                'S' -> {
                    start1 = Pair(row, col)
                    1
                }

                'E' -> {
                    end = Pair(row, col)
                    26
                }

                else -> {
                    if(c == 'a') startingPoints.add(Pair(row, col))
                    c - 'a' + 1
                }
            }
        }
    }

    println("${end}")
val pathLengths = mutableListOf<Int>()
    for(start in startingPoints) {
        val visited = mutableListOf(start)
        val paths = mutableListOf(mutableListOf(start))

        for (i in 1..1100) {
            val addedPaths = mutableListOf<MutableList<Pair<Int, Int>>>()
            for (path in paths) {
                val available = takeStep(path.last(), value).filter { step -> !visited.contains(step) }
                if (available.isNotEmpty()) {
                    for (step in available) {
                        val newPath = path + step
                        addedPaths.add(newPath.toMutableList())
                    }
                    visited.addAll(available)
                }
            }

            if (addedPaths.any { list -> list.contains(end) }) {
                val ending = addedPaths.filter { list -> list.contains(end) }
                println("Got to the end ${ending[0].size}")
                pathLengths.add(ending[0].size - 1)
                break
            }
            paths.clear()
            paths.addAll(addedPaths)
            addedPaths.clear()
        }
    }
    println("min: ${pathLengths.minOrNull()}")
    // paths.forEach { path -> println(path.size) }
}

fun takeStep(
    current: Pair<Int, Int>,
    value: List<List<Int>>
): List<Pair<Int, Int>> {
    return Direction.values().map { direction ->
        Pair(current.first + direction.dy, current.second + direction.dx)
    }.filter { step -> step.first in 0..value.lastIndex && step.second in 0..value[0].lastIndex }
        .filter { step -> canTakeStep(current, step, value) }
}

fun canTakeStep(current: Pair<Int, Int>, dir: Pair<Int, Int>, value: List<List<Int>>): Boolean {
    return value[current.first][current.second] + 1 >= value[dir.first][dir.second]
}

enum class Direction(val dx: Int, val dy: Int) {
    LEFT(-1, 0), RIGHT(1, 0), UP(0, -1), DOWN(0, 1);

    fun opposite(): Direction {
        return when(this){
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}


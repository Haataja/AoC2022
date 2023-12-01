import java.io.File


fun main() {
    val rocks = determineRocksWithFloor()
    val maxY = rocks.maxOf { r -> r.second }
    val sand = mutableListOf<Pair<Int, Int>>()
    val start = Pair(500, 0)
    var gotTillEnd = false
    /*for(rounds in 0..70000){
        var current = moveTheSand(start, rocks, sand)
        var next = moveTheSand(current, rocks, sand)
        while (current != next) {
            current = next
            next = moveTheSand(current, rocks, sand)
            if(next.second > maxY){
                gotTillEnd = true
                break
            }
        }
        println(current)
        if(gotTillEnd){
            sand.add(current)
            printCurrent(rocks, sand)
            println(rounds)
            break
        } else{
            sand.add(current)
        }
    }*/

    for(rounds in 0..100000){
        var current = moveTheSand(start, rocks, sand)
        var next = moveTheSand(current, rocks, sand)
        var i = 0
        while (current != next && i < 10000) {
            current = next
            next = moveTheSand(current, rocks, sand)
            if(next.second > maxY){
                gotTillEnd = true
                break
            }
            i++
        }
        println(current)
        if(gotTillEnd || current.second == 0){
            sand.add(current)
            println(rounds)
            printCurrent(rocks, sand)
            break
        } else{
            sand.add(current)
        }
    }


}

fun moveTheSand(grain: Pair<Int, Int>, rocks: List<Pair<Int, Int>>, sand: MutableList<Pair<Int, Int>>): Pair<Int, Int> {
    var sandX = grain.first
    var sandY = grain.second
    // down is blocked
    return if (rocks.contains(Pair(sandX, sandY + 1)) || sand.contains(Pair(sandX, sandY + 1))) {
        // left diagonal is blocked
        if (rocks.contains(Pair(sandX - 1, sandY + 1)) || sand.contains(Pair(sandX - 1, sandY + 1))) {
            if (rocks.contains(Pair(sandX + 1, sandY + 1)) || sand.contains(Pair(sandX + 1, sandY + 1))) {
                Pair(sandX, sandY)
            } else {
                Pair(sandX + 1, sandY + 1)
            }
        } else {
            Pair(sandX - 1, sandY + 1)
        }
    } else {
        Pair(sandX, sandY + 1)
    }
}

fun determineRocks(): List<Pair<Int, Int>> {
    val rocks = mutableListOf<Pair<Int, Int>>()
    File("src/main/input/day14.txt").readText().split("\n").forEach { s ->
        val lineCoordinates = s.split("->")
        for (i in lineCoordinates.indices) {
            if (i > 0) {
                val coordPair = lineCoordinates[i].split(",")
                val prev = rocks.last()
                val current = Pair(coordPair[0].trim().toInt(), coordPair[1].trim().toInt())
                if (prev.first != current.first) {
                    val difference = current.first - prev.first
                    if (difference > 0) {
                        for (j in 1 until difference) {
                            rocks.add(Pair(prev.first + j, prev.second))
                        }
                    } else {
                        for (j in 1 until kotlin.math.abs(difference)) {
                            rocks.add(Pair(prev.first - j, prev.second))
                        }
                    }
                }
                if (prev.second != current.second) {
                    val difference = current.second - prev.second
                    if (difference > 0) {
                        for (j in 1 until difference) {
                            rocks.add(Pair(prev.first, prev.second + j))
                        }
                    } else {
                        for (j in 1 until kotlin.math.abs(difference)) {
                            rocks.add(Pair(prev.first, prev.second - j))
                        }
                    }
                }
                rocks.add(current)
            } else {
                val coordPair = lineCoordinates[i].split(",")
                val current = Pair(coordPair[0].trim().toInt(), coordPair[1].trim().toInt())
                rocks.add(current)
            }

        }
    }
    return rocks
}

fun determineRocksWithFloor(): List<Pair<Int, Int>> {
    val rocks = mutableListOf<Pair<Int, Int>>()
    File("src/main/input/day14.txt").readText().split("\n").forEach { s ->
        val lineCoordinates = s.split("->")
        for (i in lineCoordinates.indices) {
            if (i > 0) {
                val coordPair = lineCoordinates[i].split(",")
                val prev = rocks.last()
                val current = Pair(coordPair[0].trim().toInt(), coordPair[1].trim().toInt())
                if (prev.first != current.first) {
                    val difference = current.first - prev.first
                    if (difference > 0) {
                        for (j in 1 until difference) {
                            rocks.add(Pair(prev.first + j, prev.second))
                        }
                    } else {
                        for (j in 1 until kotlin.math.abs(difference)) {
                            rocks.add(Pair(prev.first - j, prev.second))
                        }
                    }
                }
                if (prev.second != current.second) {
                    val difference = current.second - prev.second
                    if (difference > 0) {
                        for (j in 1 until difference) {
                            rocks.add(Pair(prev.first, prev.second + j))
                        }
                    } else {
                        for (j in 1 until kotlin.math.abs(difference)) {
                            rocks.add(Pair(prev.first, prev.second - j))
                        }
                    }
                }
                rocks.add(current)
            } else {
                val coordPair = lineCoordinates[i].split(",")
                val current = Pair(coordPair[0].trim().toInt(), coordPair[1].trim().toInt())
                rocks.add(current)
            }

        }
    }
    val maxY = rocks.maxOf { p -> p.second }
    val minX = rocks.minOf { p -> p.first }
    val maxX = rocks.maxOf { p -> p.first }
    for(x in (minX - 300)..(maxX +300)){
        rocks.add(Pair(x , maxY + 2))
    }
    return rocks
}

fun printCurrent(rock: List<Pair<Int, Int>>, sand: List<Pair<Int, Int>>) {
    val xCoordinates = rock.map { k -> k.first }
    val xAxelStart = xCoordinates.minOrNull()
    val xAxelEnd = xCoordinates.maxOrNull()

    val yCoordinates = rock.map { k -> k.second }
    val yAxelStart = 0
    val yAxelEnd = yCoordinates.maxOrNull()

    println("x $xAxelStart .. $xAxelEnd  and y $yAxelStart .. $yAxelEnd")

    for (row in yAxelStart!!..yAxelEnd!!) {
        for (col in xAxelStart!!..xAxelEnd!!) {
            if (rock.contains(Pair(col, row))) {
                print("#")
            } else if (row == 0 && col == 500) {
                print("x")
            } else if (sand.contains(Pair(col, row))) {
                print("o")
            } else {
                print(".")
            }
        }
        println()
    }

}


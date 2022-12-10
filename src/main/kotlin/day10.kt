import java.io.File

fun main() {
    val xValueList = mutableListOf<Int>()
    var x = 1
    var cycle = 0
    File("src/main/input/day10.txt").readText().split("\n").forEach { s ->
        cycle += 1
        if (cycle == 220 || cycle == 180 || cycle == 140 || cycle == 100 || cycle == 60 || cycle == 20) {
            xValueList.add(x)
        }
        if (s.startsWith("addx ")) {
            cycle += 1
            if (cycle == 220 || cycle == 180 || cycle == 140 || cycle == 100 || cycle == 60 || cycle == 20) {
                xValueList.add(x)
            }
            x += s.substring(5).trim().toInt()
        }
    }

    // println("$xValueList")
    val signalStrength =
        20 * xValueList[0] + 60 * xValueList[1] + 100 * xValueList[2] + 140 * xValueList[3] + 180 * xValueList[4] + 220 * xValueList[5]
    println("Signal strength $signalStrength")

    x = 1
    cycle = 0
    val crtRow = mutableListOf<String>()
    File("src/main/input/day10.txt").readText().split("\n").forEachIndexed { index, s ->
        cycle += 1
        if (cycle == 240 || cycle == 200 || cycle == 160 || cycle == 120 || cycle == 80 || cycle == 40) {
            cycle = 0
        }
        if (cycle - 1 >= x - 1 && cycle - 1 <= x + 1) {
            crtRow.add("#")
        } else {
            crtRow.add(".")
        }
        if (s.startsWith("addx ")) {
            cycle += 1
            if (cycle == 240 || cycle == 200 || cycle == 160 || cycle == 120 || cycle == 80 || cycle == 40) {
                cycle = 0
            }
            if (cycle - 1 >= x - 1 && cycle - 1 <= x + 1) {
                crtRow.add("#")
            } else {
                crtRow.add(".")
            }
            x += s.substring(5).trim().toInt()
        }
    }

    crtRow.chunked(40).forEach { list ->
        list.forEach { c -> print(c) }
        println("")
    }


}
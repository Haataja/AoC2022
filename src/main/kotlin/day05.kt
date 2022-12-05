import java.io.File

// Well this is huge pile of crap,  maybe I refine this later on
fun main() {
    val start = mutableListOf<MutableList<String>>(
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf()
    )

    val value = File("src/main/input/day05.txt").readText().split("\n")

    value.forEachIndexed { index, s ->
        if (index < 8) { // 8
            val columns = s.chunked(4)
            columns.forEachIndexed { ki, k ->
                val crate = k.replace("[", "").replace("]", "").trim()
                if (crate.isNotBlank()) start[ki].add(crate)
            }
        }
    }
    // println("Staring from $start")

    value.forEachIndexed { index, s ->
        if (index > 9) { // 8
            println(s.substring(14..17))
            val numberOfCrates = s.substring(0..7).filter { c -> c.isDigit() }.toInt() - 1
            val from = s.substring(10..14).filter { c -> c.isDigit() }.toInt() - 1
            val to = s.substring(14 until s.length).filter { c -> c.isDigit() }.toInt() - 1
            println("move $numberOfCrates from $from to $to")
            val moved = mutableListOf<String>()
            for (i in 0..numberOfCrates) {
                moved.add(start[from][0])
                //first part:  start[to].add(0,start[from][0])
                start[from].removeFirst()
            }
            start[to].addAll(0, moved)
            moved.clear()
            // println(start.toString())
        }
    }



    start.forEach { l -> print(l[0]) }

}
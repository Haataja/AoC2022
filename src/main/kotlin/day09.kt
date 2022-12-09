import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt


fun main() {
    val head = Coordinates(0, 0, mutableListOf())
    val tail = Coordinates(0, 0, mutableListOf())
    val value = File("src/main/input/day09.txt").readText().split("\n").forEach { row ->
        val direction = row.split(Regex("\\s"))[0]
        val count = row.split(Regex("\\s"))[1].trim().toInt()
        for (step in 0 until count) {
            head.move(direction)
            if (tail.calculateDist(head) > 1.5) {
                val history = head.history[(head.history.lastIndex - 1)]
                tail.move(history.first, history.second)
            }
        }
    }

    println("Tail of the rope runs through ${tail.history.distinct().size} positions")


    val array = mutableListOf(
        Coordinates(0, 0, mutableListOf()),
        Coordinates(0, 0, mutableListOf()),
        Coordinates(0, 0, mutableListOf()),
        Coordinates(0, 0, mutableListOf()),
        Coordinates(0, 0, mutableListOf()),
        Coordinates(0, 0, mutableListOf()),
        Coordinates(0, 0, mutableListOf()),
        Coordinates(0, 0, mutableListOf()),
        Coordinates(0, 0, mutableListOf()),
        Coordinates(0, 0, mutableListOf())
    )
    array[9].history.add(Pair(0,0)) // lazy, I know
    val value1 = File("src/main/input/day09.txt").readText().split("\n").forEach { row ->
        val direction = row.split(Regex("\\s"))[0]
        val count = row.split(Regex("\\s"))[1].trim().toInt()
        for (step in 0 until count) {
            array[0].move(direction)
            for(i in 1..9){
                if (array[i].calculateDist(array[i-1]) > 1.5) {
                    array[i].moveTowards(array[i-1])
                }
            }
        }
    }
    println("Tail of the rope runs through ${array.last().history.distinct().size} positions")
    // array.forEachIndexed { index, coordinates -> println("$index : ${coordinates.history}") }

}

data class Coordinates(
    var x: Int,
    var y: Int,
    val history: MutableList<Pair<Int, Int>>
) {
    fun calculateDist(coordinates: Coordinates): Double {
        return sqrt((coordinates.x - x).toDouble().pow(2) + (coordinates.y - y).toDouble().pow(2))
    }

    fun move(dir: String) {
        when (dir) {
            "R" -> {
                x += 1
            }

            "L" -> {
                x -= 1
            }

            "U" -> {
                y += 1
            }

            else -> {
                y -= 1
            }
        }
        history.add(Pair(x, y))
    }

    fun move(moveX: Int, moveY: Int) {
        x = moveX
        y = moveY
        history.add(Pair(x, y))
    }

    fun moveTowards(coordinates: Coordinates){
        if(x < coordinates.x){
            x += 1
        } else if (x > coordinates.x){
            x -= 1
        }

        if(y < coordinates.y){
            y += 1
        } else if (y > coordinates.y){
            y -= 1
        }
        history.add(Pair(x,y))
    }
}

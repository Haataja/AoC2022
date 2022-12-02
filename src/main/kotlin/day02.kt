import java.io.File

fun main() {

    val points = File("src/main/input/day02.txt").readText().split("\n").sumOf { it ->
        val row = it.split(Regex("\\s"))
        val me = mapToRPS(row[1])
        val opponent = mapToRPS(row[0])
        if (me - opponent == 1 || me - opponent == -2) {
            6 + me
        } else if ( opponent == me) {
            3 + me
        } else {
            me
        }
    }
    println("By following the strategy guide I wold have got $points points")

    val points2 = File("src/main/input/day02.txt").readText().split("\n").sumOf { it ->
        val row = it.split(Regex("\\s"))
        val opponent = mapToRPS(row[0])
        val me = determineMeWhenOutcomeIsKnown(row[1], opponent)
        // println("me $me, opp $opponent (${row[0]}) score ${row[1]}")
        if (row[1] == "Z") {
            6 + me
        } else if ( row[1] == "Y") {
            3 + me
        } else {
            me
        }
    }
    println("By following the ultra top secret strategy guide rightly I would have got $points2 points")

}

fun mapToRPS(char: String): Int{
    return when (char) {
        "A", "X" -> {
            1
        }
        "B", "Y" -> {
            2
        }
        else -> {
            3
        }
    }
}

fun determineMeWhenOutcomeIsKnown(end:String, opponent: Int):Int{
    return when(end){
        "Z" -> {
            if (opponent == 3) 1 else opponent + 1
        }
        "Y" -> {
             opponent
        }
        else -> {
            if (opponent == 1) 3 else opponent - 1
        }
    }
}
import java.io.File

fun main() {

    val value = File("src/main/input/day04.txt").readText().split("\n").map {
        val areas = it.split(",")
        val firstElf = areas[0].split("-")[0].toInt().rangeTo(areas[0].split("-")[1].toInt())
        val secondElf = areas[1].split("-")[0].toInt().rangeTo(areas[1].split("-")[1].trim().toInt())
        if (firstElf.intersect(secondElf).size == firstElf.toList().size || secondElf.intersect(firstElf).size == secondElf.toList().size) 1 else 0
    }.sum()


    println("$value of pairs that have fully overlapping areas")


    val partially = File("src/main/input/day04.txt").readText().split("\n").map {
        val areas = it.split(",")
        val firstElf = areas[0].split("-")[0].toInt().rangeTo(areas[0].split("-")[1].toInt())
        val secondElf = areas[1].split("-")[0].toInt().rangeTo(areas[1].split("-")[1].trim().toInt())
        if (firstElf.intersect(secondElf).isNotEmpty()) 1 else 0
    }.sum()


    println("$partially of pairs that have partially overlapping areas")


}
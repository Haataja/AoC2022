import java.io.File

fun main(args: Array<String>) {
    var currentMax = 0
    val array = mutableListOf<Int>()
    var sum = 0
    File("src/main/input/day01.txt").forEachLine {
        println(it)
        if (it.isBlank()) {
            println("Plank! CM $currentMax and SUM $sum")
            array.add(sum)
            // for the first part of just finding the top elf
            currentMax = if(currentMax > sum) currentMax else sum
            sum = 0
        } else {
            sum += it.toInt()
        }
    }

    array.sortDescending()
    println("current max: $currentMax")
    println("top 3 elves: ${array[0]} + ${array[1]} + ${array[2]} = ${array[0] + array[1] + array[2]}")
}
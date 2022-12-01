import java.io.File

fun main2(args: Array<String>) {
    var currentMax = 0
    val array = mutableListOf<Int>()
    var sum = 0
    File("src/main/input/day01.txt").forEachLine {
        println(it)
        if (it.isBlank()) {
            println("Plank! CM $currentMax and SUM $sum")
            array.add(sum)
            // for the first part of just finding the top elf
            currentMax = if (currentMax > sum) currentMax else sum
            sum = 0
        } else {
            sum += it.toInt()
        }
    }

    array.sortDescending()
    println("current max: $currentMax")
    println("top 3 elves: ${array[0]} + ${array[1]} + ${array[2]} = ${array[0] + array[1] + array[2]}")
}

// after looking at better answers, I came up with more elegant solution
fun main(args: Array<String>) {

    val max = File("src/main/input/day01.txt").readText().split(Regex("(\\r?\\n){2,}")).maxOfOrNull { it ->
        it.lines().sumOf { it.toInt() }
    }

    println("Elf that has most of calories has $max calories.")

    val sumOfThree = File("src/main/input/day01.txt").readText().split(Regex("(\\r?\\n){2,}")).map { it ->
        it.lines().sumOf { it.toInt() }
    }.sortedDescending().subList(0, 3).sum()
    println("The 3 Elves that have most of calories have $sumOfThree calories in total.")

}
import java.io.File

fun main() {

    val value = File("src/main/input/day03.txt").readText().split("\n").sumOf { it ->
        it.substring(0, it.length / 2).filter { char -> it.substring(it.length / 2).contains(char) }.toCharArray()
            .distinct().sumOf {
                if (it.isLowerCase()) it - 'a' + 1 else it - 'A' + 27
            }
    }

    println("Priority of shared items in rucksack compartments $value")

    val groups = File("src/main/input/day03.txt").readLines().chunked(3).sumOf { group ->
        group[0].filter { char -> group[1].contains(char) && group[2].contains(char) }.toCharArray().distinct()
            .sumOf { if (it.isLowerCase()) it - 'a' + 1 else it - 'A' + 27 }
    }


    println("Priority of badges in groups of 3 elves $groups")

}
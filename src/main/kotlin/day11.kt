import java.io.File

fun main() {
    var allTest : Long = 1
    val value = File("src/main/input/day11.txt").readText().split(Regex("(\\r?\\n){2,}")).map { s ->
        val heldItems = mutableListOf<Long>()
        var operation = ""
        var operationValue = 0
        var testValue = 0
        var throwToTrue = 0
        var throwToFalse = 0

        s.split("\n").forEach { k ->
            val kt = k.trim()
            if (kt.startsWith("Starting items: ")) {
                kt.substring(15).split(",").forEach { i -> heldItems.add(i.trim().toLong()) }
            } else if (kt.startsWith("Operation: ")) {
                if (kt.any { c -> c.isDigit() }) {
                    operation = kt.substring(20..21).trim()
                    operationValue = kt.filter { c -> c.isDigit() }.toInt()
                } else {
                    operation = "^"
                }
            } else if (kt.startsWith("Test:")) {
                testValue = kt.filter { c -> c.isDigit() }.toInt()
                allTest *= testValue
            } else if (kt.startsWith("If true:")) {
                throwToTrue = kt.filter { c -> c.isDigit() }.toInt()
            } else if (kt.startsWith("If false:")) {
                throwToFalse = kt.filter { c -> c.isDigit() }.toInt()
            }
        }
        Monkey(heldItems, operation, operationValue.toLong(), testValue, 0, throwToTrue, throwToFalse)
    }

    /* part one
    for (round in 0 until 20) {
        for (activeMonkey in value) {
            for (itemIndex in activeMonkey.heldItems.indices) {
                activeMonkey.inspect(itemIndex)
                if (activeMonkey.test(itemIndex)) {
                    value[activeMonkey.throwToTrue].heldItems.add(activeMonkey.heldItems[itemIndex])
                } else {
                    value[activeMonkey.throwToFalse].heldItems.add(activeMonkey.heldItems[itemIndex])
                }
            }
            activeMonkey.heldItems.clear()
        }
        // value.forEachIndexed{i, monkey -> println("$i ${monkey.heldItems}")}
    }
    value.forEachIndexed { i, monkey -> println("$i ${monkey.activityLevel}") }
    val monkeyBusinessTop = value.map { m -> m.activityLevel }.sortedDescending()
    println("Level of monkey business ${monkeyBusinessTop[0] * monkeyBusinessTop[1]}")

    */

    // second part
    for (round in 0 until 10000) {
        for (activeMonkey in value) {
            for (itemIndex in activeMonkey.heldItems.indices) {
                activeMonkey.inspectForSecond(itemIndex, allTest)
                if (activeMonkey.test(itemIndex)) {
                    value[activeMonkey.throwToTrue].heldItems.add(activeMonkey.heldItems[itemIndex])
                } else {
                    value[activeMonkey.throwToFalse].heldItems.add(activeMonkey.heldItems[itemIndex])
                }
            }
            activeMonkey.heldItems.clear()
        }
    }
    value.forEachIndexed { i, monkey -> println("$i ${monkey.activityLevel}") }
    val monkeyBusinessTop = value.map { m -> m.activityLevel }.sortedDescending()
    println("Level of monkey business ${monkeyBusinessTop[0] * monkeyBusinessTop[1]}")
}

data class Monkey(
    val heldItems: MutableList<Long>,
    val operation: String,
    val operationValue: Long,
    val testValue: Int,
    var activityLevel: Long,
    var throwToTrue: Int,
    var throwToFalse: Int
) {
    fun inspect(itemIndex: Int) {
        activityLevel++
        heldItems[itemIndex] = (makeOperation(heldItems[itemIndex]) / 3) as Long
    }

    fun inspectForSecond(itemIndex: Int, testValues: Long) {
        activityLevel++
        heldItems[itemIndex] = makeOperation(heldItems[itemIndex]) % testValues
    }

    private fun makeOperation(worryLevel: Long): Long {
        return when (operation) {
            "^" -> {
                worryLevel * worryLevel
            }

            "*" -> {
                worryLevel * operationValue
            }

            else -> {
                worryLevel + operationValue
            }
        }
    }

    fun test(itemIndex: Int): Boolean {
        return heldItems[itemIndex] % testValue == 0.toLong()
    }

}
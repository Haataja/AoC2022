import java.io.File


fun main() {

    val sum = File("src/main/input/day13.txt").readText().split(Regex("(\\r?\\n){2,}")).mapIndexed { index, s ->
        val first = parse(s.split("\n")[0].trim())
        val second = parse(s.split("\n")[1].trim())

        // println("first: $first")
        // println("second: $second")
        if (compare(first, second) == 1) {
            //println("compare on true ")
            index + 1
        } else {
            //println("compare on false ")
            0
        }
    }.sum()
    // 6369
    println("$sum")

    val divider1 = Line(mutableListOf(mutableListOf(2)))
    val divider2 = Line(mutableListOf(mutableListOf(6)))
    val order = mutableListOf(divider1, divider2)
    val parsed = File("src/main/input/day13.txt").readText().split("\n").filter { s -> s.isNotBlank() }.map { s ->
        Line(parse(s.trim()))
    }

    order.addAll(parsed)
    order.sortDescending()
    val indDiv1 = order.indexOf(divider1) + 1
    val indDiv2 = order.indexOf(divider2) + 1
    // dividers are at 129 and 200 decoder key is 25800
    println("dividers are at $indDiv1 and $indDiv2 decoder key is ${indDiv1 * indDiv2}")

}

data class Line(
    val a: Any
) : Comparable<Line> {
    override fun compareTo(other: Line) = compare(a, other.a)
}
fun compare(first: Any, second: Any): Int {
    var left = first
    var right = second
    //println("comparing $left vs $right")
    if(left is Int && right is List<*>){
        left = listOf(left)
    }

    if(right is Int && left is List<*>){
        right = listOf(right)
    }

    if(right is Int && left is Int){
        return if(left < right){
            1
        } else if(left > right){
            - 1
        } else {
            0
        }
    }

    var i = 0
    if(left is List<*> && right is List<*>){
        left = left as List<Any>
        right = right as List<Any>

        while(i < left.size && i < right.size){
            val comp = compare(left[i], right[i])
           // println("comparing deeper $left vs $right  $comp")
            if(comp == 1){
                return 1
            } else if(comp == -1) {
                return -1
            }
            i++
        }
        if(i == left.size){
            if(left.size == right.size){
                return 0
            }
            return 1
        } else {
            return -1
        }
    }
    return 0
}

fun parseToArrayOfAny(packet: String): List<Any> {
    val list = mutableListOf<Any>(mutableListOf<Any>())
    packet.forEach { c ->
        if (c == '[') {
            list.add(mutableListOf<Any>())
        } else if (c == ']') {
            (list[list.lastIndex - 1] as MutableList<Any>).add(list.last())
            list.removeLast()
        } else if (c.isDigit()) {
            (list.last() as MutableList<Int>).add(c.digitToInt())
        }
    }

    return (list[0] as MutableList<Any>)[0] as MutableList<Any>
}

// todo: kirjota ja ajattele läpi
@OptIn(ExperimentalStdlibApi::class)
fun parse(data: String): Any {
    if (data[0] != '[') {
        return data.toInt()
    }
    var nesting = 0
    var lastIndex = 1
    return buildList {
        for ((i, ch) in data.withIndex()) {
            when (ch) {
                '[' -> nesting++
                ']' -> nesting--
                ',' -> if (nesting == 1) {
                    add(parse(data.substring(lastIndex, i)))
                    lastIndex = i + 1
                }
            }
        }
        check(nesting == 0)
        if (lastIndex < data.length - 1) {
            add(parse(data.substring(lastIndex, data.length - 1)))
        }
    }
}



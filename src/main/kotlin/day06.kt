import java.io.File


fun main() {
    val value = File("src/main/input/day06.txt").readText()
    val packetMarker = findMarker(value, 4)
    println("start-of-packet marker appears after $packetMarker characters")

    val messageMarker = findMarker(value, 14)
    println("start-of-message marker appears after $messageMarker characters")

}

fun findMarker(input: String, markerRange: Int): Int {
    for (index in input.indices) {
        if (index >= markerRange) {
            val previousLetters = input.substring(index - markerRange, index)
            if (previousLetters.toCharArray().distinct().size >= markerRange) {
                return index
            }
        }
    }
    return -1
}
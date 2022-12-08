import java.io.File


fun main() {
    val size = 99
    val array = Array(size) { Array(size) { 0 } }
    val value = File("src/main/input/day08.txt").readText().split("\n").forEachIndexed { index, s ->
        s.trim().forEachIndexed { ci, c -> array[index][ci] = c.digitToInt() }
    }

    var visible = 0
    for (row in array.indices) {
        for (col in array[row].indices) {

            if (row == 0 ||
                (row == array.indices.last && col != 0) ||
                col == 0 ||
                col == array[row].indices.last
            ) {
                visible += 1
            } else {
                val dummyList = Array(size){0}
                for (i in array.indices) dummyList[i] = (array[i][col])
                if (isTheHighest(array[row][col], array[row], col) ||
                    isTheHighest(array[row][col], dummyList, row)){
                    visible += 1
                }
            }
        }
    }


    println("$visible visible trees")

    val scenicScore = mutableListOf<Int>()
    for (row in array.indices) {
        for (col in array[row].indices) {
            if (row == 0 ||
                (row == array.indices.last && col != 0) ||
                col == 0 ||
                col == array[row].indices.last
            ) {

            }else {
                scenicScore.add(calculateScore(array, row, col, size))
            }
        }
    }
    println("From tree with most of trees visible elves can see ${scenicScore.maxOrNull()} trees")

}

fun calculateScore(array: Array<Array<Int>>, row: Int, col: Int, size:Int): Int {
    val height = array[row][col]
    var right = 1
    for (i in (col + 1) until size - 1){
        if(array[row][i] < height){
            right += 1
        } else {
            break
        }
    }
    var left = 1
    for (i in (col - 1) downTo 1 ){
        if(array[row][i] < height){
            left += 1
        } else {
            break
        }
    }
    var down = 1
    for (i in (row + 1) until size - 1){
        if(array[i][col] < height){
            down += 1
        } else {
            break
        }
    }
    var up = 1
    for (i in (row - 1) downTo  1){
        if(array[i][col] < height){
            up += 1
        } else {
            break
        }
    }

    return right * left * up * down
}

fun isTheHighest(height: Int, row: Array<Int>, col:Int): Boolean {
    val before = row.toMutableList().subList(0, col)
    val after = row.toMutableList().subList(col + 1,row.indices.last + 1)
    return !before.any { h -> h >= height } || !after.any{ h-> h>= height}
}




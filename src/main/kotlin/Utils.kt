fun print2D(rows: List<List<Char>>) {
    println()
    for (row in rows.indices) {
        for (column in rows[row].indices) {
            print(rows[row][column])
        }
        println()
    }
}

fun print2DWitObjects(rows: List<List<Char>>, objects: List<Pair<Int,Int>>, objectChar: Char) {
    println()
    for (row in rows.indices) {
        for (column in rows[row].indices) {
            if(objects.contains(row to column)){
                print(objectChar)
            }else {
                print(rows[row][column])
            }
        }
        println()
    }
}

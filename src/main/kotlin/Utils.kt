fun print2D(rows: List<List<Char>>) {
    println()
    for (row in rows.indices) {
        for (column in rows[row].indices) {
            print(rows[row][column])
        }
        println()
    }
}

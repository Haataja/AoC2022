import java.io.File


fun main() {
    val parent = Directory(null, "parent", mutableListOf(), mutableListOf())
    var current = parent
    val value = File("src/main/input/day07.txt").readText().split("\n").forEach { row ->
        println(row)
        if (row.startsWith("$")) {
            // command
            if (row.startsWith("$ cd ")) {
                val name = row.substring(5).trim()
                print("name " + name.matches(Regex("[a-z]{1,8}")))
                current = if (name.matches(Regex("[a-z]{1,8}"))) {
                    current.getChildDir(name)
                } else if (name == "..") {
                    current.parent!!
                } else {
                    parent
                }
            }
        } else if (row.startsWith("dir ")) {
            // add new child
            current.addChildDir(Directory(current, row.substring(4).trim(), mutableListOf(), mutableListOf()))
        } else {
            // add file
            current.addChildFile(row.filter { c -> c.isDigit() }.toInt())
        }
    }
    println("${parent.name} : ${parent.childDirs.map { k -> k.name }}")
    val neededSpace = 30000000 - 70000000 + parent.getSize()
    println("Needed space : $neededSpace")
    val list = mutableListOf<Int>()
    parent.print(list)
    println("here is the answer ${list.minOrNull()}")
}

data class Directory(
    val parent: Directory?,
    val name: String,
    val childDirs: MutableList<Directory>,
    val childFiles: MutableList<Int>
) {
    fun addChildDir(dir: Directory) {
        childDirs.add(dir)
    }

    fun getChildDir(name: String): Directory {
        return childDirs.filter { d -> d.name == name }[0]
    }

    fun addChildFile(size: Int) {
        childFiles.add(size)
    }

    fun getSize(): Int {
        return childFiles.sum() + childDirs.sumOf { s -> s.getSize() }
    }

    fun print(list: MutableList<Int>) {
        println("$name : ${getSize()}")
        childDirs.forEach { p ->
            if (p.getSize() > 1072511) {
                list.add(p.getSize())

            }
            p.print(list)
        }
    }


}
import java.io.File

/*fun main() {

    val valves = File("src/main/input/day16.txt").readText().split(System.lineSeparator()).map {
        val valveDetails = it.split(";")
        val name = valveDetails[0].substring(6..8).trim()


        Valve(
            name,
            valveDetails[1].removePrefix(" tunnels lead to valves ").removePrefix(" tunnel leads to valve ").split(",")
                .map { v -> v.trim() }.toList(),
            false,
            -1,
            valveDetails[0].filter { c -> c.isDigit() }.toInt()
        )

    }

    println("$valves")

    var previousValve = getValve(valves, "AA")
    var currentValve = getValve(valves, "AA")
    var minLeft = 30

    while ( minLeft > 0) {
        val nextValves = currentValve.leadTo.map { valveName -> getValve(valves, valveName) } as MutableList<Valve>
        if (!currentValve.open && currentValve.flowRate > 0 && checkOpening(currentValve, valves)) {
            minLeft -= 1
            currentValve.open(minLeft)
            println("Time left $minLeft (${30 - minLeft}) Opening Valve ${currentValve.name}")
        } else if (valves.filter { valve -> valve.flowRate != 0 }.all { v -> v.open }) {
            minLeft -= 1
            break
        } else {
            minLeft -= 1
            val potentials = mutableMapOf<Valve, Int>()
            //println("$nextValves")
            if (nextValves.size > 1) {
                nextValves.forEach { valve ->
                    if (valve != previousValve) {
                        potentials[valve] = calculatePotential(valve, valves)
                    }
                }
                 println("$potentials")
                previousValve = currentValve
                currentValve = potentials.filterValues { k -> k == potentials.values.maxOrNull()!! }.keys.first()
            } else {
                val prev = previousValve
                previousValve = currentValve
                currentValve = prev
            }
            println("Time left $minLeft (${30 - minLeft}) Moving to ${currentValve.name}")
        }
    }

    println("$valves")
    println("${
        valves.sumOf { v ->
            if (v.open) {
                v.openedOn * v.flowRate
            } else {
                0
            }
        }
    }")
}

data class Valve(
    val name: String,
    val leadTo: List<String>,
    var open: Boolean,
    var openedOn: Int,
    val flowRate: Int
) {
    fun open(timeLeft: Int) {
        open = true
        openedOn = timeLeft
    }

    fun potential(): Int {
        return if (!open) flowRate else 0
    }
}

fun getValve(valves: List<Valve>, valveName: String): Valve {
    return valves.find { v -> v.name == valveName }!!
}

fun calculatePotential(valve: Valve, valves: List<Valve>): Int {
    var potential = valve.potential()
    var children = valve.leadTo.map { valveName -> getValve(valves, valveName) }
    /*for (i in 0..0) {
        potential += children.sumOf { c -> c.potential() }
        children = children.flatMap { k ->
            k.leadTo.map { valveName -> getValve(valves, valveName) }
        }
    }*/
    return potential + children.sumOf { c -> c.potential() }
}

fun checkOpening(current: Valve, valves: List<Valve>): Boolean {
    val stillToOpen =  valves.filter {valve -> !valve.open }
    val worthToSkip = stillToOpen.filter { s -> s.flowRate > current.flowRate }
    // println("worth skipping: $worthToSkip")
    if (worthToSkip.isNotEmpty()) {
        for (valve in worthToSkip) {
            val steps = valve.flowRate / current.flowRate
            //println("checking ${valve.name} steps $steps")
            val usedSteps = mutableListOf(valve)
            var currentSteps = mutableListOf(valve)
            for (i in 0 until steps) {
                currentSteps = currentSteps.flatMap { k -> k.leadTo.map { p -> getValve(valves, p) } }
                    .filter { k -> !usedSteps.contains(k) } as MutableList<Valve>
                //println("checking steps ${currentSteps}")
                if (currentSteps.any { k -> k.name == current.name }) {
                    // println("returning false")
                    return false
                } else {
                    usedSteps.addAll(currentSteps)
                }

            }
        }
    }
    return true
}*/

fun main() {

    lateinit var nameToValve: Map<String, Valve>
    lateinit var valves: List<Valve>
    lateinit var valuableValves: List<Valve>
    val pathToCost: MutableMap<Pair<Valve, Valve>, Int> = mutableMapOf()

    fun parseInput(input: List<String>) {
        valves = input.map { s ->
            val (_, name, rate, neighbors) = "Valve (\\w+) has flow rate=(\\d+); \\w+ \\w+ to \\w+ ([\\w\\s,]+)".toRegex().find(s)!!.groups.map { it!!.value }
            Valve(name, rate.toInt(), neighbors.split(",").map { it.trim() })
        }
        valuableValves = valves.filter { it.rate > 0 }
        nameToValve = valves.associateBy { it.name }
        val first = nameToValve["AA"]!!

        (valuableValves + first).forEach { start ->
            var currentNodes = start.neighbors
            (1..30).forEach { minute ->
                currentNodes = currentNodes.flatMap {
                    val end = (nameToValve[it]!!)
                    val path = start to end
                    if (pathToCost.contains(path)) {
                        emptyList()
                    } else {
                        pathToCost[path] = minute
                        end.neighbors
                    }
                }
            }
        }
    }

    fun findMaxPressure(countdown: Int, current: Valve, restValves: List<Valve>, pressure: Int): Int {
        println("round: ${countdown} current ${current.name} ")
        if (countdown <= 0) return pressure
        return restValves.maxOfOrNull { valve ->
            val cost = pathToCost[current to valve]!!
            val nextCountdown = countdown - cost - 1
            findMaxPressure(nextCountdown, valve, restValves - valve, pressure + nextCountdown * valve.rate)
        } ?: pressure
    }

    fun part1(input: List<String>): Int {
        parseInput(input)
        val start = nameToValve["AA"]!!
        return findMaxPressure(30, start, valuableValves, 0)
    }

    fun part2(input: List<String>): Int {
        parseInput(input)
        val start = nameToValve["AA"]!!
        val splitValves = valuableValves.buildSubsets()
        return splitValves
            .asSequence()
            .filter { it.isNotEmpty() && it.size != valuableValves.size }
            .maxOf { my ->
                val elephant = valuableValves.toSet() - my
                findMaxPressure(26, start, my.toList(), 0) +
                        findMaxPressure(26, start, elephant.toList(), 0)
            }
    }

    // test if implementation meets criteria from the description, like:
    /*val testInput = File("src/main/input/day16.txt").readLines()
    println(part1(testInput))
    println(part2(testInput))
    check(part1(testInput) == 1651)
    check(part2(testInput) == 1707)*/

    val input = File("src/main/input/day16.txt").readLines()
    println(part1(input))
    //println(part2(input))

    /*fun printPath(path: List<Valve>) {
        var min = 0
        var pressure = 0
        path.windowed(2).forEach { (prev, next) ->
            println("Open valve=${prev.name} at min=${min}")
            pressure += (30 - min) * prev.rate
            val cost = pathToCost[prev to next]!!
            min += cost
            println("Go to valve=${next.name} and now min=${min}")
            min += 1
        }
        pressure += (30 - min) * path.last().rate
        println("Released $pressure")
    }*/
}

private data class Valve(val name: String, val rate: Int, val neighbors: List<String>)

private fun List<Valve>.buildSubsets(): List<Set<Valve>> {
    val subsets = mutableListOf<Set<Valve>>()

    fun addAllSubsets(subset: Set<Valve> = emptySet(), index: Int = 0) {
        if (index == size) {
            subsets.add(subset)
            return
        }
        addAllSubsets(subset, index + 1)
        addAllSubsets(subset + this[index], index + 1)
    }

    addAllSubsets()

    return subsets
}



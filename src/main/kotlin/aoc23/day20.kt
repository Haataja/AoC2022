package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day20.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n)+"))

    data class FlipFlop(
        override val name: String,
        override val destination: MutableList<String>,
        var on: Boolean = false
    ) : Module {
        override fun receivePulse(pulse: String, input: Module): String {
            return if (pulse == "low") {
                if (on) {
                    on = false
                    "low"
                } else {
                    on = true
                    "high"
                }
            } else {
                "stop"
            }
        }
    }

    data class Conjunction(
        override val name: String,
        override val destination: MutableList<String>,
        var memory: MutableMap<String, String> = mutableMapOf()
    ) : Module {
        override fun receivePulse(pulse: String, input: Module): String {
            memory[input.name] = pulse
            return if (memory.values.all { it == "high" }) {
                "low"
            } else {
                "high"
            }
        }
    }



    fun checkIfInInitialState(modules: List<Module>): Boolean {
        // println(modules)
        return modules.all {
            if (it is Conjunction) {
                it.memory.values.all { mem -> mem == "low" }
            } else {
                !(it as FlipFlop).on || it.destination.isEmpty() || it.name == "broadcast"
            }
        }
    }

    fun part1(rows: List<String>): Int {
        val modules = rows.map {
            if (it.startsWith("broadcaster")) {
                val destinations = it.substring(it.indexOf(">") + 1)
                    .trim()
                    .replace(Regex("\\s"), "")
                    .split(",")
                FlipFlop("broadcast", destinations.toMutableList())
            } else if (it.startsWith("&")) {
                // conjunction
                Conjunction(
                    name = it.substring(1, it.indexOf("-")).trim(),
                    destination = it.substring(it.indexOf(">") + 1)
                        .replace(Regex("\\s"), "")
                        .trim()
                        .split(",")
                        .toMutableList()
                )
            } else {
                // flip flop
                FlipFlop(
                    name = it.substring(1, it.indexOf("-")).trim(),
                    destination = it.substring(it.indexOf(">") + 1)
                        .trim()
                        .replace(Regex("\\s"), "").split(",")
                        .toMutableList()
                )
            }
        }.toMutableList()


        modules.filterIsInstance<Conjunction>().forEach { module ->
            val inputs = modules.filter { it.destination.contains(module.name) }
            inputs.forEach {
                module.memory[it.name] = "low"
            }
        }
        val onlyDestination = mutableListOf<Module>()
        modules.forEach {
            it.destination.filter { d -> modules.find { m -> m.name == d } == null }.forEach { d ->
                onlyDestination.add(FlipFlop(d, mutableListOf()))
            }
        }
        modules.addAll(onlyDestination)

        var high = 0
        var low = 0
        var rounds = 1
        val broadcast = modules.find { module -> module.name == "broadcast" }!!
        while (rounds <= 1000) {
            low++
            println(rounds)
            val moduleMap = mutableListOf<Pair<String, Pair<String,Module>>>()
            broadcast.destination.forEach { moduleMap.add("low" to (it to broadcast)) }
            low += moduleMap.size
            while(moduleMap.isNotEmpty()){
                val addMap = mutableListOf<Pair<String, Pair<String,Module>>>()
                for (cast in moduleMap) {
                    val module = modules.find { it.name == cast.second.first }!!
                    val nextPulse = module.receivePulse(cast.first, cast.second.second)
                    if(nextPulse != "stop"){
                        for (dest in module.destination){
                            if(nextPulse == "high") high++ else low++
                            addMap.add(nextPulse to (dest to module))
                        }
                    }
                }
                moduleMap.clear()
                moduleMap.addAll(addMap)
            }
            if (checkIfInInitialState(modules)) {
                break
            }
            rounds++
        }
        println(rounds)
        println("$high $low")
        val multiplier = 1001 / rounds
        return high * low * multiplier * multiplier
    }

    fun part2(rows: List<String>): Int {
        return 0
    }

    println(part1(rows))
    println(part2(rows))
}

interface Module {
    val name: String
    val destination: MutableList<String>
    fun receivePulse(pulse: String, input: Module): String
}
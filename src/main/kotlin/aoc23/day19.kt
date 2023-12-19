package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day19.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n){2,}")) //(\r?\n){2,}

    fun parseRule(rule:String, gear: Map<String, Int>): String{
        return if(rule.contains(":")){
            val ifRule = rule.substring(0, rule.indexOf(":"))
            val isTrue = rule.substring(ifRule.length + 1, rule.indexOf(','))
            val isNotTrue = rule.substring(rule.indexOf(',') + 1)

            val category = ifRule[0].toString()
            val comp = ifRule[1]
            val value = ifRule.substring(2).toInt()
            if(comp == '>'){
                if(gear[category]!! > value){
                    parseRule(isTrue, gear)
                } else {
                    parseRule(isNotTrue,gear)
                }
            } else {
                if(gear[category]!! < value){
                    parseRule(isTrue, gear)
                } else {
                    parseRule(isNotTrue,gear)
                }
            }
        } else {
            rule
        }
    }

    fun part1(rows: List<String>): Int {
        val rules = rows[0].split(Regex("(\\r?\\n)")).associate { ruleString ->
            val splitRuleString = ruleString.replace("}", "").split("{")
            val name = splitRuleString[0]
            name to splitRuleString[1]
        }
        val gears = rows[1].split(Regex("(\\r?\\n)")).map{gearString ->
            val splitGear = gearString.replace("{","").replace("}","").split(",")
            splitGear.associate { sg -> sg.split("=")[0] to sg.split("=")[1].toInt() }
        }
        return gears.sumOf {
           var end = parseRule(rules["in"]!!, it)
            while (end != "A" && end != "R"){
                end = parseRule(rules[end]!!, it)
            }
            if(end == "A"){
                it.values.sum()
            } else {
                0
            }
        }
    }

    fun part2(rows: List<String>): Int {

        return 0
    }

    println(part1(rows))
    //println(part2(rows))
}
package me.haataja.aoc23

import java.io.File

fun main() {

    val path = "src/main/input/aoc23/day07.txt"
    val rows = File(path).readText().split(Regex("(\\r?\\n)+"))
    val strengthOfCard = "A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2"
    val strengthOfCardPart2 = "A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J"

    fun part1(rows: List<String>): Int {
        val map = rows.map {
            val value = it.split(Regex("\\s"))
            value[0] to value[1]
        }
        return map.sortedWith(compareBy<Pair<String, String>> { calcStrength(it.first) }
            .thenComparing {c1, c2 ->

                for(i in c1.first.indices){

                    if(c1.first[i] != c2.first[i]){
                        if (strengthOfCard.indexOf(c1.first[i]) < strengthOfCard.indexOf(c2.first[i])){

                            return@thenComparing 1
                        } else {
                            return@thenComparing -1
                        }
                    }
                }
                0
            }).mapIndexed{index, pair -> (index + 1 ) * pair.second.toInt()}.sum()


    }

    fun part2(rows: List<String>): Int {
        val map = rows.map {
            val value = it.split(Regex("\\s"))
            value[0] to value[1]
        }
        return map.sortedWith(compareBy<Pair<String, String>> { calcStrengthWithJokers(it.first) }
            .thenComparing {c1, c2 ->
                for(i in c1.first.indices){
                    if(c1.first[i] != c2.first[i]){
                        if (strengthOfCardPart2.indexOf(c1.first[i]) < strengthOfCardPart2.indexOf(c2.first[i])){
                            return@thenComparing 1
                        } else {
                            return@thenComparing -1
                        }
                    }
                }
                0
            }).mapIndexed{index, pair -> (index + 1 ) * pair.second.toInt()}.sum()
    }

    println(part1(rows))
    println(part2(rows))
}

fun calcStrength(hand: String): Int{
    return if(hand.all { c -> hand[0] == c }){
        6 // five of kind
    } else {
        var strength = 0
        var multiples = 0
        var character = '.'
        for (char in hand){
            val numberOfChars = hand.filter { c -> c == char }.length
            if(numberOfChars == 4){
                strength = 5 // four of kind
                break
            } else if (numberOfChars > 1 && multiples < numberOfChars) {
                multiples = numberOfChars
                character = char
            }
        }

        if(multiples == 2){
            val filtered = hand.filter { c -> c != character }
            strength = if (filtered[0] != filtered[1] && filtered[0] != filtered[2] && filtered[1] != filtered[2])  {
                1 // one pair
            } else {
                2 // two pairs
            }
        } else if (multiples == 3){
            val filtered = hand.filter { c -> c != character }
            strength = if(filtered[0] == filtered[1]){
                4 // full house
            } else {
                3 // thee of kind
            }
        }
        strength
    }
}
fun calcStrengthWithJokers(hand: String): Int{
    return if(hand.all { c -> hand[0] == c} || hand.filter { c -> c == 'J' }.length == 4){
        6 // five of kind
    } else {
        var strength = 0
        var multiples = 0
        var character = '.'
        for (char in hand){
            val numberOfChars = hand.filter { c -> c == char || c == 'J' }.length
            if(numberOfChars == 4 || numberOfChars == 5){
                strength = numberOfChars + 1
                multiples = 0
                break
            } else if (numberOfChars > 1 && multiples < numberOfChars) {
                multiples = numberOfChars
                character = char
            }
        }

        if(multiples == 2){
            val filtered = hand.filter { c -> c != character && c != 'J'}
            strength = if (filtered[0] != filtered[1] && filtered[0] != filtered[2] && filtered[1] != filtered[2])  {
                1 // one pair
            } else {
                2 // two pairs
            }
        } else if (multiples == 3){
            val filtered = hand.filter { c -> c != character && c != 'J'}
            strength = if(filtered[0] == filtered[1]){
                4 // full house
            } else {
                3 // thee of kind
            }
        }

        strength
    }
}
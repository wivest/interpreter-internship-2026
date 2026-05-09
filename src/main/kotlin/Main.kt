package org.example

import com.github.h0tk3y.betterParse.grammar.parseToEnd

fun main() {
    val expr = "1 + 2 * 3 + 4 * (5 + 6)"
    val tree = Language().parseToEnd(expr)
    println(tree)
    println(tree.evaluate())
}
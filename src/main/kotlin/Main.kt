package org.example

import com.github.h0tk3y.betterParse.grammar.parseToEnd

fun main() {
    val interpreter = Interpreter()
    var input: String

    while (true) {
        input = readln()
        if (input.isEmpty()) break

        val stmt = Language().parseToEnd(input)
        interpreter.assignVar(stmt.t1, stmt.t2.evaluate(interpreter))
    }

    println(interpreter.listVar())
}
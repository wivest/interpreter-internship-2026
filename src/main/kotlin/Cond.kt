package org.example

sealed class Cond {
    data class Value(val value: Boolean) : Cond()
    data class Less(val left: Expr, val right: Expr) : Cond()
    data class Greater(val left: Expr, val right: Expr) : Cond()
    data class LessEq(val left: Expr, val right: Expr) : Cond()
    data class GreaterEq(val left: Expr, val right: Expr) : Cond()

    fun evaluate(itpr: Interpreter): Boolean {
        return when (this) {
            is Value -> value
            is Less -> left.evaluate(itpr) < right.evaluate(itpr)
            is Greater -> left.evaluate(itpr) > right.evaluate(itpr)
            is LessEq -> left.evaluate(itpr) <= right.evaluate(itpr)
            is GreaterEq -> left.evaluate(itpr) >= right.evaluate(itpr)
        }
    }
}
package org.example

sealed class Expr {
    data class Value(val value: Int) : Expr()
    data class Add(val left: Expr, val right: Expr) : Expr()
    data class Min(val left: Expr, val right: Expr) : Expr()
    data class Mul(val left: Expr, val right: Expr) : Expr()
    data class Div(val left: Expr, val right: Expr) : Expr()
    data class Neg(val value: Expr) : Expr()

    fun evaluate(): Int {
        return when (this) {
            is Value -> value
            is Add -> left.evaluate() + right.evaluate()
            is Min -> left.evaluate() - right.evaluate()
            is Mul -> left.evaluate() * right.evaluate()
            is Div -> left.evaluate() / right.evaluate()
            is Neg -> -value.evaluate()
        }
    }
}
package org.example

sealed class Expr {
    data class Value(val value: Int) : Expr()
    data class Neg(val value: Expr) : Expr()
    data class Var(val name: String) : Expr()
    data class Add(val left: Expr, val right: Expr) : Expr()
    data class Min(val left: Expr, val right: Expr) : Expr()
    data class Mul(val left: Expr, val right: Expr) : Expr()
    data class Div(val left: Expr, val right: Expr) : Expr()

    fun evaluate(itpr: Interpreter): Int {
        return when (this) {
            is Value -> value
            is Neg -> -value.evaluate(itpr)
            is Var -> itpr.getVar(name)
            is Add -> left.evaluate(itpr) + right.evaluate(itpr)
            is Min -> left.evaluate(itpr) - right.evaluate(itpr)
            is Mul -> left.evaluate(itpr) * right.evaluate(itpr)
            is Div -> left.evaluate(itpr) / right.evaluate(itpr)
        }
    }
}
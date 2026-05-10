package org.example

sealed class Statement {
    data class Assignment(val name: String, val value: Expr) : Statement()
    data class If(val condition: Cond, val thenBranch: List<Statement>, val elseBranch: List<Statement>) : Statement()
    data class While(val condition: Cond, val body: List<Statement>) : Statement()
    data class Func(val name: String, val params: List<String>, val body: List<Statement>) : Statement()
    data class Ret(val value: Expr) : Statement()

    fun execute(itpr: Interpreter) {
        when (this) {
            is Assignment -> itpr.assignVar(name, value.evaluate(itpr))
            is If -> if (condition.evaluate(itpr)) for (stmt in thenBranch) stmt.execute(itpr)
            else for (stmt in elseBranch) stmt.execute(itpr)

            is While -> while (condition.evaluate(itpr)) for (stmt in body) stmt.execute(itpr)
            is Func -> itpr.defineFunc(name, params, body)
            is Ret -> throw ReturnException(value.evaluate(itpr))
        }
    }
}

class ReturnException(val value: Int) : Exception() {}
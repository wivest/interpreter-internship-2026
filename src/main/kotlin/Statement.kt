package org.example

sealed class Statement {
    data class Assignment(val name: String, val value: Expr) : Statement()
    data class If(val condition: Cond, val thenBranch: List<Statement>, val elseBranch: List<Statement>) : Statement()

    fun execute(itpr: Interpreter) {
        when (this) {
            is Assignment -> itpr.assignVar(name, value.evaluate(itpr))
            is If -> if (condition.evaluate(itpr)) for (stmt in thenBranch) stmt.execute(itpr)
            else for (stmt in elseBranch) stmt.execute(itpr)
        }
    }
}
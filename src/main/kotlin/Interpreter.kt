package org.example

class Interpreter {
    private val variables: MutableMap<String, Int> = mutableMapOf()
    private val functions: MutableMap<String, Pair<List<String>, List<Statement>>> = mutableMapOf()

    fun assignVar(name: String, value: Int) {
        variables[name] = value
    }

    fun defineFunc(name: String, params: List<String>, body: List<Statement>) {
        functions[name] = params to body
    }

    fun getVar(name: String): Int {
        return variables[name]!!
    }

    fun callFunc(name: String, args: List<Int>): Int {
        val outerScope = HashMap(variables)
        variables.clear()

        val func = functions[name]!!
        for ((i, param) in func.first.withIndex()) {
            variables[param] = args[i]
        }

        var ret = 0
        for (stmt in func.second) {
            try {
                stmt.execute(this)
            } catch (r: ReturnException) {
                ret = r.value
                break
            }
        }

        variables.clear()
        variables.putAll(outerScope)
        return ret
    }

    fun listVar(): String {
        var result = ""
        for (variable in variables) {
            result += "${variable.key}: ${variable.value}\n"
        }
        return result
    }
}
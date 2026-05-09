package org.example

class Interpreter {
    private val variables: MutableMap<String, Int> = mutableMapOf()

    fun assignVar(name: String, value: Int) {
        variables[name] = value
    }

    fun listVar(): String {
        var result = ""
        for (variable in variables) {
            result += "${variable.key}: ${variable.value}\n"
        }
        return result
    }
}
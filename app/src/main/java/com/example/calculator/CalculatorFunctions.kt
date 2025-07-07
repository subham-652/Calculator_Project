package com.example.calculator

import java.util.Stack
import kotlin.math.pow
import kotlin.text.toCharArray
import kotlin.text.toDouble

object CalculatorFunctions {
    fun evaluate(expression: String): Double {
        val tokens = expression.toCharArray()
        val values = Stack<Double>()
        val ops = Stack<Char>()
        var i = 0

        while (i < tokens.size) {
            if (tokens[i] == ' ') {
                i++
                continue
            }
            if (tokens[i] in '0'..'9' || tokens[i] == '.') {
                val sb = StringBuilder()
                while (i < tokens.size && (tokens[i] in '0'..'9' || tokens[i] == '.')) {
                    sb.append(tokens[i++])
                }
                values.push(sb.toString().toDouble())
                continue
            }
            if (tokens[i] == '(') {
                ops.push(tokens[i])
            } else if (tokens[i] == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()))
                }
                ops.pop()
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/' || tokens[i] == '^') {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()))
                }
                ops.push(tokens[i])
            }
            i++
        }
        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()))
        }
        return values.pop()
    }

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        return if (op2 == '(' || op2 == ')') false
        else if ((op1 == '^') && (op2 != '^')) false
        else (op1 != '*' && op1 != '/' && op1 != '^') || (op2 != '+' && op2 != '-')
    }

    private fun applyOp(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '^' -> a.pow(b)
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> if (b == 0.0) throw ArithmeticException("Cannot divide by zero") else a / b
            else -> 0.0
        }
    }
}

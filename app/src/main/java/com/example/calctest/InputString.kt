package com.example.calctest

import android.os.Build

class InputString {
    var inputString: String = "0"
    var result: Float = 0.0F
    private var inputElements = mutableListOf<String>()
    private var stack = mutableListOf<String>()
    private var queue = mutableListOf<String>()
    var addCloseBracket = false
    private var bracketsDifference = 0


    fun addCharacter(character: Char) {
        if ((inputString == "0") and (character in "0123456789")) {
            inputString = character.toString()
        } else {
            this.inputString += character
        }
    }

    fun clearInput() {
        this.inputString = "0"
    }

    fun clearResult() {
        this.result = 0.0F
        this.inputElements.clear()
        this.queue.clear()
        this.stack.clear()
    }

    fun deleteLastCharacter() {
        if (inputString.length == 1) {
            inputString = "0"
        } else if (inputString.length > 1) {
            if (inputString.endsWith("*(-1)")) {
                this.inputString = inputString.dropLast(5)
            } else {
                this.inputString = inputString.dropLast(1)
            }
        }
    }

    fun countBrackets() {
        var openBracketCounter = 0
        var closeBracketCounter = 0
        inputString.forEach {
            when (it) {
                '(' -> openBracketCounter++
                ')' -> closeBracketCounter++
            }
        }
        addCloseBracket = openBracketCounter > closeBracketCounter
        bracketsDifference = openBracketCounter - closeBracketCounter
    }

    fun addCloseBrackets() {
        countBrackets()
        for (i in 1..bracketsDifference) addCharacter(')')
    }

    private fun divideByElements() {
        var currentIndex = 0
        var inputStringForDivide = inputString.replace("*(-1)", "*(0-1)")
        for (i in inputStringForDivide.indices) {
            if (inputStringForDivide[i] in "+-*/()") {
                if (currentIndex!=i) {
                    inputElements.add(inputStringForDivide.substring(currentIndex, i)).toString()
                }
                currentIndex = i + 1
                inputElements.add(inputStringForDivide[i].toString())
            }
        }
        if (inputStringForDivide.last().toString() != ")") {
            inputElements.add(inputStringForDivide.substring(currentIndex, inputStringForDivide.length))
        }
    }

    fun getPostFixEx() {
        divideByElements()
        inputElements.forEach {
            when {
                it == "(" -> push(it)
                it == ")" -> {
                    if (inputElements.contains("(")) {
                        pop()
                    }
                }
                Regex("[\\d]").containsMatchIn(it) -> {
                    addQueue(it)
                }
                Regex("[+-]").containsMatchIn(it) -> {
                    if (stack.isEmpty() || stack.last() == "(") {
                        push(it)
                    } else if (stack.last().contains(Regex("[/*]"))) {
                        pop()
                        push(it)
                    } else {
                        addQueue(stack.last())
                        stack[stack.lastIndex] = it
                    }
                }
                Regex("[*/]").containsMatchIn(it) -> {
                    if (stack.isNotEmpty() && (stack.last() == "*" || stack.last() == "/")) {
                        pop()
                    }
                    push(it)
                }
            }
        }
        if (stack.isNotEmpty()) {
            for (i in stack.lastIndex downTo 0) {
                if (stack[i] != "(") {
                    addQueue(stack[i])
                }
            }
        }
    }

    private fun pop() {
        Loop@ for (i in stack.lastIndex downTo 0) {
            if (stack[i] == "(") {
                stack[i] = " "
                break@Loop
            }
            addQueue(stack[i])
            stack[i] = " "
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stack.removeIf { it == " " }
        } else {
            for (i in 0..stack.lastIndex) {
                if (stack[i] == " ") {
                    stack.remove(stack[i])
                }
            }
        }
    }

    private fun addQueue(item: String) {
        queue.add(item)
    }

    private fun push(item: String) {
        stack.add(item)
    }

    fun calcPostFix() {
        val stack = mutableListOf<Float>()
        for (item in queue) {
            when {
                Regex("[\\d]").containsMatchIn(item) -> {
                    stack.add(item.toFloat())
                }
                item == "+" -> {
                    stack[stack.lastIndex - 1] = stack[stack.lastIndex - 1] + stack.last()
                    stack.removeAt(stack.lastIndex)
                }
                item == "*" -> {
                    stack[stack.lastIndex - 1] = stack[stack.lastIndex - 1] * stack.last()
                    stack.removeAt(stack.lastIndex)
                }
                item == "/" -> {
                    stack[stack.lastIndex - 1] = stack[stack.lastIndex - 1] / stack.last()
                    stack.removeAt(stack.lastIndex)
                }
                item == "-" -> {
                    stack[stack.lastIndex - 1] = stack[stack.lastIndex - 1] - stack.last()
                    stack.removeAt(stack.lastIndex)
                }
            }
        }
        result = stack.first()
    }
}
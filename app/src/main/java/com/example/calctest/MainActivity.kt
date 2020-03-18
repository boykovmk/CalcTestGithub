package com.example.calctest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var input = InputString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnBracketsOpenClick(view: View) {
        when (input.inputString.last()) {
            in "/*-+(" -> {
                input.addCharacter((view as Button).text.first())
            }
        }
        tvInput.text = input.inputString
    }

    fun btnBracketsCloseClick(view: View) {
        input.countBrackets()
        if (input.addCloseBracket) {
            if (input.inputString.last() in "0123456789)") {
                input.addCharacter((view as Button).text.first())
            }
            tvInput.text = input.inputString
        }
    }

    fun btnNumbersClick(view: View) {
        if (input.inputString.last() in "0123456789(/*-+.") {
            input.addCharacter((view as Button).text.first())
        }
        tvInput.text = input.inputString
        input.getPostFixEx()
        input.calcPostFix()
        tvResult.text = input.result.toString()
        input.clearResult()
    }

    fun btnOperationsClick(view: View) {
        when (input.inputString.last()) {
            in "0123456789)" -> {
                input.addCharacter((view as Button).text.first())
            }
            in "/*-+" -> {
                input.deleteLastCharacter()
                input.addCharacter((view as Button).text.first())
            }
        }
        tvInput.text = input.inputString
    }

    fun btnDotClick(view: View) {
        if (input.inputString.last() in "0123456789") {
            input.addCharacter((view as Button).text.first())
        }
        tvInput.text = input.inputString
    }

    fun btnNegateClick(view: View) {
        if (input.inputString.last() in "0123456789)") {
            input.inputString += "*(-1)"
        }
        tvInput.text = input.inputString
    }

    fun btnDeleteClick(view: View) {
        input.deleteLastCharacter()
        tvInput.text =input.inputString
    }

    fun btnClearClick(view: View) {
        input.clearInput()
        input.clearResult()
        tvInput.text = input.inputString
        tvResult.text = input.result.toString()
    }

    fun btnResultClick(view: View) {
        if (input.inputString.last() in "+-/*.") {
            input.deleteLastCharacter()
        }
        if (input.inputString.last() in "0123456789)") {
            input.addCloseBrackets()
            tvInput.text = input.inputString
            input.getPostFixEx()
            input.calcPostFix()
            tvResult.text = input.result.toString()
        }
        input.clearResult()
    }
}

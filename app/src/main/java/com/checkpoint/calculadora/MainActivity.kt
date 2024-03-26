package com.checkpoint.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.checkpoint.calculadora.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = binding.result
        val expression = binding.expression

        binding.numberZero.setOnClickListener { addExpression("0", true) }
        binding.numberOne.setOnClickListener { addExpression("1", true) }
        binding.numberTwo.setOnClickListener { addExpression("2", true) }
        binding.numberThree.setOnClickListener { addExpression("3", true) }
        binding.numberFour.setOnClickListener { addExpression("4", true) }
        binding.numberFive.setOnClickListener { addExpression("5", true) }
        binding.numberSix.setOnClickListener { addExpression("6", true) }
        binding.numberSeven.setOnClickListener { addExpression("7", true) }
        binding.numberEight.setOnClickListener { addExpression("8", true) }
        binding.numberNine.setOnClickListener { addExpression("9", true) }
        binding.point.setOnClickListener { addExpression(".", true) }

        binding.addition.setOnClickListener { addExpression("+", false) }
        binding.subtraction.setOnClickListener { addExpression("-", false) }
        binding.multiplication.setOnClickListener { addExpression("*", false) }
        binding.division.setOnClickListener { addExpression("/", false) }

        binding.point.setOnClickListener {
            val currentExpression = expression.text.toString()

            if (currentExpression.isNotEmpty() && currentExpression.last().isDigit()) {
                addExpression(".", true)
            }
        }

        binding.clear.setOnClickListener {
            expression.text = ""
            result.text = ""
        }

        binding.backspace.setOnClickListener {
            val string = expression.text.toString()

            if (string.isNotBlank()) {
                expression.text = string.substring(0, string.length - 1)
            }
            result.text = ""
        }

        binding.equals.setOnClickListener {
            try {
                val txtexpression = ExpressionBuilder(expression.text.toString()).build()
                val txtresult = txtexpression.evaluate()
                val longResult = txtresult.toLong()

                if (txtresult == longResult.toDouble())
                    result.text = longResult.toString()
                else
                    result.text = txtresult.toString()
            } catch (_: Exception) {
            }
        }

        binding.percent.setOnClickListener {
            val currentExpression = expression.text.toString()
            val currentResult = result.text.toString()

            if (currentExpression.isNotEmpty() && currentResult.isEmpty()) {
                val firstNumber = currentExpression.takeWhile { it.isDigit() || it == '.' }
                val numberOnly = currentExpression.toDoubleOrNull()
                val lastNumber = currentExpression.takeLastWhile { it.isDigit() || it == '.' }
                val expressionWithoutLastNumber = currentExpression.dropLast(lastNumber.length)
                if (numberOnly != null) {
                    val percentage = numberOnly / 100
                    expression.text = if (percentage % 1 == 0.0) percentage.toInt().toString() else percentage.toString()
                }
                else if (lastNumber.isNotEmpty()) {
                    val percentage = (lastNumber.toDouble() / 100 * firstNumber.toDouble())
                    val newExpression = "$expressionWithoutLastNumber${if (percentage % 1 == 0.0) percentage.toInt() else percentage}"
                    expression.text = newExpression
                } else {
                    val percentage = currentExpression.toDouble() / 100
                    expression.text = if (percentage % 1 == 0.0) percentage.toInt().toString() else percentage.toString()
                }
            } else if (currentExpression.isNotEmpty() && currentResult.isNotEmpty()) {
                val lastNumber = currentResult.toDoubleOrNull() ?: 0.0
                val percentage = lastNumber / 100
                result.text = if (percentage % 1 == 0.0) percentage.toInt().toString() else percentage.toString()
            }
        }

    }

    private fun addExpression(string: String, clearData: Boolean) {
        val result = binding.result
        val expression = binding.expression

        if (result.text.isNotEmpty()) {
            expression.text = ""
        }

        if (clearData) {
            result.text = ""
            expression.append(string)
        } else {
            expression.append(result.text)
            expression.append(string)
            result.text = ""
        }
    }
}

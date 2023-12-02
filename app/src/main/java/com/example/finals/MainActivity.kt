package com.example.finals
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.result)

        val buttons = listOf(
            findViewById<Button>(R.id.btn0),
            findViewById<Button>(R.id.btn1),
            findViewById<Button>(R.id.btn2),
            findViewById<Button>(R.id.btn3),
            findViewById<Button>(R.id.btn4),
            findViewById<Button>(R.id.btn5),
            findViewById<Button>(R.id.btn6),
            findViewById<Button>(R.id.btn7),
            findViewById<Button>(R.id.btn8),
            findViewById<Button>(R.id.btn9),
            findViewById<Button>(R.id.btnAND),
            findViewById<Button>(R.id.btnOR),
            findViewById<Button>(R.id.btnXOR),
            findViewById<Button>(R.id.btnLeftShift),
            findViewById<Button>(R.id.btnRightShift),
            findViewById<Button>(R.id.btnLeftParenthesis),
            findViewById<Button>(R.id.btnRightParenthesis),
            findViewById<Button>(R.id.btnClear),
            findViewById<Button>(R.id.btnNegate),
            findViewById<Button>(R.id.btnResult)
        )

        for (button in buttons) {
            button.setOnClickListener { onButtonClick(it) }
        }
    }

    private fun onButtonClick(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()

        when (buttonText) {
            "C" -> clearResult()
            "=" -> evaluateResult()
            "~" -> negateResult()
            else -> appendToResult(buttonText)
        }
    }

    private fun clearResult() {
        resultTextView.text = "0"
    }

    private fun evaluateResult() {
        val expression = resultTextView.text.toString()

        try {
            val result = evaluateExpression(expression)
            resultTextView.text = result.toString()
        } catch (e: Exception) {
            // Handle invalid expression or other exceptions
            resultTextView.text = "Error"
        }
    }

    private fun negateResult() {
        val expression = resultTextView.text.toString()

        try {
            val result = expression.toInt().inv()
            resultTextView.text = result.toString()
        } catch (e: Exception) {
            // Handle invalid expression or other exceptions
            resultTextView.text = "Error"
        }
    }

    private fun appendToResult(value: String) {
        val currentText = resultTextView.text.toString()
        if (currentText == "0") {
            resultTextView.text = value
        } else {
            resultTextView.append(value)
        }
    }

    private fun evaluateExpression(expression: String): Int {
        if (!expression.contains("(")) {
            return evaluateSimpleExpression(expression)
        }

        var openParenIndex = expression.lastIndexOf('(')
        while (openParenIndex != -1) {
            val closeParenIndex = expression.indexOf(')', openParenIndex)
            if (closeParenIndex != -1) {
                val innerExpression = expression.substring(openParenIndex + 1, closeParenIndex)
                val innerResult = evaluateSimpleExpression(innerExpression)
                val updatedExpression =
                    expression.substring(0, openParenIndex) + innerResult + expression.substring(closeParenIndex + 1)
                return evaluateExpression(updatedExpression)
            } else {
                throw IllegalArgumentException("Invalid expression: Unmatched parenthesis")
            }

            openParenIndex = expression.lastIndexOf('(', openParenIndex - 1)
        }

        return evaluateSimpleExpression(expression)
    }

    private fun evaluateSimpleExpression(expression: String): Int {
        val result = when {
            expression.contains("&") -> {
                val operands = expression.split("&")
                operands[0].toInt() and operands[1].toInt()
            }
            expression.contains("|") -> {
                val operands = expression.split("|")
                operands[0].toInt() or operands[1].toInt()
            }
            expression.contains("^") -> {
                val operands = expression.split("^")
                operands[0].toInt() xor operands[1].toInt()
            }
            expression.contains("<<") -> {
                val operands = expression.split("<<")
                operands[0].toInt() shl operands[1].toInt()
            }
            expression.contains(">>") -> {
                val operands = expression.split(">>")
                operands[0].toInt() shr operands[1].toInt()
            }
            else -> {
                expression.toInt()
            }
        }

        return result
    }
}

package com.example.calculator

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var currentInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Root LinearLayout (vertical)
        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#00008B"))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(16, 16, 16, 16)
        }

        // Display TextView (at the top)
        display = TextView(this).apply {
            text = "0"
            textSize = 48f
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.END
            setPadding(0, 50, 0, 50)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f // Give display space to push buttons down
            )
        }
        rootLayout.addView(display)

        // GridLayout for buttons (at the bottom)
        val buttonGrid = GridLayout(this).apply {
            rowCount = 5
            columnCount = 4
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0, 1.5f
            )
        }

        // Button labels row by row
        val buttonLabels = arrayOf(
            arrayOf("AC", "⌫", "^", "÷"),
            arrayOf("7", "8", "9", "×"),
            arrayOf("4", "5", "6", "-"),
            arrayOf("1", "2", "3", "+"),
            arrayOf("00", "0", ".", "=")
        )

        // Create buttons dynamically
        for (row in buttonLabels.indices) {
            for (col in buttonLabels[row].indices) {
                val label = buttonLabels[row][col]

                val btn = Button(this).apply {
                    text = label
                    textSize = 24f
                    setTextColor(
                        if (label in arrayOf("AC", "⌫", "÷", "×", "-", "+", "^")) Color.parseColor("#FFA000")
                        else Color.BLACK
                    )
                    setBackgroundColor(Color.WHITE)
                    stateListAnimator = null
                    if (label == "=") {
                        setBackgroundColor(Color.parseColor("#FFA000"))
                        setTextColor(Color.WHITE)
                    }
                    setOnClickListener { onButtonClick(label) }
                }

                val params = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 200
                    columnSpec = GridLayout.spec(col, 1f)
                    rowSpec = GridLayout.spec(row, 1f)
                    setMargins(8, 8, 8, 8)
                }

                btn.layoutParams = params
                buttonGrid.addView(btn)
            }
        }

        rootLayout.addView(buttonGrid)
        setContentView(rootLayout)
    }

    private fun onButtonClick(label: String) {
        when (label) {
            "AC" -> {
                currentInput = ""
                display.text = "0"
            }
            "⌫" -> {
                if (currentInput.isNotEmpty()) {
                    currentInput = currentInput.dropLast(1)
                    display.text = if (currentInput.isEmpty()) "0" else currentInput
                }
            }
            "=" -> {
                try {
                    val result = eval(currentInput)
                    // Format result
                    val formattedResult = if (result == result.toInt().toDouble()) {
                        result.toInt().toString()
                    } else {
                        result.toString()
                    }
                    display.text = formattedResult
                    currentInput = formattedResult
                } catch (e: ArithmeticException) {
                    display.text = "can't divide by 0"
                    currentInput = ""
                } catch (e: Exception) {
                    display.text = "Error"
                    currentInput = ""
                }
            }
            else -> {
                currentInput += label.replace("×", "×").replace("−", "-")
                display.text = currentInput
            }
        }
    }

    private fun eval(expr: String): Double {
        val cleanExpr = expr.replace("×", "*").replace("÷", "/")
        return CalculatorFunctions.evaluate(cleanExpr)
        }
}
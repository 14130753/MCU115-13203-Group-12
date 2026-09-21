package com.example.a1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a1.ui.theme._1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CalculatorApp(modifier: Modifier = Modifier) {
    var displayExpression by remember { mutableStateOf("") }
    var displayResult by remember { mutableStateOf("0") }
    
    var operand1 by remember { mutableStateOf<Double?>(null) }
    var activeOperator by remember { mutableStateOf<String?>(null) }
    var isNewInput by remember { mutableStateOf(true) }

    fun calculate(op1: Double, op2: Double, op: String): Double {
        return when (op) {
            "+" -> op1 + op2
            "-" -> op1 - op2
            "×" -> op1 * op2
            "÷" -> if (op2 != 0.0) op1 / op2 else Double.NaN
            else -> op2
        }
    }

    fun formatResult(value: Double): String {
        if (value.isNaN()) return "Error"
        if (value.isInfinite()) return "Error"
        return if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }

    fun onNumberClick(number: String) {
        if (isNewInput) {
            displayResult = number
            isNewInput = false
        } else {
            if (displayResult == "0") {
                displayResult = number
            } else {
                displayResult += number
            }
        }
    }

    fun onOperatorClick(op: String) {
        val currentValue = displayResult.toDoubleOrNull() ?: return
        
        if (operand1 == null) {
            operand1 = currentValue
            displayExpression = "${formatResult(currentValue)} $op"
        } else if (activeOperator != null && !isNewInput) {
            val result = calculate(operand1!!, currentValue, activeOperator!!)
            operand1 = result
            displayResult = formatResult(result)
            displayExpression = "${formatResult(result)} $op"
        } else {
            displayExpression = "${formatResult(operand1!!)} $op"
        }
        
        activeOperator = op
        isNewInput = true
    }

    fun onEqualClick() {
        val currentValue = displayResult.toDoubleOrNull() ?: return
        val op = activeOperator
        val op1 = operand1
        
        if (op != null && op1 != null) {
            val result = calculate(op1, currentValue, op)
            displayExpression = "${formatResult(op1)} $op ${formatResult(currentValue)} ="
            displayResult = formatResult(result)
            operand1 = null
            activeOperator = null
            isNewInput = true
        }
    }

    fun onClearClick() {
        displayExpression = ""
        displayResult = "0"
        operand1 = null
        activeOperator = null
        isNewInput = true
    }

    fun onDeleteClick() {
        if (!isNewInput && displayResult.length > 1) {
            displayResult = displayResult.dropLast(1)
        } else {
            displayResult = "0"
            isNewInput = true
        }
    }

    fun onDotClick() {
        if (isNewInput) {
            displayResult = "0."
            isNewInput = false
        } else if (!displayResult.contains(".")) {
            displayResult += "."
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Display Area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = displayExpression,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = displayResult,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )
        }

        // Keyboard Grid
        val buttons = listOf(
            listOf("C", "⌫", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "=")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { text ->
                    val isOperator = text in listOf("÷", "×", "-", "+", "=")
                    val isAction = text in listOf("C", "⌫", "%")
                    
                    val containerColor = when {
                        text == "=" -> MaterialTheme.colorScheme.primary
                        isOperator -> MaterialTheme.colorScheme.primaryContainer
                        isAction -> MaterialTheme.colorScheme.surfaceVariant
                        else -> MaterialTheme.colorScheme.surface
                    }
                    
                    val contentColor = when {
                        text == "=" -> MaterialTheme.colorScheme.onPrimary
                        isOperator -> MaterialTheme.colorScheme.onPrimaryContainer
                        isAction -> MaterialTheme.colorScheme.onSurfaceVariant
                        else -> MaterialTheme.colorScheme.onSurface
                    }

                    val weight = if (text == "0") 2f else 1f

                    Button(
                        onClick = {
                            when (text) {
                                "C" -> onClearClick()
                                "⌫" -> onDeleteClick()
                                "=" -> onEqualClick()
                                ".", "%" -> if (text == ".") onDotClick() else onOperatorClick("%")
                                in listOf("÷", "×", "-", "+") -> onOperatorClick(text)
                                else -> onNumberClick(text)
                            }
                        },
                        modifier = Modifier
                            .weight(weight)
                            .aspectRatio(if (text == "0") 2f else 1f),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = containerColor,
                            contentColor = contentColor
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) {
                        Text(
                            text = text,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    _1Theme {
        CalculatorApp()
    }
}
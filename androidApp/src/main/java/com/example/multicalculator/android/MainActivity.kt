//A00272016 - Milankumar Pandya
//Assignment-2 Create A Calculator UI

package com.example.multicalculator.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalcView()  // Use the new CalcView function here
                }
            }
        }
    }
}
//row in the calculator that holds numeric buttons
@Composable
fun CalcRow(display: MutableState<String>, startNum: Int, numButtons: Int) {
    val endNum = startNum + numButtons
    Row(modifier = Modifier.padding(0.dp)) {
        for (i in startNum until endNum) {
            CalcNumericButton(number = i, display = display)
        }
    }
}
//display of the calculator
@Composable
fun CalcDisplay(display: MutableState<String>) {
    Text(
        text = display.value,
        modifier = Modifier
            .height(45.dp)
            .padding(4.dp)
            .fillMaxWidth()
    )
}
//numeric button in the calculator
@Composable
fun CalcNumericButton(number: Int, display: MutableState<String>) {
    Button(
        onClick = {
            display.value = if (display.value == "0") number.toString() else display.value + number.toString()
        },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = number.toString())
    }
}
//operation button in the calculator
@Composable
fun CalcOperationButton(operation: String, display: MutableState<String>,
                        firstNumber: MutableState<Double?>,
                        operator: MutableState<String?>)
{
    Button(
        onClick = {
            firstNumber.value = display.value.toDouble()
            operator.value = operation
            display.value = "0"
        },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = operation)
    }
}
//equals button in the calculator
@Composable
fun CalcEqualsButton(display: MutableState<String>,
                     firstNumber: MutableState<Double?>,
                     operator: MutableState<String?>)
{
    Button(
        onClick = {
            val secondNumber = display.value.toDouble()
            firstNumber.value?.let {
                val result = when (operator.value) {
                    "+" -> it + secondNumber
                    "-" -> it - secondNumber
                    "*" -> it * secondNumber
                    "/" -> if (secondNumber != 0.0) it / secondNumber else "Error"
                    else -> "Error"
                }
                display.value = result.toString()
            }
            operator.value = null
            firstNumber.value = null
        },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = "=")
    }
}
// Preview function to see the calculator UI in the preview pane
@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        CalcView()  // Use the CalcView function here
    }
}
//main view for the calculator
@Composable
fun CalcView() {
    val displayText = remember { mutableStateOf("0") }
    val firstNumber = remember { mutableStateOf<Double?>(null) }
    val operator = remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.background(Color.LightGray)) {
        Row {
            CalcDisplay(display = displayText)
        }
        Row {
            Column {
                for (i in 7 downTo 1 step 3) {
                    CalcRow(display = displayText, startNum = i, numButtons = 3)
                }
                Row {
                    CalcNumericButton(number = 0, display = displayText)
                    CalcEqualsButton(display = displayText, firstNumber = firstNumber, operator = operator)
                }
            }
            Column {
                val operations = listOf("+", "-", "*", "/")
                for (operation in operations) {
                    CalcOperationButton(operation = operation, display = displayText, firstNumber = firstNumber, operator = operator)
                }
            }
        }
    }
}



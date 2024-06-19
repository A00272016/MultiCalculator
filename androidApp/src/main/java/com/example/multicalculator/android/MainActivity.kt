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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


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

@Composable
fun CalcRow(onPress: (Int) -> Unit, startNum: Int, numButtons: Int) {
    val endNum = startNum + numButtons
    Row(modifier = Modifier.padding(0.dp)) {
        for (i in startNum until endNum) {
            CalcNumericButton(number = i, onPress = onPress)
        }
    }
}

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

@Composable
fun CalcNumericButton(number: Int, onPress: (Int) -> Unit) {
    Button(
        onClick = { onPress(number) },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = number.toString())
    }
}

@Composable
fun CalcOperationButton(operation: String, onPress: (String) -> Unit,
                        display: MutableState<String>,
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

@Composable
fun CalcEqualsButton(onPress: () -> Unit,
                     display: MutableState<String>,
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
                    "/" -> if (secondNumber != 0.0) it / secondNumber else 0.0  // Handle division by zero
                    else -> 0.0
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

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        CalcView()  // Use the CalcView function here
    }
}

@Composable
fun CalcView() {
    var leftNumber by rememberSaveable { mutableStateOf(0) }
    var rightNumber by rememberSaveable { mutableStateOf(0) }
    var operation by rememberSaveable { mutableStateOf("") }
    var complete by rememberSaveable { mutableStateOf(false) }

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
                    CalcRow(onPress = { numberPress(it) }, startNum = i, numButtons = 3)
                }
                Row {
                    CalcNumericButton(number = 0, onPress = { numberPress(it) })
                    CalcEqualsButton(onPress = { equalsPress() }, display = displayText, firstNumber = firstNumber, operator = operator)
                }
            }
            Column {
                val operations = listOf("+", "-", "*", "/")
                for (operation in operations) {
                    CalcOperationButton(operation = operation, onPress = { operationPress(it) }, display = displayText, firstNumber = firstNumber, operator = operator)
                }
            }
        }
    }

    fun numberPress(btnNum: Int) {
        if (complete) {
            leftNumber = 0
            rightNumber = 0
            operation = ""
            complete = false
        }

        if (operation.isNotEmpty() && !complete) {
            rightNumber = rightNumber * 10 + btnNum
        } else if (operation.isBlank() && !complete) {
            leftNumber = leftNumber * 10 + btnNum
        }
    }

    fun operationPress(op: String) {
        if (!complete) {
            operation = op
        }
    }

    fun equalsPress() {
        complete = true
    }
}

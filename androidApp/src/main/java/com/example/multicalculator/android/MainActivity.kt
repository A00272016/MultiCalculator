package com.example.multicalculator.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
                    CalcView()
                }
            }
        }
    }
}
@Composable
fun CalcView() {
    var leftNumber by rememberSaveable { mutableStateOf(0) }
    var rightNumber by rememberSaveable { mutableStateOf(0) }
    var operation by rememberSaveable { mutableStateOf("") }
    var complete by rememberSaveable { mutableStateOf(false) }

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
        if (operation.isNotEmpty() && !complete) {
            val secondNumber = rightNumber.toDouble()
            val result = when (operation) {
                "+" -> leftNumber + secondNumber
                "-" -> leftNumber - secondNumber
                "*" -> leftNumber * secondNumber
                "/" -> if (secondNumber != 0.0) leftNumber / secondNumber else Double.NaN
                else -> Double.NaN
            }
            leftNumber = result.toInt()
            operation = ""
            rightNumber = 0
            complete = true
        }
    }
    Column(modifier = Modifier.background(Color.LightGray)) {
        Row {
            CalcDisplay(display = leftNumber.toString())
        }
        Row {
            Column {
                CalcRow(onPress = { numberPress(it) }, startNum = 7, numButtons = 3)
                CalcRow(onPress = { numberPress(it) }, startNum = 4, numButtons = 3)
                CalcRow(onPress = { numberPress(it) }, startNum = 1, numButtons = 3)
                Row {
                    CalcNumericButton(onPress = { numberPress(0) }, number = 0)
                    CalcEqualsButton(onPress = { equalsPress() })
                }
            }
            Column {
                CalcOperationButton(onPress = { operationPress(it) }, operation = "+")
                CalcOperationButton(onPress = { operationPress(it) }, operation = "-")
                CalcOperationButton(onPress = { operationPress(it) }, operation = "*")
                CalcOperationButton(onPress = { operationPress(it) }, operation = "/")
            }
        }
    }
}
@Composable
fun CalcDisplay(display: String) {
    Text(
        text = display,
        modifier = Modifier
            .height(45.dp)
            .padding(4.dp)
            .fillMaxWidth()
    )
}
@Composable
fun CalcRow(onPress: (Int) -> Unit, startNum: Int, numButtons: Int) {
    val endNum = startNum + numButtons
    Row(modifier = Modifier.padding(0.dp)) {
        for (i in startNum until endNum) {
            CalcNumericButton(onPress = onPress, number = i)
        }
    }
}
@Composable
fun CalcNumericButton(onPress: (Int) -> Unit, number: Int) {
    Button(
        onClick = { onPress(number) },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = number.toString())
    }
}
@Composable
fun CalcOperationButton(onPress: (String) -> Unit, operation: String) {
    Button(
        onClick = { onPress(operation) },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = operation)
    }
}
@Composable
fun CalcEqualsButton(onPress: () -> Unit) {
    Button(
        onClick = onPress,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = "=")
    }
}
@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        CalcView()
    }
}

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



// This is a row in the calculator that holds numeric buttons
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
fun CalcOperationButton(operation: String, display: MutableState<String>) {
    Button(
        onClick = { /* Implement operation logic here */ },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = operation)
    }
}
// equals button in the calculator
@Composable
fun CalcEqualsButton(display: MutableState<String>) {
    Button(
        onClick = { display.value = "0" },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = "=")
    }
}




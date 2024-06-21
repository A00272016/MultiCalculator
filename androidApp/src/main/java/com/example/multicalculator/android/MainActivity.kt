//A00272016 - Milankumar Pandya
//Multiplatform Development - Assignment-3

package com.example.multicalculator.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    Column(
        modifier = Modifier
            .background(Color.Magenta)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            CalcDisplay(display = buildDisplayText(leftNumber, rightNumber, operation))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                CalcRow(onPress = { numberPress(it) }, startNum = 7, numButtons = 3)
                Spacer(modifier = Modifier.height(8.dp))
                CalcRow(onPress = { numberPress(it) }, startNum = 4, numButtons = 3)
                Spacer(modifier = Modifier.height(8.dp))
                CalcRow(onPress = { numberPress(it) }, startNum = 1, numButtons = 3)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    CalcNumericButton(onPress = { numberPress(0) }, number = 0)
                    Spacer(modifier = Modifier.width(8.dp))
                    CalcEqualsButton(onPress = { equalsPress() })
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .align(Alignment.CenterVertically)
            ) {
                CalcOperationButton(onPress = { operationPress(it) }, operation = "+")
                Spacer(modifier = Modifier.height(8.dp))
                CalcOperationButton(onPress = { operationPress(it) }, operation = "-")
                Spacer(modifier = Modifier.height(8.dp))
                CalcOperationButton(onPress = { operationPress(it) }, operation = "*")
                Spacer(modifier = Modifier.height(8.dp))
                CalcOperationButton(onPress = { operationPress(it) }, operation = "/")
            }
        }
    }
}
@Composable
fun CalcDisplay(display: String) {
    Text(
        text = display,
        fontSize = 32.sp,
        color = Color.White,
        textAlign = TextAlign.Right,
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
            if (i < endNum - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}
@Composable
fun CalcNumericButton(onPress: (Int) -> Unit, number: Int) {
    Button(
        onClick = { onPress(number) },
        modifier = Modifier
            .padding(4.dp)
    ) {
        Text(text = number.toString(), fontSize = 24.sp)
    }
}
@Composable
fun CalcOperationButton(onPress: (String) -> Unit, operation: String) {
    Button(
        onClick = { onPress(operation) },
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Text(text = operation, fontSize = 24.sp)
    }
}
@Composable
fun CalcEqualsButton(onPress: () -> Unit) {
    Button(
        onClick = onPress,
        modifier = Modifier
            .padding(4.dp)
    ) {
        Text(text = "=", fontSize = 24.sp)
    }
}
@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        CalcView()
    }
}
private fun buildDisplayText(leftNumber: Int, rightNumber: Int, operation: String): String {
    val left = if (operation.isNotEmpty()) "$leftNumber $operation " else leftNumber.toString()
    val right = if (rightNumber != 0) rightNumber.toString() else ""
    return "$left$right"
}

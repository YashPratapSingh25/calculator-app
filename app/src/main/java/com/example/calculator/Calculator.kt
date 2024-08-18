package com.example.calculator

import android.os.Build.Partition
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.objecthunter.exp4j.ExpressionBuilder

@Composable
fun Calculator() {
    Column (
        modifier = Modifier
            .fillMaxSize(),
    ) {

        var equation by remember { mutableStateOf("") }
        var result by remember { mutableStateOf("") }

        Column (
            modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            val scrollState = rememberScrollState()

            LaunchedEffect(equation){
                scrollState.scrollTo(scrollState.maxValue)
            }

            Text(
                text = equation,
                fontSize = 50.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .horizontalScroll(scrollState)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = result,
                fontSize = 40.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .horizontalScroll(scrollState)
            )
        }
        Divider(thickness = 5.dp)
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f),
            verticalArrangement = Arrangement.Bottom,
        ) {
            val buttons = listOf(
                listOf("AC", "()", "%", "/"),
                listOf("7", "8", "9", "*"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf("0", ".", "<-", "="),
            )

            for (row in buttons) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    var numberOfLeftParenthesis = 0
                    for (button in row) {
                        CustomButton(
                            buttonText = button,
                            onAcClick = {
                                equation = ""
                                result = ""
                            },
                            onParenthesesClick = {
                                val leftParenthesisTrigger = listOf("(", "+", "-", "*", "/", "")
                                if(numberOfLeftParenthesis == 0){
                                    equation += "("
                                    numberOfLeftParenthesis++
                                }
                                else if(leftParenthesisTrigger.any {equation.endsWith(it)}){
                                    equation += "("
                                    numberOfLeftParenthesis++
                                }
                                else {
                                    equation += ")"
                                }
                            },
                            onEqualsToClick = {
                                result = evaluate(equation)
                            },
                            onBackspaceClick = {
                                if(equation.isNotBlank())
                                    equation = equation.removeRange(equation.length - 1, equation.length)
                            },
                            onNumberClick = {
                                equation += button
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun CustomButton(
    buttonText : String,
    onAcClick : () -> Unit,
    onParenthesesClick : () -> Unit,
    onEqualsToClick : () -> Unit,
    onBackspaceClick : () -> Unit,
    onNumberClick : () -> Unit
) {
    val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".", "<-")
    var colorButton : Color = Color.Blue
    if(numbers.any {buttonText.equals(it)}){
        colorButton = Color.Red
    }else if (buttonText == "="){
        colorButton = Color.Magenta
    }
    Button(
        modifier = Modifier.size(100.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorButton
        ),

        onClick = {
            when(buttonText){
                "AC" -> {
                    onAcClick()
                }
                "()" -> {
                    onParenthesesClick()
                }
                "=" -> {
                    onEqualsToClick()
                }
                "<-" -> {
                    onBackspaceClick()
                }
                else -> {
                    onNumberClick()
                }
            }
        }
    ) {
        Text(
            text = buttonText,
            fontSize = 25.sp,
            color = Color.White
        )
    }
}

fun evaluate(equation: String): String {
    return ExpressionBuilder(equation).build().evaluate().toString()
}
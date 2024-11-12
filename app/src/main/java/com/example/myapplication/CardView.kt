package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardView(word: String, translation: String, onClick: () -> Unit) {
    var turn by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .requiredSize(width = 170.dp, height = 200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(
            Brush.verticalGradient(
                colors = listOf(colorResource(id = R.color.mid_blue_memora), colorResource(id = R.color.dark_blue_memora))
            )
        )
        .wrapContentSize()
        .clickable { onClick() }
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(modifier = Modifier
                .padding(12.dp)
                ,
                text = if (turn) word else translation,
                color = Color.White,
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                colors = ButtonColors(
                    containerColor = colorResource(id = R.color.mid_blue_memora),
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.LightGray),


                onClick = {
                    turn = !turn
                }
            ) {
                Text(
                    text = "Turn",
                    color = Color.White,
                    fontSize = 18.sp
                )

            }
        }
    }
}
package com.example.myapplication

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuffleView() {
    val context = LocalContext.current

    val viewModel = remember { WordViewModel(context) }

    var wordsShuffle by remember { mutableStateOf(viewModel.wordBank) }
    var showingIndex by remember { mutableStateOf(0) }
    var offsetX by remember { mutableStateOf(0f) }

    val dragThreshold = 100f

    Scaffold(
        topBar = {
            TopBarView()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (wordsShuffle.isNotEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    for (index in wordsShuffle.indices.reversed()) {
                        if (index >= showingIndex) {
                            WordCard(
                                word = wordsShuffle[index],
                                modifier = Modifier
                                    .offset(x = if (index == showingIndex) offsetX.dp else 20.dp)
                                    .zIndex(if (index == showingIndex) 1f else 0f)
                                    .pointerInput(Unit) {
                                        detectDragGestures(
                                            onDragEnd = {
                                                if (offsetX > dragThreshold && showingIndex > 0) {
                                                    showingIndex--
                                                } else if (offsetX < -dragThreshold && showingIndex < wordsShuffle.size - 1) {
                                                    showingIndex++
                                                }
                                                offsetX = 0f },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                if (index == showingIndex) {
                                                    offsetX += dragAmount.x
                                                }
                                            }
                                        )
                                    },
                                rotationDegrees = if (index == showingIndex) 0f else 5f
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "No words registered",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Button(
                onClick = { wordsShuffle = wordsShuffle.shuffled().toMutableList() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(60.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.mid_blue_memora),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "SHUFFLE THE CARDS",
                    fontSize = 18.sp
                )
            }
        }
    }
}



@Composable
fun WordCard(
    word: Word,
    modifier: Modifier = Modifier,
    rotationDegrees: Float = 0f
) {

    var turn by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (turn) 360f else 0f,
        animationSpec = tween(durationMillis = 400)
    )

    Box(modifier = modifier

        .size(width = 300.dp, height = 500.dp)
        .graphicsLayer {
            rotationZ = rotationDegrees
            rotationY = rotation
            cameraDistance = 12f * density
        }
        .shadow(
            elevation = 10.dp,
            shape = RoundedCornerShape(16.dp)
        )
        .clip(RoundedCornerShape(16.dp))
        .background(
            Brush.verticalGradient(
                colors = listOf(colorResource(id = R.color.mid_blue_memora), colorResource(id = R.color.dark_blue_memora))
            )
        )
        .wrapContentSize()

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
                text = if (turn) word.word else word.translation,
                color = Color.White,
                fontSize = 24.sp,
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
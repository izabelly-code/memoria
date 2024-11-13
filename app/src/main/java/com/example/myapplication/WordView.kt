package com.example.myapplication

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordView(navController: NavController) {
    val context = LocalContext.current
    val viewModel = remember { WordViewModel(context) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBarView()
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier,
                contentColor = Color.White,
                containerColor = colorResource(id = R.color.mid_blue_memora)
                , onClick = {
                    showBottomSheet = true
                    scope.launch { sheetState.show() }
                }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Deck", fontSize = 42.sp, fontWeight = FontWeight.Bold)
                IconButton(
                    onClick = {
                        navController.navigate("ShuffleView")
                    },
                    modifier = Modifier.size(42.dp)
                        .background(Color.White, shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.shuffle_icon),
                        contentDescription = null,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (viewModel.wordBank.isNotEmpty()) {

                    items(viewModel.wordBank.size) { index ->
                        val word = viewModel.wordBank[index]
                        CardView(word = word.word, translation = word.translation, onClick =  {
                            navController.navigate("CardDetailView/${word.word}/${word.translation}")
                        }, onDelete = {
                            scope.launch {
                                viewModel.removeWord(word)
                            }
                        })

                    }
                }

            }
                }
        }
        if (showBottomSheet) {
            var word by remember { mutableStateOf("") }
            var translation by remember { mutableStateOf("") }

            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "Add new flashcard", fontSize = 42.sp, fontWeight = FontWeight.Bold)
                        TextField(
                            value = word,
                            onValueChange = { newText -> word = newText },
                            label = { Text("Type the word") },
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                                .background(color = Color.LightGray, shape = RoundedCornerShape(24.dp))
                            ,
                            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )

                        )
                        TextField(
                            value = translation,
                            onValueChange = { newText -> translation = newText },
                            label = { Text("Type the translation") },
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                                .background(color = Color.LightGray, shape = RoundedCornerShape(80.dp))
                            ,
                            textStyle = TextStyle(color = Color.LightGray, fontSize = 18.sp),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        Button(onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    viewModel.addWord(word = word, translation = translation)
                                    showBottomSheet = false
                                }
                            }
                        },
                            colors = ButtonColors(
                                containerColor = colorResource(id = R.color.mid_blue_memora),
                                contentColor = Color.White,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.LightGray),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp, start = 8.dp, end = 8.dp)) {
                            Text("Add flashcard", modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            }        }
    }

}
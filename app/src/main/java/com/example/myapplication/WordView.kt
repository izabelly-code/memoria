package com.example.myapplication

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)

            ) {
                Image(
                    painterResource(id = R.drawable.top_bar_bg),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box (
                    Modifier
                        .size(40.dp),
                ) {
                    Image(
                        painterResource(id = R.drawable.icon_memora),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit

                    )
                }
            }


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
                        CardView(word = word.word, translation = word.translation) {
                            navController.navigate("CardDetailView/${word.word}/${word.translation}")
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
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextField(
                            value = word,
                            onValueChange = { newText -> word = newText },
                            label = { Text("Type the word") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = translation,
                            onValueChange = { newText -> translation = newText },
                            label = { Text("Type the translation") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    viewModel.addWord(word = word, translation = translation)
                                    showBottomSheet = false
                                }
                            }
                        }) {
                            Text("Add flashcard")
                        }
                    }
                }
            }        }
    }

}
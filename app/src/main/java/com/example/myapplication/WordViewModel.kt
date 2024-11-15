package com.example.myapplication

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import android.content.SharedPreferences

data class Word(
    var word: String,
    var translation: String
)

class WordViewModel(context: Context) : ViewModel() {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("flashcards", Context.MODE_PRIVATE)

    var wordBank: MutableList<Word> = mutableStateListOf()
        private set

    init {
        wordBank.addAll(loadWords())
    }
    fun addWord(word: String, translation: String) {
        wordBank.add(Word(word = word, translation = translation))
        saveWords()
    }

    fun removeWord(word: Word) {
        val wordToRemove = wordBank.find { it.word == word.word && it.translation == word.translation }
        if (wordToRemove != null) {
            wordBank.remove(wordToRemove)
            saveWords()
        }
    }

    private fun saveWords() {
        val editor = sharedPreferences.edit()
        val serializedWords = wordBank.joinToString(",") { "${it.word}:${it.translation}"}
        editor.putString("wordBank", serializedWords)
        editor.apply()
    }

    private fun loadWords(): MutableList<Word> {
        val serializedWords = sharedPreferences.getString("wordBank", "")
        return if (serializedWords.isNullOrEmpty()) {
            mutableListOf()
        } else {
            serializedWords.split(",").map {
                val parts = it.split(":")
                Word( word = parts[0], translation = parts[1])
            }.toMutableList()
        }
    }
}
package com.devnunens.hearx.data.model

data class Round(
    val difficulty: Int,
    val triplet: Triplet,
    val userInput: String,
    val isCorrect: Boolean
)
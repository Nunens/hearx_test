package com.devnunens.hearx.data.model

data class TestResult(
    val score: Int,
    val rounds: List<Round>,
    val timestamp: Long = System.currentTimeMillis(),
)
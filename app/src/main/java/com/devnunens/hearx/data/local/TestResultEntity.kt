package com.devnunens.hearx.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val score: Int,
    val flowJson: String,
    val timestamp: Long
)
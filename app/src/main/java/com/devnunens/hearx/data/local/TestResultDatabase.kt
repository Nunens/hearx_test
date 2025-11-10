package com.devnunens.hearx.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TestResultEntity::class], version = 1, exportSchema = false)
abstract class TestResultDatabase : RoomDatabase() {
    abstract fun testResultDao(): TestResultDao
}
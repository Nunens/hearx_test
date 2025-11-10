package com.devnunens.hearx.data.repository

import android.util.Log
import com.devnunens.hearx.data.local.TestResultDao
import com.devnunens.hearx.data.local.TestResultEntity
import com.devnunens.hearx.data.model.TestResult
import com.devnunens.hearx.data.model.request.TestPayload
import com.devnunens.hearx.data.remote.TestService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class TestRepositoryImpl(
    private val apiService: TestService,
    private val testResultDao: TestResultDao
): TestRepository {
    override suspend fun uploadTestResult(result: TestPayload) {
        try {
            apiService.uploadTestResult(result)
        } catch (e: Exception) {
            Log.e("TestRepository", "Error uploading test result", e)
        }
    }

    override suspend fun saveTestResultLocally(result: TestResult) {
        val entity = TestResultEntity(
            score = result.score,
            flowJson = Gson().toJson(result.rounds),
            timestamp = result.timestamp
        )
        testResultDao.insert(entity)
    }

    override suspend fun getAllTestResults(): Flow<List<TestResultEntity>> {
        return testResultDao.getAllResults()
    }
}
package com.devnunens.hearx.data.repository

import com.devnunens.hearx.data.local.TestResultEntity
import com.devnunens.hearx.data.model.TestResult
import com.devnunens.hearx.data.model.request.TestPayload
import kotlinx.coroutines.flow.Flow

interface TestRepository {
    suspend fun uploadTestResult(result: TestPayload)

    suspend fun saveTestResultLocally(result: TestResult)

    suspend fun getAllTestResults(): Flow<List<TestResultEntity>>
}
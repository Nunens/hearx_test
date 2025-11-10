package com.devnunens.hearx.data.remote

import com.devnunens.hearx.data.model.request.TestPayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TestService {
    @POST("/")
    suspend fun uploadTestResult(@Body result: TestPayload): Response<Unit>
}
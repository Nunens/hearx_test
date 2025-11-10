package com.devnunens.hearx.dimodule

import android.content.Context
import androidx.room.Room
import com.devnunens.hearx.data.local.TestResultDao
import com.devnunens.hearx.data.local.TestResultDatabase
import com.devnunens.hearx.data.remote.TestService
import com.devnunens.hearx.data.repository.TestRepository
import com.devnunens.hearx.data.repository.TestRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://enoqczf2j2pbadx.m.pipedream.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): TestService {
        return retrofit.create(TestService::class.java)
    }

    @Provides
    @Singleton
    fun provideTestResultDatabase(@ApplicationContext context: Context): TestResultDatabase {
        return Room.databaseBuilder(
            context,
            TestResultDatabase::class.java,
            "test_result_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTestResultDao(database: TestResultDatabase) = database.testResultDao()

    @Provides
    @Singleton
    fun provideTestRepository(apiService: TestService, dao: TestResultDao): TestRepository {
        return TestRepositoryImpl(apiService, dao)
    }
}
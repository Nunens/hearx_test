package com.devnunens.hearx.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devnunens.hearx.data.local.TestResultEntity
import com.devnunens.hearx.data.model.Round
import com.devnunens.hearx.data.model.TestResult
import com.devnunens.hearx.data.model.Triplet
import com.devnunens.hearx.data.model.request.RoundData
import com.devnunens.hearx.data.model.request.TestPayload
import com.devnunens.hearx.data.repository.TestRepository
import com.devnunens.hearx.player.TestPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: TestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TestUiState())
    val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

    var currentRound = 0
    var difficulty = 5
    private val history = mutableSetOf<String>()
    private var previousTriplet: Triplet? = null
    private val rounds = mutableListOf<Round>()
    private val audioPlayer = TestPlayer(context)

    data class TestUiState(
        val isTesting: Boolean = false,
        val currentTriplet: Triplet? = null,
        val userInput: String = "",
        val score: Int = 0,
        val showResults: Boolean = false,
        val resultsList: List<TestResultEntity> = emptyList()
    )

    init {
        loadResults()
    }

    fun startTest() {
        currentRound = 0
        difficulty = 5
        history.clear()
        rounds.clear()
        previousTriplet = null
        _uiState.value = _uiState.value.copy(isTesting = true, score = 0)
        viewModelScope.launch {
            delay(3000)
            nextRound()
        }
    }

    fun exitTest() {
        _uiState.value = _uiState.value.copy(isTesting = false, currentTriplet = null)
    }

    fun onUserInputChanged(input: String) {
        _uiState.value = _uiState.value.copy(userInput = input)
    }

    fun submitAnswer() {
        val state = _uiState.value
        if (state.userInput.length != 3) return

        val triplet = state.currentTriplet ?: return
        val isCorrect = state.userInput.all { it.isDigit() } &&
                state.userInput.map { it.digitToInt() } == triplet.digits
        val roundScore = if (isCorrect) difficulty else 0
        rounds.add(Round(difficulty, triplet, state.userInput, isCorrect))

        difficulty = if (isCorrect) minOf(10, difficulty + 1) else maxOf(1, difficulty - 1)
        currentRound++
        _uiState.value = _uiState.value.copy(
            userInput = "",
            score = state.score + roundScore
        )

        if (currentRound < 10) {
            viewModelScope.launch {
                delay(2000)
                nextRound()
            }
        } else {
            finishTest()
        }
    }

    private suspend fun nextRound() {
        val triplet = Triplet.generateUnique(previousTriplet, history)
        history.add(triplet.digits.joinToString(""))
        previousTriplet = triplet
        audioPlayer.playTriplet(difficulty, triplet.digits)
        _uiState.value = _uiState.value.copy(currentTriplet = triplet)
    }

    fun setShowResults(show: Boolean) {
        _uiState.value = _uiState.value.copy(showResults = show)
    }

    private fun finishTest() {
        viewModelScope.launch(Dispatchers.IO) {
            val testPayload = TestPayload(_uiState.value.score, rounds.map {
                RoundData(
                    difficulty = it.difficulty,
                    tripletPlayed = (
                            it.triplet.digits[0].toString() +
                                    it.triplet.digits[1].toString() +
                                    it.triplet.digits[2].toString()
                            ),
                    tripletAnswered = it.userInput
                )
            })
            try {
                repository.uploadTestResult(testPayload)
            } catch (e: Exception) {
                Log.e("TestViewModel", "Failed to upload test result", e)
            }

            repository.saveTestResultLocally(
                TestResult(
                    score = _uiState.value.score,
                    rounds = rounds,
                    timestamp = System.currentTimeMillis()
                )
            )
            loadResults()
            _uiState.value = _uiState.value.copy(isTesting = false, showResults = true)
        }
    }

    fun loadResults() {
        viewModelScope.launch {
            repository.getAllTestResults().collect { list ->
                _uiState.value = _uiState.value.copy(resultsList = list)
            }
        }
    }

    override fun onCleared() {
        audioPlayer.release()
        super.onCleared()
    }
}
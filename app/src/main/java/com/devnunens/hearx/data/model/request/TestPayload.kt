package com.devnunens.hearx.data.model.request

import com.google.gson.annotations.SerializedName

data class TestPayload(
    @SerializedName("score")
    val score: Int,

    @SerializedName("rounds")
    val rounds: List<RoundData>
)

data class RoundData(
    @SerializedName("difficulty")
    val difficulty: Int,

    @SerializedName("triplet_played")
    val tripletPlayed: String,

    @SerializedName("triplet_answered")
    val tripletAnswered: String
)
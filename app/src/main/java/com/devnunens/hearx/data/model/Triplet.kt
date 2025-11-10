package com.devnunens.hearx.data.model

data class Triplet(
    val digits: List<Int>
) {
    companion object {
        fun generateUnique(previous: Triplet?, history: Set<String>): Triplet {
            var attempts = 0
            while (attempts < 100) {
                val digits = List(3) { (1..9).random() }
                val key = digits.joinToString("")
                if (history.contains(key)) continue
                if (previous != null && digits.zip(previous.digits).any { (a, b) -> a == b }) continue
                return Triplet(digits)
            }
            throw IllegalStateException("Could not generate unique triplet")
        }
    }
}
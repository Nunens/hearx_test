package com.devnunens.hearx.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ConcatenatingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext


class TestPlayer(context: Context) {
    private val player = ExoPlayer.Builder(context).build()
    private val dataSourceFactory = DefaultDataSource.Factory(context)

    @OptIn(UnstableApi::class)
    suspend fun playTriplet(difficulty: Int, triplet: List<Int>) {
        withContext(coroutineContext) {
            val noiseSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri("asset:///noise_$difficulty.m4a"))
            val digitSources = triplet.map { digit ->
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri("asset:///$digit.m4a"))
            }

            val mediaSource = ConcatenatingMediaSource()
            mediaSource.addMediaSource(noiseSource)
            player.setMediaSource(noiseSource)
            player.prepare()
            player.playWhenReady = true

            delay(500)
            digitSources.forEachIndexed { index, source ->
                player.setMediaSource(source)
                player.prepare()
                player.playWhenReady = true
                delay(if (index < 2) 300 else 500)
            }
            player.stop()
            player.clearMediaItems()
        }
    }

    fun release() {
        player.release()
    }
}
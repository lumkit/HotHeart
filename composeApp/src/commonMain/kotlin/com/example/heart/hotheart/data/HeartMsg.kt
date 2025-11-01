package com.example.heart.hotheart.data

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class HeartMsg(
    val uuid: String = Uuid.random().toString(),
    val msg: String,
    val offsetX: Float,
    val offsetY: Float,
    val backgroundColor: String,
    val textColor: String,
) {

    private var visibility = false

    @Transient
    val alpha = Animatable(0f)

    @Transient
    val scale = Animatable(.5f)

    @Transient
    val offset = Animatable(0f)

    @Transient
    val rotation = Animatable(0f)

    suspend fun startAnimate() {
        if (visibility) return
        visibility = true
        withContext(Dispatchers.Default) {
            async {
                alpha.animateTo(
                    targetValue = 1f,
                )
            }
            async {
                scale.animateTo(
                    targetValue = 1f,
                )
            }
            async {
                rotation.animateTo(
                    targetValue = Random.nextFloat() * 15f * if (Random.nextBoolean()) 1f else -1f,
//                    animationSpec = tween(durationMillis = 1500, easing = LinearOutSlowInEasing)
                )
            }
            async {
                offset.animateTo(
                    targetValue = 50f + Random.nextInt(50),
                    animationSpec = tween(durationMillis = 6000 + Random.nextInt(1000), easing = FastOutSlowInEasing)
                )
            }
        }
    }

    suspend fun stopAnimate() {
        alpha.stop()
        scale.stop()
        offset.stop()
    }
}
package com.example.heart.hotheart.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.heart.hotheart.data.HeartMsg
import hotheart.composeapp.generated.resources.Res
import kotlin.random.Random

class HeartMsgProvider {
    var msgText by mutableStateOf(emptyList<String>())
    private val colorsName = listOf(
        "amber",
        "blue",
        "brown",
        "cyan",
        "deeporange",
        "deeppurple",
        "green",
        "indigo",
        "lightblue",
        "lightgreen",
        "lime",
        "orange",
        "pink",
        "purple",
        "purple",
        "red",
        "teal",
        "yellow",
    )

    var colors = emptyList<Color>()

    private fun createOffset(windowSize: DpSize): DpOffset {
        val widthVal = windowSize.width.value
        val heightVal = windowSize.height.value
        if (!widthVal.isFinite() || !heightVal.isFinite() || widthVal <= 0f || heightVal <= 0f) {
            return DpOffset(0.dp, 0.dp)
        }

        val randX = Random.nextDouble(-100.0, widthVal.toDouble()).toFloat()
        val randY = Random.nextDouble(-100.0, heightVal.toDouble()).toFloat()

        return DpOffset(randX.dp, randY.dp)
    }

    private fun createMsg(): String = msgText.random()

    private fun createBgColor(): Color = colors.random()

    suspend fun init() {
        msgText = Res.readBytes("files/msg").decodeToString().split("\n")
//        colors = colorsName.map {
//            val text = Res.readBytes("files/colors/$it").decodeToString()
//            string2Colors(text)
//        }.flatten()
        colors = Res.readBytes("files/colors").decodeToString().split("\n")
            .filter { it.isNotBlank() }
            .map {
                it.toComposeColor()
            }
    }

    fun setTextMsg(msg: String) {
        msgText = msg.split("\n")
    }

    fun createHeartMsg(windowSize: DpSize): HeartMsg {
        val backgroundColor = createBgColor()
        val dpOffset = createOffset(windowSize)
        return HeartMsg(
            msg = createMsg(),
            offsetX = dpOffset.x.value,
            offsetY = dpOffset.y.value,
            backgroundColor = backgroundColor.toHex(),
            textColor = backgroundColor.readableTextColorForBackground().toHex(),
        )
    }

    private fun string2Colors(text: String): List<Color> {
        if (!text.contains("(") || !text.contains(")")) return listOf()
        val afterLast = text.substring(text.indexOf("(") + 1, text.lastIndexOf(")"))
        val array = afterLast.split("\\s+".toRegex())
        return array.map {
            val text = it.split("#").lastOrNull() ?: "FFFFFF"
            "#$text".toComposeColor()
        }
    }

}
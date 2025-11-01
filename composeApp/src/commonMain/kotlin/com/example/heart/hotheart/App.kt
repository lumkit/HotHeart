package com.example.heart.hotheart

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.heart.hotheart.data.HeartMsg
import com.example.heart.hotheart.ui.typography
import com.example.heart.hotheart.util.HeartMsgProvider
import com.example.heart.hotheart.util.serializerSaver
import com.example.heart.hotheart.util.toComposeColor
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.random.Random

@Composable
@Preview
fun App() {
    val density = LocalDensity.current
    var boxSize by remember { mutableStateOf(DpSize.Unspecified) }
    val provider = remember { HeartMsgProvider() }
    var msgList by rememberSaveable(stateSaver = serializerSaver()) { mutableStateOf(emptyList<HeartMsg>()) }
    var settingDialogState by rememberSaveable { mutableStateOf(false) }

    var maxMsgCount by rememberSaveable { mutableStateOf(1000) }
    var nextShowMsgWaitTime by rememberSaveable { mutableStateOf(200L) }

    LaunchedEffect(Unit) {
        provider.init()
        delay(1000)
        async {
            while (isActive) {
                delay(1 + Random.nextLong(nextShowMsgWaitTime))
                val list = msgList.toMutableList()
                list += provider.createHeartMsg(boxSize)
                msgList = list.let {
                    if (it.size > maxMsgCount) it.takeLast(maxMsgCount - 100) else it
                }
            }
        }
    }

    MaterialTheme(
        typography = typography
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
                .onSizeChanged {
                    boxSize = with(density) { it.toSize().toDpSize() }
                },
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .combinedClickable(
                        indication = null,
                        interactionSource = null,
                        onClick = {

                        },
                        onLongClick = {
                            settingDialogState = true
                        },
                        onDoubleClick = {
                            settingDialogState = true
                        }
                    )
            ) {
                msgList.forEachIndexed { _, msg ->
                    MsgCard(msg)
                }
            }
        }

        if (settingDialogState) {
            var content by remember { mutableStateOf(provider.msgText.joinToString("\n")) }
            AlertDialog(
                modifier = Modifier.padding(vertical = 24.dp),
                onDismissRequest = { settingDialogState = false },
                title = {
                    Text(text = "自定义设置")
                },
                text = {
                    Column {
                        run {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row {
                                    Text(
                                        text = "最大消息数量（屏幕消息的个数超过给定值后就会回收以前的消息",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.weight(1f)
                                            .padding(end = 16.dp)
                                    )
                                    Text(
                                        text = "$maxMsgCount"
                                    )
                                }
                                Slider(
                                    value = maxMsgCount.toFloat(),
                                    onValueChange = {
                                        maxMsgCount = it.roundToInt()
                                    },
                                    valueRange = 150f..2000f,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        run {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row {
                                    Text(
                                        text = "随机消息最大弹出时间间隔（此功能可以设置消息弹出的时间间隔）",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.weight(1f)
                                            .padding(end = 16.dp)
                                    )
                                    Text(
                                        text = "${nextShowMsgWaitTime}毫秒"
                                    )
                                }
                                Slider(
                                    value = nextShowMsgWaitTime.toFloat(),
                                    onValueChange = {
                                        nextShowMsgWaitTime = it.roundToLong()
                                    },
                                    valueRange = 100f..1500f,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        run {
                            OutlinedTextField(
                                value = content,
                                onValueChange = {
                                    content = it
                                },
                                label = {
                                    Text("自定义消息内容（多条的话就用回车分开）")
                                },
                                modifier = Modifier.height(200.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            msgList = mutableListOf()
                            provider.setTextMsg(content)
                            settingDialogState = false
                        }
                    ) {
                        Text(text = "确定")
                    }
                },
                dismissButton = {
                    FilledTonalButton(
                        onClick = {
                            settingDialogState = false
                        }
                    ) {
                        Text("取消")
                    }
                }
            )
        }
    }
}

@Composable
private fun MsgCard(msg: HeartMsg) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val composeColor = msg.backgroundColor.toComposeColor()

    LaunchedEffect(msg) {
        msg.startAnimate()
    }

    DisposableEffect(msg) {
        onDispose {
            scope.launch {
                msg.stopAnimate()
            }
        }
    }

    Text(
        text = msg.msg,
        style = MaterialTheme.typography.titleMedium,
        color = msg.textColor.toComposeColor(),
        modifier = Modifier.offset(msg.offsetX.dp, msg.offsetY.dp - with(density) { msg.offset.value.toDp() })
            .alpha(msg.alpha.value)
            .scale(msg.scale.value)
            .shadow(elevation = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = composeColor)
            .padding(vertical = 12.dp, horizontal = 48.dp),
    )
}
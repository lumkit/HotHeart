package com.example.heart.hotheart.util

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * 把任意 @Serializable 的类型序列化为 String 存储的 Saver。
 *
 * T - 需要序列化的类型
 * json - 可传自定义 Json 配置（例如 ignoreUnknownKeys 等）
 *
 * 注意：要在调用处提供对应的 KSerializer<T>（或使用 reified 版本）
 */
inline fun <reified T> serializerSaver(
    serializer: KSerializer<T>,
    json: Json = Json {
        // 根据需要调整，如 ignoreUnknownKeys = true
        encodeDefaults = true
    }
): Saver<T, String> = Saver(
    save = { value ->
        // 将对象转为 string（JSON）
        json.encodeToString(serializer, value)
    },
    restore = { restoredString ->
        try {
            json.decodeFromString(restoredString)
        } catch (e: Exception) {
            // 反序列化失败返回 null（Saver 要求 restore 返回 T?）
            null
        }
    }
)

/**
 * reified 方便调用：serializer 由编译器生成
 */
inline fun <reified T> serializerSaver(
    json: Json = Json { encodeDefaults = true }
): Saver<T, String> {
    val ser = serializer<T>()
    return serializerSaver(ser, json)
}

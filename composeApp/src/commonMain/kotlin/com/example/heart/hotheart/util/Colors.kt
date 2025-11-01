package com.example.heart.hotheart.util

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * 把 sRGB 线性化（0..1 -> 线性光强）
 */
private fun srgbToLinear(c: Float): Float {
    return if (c <= 0.04045f) c / 12.92f else ((c + 0.055f) / 1.055f).pow(2.4f)
}

/**
 * 计算颜色的相对亮度（WCAG 定义），返回 0..1
 */
private fun relativeLuminance(color: Color): Float {
    // 如果 color 有透明度，先把其叠在白色背景上进行合成（更符合肉眼所见亮度）
    val alpha = color.alpha
    val r = if (alpha < 1f) (color.red * alpha + 1f * (1f - alpha)) else color.red
    val g = if (alpha < 1f) (color.green * alpha + 1f * (1f - alpha)) else color.green
    val b = if (alpha < 1f) (color.blue * alpha + 1f * (1f - alpha)) else color.blue

    val rLin = srgbToLinear(r)
    val gLin = srgbToLinear(g)
    val bLin = srgbToLinear(b)

    return 0.2126f * rLin + 0.7152f * gLin + 0.0722f * bLin
}

/**
 * contrast ratio between two colors according to WCAG:
 * (L1 + 0.05) / (L2 + 0.05) where L1 >= L2
 */
private fun contrastRatio(c1: Color, c2: Color): Float {
    val l1 = relativeLuminance(c1)
    val l2 = relativeLuminance(c2)
    val (maxL, minL) = if (l1 >= l2) l1 to l2 else l2 to l1
    return (maxL + 0.05f) / (minL + 0.05f)
}

/**
 * 根据背景色返回一个可读的文本色（白或黑）。选择和背景对比度更高的颜色。
 *
 * 如果你想要阈值控制（比如优先保证至少 4.5:1），可以传 minContrast 参数，
 * 这个函数会返回在黑/白中对比度更高的那个。如果两者都不满足 minContrast，则仍返回对比度更高的那个。
 */
fun Color.readableTextColorForBackground(
    minContrast: Float? = null // 可选：如 4.5f 表示希望满足 WCAG AA
): Color {
    val black = Color.Black
    val white = Color.White
    val contrastWithBlack = contrastRatio(this, black)
    val contrastWithWhite = contrastRatio(this, white)

    val chosen = if (contrastWithBlack >= contrastWithWhite) black else white

    // 如果传入 minContrast，可以做一次尝试：如果选中的不满足但另一个满足则改用满足的那个
    if (minContrast != null) {
        val chosenContrast = max(contrastWithBlack, contrastWithWhite)
        val other = if (chosen == black) white else black
        val otherContrast = if (chosen == black) contrastWithWhite else contrastWithBlack

        // 如果被选中的不满足 minContrast，而另一个满足，则选另一个
        if (chosenContrast < minContrast && otherContrast >= minContrast) {
            return other
        }
        // 否则返回对比度更高的那个（即 chosen）
    }

    return chosen
}

fun String.toComposeColor(): Color {
    val s = this.trim().removePrefix("#")
    val hex = when (s.length) {
        8 -> s // AARRGGBB
        6 -> "FF$s" // 默认 alpha = FF
        4 -> { // ARGB -> AARRGGBB, 每位重复
            val a = s[0].toString().repeat(2)
            val r = s[1].toString().repeat(2)
            val g = s[2].toString().repeat(2)
            val b = s[3].toString().repeat(2)
            "$a$r$g$b"
        }
        3 -> { // RGB -> RRGGBB -> AARRGGBB with FF
            val r = s[0].toString().repeat(2)
            val g = s[1].toString().repeat(2)
            val b = s[2].toString().repeat(2)
            "FF$r$g$b"
        }
        else -> throw IllegalArgumentException("Unsupported color format: $this")
    }

    val a = hex.take(2).toInt(16)
    val r = hex.substring(2, 4).toInt(16)
    val g = hex.substring(4, 6).toInt(16)
    val b = hex.substring(6, 8).toInt(16)

    return Color(
        red = r / 255f,
        green = g / 255f,
        blue = b / 255f,
        alpha = a / 255f
    )
}

private fun Int.toHex2(): String {
    // 保证 0..255，然后转 16 进制并补零到两位（大写）
    val v = (this and 0xFF)
    return v.toString(16).uppercase().padStart(2, '0')
}

/**
 * 将 Compose Color 转为十六进制字符串。
 * includeAlpha = true -> "#AARRGGBB"
 * includeAlpha = false -> "#RRGGBB"
 */
fun Color.toHex(includeAlpha: Boolean = true): String {
    // 直接把 0..1f 通道转换为 0..255 的 Int（四舍五入）
    val a = (alpha * 255f).roundToInt().coerceIn(0, 255)
    val r = (red * 255f).roundToInt().coerceIn(0, 255)
    val g = (green * 255f).roundToInt().coerceIn(0, 255)
    val b = (blue * 255f).roundToInt().coerceIn(0, 255)

    return if (includeAlpha) {
        "#" + a.toHex2() + r.toHex2() + g.toHex2() + b.toHex2()
    } else {
        "#" + r.toHex2() + g.toHex2() + b.toHex2()
    }
}
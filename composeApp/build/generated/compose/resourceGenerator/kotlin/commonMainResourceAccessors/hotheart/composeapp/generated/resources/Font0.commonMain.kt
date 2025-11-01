@file:OptIn(InternalResourceApi::class)

package hotheart.composeapp.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/hotheart.composeapp.generated.resources/"

internal val Res.font.SourceHanSerifSC_Regular_small: FontResource by lazy {
      FontResource("font:SourceHanSerifSC_Regular_small", setOf(
        ResourceItem(setOf(), "${MD}font/SourceHanSerifSC-Regular-small.ttf", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainFont0Resources(map: MutableMap<String, FontResource>) {
  map.put("SourceHanSerifSC_Regular_small", Res.font.SourceHanSerifSC_Regular_small)
}

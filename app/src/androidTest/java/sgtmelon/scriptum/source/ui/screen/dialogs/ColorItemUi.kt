package sgtmelon.scriptum.source.ui.screen.dialogs

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.adapter.ColorAdapter
import sgtmelon.scriptum.infrastructure.model.data.ColorData
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.source.ui.basic.withColorIndicator
import sgtmelon.scriptum.source.ui.parts.recycler.RecyclerItemPart
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withContentDescription
import sgtmelon.test.cappuccino.utils.withDrawableColor
import sgtmelon.test.cappuccino.utils.withSize

/**
 * Class for UI control of [ColorAdapter].
 */
class ColorItemUi(
    listMatcher: Matcher<View>,
    p: Int
) : RecyclerItemPart<ColorItemUi.Model>(listMatcher, p) {

    private val parentContainer by lazy { getChild(getView(R.id.parent_container)) }
    private val backgroundView by lazy { getChild(getView(R.id.background_view)) }
    private val checkImage by lazy { getChild(getView(R.id.check_image)) }
    private val clickView by lazy { getChild(getView(R.id.click_view)) }

    override fun assert(item: Model) {
        parentContainer.isDisplayed()

        backgroundView.isDisplayed()
            .withSize(R.dimen.icon_48dp, R.dimen.icon_48dp)
            .withColorIndicator(R.drawable.ic_color, theme, item.color)

        val colorId = ColorData.getColorItem(theme, item.color).content
        checkImage.isDisplayed(item.isCheck).withDrawableColor(R.drawable.ic_check, colorId)

        val colorName = context.resources.getStringArray(R.array.pref_color)[item.color.ordinal]
        val description = context.getString(R.string.description_item_color, colorName)
        clickView.isDisplayed()
                .withSize(R.dimen.icon_48dp, R.dimen.icon_48dp)
                .withContentDescription(description)
    }

    /** Model for [assert]. */
    data class Model(val color: Color, val isCheck: Boolean)

}
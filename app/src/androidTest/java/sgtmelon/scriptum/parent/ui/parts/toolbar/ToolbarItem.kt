package sgtmelon.scriptum.parent.ui.parts.toolbar

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.parent.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.longClick

/**
 * UI abstraction for toolbar items.
 */
class ToolbarItem(
    @IdRes val itemId: Int,
    @DrawableRes val iconId: Int,
    @StringRes val labelId: Int,
    @AttrRes val tintAttr: Int = R.attr.clContent
) : UiPart() {

    private val item = getView(itemId)

    fun click() {
        item.click()
    }

    fun showLabel() {
        item.longClick()
    }

    fun assert() {
        item.isDisplayed()
    }
}
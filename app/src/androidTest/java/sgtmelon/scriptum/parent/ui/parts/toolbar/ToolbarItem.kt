package sgtmelon.scriptum.parent.ui.parts.toolbar

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.parent.ui.action.longClick
import sgtmelon.scriptum.parent.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed

/**
 * UI abstraction for toolbar items. Assertion will happen in another place. It's just like a
 * storage for item data.
 */
class ToolbarItem(
    @IdRes val itemId: Int,
    @DrawableRes val iconId: Int,
    @StringRes val labelId: Int,
    @AttrRes val tintAttr: Int = R.attr.clContent
) : UiPart() {

    private val item = getView(itemId)

    fun click() = apply { item.click() }

    fun showLabel() = apply { item.longClick(commandAutomator) }

    fun assert() = apply { item.isDisplayed() }

}
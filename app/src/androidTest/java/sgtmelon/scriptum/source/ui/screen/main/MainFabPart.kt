package sgtmelon.scriptum.source.ui.screen.main

import android.view.ViewGroup.LayoutParams
import sgtmelon.scriptum.R
import sgtmelon.scriptum.source.ui.parts.UiPart
import sgtmelon.scriptum.source.ui.screen.dialogs.sheet.AddSheetDialogUi
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withCardElevation
import sgtmelon.test.cappuccino.utils.withCardRadius
import sgtmelon.test.cappuccino.utils.withContentDescription
import sgtmelon.test.cappuccino.utils.withDrawableAttr
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withSizeCode

class MainFabPart : UiPart() {

    private val fabCard = getView(R.id.gradient_fab_card)
    private val fabClick = getView(R.id.gradient_fab_click)
    private val fabIcon = getView(R.id.gradient_fab_icon)

    fun click(func: AddSheetDialogUi.() -> Unit = {}) {
        fabClick.click()
        AddSheetDialogUi(func)
    }

    fun assert(isVisible: Boolean) {
        fabCard.isDisplayed(isVisible) {
            withSize(R.dimen.gradient_fab_size, R.dimen.gradient_fab_size)
            withCardRadius(R.dimen.gradient_fab_radius)
            withCardElevation(R.dimen.gradient_fab_elevation)

            fabClick.isDisplayed()
                .isEnabled()
                .withSizeCode(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

            fabIcon.isDisplayed()
                .withSize(R.dimen.gradient_fab_image_size, R.dimen.gradient_fab_image_size)
                .withDrawableAttr(R.drawable.ic_add, R.attr.clBackgroundView)
                .withContentDescription(R.string.description_add_note)
        }.isEnabled(isVisible)
    }
}
package sgtmelon.scriptum.parent.ui.parts.info

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.parent.ui.model.key.InfoCase
import sgtmelon.scriptum.parent.ui.parts.UiSubpart
import sgtmelon.test.cappuccino.utils.includeParent
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withDrawable
import sgtmelon.test.cappuccino.utils.withDrawableAttr
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withTextColor
import sgtmelon.test.cappuccino.utils.withTextSize

/**
 * Parent class for UI abstraction of info containers.
 */
class InfoContainerPart(
    parentContainer: Matcher<View>,
    private val case: InfoCase
) : UiSubpart(parentContainer) {

    private val includeContainer = getView(R.id.info_inc)
    private val iconImage = getView(R.id.info_image).includeParent(includeContainer)
    private val titleText = case.titleId?.let { getView(R.id.title_text, it) }
    private val detailsText = case.detailsId?.let { getView(R.id.details_text, it) }

    fun assert(isVisible: Boolean) {
        includeContainer.isDisplayed(isVisible)

        if (case.iconId != null) {
            iconImage.isDisplayed(isVisible) {
                withSize(R.dimen.icon_128dp, R.dimen.icon_128dp)
            }.withDrawableAttr(case.iconId, R.attr.clContent)
        } else {
            iconImage.isDisplayed(value = false)
                .withDrawable(resourceId = null)
        }

        titleText?.isDisplayed(isVisible)
            ?.withTextColor(R.attr.clContent)
            ?.withTextSize(R.dimen.text_18sp)

        detailsText?.isDisplayed(isVisible)
            ?.withTextColor(R.attr.clContentSecond)
            ?.withTextSize(R.dimen.text_14sp)
    }
}
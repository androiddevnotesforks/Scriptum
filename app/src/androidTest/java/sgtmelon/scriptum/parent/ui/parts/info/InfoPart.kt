package sgtmelon.scriptum.parent.ui.parts.info

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.testData.InfoPage
import sgtmelon.scriptum.parent.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.includeParent
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withDrawable
import sgtmelon.test.cappuccino.utils.withDrawableAttr
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withTextColor
import sgtmelon.test.cappuccino.utils.withTextSize

/**
 * Parent class for UI abstraction of info parts
 */
class InfoPart(private val page: InfoPage) : UiPart() {

    private val includeContainer = getView(R.id.info_include)
    private val iconImage = getView(R.id.info_image).includeParent(includeContainer)
    private val titleText = getView(R.id.title_text, page.titleId)
    private val detailsText = getView(R.id.details_text, page.detailsId)

    fun assert(isVisible: Boolean) {
        includeContainer.isDisplayed(isVisible)

        if (page.iconId != null) {
            iconImage.isDisplayed(isVisible) {
                withSize(R.dimen.icon_128dp, R.dimen.icon_128dp)
            }.withDrawableAttr(page.iconId, R.attr.clContent)
        } else {
            iconImage.isDisplayed(isVisible = false)
                .withDrawable(resourceId = null)
        }

        titleText.isDisplayed(isVisible)
            .withTextColor(R.attr.clContent)
            .withTextSize(R.dimen.text_18sp)

        detailsText.isDisplayed(isVisible)
            .withTextColor(R.attr.clContentSecond)
            .withTextSize(R.dimen.text_14sp)
    }
}
package sgtmelon.scriptum.ui.part.info

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.ui.ParentUi

/**
 * Part of UI abstraction for control roll note list info.
 */
class RollNoteInfoContainer(isListEmpty: Boolean, isListHide: Boolean) : ParentUi() {

    private val includeContainer = getViewById(R.id.roll_note_info_include)

    private val iconImage get() = getViewById(R.id.info_image).includeParent(includeContainer)

    private val titleText: Matcher<View>? = when {
        isListEmpty -> getView(R.id.info_title_text, R.string.info_roll_empty_title)
        isListHide -> getView(R.id.info_title_text, R.string.info_roll_hide_title)
        else -> null
    }

    private val detailsText: Matcher<View>? = when {
        isListEmpty -> getView(R.id.info_details_text, R.string.info_roll_empty_details)
        isListHide -> getView(R.id.info_details_text, R.string.info_roll_hide_details)
        else -> null
    }

    fun assert(visible: Boolean) = waitBefore(time = 200) {
        includeContainer.isDisplayed(visible)

        iconImage.isDisplayed(visible = false)
                .withDrawableAttr(resourceId = -1)

        titleText?.isDisplayed(visible)
                ?.withTextColor(R.attr.clContent)
                ?.withTextSize(R.dimen.text_18sp)

        detailsText?.isDisplayed(visible)
                ?.withTextColor(R.attr.clContentSecond)
                ?.withTextSize(R.dimen.text_14sp)
    }

}
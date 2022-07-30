package sgtmelon.scriptum.ui.part.info

import android.view.View
import androidx.annotation.IdRes
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.withDrawableAttr
import sgtmelon.scriptum.basic.extension.withSize
import sgtmelon.scriptum.basic.extension.withTextColor
import sgtmelon.scriptum.basic.extension.withTextSize
import sgtmelon.scriptum.ui.ParentUi

/**
 * Parent class for UI abstraction of info
 */
abstract class ParentInfoContainer(@IdRes private val iconId: Int? = null) : ParentUi() {

    abstract val includeContainer: Matcher<View>
    abstract val titleText: Matcher<View>
    abstract val detailsText: Matcher<View>

    private val iconImage get() = getViewById(R.id.info_image).includeParent(includeContainer)

    fun assert(isVisible: Boolean) {
        includeContainer.isDisplayed(isVisible)

        iconImage.isDisplayed(isVisible) {
            withSize(R.dimen.icon_128dp, R.dimen.icon_128dp)
        }.withDrawableAttr(iconId, R.attr.clContent)

        titleText.isDisplayed(isVisible)
            .withTextColor(R.attr.clContent)
            .withTextSize(R.dimen.text_18sp)

        detailsText.isDisplayed(isVisible)
            .withTextColor(R.attr.clContentSecond)
            .withTextSize(R.dimen.text_14sp)
    }

}
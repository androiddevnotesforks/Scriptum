package sgtmelon.scriptum.ui.item

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.withText
import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.ui.ParentRecyclerItem

/**
 * Class for UI control of [ParentPreferenceFragment].
 */
class PreferenceItemUi(
    listMatcher: Matcher<View>,
    p: Int
) : ParentRecyclerItem<PreferenceItem>(listMatcher, p) {

    override fun assert(item: PreferenceItem) = when (item) {
        is PreferenceItem.Header -> Header().assert(item)
        is PreferenceItem.Simple -> Simple().assert(item)
        is PreferenceItem.Summary -> Summary().assert(item)
    }

    inner class Header {

        private val titleText = getChild(getViewById(android.R.id.title))

        fun assert(item: PreferenceItem.Header) {
            titleText.isDisplayed()
                .withText(item.titleId, R.attr.clAccent)
        }
    }

    inner class Simple {

        private val titleText = getChild(getViewById(android.R.id.title))

        fun assert(item: PreferenceItem.Simple) {
            titleText.isDisplayed()
                .withText(item.titleId)
        }

        fun onItemClick() {
            titleText.click()
        }
    }

    inner class Summary {

        private val titleText = getChild(getViewById(android.R.id.title))
        private val summaryText = getChild(getViewById(android.R.id.summary))

        fun assert(item: PreferenceItem.Summary) {
            titleText.isDisplayed()
                .withText(item.titleId)
            summaryText.isDisplayed()
                .withText(item.summaryText)
        }

        fun onItemClick() {
            titleText.click()
        }
    }

}
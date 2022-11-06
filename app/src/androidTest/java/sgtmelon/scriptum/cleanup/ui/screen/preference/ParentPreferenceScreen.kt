package sgtmelon.scriptum.cleanup.ui.screen.preference

import androidx.annotation.StringRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.cleanup.ui.IPressBack
import sgtmelon.scriptum.cleanup.ui.ParentScreen
import sgtmelon.scriptum.cleanup.ui.item.PreferenceItemUi
import sgtmelon.scriptum.cleanup.ui.logic.parent.ParentPreferenceLogic
import sgtmelon.scriptum.cleanup.ui.part.toolbar.SimpleToolbar
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceActivity
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed

/**
 * Class for UI control of fragments inside [PreferenceActivity].
 */
abstract class ParentPreferenceScreen<L : ParentPreferenceLogic>(
    @StringRes titleId: Int
) : ParentScreen(),
    IPressBack {

    //region Views

    private val recyclerView = getViewById(R.id.recycler_view)

    private val parentContainer = getViewById(R.id.parent_container)
    private val toolbar = SimpleToolbar(titleId, withBack = true)

    protected fun getItem(p: Int) = PreferenceItemUi(recyclerView, p)

    //endregion

    abstract val screenLogic: L

    private fun getScreenList(): List<PreferenceItem> = screenLogic.getScreenList()

    fun onClickClose() {
        toolbar.getToolbarButton().click()
    }

    fun assert() = apply {
        parentContainer.isDisplayed()
        toolbar.assert()

        for ((i, item) in getScreenList().withIndex()) {
            getItem(i).assert(item)
        }
    }
}
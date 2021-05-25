package sgtmelon.scriptum.ui.screen.preference

import androidx.annotation.StringRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceActivity
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.item.PreferenceItemUi
import sgtmelon.scriptum.ui.part.toolbar.SimpleToolbar

/**
 * Class for UI control of fragments inside [PreferenceActivity].
 */
abstract class ParentPreferenceScreen(@StringRes titleId: Int) : ParentUi(),
    IPressBack {

    //region Views

    private val recyclerView = getViewById(R.id.recycler_view)

    private val parentContainer = getViewById(R.id.preference_parent_container)
    private val toolbar = SimpleToolbar(titleId, withBack = true)

    protected fun getItem(p: Int) = PreferenceItemUi(recyclerView, p)

    //endregion

    protected val provider = SummaryProvider(context.resources)

    abstract fun getScreenList(): List<PreferenceItem>

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
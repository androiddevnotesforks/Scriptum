package sgtmelon.scriptum.parent.ui.parts.preferences

import androidx.annotation.StringRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.cleanup.ui.item.PreferenceItemUi
import sgtmelon.scriptum.cleanup.ui.logic.parent.PreferenceLogic
import sgtmelon.scriptum.cleanup.ui.part.toolbar.SimpleToolbar
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceActivity
import sgtmelon.scriptum.parent.ui.feature.BackPress
import sgtmelon.scriptum.parent.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.isDisplayed

/**
 * Class for UI control of fragments inside [PreferenceActivity].
 */
abstract class ParentPreferencePart<L : PreferenceLogic>(
    @StringRes titleId: Int
) : UiPart(),
    BackPress {

    //region Views

    private val recyclerView by lazy { getView(R.id.recycler_view) }
    private val parentContainer by lazy { getView(R.id.parent_container) }

    private val toolbar = SimpleToolbar(titleId, withBack = true)

    protected fun getItem(p: Int) = PreferenceItemUi(recyclerView, p)

    //endregion

    abstract val screenLogic: L

    private fun getScreenList(): List<PreferenceItem> = screenLogic.getScreenList()

    fun clickClose() = toolbar.clickButton()

    fun assert() = apply {
        parentContainer.isDisplayed()
        toolbar.assert()

        for ((i, item) in getScreenList().withIndex()) {
            getItem(i).assert(item)
        }
    }
}
package sgtmelon.scriptum.parent.ui.parts.preferences

import androidx.annotation.StringRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.cleanup.ui.item.PreferenceItemUi
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceActivity
import sgtmelon.scriptum.parent.ui.feature.BackPress
import sgtmelon.scriptum.parent.ui.feature.ToolbarBack
import sgtmelon.scriptum.parent.ui.parts.ContainerPart
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerPart
import sgtmelon.scriptum.parent.ui.parts.toolbar.TitleToolbarPart
import sgtmelon.test.cappuccino.utils.isDisplayed

/**
 * Class for UI control of fragments inside [PreferenceActivity].
 */
abstract class PreferencePart<L : PreferenceLogic>(
    @StringRes titleId: Int,
    @TestViewTag tag: String
) : ContainerPart(tag),
    RecyclerPart<PreferenceItem, PreferenceItemUi>,
    ToolbarBack,
    BackPress {

    abstract val screenLogic: L

    override val toolbar = TitleToolbarPart(parentContainer, titleId)

    override val recyclerView by lazy { getView(R.id.recycler_view) }

    override fun getItem(p: Int) = PreferenceItemUi(recyclerView, p)

    fun assert() = apply {
        parentContainer.isDisplayed()
        toolbar.assert()
        recyclerView.isDisplayed()
        assertList(screenLogic.getScreenList())
    }
}
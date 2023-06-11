package sgtmelon.scriptum.source.ui.parts.preferences

import androidx.annotation.StringRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.item.PreferenceItemUi
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceActivity
import sgtmelon.scriptum.source.ui.feature.BackPress
import sgtmelon.scriptum.source.ui.feature.ToolbarBack
import sgtmelon.scriptum.source.ui.model.PreferenceItem
import sgtmelon.scriptum.source.ui.parts.ContainerPart
import sgtmelon.scriptum.source.ui.parts.recycler.RecyclerPart
import sgtmelon.scriptum.source.ui.parts.toolbar.TitleToolbarPart
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

    protected fun getItem(enum: Enum<*>) = getItem(enum.ordinal)

    override fun getItem(p: Int) = PreferenceItemUi(recyclerView, p)

    fun assert() = apply {
        parentContainer.isDisplayed()
        toolbar.assert()
        recyclerView.isDisplayed()
        assertList(screenLogic.getScreenList())
    }
}
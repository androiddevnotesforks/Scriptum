package sgtmelon.scriptum.ui.screen.main

import androidx.test.espresso.Espresso.closeSoftKeyboard
import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Часть UI абстракции для [RankScreen]
 *
 * @author SerjantArbuz
 */
class RankToolbar : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun onEnterName(name: String, isAddEnabled: Boolean) {
        action { onEnter(R.id.toolbar_rank_enter, name) }
        assert { onDisplayContent(isClearEnabled = name.isNotEmpty(), isAddEnabled = isAddEnabled) }
    }

    fun onClickClear() {
        action { onClick(R.id.toolbar_rank_cancel_button) }
        assert { onDisplayContent() }
    }

    fun onClickAdd() {
        closeSoftKeyboard()
        action { onClick(R.id.toolbar_rank_add_button) }
        assert { onDisplayContent() }
    }

    fun onLongClickAdd() = action { onLongClick(R.id.toolbar_rank_add_button) }

    companion object {
        operator fun invoke(func: RankToolbar.() -> Unit) = RankToolbar().apply(func)
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(isClearEnabled: Boolean = false, isAddEnabled: Boolean = false) {
            onDisplay(R.id.toolbar_rank_container)
            onDisplay(R.id.toolbar_rank_cancel_button)
            onDisplay(R.id.toolbar_rank_enter)
            onDisplay(R.id.toolbar_rank_add_button)

            isEnabled(R.id.toolbar_rank_cancel_button, isClearEnabled)
            isEnabled(R.id.toolbar_rank_add_button, isAddEnabled)
        }

    }

}
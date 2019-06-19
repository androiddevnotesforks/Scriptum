package sgtmelon.scriptum.ui.widget

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

class RankToolbar : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun onEnterName(name: String) = action { onEnter(R.id.toolbar_rank_enter, name) }

    fun onClickClear() = action { onClick(R.id.toolbar_rank_cancel_button) }

    fun onClickAdd() = action { onClick(R.id.toolbar_rank_add_button) }

    fun onLongClickAdd() = action { onLongClick(R.id.toolbar_rank_add_button) }

    companion object {
        operator fun invoke(func: RankToolbar.() -> Unit) = RankToolbar().apply {
            assert { onDisplayContent() }
            func()
        }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent() {
            onDisplay(R.id.toolbar_rank_container)
            onDisplay(R.id.toolbar_rank_cancel_button)
            onDisplay(R.id.toolbar_rank_enter)
            onDisplay(R.id.toolbar_rank_add_button)
        }

        fun isClearEnabled(enabled: Boolean) = isEnabled(R.id.toolbar_rank_cancel_button, enabled)

        fun isAddEnabled(enabled: Boolean) = isEnabled(R.id.toolbar_rank_add_button, enabled)

    }

}
package sgtmelon.scriptum.ui.screen.main

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import sgtmelon.scriptum.R
import sgtmelon.scriptum.screen.view.main.RankFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.dialog.RenameDialogUi
import sgtmelon.scriptum.ui.widget.RankToolbar

/**
 * Класс для ui контроля экрана [RankFragment]
 *
 * @author SerjantArbuz
 */
class RankScreen : ParentRecyclerScreen(R.id.rank_recycler) {

    private fun rankAction(func: RankAction.() -> Unit) = RankAction().apply { func() }

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun toolbar(func: RankToolbar.() -> Unit) = RankToolbar().apply {
        assert { onDisplayContent() }
        func()
    }

    fun renameDialogUi(title: String, p: Int = 0, func: RenameDialogUi.() -> Unit = {}) =
            RenameDialogUi(title).apply {
                onClickItem(p)
                assert { onDisplayContent(enter = "") }
                func()
            }

    fun onClickVisible(name: String) = rankAction { onClick(name, R.id.rank_visible_button) }

    fun onLongClickVisible(name: String) = rankAction { onLongClick(name, R.id.rank_visible_button) }

    fun onClickCancel(name: String) = rankAction { onClick(name, R.id.rank_cancel_button) }

    class RankAction {

        fun onClick(name: String, @IdRes childId: Int) =
                getRankButton(name, childId).perform(click())

        fun onLongClick(name: String, @IdRes childId: Int) =
                getRankButton(name, childId).perform(longClick())

        private fun getRankButton(name: String, @IdRes childId: Int) =
                onView(allOf(withId(childId), withParent(allOf(
                        withId(R.id.rank_click_container),
                        withChild(allOf(withId(R.id.rank_content_container), withChild(withText(name))))
                ))))

    }

    class Assert : BasicMatch() {

        fun onDisplayContent(empty: Boolean) {
            onDisplay(R.id.rank_parent_container)

            onDisplayToolbar()

            if (empty) {
                onDisplay(R.id.info_title_text, R.string.info_rank_title)
                onDisplay(R.id.info_details_text, R.string.info_rank_details)
                notDisplay(R.id.rank_recycler)
            } else {
                notDisplay(R.id.info_title_text, R.string.info_rank_title)
                notDisplay(R.id.info_details_text, R.string.info_notes_details)
                onDisplay(R.id.rank_recycler)
            }
        }

        private fun onDisplayToolbar() {
            onDisplay(R.id.toolbar_rank_container)

            onDisplay(R.id.toolbar_rank_cancel_button)
            onDisplay(R.id.toolbar_rank_enter)
            onDisplay(R.id.toolbar_rank_add_button)
        }

    }

}
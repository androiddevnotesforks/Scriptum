package sgtmelon.scriptum.ui.screen.main

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.dialog.RenameDialogUi

/**
 * Class for UI control of [RankFragment]
 */
class RankScreen : ParentRecyclerScreen(R.id.rank_recycler) {

    private fun rankAction(func: RankAction.() -> Unit) = RankAction().apply { func() }

    fun assert(empty: Boolean) = Assert(empty)

    fun toolbar(func: RankToolbar.() -> Unit) = RankToolbar.invoke(func)

    fun openRenameDialog(title: String, p: Int = positionRandom,
                         func: RenameDialogUi.() -> Unit = {}) {
        onClickItem(p)
        RenameDialogUi.invoke(func, title)
    }

    fun onClickVisible(rankEntity: RankEntity) =
            rankAction { onClick(rankEntity.name, R.id.rank_visible_button) }

    fun onLongClickVisible(rankEntity: RankEntity) =
            rankAction { onLongClick(rankEntity.name, R.id.rank_visible_button) }

    fun onClickCancel(rankEntity: RankEntity) =
            rankAction { onClick(rankEntity.name, R.id.rank_cancel_button) }


    class RankAction {

        fun onClick(name: String, @IdRes childId: Int): ViewInteraction =
                onView(button(name, childId)).perform(click())

        fun onLongClick(name: String, @IdRes childId: Int): ViewInteraction =
                onView(button(name, childId)).perform(longClick())

        private fun button(name: String, @IdRes childId: Int): Matcher<View> =
                allOf(withId(childId), withParent(allOf(
                        withId(R.id.rank_click_container), withChild(allOf(
                        withId(R.id.rank_content_container), withChild(withText(name))
                )))))

    }

    class Assert(empty: Boolean) : BasicMatch() {
        init {
            onDisplay(R.id.rank_parent_container)

            if (empty) {
                onDisplay(R.id.info_title_text, R.string.info_rank_empty_title)
                onDisplay(R.id.info_details_text, R.string.info_rank_empty_details)
                notDisplay(R.id.rank_recycler)
            } else {
                notDisplay(R.id.info_title_text, R.string.info_rank_empty_title)
                notDisplay(R.id.info_details_text, R.string.info_rank_empty_details)
                onDisplay(R.id.rank_recycler)
            }
        }
    }

    companion object {
        operator fun invoke(func: RankScreen.() -> Unit, empty: Boolean) = RankScreen().apply {
            assert(empty)
            toolbar { assert() }
            func()
        }
    }

}
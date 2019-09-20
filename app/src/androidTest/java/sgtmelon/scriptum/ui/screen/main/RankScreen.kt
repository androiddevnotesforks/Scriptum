package sgtmelon.scriptum.ui.screen.main

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.basic.click
import sgtmelon.scriptum.ui.basic.isDisplayed
import sgtmelon.scriptum.ui.basic.longClick
import sgtmelon.scriptum.ui.dialog.RenameDialogUi

/**
 * Class for UI control of [RankFragment]
 */
class RankScreen : ParentRecyclerScreen(R.id.rank_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.rank_parent_container)

    private val infoTitleText = getViewById(R.id.info_title_text).withText(R.string.info_rank_empty_title)
    private val infoDetailsText = getViewById(R.id.info_details_text).withText(R.string.info_rank_empty_details)

    private fun getVisibleButton(name: String) = getButton(name, R.id.rank_visible_button)

    private fun getCancelButton(name: String) = getButton(name, R.id.rank_cancel_button)

    // TODO replace with recyclerAction
    private fun getButton(name: String, @IdRes childId: Int): Matcher<View> =
            allOf(withId(childId), withParent(allOf(
                    withId(R.id.rank_click_container), withChild(allOf(
                    withId(R.id.rank_content_container), withChild(withText(name))
            )))))

    //endregion


    fun toolbar(func: RankToolbar.() -> Unit) = RankToolbar.invoke(func)

    fun openRenameDialog(title: String, p: Int = positionRandom,
                         func: RenameDialogUi.() -> Unit = {}) {
        onClickItem(p)
        RenameDialogUi.invoke(func, title)
    }

    fun onClickVisible(rankEntity: RankEntity) {
        getVisibleButton(rankEntity.name).click()
    }

    fun onLongClickVisible(rankEntity: RankEntity) {
        getVisibleButton(rankEntity.name).longClick()
    }

    fun onClickCancel(rankEntity: RankEntity) {
        getCancelButton(rankEntity.name).click()
    }


    fun assert(empty: Boolean) {
        toolbar { assert() }

        parentContainer.isDisplayed()

        infoTitleText.isDisplayed(empty)
        infoDetailsText.isDisplayed(empty)
        recyclerView.isDisplayed(!empty)
    }

    companion object {
        operator fun invoke(func: RankScreen.() -> Unit, empty: Boolean) =
                RankScreen().apply { assert(empty) }.apply(func)
    }

}
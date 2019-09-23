package sgtmelon.scriptum.ui.screen.main

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.longClick
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.ui.ParentRecyclerItem
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.RenameDialogUi

/**
 * Class for UI control of [RankFragment]
 */
class RankScreen : ParentRecyclerScreen(R.id.rank_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.rank_parent_container)

    private val infoTitleText = getViewById(R.id.info_title_text).withText(R.string.info_rank_empty_title)
    private val infoDetailsText = getViewById(R.id.info_details_text).withText(R.string.info_rank_empty_details)

    private fun getItem(rankEntity: RankEntity) = Item(recyclerView, rankEntity)

    fun toolbar(func: RankToolbar.() -> Unit) = RankToolbar.invoke(func)

    //endregion

    fun openRenameDialog(title: String, p: Int = random, func: RenameDialogUi.() -> Unit = {}) {
        onClickItem(p)
        RenameDialogUi.invoke(func, title)
    }

    fun onClickVisible(rankEntity: RankEntity) {
        getItem(rankEntity).visibleButton.click()
    }

    fun onLongClickVisible(rankEntity: RankEntity) {
        getItem(rankEntity).visibleButton.longClick()
    }

    fun onClickCancel(rankEntity: RankEntity) {
        getItem(rankEntity).cancelButton.click()
    }


    fun onAssertItem(rankEntity: RankEntity) {
        getItem(rankEntity).assert()
    }

    fun assert(empty: Boolean) {
        toolbar { assert() }

        parentContainer.isDisplayed()

        infoTitleText.isDisplayed(empty)
        infoDetailsText.isDisplayed(empty)
        recyclerView.isDisplayed(!empty)
    }

    private inner class Item(list: Matcher<View>, private val rankEntity: RankEntity) :
            ParentRecyclerItem(list, hasDescendant(
                    getViewById(R.id.rank_name_text).withText(rankEntity.name)
            )) {

        val visibleButton by lazy { getChild(getViewById(R.id.rank_visible_button)) }
        val cancelButton by lazy { getChild(getViewById(R.id.rank_cancel_button)) }

        val nameText by lazy { getChild(getViewById(R.id.rank_name_text)) }
        val countText by lazy { getChild(getViewById(R.id.rank_text_count_text)) }

        fun assert() {
            visibleButton.isDisplayed()
            cancelButton.isDisplayed()

            nameText.isDisplayed()
            countText.isDisplayed()
        }

    }

    companion object {
        operator fun invoke(func: RankScreen.() -> Unit, empty: Boolean) =
                RankScreen().apply { assert(empty) }.apply(func)
    }

}
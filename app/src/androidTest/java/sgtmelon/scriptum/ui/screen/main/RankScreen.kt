package sgtmelon.scriptum.ui.screen.main

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.RankAdapter
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.InfoPage
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.ui.ParentRecyclerItem
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.RenameDialogUi
import sgtmelon.scriptum.ui.part.InfoContainer
import sgtmelon.scriptum.ui.part.toolbar.RankToolbar

/**
 * Class for UI control of [RankFragment].
 */
class RankScreen : ParentRecyclerScreen(R.id.rank_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.rank_parent_container)
    private val infoContainer = InfoContainer(InfoPage.RANK)

    private fun getItem(rankItem: RankItem): Item {
        return Item(recyclerView, hasDescendant(getView(R.id.rank_name_text, rankItem.name)))
    }

    private fun getItem(position: Int) = Item(recyclerView, position)

    fun toolbar(func: RankToolbar.() -> Unit) = RankToolbar.invoke(func)

    //endregion

    fun openRenameDialog(title: String, p: Int = random,
                         func: RenameDialogUi.() -> Unit = {}) = apply {
        getItem(p).view.click()
        RenameDialogUi.invoke(func, title)
    }

    fun onClickVisible(rankItem: RankItem) = apply {
        waitAfter(ANIM_TIME) { getItem(rankItem).visibleButton.click() }
    }

    fun onClickVisible(p: Int = random) = apply {
        waitAfter(ANIM_TIME) { getItem(p).visibleButton.click() }
    }

    fun onLongClickVisible(rankItem: RankItem) = apply {
        waitAfter(ANIM_TIME) { getItem(rankItem).visibleButton.longClick() }
    }

    fun onLongClickVisible(p: Int = random) = apply {
        waitAfter(ANIM_TIME) { getItem(p).visibleButton.longClick() }
    }

    fun onClickCancel(p: Int = random) = apply {
        waitAfter(ANIM_TIME) { getItem(p).cancelButton.click() }
    }


    fun onAssertItem(rankItem: RankItem) {
        getItem(rankItem).assert(rankItem)
    }

    fun assert(empty: Boolean) {
        toolbar { assert() }

        parentContainer.isDisplayed()

        infoContainer.assert(empty)
        recyclerView.isDisplayed(!empty)
    }

    /**
     * Class for UI control of [RankAdapter].
     */
    private class Item private constructor(
            listMatcher: Matcher<View>,
            itemMatcher: Matcher<View>?,
            position: Int?
    ) : ParentRecyclerItem<RankItem>(listMatcher, itemMatcher, position) {

        constructor(listMatcher: Matcher<View>, position: Int) :
                this(listMatcher, null, position)

        constructor(listMatcher: Matcher<View>, itemMatcher: Matcher<View>) :
                this(listMatcher, itemMatcher, null)

        val visibleButton by lazy { getChild(getViewById(R.id.rank_visible_button)) }
        val cancelButton by lazy { getChild(getViewById(R.id.rank_cancel_button)) }

        private val nameText by lazy { getChild(getViewById(R.id.rank_name_text)) }
        private val countText by lazy { getChild(getViewById(R.id.rank_text_count_text)) }

        override fun assert(model: RankItem) {
            val isVisible = model.isVisible
            visibleButton.isDisplayed().withDrawableAttr(
                    if (isVisible) R.drawable.ic_visible_exit else R.drawable.ic_visible_enter,
                    if (isVisible) R.attr.clAccent else R.attr.clContent
            )

            cancelButton.isDisplayed().withDrawableAttr(
                    R.drawable.ic_cancel_enter, R.attr.clContent
            )

            nameText.isDisplayed().haveText(model.name)
            countText.isDisplayed().haveText(string = "${context.getString(R.string.list_item_rank_count)} ${model.noteId.size}")
        }

    }

    companion object {
        private const val ANIM_TIME = 300L

        operator fun invoke(func: RankScreen.() -> Unit, empty: Boolean) =
                RankScreen().apply { assert(empty) }.apply(func)
    }

}
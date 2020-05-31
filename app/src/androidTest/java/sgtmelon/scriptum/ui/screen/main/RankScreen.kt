package sgtmelon.scriptum.ui.screen.main

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import org.hamcrest.Matcher
import org.junit.Assert.assertTrue
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.SimpleInfoPage
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.presentation.adapter.RankAdapter
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.ui.ParentRecyclerItem
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.RenameDialogUi
import sgtmelon.scriptum.ui.part.info.SimpleInfoContainer
import sgtmelon.scriptum.ui.part.panel.SnackbarPanel
import sgtmelon.scriptum.ui.part.toolbar.RankToolbar

/**
 * Class for UI control of [RankFragment].
 */
class RankScreen : ParentRecyclerScreen(R.id.rank_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.rank_parent_container)
    private val infoContainer = SimpleInfoContainer(SimpleInfoPage.RANK)

    fun getSnackbar(func: SnackbarPanel.() -> Unit = {}): SnackbarPanel {
        val message = R.string.snackbar_message_rank
        val action = R.string.snackbar_action_cancel

        return SnackbarPanel(message, action, func)
    }

    private fun getItem(rankItem: RankItem): Item {
        return Item(recyclerView, hasDescendant(getView(R.id.rank_name_text, rankItem.name)))
    }

    private fun getItem(position: Int) = Item(recyclerView, position)

    fun toolbar(func: RankToolbar.() -> Unit) = RankToolbar(func)

    //endregion

    fun openRenameDialog(title: String, p: Int? = random,
                         func: RenameDialogUi.() -> Unit = {}) = apply {
        if (p == null) return@apply

        getItem(p).view.click()
        RenameDialogUi(func, title)
    }

    fun onClickVisible(rankItem: RankItem) = apply {
        waitAfter(ANIM_TIME) { getItem(rankItem).visibleButton.click() }
    }

    fun onClickVisible(p: Int? = random) = apply {
        if (p == null) return@apply

        waitAfter(ANIM_TIME) { getItem(p).visibleButton.click() }
    }

    fun onLongClickVisible(rankItem: RankItem) = apply {
        waitAfter(ANIM_TIME) { getItem(rankItem).visibleButton.longClick() }
    }

    fun onLongClickVisible(p: Int? = random) = apply {
        if (p == null) return@apply

        waitAfter(ANIM_TIME) { getItem(p).visibleButton.longClick() }
    }

    fun onClickCancel(p: Int? = random, wait: Boolean = false) = apply {
        if (p == null) return@apply

        waitAfter(time = if (wait) SNACK_BAR_TIME else 0) {
            getItem(p).cancelButton.click()
            getSnackbar { assert() }
        }
    }


    // TODO add alternative fast assertion by position (use openRenameDialog)
    fun onAssertItem(rankItem: RankItem) {
        getItem(rankItem).assert(rankItem)
    }

    fun assert(empty: Boolean) = apply {
        toolbar { assert() }

        parentContainer.isDisplayed()

        infoContainer.assert(empty)
        recyclerView.isDisplayed(!empty)
    }

    fun assertSnackbarDismiss() {
        assertTrue(try {
            getSnackbar().assert()
            false
        } catch (e: Throwable) {
            true
        })
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

        private val parentCard by lazy { getChild(getViewById(R.id.rank_parent_card)) }

        val visibleButton by lazy { getChild(getViewById(R.id.rank_visible_button)) }
        val cancelButton by lazy { getChild(getViewById(R.id.rank_cancel_button)) }

        private val nameText by lazy { getChild(getViewById(R.id.rank_name_text)) }
        private val countText by lazy { getChild(getViewById(R.id.rank_text_count_text)) }

        private val notificationImage by lazy { getChild(getViewById(R.id.rank_notification_image)) }
        private val bindImage by lazy { getChild(getViewById(R.id.rank_bind_image)) }

        override fun assert(item: RankItem) {
            parentCard.isDisplayed().withCardBackground(R.attr.clBackgroundView)

            val visible = item.isVisible
            val drawable = if (visible) R.drawable.ic_visible_enter else R.drawable.ic_visible_exit
            val tint = if (visible) R.attr.clAccent else R.attr.clContent
            val visibleDescription = if (visible) {
                context.getString(R.string.description_item_rank_hide).plus(other = " ").plus(item.name)
            } else {
                context.getString(R.string.description_item_rank_show).plus(other = " ").plus(item.name)
            }

            visibleButton.isDisplayed()
                    .withDrawableAttr(drawable, tint)
                    .withContentDescription(visibleDescription)

            val cancelDescription = context.getString(R.string.description_item_rank_cancel).plus(other = " ").plus(item.name)
            cancelButton.isDisplayed()
                    .withDrawableAttr(R.drawable.ic_cancel_enter, R.attr.clContent)
                    .withContentDescription(cancelDescription)

            nameText.isDisplayed().withText(item.name, R.attr.clContent, R.dimen.text_16sp)

            val text = "${context.getString(R.string.list_item_rank_count)} ${item.noteId.size}"
            countText.isDisplayed().withText(text, R.attr.clContentSecond, R.dimen.text_14sp)

            notificationImage.isDisplayed(item.hasNotification) {
                withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
            }.withDrawableAttr(R.drawable.ic_notifications, R.attr.clContent)

            bindImage.isDisplayed(item.hasBind) {
                withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
            }.withDrawableAttr(R.drawable.ic_bind_text, R.attr.clContent)
        }

    }

    companion object {
        private const val ANIM_TIME = 300L

        operator fun invoke(func: RankScreen.() -> Unit, empty: Boolean): RankScreen {
            return RankScreen().assert(empty).apply(func)
        }
    }

}
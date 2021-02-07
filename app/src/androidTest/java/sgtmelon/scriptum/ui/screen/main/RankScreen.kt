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
import sgtmelon.scriptum.presentation.adapter.holder.RankHolder
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.ui.ParentRecyclerItem
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.RenameDialogUi
import sgtmelon.scriptum.ui.part.info.SimpleInfoContainer
import sgtmelon.scriptum.ui.part.panel.SnackbarPanel
import sgtmelon.scriptum.ui.part.toolbar.RankToolbar
import kotlin.math.min

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

    private fun getItem(item: RankItem): Item {
        return Item(recyclerView, hasDescendant(getView(R.id.rank_name_text, item.name)))
    }

    private fun getItem(position: Int) = Item(recyclerView, position)

    fun toolbar(func: RankToolbar.() -> Unit) = RankToolbar(func)

    //endregion

    fun openRenameDialog(
        title: String, p: Int? = random,
        func: RenameDialogUi.() -> Unit = {}
    ) = apply {
        if (p == null) return@apply

        getItem(p).view.click()
        RenameDialogUi(func, title)
    }

    fun onClickVisible(item: RankItem) = apply {
        waitAfter(ANIM_TIME) { getItem(item).visibleButton.click() }
    }

    fun onClickVisible(p: Int? = random) = apply {
        if (p == null) return@apply

        waitAfter(ANIM_TIME) { getItem(p).visibleButton.click() }
    }

    fun onLongClickVisible(item: RankItem) = apply {
        waitAfter(ANIM_TIME) { getItem(item).visibleButton.longClick() }
    }

    fun onLongClickVisible(p: Int? = random) = apply {
        if (p == null) return@apply

        waitAfter(ANIM_TIME) { getItem(p).visibleButton.longClick() }
    }

    fun onClickCancel(p: Int? = random, isWait: Boolean = false) = apply {
        if (p == null) return@apply

        waitAfter(time = if (isWait) SNACK_BAR_TIME else 0) {
            getItem(p).cancelButton.click()
            getSnackbar { assert() }
        }
    }


    // TODO add alternative fast assertion by position (use openRenameDialog)
    fun onAssertItem(item: RankItem) = getItem(item).assert(item)

    fun assert(isEmpty: Boolean) = apply {
        toolbar { assert() }

        parentContainer.isDisplayed()

        infoContainer.assert(isEmpty)
        recyclerView.isDisplayed(!isEmpty)
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

        private val imageContainer by lazy { getChild(getViewById(R.id.rank_image_container)) }
        private val notificationText by lazy { getChild(getViewById(R.id.rank_notification_text)) }
        private val notificationImage by lazy { getChild(getViewById(R.id.rank_notification_image)) }
        private val bindText by lazy { getChild(getViewById(R.id.rank_bind_text)) }
        private val bindImage by lazy { getChild(getViewById(R.id.rank_bind_image)) }

        override fun assert(item: RankItem) {
            parentCard.isDisplayed().withCardBackground(R.attr.clBackgroundView)

            val visible = item.isVisible
            val drawable = if (visible) R.drawable.ic_visible_enter else R.drawable.ic_visible_exit
            val tint = if (visible) R.attr.clAccent else R.attr.clContent
            val visibleDescription = if (visible) {
                context.getString(R.string.description_item_rank_hide, item.name)
            } else {
                context.getString(R.string.description_item_rank_show, item.name)
            }
            visibleButton.isDisplayed()
                .withDrawableAttr(drawable, tint)
                .withContentDescription(visibleDescription)

            nameText.isDisplayed().withText(item.name, R.attr.clContent, R.dimen.text_16sp)

            assertIndicators(item)

            val cancelDescription = context.getString(R.string.description_item_rank_cancel, item.name)
            cancelButton.isDisplayed()
                .withDrawableAttr(R.drawable.ic_cancel_enter, R.attr.clContent)
                .withContentDescription(cancelDescription)
        }

        private fun assertIndicators(item: RankItem) {
            val isNotificationVisible = RankHolder.isMaxTest || item.notificationCount != 0
            val isBindVisible = RankHolder.isMaxTest || item.bindCount != 0

            imageContainer.isDisplayed(isVisible = isNotificationVisible || isBindVisible)

            var indicator = if (RankHolder.isMaxTest) {
                RankHolder.INDICATOR_MAX_COUNT.toString()
            } else {
                min(item.notificationCount, RankHolder.INDICATOR_MAX_COUNT).toString()
            }

            notificationText.isDisplayed(isNotificationVisible)
                .withText(indicator, R.attr.clCardIndicator, R.dimen.text_14sp)
            notificationImage.isDisplayed(isNotificationVisible) {
                withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
            }.withDrawableAttr(R.drawable.ic_notifications, R.attr.clCardIndicator)

            indicator = if (RankHolder.isMaxTest) {
                RankHolder.INDICATOR_MAX_COUNT.toString()
            } else {
                min(item.bindCount, RankHolder.INDICATOR_MAX_COUNT).toString()
            }

            bindText.isDisplayed(isBindVisible)
                .withText(indicator, R.attr.clCardIndicator, R.dimen.text_14sp)
            bindImage.isDisplayed(isBindVisible) {
                withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
            }.withDrawableAttr(R.drawable.ic_bind_text, R.attr.clCardIndicator)

            val text = context.getString(R.string.list_item_rank_count, item.noteId.size)
            countText.isDisplayed().withText(text, R.attr.clContentSecond, R.dimen.text_14sp)
        }
    }

    companion object {
        private const val ANIM_TIME = 300L

        operator fun invoke(func: RankScreen.() -> Unit, isEmpty: Boolean): RankScreen {
            return RankScreen().assert(isEmpty).apply(func)
        }
    }
}
package sgtmelon.scriptum.source.ui.screen.item

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.infrastructure.adapter.RankAdapter
import sgtmelon.scriptum.infrastructure.utils.extensions.getIndicatorText
import sgtmelon.scriptum.infrastructure.utils.extensions.maxIndicatorTest
import sgtmelon.scriptum.source.ui.parts.recycler.RecyclerItemPart
import sgtmelon.scriptum.source.ui.screen.dialogs.RenameDialogUi
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withCard
import sgtmelon.test.cappuccino.utils.withContentDescription
import sgtmelon.test.cappuccino.utils.withDrawableAttr
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withText

/**
 * Class for UI control of [RankAdapter].
 */
class RankItemUi(
    listMatcher: Matcher<View>,
    position: Int
) : RecyclerItemPart<RankItem>(listMatcher, position) {

    private val parentCard by lazy { getChild(getView(R.id.parent_card)) }
    private val clickContainer by lazy { getChild(getView(R.id.click_container)) }

    private val visibleButton by lazy { getChild(getView(R.id.visible_button)) }
    private val cancelButton by lazy { getChild(getView(R.id.cancel_button)) }
    private val nameText by lazy { getChild(getView(R.id.name_text)) }
    private val countText by lazy { getChild(getView(R.id.count_text)) }

    private val imageContainer by lazy { getChild(getView(R.id.image_container)) }
    private val notificationText by lazy { getChild(getView(R.id.notification_text)) }
    private val notificationImage by lazy { getChild(getView(R.id.notification_image)) }
    private val bindText by lazy { getChild(getView(R.id.bind_text)) }
    private val bindImage by lazy { getChild(getView(R.id.bind_image)) }

    fun open(title: String, func: RenameDialogUi.() -> Unit) {
        clickContainer.click()
        RenameDialogUi(func, title)
    }

    fun visible() {
        visibleButton.click()
    }

    fun cancel() {
        cancelButton.click()
    }

    override fun assert(item: RankItem) {
        parentCard.isDisplayed().withCard(
            R.attr.clBackgroundView, R.dimen.item_card_radius, R.dimen.item_card_elevation
        )

        val isVisible = item.isVisible
        val drawable = if (isVisible) {
            sgtmelon.iconanim.R.drawable.ic_visible_enter
        } else {
            sgtmelon.iconanim.R.drawable.ic_visible_exit
        }
        val tint = if (isVisible) R.attr.clAccent else R.attr.clContent
        val visibleDescription = if (isVisible) {
            context.getString(R.string.desc_rank_hide, item.name)
        } else {
            context.getString(R.string.desc_rank_show, item.name)
        }
        visibleButton.isDisplayed()
            .withDrawableAttr(drawable, tint)
            .withContentDescription(visibleDescription)

        nameText.isDisplayed().withText(item.name, R.attr.clContent, R.dimen.text_16sp)

        assertIndicators(item)

        val cancelDescription = context.getString(R.string.desc_rank_cancel, item.name)
        cancelButton.isDisplayed()
            .withDrawableAttr(sgtmelon.iconanim.R.drawable.ic_cancel_enter, R.attr.clContent)
            .withContentDescription(cancelDescription)
    }

    private fun assertIndicators(item: RankItem) = with(item) {
        val isNotificationVisible = maxIndicatorTest || notificationCount != 0
        val isBindVisible = maxIndicatorTest || bindCount != 0

        imageContainer.isDisplayed(value = isNotificationVisible || isBindVisible)

        notificationText.isDisplayed(isNotificationVisible)
            .withText(notificationCount.getIndicatorText(), R.attr.clIndicator, R.dimen.text_14sp)
        notificationImage.isDisplayed(isNotificationVisible) {
            withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
        }.withDrawableAttr(R.drawable.ic_notifications, R.attr.clIndicator)

        bindText.isDisplayed(isBindVisible)
            .withText(bindCount.getIndicatorText(), R.attr.clIndicator, R.dimen.text_14sp)
        bindImage.isDisplayed(isBindVisible) {
            withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
        }.withDrawableAttr(R.drawable.ic_bind_text, R.attr.clIndicator)

        val text = context.getString(R.string.list_rank_count, noteId.size)
        countText.isDisplayed().withText(text, R.attr.clContentSecond, R.dimen.text_14sp)
    }
}
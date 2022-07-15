package sgtmelon.scriptum.ui.item

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.presentation.adapter.RankAdapter
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.RankHolder
import sgtmelon.scriptum.ui.ParentRecyclerItem
import kotlin.math.min

/**
 * Class for UI control of [RankAdapter].
 */
class RankItemUi(
    listMatcher: Matcher<View>,
    position: Int
) : ParentRecyclerItem<RankItem>(listMatcher, position) {

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
        parentCard.isDisplayed().withCardBackground(
            R.attr.clBackgroundView,
            R.dimen.item_card_radius,
            R.dimen.item_card_elevation
        )

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
            .withText(indicator, R.attr.clIndicator, R.dimen.text_14sp)
        notificationImage.isDisplayed(isNotificationVisible) {
            withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
        }.withDrawableAttr(R.drawable.ic_notifications, R.attr.clIndicator)

        indicator = if (RankHolder.isMaxTest) {
            RankHolder.INDICATOR_MAX_COUNT.toString()
        } else {
            min(item.bindCount, RankHolder.INDICATOR_MAX_COUNT).toString()
        }

        bindText.isDisplayed(isBindVisible)
            .withText(indicator, R.attr.clIndicator, R.dimen.text_14sp)
        bindImage.isDisplayed(isBindVisible) {
            withSize(R.dimen.icon_16dp, R.dimen.icon_16dp)
        }.withDrawableAttr(R.drawable.ic_bind_text, R.attr.clIndicator)

        val text = context.getString(R.string.list_item_rank_count, item.noteId.size)
        countText.isDisplayed().withText(text, R.attr.clContentSecond, R.dimen.text_14sp)
    }
}
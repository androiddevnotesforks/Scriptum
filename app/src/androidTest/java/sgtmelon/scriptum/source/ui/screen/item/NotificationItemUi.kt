package sgtmelon.scriptum.source.ui.screen.item

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.extensions.formatFuture
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.adapter.NotificationAdapter
import sgtmelon.scriptum.source.ui.basic.withColorIndicator
import sgtmelon.scriptum.source.ui.feature.OpenNote
import sgtmelon.scriptum.source.ui.parts.recycler.RecyclerItemPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withCard
import sgtmelon.test.cappuccino.utils.withContentDescription
import sgtmelon.test.cappuccino.utils.withDrawableAttr
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withText

/**
 * Class for UI control of [NotificationAdapter].
 */
class NotificationItemUi(
    listMatcher: Matcher<View>,
    p: Int
) : RecyclerItemPart<NoteItem>(listMatcher, p),
    OpenNote.Callback {

    private val parentCard by lazy { getChild(getView(R.id.parent_card)) }
    private val clickContainer by lazy { getChild(getView(R.id.click_container)) }
    private val nameText by lazy { getChild(getView(R.id.name_text)) }
    private val dateText by lazy { getChild(getView(R.id.date_text)) }
    private val colorView by lazy { getChild(getView(R.id.color_view)) }
    private val cancelButton by lazy { getChild(getView(R.id.cancel_button)) }

    override fun openClick(item: NoteItem) {
        clickContainer.click()
    }

    fun cancel() {
        cancelButton.click()
    }

    override fun assert(item: NoteItem) {
        parentCard.isDisplayed().withCard(
            R.attr.clBackgroundView,
            R.dimen.item_card_radius,
            R.dimen.item_card_elevation
        )

        val name = item.name.ifEmpty { context.getString(R.string.empty_note_name) }
        val date = item.alarm.date.toCalendar().formatFuture(context)
        val cancelDesc = context.getString(R.string.desc_notification_cancel, name, item.alarm.date)

        colorView.isDisplayed()
            .withSize(widthId = R.dimen.layout_8dp)
            .withColorIndicator(R.drawable.ic_color_indicator, theme, item.color)

        nameText.isDisplayed().withText(name, R.attr.clContent, R.dimen.text_16sp)
        dateText.isDisplayed().withText(date, R.attr.clContentSecond, R.dimen.text_14sp)

        cancelButton.isDisplayed()
            .withDrawableAttr(sgtmelon.iconanim.R.drawable.ic_cancel_enter, R.attr.clContent)
            .withContentDescription(cancelDesc)
    }
}
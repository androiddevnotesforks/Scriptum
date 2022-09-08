package sgtmelon.scriptum.cleanup.ui.item

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.common.utils.formatFuture
import sgtmelon.common.utils.toCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.isDisplayed
import sgtmelon.scriptum.cleanup.basic.extension.withCardBackground
import sgtmelon.scriptum.cleanup.basic.extension.withColorIndicator
import sgtmelon.scriptum.cleanup.basic.extension.withContentDescription
import sgtmelon.scriptum.cleanup.basic.extension.withDrawableAttr
import sgtmelon.scriptum.cleanup.basic.extension.withSize
import sgtmelon.scriptum.cleanup.basic.extension.withText
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.adapter.NotificationAdapter
import sgtmelon.scriptum.cleanup.ui.ParentRecyclerItem

/**
 * Class for UI control of [NotificationAdapter].
 */
class NotificationItemUi(
    listMatcher: Matcher<View>,
    p: Int
) : ParentRecyclerItem<NoteItem>(listMatcher, p) {

    private val parentCard by lazy { getChild(getViewById(R.id.notification_parent_card)) }

    private val nameText by lazy { getChild(getViewById(R.id.notification_name_text)) }
    private val dateText by lazy { getChild(getViewById(R.id.notification_date_text)) }

    private val colorView by lazy { getChild(getViewById(R.id.notification_color_view)) }

    val cancelButton by lazy { getChild(getViewById(R.id.notification_cancel_button)) }

    override fun assert(item: NoteItem) {
        parentCard.isDisplayed().withCardBackground(
            R.attr.clBackgroundView,
            R.dimen.item_card_radius,
            R.dimen.item_card_elevation
        )

        val name = item.name.ifEmpty { context.getString(R.string.hint_text_name) }

        nameText.isDisplayed().withText(name, R.attr.clContent, R.dimen.text_16sp)

        val date = item.alarmDate.toCalendar().formatFuture(context)
        dateText.isDisplayed().withText(date, R.attr.clContentSecond, R.dimen.text_14sp)

        colorView.isDisplayed()
            .withSize(widthId = R.dimen.layout_8dp)
            .withColorIndicator(R.drawable.ic_color_indicator, appTheme, item.color)

        val description = context.getString(R.string.description_item_notification_cancel)
            .plus(other = " ")
            .plus(name)
            .plus(context.getString(R.string.description_item_notification_cancel_at))
            .plus(other = " ")
            .plus(item.alarmDate)

        cancelButton.isDisplayed()
            .withDrawableAttr(sgtmelon.iconanim.R.drawable.ic_cancel_enter, R.attr.clContent)
            .withContentDescription(description)
    }

}
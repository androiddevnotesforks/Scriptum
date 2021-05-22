package sgtmelon.scriptum.ui.item

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.extension.formatFuture
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.adapter.NotificationAdapter
import sgtmelon.scriptum.ui.ParentRecyclerItem

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
        parentCard.isDisplayed().withCardBackground(R.attr.clBackgroundView)

        val name = if (item.name.isEmpty()) context.getString(R.string.hint_text_name) else item.name

        nameText.isDisplayed().withText(name, R.attr.clContent, R.dimen.text_16sp)

        val date = item.alarmDate.getCalendar().formatFuture(context)
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
            .withDrawableAttr(R.drawable.ic_cancel_enter, R.attr.clContent)
            .withContentDescription(description)
    }

}
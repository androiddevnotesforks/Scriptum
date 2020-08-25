package sgtmelon.scriptum.presentation.adapter.holder

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import sgtmelon.extension.formatFuture
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.extension.bindIndicatorColor
import sgtmelon.scriptum.presentation.adapter.NotificationAdapter
import sgtmelon.scriptum.presentation.listener.ItemListener

/**
 * Holder for notification, use in [NotificationAdapter]
 */
class NotificationHolder(
    itemView: View,
    private val clickListener: ItemListener.Click
) : ParentHolder(itemView) {

    private val clickView: View = itemView.findViewById(R.id.notification_click_container)
    private val colorView: View = itemView.findViewById(R.id.notification_color_view)
    private val nameText: TextView = itemView.findViewById(R.id.notification_name_text)
    private val dateText: TextView = itemView.findViewById(R.id.notification_date_text)
    private val cancelButton: ImageButton = itemView.findViewById(R.id.notification_cancel_button)

    init {
        clickView.setOnClickListener { v -> checkNoPosition { clickListener.onItemClick(v, it) } }
        cancelButton.setOnClickListener { v ->
            checkNoPosition { clickListener.onItemClick(v, it) }
        }
    }

    fun bind(item: NotificationItem, @Theme theme: Int) {
        val context = itemView.context
        val name = if (item.note.name.isEmpty()) {
            context.getString(R.string.hint_text_name)
        } else {
            item.note.name
        }
        val date = item.alarm.date

        colorView.bindIndicatorColor(theme, item.note.color)
        nameText.text = name
        dateText.text = date.getCalendar().formatFuture(context)

        cancelButton.contentDescription = context.getString(
            R.string.description_item_notification_cancel, name, date
        )
    }
}
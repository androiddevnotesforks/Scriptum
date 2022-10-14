package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.view.View
import android.widget.ImageButton
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.extension.bindFutureTime
import sgtmelon.scriptum.cleanup.extension.bindIndicatorColor
import sgtmelon.scriptum.databinding.ItemNotificationBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NotificationClickListener

class NotificationHolder(
    private val binding: ItemNotificationBinding
) : ParentHolder(binding.root),
    UnbindCallback {

    private val clickView: View = itemView.findViewById(R.id.click_container)
    private val cancelButton: ImageButton = itemView.findViewById(R.id.cancel_button)

    fun bind(item: NotificationItem, callback: NotificationClickListener) {
        clickView.setOnClickListener { callback.onNotificationClick(item) }
        cancelButton.setOnClickListener { checkPosition { callback.onNotificationCancel(it) } }

        val name = item.note.name.ifEmpty { context.getString(R.string.empty_note_name) }
        val cancelDesc = context.getString(R.string.desc_notification_cancel, name, item.alarm.date)

        binding.colorView.bindIndicatorColor(item.note.color)
        binding.nameText.text = name
        binding.dateText.bindFutureTime(item.alarm.date)
        binding.cancelButton.contentDescription = cancelDesc
    }

    override fun unbind() {
        clickView.setOnClickListener(null)
        cancelButton.setOnClickListener(null)
    }
}
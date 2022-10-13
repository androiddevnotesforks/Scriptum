package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.view.View
import android.widget.ImageButton
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.databinding.ItemNotificationBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NotificationClickListener

class NotificationHolder(
    private val binding: ItemNotificationBinding,
) : ParentHolder(binding.root),
    UnbindCallback {

    private val clickView: View = itemView.findViewById(R.id.notification_click_container)
    private val cancelButton: ImageButton = itemView.findViewById(R.id.notification_cancel_button)

    fun bind(item: NotificationItem, callback: NotificationClickListener) {
        clickView.setOnClickListener { callback.onNotificationClick(item) }
        cancelButton.setOnClickListener { checkNoPosition { p -> callback.onNotificationCancel(p) } }

        // TODO remove databinding and use only view binding
        binding.apply { this.item = item }.executePendingBindings()
    }

    override fun unbind() {
        clickView.setOnClickListener(null)
        cancelButton.setOnClickListener(null)
    }
}
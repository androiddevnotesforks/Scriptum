package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.view.View
import android.widget.ImageButton
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.presentation.adapter.NotificationAdapter
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.databinding.ItemNotificationBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback

/**
 * Holder for notification, use in [NotificationAdapter]
 */
class NotificationHolder(
    private val binding: ItemNotificationBinding,
) : ParentHolder(binding.root),
    UnbindCallback {

    private val clickView: View = itemView.findViewById(R.id.notification_click_container)
    private val cancelButton: ImageButton = itemView.findViewById(R.id.notification_cancel_button)

    fun bind(item: NotificationItem, callback: ItemListener.Click) {
        clickView.setOnClickListener { v -> checkNoPosition { callback.onItemClick(v, it) } }
        cancelButton.setOnClickListener { v -> checkNoPosition { callback.onItemClick(v, it) } }

        binding.apply { this.item = item }.executePendingBindings()
    }

    override fun unbind() {
        clickView.setOnClickListener(null)
        cancelButton.setOnClickListener(null)
    }
}
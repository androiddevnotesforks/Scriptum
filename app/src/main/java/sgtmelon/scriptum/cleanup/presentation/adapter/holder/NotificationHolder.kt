package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.view.View
import android.widget.ImageButton
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemNotificationBinding
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.presentation.adapter.NotificationAdapter
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener

/**
 * Holder for notification, use in [NotificationAdapter]
 */
class NotificationHolder(
    private val binding: ItemNotificationBinding,
    private val clickListener: ItemListener.Click
) : ParentHolder(binding.root) {

    private val clickView: View = itemView.findViewById(R.id.notification_click_container)
    private val cancelButton: ImageButton = itemView.findViewById(R.id.notification_cancel_button)

    init {
        clickView.setOnClickListener { v -> checkNoPosition { clickListener.onItemClick(v, it) } }
        cancelButton.setOnClickListener { v ->
            checkNoPosition { clickListener.onItemClick(v, it) }
        }
    }

    fun bind(item: NotificationItem) = binding.apply { this.item = item }.executePendingBindings()

}
package sgtmelon.scriptum.adapter.holder

import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NotificationAdapter
import sgtmelon.scriptum.databinding.ItemNotificationBinding
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.checkNoPosition

/**
 * Держатель уведомления для [NotificationAdapter]
 *
 * @author SerjantArbuz
 */
class NotificationHolder(private val binding: ItemNotificationBinding,
                         private val clickListener: ItemListener.ClickListener
) : RecyclerView.ViewHolder(binding.root) {

    private val clickView: View = itemView.findViewById(R.id.notification_click_container)
    private val cancelButton: ImageButton = itemView.findViewById(R.id.notification_cancel_button)

    init {
        clickView.setOnClickListener { v ->
            checkNoPosition { clickListener.onItemClick(v, adapterPosition) }
        }
        cancelButton.setOnClickListener { v ->
            checkNoPosition { clickListener.onItemClick(v, adapterPosition) }
        }
    }

    fun bind(notificationItem: NotificationItem) =
            binding.apply { this.notificationItem = notificationItem }.executePendingBindings()

}
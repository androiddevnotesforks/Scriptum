package sgtmelon.scriptum.adapter.holder

import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NotificationAdapter
import sgtmelon.scriptum.databinding.ItemNotificationBinding
import sgtmelon.scriptum.extension.checkNoPosition
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.NotificationItem
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Держатель уведомления для [NotificationAdapter]
 *
 * @author SerjantArbuz
 */
class NotificationHolder(private val binding: ItemNotificationBinding,
                         private val clickListener: ItemListener.Click
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

    fun bind(@Theme theme: Int, notificationItem: NotificationItem) = binding.apply {
        this.currentTheme = theme
        this.notificationItem = notificationItem
    }.executePendingBindings()

}
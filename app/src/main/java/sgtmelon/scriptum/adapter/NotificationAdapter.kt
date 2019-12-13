package sgtmelon.scriptum.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.diff.NotificationDiff
import sgtmelon.scriptum.adapter.holder.NotificationHolder
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity

/**
 * Adapter which displays list of notifications for [NotificationActivity]
 */
class NotificationAdapter(private val clickListener: ItemListener.Click) :
        ParentAdapter<NotificationItem, NotificationHolder>() {

    @Theme var theme: Int = Theme.UNDEFINED

    private val diff = NotificationDiff()

    override fun setList(list: List<NotificationItem>) {
        this.list.clearAndAdd(ArrayList(list.map { it.copy() }))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(parent.inflateBinding(R.layout.item_notification), clickListener)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.bind(theme, list[position])
    }

    override fun notifyList(list: List<NotificationItem>) {
        diff.setList(this.list, list)

        val diffResult = DiffUtil.calculateDiff(diff)
        setList(list)
        diffResult.dispatchUpdatesTo(this)
    }

}
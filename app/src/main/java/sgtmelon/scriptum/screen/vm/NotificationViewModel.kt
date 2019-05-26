package sgtmelon.scriptum.screen.vm

import android.app.Application
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.screen.callback.NotificationCallback
import sgtmelon.scriptum.screen.view.NotificationActivity

/**
 * ViewModel для [NotificationActivity]
 *
 * @author SerjantArbuz
 */
class NotificationViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: NotificationCallback

    fun onUpdateData() = callback.notifyDataSetChanged(ArrayList<NotificationItem>().apply {
        repeat(times = 3) {
            add(NotificationItem(date = "2019-05-26 15:12:00").apply { name = "Future today" })
            add(NotificationItem(date = "2019-05-27 19:00:00").apply { name = "Future tomorrow" })
            add(NotificationItem(date = "2019-06-28 07:00:00").apply { name = "This year" })
            add(NotificationItem(date = "2020-06-29 19:00:00").apply { name = "Very very future" })
        }
    })

    fun onClickNote(p: Int) = callback.testToast("Open note at position = $p")

    fun onClickCancel(p: Int) = callback.testToast("Cancel notification at position = $p")

}
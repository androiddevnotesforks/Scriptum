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
        repeat(times = 2) {
            add(NotificationItem(date = "1990-05-22 12:11:51").apply { name = "Найти" })
            add(NotificationItem(date = "2019-05-24 12:11:51").apply { name = "Идеи" })
            add(NotificationItem(date = "2019-05-22 12:11:51").apply { name = "Сделать" })
            add(NotificationItem(date = "2019-10-04 03:55:21").apply { name = "Купить" })
            add(NotificationItem(date = "2020-07-22 13:50:20").apply { name = "Посмотреть" })
            add(NotificationItem(date = "2020-01-07 09:05:01").apply { name = "Срочно" })
        }
    })

    fun onClickNote(p: Int) = callback.testToast("Open note at position = $p")

    fun onClickCancel(p: Int) = callback.testToast("Cancel notification at position = $p")

}
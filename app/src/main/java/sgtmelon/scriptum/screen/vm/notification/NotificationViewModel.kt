package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.callback.notification.NotificationCallback
import sgtmelon.scriptum.screen.view.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.view.notification.AlarmActivity.Companion.getAlarmIntent
import sgtmelon.scriptum.screen.view.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * ViewModel для [NotificationActivity]
 *
 * @author SerjantArbuz
 */
class NotificationViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: NotificationCallback

    private val nameList = arrayListOf("Задания на сегодня", "Купить домой", "Идеи для проекта", "Важные дела")
    private val dateList = arrayListOf("2019-05-26 15:12:00", "2019-05-27 19:00:00", "2019-06-28 07:00:00", "2020-06-29 19:00:00")

    private val noteModelList: MutableList<NoteModel> = ArrayList<NoteModel>().apply {
        for (i in 0 until ColorData.size) {
            add(NoteModel(
                    noteItem = NoteItem(id = 1, type = NoteType.TEXT, name = nameList.random(), color = i),
                    notificationItem = NotificationItem(date = dateList.random())
            ))
        }
    }

    fun onSetup() = callback.apply {
        setupToolbar()
        setupRecycler(preference.getTheme())
    }

    /**
     * TODO сделать опитимизацию запросов для будущего репозитория (получать для noteItem только id, type, name, color)
     */
    fun onUpdateData() {
        callback.notifyDataSetChanged(noteModelList)
        callback.bind()
    }

    /**
     * TODO открытие заметки
     */
    fun onClickNote(p: Int) = callback.startActivity(with(noteModelList[p].noteItem) {
        if (true) context.getAlarmIntent(id, color) else context.getNoteIntent(type, id)
    })

    fun onClickCancel(p: Int) =
            callback.notifyItemRemoved(p, noteModelList.apply { removeAt(p) })

}
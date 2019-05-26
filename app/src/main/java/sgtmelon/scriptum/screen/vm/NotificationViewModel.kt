package sgtmelon.scriptum.screen.vm

import android.app.Application
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.screen.callback.NotificationCallback
import sgtmelon.scriptum.screen.view.NotificationActivity
import sgtmelon.scriptum.screen.view.note.NoteActivity.Companion.getNoteIntent

/**
 * ViewModel для [NotificationActivity]
 *
 * @author SerjantArbuz
 */
class NotificationViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: NotificationCallback

    private val noteModelList: MutableList<NoteModel> =ArrayList<NoteModel>().apply {
        repeat(times = 3) {
            add(NoteModel(
                    noteItem = NoteItem(id = 12, type = NoteType.ROLL, name = "Задания на сегодня", color = ColorDef.red),
                    notificationItem = NotificationItem(date = "2019-05-26 15:12:00")
            ))
            add(NoteModel(
                    noteItem = NoteItem(id = 12, type = NoteType.ROLL, name = "Купить домой", color = ColorDef.teal),
                    notificationItem = NotificationItem(date = "2019-05-27 19:00:00")
            ))
            add(NoteModel(
                    noteItem = NoteItem(id = 12, type = NoteType.ROLL, name = "Идеи для проекта", color = ColorDef.green),
                    notificationItem = NotificationItem(date = "2019-06-28 07:00:00")
            ))
            add(NoteModel(
                    noteItem = NoteItem(id = 12, type = NoteType.ROLL, name = "Важные дела", color = ColorDef.blue),
                    notificationItem = NotificationItem(date = "2020-06-29 19:00:00")
            ))
        }
    }

    /**
     * TODO сделать опитимизацию запросов для будущего репозитория (получать для noteItem только id, type, name, color)
     */
    fun onUpdateData() = callback.notifyDataSetChanged(noteModelList)

    fun onClickNote(p: Int) =  with(noteModelList[p].noteItem) {
        callback.startNote(context.getNoteIntent(type, id))
    }

    fun onClickCancel(p: Int) =
            callback.notifyItemRemoved(p, noteModelList.apply { removeAt(p) })

}
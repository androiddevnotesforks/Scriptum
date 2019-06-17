package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import android.util.Log
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.callback.notification.NotificationCallback
import sgtmelon.scriptum.screen.view.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.view.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * ViewModel для [NotificationActivity]
 *
 * @author SerjantArbuz
 */
class NotificationViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: NotificationCallback

    private val noteModelList: MutableList<NoteModel> = ArrayList()

    fun onSetup() = callback.apply {
        setupToolbar()
        setupRecycler(iPreferenceRepo.theme)
    }

    /**
     * TODO #RELEASE сделать опитимизацию запросов для будущего репозитория (получать для noteItem только id, type, name)
     */
    fun onUpdateData() {
        noteModelList.clearAndAdd(iAlarmRepo.getList())

        noteModelList.forEach { Log.i("HERE", it.toString()) }

        callback.notifyDataSetChanged(noteModelList)
        callback.bind()
    }

    fun onClickNote(p: Int) {
        val noteItem = noteModelList[p].noteItem
        callback.startActivity(context.getNoteIntent(noteItem.type, noteItem.id))
    }

    fun onClickCancel(p: Int) {
        iAlarmRepo.delete(noteModelList[p].alarmEntity)
        callback.notifyItemRemoved(p, noteModelList.apply { removeAt(p) })
    }

}
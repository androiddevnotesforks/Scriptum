package sgtmelon.scriptum.interactor.notification

import android.content.Context
import sgtmelon.extension.clearSeconds
import sgtmelon.extension.getText
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.repository.room.AlarmRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.repository.room.NoteRepo
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmBridge
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import java.util.*

/**
 * Interactor for [AlarmViewModel]
 */
class AlarmInteractor(context: Context, private var callback: IAlarmBridge?) :
        ParentInteractor(context),
        IAlarmInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)
    private val iNoteRepo: INoteRepo = NoteRepo(context)

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override val repeat: Int get() = iPreferenceRepo.repeat

    override val volume: Int get() = iPreferenceRepo.volume

    override val volumeIncrease: Boolean get() = iPreferenceRepo.volumeIncrease

    override suspend fun getModel(id: Long): NoteItem? {
        /**
         * Delete before return noteModel for hide alarm icon.
         */
        iAlarmRepo.delete(id)


        return iNoteRepo.getItem(id, optimisation = true)
    }

    override suspend fun setupRepeat(noteItem: NoteItem, valueArray: IntArray,
                                     @Repeat repeat: Int) {
        val calendar = Calendar.getInstance().clearSeconds().apply {
            add(Calendar.MINUTE, valueArray[repeat])
        }

        checkDateExist(calendar)
        
        iAlarmRepo.insertOrUpdate(noteItem, calendar.getText())
        callback?.setAlarm(calendar, noteItem.id)
    }

    private suspend fun checkDateExist(calendar: Calendar) {
        val dateList = iAlarmRepo.getList().map { it.alarm.date }

        while (dateList.contains(calendar.getText())) {
            calendar.add(Calendar.MINUTE, 1)
        }
    }

}
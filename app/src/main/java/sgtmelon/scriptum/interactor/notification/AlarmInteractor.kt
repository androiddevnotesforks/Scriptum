package sgtmelon.scriptum.interactor.notification

import android.content.Context
import sgtmelon.extension.getDateFormat
import sgtmelon.scriptum.control.alarm.callback.AlarmCallback
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import java.util.*

/**
 * Interactor for [AlarmViewModel]
 */
class AlarmInteractor(context: Context) : ParentInteractor(context), IAlarmInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)

    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override val repeat: Int get() = iPreferenceRepo.repeat

    override val volume: Int get() = iPreferenceRepo.volume

    override val volumeIncrease: Boolean get() = iPreferenceRepo.volumeIncrease

    override fun getModel(id: Long): NoteModel? {
        /**
         * Delete before return noteModel for hide alarm icon
         */
        iAlarmRepo.delete(id)
        return iRoomRepo.getNoteModel(id)
    }

    override fun setupRepeat(noteModel: NoteModel, callback: AlarmCallback.Set?,
                             valueArray: IntArray) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, valueArray[repeat])
        }

        iAlarmRepo.insertOrUpdate(noteModel.alarmEntity.apply {
            date = getDateFormat().format(calendar.time)
        })

        callback?.setAlarm(calendar, AlarmReceiver[noteModel.noteEntity])
    }

}
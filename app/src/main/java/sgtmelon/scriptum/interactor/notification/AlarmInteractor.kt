package sgtmelon.scriptum.interactor.notification

import sgtmelon.extension.clearSeconds
import sgtmelon.extension.getText
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.IAlarmBridge
import sgtmelon.scriptum.presentation.screen.vm.impl.notification.AlarmViewModel
import java.util.*

/**
 * Interactor for [AlarmViewModel].
 */
class AlarmInteractor(
        private val preferenceRepo: IPreferenceRepo,
        private val alarmRepo: IAlarmRepo,
        private val noteRepo: INoteRepo,
        private var callback: IAlarmBridge?
) : ParentInteractor(),
        IAlarmInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = preferenceRepo.theme

    override val repeat: Int get() = preferenceRepo.repeat

    override val volume: Int get() = preferenceRepo.volume

    override val volumeIncrease: Boolean get() = preferenceRepo.volumeIncrease

    override suspend fun getModel(id: Long): NoteItem? {
        /**
         * Delete before return noteModel for hide alarm icon.
         */
        alarmRepo.delete(id)

        return noteRepo.getItem(id, optimisation = true)
    }

    /**
     * TODO test get nullPointerException
     */
    override suspend fun setupRepeat(noteItem: NoteItem, valueArray: IntArray,
                                     @Repeat repeat: Int) {
        val calendar = Calendar.getInstance().clearSeconds().apply {
            add(Calendar.MINUTE, valueArray[repeat])
        }

        checkDateExist(calendar)
        
        alarmRepo.insertOrUpdate(noteItem, calendar.getText())
        callback?.setAlarm(calendar, noteItem.id)
    }

    private suspend fun checkDateExist(calendar: Calendar) {
        val dateList = alarmRepo.getList().map { it.alarm.date }

        while (dateList.contains(calendar.getText())) {
            calendar.add(Calendar.MINUTE, 1)
        }
    }

}
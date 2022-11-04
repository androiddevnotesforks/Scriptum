package sgtmelon.scriptum.cleanup.domain.interactor.impl.system

import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.runMain
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.system.ISystemInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.presenter.system.ISystemPresenter
import sgtmelon.scriptum.cleanup.presentation.screen.system.ISystemBridge
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.test.prod.RunPrivate

/**
 * Interactor for [ISystemPresenter]
 */
class SystemInteractor(
    private val preferenceRepo: PreferencesRepo,
    private val bindRepo: BindRepo,
    private val alarmRepo: AlarmRepo,
    private val noteRepo: NoteRepo,
    @RunPrivate var callback: ISystemBridge?
) : ParentInteractor(),
    ISystemInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }

    override suspend fun tidyUpAlarm() {
        for (it in alarmRepo.getList()) {
            val id = it.note.id
            val calendar = it.alarm.date.toCalendar()

            if (calendar.isBeforeNow()) {
                runMain { callback?.cancelAlarm(id) }
                alarmRepo.delete(id)
            } else {
                runMain { callback?.setAlarm(id, calendar, showToast = false) }
            }
        }
    }

    /**
     * Update all bind notes in status bar rely on rank visibility.
     */
    override suspend fun notifyNotesBind() {
        callback?.notifyNotesBind(noteRepo.getBindNoteList(preferenceRepo.sort))
    }

    override suspend fun notifyCountBind() {
        callback?.notifyCountBind(bindRepo.getNotificationsCount())
    }

    override suspend fun unbindNote(id: Long) = bindRepo.unbindNote(id)

}
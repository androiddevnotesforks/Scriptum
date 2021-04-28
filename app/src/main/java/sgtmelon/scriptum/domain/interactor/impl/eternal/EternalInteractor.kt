package sgtmelon.scriptum.domain.interactor.impl.eternal

import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.interactor.callback.eternal.IEternalInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.extension.runMain
import sgtmelon.scriptum.presentation.service.IEternalBridge
import sgtmelon.scriptum.presentation.service.presenter.IEternalPresenter

/**
 * Interactor for [IEternalPresenter]
 */
class EternalInteractor(
    private val preferenceRepo: IPreferenceRepo,
    private val bindRepo: IBindRepo,
    private val alarmRepo: IAlarmRepo,
    private val rankRepo: IRankRepo,
    private val noteRepo: INoteRepo,
    @RunPrivate var callback: IEternalBridge?
) : ParentInteractor(),
    IEternalInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }

    override suspend fun tidyUpAlarm() {
        for (it in alarmRepo.getList()) {
            val id = it.note.id
            val calendar = it.alarm.date.getCalendar()

            if (calendar.beforeNow()) {
                runMain { callback?.cancelAlarm(id) }
                alarmRepo.delete(id)
            } else {
                runMain { callback?.setAlarm(calendar, id, showToast = false) }
            }
        }
    }

    /**
     * Update all bind notes in status bar rely on rank visibility.
     */
    override suspend fun notifyNotesBind() {
        val sort = preferenceRepo.sort
        val rankIdVisibleList = rankRepo.getIdVisibleList()
        val itemList = noteRepo.getList(sort, isBin = false, isOptimal = false, filterVisible = false)

        callback?.notifyNotesBind(itemList, rankIdVisibleList)
    }

    override suspend fun notifyInfoBind() {
        callback?.notifyInfoBind(bindRepo.getNotificationCount())
    }

    override suspend fun unbindNote(id: Long) = bindRepo.unbindNote(id)

}
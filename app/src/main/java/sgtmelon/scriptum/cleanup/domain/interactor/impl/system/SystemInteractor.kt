package sgtmelon.scriptum.cleanup.domain.interactor.impl.system

import sgtmelon.common.utils.beforeNow
import sgtmelon.common.utils.getCalendar
import sgtmelon.scriptum.infrastructure.preferences.AppPreferences
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.system.ISystemInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.common.utils.runMain
import sgtmelon.scriptum.cleanup.presentation.screen.presenter.system.ISystemPresenter
import sgtmelon.scriptum.cleanup.presentation.screen.system.ISystemBridge

/**
 * Interactor for [ISystemPresenter]
 */
class SystemInteractor(
    private val preferenceRepo: AppPreferences,
    private val bindRepo: IBindRepo,
    private val alarmRepo: IAlarmRepo,
    private val rankRepo: IRankRepo,
    private val noteRepo: INoteRepo,
    @RunPrivate var callback: ISystemBridge?
) : ParentInteractor(),
    ISystemInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }

    override suspend fun tidyUpAlarm() {
        for (it in alarmRepo.getList()) {
            val id = it.note.id
            val calendar = it.alarm.date.getCalendar()

            if (calendar.beforeNow()) {
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
        val sort = preferenceRepo.sort
        val itemList = noteRepo.getList(sort, isBin = false, isOptimal = false, filterVisible = false)
        val rankIdVisibleList = rankRepo.getIdVisibleList()

        val filterList = getFilterList(itemList, rankIdVisibleList)

        callback?.notifyNotesBind(filterList)
    }

    @RunPrivate fun getFilterList(
        itemList: List<NoteItem>,
        rankIdVisibleList: List<Long>
    ): List<NoteItem> {
        return itemList.filter { !it.isBin && it.isStatus && it.isRankVisible(rankIdVisibleList) }
    }

    override suspend fun notifyCountBind() {
        callback?.notifyCountBind(bindRepo.getNotificationCount())
    }

    override suspend fun unbindNote(id: Long) = bindRepo.unbindNote(id)

}
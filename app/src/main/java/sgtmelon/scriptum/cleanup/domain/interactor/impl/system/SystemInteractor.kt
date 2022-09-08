package sgtmelon.scriptum.cleanup.domain.interactor.impl.system

import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.common.utils.isBeforeNow
import sgtmelon.common.utils.runMain
import sgtmelon.common.utils.toCalendar
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.system.ISystemInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.presenter.system.ISystemPresenter
import sgtmelon.scriptum.cleanup.presentation.screen.system.ISystemBridge
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

/**
 * Interactor for [ISystemPresenter]
 */
class SystemInteractor(
    private val preferenceRepo: PreferencesRepo,
    private val bindRepo: BindRepo,
    private val alarmRepo: AlarmRepo,
    private val rankRepo: RankRepo,
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
        callback?.notifyCountBind(bindRepo.getNotificationsCount())
    }

    override suspend fun unbindNote(id: Long) = bindRepo.unbindNote(id)

}
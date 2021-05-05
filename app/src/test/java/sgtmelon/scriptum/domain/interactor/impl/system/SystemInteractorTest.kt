package sgtmelon.scriptum.domain.interactor.impl.system

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastMock
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.presentation.screen.system.ISystemBridge
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

/**
 * Test for [SystemInteractor]
 */
@ExperimentalCoroutinesApi
class SystemInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var bindRepo: IBindRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo

    @MockK lateinit var callback: ISystemBridge

    private val interactor by lazy {
        SystemInteractor(preferenceRepo, bindRepo, alarmRepo, rankRepo, noteRepo, callback)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(preferenceRepo, bindRepo, alarmRepo, rankRepo, noteRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }

    @Test fun tidyUpAlarm() = startCoTest {
        val size = getRandomSize()
        val list = MutableList<NotificationItem>(size) { mockk() }
        val noteList = List<NotificationItem.Note>(size) { mockk() }
        val idList = List(size) { Random.nextLong() }
        val alarmList = List<NotificationItem.Alarm>(size) { mockk() }
        val dateList = List(size) { nextString() }
        val calendarList = List<Calendar>(size) { mockk() }
        val beforeList = List(size) { Random.nextBoolean() }

        FastMock.timeExtension()

        coEvery { alarmRepo.getList() } returns list

        for ((i, item) in list.withIndex()) {
            every { item.note } returns noteList[i]
            every { noteList[i].id } returns idList[i]
            every { item.alarm } returns alarmList[i]
            every { alarmList[i].date } returns dateList[i]
            every { dateList[i].getCalendar() } returns calendarList[i]
            every { calendarList[i].beforeNow() } returns beforeList[i]
        }

        interactor.tidyUpAlarm()

        coVerifySequence {
            alarmRepo.getList()

            for ((i, item) in list.withIndex()) {
                item.note
                noteList[i].id

                item.alarm
                alarmList[i].date
                dateList[i].getCalendar()

                calendarList[i].beforeNow()
                if (beforeList[i]) {
                    callback.cancelAlarm(idList[i])
                    alarmRepo.delete(idList[i])
                } else {
                    callback.setAlarm(idList[i], calendarList[i], showToast = false)
                }
            }
        }
    }

    @Test fun notifyNotesBind() = startCoTest {
        val sort = Random.nextInt()
        val itemList = mockk<MutableList<NoteItem>>()
        val rankIdVisibleList = mockk<List<Long>>()
        val filterList = mockk<List<NoteItem>>()

        every { preferenceRepo.sort } returns sort
        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList
        coEvery {
            noteRepo.getList(sort, isBin = false, isOptimal = false, filterVisible = false)
        } returns itemList
        coEvery { spyInteractor.getFilterList(itemList, rankIdVisibleList) } returns filterList

        spyInteractor.notifyNotesBind()

        coVerifySequence {
            spyInteractor.notifyNotesBind()

            preferenceRepo.sort
            noteRepo.getList(sort, isBin = false, isOptimal = false, filterVisible = false)
            rankRepo.getIdVisibleList()
            spyInteractor.getFilterList(itemList, rankIdVisibleList)

            callback.notifyNotesBind(filterList)
        }
    }

    @Test fun getFilterList() {
        val rankIdVisibleList = mockk<List<Long>>()

        val size = getRandomSize()
        val itemList = List<NoteItem>(size) { mockk() }
        val isBinList = List(size) { Random.nextBoolean() }
        val isStatusList = List(size) { Random.nextBoolean() }
        val isRankVisibleList = List(size) { Random.nextBoolean() }

        for ((i, item) in itemList.withIndex()) {
            every { item.isBin } returns isBinList[i]
            every { item.isStatus } returns isStatusList[i]
            every { item.isRankVisible(rankIdVisibleList) } returns isRankVisibleList[i]
        }

        val resultList = ArrayList(itemList)
        resultList.removeAll { isBinList[itemList.indexOf(it)] }
        resultList.removeAll { !isStatusList[itemList.indexOf(it)] }
        resultList.removeAll { !isRankVisibleList[itemList.indexOf(it)] }

        assertEquals(resultList, interactor.getFilterList(itemList, rankIdVisibleList))
    }

    @Test fun notifyCountBind() = startCoTest {
        val count = Random.nextInt()

        coEvery { bindRepo.getNotificationCount() } returns count

        interactor.notifyCountBind()

        coVerifySequence {
            bindRepo.getNotificationCount()
            callback.notifyCountBind(count)
        }
    }

    @Test fun unbindNote() = startCoTest {
        val id = Random.nextLong()

        interactor.unbindNote(id)

        coVerifySequence {
            bindRepo.unbindNote(id)
        }
    }
}
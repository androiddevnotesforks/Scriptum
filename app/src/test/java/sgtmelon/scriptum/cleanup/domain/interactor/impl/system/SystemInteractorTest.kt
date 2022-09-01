package sgtmelon.scriptum.cleanup.domain.interactor.impl.system

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import java.util.Calendar
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.beforeNow
import sgtmelon.common.utils.getCalendar
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.getRandomSize
import sgtmelon.scriptum.cleanup.parent.ParentInteractorTest
import sgtmelon.scriptum.cleanup.presentation.screen.system.ISystemBridge
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.test.common.nextString

/**
 * Test for [SystemInteractor]
 */
@ExperimentalCoroutinesApi
class SystemInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var bindRepo: BindRepo
    @MockK lateinit var alarmRepo: AlarmRepo
    @MockK lateinit var rankRepo: RankRepo
    @MockK lateinit var noteRepo: NoteRepo

    @MockK lateinit var callback: ISystemBridge

    private val interactor by lazy {
        SystemInteractor(preferencesRepo, bindRepo, alarmRepo, rankRepo, noteRepo, callback)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo, bindRepo, alarmRepo, rankRepo, noteRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }

    @Test fun tidyUpAlarm() = startCoTest {
        val size = getRandomSize()
        val list = List<NotificationItem>(size) { mockk() }
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
        val sort = mockk<Sort>()
        val itemList = mockk<MutableList<NoteItem>>()
        val rankIdVisibleList = mockk<List<Long>>()
        val filterList = mockk<List<NoteItem>>()

        every { preferencesRepo.sort } returns sort
        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList
        coEvery {
            noteRepo.getList(sort, isBin = false, isOptimal = false, filterVisible = false)
        } returns itemList
        coEvery { spyInteractor.getFilterList(itemList, rankIdVisibleList) } returns filterList

        spyInteractor.notifyNotesBind()

        coVerifySequence {
            spyInteractor.notifyNotesBind()

            preferencesRepo.sort
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

        coEvery { bindRepo.getNotificationsCount() } returns count

        interactor.notifyCountBind()

        coVerifySequence {
            bindRepo.getNotificationsCount()
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
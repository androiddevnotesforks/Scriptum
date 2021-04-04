package sgtmelon.scriptum.domain.interactor.impl.preference

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.provider.PreferenceProvider
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.presentation.control.file.IFileControl

/**
 * Test for [PrintInteractor]
 */
@ExperimentalCoroutinesApi
class PrintInteractorTest : ParentInteractorTest() {

    @MockK lateinit var developRepo: IDevelopRepo
    @MockK lateinit var key: PreferenceProvider.Key
    @MockK lateinit var def: PreferenceProvider.Def
    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var fileControl: IFileControl

    private val interactor by lazy {
        PrintInteractor(developRepo, key, def, preferenceRepo, fileControl)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @Test fun getList_forNote() = startCoTest {
        val type = PrintType.NOTE
        val list = mockk<List<PrintItem.Note>>()

        coEvery { developRepo.getPrintNoteList(isBin = false) } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintNoteList(isBin = false)
        }
    }

    @Test fun getList_forBin() = startCoTest {
        val type = PrintType.BIN
        val list = mockk<List<PrintItem.Note>>()

        coEvery { developRepo.getPrintNoteList(isBin = true) } returns list
        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintNoteList(isBin = true)
        }
    }

    @Test fun getList_forRoll() = startCoTest {
        val type = PrintType.ROLL
        val list = mockk<List<PrintItem.Roll>>()

        coEvery { developRepo.getPrintRollList() } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintRollList()
        }
    }

    @Test fun getList_forVisible() = startCoTest {
        val type = PrintType.VISIBLE
        val list = mockk<List<PrintItem.Visible>>()

        coEvery { developRepo.getPrintVisibleList() } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintVisibleList()
        }
    }

    @Test fun getList_forRank() = startCoTest {
        val type = PrintType.RANK
        val list = mockk<List<PrintItem.Rank>>()

        coEvery { developRepo.getPrintRankList() } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintRankList()
        }
    }

    @Test fun getList_forAlarm() = startCoTest {
        val type = PrintType.ALARM
        val list = mockk<List<PrintItem.Alarm>>()

        coEvery { developRepo.getPrintAlarmList() } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintAlarmList()
        }
    }

    @Test fun getList_forKey() = startCoTest {
        val type = PrintType.KEY
        val list = mockk<List<PrintItem.Preference>>()

        coEvery { spyInteractor.getPreferenceKeyList() } returns list

        assertEquals(list, spyInteractor.getList(type))

        coVerifySequence {
            spyInteractor.getList(type)
            spyInteractor.getPreferenceKeyList()
        }
    }

    @Test fun getList_forFile() = startCoTest {
        val type = PrintType.FILE
        val list = mockk<List<PrintItem.Preference>>()

        coEvery { spyInteractor.getPreferenceFileList() } returns list

        assertEquals(list, spyInteractor.getList(type))

        coVerifySequence {
            spyInteractor.getList(type)
            spyInteractor.getPreferenceFileList()
        }
    }
}
package sgtmelon.scriptum.presentation.screen.vm.impl.main

import android.os.Bundle
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IMainInteractor
import sgtmelon.scriptum.domain.model.key.MainPage
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IMainActivity
import kotlin.random.Random

/**
 * Test for [MainViewModel].
 */
@ExperimentalCoroutinesApi
class MainViewModelTest : ParentViewModelTest() {

    private val data = TestData.Note

    @MockK lateinit var callback: IMainActivity

    @MockK lateinit var interactor: IMainInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    private val bundle = mockkClass(Bundle::class)

    private val viewModel by lazy { MainViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor, bindInteractor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        viewModel.onDestroy()

        assertNull(viewModel.callback)
        verifySequence { interactor.onDestroy() }
    }


    @Test fun onSetup_onFirstStart() = startCoTest {
        viewModel.pageFrom = MainPage.NOTES
        viewModel.onSetup()

        coVerifySequence {
            interactor.tidyUpAlarm()
            bindInteractor.notifyNoteBind(callback)
            bindInteractor.notifyInfoBind(callback)

            callback.setupNavigation(R.id.item_page_notes)
        }
    }

    @Test fun onSetup_onOtherStart() {
        every { bundle.getBoolean(MainViewModel.FIRST_START) } returns false

        every { bundle.getInt(MainViewModel.PAGE_CURRENT) } returns MainPage.RANK.ordinal
        viewModel.onSetup(bundle)

        every { bundle.getInt(MainViewModel.PAGE_CURRENT) } returns MainPage.NOTES.ordinal
        viewModel.onSetup(bundle)

        every { bundle.getInt(MainViewModel.PAGE_CURRENT) } returns MainPage.BIN.ordinal
        viewModel.onSetup(bundle)

        every { bundle.getInt(MainViewModel.PAGE_CURRENT) } returns -1
        viewModel.onSetup(bundle)

        verifySequence {
            callback.setupNavigation(R.id.item_page_rank)
            callback.setFabState(state = false)

            callback.setupNavigation(R.id.item_page_notes)
            callback.setFabState(state = true)

            callback.setupNavigation(R.id.item_page_bin)
            callback.setFabState(state = false)

            callback.setupNavigation(R.id.item_page_notes)
            callback.setFabState(state = true)
        }
    }

    @Test fun onSaveData() {
        every { bundle.putBoolean(MainViewModel.FIRST_START, any()) } returns Unit
        every { bundle.putInt(MainViewModel.PAGE_CURRENT, any()) } returns Unit

        viewModel.firstStart = false
        viewModel.pageFrom = MainPage.BIN
        viewModel.onSaveData(bundle)

        viewModel.firstStart = true
        viewModel.pageFrom = MainPage.NOTES
        viewModel.onSaveData(bundle)

        verifySequence {
            bundle.putBoolean(MainViewModel.FIRST_START, false)
            bundle.putInt(MainViewModel.PAGE_CURRENT, MainPage.BIN.ordinal)

            bundle.putBoolean(MainViewModel.FIRST_START, true)
            bundle.putInt(MainViewModel.PAGE_CURRENT, MainPage.NOTES.ordinal)
        }
    }

    @Test fun onSelectItem_onScrollTOp() {
        viewModel.firstStart = false

        viewModel.pageFrom = MainPage.RANK
        viewModel.onSelectItem(R.id.item_page_rank)

        viewModel.pageFrom = MainPage.NOTES
        viewModel.onSelectItem(R.id.item_page_notes)

        viewModel.pageFrom = MainPage.BIN
        viewModel.onSelectItem(R.id.item_page_bin)

        viewModel.onSelectItem(itemId = -1)

        verifySequence {
            callback.scrollTop(MainPage.RANK)
            callback.scrollTop(MainPage.NOTES)
            callback.scrollTop(MainPage.BIN)
        }
    }

    @Test fun onSelectItem_onChangePage() {
        viewModel.firstStart = true
        viewModel.pageFrom = MainPage.RANK
        viewModel.onSelectItem(R.id.item_page_rank)

        assertFalse(viewModel.firstStart)
        assertEquals(MainPage.RANK, viewModel.pageFrom)

        viewModel.firstStart = false
        viewModel.pageFrom = MainPage.BIN
        viewModel.onSelectItem(R.id.item_page_notes)

        assertFalse(viewModel.firstStart)
        assertEquals(MainPage.NOTES, viewModel.pageFrom)

        viewModel.firstStart = false
        viewModel.pageFrom = MainPage.NOTES
        viewModel.onSelectItem(R.id.item_page_bin)

        assertFalse(viewModel.firstStart)
        assertEquals(MainPage.BIN, viewModel.pageFrom)

        viewModel.onSelectItem(itemId = -1)

        verifySequence {
            callback.setFabState(state = false)
            callback.showPage(MainPage.RANK, MainPage.RANK)

            callback.setFabState(state = true)
            callback.showPage(MainPage.BIN, MainPage.NOTES)

            callback.setFabState(state = false)
            callback.showPage(MainPage.NOTES, MainPage.BIN)
        }
    }

    @Test fun onFabStateChange() {
        viewModel.pageFrom = MainPage.RANK
        viewModel.onFabStateChange(state = false)
        viewModel.onFabStateChange(state = true)

        viewModel.pageFrom = MainPage.NOTES
        viewModel.onFabStateChange(state = false)
        viewModel.onFabStateChange(state = true)

        viewModel.pageFrom = MainPage.BIN
        viewModel.onFabStateChange(state = false)
        viewModel.onFabStateChange(state = true)

        verifySequence {
            callback.setFabState(state = false)
            callback.setFabState(state = false)

            callback.setFabState(state = false)
            callback.setFabState(state = true)

            callback.setFabState(state = false)
            callback.setFabState(state = false)
        }
    }

    @Test fun onResultAddDialog() {
        viewModel.onResultAddDialog(R.id.item_add_text)
        viewModel.onResultAddDialog(R.id.item_add_roll)
        viewModel.onResultAddDialog(itemId = -1)

        verifySequence {
            callback.startNoteActivity(NoteType.TEXT)
            callback.startNoteActivity(NoteType.ROLL)
        }
    }

    @Test fun onReceiveUnbindNote() {
        val firstId = Random.nextLong()
        val secondId = Random.nextLong()

        viewModel.onReceiveUnbindNote(firstId)
        viewModel.pageFrom = MainPage.RANK
        viewModel.onReceiveUnbindNote(secondId)

        verifySequence {
            callback.onReceiveUnbindNote(firstId)
            callback.onReceiveUnbindNote(secondId)
        }
    }

    @Test fun onReceiveUpdateAlarm() {
        val firstId = Random.nextLong()
        val secondId = Random.nextLong()

        viewModel.onReceiveUpdateAlarm(firstId)
        viewModel.pageFrom = MainPage.RANK
        viewModel.onReceiveUpdateAlarm(secondId)

        verifySequence {
            callback.onReceiveUpdateAlarm(secondId)
        }
    }
}
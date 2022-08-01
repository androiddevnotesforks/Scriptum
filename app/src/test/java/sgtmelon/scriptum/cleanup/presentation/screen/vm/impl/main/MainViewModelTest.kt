package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main

import android.os.Bundle
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Main.Intent
import sgtmelon.scriptum.cleanup.domain.model.key.MainPage
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.IMainActivity

/**
 * Test for [MainViewModel].
 */
@ExperimentalCoroutinesApi
class MainViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: IMainActivity

    @MockK lateinit var bundle: Bundle

    private val viewModel by lazy { MainViewModel(callback) }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    @Test fun onSetup_onFirstStart() = startCoTest {
        viewModel.pageFrom = MainPage.NOTES
        viewModel.onSetup()

        coVerifySequence {
            callback.setupNavigation(R.id.item_page_notes)
            callback.setupInsets()
        }
    }

    @Test fun onSetup_onOtherStart() {
        every { bundle.getBoolean(Intent.FIRST_START) } returns false

        every { bundle.getInt(Intent.PAGE_CURRENT) } returns MainPage.RANK.ordinal
        viewModel.onSetup(bundle)

        every { bundle.getInt(Intent.PAGE_CURRENT) } returns MainPage.NOTES.ordinal
        viewModel.onSetup(bundle)

        every { bundle.getInt(Intent.PAGE_CURRENT) } returns MainPage.BIN.ordinal
        viewModel.onSetup(bundle)

        every { bundle.getInt(Intent.PAGE_CURRENT) } returns -1
        viewModel.onSetup(bundle)

        verifySequence {
            bundle.getBoolean(Intent.FIRST_START)
            bundle.getInt(Intent.PAGE_CURRENT)
            callback.setupNavigation(R.id.item_page_rank)
            callback.setupInsets()
            callback.setFabState(state = false)

            bundle.getBoolean(Intent.FIRST_START)
            bundle.getInt(Intent.PAGE_CURRENT)
            callback.setupNavigation(R.id.item_page_notes)
            callback.setupInsets()
            callback.setFabState(state = true)

            bundle.getBoolean(Intent.FIRST_START)
            bundle.getInt(Intent.PAGE_CURRENT)
            callback.setupNavigation(R.id.item_page_bin)
            callback.setupInsets()
            callback.setFabState(state = false)

            bundle.getBoolean(Intent.FIRST_START)
            bundle.getInt(Intent.PAGE_CURRENT)
            callback.setupNavigation(R.id.item_page_notes)
            callback.setupInsets()
            callback.setFabState(state = true)
        }
    }

    @Test fun onSaveData() {
        val isFirstStart = Random.nextBoolean()
        val pageFrom = MainPage.values().random()

        every { bundle.putBoolean(Intent.FIRST_START, isFirstStart) } returns Unit
        every { bundle.putInt(Intent.PAGE_CURRENT, pageFrom.ordinal) } returns Unit

        viewModel.isFirstStart = isFirstStart
        viewModel.pageFrom = pageFrom
        viewModel.onSaveData(bundle)

        verifySequence {
            bundle.putBoolean(Intent.FIRST_START, isFirstStart)
            bundle.putInt(Intent.PAGE_CURRENT, pageFrom.ordinal)
        }
    }

    @Test fun onSelectItem_onScrollTOp() {
        viewModel.isFirstStart = false

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
        viewModel.isFirstStart = true
        viewModel.pageFrom = MainPage.RANK
        viewModel.onSelectItem(R.id.item_page_rank)

        assertFalse(viewModel.isFirstStart)
        assertEquals(MainPage.RANK, viewModel.pageFrom)

        viewModel.isFirstStart = false
        viewModel.pageFrom = MainPage.BIN
        viewModel.onSelectItem(R.id.item_page_notes)

        assertFalse(viewModel.isFirstStart)
        assertEquals(MainPage.NOTES, viewModel.pageFrom)

        viewModel.isFirstStart = false
        viewModel.pageFrom = MainPage.NOTES
        viewModel.onSelectItem(R.id.item_page_bin)

        assertFalse(viewModel.isFirstStart)
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
            callback.openNoteScreen(NoteType.TEXT)
            callback.openNoteScreen(NoteType.ROLL)
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
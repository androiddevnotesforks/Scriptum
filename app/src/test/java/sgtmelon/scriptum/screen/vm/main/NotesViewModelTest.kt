package sgtmelon.scriptum.screen.vm.main

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.screen.vm.main.NotesViewModel.Companion.sort

/**
 * Test for [NotesViewModel].
 */
@ExperimentalCoroutinesApi
class NotesViewModelTest : ParentViewModelTest() {

    private val data = TestData.Note

    @MockK lateinit var callback: INotesFragment

    @MockK lateinit var interactor: INotesInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    private val viewModel by lazy { NotesViewModel(application) }

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


    @Test fun onSetup() {
        TODO()
    }

    @Test fun onUpdateData() {
        TODO()
    }

    @Test fun onClickNote() {
        TODO()
    }

    @Test fun onShowOptionsDialog() {
        TODO()
    }

    @Test fun onResultOptionsDialog() {
        TODO()
    }

    @Test fun onResultDateDialog() {
        TODO()
    }

    @Test fun onResultDateDialogClear() {
        TODO()
    }

    @Test fun onResultTimeDialog() {
        TODO()
    }

    @Test fun onReceiveUnbindNote() {
        TODO()
    }

    @Test fun onReceiveUpdateAlarm() {
        TODO()
    }


    @Test fun sort() = with(data) {
        assertEquals(changeList, itemList.sort(Sort.CHANGE))
        assertEquals(createList, itemList.sort(Sort.CREATE))
        assertEquals(rankList, itemList.sort(Sort.RANK))
        assertEquals(colorList, itemList.sort(Sort.COLOR))
    }

}
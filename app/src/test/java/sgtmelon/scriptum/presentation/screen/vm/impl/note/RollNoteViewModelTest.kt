package sgtmelon.scriptum.presentation.screen.vm.impl.note

import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.presentation.screen.ui.callback.note.roll.IRollNoteFragment

/**
 * Test for [RollNoteViewModel].
 */
@ExperimentalCoroutinesApi
class RollNoteViewModelTest : ParentViewModelTest() {


    @MockK lateinit var callback: IRollNoteFragment
    @MockK lateinit var parentCallback: INoteChild

    @MockK lateinit var interactor: IRollNoteInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    private val viewModel by lazy { RollNoteViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setParentCallback(parentCallback)
        viewModel.setInteractor(interactor, bindInteractor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        viewModel.onDestroy()

        TODO()

        assertNull(viewModel.callback)
        verifySequence { interactor.onDestroy() }
    }



    @Test fun todo() {
        TODO()
    }

}
package sgtmelon.scriptum.infrastructure.screen.main.bin

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.main.GetBinListUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest

/**
 * Test for [BinViewModelImpl].
 */
class BinViewModelImplTest : ParentLiveDataTest() {

    //region Setup

    @MockK lateinit var getList: GetBinListUseCase
    @MockK lateinit var getCopyText: GetCopyTextUseCase
    @MockK lateinit var restoreNote: RestoreNoteUseCase
    @MockK lateinit var clearBin: ClearBinUseCase
    @MockK lateinit var clearNote: ClearNoteUseCase

//    private val viewModel by lazy {
//        BinViewModelImpl(getList, getCopyText, restoreNote, clearBin, clearNote)
//    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(getList, getCopyText, restoreNote, clearBin, clearNote)
    }

    //endregion

    @Test fun todo() {
        TODO()
    }
}
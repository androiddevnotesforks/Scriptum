package sgtmelon.scriptum.screen.vm.main

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.extension.move
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.screen.vm.main.RankViewModel.Companion.correctPositions
import kotlin.random.Random

/**
 * Test for [RankViewModel].
 */
@ExperimentalCoroutinesApi
class RankViewModelTest : ParentViewModelTest() {

    private val data = TestData.Rank

    @MockK lateinit var callback: IRankFragment

    @MockK lateinit var interactor: IRankInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    private val openState = mockkClass(OpenState::class)

    private val viewModel by lazy { RankViewModel(application) }

    override fun setUp() {
        super.setUp()

        every { callback.openState } returns openState

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
        viewModel.onSetup()

        verifySequence {
            callback.setupToolbar()
            callback.setupRecycler()
        }
    }

    @Test fun onUpdateData_startEmpty_getNotEmpty() = startCoTest {
        coEvery { interactor.getCount() } returns data.itemList.size
        coEvery { interactor.getList() } returns data.itemList

        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()

            interactor.getCount()
            callback.showProgress()
            interactor.getList()
            updateList(data.itemList)
        }
    }

    @Test fun onUpdateData_startEmpty_getEmpty() = startCoTest {
        coEvery { interactor.getCount() } returns 0
        coEvery { interactor.getList() } returns mutableListOf()

        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()

            interactor.getCount()
            updateList(mutableListOf())
        }
    }

    @Test fun onUpdateData_startNotEmpty_getNotEmpty() = startCoTest {
        val returnList = mutableListOf(data.itemList.first())

        coEvery { interactor.getCount() } returns returnList.size
        coEvery { interactor.getList() } returns returnList

        viewModel.itemList.addAll(data.itemList)
        assertEquals(data.itemList, viewModel.itemList)

        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            updateList(any())

            interactor.getCount()
            interactor.getList()
            updateList(returnList)
        }
    }

    @Test fun onUpdateData_startNotEmpty_getEmpty() = startCoTest {
        coEvery { interactor.getCount() } returns 0
        coEvery { interactor.getList() } returns mutableListOf()

        viewModel.itemList.addAll(data.itemList)
        assertEquals(data.itemList, viewModel.itemList)

        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            updateList(any())

            interactor.getCount()
            updateList(mutableListOf())
        }
    }

    private fun updateList(itemList: List<RankItem>) = with(callback) {
        notifyList(itemList)
        onBindingList()
    }


    @Test fun onUpdateToolbar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Test fun onShowRenameDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Test fun onResultRenameDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Test fun onClickEnterCancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Test fun onEditorClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Test fun onClickEnterAdd() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Test fun onClickVisible() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Test fun onLongClickVisible() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Test fun onClickCancel() = startCoTest {
        viewModel.onClickCancel(Random.nextInt())

        val itemList = data.correctListSecond
        val noteIdList = listOf(4L, 6, 1, 2)

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = 1
        val item = itemList.removeAt(p)

        viewModel.onClickCancel(p)

        coVerifyAll {
            callback.notifyItemRemoved(itemList, p)

            interactor.delete(item)
            interactor.updatePosition(itemList, noteIdList)
            bindInteractor.notifyNoteBind(callback)
        }
    }

    @Test fun onTouchDrag() {
        every { openState.value } returns false
        assertTrue(viewModel.onTouchDrag())

        every { openState.value } returns true
        assertFalse(viewModel.onTouchDrag())

        verifySequence {
            callback.openState
            callback.openState
        }
    }

    @Test fun onTouchMove() {
        val itemList = data.itemList

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        val from = 0
        val to = 2

        assertTrue(viewModel.onTouchMove(from, to))
        itemList.move(from, to)

        verifySequence { callback.notifyItemMoved(itemList, from, to) }
    }

    @Test fun onTouchMoveResult() = startCoTest {
        every { openState.clear() } returns Unit

        val itemList = data.correctListFirst
        val noteIdList = data.correctPositionFirst

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onTouchMoveResult()
        itemList.correctPositions()

        coVerifySequence {
            callback.openState

            callback.setList(itemList)
            interactor.updatePosition(itemList, noteIdList)
        }
    }


    @Test fun correctPositions() = with(data) {
        var list: List<RankItem> = itemList
        var noteIdList = list.correctPositions()

        assertTrue(noteIdList.isEmpty())
        assertPositions(list)

        list = correctListFirst
        noteIdList = list.correctPositions()

        assertEquals(correctPositionFirst, noteIdList)
        assertPositions(list)

        list = correctListSecond
        noteIdList = list.correctPositions()

        assertEquals(correctPositionSecond, noteIdList)
        assertPositions(list)
    }

    private fun assertPositions(list: List<RankItem>) = list.forEachIndexed { i, item ->
        assertEquals(i, item.position)
    }

}
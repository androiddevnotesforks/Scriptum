package sgtmelon.scriptum.infrastructure.screen.main.rank

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verifyOrder
import io.mockk.verifySequence
import kotlin.math.max
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.domain.useCase.rank.CorrectRankPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.DeleteRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankListUseCase
import sgtmelon.scriptum.domain.useCase.rank.InsertRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankUseCase
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Snackbar
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.testing.getRandomSize
import sgtmelon.test.common.isDivideEntirely
import sgtmelon.test.common.nextString

/**
 * Test for [RankViewModelImpl].
 */
@ExperimentalCoroutinesApi
class RankViewModelImplTest : ParentViewModelTest() {

    //region Setup

    private val data = TestData.Rank

    @MockK lateinit var interactor: IRankInteractor
    @MockK lateinit var getList: GetRankListUseCase
    @MockK lateinit var insertRank: InsertRankUseCase
    @MockK lateinit var deleteRank: DeleteRankUseCase
    @MockK lateinit var updateRank: UpdateRankUseCase
    @MockK lateinit var correctRankPositions: CorrectRankPositionsUseCase

    @MockK lateinit var openState: OpenState

    private val viewModel by lazy {
        RankViewModelImpl(
            callback, interactor, getList, insertRank, deleteRank, updateRank, correctRankPositions
        )
    }
    private val spyViewModel by lazy { spyk(viewModel) }

    @Before override fun setUp() {
        super.setUp()

        every { callback.openState } returns openState

        assertTrue(viewModel._itemList.isEmpty())
        assertTrue(viewModel.undoList.isEmpty())

        assertFalse(viewModel.inTouchAction)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            callback, interactor, getList, insertRank, deleteRank, updateRank,
            correctRankPositions, openState
        )
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    @Test fun onSetup() {
        val bundle = mockk<Bundle>()

        viewModel.onSetup()

        every { spyViewModel.restoreSnackbar(bundle) } returns Unit
        spyViewModel.onSetup(bundle)

        verifySequence {
            callback.setupToolbar()
            callback.setupRecycler()
            callback.setupDialog()
            callback.prepareForLoad()

            spyViewModel.onSetup(bundle)
            spyViewModel.callback
            callback.setupToolbar()
            spyViewModel.callback
            callback.setupRecycler()
            spyViewModel.callback
            callback.setupDialog()
            spyViewModel.callback
            callback.prepareForLoad()
            spyViewModel.restoreSnackbar(bundle)
        }
    }

    @Test fun restoreSnackbar() {
        val bundle = mockk<Bundle>()

        val size = getRandomSize()
        val positionArray = IntArray(size) { Random.nextInt() }
        val jsonArray = Array(size) { nextString() }
        val itemList = List<RankItem?>(size) { if (it.isDivideEntirely()) mockk() else null }

        val cancelList = mutableListOf<Pair<Int, RankItem>>()

        mockkObject(RankItem)
        for ((i, item) in itemList.withIndex()) {
            every { RankItem[jsonArray[i]] } returns item

            if (item != null) {
                cancelList.add(Pair(positionArray[i], item))
            }
        }

        every { bundle.getIntArray(Snackbar.Intent.POSITIONS) } returns null
        viewModel.restoreSnackbar(bundle)

        assertTrue(viewModel.undoList.isEmpty())

        every { bundle.getIntArray(Snackbar.Intent.POSITIONS) } returns positionArray
        every { bundle.getStringArray(Snackbar.Intent.ITEMS) } returns null
        viewModel.restoreSnackbar(bundle)

        assertTrue(viewModel.undoList.isEmpty())

        every { bundle.getStringArray(Snackbar.Intent.ITEMS) } returns jsonArray
        viewModel.restoreSnackbar(bundle)

        assertEquals(cancelList, viewModel.undoList)

        verifySequence {
            bundle.getIntArray(Snackbar.Intent.POSITIONS)

            bundle.getIntArray(Snackbar.Intent.POSITIONS)
            bundle.getStringArray(Snackbar.Intent.ITEMS)

            bundle.getIntArray(Snackbar.Intent.POSITIONS)
            bundle.getStringArray(Snackbar.Intent.ITEMS)
            for (i in itemList.indices) {
                RankItem[jsonArray[i]]
            }
            callback.showSnackbar()
        }
    }

    @Test fun onSaveData() {
        val size = getRandomSize()
        val positionArray = IntArray(size) { Random.nextInt() }
        val jsonArray = Array(size) { nextString() }
        val itemList = List(size) { mockk<RankItem>() }

        val bundle = mockk<Bundle>()

        for ((i, item) in itemList.withIndex()) {
            every { item.toJson() } returns jsonArray[i]

            viewModel.undoList.add(Pair(positionArray[i], item))
        }

        every { bundle.putIntArray(Snackbar.Intent.POSITIONS, positionArray) } returns Unit
        every { bundle.putStringArray(Snackbar.Intent.ITEMS, jsonArray) } returns Unit

        viewModel.onSaveData(bundle)
        assertTrue(viewModel.undoList.isEmpty())

        verifySequence {
            bundle.putIntArray(Snackbar.Intent.POSITIONS, positionArray)
            bundle.putStringArray(Snackbar.Intent.ITEMS, jsonArray)
        }
    }

    @Test fun onUpdateData_startEmpty_getNotEmpty() = startCoTest {
        val itemList = data.itemList

        coEvery { interactor.getCount() } returns itemList.size
        coEvery { getList() } returns itemList

        viewModel._itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            interactor.getCount()
            callback.hideEmptyInfo()
            callback.showProgress()
            getList()
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startEmpty_getEmpty() = startCoTest {
        val itemList = mutableListOf<RankItem>()

        coEvery { interactor.getCount() } returns itemList.size
        viewModel._itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            interactor.getCount()
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startNotEmpty_getNotEmpty() = startCoTest {
        val startList = data.itemList
        val returnList = data.itemList.apply { shuffle() }

        coEvery { interactor.getCount() } returns returnList.size
        coEvery { getList() } returns returnList

        viewModel._itemList.clearAdd(startList)
        assertEquals(startList, viewModel._itemList)
        viewModel.onUpdateData()

        coVerifySequence {
            updateList(any())
            interactor.getCount()
            getList()
            updateList(returnList)
        }
    }

    @Test fun onUpdateData_startNotEmpty_getEmpty() = startCoTest {
        val startList = data.itemList
        val returnList = mutableListOf<NoteItem>()

        coEvery { interactor.getCount() } returns returnList.size

        viewModel._itemList.clearAdd(startList)
        assertEquals(startList, viewModel._itemList)
        viewModel.onUpdateData()

        coVerifySequence {
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
        val itemList = data.itemList
        val item = itemList[itemList.indices.random()]

        every { callback.getEnterText() } returns ""
        viewModel.onUpdateToolbar()

        every { callback.getEnterText() } returns "   "
        viewModel.onUpdateToolbar()

        every { callback.getEnterText() } returns item.name
        viewModel.onUpdateToolbar()

        viewModel._itemList.clearAdd(data.itemList)
        viewModel.onUpdateToolbar()

        verifySequence {
            callback.apply {
                getEnterText()
                onBindingToolbar(isClearEnable = false, isAddEnable = false)

                getEnterText()
                onBindingToolbar(isClearEnable = true, isAddEnable = false)

                getEnterText()
                onBindingToolbar(isClearEnable = true, isAddEnable = true)

                getEnterText()
                onBindingToolbar(isClearEnable = true, isAddEnable = false)
            }
        }
    }

    @Test fun onShowRenameDialog() {
        viewModel.onShowRenameDialog(Random.nextInt())

        val itemList = data.itemList
        val nameList = mockk<List<String>>()

        every { spyViewModel.getNameList(itemList) } returns nameList

        spyViewModel._itemList.clearAdd(itemList)
        assertEquals(itemList, spyViewModel._itemList)

        val p = itemList.indices.random()
        val item = itemList[p]

        spyViewModel.onShowRenameDialog(p)

        verifyOrder {
            callback.dismissSnackbar()

            spyViewModel.getNameList(itemList)
            callback.showRenameDialog(p, item.name, nameList)
        }
    }

    @Test fun onResultRenameDialog() = startCoTest {
        TODO()
        //        val newName = nextString()
        //
        //        every { callback.getEnterText() } returns newName
        //
        //        viewModel.onResultRenameDialog(Random.nextInt(), newName)
        //
        //        val itemList = data.itemList
        //
        //        viewModel.itemList.clearAdd(itemList)
        //        assertEquals(itemList, viewModel.itemList)
        //
        //        val p = itemList.indices.random()
        //        val item = itemList[p].apply { name = newName }
        //
        //        viewModel.onResultRenameDialog(p, newName)
        //
        //        coVerifySequence {
        //            updateRank(item)
        //
        //            callback.getEnterText()
        //            callback.onBindingToolbar(isClearEnable = true, isAddEnable = false)
        //
        //            callback.notifyItemChanged(itemList, p)
        //        }
    }

    @Test fun onClickEnterCancel() {
        every { callback.clearEnter() } returns data.itemList.random().name

        viewModel.onClickEnterCancel()

        verifySequence { callback.clearEnter() }
    }

    @Test fun onEditorClick() {
        assertFalse(spyViewModel.onEditorClick(EditorInfo.IME_ACTION_NEXT))

        every { callback.getEnterText() } returns ""
        assertFalse(spyViewModel.onEditorClick(EditorInfo.IME_ACTION_DONE))

        every { spyViewModel.onClickEnterAdd(addToBottom = true) } returns Unit
        every { callback.getEnterText() } returns nextString()
        every { callback.clearEnter() } returns ""
        assertTrue(spyViewModel.onEditorClick(EditorInfo.IME_ACTION_DONE))

        val name = nextString()
        var itemList = listOf(data.firstRank.copy(name = name))
        spyViewModel._itemList.clearAdd(itemList)
        assertEquals(itemList, spyViewModel._itemList)

        every { callback.getEnterText() } returns name
        assertFalse(spyViewModel.onEditorClick(EditorInfo.IME_ACTION_DONE))

        itemList = data.itemList
        spyViewModel._itemList.clearAdd(itemList)
        assertEquals(itemList, spyViewModel._itemList)

        every { callback.getEnterText() } returns itemList.random().name
        assertFalse(spyViewModel.onEditorClick(EditorInfo.IME_ACTION_DONE))

        verifyOrder {
            callback.getEnterText()

            callback.getEnterText()
            spyViewModel.onClickEnterAdd(addToBottom = true)

            callback.getEnterText()

            callback.getEnterText()
        }
    }

    @Test fun onClickEnterAdd_onNameError() {
        every { callback.clearEnter() } returns ""
        viewModel.onClickEnterAdd(Random.nextBoolean())

        val name = nextString()

        val itemList = listOf(data.firstRank.copy(name = name))
        viewModel._itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel._itemList)

        every { callback.getEnterText() } returns name
        viewModel.onClickEnterAdd(Random.nextBoolean())

        coVerifyAll {
            callback.clearEnter()
            callback.clearEnter()
        }
    }

    @Test fun onClickEnterAdd_onSimple() = startCoTest {
        val itemList = data.itemList
        val name = nextString()
        val item = data.firstRank.copy(name = name)

        every { callback.clearEnter() } returns name
        coEvery { insertRank(name) } returns item

        val p = itemList.size
        val resultList = ArrayList(itemList).apply { add(p, item) }
        val noteIdList = mockk<List<Long>>()

        every { spyViewModel.getNameList(any()) } returns emptyList()
        every { correctRankPositions(resultList) } returns noteIdList

        spyViewModel._itemList.clearAdd(itemList)
        assertEquals(itemList, spyViewModel._itemList)

        spyViewModel.onClickEnterAdd(addToBottom = true)

        assertEquals(resultList, spyViewModel._itemList)

        coVerifyOrder {
            callback.clearEnter()
            spyViewModel.getNameList(any())
            callback.dismissSnackbar()
            insertRank(name)

            correctRankPositions(resultList)
            interactor.updatePositions(resultList, noteIdList)
            callback.scrollToItem(resultList, p, addToBottom = true)
        }
    }

    @Test fun onClickEnterAdd_onLong() = startCoTest {
        val itemList = data.itemList

        val name = nextString()
        val item = data.firstRank.copy(name = name)

        every { callback.clearEnter() } returns name
        coEvery { insertRank(name) } returns item

        val p = 0
        val resultList = ArrayList(itemList).apply { add(p, item) }
        val noteIdList = mockk<List<Long>>()

        every { spyViewModel.getNameList(any()) } returns emptyList()
        every { correctRankPositions(resultList) } returns noteIdList

        spyViewModel._itemList.clearAdd(itemList)
        assertEquals(itemList, spyViewModel._itemList)

        spyViewModel.onClickEnterAdd(addToBottom = false)

        assertEquals(resultList, spyViewModel._itemList)

        coVerifyOrder {
            callback.clearEnter()
            spyViewModel.getNameList(any())
            callback.dismissSnackbar()
            insertRank(name)

            correctRankPositions(resultList)
            interactor.updatePositions(resultList, noteIdList)
            callback.scrollToItem(resultList, p, addToBottom = false)
        }
    }

    @Test fun onClickEnterAdd_onNull() = startCoTest {
        val itemList = data.itemList
        val name = nextString()

        every { spyViewModel.getNameList(itemList) } returns emptyList()
        every { callback.clearEnter() } returns name
        coEvery { insertRank(name) } returns null

        spyViewModel._itemList.clearAdd(itemList)

        assertEquals(itemList, spyViewModel._itemList)

        spyViewModel.onClickEnterAdd(addToBottom = false)
        spyViewModel.onClickEnterAdd(addToBottom = true)

        assertEquals(itemList, spyViewModel._itemList)

        coVerifyOrder {
            repeat(times = 2) {
                callback.clearEnter()
                spyViewModel.getNameList(itemList)
                callback.dismissSnackbar()
                insertRank(name)
            }
        }
    }

    @Test fun onClickVisible() = startCoTest {
        viewModel.onClickVisible(Random.nextInt())

        val itemList = data.itemList

        viewModel._itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel._itemList)

        val p = itemList.indices.random()
        val item = itemList[p].switchVisible()

        viewModel.onClickVisible(p)

        coVerifySequence {
            callback.setList(itemList)

            updateRank(item)
            callback.sendNotifyNotesBroadcast()
        }
    }

    @Test fun onClickCancel() = startCoTest {
        TODO()
        //        viewModel.onClickCancel(Random.nextInt())
        //
        //        val itemList = data.secondCorrectList
        //        val noteIdList = listOf(4L, 6, 1, 2)
        //
        //        viewModel.itemList.clearAdd(itemList)
        //        assertEquals(itemList, viewModel.itemList)
        //
        //        assertEquals(itemList, viewModel.itemList)
        //        assertTrue(viewModel.cancelList.isEmpty())
        //
        //        val p = 1
        //        val item = itemList.removeAt(p)
        //
        //        every { correctPositions(itemList) } returns noteIdList
        //
        //        viewModel.onClickCancel(p)
        //
        //        assertEquals(itemList, viewModel.itemList)
        //        assertEquals(mutableListOf(Pair(p, item)), viewModel.cancelList)
        //
        //        coVerifySequence {
        //            correctPositions(itemList)
        //
        //            callback.notifyItemRemoved(itemList, p)
        //            callback.showSnackbar()
        //
        //            deleteRank(item)
        //            interactor.updatePositions(itemList, noteIdList)
        //
        //            callback.sendNotifyNotesBroadcast()
        //        }
    }

    @Test fun onItemAnimationFinished() {
        every { openState.clear() } returns Unit

        viewModel.onItemAnimationFinished()

        viewModel.inTouchAction = true
        viewModel.onItemAnimationFinished()

        verifySequence {
            callback.onBindingList()
            callback.openState
            openState.clear()

            callback.onBindingList()
        }
    }


    @Test fun onSnackbarAction_correct() {
        viewModel.onSnackbarAction()

        val item = mockk<RankItem>()

        val firstPair = Pair(Random.nextInt(), mockk<RankItem>())
        val secondPair = Pair(0, item)

        val itemList = mutableListOf(mockk<RankItem>())
        val cancelList = mutableListOf(firstPair, secondPair)

        val resultList = ArrayList(itemList).apply { add(0, item) }
        val noteIdList = mockk<List<Long>>()

        every { correctRankPositions(resultList) } returns noteIdList

        viewModel._itemList.clearAdd(itemList)
        viewModel.undoList.clearAdd(cancelList)

        assertEquals(itemList, viewModel._itemList)
        assertEquals(cancelList, viewModel.undoList)

        viewModel.onSnackbarAction()

        cancelList.removeAt(index = 1)

        assertEquals(resultList, viewModel._itemList)
        assertEquals(cancelList, viewModel.undoList)

        coVerifySequence {
            callback.apply {
                notifyItemInsertedScroll(resultList, secondPair.first)
                showSnackbar()
            }

            insertRank(item)
            correctRankPositions(resultList)
            interactor.updatePositions(resultList, noteIdList)
            callback.setList(resultList)
            callback.sendNotifyNotesBroadcast()
        }
    }

    @Test fun onSnackbarAction_incorrect() {
        viewModel.onSnackbarAction()

        val item = mockk<RankItem>()
        val pair = Pair(Random.nextInt(), item)

        val itemList = mutableListOf(mockk<RankItem>())
        val cancelList = mutableListOf(pair)

        val resultList = ArrayList(itemList).apply { add(item) }
        val noteIdList = mockk<List<Long>>()

        every { correctRankPositions(resultList) } returns noteIdList

        viewModel._itemList.clearAdd(itemList)
        viewModel.undoList.clearAdd(cancelList)

        assertEquals(itemList, viewModel._itemList)
        assertEquals(cancelList, viewModel.undoList)

        viewModel.onSnackbarAction()

        cancelList.removeAt(index = 0)

        assertEquals(resultList, viewModel._itemList)
        assertEquals(cancelList, viewModel.undoList)

        coVerifyOrder {
            callback.notifyItemInsertedScroll(resultList, resultList.lastIndex)

            insertRank(item)
            correctRankPositions(resultList)
            interactor.updatePositions(resultList, noteIdList)
            callback.setList(resultList)
            callback.sendNotifyNotesBroadcast()
        }
    }

    @Test fun onSnackbarAction_onBindingList() {
        viewModel.onSnackbarAction()

        val item = mockk<RankItem>()
        val pair = Pair(Random.nextInt(), item)

        val itemList = mutableListOf<RankItem>()
        val cancelList = mutableListOf(pair)

        val resultList = mutableListOf(item)
        val noteIdList = mockk<List<Long>>()

        every { correctRankPositions(resultList) } returns noteIdList

        viewModel.undoList.clearAdd(cancelList)

        assertEquals(itemList, viewModel._itemList)
        assertEquals(cancelList, viewModel.undoList)

        viewModel.onSnackbarAction()

        cancelList.removeAt(index = 0)

        assertEquals(resultList, viewModel._itemList)
        assertEquals(cancelList, viewModel.undoList)

        coVerifyOrder {
            callback.notifyItemInsertedScroll(resultList, resultList.lastIndex)
            callback.onBindingList()

            insertRank(item)
            correctRankPositions(resultList)
            interactor.updatePositions(resultList, noteIdList)
            callback.setList(resultList)
            callback.sendNotifyNotesBroadcast()
        }
    }

    @Test fun onSnackbarDismiss() {
        val cancelList = MutableList(size = 5) { Pair(Random.nextInt(), mockk<RankItem>()) }

        viewModel.undoList.clearAdd(cancelList)
        assertEquals(cancelList, viewModel.undoList)

        viewModel.onSnackbarDismiss()

        assertTrue(viewModel.undoList.isEmpty())
    }


    @Test fun onReceiveUnbindNote() {
        val itemList = List<RankItem>(getRandomSize()) { mockk() }
        val index = itemList.indices.random()
        val item = itemList[index]

        val id = Random.nextLong()
        val bindCount = (0..10).random()
        val resultBindCount = max(a = 0, b = bindCount - 1)

        every { item.noteId } returns mutableListOf(id)
        every { item.bindCount } returns bindCount
        every { item.bindCount = resultBindCount } returns Unit

        for ((i, rank) in itemList.withIndex()) {
            if (i == index) break

            every { rank.noteId } returns mutableListOf()
        }

        viewModel._itemList.clearAdd(itemList)
        viewModel.onReceiveUnbindNote(id)

        coVerifySequence {
            for ((i, rank) in itemList.withIndex()) {
                when {
                    i < index -> rank.noteId
                    i == index -> {
                        item.noteId
                        item.bindCount
                        item.bindCount = resultBindCount
                    }
                    i > index -> break
                }
            }

            callback.notifyList(itemList)
        }
    }


    @Test fun onTouchAction() {
        every { openState.isBlocked = true } returns Unit
        viewModel.onTouchAction(inAction = true)
        assertTrue(viewModel.inTouchAction)

        every { openState.isBlocked = false } returns Unit
        viewModel.onTouchAction(inAction = false)
        assertFalse(viewModel.inTouchAction)

        verifySequence {
            callback.dismissSnackbar()
            callback.openState
            openState.isBlocked = true

            callback.openState
            openState.isBlocked = false
        }
    }

    @Test fun onTouchGetDrag() {
        every { openState.isBlocked } returns false
        assertTrue(viewModel.onTouchGetDrag())

        every { openState.isBlocked } returns true
        assertFalse(viewModel.onTouchGetDrag())

        verifySequence {
            callback.openState
            openState.isBlocked
            callback.hideKeyboard()

            callback.openState
            openState.isBlocked
        }
    }

    @Test fun onTouchMove() {
        val itemList = data.itemList
        val from = Random.nextInt()
        val to = Random.nextInt()

        viewModel._itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel._itemList)

        FastMock.listExtension()
        every { itemList.move(from, to) } returns Unit

        assertTrue(viewModel.onTouchMove(from, to))

        verifySequence {
            itemList.move(from, to)
            callback.notifyItemMoved(itemList, from, to)
            callback.hideKeyboard()
        }
    }

    @Test fun onTouchMoveResult() = startCoTest {
        every { openState.clear() } returns Unit

        val itemList = data.firstCorrectList
        val noteIdList = mockk<List<Long>>()

        every { correctRankPositions(itemList) } returns noteIdList

        viewModel._itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel._itemList)

        viewModel.onTouchMoveResult()

        coVerifyOrder {
            callback.openState
            openState.clear()

            correctRankPositions(itemList)
            callback.setList(itemList)
            interactor.updatePositions(itemList, noteIdList)
        }
    }


    //region Companion test

    @Test fun getNameList() {
        val itemList = List(size = 5) {
            RankItem(id = Random.nextLong(), name = nextString())
        }
        val nameList = itemList.map { it.name.uppercase() }

        assertEquals(nameList, viewModel.getNameList(itemList))
    }

    //endregion

}
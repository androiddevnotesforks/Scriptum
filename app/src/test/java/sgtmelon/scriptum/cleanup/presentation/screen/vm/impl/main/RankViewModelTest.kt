package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main

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
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Snackbar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.domain.model.state.OpenState
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.cleanup.getRandomSize
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.domain.useCase.rank.CorrectPositionsUseCase
import sgtmelon.test.common.isDivideEntirely
import sgtmelon.test.common.nextString

/**
 * Test for [RankViewModel].
 */
@ExperimentalCoroutinesApi
class RankViewModelTest : ParentViewModelTest() {

    //region Setup

    private val data = TestData.Rank

    @MockK lateinit var callback: IRankFragment
    @MockK lateinit var interactor: IRankInteractor
    @MockK lateinit var correctPositions: CorrectPositionsUseCase

    @MockK lateinit var openState: OpenState

    private val viewModel by lazy { RankViewModel(callback, interactor, correctPositions) }
    private val spyViewModel by lazy { spyk(viewModel) }

    @Before override fun setUp() {
        super.setUp()

        every { callback.openState } returns openState

        assertTrue(viewModel.itemList.isEmpty())
        assertTrue(viewModel.cancelList.isEmpty())

        assertFalse(viewModel.inTouchAction)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor, correctPositions, openState)
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

        assertTrue(viewModel.cancelList.isEmpty())

        every { bundle.getIntArray(Snackbar.Intent.POSITIONS) } returns positionArray
        every { bundle.getStringArray(Snackbar.Intent.ITEMS) } returns null
        viewModel.restoreSnackbar(bundle)

        assertTrue(viewModel.cancelList.isEmpty())

        every { bundle.getStringArray(Snackbar.Intent.ITEMS) } returns jsonArray
        viewModel.restoreSnackbar(bundle)

        assertEquals(cancelList, viewModel.cancelList)

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

            viewModel.cancelList.add(Pair(positionArray[i], item))
        }

        every { bundle.putIntArray(Snackbar.Intent.POSITIONS, positionArray) } returns Unit
        every { bundle.putStringArray(Snackbar.Intent.ITEMS, jsonArray) } returns Unit

        viewModel.onSaveData(bundle)
        assertTrue(viewModel.cancelList.isEmpty())

        verifySequence {
            bundle.putIntArray(Snackbar.Intent.POSITIONS, positionArray)
            bundle.putStringArray(Snackbar.Intent.ITEMS, jsonArray)
        }
    }

    @Test fun onUpdateData_startEmpty_getNotEmpty() = startCoTest {
        val itemList = data.itemList

        coEvery { interactor.getCount() } returns itemList.size
        coEvery { interactor.getList() } returns itemList

        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            interactor.getCount()
            callback.hideEmptyInfo()
            callback.showProgress()
            interactor.getList()
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startEmpty_getEmpty() = startCoTest {
        val itemList = mutableListOf<RankItem>()

        coEvery { interactor.getCount() } returns itemList.size
        viewModel.itemList.clear()
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
        coEvery { interactor.getList() } returns returnList

        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
        viewModel.onUpdateData()

        coVerifySequence {
            updateList(any())
            interactor.getCount()
            interactor.getList()
            updateList(returnList)
        }
    }

    @Test fun onUpdateData_startNotEmpty_getEmpty() = startCoTest {
        val startList = data.itemList
        val returnList = mutableListOf<NoteItem>()

        coEvery { interactor.getCount() } returns returnList.size

        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
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

        viewModel.itemList.clearAdd(data.itemList)
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

        spyViewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, spyViewModel.itemList)

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
        val newName = nextString()

        every { callback.getEnterText() } returns newName

        viewModel.onResultRenameDialog(Random.nextInt(), newName)

        val itemList = data.itemList

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList[p].apply { name = newName }

        viewModel.onResultRenameDialog(p, newName)

        coVerifySequence {
            interactor.update(item)

            callback.getEnterText()
            callback.onBindingToolbar(isClearEnable = true, isAddEnable = false)

            callback.notifyItemChanged(itemList, p)
        }
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

        every { spyViewModel.onClickEnterAdd(simpleClick = true) } returns Unit
        every { callback.getEnterText() } returns nextString()
        every { callback.clearEnter() } returns ""
        assertTrue(spyViewModel.onEditorClick(EditorInfo.IME_ACTION_DONE))

        val name = nextString()
        var itemList = listOf(data.firstRank.copy(name = name))
        spyViewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, spyViewModel.itemList)

        every { callback.getEnterText() } returns name
        assertFalse(spyViewModel.onEditorClick(EditorInfo.IME_ACTION_DONE))

        itemList = data.itemList
        spyViewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, spyViewModel.itemList)

        every { callback.getEnterText() } returns itemList.random().name
        assertFalse(spyViewModel.onEditorClick(EditorInfo.IME_ACTION_DONE))

        verifyOrder {
            callback.getEnterText()

            callback.getEnterText()
            spyViewModel.onClickEnterAdd(simpleClick = true)

            callback.getEnterText()

            callback.getEnterText()
        }
    }

    @Test fun onClickEnterAdd_onNameError() {
        every { callback.clearEnter() } returns ""
        viewModel.onClickEnterAdd(Random.nextBoolean())

        val name = nextString()

        val itemList = listOf(data.firstRank.copy(name = name))
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

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
        coEvery { interactor.insert(name) } returns item

        val p = itemList.size
        val resultList = ArrayList(itemList).apply { add(p, item) }
        val noteIdList = mockk<List<Long>>()

        every { spyViewModel.getNameList(any()) } returns emptyList()
        every { correctPositions(resultList) } returns noteIdList

        spyViewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, spyViewModel.itemList)

        spyViewModel.onClickEnterAdd(simpleClick = true)

        assertEquals(resultList, spyViewModel.itemList)

        coVerifyOrder {
            callback.clearEnter()
            spyViewModel.getNameList(any())
            callback.dismissSnackbar()
            interactor.insert(name)

            correctPositions(resultList)
            interactor.updatePositions(resultList, noteIdList)
            callback.scrollToItem(resultList, p, simpleClick = true)
        }
    }

    @Test fun onClickEnterAdd_onLong() = startCoTest {
        val itemList = data.itemList

        val name = nextString()
        val item = data.firstRank.copy(name = name)

        every { callback.clearEnter() } returns name
        coEvery { interactor.insert(name) } returns item

        val p = 0
        val resultList = ArrayList(itemList).apply { add(p, item) }
        val noteIdList = mockk<List<Long>>()

        every { spyViewModel.getNameList(any()) } returns emptyList()
        every { correctPositions(resultList) } returns noteIdList

        spyViewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, spyViewModel.itemList)

        spyViewModel.onClickEnterAdd(simpleClick = false)

        assertEquals(resultList, spyViewModel.itemList)

        coVerifyOrder {
            callback.clearEnter()
            spyViewModel.getNameList(any())
            callback.dismissSnackbar()
            interactor.insert(name)

            correctPositions(resultList)
            interactor.updatePositions(resultList, noteIdList)
            callback.scrollToItem(resultList, p, simpleClick = false)
        }
    }

    @Test fun onClickEnterAdd_onNull() = startCoTest {
        val itemList = data.itemList
        val name = nextString()

        every { spyViewModel.getNameList(itemList) } returns emptyList()
        every { callback.clearEnter() } returns name
        coEvery { interactor.insert(name) } returns null

        spyViewModel.itemList.clearAdd(itemList)

        assertEquals(itemList, spyViewModel.itemList)

        spyViewModel.onClickEnterAdd(simpleClick = false)
        spyViewModel.onClickEnterAdd(simpleClick = true)

        assertEquals(itemList, spyViewModel.itemList)

        coVerifyOrder {
            repeat(times = 2) {
                callback.clearEnter()
                spyViewModel.getNameList(itemList)
                callback.dismissSnackbar()
                interactor.insert(name)
            }
        }
    }

    @Test fun onClickVisible() = startCoTest {
        viewModel.onClickVisible(Random.nextInt())

        val itemList = data.itemList

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList[p].switchVisible()

        viewModel.onClickVisible(p)

        coVerifySequence {
            callback.setList(itemList)

            interactor.update(item)
            callback.sendNotifyNotesBroadcast()
        }
    }

    @Test fun onLongClickVisible() = startCoTest {
        viewModel.onLongClickVisible(Random.nextInt())

        val itemList = data.itemList
        val p = itemList.indices.random()
        val animationArray = BooleanArray(size = 5) { Random.nextBoolean() }

        every { spyViewModel.switchVisible(itemList, p) } returns animationArray

        spyViewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, spyViewModel.itemList)

        spyViewModel.onLongClickVisible(p)

        coVerifyOrder {
            spyViewModel.switchVisible(itemList, p)
            callback.notifyDataSetChanged(itemList, animationArray)

            interactor.update(itemList)
            callback.sendNotifyNotesBroadcast()
        }
    }

    @Test fun onClickCancel() = startCoTest {
        viewModel.onClickCancel(Random.nextInt())

        val itemList = data.secondCorrectList
        val noteIdList = listOf(4L, 6, 1, 2)

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        assertEquals(itemList, viewModel.itemList)
        assertTrue(viewModel.cancelList.isEmpty())

        val p = 1
        val item = itemList.removeAt(p)

        every { correctPositions(itemList) } returns noteIdList

        viewModel.onClickCancel(p)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(mutableListOf(Pair(p, item)), viewModel.cancelList)

        coVerifySequence {
            correctPositions(itemList)

            callback.notifyItemRemoved(itemList, p)
            callback.showSnackbar()

            interactor.delete(item)
            interactor.updatePositions(itemList, noteIdList)

            callback.sendNotifyNotesBroadcast()
        }
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

        every { correctPositions(resultList) } returns noteIdList

        viewModel.itemList.clearAdd(itemList)
        viewModel.cancelList.clearAdd(cancelList)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        viewModel.onSnackbarAction()

        cancelList.removeAt(index = 1)

        assertEquals(resultList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coVerifySequence {
            callback.apply {
                notifyItemInsertedScroll(resultList, secondPair.first)
                showSnackbar()
            }

            interactor.insert(item)
            correctPositions(resultList)
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

        every { correctPositions(resultList) } returns noteIdList

        viewModel.itemList.clearAdd(itemList)
        viewModel.cancelList.clearAdd(cancelList)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        viewModel.onSnackbarAction()

        cancelList.removeAt(index = 0)

        assertEquals(resultList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coVerifyOrder {
            callback.notifyItemInsertedScroll(resultList, resultList.lastIndex)

            interactor.insert(item)
            correctPositions(resultList)
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

        every { correctPositions(resultList) } returns noteIdList

        viewModel.cancelList.clearAdd(cancelList)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        viewModel.onSnackbarAction()

        cancelList.removeAt(index = 0)

        assertEquals(resultList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coVerifyOrder {
            callback.notifyItemInsertedScroll(resultList, resultList.lastIndex)
            callback.onBindingList()

            interactor.insert(item)
            correctPositions(resultList)
            interactor.updatePositions(resultList, noteIdList)
            callback.setList(resultList)
            callback.sendNotifyNotesBroadcast()
        }
    }

    @Test fun onSnackbarDismiss() {
        val cancelList = MutableList(size = 5) { Pair(Random.nextInt(), mockk<RankItem>()) }

        viewModel.cancelList.clearAdd(cancelList)
        assertEquals(cancelList, viewModel.cancelList)

        viewModel.onSnackbarDismiss()

        assertTrue(viewModel.cancelList.isEmpty())
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

        viewModel.itemList.clearAdd(itemList)
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
        every { openState.value = true } returns Unit
        viewModel.onTouchAction(inAction = true)
        assertTrue(viewModel.inTouchAction)

        every { openState.value = false } returns Unit
        viewModel.onTouchAction(inAction = false)
        assertFalse(viewModel.inTouchAction)

        verifySequence {
            callback.dismissSnackbar()
            callback.openState
            openState.value = true

            callback.openState
            openState.value = false
        }
    }

    @Test fun onTouchGetDrag() {
        every { openState.value } returns false
        assertTrue(viewModel.onTouchGetDrag())

        every { openState.value } returns true
        assertFalse(viewModel.onTouchGetDrag())

        verifySequence {
            callback.openState
            openState.value
            callback.hideKeyboard()

            callback.openState
            openState.value
        }
    }

    @Test fun onTouchMove() {
        val itemList = data.itemList
        val from = Random.nextInt()
        val to = Random.nextInt()

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

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

        every { correctPositions(itemList) } returns noteIdList

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onTouchMoveResult()

        coVerifyOrder {
            callback.openState
            openState.clear()

            correctPositions(itemList)
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

    @Test fun switchVisible() = with(TestData.Rank) {
        var list = itemList
        var p = 0
        var animationArray = booleanArrayOf(false, false, true, false)

        assertArrayEquals(animationArray, viewModel.switchVisible(list, p))
        assertVisible(list, p)

        list = itemList
        p = 1
        animationArray = booleanArrayOf(true, true, true, false)

        assertArrayEquals(animationArray, viewModel.switchVisible(list, p))
        assertVisible(list, p)

        list = itemList
        p = 2
        animationArray = booleanArrayOf(true, false, false, false)

        assertArrayEquals(animationArray, viewModel.switchVisible(list, p))
        assertVisible(list, p)

        list = itemList
        p = 3
        animationArray = booleanArrayOf(true, false, true, true)

        assertArrayEquals(animationArray, viewModel.switchVisible(list, p))
        assertVisible(list, p)
    }

    private fun assertVisible(list: List<RankItem>, p: Int) {
        for ((i, item) in list.withIndex()) {
            assertEquals(i == p, item.isVisible)
        }
    }

    //endregion

}
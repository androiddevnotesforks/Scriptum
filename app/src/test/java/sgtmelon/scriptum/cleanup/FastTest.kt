package sgtmelon.scriptum.cleanup

import android.os.Bundle
import io.mockk.MockKVerificationScope
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verifyOrder
import io.mockk.verifySequence
import java.util.Calendar
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IParentNoteInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.state.NoteState
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.IParentNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.ParentNoteViewModel
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.note.INoteConnector
import sgtmelon.test.common.nextString

/**
 * Object for describe common and fast tests.
 */
@Deprecated("Repeat yourself - it's bad thing in you!")
object FastTest {

    // TODO create common interactor or anything else for remove some fast test functions (like date)

    class ViewModel<N : NoteItem, C : IParentNoteFragment<N>, I : IParentNoteInteractor<N>>(
        private val callback: IParentNoteFragment<N>,
        private val parentCallback: INoteConnector,
        private val colorConverter: ColorConverter,
        private val interactor: IParentNoteInteractor<N>,

        private val updateNote: UpdateNoteUseCase,
        private val deleteNote: DeleteNoteUseCase,
        private val restoreNote: RestoreNoteUseCase,
        private val clearNote: ClearNoteUseCase,
        private val setNotification: SetNotificationUseCase,
        private val deleteNotification: DeleteNotificationUseCase,
        private val getNotificationDateList: GetNotificationDateListUseCase,
        private val getRankId: GetRankIdUseCase,

        private val saveControl: SaveControl,
        private val inputControl: IInputControl,
        private val viewModel: ParentNoteViewModel<N, C, I>,
        private val spyViewModel: ParentNoteViewModel<N, C, I>,
        private val mockDeepCopy: (item: N) -> Unit,
        private val verifyDeepCopy: MockKVerificationScope.(item: N) -> Unit,
        private val mockkInit: () -> Color,
        private val verifyInit: () -> Unit
    ) {

        fun cacheData(noteItem: N) {
            mockkInit()

            mockDeepCopy(noteItem)

            spyViewModel.noteItem = noteItem
            spyViewModel.cacheData()

            verifySequence {
                verifyInit()

                spyViewModel.noteItem = noteItem
                spyViewModel.cacheData()

                spyViewModel.noteItem
                verifyDeepCopy(noteItem)
                spyViewModel.restoreItem = noteItem
            }
        }

        fun onSetup() {
            mockkInit()

            val bundle = mockk<Bundle>()

            every { spyViewModel.getBundleData(bundle) } returns Unit
            every { spyViewModel.setupBeforeInitialize() } returns Unit
            coEvery { spyViewModel.tryInitializeNote() } returns false

            spyViewModel.onSetup(bundle)

            coEvery { spyViewModel.tryInitializeNote() } returns true
            coEvery { spyViewModel.setupAfterInitialize() } returns Unit

            spyViewModel.onSetup(bundle)

            coVerifyOrder {
                verifyInit()

                spyViewModel.onSetup(bundle)
                spyViewModel.getBundleData(bundle)
                spyViewModel.setupBeforeInitialize()
                spyViewModel.tryInitializeNote()

                spyViewModel.onSetup(bundle)
                spyViewModel.getBundleData(bundle)
                spyViewModel.setupBeforeInitialize()
                spyViewModel.tryInitializeNote()
                spyViewModel.setupAfterInitialize()
            }
        }

        fun getBundleData() {
            val defaultColor = mockkInit()

            val bundle = mockk<Bundle>()
            val id = Random.nextLong()
            val bundleColor = Random.nextInt()
            val color = Color.values().random()

            every { colorConverter.toEnum(bundleColor) } returns null
            viewModel.getBundleData(bundle = null)

            assertEquals(viewModel.id, Note.Default.ID)
            assertEquals(viewModel.color, defaultColor)

            every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns id
            every { bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR) } returns bundleColor
            viewModel.getBundleData(bundle)

            assertEquals(viewModel.id, id)
            assertEquals(viewModel.color, defaultColor)

            every { colorConverter.toEnum(bundleColor) } returns color
            viewModel.getBundleData(bundle)

            assertEquals(viewModel.id, id)
            assertEquals(viewModel.color, color)

            verifySequence {
                verifyInit()

                colorConverter.toEnum(Note.Default.COLOR)

                repeat(times = 2) {
                    bundle.getLong(Note.Intent.ID, Note.Default.ID)
                    bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR)
                    colorConverter.toEnum(Note.Default.COLOR)
                }
            }
        }

        fun isNoteInitialized(noteItem: N) {
            mockkInit()

            assertFalse(viewModel.isNoteInitialized())
            viewModel.noteItem = noteItem
            assertTrue(viewModel.isNoteInitialized())

            verifySequence {
                verifyInit()
            }
        }

        fun onDestroy() {
            mockkInit()

            viewModel.onDestroy()

            assertNull(viewModel.callback)
            assertNull(viewModel.parentCallback)

            verifySequence {
                verifyInit()

                interactor.onDestroy()
                saveControl.changeAutoSaveWork(isWork = false)
            }
        }

        fun onSaveData() {
            mockkInit()

            val id = Random.nextLong()
            val color = Color.values().random()
            val ordinal = Random.nextInt()
            val bundle = mockk<Bundle>(relaxUnitFun = true)

            every { colorConverter.toInt(color) } returns ordinal

            viewModel.id = id
            viewModel.color = color
            viewModel.onSaveData(bundle)

            verifySequence {
                verifyInit()

                bundle.putLong(Note.Intent.ID, id)
                colorConverter.toInt(color)
                bundle.putInt(Note.Intent.COLOR, ordinal)
            }
        }

        fun onResume() {
            mockkInit()

            val noteState = mockk<NoteState>(relaxUnitFun = true)

            every { noteState.isEdit } returns false
            viewModel.noteState = noteState
            viewModel.onResume()

            every { noteState.isEdit } returns true
            viewModel.onResume()

            verifySequence {
                verifyInit()

                noteState.isEdit

                noteState.isEdit
                saveControl.changeAutoSaveWork(isWork = true)
            }
        }

        fun onPause() {
            mockkInit()

            val noteState = mockk<NoteState>(relaxUnitFun = true)

            every { parentCallback.isOrientationChanging() } returns true
            viewModel.onPause()

            every { parentCallback.isOrientationChanging() } returns false
            every { noteState.isEdit } returns false
            viewModel.noteState = noteState
            viewModel.onPause()

            every { noteState.isEdit } returns true
            viewModel.onPause()

            verifySequence {
                verifyInit()

                parentCallback.isOrientationChanging()

                parentCallback.isOrientationChanging()
                noteState.isEdit

                parentCallback.isOrientationChanging()
                noteState.isEdit
                saveControl.onPauseSave()
                saveControl.changeAutoSaveWork(isWork = false)
            }
        }


        fun onClickBackArrow() {
            mockkInit()

            val noteState = mockk<NoteState>(relaxUnitFun = true)
            val id = Random.nextLong()

            every { noteState.isCreate } returns true
            spyViewModel.noteState = noteState
            spyViewModel.onClickBackArrow()

            every { noteState.isCreate } returns false
            every { noteState.isEdit } returns false
            spyViewModel.onClickBackArrow()

            every { noteState.isEdit } returns true
            spyViewModel.onClickBackArrow()

            every { spyViewModel.onRestoreData() } returns Random.nextBoolean()
            spyViewModel.id = id
            spyViewModel.onClickBackArrow()

            verifySequence {
                verifyInit()

                spyViewModel.noteState = noteState

                spyViewModel.onClickBackArrow()
                noteState.isCreate
                spyViewModel.saveControl
                saveControl.isNeedSave = false
                parentCallback.finish()

                spyViewModel.onClickBackArrow()
                noteState.isCreate
                noteState.isEdit
                spyViewModel.saveControl
                saveControl.isNeedSave = false
                parentCallback.finish()

                spyViewModel.onClickBackArrow()
                noteState.isCreate
                noteState.isEdit
                spyViewModel.saveControl
                saveControl.isNeedSave = false
                parentCallback.finish()

                spyViewModel.id = id
                spyViewModel.onClickBackArrow()
                noteState.isCreate
                noteState.isEdit
                spyViewModel.callback
                callback.hideKeyboard()
                spyViewModel.onRestoreData()
            }
        }

        fun onPressBack() {
            mockkInit()

            val noteState = mockk<NoteState>(relaxUnitFun = true)

            every { noteState.isEdit } returns false

            spyViewModel.noteState = noteState
            assertFalse(spyViewModel.onPressBack())

            val restoreResult = Random.nextBoolean()

            every { noteState.isEdit } returns true
            every { spyViewModel.onMenuSave(changeMode = true) } returns false
            every { noteState.isCreate } returns false
            every { spyViewModel.onRestoreData() } returns restoreResult

            assertEquals(restoreResult, spyViewModel.onPressBack())

            every { noteState.isCreate } returns true
            assertFalse(spyViewModel.onPressBack())

            every { spyViewModel.onMenuSave(changeMode = true) } returns true
            assertTrue(spyViewModel.onPressBack())

            verifyOrder {
                verifyInit()

                spyViewModel.onPressBack()
                noteState.isEdit

                spyViewModel.onPressBack()
                noteState.isEdit
                spyViewModel.saveControl
                saveControl.isNeedSave = false
                spyViewModel.onMenuSave(changeMode = true)
                noteState.isCreate
                spyViewModel.onRestoreData()

                spyViewModel.onPressBack()
                noteState.isEdit
                spyViewModel.saveControl
                saveControl.isNeedSave = false
                spyViewModel.onMenuSave(changeMode = true)
                noteState.isCreate

                spyViewModel.onPressBack()
                noteState.isEdit
                spyViewModel.saveControl
                saveControl.isNeedSave = false
                spyViewModel.onMenuSave(changeMode = true)
            }
        }


        fun onResultColorDialog(noteItem: N) {
            mockkInit()

            val check = Random.nextInt()
            val oldColor = mockk<Color>()
            val newColor = mockk<Color>()
            val access = mockk<InputControl.Access>()

            every { colorConverter.toEnum(check) } returns null

            viewModel.onResultColorDialog(check)

            every { colorConverter.toEnum(check) } returns newColor
            every { noteItem.color } returns oldColor
            every { noteItem.color = newColor } returns Unit
            every { inputControl.access } returns access

            viewModel.noteItem = noteItem
            viewModel.onResultColorDialog(check)

            verifySequence {
                verifyInit()

                colorConverter.toEnum(check)

                colorConverter.toEnum(check)
                noteItem.color
                inputControl.onColorChange(oldColor, newColor)
                noteItem.color = newColor

                inputControl.access
                callback.onBindingInput(noteItem, access)
                callback.tintToolbar(newColor)
            }
        }

        fun onResultRankDialog(noteItem: N) {
            mockkInit()

            val oldRankId = Random.nextLong()
            val oldRankPs = Random.nextInt()
            val newRankId = Random.nextLong()
            val newRankPs = Random.nextInt()

            val access = mockk<InputControl.Access>()

            coEvery { getRankId(newRankPs) } returns newRankId
            every { noteItem.rankId } returns oldRankId
            every { noteItem.rankPs } returns oldRankPs
            every { noteItem.rankId = newRankId } returns Unit
            every { noteItem.rankPs = newRankPs } returns Unit
            every { inputControl.access } returns access

            viewModel.noteItem = noteItem
            viewModel.onResultRankDialog(newRankPs)

            coVerifySequence {
                verifyInit()

                getRankId(newRankPs)

                noteItem.rankId
                noteItem.rankPs
                inputControl.onRankChange(oldRankId, oldRankPs, newRankId, newRankPs)

                noteItem.rankId = newRankId
                noteItem.rankPs = newRankPs

                inputControl.access
                callback.onBindingInput(noteItem, access)
                callback.onBindingNote(noteItem)
            }
        }

        fun onResultDateDialog() {
            mockkInit()

            val calendar = mockk<Calendar>()
            val dateList = mockk<List<String>>()

            coEvery { getNotificationDateList() } returns dateList

            viewModel.onResultDateDialog(calendar)

            coVerifySequence {
                verifyInit()

                getNotificationDateList()
                callback.showTimeDialog(calendar, dateList)
            }
        }

        fun onResultDateDialogClear(noteItem: N, restoreItem: N) {
            mockkInit()

            every { noteItem.clearAlarm() } returns noteItem
            every { spyViewModel.cacheData() } returns Unit

            spyViewModel.noteItem = noteItem
            spyViewModel.restoreItem = restoreItem
            spyViewModel.onResultDateDialogClear()

            coVerifyOrder {
                verifyInit()

                spyViewModel.noteItem = noteItem
                spyViewModel.restoreItem = restoreItem
                spyViewModel.onResultDateDialogClear()

                spyViewModel.interactor
                spyViewModel.noteItem
                deleteNotification(noteItem)

                spyViewModel.callback
                spyViewModel.noteItem
                callback.sendCancelAlarmBroadcast(noteItem)
                spyViewModel.callback
                callback.sendNotifyInfoBroadcast()

                noteItem.clearAlarm()
                spyViewModel.cacheData()

                spyViewModel.callback
                callback.onBindingNote(noteItem)
            }
        }

        fun onResultTimeDialog(noteItem: N, restoreItem: N) {
            mockkInit()

            val calendar = mockk<Calendar>()
            val id = Random.nextLong()

            FastMock.timeExtension()
            every { calendar.isBeforeNow() } returns true
            every { spyViewModel.cacheData() } returns Unit
            every { noteItem.id } returns id

            spyViewModel.noteItem = noteItem
            spyViewModel.restoreItem = restoreItem
            spyViewModel.onResultTimeDialog(calendar)

            every { calendar.isBeforeNow() } returns false

            spyViewModel.onResultTimeDialog(calendar)

            coVerifyOrder {
                verifyInit()

                spyViewModel.noteItem = noteItem
                spyViewModel.restoreItem = restoreItem
                spyViewModel.onResultTimeDialog(calendar)
                calendar.isBeforeNow()

                spyViewModel.onResultTimeDialog(calendar)
                calendar.isBeforeNow()
                spyViewModel.interactor
                spyViewModel.noteItem
                setNotification(noteItem, calendar)
                spyViewModel.cacheData()
                spyViewModel.callback
                spyViewModel.noteItem
                callback.onBindingNote(noteItem)
                spyViewModel.callback
                spyViewModel.noteItem
                noteItem.id
                callback.sendSetAlarmBroadcast(id, calendar)
                spyViewModel.callback
                callback.sendNotifyInfoBroadcast()
            }
        }

        fun onResultConvertDialog(noteItem: N) {
            mockkInit()

            viewModel.noteItem = noteItem
            viewModel.onResultConvertDialog()

            coVerifySequence {
                verifyInit()

                interactor.convertNote(noteItem)
                parentCallback.onConvertNote()
            }
        }


        fun onReceiveUnbindNote(noteItem: N, restoreItem: N) {
            mockkInit()

            viewModel.onReceiveUnbindNote(Random.nextLong())

            val id = Random.nextLong()

            every { noteItem.isStatus = false } returns Unit
            every { restoreItem.isStatus = false } returns Unit

            viewModel.id = id
            viewModel.noteItem = noteItem
            viewModel.restoreItem = restoreItem

            viewModel.onReceiveUnbindNote(id)

            verifySequence {
                verifyInit()

                noteItem.isStatus = false
                restoreItem.isStatus = false

                callback.onBindingNote(noteItem)
            }
        }


        fun onMenuRestore(noteItem: N) {
            mockkInit()

            viewModel.noteItem = noteItem
            viewModel.onMenuRestore()

            coVerifySequence {
                verifyInit()

                restoreNote(noteItem)
                parentCallback.finish()
            }
        }

        fun onMenuRestoreOpen(noteItem: N) {
            mockkInit()

            val noteState = mockk<NoteState>(relaxUnitFun = true)

            every { noteItem.onRestore() } returns noteItem
            every { spyViewModel.setupEditMode(isEdit = false) } returns Unit

            spyViewModel.noteState = noteState
            spyViewModel.noteItem = noteItem
            spyViewModel.onMenuRestoreOpen()

            coVerifyOrder {
                verifyInit()

                noteState.isBin = false
                noteItem.onRestore()
                spyViewModel.setupEditMode(isEdit = false)
                updateNote(noteItem)
            }
        }

        fun onMenuClear(noteItem: N) {
            mockkInit()

            viewModel.noteItem = noteItem
            viewModel.onMenuClear()

            coVerifySequence {
                verifyInit()

                clearNote(noteItem)
                parentCallback.finish()
            }
        }

        fun onMenuUndo() {
            mockkInit()

            every { spyViewModel.onMenuUndoRedo(isUndo = true) } returns Unit

            spyViewModel.onMenuUndo()

            verifySequence {
                verifyInit()

                spyViewModel.onMenuUndo()
                spyViewModel.onMenuUndoRedo(isUndo = true)
            }
        }

        fun onMenuRedo() {
            mockkInit()

            every { spyViewModel.onMenuUndoRedo(isUndo = false) } returns Unit

            spyViewModel.onMenuRedo()

            verifySequence {
                verifyInit()

                spyViewModel.onMenuRedo()
                spyViewModel.onMenuUndoRedo(isUndo = false)
            }
        }

        fun onMenuUndoRedo(noteItem: N) {
            mockkInit()

            val noteState = mockk<NoteState>()
            val isUndo = Random.nextBoolean()
            val item = mockk<InputItem>()
            val access = mockk<InputControl.Access>()

            spyViewModel.noteItem = noteItem
            spyViewModel.noteState = noteState

            every { callback.isDialogOpen } returns true
            every { noteState.isEdit } returns false
            spyViewModel.onMenuUndoRedo(isUndo)

            every { callback.isDialogOpen } returns true
            every { noteState.isEdit } returns true
            spyViewModel.onMenuUndoRedo(isUndo)

            every { callback.isDialogOpen } returns false
            every { noteState.isEdit } returns false
            spyViewModel.onMenuUndoRedo(isUndo)

            every { callback.isDialogOpen } returns false
            every { noteState.isEdit } returns true
            every { if (isUndo) inputControl.undo() else inputControl.redo() } returns null
            every { inputControl.access } returns access
            spyViewModel.onMenuUndoRedo(isUndo)

            every { if (isUndo) inputControl.undo() else inputControl.redo() } returns item
            every { spyViewModel.onMenuUndoRedoSelect(item, isUndo) } returns Unit
            spyViewModel.onMenuUndoRedo(isUndo)

            coVerifySequence {
                verifyInit()

                spyViewModel.noteItem = noteItem
                spyViewModel.noteState = noteState

                spyViewModel.onMenuUndoRedo(isUndo)
                spyViewModel.callback
                callback.isDialogOpen

                spyViewModel.onMenuUndoRedo(isUndo)
                spyViewModel.callback
                callback.isDialogOpen

                spyViewModel.onMenuUndoRedo(isUndo)
                spyViewModel.callback
                callback.isDialogOpen
                noteState.isEdit

                spyViewModel.onMenuUndoRedo(isUndo)
                spyViewModel.callback
                callback.isDialogOpen
                noteState.isEdit
                if (isUndo) inputControl.undo() else inputControl.redo()
                spyViewModel.callback
                inputControl.access
                callback.onBindingInput(noteItem, access)

                spyViewModel.onMenuUndoRedo(isUndo)
                spyViewModel.callback
                callback.isDialogOpen
                noteState.isEdit
                if (isUndo) inputControl.undo() else inputControl.redo()
                spyViewModel.onMenuUndoRedoSelect(item, isUndo)
                spyViewModel.callback
                inputControl.access
                callback.onBindingInput(noteItem, access)
            }
        }

        fun onMenuUndoRedoRank(noteItem: N) {
            mockkInit()

            val item = mockk<InputItem>(relaxUnitFun = true)
            val isUndo = Random.nextBoolean()

            val rankId = Random.nextLong()
            val rankPs = Random.nextLong()
            val text = "$rankId, $rankPs"

            viewModel.noteItem = noteItem

            every { item[isUndo] } returns nextString()
            viewModel.onMenuUndoRedoRank(item, isUndo)

            every { item[isUndo] } returns text
            viewModel.onMenuUndoRedoRank(item, isUndo)

            verifySequence {
                verifyInit()

                item[isUndo]

                item[isUndo]
                noteItem.rankId = rankId
                noteItem.rankPs = rankPs.toInt()
            }
        }

        fun onMenuUndoRedoColor(noteItem: N) {
            mockkInit()

            val item = mockk<InputItem>(relaxUnitFun = true)
            val isUndo = Random.nextBoolean()

            val ordinal = Random.nextInt()
            val colorFrom = mockk<Color>()
            val colorTo = mockk<Color>()

            every { noteItem.color } returns colorFrom
            every { noteItem.color = colorTo } returns Unit

            viewModel.noteItem = noteItem

            every { item[isUndo] } returns nextString()
            viewModel.onMenuUndoRedoColor(item, isUndo)

            every { item[isUndo] } returns ordinal.toString()
            every { colorConverter.toEnum(ordinal) } returns null
            viewModel.onMenuUndoRedoColor(item, isUndo)

            every { colorConverter.toEnum(ordinal) } returns colorTo
            viewModel.onMenuUndoRedoColor(item, isUndo)

            verifySequence {
                verifyInit()

                noteItem.color
                item[isUndo]

                noteItem.color
                item[isUndo]
                colorConverter.toEnum(ordinal)

                noteItem.color
                item[isUndo]
                colorConverter.toEnum(ordinal)
                noteItem.color = colorTo
                callback.tintToolbar(colorFrom, colorTo)
            }
        }

        fun onMenuUndoRedoName() {
            mockkInit()

            val item = mockk<InputItem>(relaxUnitFun = true)
            val isUndo = Random.nextBoolean()

            val text = nextString()
            val position = Random.nextInt()
            val cursor = mockk<InputItem.Cursor>(relaxUnitFun = true)

            mockkObject(InputItem.Cursor)

            every { item[isUndo] } returns text
            every { item.cursor } returns cursor
            every { cursor[isUndo] } returns position

            viewModel.onMenuUndoRedoName(item, isUndo)

            verifySequence {
                verifyInit()

                item[isUndo]
                item.cursor
                cursor[isUndo]
                callback.changeName(text, position)
            }
        }

        fun onMenuRank(noteItem: N) {
            mockkInit()

            val rankPs = Random.nextInt()

            val noteState = mockk<NoteState>()

            every { noteItem.rankPs } returns rankPs

            viewModel.noteItem = noteItem
            viewModel.noteState = noteState

            every { noteState.isEdit } returns false
            viewModel.onMenuRank()

            every { noteState.isEdit } returns true
            viewModel.onMenuRank()

            verifySequence {
                verifyInit()

                noteState.isEdit

                noteState.isEdit
                noteItem.rankPs
                callback.showRankDialog(check = rankPs + 1)
            }
        }

        fun onMenuColor(noteItem: N) {
            mockkInit()

            val color = mockk<Color>()

            val noteState = mockk<NoteState>()

            every { noteItem.color } returns color

            viewModel.noteItem = noteItem
            viewModel.noteState = noteState

            every { noteState.isEdit } returns false
            viewModel.onMenuColor()

            every { noteState.isEdit } returns true
            viewModel.onMenuColor()

            verifySequence {
                verifyInit()

                noteState.isEdit

                noteState.isEdit
                noteItem.color
                callback.showColorDialog(color)
            }
        }

        fun onMenuNotification(noteItem: N) {
            mockkInit()

            val alarmDate = nextString()
            val haveAlarm = Random.nextBoolean()
            val calendar = mockk<Calendar>()

            val noteState = mockk<NoteState>()

            every { noteItem.alarmDate } returns alarmDate
            every { noteItem.haveAlarm() } returns haveAlarm

            FastMock.timeExtension()
            every { alarmDate.toCalendar() } returns calendar

            viewModel.noteItem = noteItem
            viewModel.noteState = noteState

            every { noteState.isEdit } returns false
            viewModel.onMenuNotification()

            every { noteState.isEdit } returns true
            viewModel.onMenuNotification()

            verifySequence {
                verifyInit()

                noteState.isEdit

                noteItem.alarmDate
                alarmDate.toCalendar()
                noteItem.haveAlarm()
                callback.showDateDialog(calendar, haveAlarm)

                noteState.isEdit
            }
        }

        fun onMenuBind(noteItem: N, restoreItem: N) {
            TODO()
            //            mockkInit()
            //
            //            val noteState = mockk<NoteState>()
            //
            //            every { noteItem.switchStatus() } returns noteItem
            //            mockDeepCopy(noteItem)
            //
            //            viewModel.noteItem = noteItem
            //            viewModel.restoreItem = restoreItem
            //            viewModel.noteState = noteState
            //
            //            every { callback.isDialogOpen } returns true
            //            every { noteState.isEdit } returns true
            //            viewModel.onMenuBind()
            //
            //            assertEquals(restoreItem, viewModel.restoreItem)
            //
            //            every { callback.isDialogOpen } returns true
            //            every { noteState.isEdit } returns false
            //            viewModel.onMenuBind()
            //
            //            assertEquals(restoreItem, viewModel.restoreItem)
            //
            //            every { callback.isDialogOpen } returns false
            //            every { noteState.isEdit } returns true
            //            viewModel.onMenuBind()
            //
            //            assertEquals(restoreItem, viewModel.restoreItem)
            //
            //            every { callback.isDialogOpen } returns false
            //            every { noteState.isEdit } returns false
            //            viewModel.onMenuBind()
            //
            //            coVerifySequence {
            //                verifyInit()
            //
            //                callback.isDialogOpen
            //                callback.isDialogOpen
            //                callback.isDialogOpen
            //                noteState.isEdit
            //
            //                callback.isDialogOpen
            //                noteState.isEdit
            //                noteItem.switchStatus()
            //                verifyDeepCopy(noteItem)
            //                noteState.isEdit
            //                callback.onBindingEdit(noteItem, isEditMode = false)
            //                updateNote(noteItem)
            //                callback.sendNotifyNotesBroadcast()
            //            }
            //
            //            assertEquals(noteItem, viewModel.restoreItem)
        }

        fun onMenuConvert() {
            mockkInit()

            val noteState = mockk<NoteState>()

            viewModel.noteState = noteState

            every { noteState.isEdit } returns false
            viewModel.onMenuConvert()

            every { noteState.isEdit } returns true
            viewModel.onMenuConvert()

            verifySequence {
                verifyInit()

                noteState.isEdit
                callback.showConvertDialog()

                noteState.isEdit
            }
        }

        fun onMenuDelete(noteItem: N) {
            mockkInit()

            val noteState = mockk<NoteState>()

            viewModel.noteItem = noteItem
            viewModel.noteState = noteState

            every { callback.isDialogOpen } returns true
            every { noteState.isEdit } returns true
            viewModel.onMenuDelete()

            every { callback.isDialogOpen } returns true
            every { noteState.isEdit } returns false
            viewModel.onMenuDelete()

            every { callback.isDialogOpen } returns false
            every { noteState.isEdit } returns true
            viewModel.onMenuDelete()

            every { callback.isDialogOpen } returns false
            every { noteState.isEdit } returns false
            viewModel.onMenuDelete()

            coVerifySequence {
                verifyInit()

                callback.isDialogOpen
                callback.isDialogOpen
                callback.isDialogOpen
                noteState.isEdit

                callback.isDialogOpen
                noteState.isEdit
                deleteNote(noteItem)
                callback.sendCancelAlarmBroadcast(noteItem)
                callback.sendCancelNoteBroadcast(noteItem)
                callback.sendNotifyInfoBroadcast()
                parentCallback.finish()
            }
        }

        fun onMenuEdit() {
            mockkInit()

            val noteState = mockk<NoteState>()

            every { spyViewModel.setupEditMode(isEdit = true) } returns Unit

            spyViewModel.noteState = noteState

            every { callback.isDialogOpen } returns true
            every { noteState.isEdit } returns true
            spyViewModel.onMenuEdit()

            every { callback.isDialogOpen } returns true
            every { noteState.isEdit } returns false
            spyViewModel.onMenuEdit()

            every { callback.isDialogOpen } returns false
            every { noteState.isEdit } returns true
            spyViewModel.onMenuEdit()

            every { callback.isDialogOpen } returns false
            every { noteState.isEdit } returns false
            spyViewModel.onMenuEdit()

            verifySequence {
                verifyInit()

                spyViewModel.noteState = noteState

                spyViewModel.onMenuEdit()
                spyViewModel.callback
                callback.isDialogOpen

                spyViewModel.onMenuEdit()
                spyViewModel.callback
                callback.isDialogOpen

                spyViewModel.onMenuEdit()
                spyViewModel.callback
                callback.isDialogOpen
                noteState.isEdit

                spyViewModel.onMenuEdit()
                spyViewModel.callback
                callback.isDialogOpen
                noteState.isEdit
                spyViewModel.setupEditMode(isEdit = true)
            }
        }


        fun onResultSaveControl() {
            mockkInit()

            val saveResult = Random.nextBoolean()

            every { spyViewModel.onMenuSave(changeMode = false) } returns saveResult

            spyViewModel.onResultSaveControl()

            verifySequence {
                verifyInit()

                spyViewModel.onResultSaveControl()

                spyViewModel.callback
                spyViewModel.onMenuSave(changeMode = false)
                callback.showSaveToast(saveResult)
            }
        }

        fun onInputTextChange(noteItem: N) {
            mockkInit()

            val access = mockk<InputControl.Access>()

            every { inputControl.access } returns access

            every { spyViewModel.isNoteInitialized() } returns false
            spyViewModel.onInputTextChange()

            every { spyViewModel.isNoteInitialized() } returns true
            spyViewModel.noteItem = noteItem
            spyViewModel.onInputTextChange()

            verifySequence {
                verifyInit()

                spyViewModel.onInputTextChange()
                spyViewModel.isNoteInitialized()

                spyViewModel.noteItem = noteItem
                spyViewModel.onInputTextChange()
                spyViewModel.isNoteInitialized()
                spyViewModel.callback
                spyViewModel.noteItem
                inputControl.access
                callback.onBindingInput(noteItem, access)
            }
        }

    }

    object Interactor {

        //region Note

        suspend inline fun <reified T : NoteItem> saveNote(
            noteRepo: NoteRepo,
            rankRepo: RankRepo,
            callFunc: (item: T, isCreate: Boolean) -> Unit
        ) {
            val item = mockk<T>()
            val isCreate = Random.nextBoolean()

            callFunc(item, isCreate)

            coVerifySequence {
                when (item) {
                    is NoteItem.Text -> noteRepo.saveNote(item, isCreate)
                    is NoteItem.Roll -> noteRepo.saveNote(item, isCreate)
                }

                rankRepo.updateConnection(item)
            }
        }

        //endregion

    }

}
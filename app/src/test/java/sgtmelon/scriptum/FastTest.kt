package sgtmelon.scriptum

import android.os.Bundle
import io.mockk.*
import org.junit.Assert.assertEquals
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.extension.nextString
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IParentNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.data.NoteData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.state.IconState
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IParentNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.note.ParentNoteViewModel
import java.util.*
import kotlin.random.Random

/**
 * Object for describe common and fast tests.
 */
object FastTest {

    object Note {

        object Interactor {

            fun getSaveModel(preferenceRepo: IPreferenceRepo, callFun: () -> SaveControl.Model) {
                val model = with(Random) { SaveControl.Model(nextBoolean(), nextBoolean(), nextInt()) }

                every { preferenceRepo.pauseSaveOn } returns model.pauseSaveOn
                every { preferenceRepo.autoSaveOn } returns model.autoSaveOn
                every { preferenceRepo.savePeriod } returns model.savePeriod
                assertEquals(model, callFun())

                verifySequence {
                    preferenceRepo.pauseSaveOn
                    preferenceRepo.autoSaveOn
                    preferenceRepo.savePeriod
                }
            }

        }

        // TODO make good order
        class ViewModel<N : NoteItem, C : IParentNoteFragment<N>, I : IParentNoteInteractor<N>>(
                private val callback: IParentNoteFragment<N>,
                private val parentCallback: INoteConnector,
                private val interactor: IParentNoteInteractor<N>,
                private val bindInteractor: IBindInteractor,
                private val inputControl: IInputControl,
                private val iconState: IconState,
                private val viewModel: ParentNoteViewModel<N, C, I>,
                private val mockDeepCopy: (item: N) -> Unit,
                private val verifyDeepCopy: MockKVerificationScope.(item: N) -> Unit
        ) {

            fun onSaveData() {
                val id = Random.nextLong()
                val color = Random.nextInt()
                val bundle = mockk<Bundle>()

                every { bundle.putLong(NoteData.Intent.ID, id) } returns Unit
                every { bundle.putInt(NoteData.Intent.COLOR, color) } returns Unit

                viewModel.id = id
                viewModel.color = color
                viewModel.onSaveData(bundle)

                verifySequence {
                    bundle.putLong(NoteData.Intent.ID, id)
                    bundle.putInt(NoteData.Intent.COLOR, color)
                }
            }


            fun onResultColorDialog(noteItem: N) {
                val oldColor = Random.nextInt()
                val newColor = Random.nextInt()
                val access = mockk<InputControl.Access>()

                every { noteItem.color } returns oldColor
                every { noteItem.color = newColor } returns Unit
                every { inputControl.access } returns access

                viewModel.noteItem = noteItem
                viewModel.onResultColorDialog(newColor)

                verifySequence {
                    noteItem.color
                    inputControl.onColorChange(oldColor, newColor)
                    noteItem.color = newColor

                    inputControl.access
                    callback.onBindingInput(noteItem, access)
                    callback.tintToolbar(newColor)
                }
            }

            fun onResultRankDialog(noteItem: N) {
                val oldRankId = Random.nextLong()
                val oldRankPs = Random.nextInt()
                val newRankId = Random.nextLong()
                val newRankPs = Random.nextInt()

                val access = mockk<InputControl.Access>()

                coEvery { interactor.getRankId(newRankPs) } returns newRankId
                every { noteItem.rankId } returns oldRankId
                every { noteItem.rankPs } returns oldRankPs
                every { noteItem.rankId = newRankId } returns Unit
                every { noteItem.rankPs = newRankPs } returns Unit
                every { inputControl.access } returns access

                viewModel.noteItem = noteItem
                viewModel.onResultRankDialog(newRankPs)

                coVerifySequence {
                    interactor.getRankId(newRankPs)

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
                val calendar = mockk<Calendar>()
                val dateList = mockk<List<String>>()

                coEvery { interactor.getDateList() } returns dateList

                viewModel.onResultDateDialog(calendar)

                coVerifySequence {
                    interactor.getDateList()
                    callback.showTimeDialog(calendar, dateList)
                }
            }

            fun onResultDateDialogClear(noteItem: N, restoreItem: N) {
                every { noteItem.clearAlarm() } returns noteItem
                mockDeepCopy(noteItem)

                viewModel.noteItem = noteItem
                viewModel.restoreItem = restoreItem

                viewModel.onResultDateDialogClear()

                coVerifySequence {
                    interactor.clearDate(noteItem)
                    bindInteractor.notifyInfoBind(callback)

                    noteItem.clearAlarm()
                    verifyDeepCopy(noteItem)

                    callback.onBindingNote(noteItem)
                }

                assertEquals(noteItem, viewModel.restoreItem)
            }

            fun onResultTimeDialog(noteItem: N, restoreItem: N) {
                val calendar = mockk<Calendar>()

                FastMock.timeExtension()
                mockDeepCopy(noteItem)

                viewModel.noteItem = noteItem
                viewModel.restoreItem = restoreItem

                every { calendar.beforeNow() } returns true
                viewModel.onResultTimeDialog(calendar)

                every { calendar.beforeNow() } returns false
                viewModel.onResultTimeDialog(calendar)

                coVerifySequence {
                    calendar.beforeNow()

                    calendar.beforeNow()
                    interactor.setDate(noteItem, calendar)
                    verifyDeepCopy(noteItem)
                    callback.onBindingNote(noteItem)
                    bindInteractor.notifyInfoBind(callback)
                }

                assertEquals(noteItem, viewModel.restoreItem)
            }

            fun onResultConvertDialog(noteItem: N) {
                viewModel.noteItem = noteItem
                viewModel.onResultConvertDialog()

                coVerifySequence {
                    interactor.convertNote(noteItem)
                    parentCallback.onConvertNote()
                }
            }


            fun onReceiveUnbindNote(noteItem: N, restoreItem: N) {
                viewModel.onReceiveUnbindNote(Random.nextLong())

                val id = Random.nextLong()

                every { noteItem.isStatus = false } returns Unit
                every { restoreItem.isStatus = false } returns Unit

                viewModel.id = id
                viewModel.noteItem = noteItem
                viewModel.restoreItem = restoreItem

                viewModel.onReceiveUnbindNote(id)

                verifySequence {
                    noteItem.isStatus = false
                    restoreItem.isStatus = false

                    callback.onBindingNote(noteItem)
                }
            }


            fun onMenuRestore(noteItem: N) {
                viewModel.noteItem = noteItem
                viewModel.onMenuRestore()

                coVerifySequence {
                    interactor.restoreNote(noteItem)
                    parentCallback.finish()
                }
            }

            fun onMenuRestoreOpen() {
                TODO()
            }

            fun onMenuClear(noteItem: N) {
                viewModel.noteItem = noteItem
                viewModel.onMenuClear()

                coVerifySequence {
                    interactor.clearNote(noteItem)
                    parentCallback.finish()
                }
            }

            fun onMenuUndo() {
                TODO()
            }

            fun onMenuRedo() {
                TODO()
            }

            fun onMenuRank(noteItem: N) {
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
                    noteState.isEdit

                    noteState.isEdit
                    noteItem.rankPs
                    callback.showRankDialog(check = rankPs + 1)
                }
            }

            fun onMenuColor(noteItem: N) {
                val color = Random.nextInt()
                val theme = Random.nextInt()

                val noteState = mockk<NoteState>()

                every { noteItem.color } returns color
                every { interactor.theme } returns theme

                viewModel.noteItem = noteItem
                viewModel.noteState = noteState

                every { noteState.isEdit } returns false
                viewModel.onMenuColor()

                every { noteState.isEdit } returns true
                viewModel.onMenuColor()

                verifySequence {
                    noteState.isEdit

                    noteState.isEdit
                    noteItem.color
                    interactor.theme
                    callback.showColorDialog(color, theme)
                }
            }

            fun onMenuSave() {
                TODO()
            }

            fun onMenuNotification(noteItem: N) {
                val alarmDate = Random.nextString()
                val haveAlarm = Random.nextBoolean()
                val calendar = mockk<Calendar>()

                val noteState = mockk<NoteState>()

                every { noteItem.alarmDate } returns alarmDate
                every { noteItem.haveAlarm() } returns haveAlarm

                FastMock.timeExtension()
                every { alarmDate.getCalendar() } returns calendar

                viewModel.noteItem = noteItem
                viewModel.noteState = noteState

                every { noteState.isEdit } returns false
                viewModel.onMenuNotification()

                every { noteState.isEdit } returns true
                viewModel.onMenuNotification()

                verifySequence {
                    noteState.isEdit

                    noteItem.alarmDate
                    alarmDate.getCalendar()
                    noteItem.haveAlarm()
                    callback.showDateDialog(calendar, haveAlarm)

                    noteState.isEdit
                }
            }

            fun onMenuBind(noteItem: N, restoreItem: N) {
                val noteState = mockk<NoteState>()

                every { noteItem.switchStatus() } returns noteItem
                mockDeepCopy(noteItem)

                viewModel.noteItem = noteItem
                viewModel.restoreItem = restoreItem
                viewModel.noteState = noteState

                every { callback.isDialogOpen } returns true
                every { noteState.isEdit } returns true
                viewModel.onMenuBind()

                assertEquals(restoreItem, viewModel.restoreItem)

                every { callback.isDialogOpen } returns true
                every { noteState.isEdit } returns false
                viewModel.onMenuBind()

                assertEquals(restoreItem, viewModel.restoreItem)

                every { callback.isDialogOpen } returns false
                every { noteState.isEdit } returns true
                viewModel.onMenuBind()

                assertEquals(restoreItem, viewModel.restoreItem)

                every { callback.isDialogOpen } returns false
                every { noteState.isEdit } returns false
                viewModel.onMenuBind()

                coVerifySequence {
                    callback.isDialogOpen
                    callback.isDialogOpen
                    callback.isDialogOpen
                    noteState.isEdit

                    callback.isDialogOpen
                    noteState.isEdit
                    noteItem.switchStatus()
                    verifyDeepCopy(noteItem)
                    noteState.isEdit
                    callback.onBindingEdit(noteItem, isEditMode = false)
                    interactor.updateNote(noteItem, updateBind = true)
                }

                assertEquals(noteItem, viewModel.restoreItem)
            }

            fun onMenuConvert() {
                val noteState = mockk<NoteState>()

                viewModel.noteState = noteState

                every { noteState.isEdit } returns false
                viewModel.onMenuConvert()

                every { noteState.isEdit } returns true
                viewModel.onMenuConvert()

                verifySequence {
                    noteState.isEdit
                    callback.showConvertDialog()

                    noteState.isEdit
                }
            }

            fun onMenuDelete(noteItem: N) {
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
                    callback.isDialogOpen
                    callback.isDialogOpen
                    callback.isDialogOpen
                    noteState.isEdit

                    callback.isDialogOpen
                    noteState.isEdit
                    interactor.deleteNote(noteItem)
                    bindInteractor.notifyInfoBind(callback)
                    parentCallback.finish()
                }
            }

            fun onMenuEdit() {
                TODO()
            }


            fun onResultSaveControl() {
                TODO()
            }

            fun onInputTextChange(noteItem: N) {
                val access = mockk<InputControl.Access>()

                every { inputControl.access } returns access

                viewModel.noteItem = noteItem
                viewModel.onInputTextChange()

                verifySequence {
                    inputControl.access
                    callback.onBindingInput(noteItem, access)
                }
            }

        }

    }

    fun getFirstStart(preferenceRepo: IPreferenceRepo, callFun: () -> Boolean) {
        fun checkRequestGet(value: Boolean) {
            every { preferenceRepo.firstStart } returns value
            assertEquals(callFun(), value)
        }

        val valueList = listOf(Random.nextBoolean(), Random.nextBoolean())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.firstStart }
        }
    }

    fun getTheme(preferenceRepo: IPreferenceRepo, callFun: () -> Int) {
        fun checkRequestGet(value: Int) {
            every { preferenceRepo.theme } returns value
            assertEquals(callFun(), value)
        }

        val valueList = listOf(Theme.LIGHT, Theme.DARK, Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.theme }
        }
    }

    fun getSort(preferenceRepo: IPreferenceRepo, callFun: () -> Int) {
        fun checkRequestGet(value: Int) {
            every { preferenceRepo.sort } returns value
            assertEquals(callFun(), value)
        }

        val valueList = listOf(Sort.CHANGE, Sort.RANK, Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.sort }
        }
    }

    fun getDefaultColor(preferenceRepo: IPreferenceRepo, callFun: () -> Int) {
        fun checkRequestGet(value: Int) {
            every { preferenceRepo.defaultColor } returns value
            assertEquals(callFun(), value)
        }

        val valueList = listOf(Color.RED, Color.PURPLE, Color.INDIGO, Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.defaultColor }
        }
    }

    fun getRepeat(preferenceRepo: IPreferenceRepo, callFun: () -> Int) {
        fun checkRequestGet(value: Int) {
            every { preferenceRepo.repeat } returns value
            assertEquals(callFun(), value)
        }

        val valueList = listOf(Repeat.MIN_10, Repeat.MIN_180, Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.repeat }
        }
    }

    fun getVolume(preferenceRepo: IPreferenceRepo, callFun: () -> Int) {
        fun checkRequestGet(value: Int) {
            every { preferenceRepo.volume } returns value
            assertEquals(callFun(), value)
        }

        val valueList = listOf(Random.nextInt(), Random.nextInt(), Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.volume }
        }
    }

}
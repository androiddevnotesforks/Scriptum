package sgtmelon.scriptum.ui.dialog

import sgtmelon.extension.getTime
import sgtmelon.safedialog.MultipleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi

/**
 * Class for UI control of [MultipleDialog] when cause long click on note.
 */
class NoteDialogUi(private val noteModel: NoteModel) : ParentUi(), IDialogUi {

    //region Views

    private val bindButton = getViewByText(if (noteModel.noteEntity.isStatus) {
        R.string.dialog_menu_status_unbind
    } else {
        R.string.dialog_menu_status_bind
    })

    private val convertButton = getViewByText(when (noteModel.noteEntity.type) {
        NoteType.TEXT -> R.string.dialog_menu_convert_to_roll
        NoteType.ROLL -> R.string.dialog_menu_convert_to_text
    })

    private val copyButton = getViewByText(R.string.dialog_menu_copy)

    private val deleteButton = getViewByText(R.string.dialog_menu_delete)
    private val restoreButton = getViewByText(R.string.dialog_menu_restore)
    private val clearButton = getViewByText(R.string.dialog_menu_clear)

    //endregion

    fun onClickBind() = waitClose {
        bindButton.click()

        noteModel.noteEntity.apply { isStatus = !isStatus }
    }

    fun onClickConvert() = waitClose {
        convertButton.click()

        noteModel.noteEntity.apply {
            change = getTime()
            when (type) {
                NoteType.TEXT -> {
                    type = NoteType.ROLL

                    // TODO #TEST optimization
                    var p = 0
                    splitTextForRoll().forEach {
                        if (it.isNotEmpty()) noteModel.rollList.add(RollEntity().apply {
                            noteId = noteModel.noteEntity.id
                            position = p++
                            text = it
                        })
                    }

                    setCompleteText(check = 0, size = noteModel.rollList.size)
                }
                NoteType.ROLL -> {
                    type = NoteType.TEXT
                    setText(noteModel.rollList)
                }
            }

        }
    }

    fun onClickCopy() = waitClose { copyButton.click() }

    fun onClickDelete() = waitClose {
        deleteButton.click()

        noteModel.noteEntity.apply {
            change = getTime()
            isBin = true
            isStatus = false
        }
    }

    fun onClickRestore() = waitClose {
        restoreButton.click()

        noteModel.noteEntity.apply {
            change = getTime()
            isBin = false
        }
    }

    fun onClickClear() = waitClose { clearButton.click() }


    fun assert() {
        if (noteModel.noteEntity.isBin) {
            restoreButton.isDisplayed().isEnabled()
            copyButton.isDisplayed().isEnabled()
            clearButton.isDisplayed().isEnabled()
        } else {
            bindButton.isDisplayed().isEnabled()
            convertButton.isDisplayed().isEnabled()
            copyButton.isDisplayed().isEnabled()
            deleteButton.isDisplayed().isEnabled()
        }
    }

    companion object {
        operator fun invoke(func: NoteDialogUi.() -> Unit, noteModel: NoteModel) =
                NoteDialogUi(noteModel).apply { waitOpen { assert() } }.apply(func)
    }

}
package sgtmelon.scriptum.test.content.note

import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Parent class for tests of [NoteAdapter]
 */
abstract class ParentNoteContentTest(private val page: MainPage) : ParentUiTest() {

    protected fun startColorTest(type: NoteType, @Theme theme: Int) {
        iPreferenceRepo.theme = theme

        onAssertList(ArrayList<NoteModel>().also { list ->
            Color.list.forEach {
                list.add(when(page) {
                    MainPage.RANK -> throw IllegalAccessException(ERROR_TEXT)
                    MainPage.NOTES ->when (type) {
                        NoteType.TEXT -> with(data) { insertText(textNote.copy(color = it)) }
                        NoteType.ROLL -> with(data) { insertRoll(rollNote.copy(color = it)) }
                    }
                    MainPage.BIN -> when (type) {
                        NoteType.TEXT -> with(data) { insertRollToBin(textNote.copy(color = it)) }
                        NoteType.ROLL -> with(data) { insertRollToBin(rollNote.copy(color = it)) }
                    }
                })
            }
        })
    }

    protected fun onAssertList(list: List<NoteModel>) {
        launch {
            mainScreen {
                when (page) {
                    MainPage.RANK -> throw IllegalAccessException(ERROR_TEXT)
                    MainPage.NOTES -> openNotesPage {
                        list.forEachIndexed { p, model -> onAssertItem(p, model) }
                    }
                    MainPage.BIN -> openBinPage {
                        list.forEachIndexed { p, model -> onAssertItem(p, model) }
                    }
                }
            }
        }
    }


    private companion object {
        const val ERROR_TEXT = "This class test only screens with [NoteAdapter]"
    }

}
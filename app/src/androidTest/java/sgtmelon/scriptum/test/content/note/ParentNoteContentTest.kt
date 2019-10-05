package sgtmelon.scriptum.test.content.note

import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.basic.extension.getTime
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Parent class for tests of [NoteAdapter]
 */
abstract class ParentNoteContentTest(private val page: MainPage) : ParentUiTest() {

    // TODO #TEST create sort test

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.sort = Sort.CHANGE
    }

    open fun colorTextLight() = startColorTest(NoteType.TEXT, Theme.LIGHT)

    open fun colorTextDark() = startColorTest(NoteType.TEXT, Theme.DARK)

    open fun colorRollLight() = startColorTest(NoteType.ROLL, Theme.LIGHT)

    open fun colorRollDark() = startColorTest(NoteType.ROLL, Theme.DARK)

    private fun startColorTest(type: NoteType, @Theme theme: Int) {
        iPreferenceRepo.theme = theme
        iPreferenceRepo.sort = Sort.COLOR

        onAssertList(ArrayList<NoteModel>().also { list ->
            Color.list.forEach {
                val note = when(type) {
                    NoteType.TEXT -> data.textNote.copy(color = it)
                    NoteType.ROLL -> data.rollNote.copy(color = it)
                }

                list.add(when(page) {
                    MainPage.RANK -> throw IllegalAccessException(ERROR_TEXT)
                    MainPage.NOTES ->when (type) {
                        NoteType.TEXT -> data.insertText(note)
                        NoteType.ROLL -> data.insertRoll(note)
                    }
                    MainPage.BIN -> when (type) {
                        NoteType.TEXT -> data.insertTextToBin(note)
                        NoteType.ROLL -> data.insertRollToBin(note)
                    }
                })
            }
        })
    }


    open fun timeText() = startTimeTest(NoteType.TEXT)

    open fun timeRoll() = startTimeTest(NoteType.ROLL)

    private fun startTimeTest(type: NoteType) = onAssertList(ArrayList<NoteModel>().also { list ->
        lastArray.forEach {
            val time = getTime(it)
            val note = when(type) {
                NoteType.TEXT -> data.textNote.copy(create = time, change = time)
                NoteType.ROLL -> data.rollNote.copy(create = time, change = time)
            }

            list.add(when(page) {
                MainPage.RANK -> throw IllegalAccessException(ERROR_TEXT)
                MainPage.NOTES -> when(type) {
                    NoteType.TEXT -> data.insertText(note)
                    NoteType.ROLL -> data.insertRoll(note)
                }
                MainPage.BIN -> when(type) {
                    NoteType.TEXT -> data.insertTextToBin(note)
                    NoteType.ROLL -> data.insertRollToBin(note)
                }
            })
        }
    })


    open fun rollRow1() = startRowTest(count = 1)

    open fun rollRow2() = startRowTest(count = 2)

    open fun rollRow3() = startRowTest(count = 3)

    open fun rollRow4() = startRowTest(count = 4)

    private fun startRowTest(count: Int) = onAssertList(ArrayList<NoteModel>().apply {
        TODO ("#TEST create test")
    })

    private fun onAssertList(list: List<NoteModel>) {
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

    companion object {
        private const val ERROR_TEXT = "This class test only screens with [NoteAdapter]"

        private const val LAST_HOUR = -60
        private const val LAST_DAY = LAST_HOUR * 24
        private const val LAST_MONTH = LAST_DAY * 30
        private const val LAST_YEAR = LAST_MONTH * 12

        val lastArray = arrayListOf(LAST_HOUR, LAST_DAY, LAST_MONTH, LAST_YEAR)
    }

}
package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main

import sgtmelon.common.utils.getCalendarWithAdd
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.key.MainPage
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.presentation.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Parent class for tests of [NoteAdapter]
 */
abstract class ParentNoteContentTest(private val page: MainPage) : ParentUiTest() {

    private val lastArray = arrayListOf(LAST_HOUR, LAST_DAY, LAST_MONTH, LAST_YEAR)

    open fun colorTextLight() = startColorTest(ThemeDisplayed.LIGHT, NoteType.TEXT)

    open fun colorTextDark() = startColorTest(ThemeDisplayed.DARK, NoteType.TEXT)

    open fun colorRollLight() = startColorTest(ThemeDisplayed.LIGHT, NoteType.ROLL)

    open fun colorRollDark() = startColorTest(ThemeDisplayed.DARK, NoteType.ROLL)

    private fun startColorTest(theme: ThemeDisplayed, type: NoteType) {
        setupTheme(theme)
        preferencesRepo.sort = Sort.COLOR

        onAssertList(ArrayList<NoteItem>().also { list ->
            for (it in Color.values()) {
                val note = when (type) {
                    NoteType.TEXT -> db.textNote.copy(color = it)
                    NoteType.ROLL -> db.rollNote.copy(color = it)
                }

                list.add(when (page) {
                    MainPage.RANK -> throwPageError()
                    MainPage.NOTES -> when (type) {
                        NoteType.TEXT -> db.insertText(note)
                        NoteType.ROLL -> db.insertRoll(note)
                    }
                    MainPage.BIN -> when (type) {
                        NoteType.TEXT -> db.insertTextToBin(note)
                        NoteType.ROLL -> db.insertRollToBin(note)
                    }
                })
            }
        })
    }


    open fun timeCreateText() = startTimeTest(NoteType.TEXT, Sort.CREATE)

    open fun timeCreateRoll() = startTimeTest(NoteType.ROLL, Sort.CREATE)

    open fun timeChangeText() = startTimeTest(NoteType.TEXT, Sort.CHANGE)

    open fun timeChangeRoll() = startTimeTest(NoteType.ROLL, Sort.CHANGE)

    private fun startTimeTest(type: NoteType, sort: Sort) {
        preferencesRepo.sort = sort

        onAssertList(ArrayList<NoteItem>().also { list ->
            for (it in lastArray) {
                val time = getCalendarWithAdd(it).getText()

                val note = when (type) {
                    NoteType.TEXT -> when (sort) {
                        Sort.CREATE -> db.textNote.copy(create = time)
                        Sort.CHANGE -> db.textNote.copy(change = time)
                        else -> throw IllegalAccessException(SORT_ERROR_TEXT)
                    }
                    NoteType.ROLL -> when (sort) {
                        Sort.CREATE -> db.rollNote.copy(create = time)
                        Sort.CHANGE -> db.rollNote.copy(change = time)
                        else -> throw IllegalAccessException(SORT_ERROR_TEXT)
                    }
                }

                list.add(when (page) {
                    MainPage.RANK -> throwPageError()
                    MainPage.NOTES -> when (type) {
                        NoteType.TEXT -> db.insertText(note)
                        NoteType.ROLL -> db.insertRoll(note)
                    }
                    MainPage.BIN -> when (type) {
                        NoteType.TEXT -> db.insertTextToBin(note)
                        NoteType.ROLL -> db.insertRollToBin(note)
                    }
                })
            }
        })
    }


    open fun rollRow1() = startRowTest(count = 1)

    open fun rollRow2() = startRowTest(count = 2)

    open fun rollRow3() = startRowTest(count = 3)

    open fun rollRow4() = startRowTest(count = 4)

    private fun startRowTest(count: Int) {
        val rollList = ArrayList<RollEntity>()

        for (i in 0 until count) {
            rollList.add(db.rollEntity.apply {
                position = i
                text = "$i | $text"
            })
        }

        onAssertList(ArrayList<NoteItem>().apply {
            add(
                when (page) {
                    MainPage.RANK -> throwPageError()
                    MainPage.NOTES -> db.insertRoll(list = rollList)
                    MainPage.BIN -> db.insertRollToBin(list = rollList)
                }
            )
        })
    }


    open fun progressIndicator1() = startProgressTest(check = 10, size = 99)

    open fun progressIndicator2() = startProgressTest(check = 10, size = 100)

    open fun progressIndicator3() = startProgressTest(check = 99, size = 99)

    open fun progressIndicator4() = startProgressTest(check = 100, size = 100)

    private fun startProgressTest(check: Int, size: Int) {
        if (size < check) throw IllegalAccessException(OVERFLOW_ERROR_TEXT)

        val rollList = ArrayList<RollEntity>()

        for (i in 0 until size) {
            rollList.add(db.rollEntity.apply {
                position = i
                text = "$i | $text"
            })
        }

        for ((done, entity) in rollList.withIndex()) {
            entity.isCheck = true

            if (done + 1 == check) break
        }

        onAssertList(ArrayList<NoteItem>().apply {
            add(
                when (page) {
                    MainPage.RANK -> throwPageError()
                    MainPage.NOTES -> db.insertRoll(list = rollList)
                    MainPage.BIN -> db.insertRollToBin(list = rollList)
                }
            )
        })
    }


    open fun rankTextLight() = startRankTest(ThemeDisplayed.LIGHT, NoteType.TEXT)

    open fun rankTextDark() = startRankTest(ThemeDisplayed.DARK, NoteType.TEXT)

    open fun rankRollLight() = startRankTest(ThemeDisplayed.LIGHT, NoteType.ROLL)

    open fun rankRollDark() = startRankTest(ThemeDisplayed.DARK, NoteType.ROLL)

    private fun startRankTest(theme: ThemeDisplayed, type: NoteType) {
        setupTheme(theme)
        preferencesRepo.sort = Sort.RANK

        onAssertList(ArrayList<NoteItem>().also { list ->
            for (i in 10 downTo 0) {
                val rankEntity = db.rankEntity.apply { position = i }

                list.add(
                    when (page) {
                        MainPage.RANK -> throwPageError()
                        MainPage.NOTES -> db.insertRankForNotes(rankEntity, type).second
                        MainPage.BIN -> db.insertRankForBin(rankEntity, type).second
                    }
                )
            }
        }.reversed())
    }

    open fun rankTextCancel() = startRankCancelTest(NoteType.TEXT)

    open fun rankRollCancel() = startRankCancelTest(NoteType.ROLL)

    private fun startRankCancelTest(type: NoteType) {
        val pair = when (page) {
            MainPage.RANK -> throw IllegalAccessError(PAGE_ERROR_TEXT)
            MainPage.NOTES -> db.insertRankForNotes(type = type)
            MainPage.BIN -> db.insertRankForBin(type = type)
        }

        launch {
            mainScreen {
                rankScreen { onClickVisible() }

                when (page) {
                    MainPage.RANK -> throwPageError()
                    MainPage.NOTES -> notesScreen(isEmpty = true, isHide = true)
                    MainPage.BIN -> binScreen { onAssertItem(pair.second) }
                }

                rankScreen { onClickCancel() }
                pair.second.clearRank()

                when (page) {
                    MainPage.RANK -> throwPageError()
                    MainPage.NOTES -> notesScreen { onAssertItem(pair.second) }
                    MainPage.BIN -> binScreen { onAssertItem(pair.second) }
                }
            }
        }
    }

    private fun onAssertList(list: List<NoteItem>) {
        launch {
            mainScreen {
                when (page) {
                    MainPage.RANK -> throwPageError()
                    MainPage.NOTES -> notesScreen {
                        for ((i, item) in list.withIndex()) onAssertItem(item, i)
                    }
                    MainPage.BIN -> binScreen {
                        for ((i, item) in list.withIndex()) onAssertItem(item, i)
                    }
                }
            }
        }
    }

    private fun throwPageError(): Nothing = throw Exception(PAGE_ERROR_TEXT)

    companion object {
        private const val PAGE_ERROR_TEXT = "This class test only screens with [NoteAdapter]"
        private const val OVERFLOW_ERROR_TEXT = "Check count must be < than size"
        private const val SORT_ERROR_TEXT = "Wrong sort type"

        private const val LAST_HOUR = -60
        private const val LAST_DAY = LAST_HOUR * 24
        private const val LAST_MONTH = LAST_DAY * 30
        private const val LAST_YEAR = LAST_MONTH * 12
    }
}
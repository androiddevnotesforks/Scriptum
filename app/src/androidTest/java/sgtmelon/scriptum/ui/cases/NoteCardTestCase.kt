package sgtmelon.scriptum.ui.cases

import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.auto.lastArray

/**
 * Parent class for tests of [NoteAdapter]
 */
abstract class NoteCardTestCase(
    private val theme: ThemeDisplayed,
    private val page: MainPage
) : ParentUiTest() {

    override fun setUp() {
        super.setUp()
        setupTheme(theme)
    }

    open fun colorText() = startColorTest(NoteType.TEXT)

    open fun colorRoll() = startColorTest(NoteType.ROLL)

    /**
     * Test of different note colors by [type].
     */
    private fun startColorTest(type: NoteType) {
        preferencesRepo.sort = Sort.COLOR

        assertList(ArrayList<NoteItem>().also { list ->
            for (it in Color.values()) {
                val note = when (type) {
                    NoteType.TEXT -> db.textNote.copy(color = it)
                    NoteType.ROLL -> db.rollNote.copy(color = it)
                }

                list.add(
                    when (page) {
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

    /**
     * Test of time display and sorting.
     */
    private fun startTimeTest(type: NoteType, sort: Sort) {
        preferencesRepo.sort = sort

        assertList(ArrayList<NoteItem>().also { list ->
            for (it in lastArray) {
                val time = getClearCalendar(it).toText()

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

    /**
     * Test of displaying different [count] of roll rows.
     */
    private fun startRowTest(count: Int) {
        val rollList = ArrayList<RollEntity>()

        for (i in 0 until count) {
            rollList.add(db.rollEntity.apply {
                position = i
                text = "$i | $text"
            })
        }

        assertList(ArrayList<NoteItem>().apply {
            add(
                when (page) {
                    MainPage.RANK -> throwPageError()
                    MainPage.NOTES -> db.insertRoll(list = rollList)
                    MainPage.BIN -> db.insertRollToBin(list = rollList)
                }
            )
        })
    }


    open fun notificationText() = startNotificationIndicatorTest(NoteType.TEXT)

    open fun notificationRoll() = startNotificationIndicatorTest(NoteType.ROLL)

    /**
     * Test of notification indicator. It's impossible to setup notification for [MainPage.BIN].
     */
    private fun startNotificationIndicatorTest(type: NoteType) {
        val item = when (page) {
            MainPage.RANK -> throwPageError()
            MainPage.NOTES -> when (type) {
                NoteType.TEXT -> db.insertText()
                NoteType.ROLL -> db.insertRoll()
            }
            MainPage.BIN -> throwPageError()
        }

        assertList(listOf(db.insertNotification(item)))
    }


    open fun rollInvisible() = startVisibleIndicatorTest(isVisible = false)

    open fun rollVisible() = startVisibleIndicatorTest(isVisible = true)

    /**
     * Test of roll visible indicator.
     */
    private fun startVisibleIndicatorTest(isVisible: Boolean) {
        val item = when (page) {
            MainPage.RANK -> throwPageError()
            MainPage.NOTES -> db.insertRoll(isVisible = isVisible)
            MainPage.BIN -> db.insertRollToBin(isVisible = isVisible)
        }

        assertList(listOf(item))
    }


    open fun progressIndicator1() = startProgressTest(check = 10, size = 99)

    open fun progressIndicator2() = startProgressTest(check = 10, size = 100)

    open fun progressIndicator3() = startProgressTest(check = 99, size = 99)

    open fun progressIndicator4() = startProgressTest(check = 100, size = 100)

    /**
     * Test of roll item progress.
     */
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

        assertList(ArrayList<NoteItem>().apply {
            add(
                when (page) {
                    MainPage.RANK -> throwPageError()
                    MainPage.NOTES -> db.insertRoll(list = rollList)
                    MainPage.BIN -> db.insertRollToBin(list = rollList)
                }
            )
        })
    }


    open fun rankText() = startRankTest(NoteType.TEXT)

    open fun rankRoll() = startRankTest(NoteType.ROLL)

    /**
     * Test of rank indicator and sort.
     */
    private fun startRankTest(type: NoteType) {
        preferencesRepo.sort = Sort.RANK

        assertList(ArrayList<NoteItem>().also { list ->
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

    /**
     * Test of rank visible and cancel works with items in [page].
     */
    private fun startRankCancelTest(type: NoteType) {
        val pair = when (page) {
            MainPage.RANK -> throw IllegalAccessError(PAGE_ERROR_TEXT)
            MainPage.NOTES -> db.insertRankForNotes(type = type)
            MainPage.BIN -> db.insertRankForBin(type = type)
        }

        launch {
            mainScreen {
                openRank { itemVisible() }

                when (page) {
                    MainPage.RANK -> throwPageError()
                    MainPage.NOTES -> openNotes(isEmpty = true, isHide = true)
                    MainPage.BIN -> openBin { assertItem(pair.second) }
                }

                openRank { itemCancel() }
                pair.second.clearRank()

                when (page) {
                    MainPage.RANK -> throwPageError()
                    MainPage.NOTES -> openNotes { assertItem(pair.second) }
                    MainPage.BIN -> openBin { assertItem(pair.second) }
                }
            }
        }
    }

    private fun assertList(list: List<NoteItem>) {
        launch {
            mainScreen {
                when (page) {
                    MainPage.RANK -> throwPageError()
                    MainPage.NOTES -> openNotes { assertList(list) }
                    MainPage.BIN -> openBin { assertList(list) }
                }
            }
        }
    }

    private fun throwPageError(): Nothing = throw Exception(PAGE_ERROR_TEXT)

    companion object {
        private const val PAGE_ERROR_TEXT = "This class test only screens with [NoteAdapter]"
        private const val OVERFLOW_ERROR_TEXT = "Check count must be < than size"
        private const val SORT_ERROR_TEXT = "Wrong sort type"
    }
}
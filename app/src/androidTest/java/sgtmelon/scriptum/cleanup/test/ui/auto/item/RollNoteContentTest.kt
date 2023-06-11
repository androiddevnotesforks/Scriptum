package sgtmelon.scriptum.cleanup.test.ui.auto.item

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.ui.item.RollItemUi

import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test for [RollItemUi].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteContentTest : ParentUiTest() {

    @Test fun itemListOnNotes() = db.insertRoll().let {
        launchSplash { mainScreen { openNotes { openRoll(it) { onAssertAll() } } } }
    }

    @Test fun itemListOnBin() = db.insertRollToBin().let {
        launchSplash { mainScreen { openBin { openRoll(it) { onAssertAll() } } } }
    }

    @Test fun itemOnChangeText() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            enterPanel { onAdd(nextString()) }
                            onAssertAll()

                            onEnterText().onEnterText(nextString())
                        }
                    }
                }
            }
        }
    }

    @Test fun itemOnClickCheck() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            repeat(times = 3) { enterPanel { onAdd(nextString()) } }
                            onAssertAll()

                            controlPanel { onSave() }
                            repeat(times = 5) { onClickCheck() }
                        }
                    }
                }
            }
        }
    }

    @Test fun itemOnEmptyDelete() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            repeat(times = 5) { enterPanel { onAdd(nextString()) } }
                            onEnterText(p = 0).onEnterText(p = 3)

                            controlPanel { onSave() }
                            onAssertAll()
                        }
                    }
                }
            }
        }
    }
}
package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteFragmentImpl

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test toolbar arrow and back press for [TextNoteFragmentImpl].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteBackTest : ParentUiTest() {

    @Test fun closeOnBin() = db.insertTextToBin().let {
        launchSplash {
            mainScreen {
                openBin { openText(it) { toolbar { clickBack() } } }
                assert()
                openBin { openText(it) { pressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnCreate() = db.createText().let {
        launchSplash {
            mainScreen {
                openAddDialog { createText(it) { toolbar { clickBack() } } }
                assert()
                openAddDialog { createText(it) { pressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnRead() = db.insertText().let {
        launchSplash {
            mainScreen {
                openNotes { openText(it) { toolbar { clickBack() } } }
                assert()
                openNotes { openText(it) { pressBack() } }
                assert()
            }
        }
    }


    @Test fun saveOnCreate() = db.createText().let {
        launchSplash {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        toolbar { onEnterName(nextString()) }
                        onEnterText(nextString())
                        pressBack()
                    }
                }
            }
        }
    }

    @Test fun saveOnEdit() = db.insertText().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel { onEdit() }
                        toolbar { onEnterName(nextString()) }
                        onEnterText(nextString())
                        pressBack()
                    }
                }
            }
        }
    }


    @Test fun cancelOnEdit() = db.insertText().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel { onEdit() }
                        onEnterText(nextString())
                        toolbar { onEnterName(nextString()).clickBack() }
                    }
                }
            }
        }
    }
}
package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.ui.cases.ListContentCase

/**
 * Test of [NotesFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotesRotationTest : ParentUiRotationTest(),
    ListContentCase {

    @Test override fun contentEmpty() = launch {
        mainScreen {
            openNotes(isEmpty = true) {
                rotate.toSide()
                assert(isEmpty = true)
            }
            assert(isFabVisible = true)
        }
    }

    @Test override fun contentList() = db.fillNotes().let {
        launch {
            mainScreen {
                openNotes {
                    rotate.toSide()
                    assert(isEmpty = false)
                    assertList(it)
                }
                assert(isFabVisible = true)
            }
        }
    }

    @Test fun textNoteDialog() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) {
                        rotate.toSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun rollNoteDialog() = db.insertRoll().let {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) {
                        rotate.toSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun dateDialog() = db.insertNote().let { startDateDialogTest(it) }

    @Test fun dateDialogReset() = db.insertNotification(db.insertNote()).let {
        startDateDialogTest(it)
    }

    private fun startDateDialogTest(item: NoteItem) {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(item) {
                        notification {
                            rotate.toSide()
                            assert()
                        }
                    }
                }
            }
        }
    }

    @Test fun timeDialog() = db.insertNote().let {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) {
                        notification {
                            applyDate {
                                rotate.toSide()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }
}
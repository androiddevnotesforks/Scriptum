package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.parent.ui.launch
import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest

/**
 * Test of [NotesFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotesRotationTest : ParentUiRotationTest() {

    @Test fun contentEmpty() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                rotate.toSide()
                assert(isEmpty = true)
            }
            assert(isFabVisible = true)
        }
    }

    @Test fun contentList() = launch({ db.fillNotes() }) {
        mainScreen {
            notesScreen {
                rotate.toSide()
                assert(isEmpty = false)
            }
            assert(isFabVisible = true)
        }
    }

    @Test fun textNoteDialog() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen {
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
                notesScreen {
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
                notesScreen {
                    openNoteDialog(item) {
                        onNotification {
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
                notesScreen {
                    openNoteDialog(it) {
                        onNotification {
                            onClickApply {
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
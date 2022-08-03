package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.cleanup.test.parent.ParentRotationTest

/**
 * Test of [NotesFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotesRotationTest : ParentRotationTest() {

    @Test fun contentEmpty() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                automator.rotateSide()
                assert(isEmpty = true)
            }
            assert(isFabVisible = true)
        }
    }

    @Test fun contentList() = launch({ data.fillNotes() }) {
        mainScreen {
            notesScreen {
                automator.rotateSide()
                assert(isEmpty = false)
            }
            assert(isFabVisible = true)
        }
    }

    @Test fun textNoteDialog() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openNoteDialog(it) {
                        automator.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun rollNoteDialog() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openNoteDialog(it) {
                        automator.rotateSide()
                        assert()
                    }
                }
            }
        }
    }

    @Test fun dateDialog() = data.insertNote().let { startDateDialogTest(it) }

    @Test fun dateDialogReset() = data.insertNotification(data.insertNote()).let {
        startDateDialogTest(it)
    }

    private fun startDateDialogTest(item: NoteItem) {
        launch {
            mainScreen {
                notesScreen {
                    openNoteDialog(item) {
                        onNotification {
                            automator.rotateSide()
                            assert()
                        }
                    }
                }
            }
        }
    }

    @Test fun timeDialog() = data.insertNote().let {
        launch {
            mainScreen {
                notesScreen {
                    openNoteDialog(it) {
                        onNotification {
                            onClickApply {
                                automator.rotateSide()
                                assert()
                            }
                        }
                    }
                }
            }
        }
    }
}
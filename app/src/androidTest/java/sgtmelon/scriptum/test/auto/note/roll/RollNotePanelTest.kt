package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test control panel for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNotePanelTest : ParentUiTest() {

    @Test fun actionOnBinRestore() = data.insertRollToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                binScreen { openRollNote(it) { controlPanel { onRestore() } }.assert(isEmpty = true) }
                notesScreen()
            }
        }
    }

    @Test fun actionOnBinRestoreOpen() = data.insertRollToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)

                binScreen {
                    openRollNote(it) {
                        controlPanel { onRestoreOpen() }
                        onPressBack()
                    }
                    assert(isEmpty = true)
                }

                notesScreen()
            }
        }
    }

    @Test fun actionOnBinClear() = data.insertRollToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                binScreen { openRollNote(it) { controlPanel { onClear() } }.assert(isEmpty = true) }
                notesScreen(isEmpty = true)
            }
        }
    }


    @Test fun actionOnReadNotification() = data.insertRoll().let {
        launch {
            mainScreen { notesScreen { openRollNote(it) { controlPanel { onNotification() } } } }
        }
    }

    @Test fun actionOnReadBind() = startBindTest(isStatus = false)

    @Test fun actionOnReadUnbind() = startBindTest(isStatus = true)

    private fun startBindTest(isStatus: Boolean) {
        val model = data.insertRoll(data.rollNote.copy(isStatus = isStatus))

        launch {
            mainScreen {
                notesScreen {
                    openRollNote(model) {
                        controlPanel { onBind() }
                        onPressBack()
                    }

                    openNoteDialog(model)
                }
            }
        }
    }

    @Test fun actionOnReadConvert() = data.insertRoll().let {
        launch {
            mainScreen { notesScreen { openRollNote(it) { controlPanel { onConvert() } } } }
        }
    }

    @Test fun actionOnReadDelete() = data.insertRoll().let {
        launch {
            mainScreen {
                binScreen(isEmpty = true)

                notesScreen {
                    openRollNote(it) { controlPanel { onDelete() } }.assert(isEmpty = true)
                }

                binScreen()
            }
        }
    }

    @Test fun actionOnReadEdit() = data.insertRoll().let {
        launch { mainScreen { notesScreen { openRollNote(it) { controlPanel { onEdit() } } } } }
    }


    // TODO finish test
    @Test fun actionOnEditUndoRedo() {
        TODO(reason = "#TEST write test")
    }

    @Test fun actionOnCreateRank() = data.fillRank(count = 3).let {
        val item = data.createRoll()
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(item, isRankEmpty = false) { controlPanel { onRank(it) } }
                }
            }
        }
    }

    @Test fun actionOnEditRank() = data.fillRank(count = 3).let {
        val item = data.insertRoll()
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(item, isRankEmpty = false) {
                        controlPanel { onEdit().onRank(it) }
                    }
                }
            }
        }
    }

    @Test fun actionOnCreateColor() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRoll(it) { controlPanel { onColor() } } } } }
    }

    @Test fun actionOnEditColor() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openRollNote(it) { controlPanel { onEdit().onColor() } } }
            }
        }
    }


    @Test fun actionOnCreateSave() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        toolbar { onEnterName(nextString()) }
                        repeat(times = 3) { enterPanel { onAdd(nextString()) } }
                        onSwipe()

                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun actionOnEditSave() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onEdit() }

                        toolbar { onEnterName(nextString()) }
                        onSwipeAll()
                        repeat(times = 3) { enterPanel { onAdd(nextString()) } }

                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun actionOnCreateLongSave() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        toolbar { onEnterName(nextString()) }
                        repeat(times = 3) { enterPanel { onAdd(nextString()) } }
                        onSwipe()

                        controlPanel { onLongSave() }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

    /**
     * androidx.test.espresso.base.DefaultFailureHandler$AssertionFailedWithCauseError: 'not is displayed on the screen to the user' doesn't match the selected view.
     * Expected: not is displayed on the screen to the user
     * Got: "LinearLayout{id=2131296666, res-name=roll_note_info_include, visibility=VISIBLE, width=480, height=340, has-focus=false, has-focusable=false, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=false, is-selected=false, layout-params=android.widget.FrameLayout$LayoutParams@b5a28cf, tag=null, root-is-layout-requested=false, has-input-connection=false, x=0.0, y=0.0, child-count=3}"
     */
    @Test fun actionOnEditLongSave() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onEdit() }

                        toolbar { onEnterName(nextString()) }
                        onSwipeAll()
                        repeat(times = 3) { enterPanel { onAdd(nextString()) } }

                        controlPanel { onLongSave() }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

}
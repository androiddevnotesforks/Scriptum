package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test visible button and list for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteVisibleTest : ParentUiTest() {

    @Test fun changeOpen() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onClickVisible()
                        onPressBack()
                    }

                    openRollNote(it) {
                        onClickVisible()
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun changeCreate() {
        var item = data.createRoll()

        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(item) {
                            onClickVisible()

                            enterPanel { onAdd(nextString()) }
                            controlPanel { onSave() }

                            item = this.item

                            onPressBack()
                        }
                    }

                    openRollNote(item) { onClickVisible() }
                }
            }
        }
    }

    @Test fun changeRestore() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onEdit() }.onClickVisible()
                        toolbar { onClickBack() }.onClickVisible()
                    }
                }
            }
        }
    }

    @Test fun switch() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            onClickVisible().onClickVisible()

                            repeat(times = 3) { enterPanel { onAdd(nextString()) } }
                            controlPanel { onSave() }
                            onClickCheck()
                            onClickVisible()

                            controlPanel { onEdit() }
                            onClickVisible()
                        }
                    }
                }
            }
        }
    }

    @Test fun fullSwitch() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            onClickVisible().onClickVisible()

                            repeat(times = 3) { enterPanel { onAdd(nextString()) } }
                            controlPanel { onSave() }
                            onClickVisible()
                            repeat(times = 3) { onClickCheck() }

                            controlPanel { onEdit() }
                            onClickVisible()
                        }
                    }
                }
            }
        }
    }

    @Test fun itemCheck() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onClickCheck()
                        onClickVisible()

                        repeat(times = 2) { onClickCheck() }
                        onClickVisible()
                    }
                }
            }
        }
    }

    @Test fun itemLongCheck() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        repeat(times = 2) { onLongClickCheck() }
                        onClickVisible()

                        onLongClickCheck()
                        onClickVisible()
                    }
                }
            }
        }
    }

    @Test fun itemSwipe() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onClickVisible()

                        controlPanel { onEdit() }
                        onSwipe()
                        controlPanel { onSave() }

                        onClickVisible()
                        controlPanel { onEdit() }
                    }
                }
            }
        }
    }

    /**
     * androidx.test.espresso.base.DefaultFailureHandler$AssertionFailedWithCauseError: 'with text: is "21079e04-72bd-4c"' doesn't match the selected view.
     * Expected: with text: is "21079e04-72bd-4c"
     * Got: "AppCompatEditText{id=2131296665, res-name=roll_add_panel_enter, visibility=VISIBLE, width=408, height=25, has-focus=true, has-focusable=true, has-window-focus=true, is-clickable=true, is-enabled=true, is-focused=true, is-focusable=true, is-layout-requested=false, is-selected=false, layout-params=android.widget.LinearLayout$LayoutParams@41a5a6a, tag=null, root-is-layout-requested=false, has-input-connection=true, editor-info=[inputType=0x1c001 imeOptions=0x6000006 privateImeOptions=null actionLabel=null actionId=0 initialSelStart=15 initialSelEnd=15 initialCapsMode=0x0 hintText=Your list item label=null packageName=null fieldId=0 fieldName=null extras=null hintLocales=null contentMimeTypes=null ], x=12.0, y=11.0, text=21079e04-72bd-4, hint=Your list item, input-type=114689, ime-target=true, has-links=false}"
     */
    @Test fun itemAdd() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            onClickVisible()

                            enterPanel { repeat(times = 5) { onAdd(nextString()) } }
                            controlPanel { onSave() }

                            onClickVisible()
                        }
                    }
                }
            }
        }
    }

    @Test fun itemEdit() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onClickVisible()

                        controlPanel { onEdit() }
                        onEnterText(nextString())
                        controlPanel { onSave() }

                        onClickVisible()
                    }
                }
            }
        }
    }

    // TODO finish test
    @Test fun itemUndoRedo() {
        TODO()
    }
}
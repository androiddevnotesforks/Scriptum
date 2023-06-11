package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragmentImpl

import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test visible button and list for [RollNoteFragmentImpl].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteVisibleTest : ParentUiTest() {

    @Test fun changeOpen() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        onClickVisible()
                        pressBack()
                    }

                    openRoll(it) {
                        onClickVisible()
                        pressBack()
                    }
                }
            }
        }
    }

    @Test fun changeCreate() {
        var item = db.createRoll()

        launchSplash {
            mainScreen {
                openNotes(isEmpty = true) {
                    openAddDialog {
                        createRoll(item) {
                            onClickVisible()

                            enterPanel { onAdd(nextString()) }
                            controlPanel { onSave() }

                            item = this.item

                            pressBack()
                        }
                    }

                    openRoll(item) { onClickVisible() }
                }
            }
        }
    }

    @Test fun changeRestore() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel { onEdit() }
                        onClickVisible()
                        toolbar { clickBack() }
                        onClickVisible()
                    }
                }
            }
        }
    }

    @Test fun switch() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true) {
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

    @Test fun fullSwitch() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true) {
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

    @Test fun itemCheck() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        onClickCheck()
                        onClickVisible()

                        repeat(times = 2) { onClickCheck() }
                        onClickVisible()
                    }
                }
            }
        }
    }

    @Test fun itemSwipe() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
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
    @Test fun itemAdd() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true) {
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

    @Test fun itemEdit() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
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
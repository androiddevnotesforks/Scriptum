package sgtmelon.scriptum.ui.screen

import androidx.test.espresso.Espresso.pressBackUnconditionally
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.waitBefore
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [AlarmActivity].
 */
class AlarmScreen(private val noteModel: NoteModel) : ParentUi() {

    //region Views

    private val parentContainer = getViewById(R.id.alarm_parent_container)
    private val rippleContainer = getViewById(R.id.alarm_ripple_container)
    private val logoView = getViewById(R.id.alarm_logo_view)
    private val recyclerView = getViewById(R.id.alarm_recycler)
    private val buttonContainer = getViewById(R.id.alarm_button_container)

    private val disableButton = getViewById(R.id.alarm_disable_button)
    private val postponeButton = getViewById(R.id.alarm_postpone_button)

    //endregion

    fun openTextNote(func: TextNoteScreen.() -> Unit = {}) {
        recyclerView.click(p = 0)
        TextNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun openRollNote(func: RollNoteScreen.() -> Unit = {}) {
        recyclerView.click(p = 0)
        RollNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun onClickDisable() {
        disableButton.click()
    }

    fun onClickPostpone() {
        postponeButton.click()
    }

    fun onPressBack() = pressBackUnconditionally()


    fun assert() {
        parentContainer.isDisplayed()
        rippleContainer.isDisplayed()
        logoView.isDisplayed()
        recyclerView.isDisplayed()

        buttonContainer.isDisplayed()
        disableButton.isDisplayed()
        postponeButton.isDisplayed()
    }

    companion object {
        operator fun invoke(func: AlarmScreen.() -> Unit, noteModel: NoteModel) =
                AlarmScreen(noteModel).apply { waitBefore(time = 1000) { assert() } }.apply(func)
    }

}
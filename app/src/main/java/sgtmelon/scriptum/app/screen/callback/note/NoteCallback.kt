package sgtmelon.scriptum.app.screen.callback.note

import sgtmelon.scriptum.app.screen.view.note.NoteActivity
import sgtmelon.scriptum.app.screen.vm.note.NoteViewModel

/**
 * Интерфейс общения [NoteViewModel] с [NoteActivity]
 *
 * @author SerjantArbuz
 */
interface NoteCallback {

    fun showTextFragment(id: Long, isSave: Boolean)

    fun showRollFragment(id: Long, isSave: Boolean)

    fun onPressBackText(): Boolean

    fun onPressBackRoll(): Boolean

    fun finish()

}
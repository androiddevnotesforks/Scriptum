package sgtmelon.scriptum.screen.ui.callback.note

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

/**
 * Interface for communication [NoteViewModel] with [NoteActivity]
 */
interface INoteActivity {

    fun updateHolder(@Theme theme: Int, @Color color: Int)

    /**
     * [checkCache] - find fragment by tag or create new
     */
    fun showTextFragment(id: Long, @Color color: Int, checkCache: Boolean)

    /**
     * [checkCache] - find fragment by tag or create new
     */
    fun showRollFragment(id: Long, @Color color: Int, checkCache: Boolean)

    fun onPressBackText(): Boolean

    fun onPressBackRoll(): Boolean

    fun onReceiveUnbindNote(id: Long)

    fun finish()

}
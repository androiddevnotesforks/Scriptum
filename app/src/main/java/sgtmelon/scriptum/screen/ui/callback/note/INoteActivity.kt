package sgtmelon.scriptum.screen.ui.callback.note

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

/**
 * Interface for communication [NoteViewModel] with [NoteActivity]
 */
interface INoteActivity {

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

    fun onCancelNoteBind(type: NoteType)

    fun finish()

}
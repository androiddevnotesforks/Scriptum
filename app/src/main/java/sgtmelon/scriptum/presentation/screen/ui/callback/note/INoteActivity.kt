package sgtmelon.scriptum.presentation.screen.ui.callback.note

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.note.INoteViewModel

/**
 * Interface for communication [INoteViewModel] with [NoteActivity].
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
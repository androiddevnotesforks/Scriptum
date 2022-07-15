package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note

import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.INoteViewModel

/**
 * Interface for communication [INoteViewModel] with [NoteActivity].
 */
interface INoteActivity {

    fun updateHolder(@Color color: Int)

    fun setupInsets()

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
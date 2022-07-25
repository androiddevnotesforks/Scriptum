package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note

import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.infrastructure.model.key.Color

/**
 * Interface for communication [INoteViewModel] with [NoteActivity].
 */
interface INoteActivity {

    fun updateHolder(color: Color)

    fun setupInsets()

    /**
     * [checkCache] - find fragment by tag or create new
     */
    fun showTextFragment(id: Long, color: Color, checkCache: Boolean)

    /**
     * [checkCache] - find fragment by tag or create new
     */
    fun showRollFragment(id: Long, color: Color, checkCache: Boolean)

    fun onPressBackText(): Boolean

    fun onPressBackRoll(): Boolean

    fun onReceiveUnbindNote(id: Long)

    fun finish()

}
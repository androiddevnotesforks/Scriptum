package sgtmelon.scriptum.infrastructure.screen.note

/**
 * Interface for communication [NoteViewModel] with [NoteActivity].
 */
@Deprecated("delete it")
interface INoteActivity {

    //    fun updateHolder(color: Color)
    //
    //    fun setupInsets()
    //
    //    /**
    //     * [checkCache] - find fragment by tag or create new
    //     */
    //    fun showTextFragment(id: Long, color: Color, checkCache: Boolean)
    //
    //    /**
    //     * [checkCache] - find fragment by tag or create new
    //     */
    //    fun showRollFragment(id: Long, color: Color, checkCache: Boolean)

    fun onPressBackText(): Boolean

    fun onPressBackRoll(): Boolean

    fun onReceiveUnbindNote(noteId: Long)

    fun finish()

}
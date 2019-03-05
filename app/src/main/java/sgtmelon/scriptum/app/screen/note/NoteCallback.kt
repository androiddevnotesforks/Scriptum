package sgtmelon.scriptum.app.screen.note

/**
 * Интерфейс общения [NoteViewModel] с [NoteActivity]
 */
interface NoteCallback {

    fun showTextFragment(id: Long, isSave: Boolean)

    fun showRollFragment(id: Long, isSave: Boolean)

    fun onPressBackText(): Boolean

    fun onPressBackRoll(): Boolean

    fun finish()

}
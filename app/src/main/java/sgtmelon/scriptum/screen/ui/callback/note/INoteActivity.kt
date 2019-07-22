package sgtmelon.scriptum.screen.ui.callback.note

import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

/**
 * Interface for communication [NoteViewModel] with [NoteActivity]
 *
 * @author SerjantArbuz
 */
interface INoteActivity {

    /**
     * [checkCache] - пытаться найти фрагмент по тегу или пропустить этот шаг и создать новый
     */
    fun showTextFragment(id: Long, checkCache: Boolean)

    /**
     * [checkCache] - пытаться найти фрагмент по тегу или пропустить этот шаг и создать новый
     */
    fun showRollFragment(id: Long, checkCache: Boolean)

    fun onPressBackText(): Boolean

    fun onPressBackRoll(): Boolean

    fun onCancelNoteBind(type: NoteType)

    fun finish()

}
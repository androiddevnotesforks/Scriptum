package sgtmelon.scriptum.screen.callback.note

import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.view.note.NoteActivity
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

/**
 * Интерфейс общения [NoteViewModel] с [NoteActivity]
 *
 * @author SerjantArbuz
 */
interface NoteCallback {

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
package sgtmelon.scriptum.infrastructure.screen.note

import sgtmelon.scriptum.infrastructure.model.key.preference.Color

interface NoteViewModel/* : IParentViewModel*/ {

    val defaultColor: Color

    //    fun onSaveData(bundle: Bundle)

    //    fun onSetupFragment(checkCache: Boolean)

    fun onPressBack(): Boolean

    fun onUpdateNoteId(id: Long)

    fun onUpdateNoteColor(color: Color)

    fun onConvertNote()

}
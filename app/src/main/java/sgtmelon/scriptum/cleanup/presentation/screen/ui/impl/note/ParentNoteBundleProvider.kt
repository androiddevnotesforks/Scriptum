package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.converter.key.NoteStateConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.screen.parent.ParentBundleProvider
import sgtmelon.scriptum.infrastructure.utils.extensions.getEnum
import sgtmelon.scriptum.infrastructure.utils.extensions.putEnum

abstract class ParentNoteBundleProvider(
    private val stateConverter: NoteStateConverter
) : ParentBundleProvider {

    private var isEdit: Boolean? = null
    private var noteState: NoteState? = null

    val state: Pair<Boolean, NoteState>?
        get() {
            val isEdit = isEdit ?: return null
            val noteState = noteState ?: return null
            return isEdit to noteState
        }

    override fun getData(bundle: Bundle?) {
        if (bundle == null) return

        isEdit = bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT)
        noteState = bundle.getEnum(Intent.STATE, Default.STATE, stateConverter)
    }

    override fun saveData(outState: Bundle) {
        isEdit?.let { outState.putBoolean(Intent.IS_EDIT, it) }
        noteState?.let { outState.putEnum(Intent.STATE, stateConverter, it) }
    }

    fun updateEdit(isEdit: Boolean) = run { this.isEdit = isEdit }
    fun updateState(noteState: NoteState) = run { this.noteState = noteState }
}
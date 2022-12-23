package sgtmelon.scriptum.infrastructure.screen.note

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteStateConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.parent.ParentBundleProvider
import sgtmelon.scriptum.infrastructure.utils.extensions.getEnum
import sgtmelon.scriptum.infrastructure.utils.extensions.putEnum

/**
 * Bundle data provider for [NoteActivity] screen.
 */
class NoteBundleProvider(
    private val defaultColor: Color,
    private val typeConverter: NoteTypeConverter,
    private val colorConverter: ColorConverter,
    private val stateConverter: NoteStateConverter
) : ParentBundleProvider {

    private var _init: NoteInit? = null
    val init: NoteInit? get() = _init

    override fun getData(bundle: Bundle?) {
        if (bundle == null) return

        val isEdit = bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT)
        val noteState = bundle.getEnum(Intent.STATE, Default.STATE, stateConverter) ?: return

        /** Id may be equals default value, because not created note hasn't id */
        val id = bundle.getLong(Intent.ID, Default.ID)
        val type = bundle.getEnum(Intent.TYPE, Default.TYPE, typeConverter) ?: return
        val color = bundle.getEnum(Intent.COLOR, Default.COLOR, colorConverter) ?: defaultColor

        _init = NoteInit(isEdit, noteState, id, type, color)
    }

    override fun saveData(outState: Bundle) {
        init?.let {
            outState.putBoolean(Intent.IS_EDIT, it.isEdit)
            outState.putEnum(Intent.STATE, stateConverter, it.noteState)
            outState.putLong(Intent.ID, it.id)
            outState.putEnum(Intent.TYPE, typeConverter, it.type)
            outState.putEnum(Intent.COLOR, colorConverter, it.color)
        }
    }
}
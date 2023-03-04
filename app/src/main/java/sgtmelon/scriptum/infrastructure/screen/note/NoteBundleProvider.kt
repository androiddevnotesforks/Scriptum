package sgtmelon.scriptum.infrastructure.screen.note

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.bundle.ParentBundleProvider
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteStateConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Key
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
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

        val isEdit = bundle.getBoolean(Key.IS_EDIT, Default.IS_EDIT)
        val state = bundle.getEnum(Key.STATE, Default.STATE, stateConverter) ?: return

        /** Id may be equals default value, because not created note hasn't id */
        val id = bundle.getLong(Key.ID, Default.ID)
        val type = bundle.getEnum(Key.TYPE, Default.TYPE, typeConverter) ?: return
        val color = bundle.getEnum(Key.COLOR, Default.COLOR, colorConverter) ?: defaultColor
        val name = bundle.getString(Key.NAME, Default.NAME) ?: Default.NAME

        _init = NoteInit(isEdit, state, id, type, color, name)
    }

    override fun saveData(outState: Bundle) {
        init?.let {
            outState.putBoolean(Key.IS_EDIT, it.isEdit)
            outState.putEnum(Key.STATE, stateConverter, it.state)
            outState.putLong(Key.ID, it.id)
            outState.putEnum(Key.TYPE, typeConverter, it.type)
            outState.putEnum(Key.COLOR, colorConverter, it.color)
            outState.putString(Key.NAME, it.name)
        }
    }
}
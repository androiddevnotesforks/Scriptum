package sgtmelon.scriptum.infrastructure.screen.note

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.bundle.ParentBundleProvider
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteStateConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Key
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

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

        val state = bundle.get(Key.STATE) as NoteState

        /**
         * TODO надо ли тут передавать isEdit? Это значение можно передать внутрь viewModel
         *      основываясь на noteState (если новая - значит редактирование), для других состояний
         *      такого нет.
         */
        val isEdit = bundle.getBoolean(Key.IS_EDIT, Default.IS_EDIT)

        /** Id may be equals default value, because not created note hasn't id */
        val id = bundle.getLong(Key.ID, Default.ID)
        val type = bundle.get(Key.TYPE) as NoteType
        val color = bundle.get(Key.COLOR) as? Color ?: defaultColor
        val name = bundle.getString(Key.NAME, Default.NAME) ?: Default.NAME

        _init = NoteInit(state, isEdit, id, type, color, name)
    }

    override fun saveData(outState: Bundle) {
        init?.let {
            outState.putBoolean(Key.IS_EDIT, it.isEdit)
            outState.putSerializable(Key.STATE, it.state)
            outState.putLong(Key.ID, it.id)
            outState.putSerializable(Key.TYPE, it.type)
            outState.putSerializable(Key.COLOR, it.color)
            outState.putString(Key.NAME, it.name)
        }
    }
}
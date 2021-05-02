package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.factory.FragmentFactory
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceActivity

/**
 * Screen for display [NotePrefFragment].
 */
class NotePrefActivity : ParentPreferenceActivity(
    R.layout.activity_preference_note,
    R.id.note_pref_parent_container,
    R.id.note_pref_fragment_container,
    R.string.pref_header_note
) {

    override val tag: String = FragmentFactory.Preference.Tag.NOTE

    override val fragment by lazy { fragmentFactory.getNoteFragment() }

    companion object {
        operator fun get(context: Context) = Intent(context, NotePrefActivity::class.java)
    }
}
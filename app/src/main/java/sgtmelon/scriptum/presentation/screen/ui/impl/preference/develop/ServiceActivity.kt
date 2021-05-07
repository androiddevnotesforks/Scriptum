package sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop

import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.factory.FragmentFactory
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceActivity

/**
 * Screen for display [ServiceFragment]
 */
class ServiceActivity : ParentPreferenceActivity(
    R.layout.activity_preference_service,
    R.id.service_parent_container,
    R.id.service_fragment_container,
    R.string.pref_header_service
) {

    override val tag: String = FragmentFactory.Preference.Tag.SERVICE

    override val fragment by lazy { fragmentFactory.getServiceFragment() }

    companion object {
        operator fun get(context: Context) = Intent(context, ServiceActivity::class.java)
    }
}
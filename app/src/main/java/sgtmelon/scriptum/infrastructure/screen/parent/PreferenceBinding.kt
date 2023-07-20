package sgtmelon.scriptum.infrastructure.screen.parent

import androidx.annotation.StringRes
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

/**
 * Class for hold binding in [PreferenceFragment].
 */
open class PreferenceBinding(private var fragment: PreferenceFragmentCompat?) {

    fun release() {
        fragment = null
    }

    /** Function which provide preference items by [stringId]. */
    protected fun <T : Preference> findPreference(@StringRes stringId: Int): T? = fragment?.run {
        return findPreference(getString(stringId))
    }
}
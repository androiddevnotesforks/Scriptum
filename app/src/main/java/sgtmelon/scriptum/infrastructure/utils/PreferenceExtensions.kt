package sgtmelon.scriptum.infrastructure.utils

import androidx.annotation.StringRes
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

/**
 * Function which provide preference items by [stringId].
 */
fun <T : Preference> PreferenceFragmentCompat.findPreference(@StringRes stringId: Int): T? {
    return findPreference(getString(stringId))
}

inline fun Preference.setOnClickListener(crossinline onClick: (Preference) -> Unit) {
    setOnPreferenceClickListener {
        onClick(it)
        return@setOnPreferenceClickListener true
    }
}


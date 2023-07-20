package sgtmelon.scriptum.infrastructure.utils.extensions

import androidx.preference.Preference

inline fun Preference.setOnClickListener(crossinline onClick: (Preference) -> Unit) {
    setOnPreferenceClickListener {
        onClick(it)
        return@setOnPreferenceClickListener true
    }
}


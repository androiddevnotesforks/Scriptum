package sgtmelon.scriptum.infrastructure.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

inline fun <reified F : Fragment> FragmentManager.getFragmentByTag(tag: String): F? {
    return findFragmentByTag(tag) as? F
}
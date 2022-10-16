package sgtmelon.scriptum.infrastructure.screen.parent

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.preference.PreferenceFragmentCompat

abstract class ParentPreferenceBinding(
    private val lifecycle: Lifecycle,
    private var _fragment: PreferenceFragmentCompat?
) : DefaultLifecycleObserver {

    init {
        addObserver()
    }

    private fun addObserver() = lifecycle.addObserver(this)

    protected val fragment get() = _fragment

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        _fragment = null
    }
}
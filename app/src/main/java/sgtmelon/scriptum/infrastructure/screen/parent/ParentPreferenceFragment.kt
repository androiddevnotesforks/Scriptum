package sgtmelon.scriptum.infrastructure.screen.parent

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.XmlRes
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.safedialog.utils.DialogOwner
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.screen.ScriptumApplication
import sgtmelon.scriptum.infrastructure.factory.SystemDelegatorFactory
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.utils.extensions.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.setPaddingInsets
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Parent class for preference fragments.
 */
abstract class ParentPreferenceFragment : PreferenceFragmentCompat(),
    DialogOwner {

    @get:XmlRes
    abstract val xmlId: Int

    override val fm get() = parentFragmentManager

    private lateinit var _system: SystemDelegatorFactory
    protected val system get() = _system

    protected val open: OpenState = OpenState(lifecycle)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(xmlId, rootKey)
        inject(ScriptumApplication.component)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _system = SystemDelegatorFactory(view.context, lifecycle)
        open.restore(savedInstanceState)

        setupInsets()
        setupRecycler()

        setup()
        setupDialogs()
        setupObservers()
    }

    abstract fun inject(component: ScriptumComponent)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        open.save(outState)
    }

    /**
     * Setup spaces from android bars and other staff for current screen.
     */
    @CallSuper
    open fun setupInsets() {
        listView.setPaddingInsets(InsetsDir.BOTTOM)
    }

    private fun setupRecycler() {
        listView.clipToPadding = false
        listView.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
    }

    open fun setup() = Unit

    open fun setupDialogs() = Unit

    open fun setupObservers() = Unit
}
package sgtmelon.scriptum.infrastructure.screen.parent

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.XmlRes
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.safedialog.utils.DialogOwner
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.screen.ScriptumApplication
import sgtmelon.scriptum.infrastructure.bundle.BundleValue
import sgtmelon.scriptum.infrastructure.factory.SystemDelegatorFactory
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.setPaddingInsets
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Parent class for preference fragments.
 */
abstract class PreferenceFragment : PreferenceFragmentCompat(),
    DialogOwner,
    ReceiverRegistrar {

    @get:XmlRes
    abstract val xmlId: Int

    override val fm get() = parentFragmentManager

    private lateinit var _system: SystemDelegatorFactory
    protected val system get() = _system

    /**
     * List for skip repeatable code of get/save values. Override it in realization and put
     * inside your [BundleValue]s.
     */
    open val bundleValues: List<BundleValue> = listOf()

    protected val open: OpenState = OpenState(lifecycle)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(xmlId, rootKey)
        inject(ScriptumApplication.component)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _system = SystemDelegatorFactory(view.context, lifecycle)

        bundleValues.forEach { it.get(bundle = savedInstanceState ?: arguments) }
        open.restore(savedInstanceState)

        registerReceivers()

        setupInsets()
        setupRecycler()

        setup()
        setupDialogs()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterReceivers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bundleValues.forEach { it.save(outState) }
        open.save(outState)
    }

    abstract fun inject(component: ScriptumComponent)

    /** Setup spaces from android bars and other staff for current screen. */
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
package sgtmelon.scriptum.infrastructure.screen.parent

import android.os.Bundle
import android.view.View
import androidx.annotation.XmlRes
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.safedialog.utils.DialogOwner
import sgtmelon.scriptum.infrastructure.bundle.BundleValue
import sgtmelon.scriptum.infrastructure.factory.SystemDelegatorFactory
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.setPaddingInsets
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Parent class for preference fragments.
 */
abstract class PreferenceFragment<T: PreferenceBinding> : PreferenceFragmentCompat(),
    ComponentInject,
    UiSetup,
    UiRelease,
    DialogOwner,
    ReceiverReception {

    @get:XmlRes abstract val xmlId: Int

    private var _binding: T? = null
    protected val binding: T? get() = _binding

    override val fm get() = parentFragmentManager

    private var _system: SystemDelegatorFactory? = null
    protected val system get() = _system

    /**
     * List for skip repeatable code of get/save values. Override it in realization and put
     * inside your [BundleValue]s.
     */
    open val bundleValues: List<BundleValue> = listOf()

    protected val open: OpenState by lazy { OpenState(lifecycle) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(xmlId, rootKey)
        inject()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = createBinding()
        _system = SystemDelegatorFactory(view.context, lifecycle)

        bundleValues.forEach { it.get(bundle = savedInstanceState ?: arguments) }
        open.restore(savedInstanceState)

        checkInReceivers(requireContext())
        setupUi()
    }

    abstract fun createBinding(): T

    override fun onDestroyView() {
        super.onDestroyView()

        releaseBinding()
        _binding = null

        releaseSystem()
        _system = null

        checkOutReceivers(requireContext())
    }

    override fun releaseBinding() {
        binding?.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bundleValues.forEach { it.save(outState) }
        open.save(outState)
    }

    /** Setup spaces from android bars and other staff for current screen. */
    override fun setupInsets() {
        listView.setPaddingInsets(InsetsDir.BOTTOM)
    }

    override fun setupView() {
        listView.clipToPadding = false
        listView.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
    }
}
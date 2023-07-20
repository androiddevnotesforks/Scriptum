package sgtmelon.scriptum.infrastructure.screen.parent

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import sgtmelon.safedialog.utils.DialogOwner
import sgtmelon.scriptum.infrastructure.bundle.BundleValue
import sgtmelon.scriptum.infrastructure.factory.SystemDelegatorFactory
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.utils.extensions.inflateBinding

/**
 * Parent class for fragments with [ViewDataBinding].
 */
abstract class BindingFragment<T : ViewDataBinding> : Fragment(),
    UiInject,
    UiSetup,
    UiRelease,
    DialogOwner,
    ReceiverRegistrar {

    @get:LayoutRes abstract val layoutId: Int

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
    protected val parentOpen: OpenState? get() = (activity as? BindingActivity<*>)?.open

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = inflater.inflateBinding<T>(layoutId, container).also { _binding = it }
        inject()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _system = SystemDelegatorFactory(view.context, lifecycle)

        bundleValues.forEach { it.get(bundle = savedInstanceState ?: arguments) }
        open.restore(savedInstanceState)

        registerReceivers()
    }

    /**
     * [onViewStateRestored] - is a final function before STARTED fragment state.
     *
     * Link: https://developer.android.com/guide/fragments/lifecycle
     */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        /**
         * Call all setup staff here, for prevent [TextWatcher]'s false call.
         *
         * Bad case:
         * 1. [onViewCreated] = [EditText] is empty
         * 2. [setupView] = add [TextWatcher]
         * 3. [onViewStateRestored] = [EditText] restore text and it calls [TextWatcher]
         * You got text change from '' to 'your text'.
         *
         * Good case:
         * 1. [onViewCreated] = [EditText] is empty
         * 2. [onViewStateRestored] = [EditText] restore text
         * 3. [setupView] = add [TextWatcher]
         * You didn't get any text changes while [BindingFragment] initialization.
         */
        setupUi()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bundleValues.forEach { it.save(outState) }
        open.save(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        releaseBinding()
        _binding = null

        releaseSystem()
        _system = null

        unregisterReceivers()
    }
}
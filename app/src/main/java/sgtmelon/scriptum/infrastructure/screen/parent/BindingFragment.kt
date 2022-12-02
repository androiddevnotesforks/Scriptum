package sgtmelon.scriptum.infrastructure.screen.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import sgtmelon.safedialog.utils.DialogOwner
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.infrastructure.factory.DelegatorFactory
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.utils.extensions.inflateBinding

/**
 * Parent class for fragments with [ViewDataBinding].
 */
abstract class BindingFragment<T : ViewDataBinding> : Fragment(),
    DialogOwner {

    @get:LayoutRes
    abstract val layoutId: Int

    private var _binding: T? = null
    protected val binding: T? get() = _binding

    override val fm get() = parentFragmentManager

    private lateinit var _delegators: DelegatorFactory
    protected val delegators get() = _delegators

    protected val open: OpenState = OpenState(lifecycle)
    protected val parentOpen: OpenState? get() = (activity as? BindingActivity<*>)?.open

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = inflater.inflateBinding<T>(layoutId, container).also { _binding = it }
        inject(ScriptumApplication.component)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _delegators = DelegatorFactory(view.context, lifecycle)
        open.restore(savedInstanceState)

        setupView()
        setupDialogs()
        setupObservers()
        registerReceivers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        open.save(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        unregisterReceivers()
    }

    abstract fun inject(component: ScriptumComponent)

    open fun setupView() = Unit

    open fun setupDialogs() = Unit

    open fun setupObservers() = Unit

    open fun registerReceivers() = Unit

    open fun unregisterReceivers() = Unit

}
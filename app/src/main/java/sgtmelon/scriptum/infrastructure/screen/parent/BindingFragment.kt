package sgtmelon.scriptum.infrastructure.screen.parent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import sgtmelon.safedialog.utils.DialogOwner
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.screen.ScriptumApplication
import sgtmelon.scriptum.infrastructure.factory.SystemDelegatorFactory
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

    private lateinit var _system: SystemDelegatorFactory
    protected val system get() = _system

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
        _system = SystemDelegatorFactory(view.context, lifecycle)
        open.restore(savedInstanceState)

        setupView(view.context)
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

    open fun setupView(context: Context) = Unit

    open fun setupDialogs() = Unit

    open fun setupObservers() = Unit

    open fun registerReceivers() = Unit

    open fun unregisterReceivers() = Unit

}
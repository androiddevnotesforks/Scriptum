package sgtmelon.scriptum.infrastructure.screen.parent

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import sgtmelon.safedialog.utils.DialogOwner
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.screen.ScriptumApplication
import sgtmelon.scriptum.infrastructure.bundle.BundleValue
import sgtmelon.scriptum.infrastructure.factory.SystemDelegatorFactory
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.utils.extensions.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.extensions.inflateBinding

abstract class BindingActivity<T : ViewDataBinding> : AppCompatActivity(),
    DialogOwner {

    @get:LayoutRes
    abstract val layoutId: Int

    private var _binding: T? = null
    protected val binding: T? get() = _binding

    override val fm get() = supportFragmentManager

    private var _system: SystemDelegatorFactory? = null
    protected val system get() = _system

    /**
     * List for skip repeatable code of get/save values. Override it in realization and put
     * inside your [BundleValue]s.
     */
    open val bundleValues: List<BundleValue> = listOf()

    val open: OpenState = OpenState(lifecycle)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateBinding(layoutId)
        _system = SystemDelegatorFactory(context = this, lifecycle)

        bundleValues.forEach { it.get(bundle = savedInstanceState ?: intent.extras) }
        open.restore(savedInstanceState)

        inject(ScriptumApplication.component)
        registerReceivers()

        /** If keyboard was opened in another app. */
        hideKeyboard()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bundleValues.forEach { it.save(outState) }
        open.save(outState)
    }

    abstract fun inject(component: ScriptumComponent)

    open fun registerReceivers() = Unit

    open fun unregisterReceivers() = Unit

    override fun onDestroy() {
        super.onDestroy()

        releaseBinding()
        _binding = null

        releaseSystem()
        _system = null

        unregisterReceivers()
    }

    open fun releaseBinding() = Unit

    open fun releaseSystem() = Unit
}
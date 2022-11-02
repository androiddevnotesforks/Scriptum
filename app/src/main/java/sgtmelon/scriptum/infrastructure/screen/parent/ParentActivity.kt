package sgtmelon.scriptum.infrastructure.screen.parent

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import sgtmelon.safedialog.utils.DialogOwner
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.infrastructure.factory.DelegatorFactory
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.utils.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.inflateBinding

abstract class ParentActivity<T : ViewDataBinding> : AppCompatActivity(),
    DialogOwner {

    @get:LayoutRes
    abstract val layoutId: Int

    private var _binding: T? = null
    protected val binding: T? get() = _binding

    override val fm get() = supportFragmentManager

    private var _delegators: DelegatorFactory? = null
    protected val delegators get() = _delegators

    val open: OpenState = OpenState(lifecycle)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateBinding(layoutId)
        _delegators = DelegatorFactory(context = this, lifecycle)
        open.restore(savedInstanceState)

        inject(ScriptumApplication.component)
        registerReceivers()

        /** If keyboard was opened in another app. */
        hideKeyboard()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        open.save(outState)
    }

    abstract fun inject(component: ScriptumComponent)

    open fun registerReceivers() = Unit

    open fun unregisterReceivers() = Unit

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _delegators = null
        unregisterReceivers()
    }
}
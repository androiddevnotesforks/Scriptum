package sgtmelon.scriptum.infrastructure.screen.parent

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import sgtmelon.scriptum.cleanup.extension.hideKeyboard
import sgtmelon.scriptum.cleanup.extension.inflateBinding
import sgtmelon.scriptum.infrastructure.factory.DelegatorFactory

/**
 * Parent activity class for setup only dataBinding.
 */
abstract class ParentActivity<T : ViewDataBinding> : AppCompatActivity() {

    @get:LayoutRes
    abstract val layoutId: Int

    private var _binding: T? = null
    protected val binding: T? get() = _binding

    private lateinit var delegatorFactory: DelegatorFactory
    protected val delegators get() = delegatorFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateBinding(layoutId)

        delegatorFactory = DelegatorFactory(context = this, lifecycle)

        /** If it was opened in another app. */
        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
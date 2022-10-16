package sgtmelon.scriptum.infrastructure.screen.parent

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.annotation.XmlRes
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.infrastructure.factory.DelegatorFactory
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.utils.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.setPaddingInsets
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Parent class for preference fragments.
 */
abstract class ParentPreferenceFragment : PreferenceFragmentCompat() {

    @get:XmlRes
    abstract val xmlId: Int

    protected val fm get() = parentFragmentManager

    private lateinit var delegatorFactory: DelegatorFactory
    protected val delegators get() = delegatorFactory

    protected val open: OpenState = OpenState(lifecycle)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(xmlId, rootKey)
        inject(ScriptumApplication.component)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delegatorFactory = DelegatorFactory(view.context, lifecycle)

        open.restore(savedInstanceState)

        setupInsets()
        setupRecycler()
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

    /**
     * Function which provide preference items by [stringId].
     * Short realization of parent function for app needs.
     */
    fun <T : Preference> findPreference(@StringRes stringId: Int): T? {
        return findPreference(getString(stringId))
    }
}
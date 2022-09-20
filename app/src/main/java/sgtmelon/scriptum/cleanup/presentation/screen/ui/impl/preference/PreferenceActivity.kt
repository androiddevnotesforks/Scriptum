package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.key.PreferenceScreen
import sgtmelon.scriptum.cleanup.extension.InsetsDir
import sgtmelon.scriptum.cleanup.extension.getTintDrawable
import sgtmelon.scriptum.cleanup.extension.setMarginInsets
import sgtmelon.scriptum.cleanup.presentation.factory.FragmentFactory
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Preference
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Preference.Default
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys

/**
 * Screen for display [PreferenceFragment].
 */
class PreferenceActivity : ParentActivity() {

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    private val parentContainer by lazy { findViewById<ViewGroup>(R.id.preference_parent_container) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar_container) }

    private var screen: PreferenceScreen? = null

    //region Fragment

    private val fragmentFactory = FragmentFactory.Preference(fm)

    private val tag by lazy { fragmentFactory.getTag(screen) }
    private val fragment by lazy { fragmentFactory.getFragment(screen) }

    //endregion

    //region System

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getMainPreferenceBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)

        val bundle = savedInstanceState ?: intent.extras
        val typeOrdinal = bundle?.getInt(Preference.Intent.SCREEN, Default.SCREEN) ?: Default.SCREEN
        screen = PreferenceScreen.values().getOrNull(typeOrdinal) ?: return

        setupView()
        setupInsets()
        showFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val ordinal = screen?.ordinal ?: return
        outState.putInt(Preference.Intent.SCREEN, ordinal)
    }

    private fun setupView() {
        val titleId = when (screen) {
            PreferenceScreen.PREFERENCE -> R.string.title_preference
            PreferenceScreen.BACKUP -> R.string.pref_title_backup
            PreferenceScreen.NOTE -> R.string.pref_title_note
            PreferenceScreen.ALARM -> R.string.pref_title_alarm
            PreferenceScreen.HELP -> R.string.pref_title_other_help
            PreferenceScreen.DEVELOP -> R.string.pref_title_other_develop
            PreferenceScreen.SERVICE -> R.string.pref_header_service
            null -> return
        }

        toolbar?.apply {
            title = getString(titleId)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }
    }

    /**
     * [InsetsDir.BOTTOM] setup in [ParentPreferenceFragment].
     */
    private fun setupInsets() {
        parentContainer?.setMarginInsets(InsetsDir.LEFT, InsetsDir.TOP, InsetsDir.RIGHT)
    }

    private fun showFragment() {
        val tag = tag ?: return
        val fragment = fragment ?: return

        lifecycleScope.launchWhenResumed {
            fm.beginTransaction()
                .replace(R.id.preference_fragment_container, fragment, tag)
                .commit()
        }
    }

    //endregion

    companion object {
        operator fun get(
            context: Context,
            screen: PreferenceScreen
        ): Intent {
            return Intent(context, PreferenceActivity::class.java)
                .putExtra(Preference.Intent.SCREEN, screen.ordinal)
        }
    }
}
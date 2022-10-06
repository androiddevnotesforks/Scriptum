package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.key.PreferenceScreen
import sgtmelon.scriptum.cleanup.extension.InsetsDir
import sgtmelon.scriptum.cleanup.extension.getTintDrawable
import sgtmelon.scriptum.cleanup.extension.setPaddingInsets
import sgtmelon.scriptum.cleanup.presentation.factory.FragmentFactory
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.databinding.ActivityPreferenceBinding
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Preference
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Preference.Default
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys

/**
 * Screen for display [PreferenceFragment].
 */
class PreferenceActivity : ThemeActivity<ActivityPreferenceBinding>() {

    override val layoutId: Int = R.layout.activity_preference

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    // TODO remove and use binding
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar_container) }

    private var screen: PreferenceScreen? = null

    //region Fragment

    private val fragmentFactory = FragmentFactory.Preference(fm)

    private val tag by lazy { fragmentFactory.getTag(screen) }
    private val fragment by lazy { fragmentFactory.getFragment(screen) }

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = savedInstanceState ?: intent.extras
        val typeOrdinal = bundle?.getInt(Preference.Intent.SCREEN, Default.SCREEN) ?: Default.SCREEN
        screen = PreferenceScreen.values().getOrNull(typeOrdinal) ?: return

        setupView()
        showFragment()
    }

    override fun inject(component: ScriptumComponent) {
        component.getMainPreferenceBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    /**
     * [InsetsDir.BOTTOM] will be set in [ParentPreferenceFragment] (list padding).
     */
    override fun setupInsets() {
        binding?.parentContainer?.setPaddingInsets(InsetsDir.LEFT, InsetsDir.TOP, InsetsDir.RIGHT)
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

    private fun showFragment() {
        val tag = tag ?: return
        val fragment = fragment ?: return

        lifecycleScope.launchWhenResumed {
            fm.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit()
        }
    }
}
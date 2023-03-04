package sgtmelon.scriptum.infrastructure.screen.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import sgtmelon.extensions.emptyString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.databinding.ActivityPreferenceBinding
import sgtmelon.scriptum.infrastructure.bundle.BundleValue
import sgtmelon.scriptum.infrastructure.bundle.BundleValueImpl
import sgtmelon.scriptum.infrastructure.bundle.intent
import sgtmelon.scriptum.infrastructure.factory.FragmentFactory
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Preference.Key
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.extensions.getTintDrawable
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.setPaddingInsets

/**
 * Screen for display different [PreferenceScreen]'s.
 */
class PreferenceActivity : ThemeActivity<ActivityPreferenceBinding>() {

    override val layoutId: Int = R.layout.activity_preference

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    private val screen = BundleValueImpl<PreferenceScreen>(Key.SCREEN)
    override val bundleValues: List<BundleValue> = listOf(screen)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** Simply finish our activity, because [inject] already done (all lateinit injected). */
        val screen = screen.value ?: return finish()
        setupView(screen)
        showFragment(screen)
    }

    override fun inject(component: ScriptumComponent) {
        component.getMainPreferenceBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    /** [InsetsDir.BOTTOM] will be set in [ParentPreferenceFragment] (list padding). */
    override fun setupInsets() {
        super.setupInsets()

        binding?.parentContainer?.setPaddingInsets(InsetsDir.LEFT, InsetsDir.TOP, InsetsDir.RIGHT)
    }

    private fun setupView(screen: PreferenceScreen) {
        binding?.parentContainer?.tag = when (screen) {
            PreferenceScreen.MENU -> TestViewTag.PREF_MENU
            PreferenceScreen.BACKUP -> TestViewTag.PREF_BACKUP
            PreferenceScreen.NOTES -> TestViewTag.PREF_NOTE
            PreferenceScreen.ALARM -> TestViewTag.PREF_ALARM
            else -> emptyString()
        }

        val titleId = when (screen) {
            PreferenceScreen.MENU -> R.string.title_preference
            PreferenceScreen.BACKUP -> R.string.pref_title_backup
            PreferenceScreen.NOTES -> R.string.pref_title_note
            PreferenceScreen.ALARM -> R.string.pref_title_alarm
            PreferenceScreen.HELP -> R.string.pref_title_help
            PreferenceScreen.DEVELOP -> R.string.pref_title_developer
            PreferenceScreen.SERVICE -> R.string.pref_header_service
        }

        binding?.appBar?.toolbar?.apply {
            title = getString(titleId)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }
    }

    private fun showFragment(screen: PreferenceScreen) {
        val (fragment, tag) = FragmentFactory.Preference(fm).get(screen)

        fm.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }

    companion object {
        operator fun get(context: Context, screen: PreferenceScreen): Intent {
            return context.intent<PreferenceActivity>(Key.SCREEN to screen)
        }
    }
}
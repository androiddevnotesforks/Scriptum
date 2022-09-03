package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference

import android.content.ActivityNotFoundException
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.preference.Preference
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.key.PreferenceScreen
import sgtmelon.scriptum.cleanup.domain.model.state.OpenState
import sgtmelon.scriptum.cleanup.extension.getSiteIntent
import sgtmelon.scriptum.cleanup.extension.startActivitySafe
import sgtmelon.scriptum.cleanup.presentation.factory.DialogFactory
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IPreferenceViewModel
import sgtmelon.scriptum.infrastructure.model.key.Theme

/**
 * Fragment of preference.
 */
class PreferenceFragment : ParentPreferenceFragment(), IPreferenceFragment {

    @Inject internal lateinit var viewModel: IPreferenceViewModel

    private val openState = OpenState()

    //region Dialogs

    private val dialogFactory by lazy { DialogFactory.Preference.Main(context, fm) }

    private val themeDialog by lazy { dialogFactory.getThemeDialog() }
    private val aboutDialog by lazy { dialogFactory.getAboutDialog() }

    //endregion

    //region Preferences

    private val themePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_app_theme)) }

    private val backupPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_backup)) }
    private val notePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note)) }
    private val alarmPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm)) }

    private val developerPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_other_develop)) }

    //endregion

    //region System

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_main, rootKey)

        ScriptumApplication.component.getPreferenceBuilder().set(fragment = this).build()
            .inject(fragment = this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        openState.get(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onSetup()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        openState.save(outState)
    }

    //endregion

    override fun showToast(@StringRes stringId: Int) = toastControl.show(stringId)

    override fun setupApp() {
        themePreference?.setOnPreferenceClickListener {
            viewModel.onClickTheme()
            return@setOnPreferenceClickListener true
        }

        themeDialog.onPositiveClick {
            viewModel.onResultTheme(themeDialog.check)
            activity?.checkThemeChange()
        }
        themeDialog.onDismiss { openState.clear() }

        backupPreference?.setOnPreferenceClickListener {
            val context = context
            if (context != null) {
                startActivity(PreferenceActivity[context, PreferenceScreen.BACKUP])
            }

            return@setOnPreferenceClickListener true
        }

        notePreference?.setOnPreferenceClickListener {
            val context = context
            if (context != null) {
                startActivity(PreferenceActivity[context, PreferenceScreen.NOTE])
            }

            return@setOnPreferenceClickListener true
        }

        alarmPreference?.setOnPreferenceClickListener {
            val context = context
            if (context != null) {
                startActivity(PreferenceActivity[context, PreferenceScreen.ALARM])
            }

            return@setOnPreferenceClickListener true
        }
    }

    override fun setupOther() {
        findPreference<Preference>(getString(R.string.pref_key_other_rate))?.setOnPreferenceClickListener {
            onRateClick()
            return@setOnPreferenceClickListener true
        }

        findPreference<Preference>(getString(R.string.pref_key_other_help))?.setOnPreferenceClickListener {
            val context = context
            if (context != null) {
                startActivity(PreferenceActivity[context, PreferenceScreen.HELP])
            }

            return@setOnPreferenceClickListener true
        }

        findPreference<Preference>(getString(R.string.pref_key_other_about))?.setOnPreferenceClickListener {
            openState.tryInvoke {
                aboutDialog.safeShow(fm, DialogFactory.Preference.Main.ABOUT, owner = this)
            }
            return@setOnPreferenceClickListener true
        }

        aboutDialog.onDismiss {
            openState.clear()

            if (aboutDialog.hideOpen) {
                viewModel.onUnlockDeveloper()
            }

            aboutDialog.clear()
        }
    }

    private fun onRateClick() {
        val context = context
        val packageName = context?.packageName ?: return

        /**
         * If marketUrl is not available -> open it via browser -> if can't - show error to user.
         */
        try {
            val intent = getSiteIntent(BuildConfig.MARKET_URL.plus(packageName))
            if (intent != null) {
                startActivity(intent)
            } else {
                toastControl.show(R.string.error_something_wrong)
            }
        } catch (e: ActivityNotFoundException) {
            val intent = getSiteIntent(BuildConfig.BROWSER_URL.plus(packageName))
            context.startActivitySafe(intent, toastControl)
        }
    }

    override fun setupDeveloper() {
        developerPreference?.isVisible = true
        developerPreference?.setOnPreferenceClickListener {
            val context = context
            if (context != null) {
                startActivity(PreferenceActivity[context, PreferenceScreen.DEVELOP])
            }

            return@setOnPreferenceClickListener true
        }
    }

    override fun updateThemeSummary(summary: String) {
        themePreference?.summary = summary
    }

    override fun showThemeDialog(value: Theme) = openState.tryInvoke {
        themeDialog.setArguments(value.ordinal)
            .safeShow(fm, DialogFactory.Preference.Main.THEME, owner = this)
    }
}
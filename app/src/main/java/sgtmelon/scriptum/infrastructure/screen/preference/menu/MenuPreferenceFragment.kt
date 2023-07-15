package sgtmelon.scriptum.infrastructure.screen.preference.menu

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.safedialog.utils.DialogStorage
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.infrastructure.dialogs.AboutDialog
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.Screens
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceFragment
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceScreen
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeChangeCallback
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnClickListener
import sgtmelon.scriptum.infrastructure.utils.extensions.startMarketActivity
import sgtmelon.scriptum.infrastructure.utils.extensions.startUrlActivity
import javax.inject.Inject

/**
 * Fragment with main preference menu
 */
class MenuPreferenceFragment : PreferenceFragment() {

    override val xmlId: Int = R.xml.preference_menu

    private val binding = MenuPreferenceBinding(fragment = this)

    @Inject lateinit var viewModel: MenuPreferenceViewModel

    private val dialogs by lazy { DialogFactory.Preference.Main(resources) }
    private val themeDialog = DialogStorage(
        DialogFactory.Preference.Main.THEME, owner = this,
        create = { dialogs.getTheme() },
        setup = { setupThemeDialog(it) }
    )
    private val aboutDialog = DialogStorage(
        DialogFactory.Preference.Main.ABOUT, owner = this,
        create = { dialogs.getAbout() },
        setup = { setupAboutDialog(it) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialogs()
    }

    override fun inject(component: ScriptumComponent) {
        component.getPreferenceBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setupView() {
        binding.apply {
            themeButton?.setOnClickListener { showThemeDialog(viewModel.theme) }
            backupButton?.setOnClickListener { it.openScreen(PreferenceScreen.BACKUP) }
            noteButton?.setOnClickListener { it.openScreen(PreferenceScreen.NOTES) }
            alarmButton?.setOnClickListener { it.openScreen(PreferenceScreen.ALARM) }

            binding.policyButton?.setOnClickListener {
                open.attempt {
                    val url = BuildConfig.PRIVACY_POLICY_URL
                    it.context.startUrlActivity(url, system?.toast)
                }
            }
            rateButton?.setOnClickListener {
                open.attempt { it.context.startMarketActivity(system?.toast) }
            }
            aboutButton?.setOnClickListener { showAboutDialog() }

            developerButton?.setOnClickListener { it.openScreen(PreferenceScreen.DEVELOP) }
        }
    }

    private fun Preference.openScreen(screen: PreferenceScreen) = open.attempt {
        startActivity(Screens.toPreference(context, screen))
    }

    override fun setupObservers() {
        viewModel.isDeveloper.observe(this) { binding.developerButton?.isVisible = it }
        viewModel.themeSummary.observe(this) { binding.themeButton?.summary = it }
    }

    override fun setupDialogs() {
        super.setupDialogs()

        themeDialog.restore()
        aboutDialog.release()
    }

    private fun setupThemeDialog(dialog: SingleDialog): Unit = with(dialog) {
        onPositiveClick {
            /**
             * Dismiss dialog before apply theme, because otherwise after activity recreation it
             * will be shown.
             */
            it.dismiss()

            viewModel.updateTheme(check)
            (activity as? ThemeChangeCallback)?.checkThemeChange()
        }
        onDismiss {
            themeDialog.release()
            open.clear()
        }
    }

    private fun setupAboutDialog(dialog: AboutDialog): Unit = with(dialog) {
        onDismiss {
            aboutDialog.release()
            open.clear()

            if (hideOpen) {
                unlockDeveloper()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        /** Need clear [open], because may be case when open new screens. */
        open.clear()
    }

    private fun showThemeDialog(value: Theme) = open.attempt {
        themeDialog.show { setArguments(value.ordinal) }
    }

    private fun showAboutDialog() = open.attempt { aboutDialog.show() }

    private fun unlockDeveloper() {
        val isDeveloper = viewModel.isDeveloper.value ?: return

        val toastId = if (isDeveloper) R.string.toast_dev_already else R.string.toast_dev_unlock
        system?.toast?.show(context, toastId)

        viewModel.unlockDeveloper()
    }
}
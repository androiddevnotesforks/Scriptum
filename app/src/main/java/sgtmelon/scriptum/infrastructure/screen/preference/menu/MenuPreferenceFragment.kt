package sgtmelon.scriptum.infrastructure.screen.preference.menu

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeChangeCallback
import sgtmelon.scriptum.infrastructure.utils.setOnClickListener
import sgtmelon.scriptum.infrastructure.utils.startMarketActivity

/**
 * Fragment with main preference menu
 */
class MenuPreferenceFragment : ParentPreferenceFragment() {

    override val xmlId: Int = R.xml.preference_menu

    private val binding = MenuPreferenceDataBinding(fragment = this)

    @Inject lateinit var viewModel: MenuPreferenceViewModel

    private val dialogs by lazy { DialogFactory.Preference.Main(context, fm) }
    private val themeDialog by lazy { dialogs.getTheme() }
    private val aboutDialog by lazy { dialogs.getAbout() }

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

    override fun setup() {
        binding.apply {
            themeButton?.setOnClickListener { showThemeDialog(viewModel.theme) }
            backupButton?.setOnClickListener { it.openScreen(PreferenceScreen.BACKUP) }
            noteButton?.setOnClickListener { it.openScreen(PreferenceScreen.NOTE) }
            alarmButton?.setOnClickListener { it.openScreen(PreferenceScreen.ALARM) }
            rateButton?.setOnClickListener { it.context.startMarketActivity(delegators.toast) }
            helpButton?.setOnClickListener { it.openScreen(PreferenceScreen.HELP) }
            aboutButton?.setOnClickListener { showAboutDialog() }
            developerButton?.setOnClickListener { it.openScreen(PreferenceScreen.DEVELOP) }
        }
    }

    private fun Preference.openScreen(key: PreferenceScreen) {
        startActivity(InstanceFactory.Preference[context, key])
    }

    override fun setupObservers() {
        viewModel.isDeveloper.observe(this) { binding.developerButton?.isVisible = it }
        viewModel.themeSummary.observe(this) { binding.themeButton?.summary = it }
    }

    private fun setupDialogs() {
        themeDialog.onPositiveClick {
            viewModel.updateTheme(themeDialog.check)
            (activity as? ThemeChangeCallback)?.checkThemeChange()
        }
        themeDialog.onDismiss { open.clear() }

        aboutDialog.onDismiss {
            open.clear()
            if (aboutDialog.hideOpen) {
                unlockDeveloper()
            }
        }
    }

    private fun showThemeDialog(value: Theme) = open.attempt {
        themeDialog.setArguments(value.ordinal)
            .safeShow(fm, DialogFactory.Preference.Main.THEME, owner = this)
    }

    private fun showAboutDialog() = open.attempt {
        aboutDialog.safeShow(fm, DialogFactory.Preference.Main.ABOUT, owner = this)
    }

    private fun unlockDeveloper() {
        val isDeveloper = viewModel.isDeveloper.value ?: return

        val toastId = if (isDeveloper) R.string.toast_dev_already else R.string.toast_dev_unlock
        delegators.toast.show(context, toastId)

        viewModel.unlockDeveloper()
    }
}
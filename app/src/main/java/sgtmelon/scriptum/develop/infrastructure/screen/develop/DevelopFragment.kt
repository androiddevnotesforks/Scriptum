package sgtmelon.scriptum.develop.infrastructure.screen.develop

import android.content.Context
import androidx.preference.Preference
import javax.inject.Inject
import sgtmelon.extensions.collect
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.develop.infrastructure.model.PrintType
import sgtmelon.scriptum.develop.infrastructure.screen.print.PrintDevelopActivity
import sgtmelon.scriptum.infrastructure.screen.Screens
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceFragment
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceScreen
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnClickListener

/**
 * Fragment of develop preferences.
 */
class DevelopFragment : PreferenceFragment() {

    override val xmlId: Int = R.xml.preference_develop

    private val binding = DevelopBinding(fragment = this)

    @Inject lateinit var viewModel: DevelopViewModel

    override fun inject(component: ScriptumComponent) {
        component.getDevelopBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setup() {
        binding.apply {
            printNoteButton?.setOnPrintClickListener(PrintType.NOTE)
            printBinButton?.setOnPrintClickListener(PrintType.BIN)
            printRollButton?.setOnPrintClickListener(PrintType.ROLL)
            printVisibleButton?.setOnPrintClickListener(PrintType.VISIBLE)
            printRankButton?.setOnPrintClickListener(PrintType.RANK)
            printAlarmButton?.setOnPrintClickListener(PrintType.ALARM)
            printKeyButton?.setOnPrintClickListener(PrintType.KEY)
            printFileButton?.setOnPrintClickListener(PrintType.FILE)

            alarmButton?.setOnClickListener { openRandomAlarm(it.context) }

            eternalButton?.setOnClickListener {
                startActivity(Screens.toPreference(it.context, PreferenceScreen.SERVICE))
            }

            resetButton?.setOnClickListener {
                viewModel.resetPreferences()
                system?.toast?.show(it.context, R.string.toast_dev_clear)
            }
        }
    }

    private fun Preference.setOnPrintClickListener(type: PrintType) {
        setOnClickListener {
            startActivity(PrintDevelopActivity[it.context, type])
        }
    }

    private fun openRandomAlarm(context: Context) {
        viewModel.randomNoteId.collect(owner = this) { id ->
            startActivity(Screens.Splash.toAlarm(context, id))
        }
    }
}
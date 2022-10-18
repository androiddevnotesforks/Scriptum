package sgtmelon.scriptum.develop.screen.develop

import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import javax.inject.Inject
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.develop.model.PrintType
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.utils.setOnClickListener

/**
 * Fragment of develop preferences.
 */
class DevelopFragment : ParentPreferenceFragment() {

    override val xmlId: Int = R.xml.preference_develop

    private val binding = DevelopDataBinding(lifecycle, fragment = this)

    @Inject lateinit var viewModel: DevelopViewModel

    override fun inject(component: ScriptumComponent) {
        component.getDevelopBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setup() {
        super.setup()

        binding.apply {
            printNoteButton?.setOnPrintClickListener(PrintType.NOTE)
            printBinButton?.setOnPrintClickListener(PrintType.BIN)
            printRollButton?.setOnPrintClickListener(PrintType.ROLL)
            printVisibleButton?.setOnPrintClickListener(PrintType.VISIBLE)
            printRankButton?.setOnPrintClickListener(PrintType.RANK)
            printAlarmButton?.setOnPrintClickListener(PrintType.ALARM)
            printKeyButton?.setOnPrintClickListener(PrintType.KEY)
            printFileButton?.setOnPrintClickListener(PrintType.FILE)

            introButton?.setOnClickListener { startActivity(InstanceFactory.Intro[it.context]) }
            alarmButton?.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.randomNoteId.collect { id ->
                        startActivity(InstanceFactory.Splash.getAlarm(it.context, id))
                    }
                }
            }

            eternalButton?.setOnClickListener {
                startActivity(InstanceFactory.Preference[it.context, PreferenceScreen.SERVICE])
            }

            resetButton?.setOnClickListener {
                viewModel.resetPreferences()
                delegators.toast.show(it.context, R.string.toast_dev_clear)
            }
        }
    }

    private fun Preference.setOnPrintClickListener(type: PrintType) {
        setOnClickListener {
            startActivity(InstanceFactory.Preference.Develop.Print[it.context, type])
        }
    }
}
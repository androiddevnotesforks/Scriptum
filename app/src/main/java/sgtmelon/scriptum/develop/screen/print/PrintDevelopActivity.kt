package sgtmelon.scriptum.develop.screen.print

import android.os.Bundle
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.extension.animateAlpha
import sgtmelon.scriptum.databinding.ActivityDevelopPrintBinding
import sgtmelon.scriptum.develop.adapter.PrintAdapter
import sgtmelon.scriptum.develop.model.PrintItem
import sgtmelon.scriptum.develop.model.PrintType
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.getTintDrawable
import sgtmelon.scriptum.infrastructure.utils.makeGone
import sgtmelon.scriptum.infrastructure.utils.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.makeVisible
import sgtmelon.scriptum.infrastructure.utils.setMarginInsets
import sgtmelon.scriptum.infrastructure.utils.setPaddingInsets
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Screen for print data of data base and preference.
 */
class PrintDevelopActivity : ThemeActivity<ActivityDevelopPrintBinding>() {

    override val layoutId: Int = R.layout.activity_develop_print

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    @Inject lateinit var viewModel: PrintDevelopViewModel

    private val adapter = PrintAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onSetup(bundle = savedInstanceState ?: intent.extras)
    }

    override fun inject(component: ScriptumComponent) {
        component.getPrintBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUpdateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveData(outState)
    }

    override fun setupView(type: PrintType) {
        val toolbar = binding?.toolbarInclude?.toolbar ?: return
        val recyclerView = binding?.recyclerView ?: return

        val titleText = getString(when (type) {
            PrintType.NOTE, PrintType.BIN -> R.string.pref_title_print_note
            PrintType.ROLL -> R.string.pref_title_print_roll
            PrintType.VISIBLE -> R.string.pref_title_print_visible
            PrintType.RANK -> R.string.pref_title_print_rank
            PrintType.ALARM -> R.string.pref_title_print_alarm
            PrintType.KEY -> R.string.pref_title_print_key
            PrintType.FILE -> R.string.pref_title_print_file
        }).lowercase()

        toolbar.title = getString(R.string.title_print, titleText)

        if (type == PrintType.NOTE) {
            toolbar.subtitle = getString(R.string.pref_summary_print_note)
        } else if (type == PrintType.BIN) {
            toolbar.subtitle = getString(R.string.pref_summary_print_bin)
        }

        toolbar.navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
        toolbar.setNavigationOnClickListener { finish() }

        recyclerView.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun setupInsets() {
        binding?.parentContainer?.setMarginInsets(InsetsDir.LEFT, InsetsDir.TOP, InsetsDir.RIGHT)
        binding?.recyclerView?.setPaddingInsets(InsetsDir.BOTTOM)
    }

    /**
     * For first time [recyclerView] visibility flag set inside xml file.
     */
    override fun beforeLoad() {
        binding?.infoInclude?.parentContainer?.makeGone()
        binding?.progressBar?.makeGone()
    }

    override fun showProgress() {
        binding?.progressBar?.makeVisible()
    }

    override fun onBindingList() {
        binding?.progressBar?.makeGone()

        if (adapter.itemCount == 0) {
            binding?.infoInclude?.parentContainer?.makeVisible()
            binding?.recyclerView?.makeInvisible()

            binding?.infoInclude?.parentContainer?.alpha = 0f
            binding?.infoInclude?.parentContainer?.animateAlpha(isVisible = true)
        } else {
            binding?.recyclerView?.makeVisible()

            binding?.infoInclude?.parentContainer?.animateAlpha(isVisible = false) {
                binding?.infoInclude?.parentContainer?.makeGone()
            }
        }
    }

    override fun notifyList(list: List<PrintItem>) = adapter.notifyList(list)

}
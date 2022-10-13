package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.cleanup.extension.animateAlpha
import sgtmelon.scriptum.cleanup.extension.getTintDrawable
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop.IPrintDevelopActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IPrintDevelopViewModel
import sgtmelon.scriptum.databinding.ActivityDevelopPrintBinding
import sgtmelon.scriptum.infrastructure.develop.PrintAdapter
import sgtmelon.scriptum.infrastructure.develop.PrintItem
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.setMarginInsets
import sgtmelon.scriptum.infrastructure.utils.setPaddingInsets
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Screen for print data of data base and preference.
 */
class PrintDevelopActivity : ThemeActivity<ActivityDevelopPrintBinding>(), IPrintDevelopActivity {

    override val layoutId: Int = R.layout.activity_develop_print

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    @Inject lateinit var viewModel: IPrintDevelopViewModel

    // TODO remove and use binding
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar_container) }
    private val emptyInfoView by lazy { findViewById<View>(R.id.print_info_include) }

    private val adapter = PrintAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onSetup(bundle = savedInstanceState ?: intent.extras)
    }

    override fun inject(component: ScriptumComponent) {
        component.getPrintBuilder()
            .set(activity = this)
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
        val toolbar = toolbar ?: return
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
        emptyInfoView?.visibility = View.GONE
        binding?.progressBar?.visibility = View.GONE
    }

    override fun showProgress() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    override fun onBindingList() {
        binding?.progressBar?.visibility = View.GONE

        if (adapter.itemCount == 0) {
            emptyInfoView?.visibility = View.VISIBLE
            binding?.recyclerView?.visibility = View.INVISIBLE

            emptyInfoView?.alpha = 0f
            emptyInfoView?.animateAlpha(isVisible = true)
        } else {
            binding?.recyclerView?.visibility = View.VISIBLE

            emptyInfoView?.animateAlpha(isVisible = false) {
                emptyInfoView?.visibility = View.GONE
            }
        }
    }

    override fun notifyList(list: List<PrintItem>) = adapter.notifyList(list)

}
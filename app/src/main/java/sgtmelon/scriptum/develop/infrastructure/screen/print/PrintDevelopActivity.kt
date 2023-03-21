package sgtmelon.scriptum.develop.infrastructure.screen.print

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.databinding.ActivityDevelopPrintBinding
import sgtmelon.scriptum.develop.infrastructure.adapter.PrintAdapter
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.develop.infrastructure.model.PrintType
import sgtmelon.scriptum.infrastructure.animation.ShowListAnimation
import sgtmelon.scriptum.infrastructure.bundle.BundleValue
import sgtmelon.scriptum.infrastructure.bundle.BundleValueImpl
import sgtmelon.scriptum.infrastructure.bundle.intent
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Print.Key
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListScreen
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.extensions.getTintDrawable
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.setMarginInsets
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.setPaddingInsets
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Screen for print data of data base and preference.
 */
class PrintDevelopActivity : ThemeActivity<ActivityDevelopPrintBinding>(),
    ListScreen<PrintItem> {

    override val layoutId: Int = R.layout.activity_develop_print

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    @Inject override lateinit var viewModel: PrintDevelopViewModel

    private val listAnimation = ShowListAnimation()

    private val type = BundleValueImpl<PrintType>(Key.TYPE)
    override val bundleValues: List<BundleValue> = listOf(type)

    override val adapter = PrintAdapter()
    override val layoutManager = LinearLayoutManager(this)
    override val recyclerView: RecyclerView? get() = binding?.recyclerView

    override fun inject(component: ScriptumComponent) {
        component.getPrintBuilder()
            .set(owner = this)
            .set(type.value)
            .build()
            .inject(activity = this)
    }

    override fun setupInsets() {
        super.setupInsets()

        binding?.parentContainer?.setMarginInsets(InsetsDir.LEFT, InsetsDir.TOP, InsetsDir.RIGHT)
        binding?.recyclerView?.setPaddingInsets(InsetsDir.BOTTOM)
    }

    override fun setupView() {
        super.setupView()
        setupToolbar()
        setupRecycler()
    }

    private fun setupToolbar() {
        val type = type.value
        val toolbar = binding?.appBar?.toolbar ?: return

        val titleId = when (type) {
            PrintType.NOTE, PrintType.BIN -> R.string.pref_title_print_note
            PrintType.ROLL -> R.string.pref_title_print_roll
            PrintType.VISIBLE -> R.string.pref_title_print_visible
            PrintType.RANK -> R.string.pref_title_print_rank
            PrintType.ALARM -> R.string.pref_title_print_alarm
            PrintType.KEY -> R.string.pref_title_print_key
            PrintType.FILE -> R.string.pref_title_print_file
        }
        toolbar.title = getString(R.string.title_print, getString(titleId).lowercase())

        val subtitleId = when (type) {
            PrintType.NOTE -> R.string.pref_summary_print_note
            PrintType.BIN -> R.string.pref_summary_print_bin
            else -> null
        }
        if (subtitleId != null) {
            toolbar.subtitle = getString(subtitleId)
        }

        toolbar.navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecycler() {
        binding?.recyclerView?.let {
            it.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
            it.setHasFixedSize(true) /** The height of all items absolutely the same. */
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.list.show.observe(this) {
            val binding = binding ?: return@observe

            listAnimation.startFade(
                it, binding.parentContainer, binding.progressBar,
                binding.recyclerView, binding.emptyInfo.root
            )
        }
        viewModel.list.data.observe(this) { onListUpdate(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
    }

    companion object {
        operator fun get(context: Context, type: PrintType): Intent {
            return context.intent<PrintDevelopActivity>(Key.TYPE to type)
        }
    }
}
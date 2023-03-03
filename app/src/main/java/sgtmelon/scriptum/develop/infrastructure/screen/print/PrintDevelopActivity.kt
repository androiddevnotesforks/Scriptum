package sgtmelon.scriptum.develop.infrastructure.screen.print

import android.content.Context
import android.content.Intent
import android.os.Bundle
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.databinding.ActivityDevelopPrintBinding
import sgtmelon.scriptum.develop.infrastructure.adapter.PrintAdapter
import sgtmelon.scriptum.develop.infrastructure.model.PrintType
import sgtmelon.scriptum.infrastructure.animation.ShowListAnimation
import sgtmelon.scriptum.infrastructure.bundle.SerializableBundleValue
import sgtmelon.scriptum.infrastructure.bundle.intent
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Print.Key
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
class PrintDevelopActivity : ThemeActivity<ActivityDevelopPrintBinding>() {

    override val layoutId: Int = R.layout.activity_develop_print

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    @Inject lateinit var viewModel: PrintDevelopViewModel

    private val listAnimation = ShowListAnimation()
    private val type = SerializableBundleValue<PrintType>(Key.TYPE)

    private val adapter = PrintAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        type.getData(bundle = savedInstanceState ?: intent.extras)
        super.onCreate(savedInstanceState)

        setupToolbar()
        setupRecycler()
    }

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        type.saveData(outState)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.showList.observe(this) {
            val binding = binding ?: return@observe

            listAnimation.startFade(
                it, binding.parentContainer, binding.progressBar,
                binding.recyclerView, binding.emptyInfo.parentContainer
            )
        }
        viewModel.itemList.observe(this) { adapter.notifyList(it) }
    }

    private fun setupToolbar() {
        val type = type.value ?: return
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
            it.adapter = adapter
        }
    }

    companion object {
        operator fun get(context: Context, type: PrintType): Intent {
            return context.intent<PrintDevelopActivity>(Key.TYPE to type)
        }
    }
}
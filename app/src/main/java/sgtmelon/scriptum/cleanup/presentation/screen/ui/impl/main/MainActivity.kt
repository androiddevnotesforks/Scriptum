package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main

import android.content.IntentFilter
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.cleanup.domain.model.key.MainPage
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.domain.model.state.OpenState
import sgtmelon.scriptum.cleanup.extension.InsetsDir
import sgtmelon.scriptum.cleanup.extension.addSystemInsetsMargin
import sgtmelon.scriptum.cleanup.extension.doOnApplyWindowInsets
import sgtmelon.scriptum.cleanup.extension.hideKeyboard
import sgtmelon.scriptum.cleanup.extension.updateMargin
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.show.HolderShowControl
import sgtmelon.scriptum.cleanup.presentation.factory.DialogFactory
import sgtmelon.scriptum.cleanup.presentation.factory.FragmentFactory
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IMainViewModel
import sgtmelon.scriptum.databinding.ActivityMainBinding
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Filter
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.widgets.delegators.GradientFabDelegator
import sgtmelon.test.idling.getIdling

/**
 * Screen which displays main menu and fragments: [RankFragment], [NotesFragment], [BinFragment].
 */
class MainActivity : ThemeActivity<ActivityMainBinding>(), IMainActivity {

    override val layoutId: Int = R.layout.activity_main

    @Inject lateinit var viewModel: IMainViewModel

    private val holderControl by lazy { HolderShowControl[binding?.toolbarHolder] }

    private val unbindNoteReceiver by lazy { UnbindNoteReceiver[this] }

    private val fragmentFactory = FragmentFactory.Main(fm)
    private val rankFragment by lazy { fragmentFactory.getRankFragment() }
    private val notesFragment by lazy { fragmentFactory.getNotesFragment() }
    private val binFragment by lazy { fragmentFactory.getBinFragment() }

    override val openState = OpenState()

    private val dialogFactory by lazy { DialogFactory.Main(context = this, fm = fm) }
    private val addDialog by lazy { dialogFactory.getAddDialog() }

    private var fabDelegator: GradientFabDelegator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fabDelegator = GradientFabDelegator(activity = this) { openAddDialog() }

        openState.get(savedInstanceState)
        viewModel.onSetup(savedInstanceState)

        registerReceiver(unbindNoteReceiver, IntentFilter(Filter.MAIN))

        getIdling().stop(IdlingTag.Intro.FINISH)
    }

    override fun inject(component: ScriptumComponent) {
        component.getMainBuilder()
            .set(activity = this)
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    override fun onResume() {
        super.onResume()

        /**
         * Clear [openState] after click on item container.
         */
        openState.changeEnabled = true
        openState.clear()

        /**
         * Show FAB on return to screen if it possible.
         */
        onFabStateChange(isVisible = true, withGap = false)
    }

    override fun onDestroy() {
        super.onDestroy()

        fabDelegator = null

        openState.clearBlockCallback()
        holderControl.onDestroy()
        viewModel.onDestroy()

        unregisterReceiver(unbindNoteReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        openState.save(outState)
        viewModel.onSaveData(outState)
    }

    /**
     * If touch was outside of [RankFragment.enterCard], when need hide keyboard
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action != MotionEvent.ACTION_DOWN) return super.dispatchTouchEvent(ev)

        rankFragment.enterCard?.let { if (!ev.onView(it)) hideKeyboard() }

        return super.dispatchTouchEvent(ev)
    }

    private fun openAddDialog() {
        openState.tryInvoke { addDialog.safeShow(fm, DialogFactory.Main.ADD, owner = this) }
    }

    override fun setupNavigation(@IdRes itemId: Int) {
        val animTime = resources.getInteger(R.integer.fragment_fade_time).toLong()
        binding?.menuNavigation?.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener openState.tryReturnInvoke {
                openState.block(animTime)
                viewModel.onSelectItem(it.itemId)

                return@tryReturnInvoke true
            } ?: false
        }
        binding?.menuNavigation?.selectedItemId = itemId

        addDialog.apply {
            onItemSelected(owner = this@MainActivity) { viewModel.onResultAddDialog(it.itemId) }
            onDismiss { openState.clear() }
        }
    }

    override fun setupInsets() {
        binding?.parentContainer?.doOnApplyWindowInsets { view, insets, _, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)

            /**
             * Need use this function (not [View.updateMargin]), because otherwise snackbar
             * inside [RankFragment] will handle this inset (and this cause strange bottom padding)
             */
            view.addSystemInsetsMargin(InsetsDir.BOTTOM, binding?.menuNavigation)

            return@doOnApplyWindowInsets insets
        }
    }


    override fun onFabStateChange(isVisible: Boolean, withGap: Boolean) {
        viewModel.onFabStateChange(isVisible, withGap)
    }

    override fun changeFabVisible(isVisible: Boolean, withGap: Boolean) {
        fabDelegator?.changeVisibility(isVisible, withGap)
    }

    override fun scrollTop(mainPage: MainPage) = onFragmentAdd(mainPage) {
        when (mainPage) {
            MainPage.RANK -> rankFragment.scrollTop()
            MainPage.NOTES -> notesFragment.scrollTop()
            MainPage.BIN -> binFragment.scrollTop()
        }
    }

    override fun showPage(pageFrom: MainPage, pageTo: MainPage) {
        lifecycleScope.launchWhenResumed {
            holderControl.display()

            val fm = fm
            fm.beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                .changeMainFragments(fm, pageFrom, pageTo)
                .commit()
        }
    }

    private fun FragmentTransaction.changeMainFragments(
        fm: FragmentManager,
        pageFrom: MainPage,
        pageTo: MainPage
    ): FragmentTransaction {
        if (fm.findFragmentByTag(pageFrom.getFragmentTag()) != null) {
            val fragmentFrom = pageFrom.getFragmentByName()

            hide(fragmentFrom)
            fragmentFrom.onPause()

            /**
             * Dismiss without callback happen inside [Fragment.onStop] function.
             * So be careful when call it manually.
             */
            if (fragmentFrom is RankFragment) {
                fragmentFrom.dismissSnackbar()
            }
        }

        val fragmentTo = pageTo.getFragmentByName()
        val fragmentToTag = pageTo.getFragmentTag()

        if (fm.findFragmentByTag(fragmentToTag) != null) {
            show(fragmentTo)
            fragmentTo.onResume()
        } else {
            add(R.id.fragment_container, fragmentTo, fragmentToTag)
        }

        return this
    }

    override fun openNoteScreen(noteType: NoteType) = openState.tryInvoke {
        startActivity(InstanceFactory.Note[this, noteType.ordinal])
    }

    //region Receiver callback

    override fun onReceiveUnbindNote(noteId: Long) {
        onFragmentAdd(MainPage.RANK) { rankFragment.onReceiveUnbindNote(noteId) }
        onFragmentAdd(MainPage.NOTES) { notesFragment.onReceiveUnbindNote(noteId) }
    }

    //endregion

    private fun MainPage.getFragmentByName(): Fragment = when (this) {
        MainPage.RANK -> rankFragment
        MainPage.NOTES -> notesFragment
        MainPage.BIN -> binFragment
    }

    @FragmentFactory.Main.Tag
    private fun MainPage.getFragmentTag(): String = when (this) {
        MainPage.RANK -> FragmentFactory.Main.Tag.RANK
        MainPage.NOTES -> FragmentFactory.Main.Tag.NOTES
        MainPage.BIN -> FragmentFactory.Main.Tag.BIN
    }

    /**
     * Func return was [MotionEvent] happen on [view]
     */
    private fun MotionEvent?.onView(view: View?): Boolean {
        if (view == null || this == null) return false

        return Rect().apply {
            view.getGlobalVisibleRect(this)
        }.contains(rawX.toInt(), rawY.toInt())
    }

    /**
     * Call [func] only if correct fragment added.
     */
    private inline fun onFragmentAdd(mainPage: MainPage, func: () -> Unit) {
        if (fm.findFragmentByTag(mainPage.getFragmentTag()) == null) return

        func()
    }

}
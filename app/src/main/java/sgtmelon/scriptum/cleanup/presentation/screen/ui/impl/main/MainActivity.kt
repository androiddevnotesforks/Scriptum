package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
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
import sgtmelon.scriptum.cleanup.presentation.receiver.screen.MainScreenReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IMainViewModel
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Filter
import sgtmelon.scriptum.infrastructure.widgets.delegators.GradientFabDelegator
import sgtmelon.test.idling.getIdling

/**
 * Screen which displays main menu and fragments: [RankFragment], [NotesFragment], [BinFragment].
 */
class MainActivity : AppActivity(), IMainActivity {

    @Inject internal lateinit var viewModel: IMainViewModel

    private val holderControl by lazy { HolderShowControl[toolbarHolder] }

    private val mainReceiver by lazy { MainScreenReceiver[viewModel, viewModel] }

    private val fragmentFactory = FragmentFactory.Main(fm)
    private val rankFragment by lazy { fragmentFactory.getRankFragment() }
    private val notesFragment by lazy { fragmentFactory.getNotesFragment() }
    private val binFragment by lazy { fragmentFactory.getBinFragment() }

    override val openState = OpenState()

    private val dialogFactory by lazy { DialogFactory.Main(context = this, fm = fm) }
    private val addDialog by lazy { dialogFactory.getAddDialog() }

    private val toolbarHolder by lazy { findViewById<View?>(R.id.main_toolbar_holder) }
    private val parentContainer by lazy { findViewById<ViewGroup?>(R.id.main_parent_container) }
    private val menuNavigation by lazy { findViewById<BottomNavigationView>(R.id.main_menu_navigation) }

    private var fabDelegator: GradientFabDelegator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getMainBuilder().set(activity = this).build()
            .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabDelegator = GradientFabDelegator(activity = this) { openAddDialog() }

        openState.get(savedInstanceState)
        viewModel.onSetup(savedInstanceState)
        registerReceiver(mainReceiver, IntentFilter(Filter.MAIN))

        getIdling().stop(IdlingTag.Intro.FINISH)
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

        openState.clearBlockCallback()
        holderControl.onDestroy()
        viewModel.onDestroy()
        unregisterReceiver(mainReceiver)
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
        menuNavigation?.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener openState.tryReturnInvoke {
                openState.block(animTime)
                viewModel.onSelectItem(it.itemId)

                return@tryReturnInvoke true
            } ?: false
        }
        menuNavigation?.selectedItemId = itemId

        addDialog.apply {
            onItemSelected(owner = this@MainActivity) { viewModel.onResultAddDialog(it.itemId) }
            onDismiss { openState.clear() }
        }
    }

    override fun setupInsets() {
        parentContainer?.doOnApplyWindowInsets { view, insets, _, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)

            /**
             * Need use this function (not [View.updateMargin]), because otherwise snackbar
             * inside [RankFragment] will handle this inset (and this cause strange bottom padding)
             */
            view.addSystemInsetsMargin(InsetsDir.BOTTOM, menuNavigation)

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
            add(R.id.main_fragment_container, fragmentTo, fragmentToTag)
        }

        return this
    }

    override fun openNoteScreen(noteType: NoteType) = openState.tryInvoke {
        startActivity(NoteActivity[this, noteType.ordinal])
    }

    //region Receiver callback

    override fun onReceiveUnbindNote(id: Long) {
        onFragmentAdd(MainPage.RANK) { rankFragment.onReceiveUnbindNote(id) }
        onFragmentAdd(MainPage.NOTES) { notesFragment.onReceiveUnbindNote(id) }
    }

    override fun onReceiveUpdateAlarm(id: Long) {
        onFragmentAdd(MainPage.NOTES) { notesFragment.onReceiveUpdateAlarm(id) }
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
    private fun onFragmentAdd(mainPage: MainPage, func: () -> Unit) {
        if (fm.findFragmentByTag(mainPage.getFragmentTag()) == null) return

        func()
    }

    companion object {
        operator fun get(context: Context) = Intent(context, MainActivity::class.java)
    }

}
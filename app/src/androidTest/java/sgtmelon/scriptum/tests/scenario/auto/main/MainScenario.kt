package sgtmelon.scriptum.tests.scenario.auto.main

import sgtmelon.scriptum.infrastructure.dialogs.sheet.AddSheetDialog
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerMainFabListener
import sgtmelon.scriptum.tests.ui.auto.main.MainDialogAddTest
import sgtmelon.scriptum.tests.ui.auto.main.MainDialogHelpTest
import sgtmelon.scriptum.tests.ui.auto.main.MainFabTest
import sgtmelon.scriptum.tests.ui.auto.main.MainPageTest
import sgtmelon.scriptum.tests.ui.auto.main.MainScrollTopTest
import sgtmelon.scriptum.tests.ui.control.main.MainFabGradientTest

/**
 * Scenarios for test [MainActivity].
 */
@Suppress("unused")
interface MainScenario {

    /** For add FAB. */
    interface Fab {

        /**
         * Check fab hide/show work while switching between navigation pages.
         * - Show fab only in [MainPage.NOTES]
         */
        fun hideFabOnDifferentPages() = MainFabTest().pageSelect()

        /**
         * Fab hide/show while scroll screen and change page.
         * - Show fab only in [MainPage.NOTES]
         * - Hide when scroll down [MainPage.NOTES] screen, and show up fab when scroll up.
         */
        fun scrollScreenAndPageChange() = MainFabTest().scrollAndPageChange()

        /**
         * If fab was hide (when [MainActivity] reach onPause state) - it must be displayed after
         * onResume state.
         */
        fun showFabAfterScreenResume() = MainFabTest().screenResumeState()

        /**
         * If fab was hide - it must be displayed after [RecyclerMainFabListener.FAB_STANDSTILL_TIME].
         */
        fun showFabAfterLongAwait() = MainFabTest().standstill()

        /** Manually check of how gradient looks in different themes. */
        fun gradientLook() = MainFabGradientTest()

    }

    /**
     * Check scroll top feature in [MainActivity]. When you can simply reach top of list, by
     * second tap on current navigation page.
     * - Assert item in list by position, before and after double tab click happened (feature call).
     */
    fun scrollListTopOnTabDoubleClick() = MainScrollTopTest()

    /** Check switch between navigation view items. */
    interface Page {

        /**
         * Check menu works and show needed fragment (screen).
         * - Compare page and opened fragment/screen.
         */
        fun changePages() = MainPageTest().correctPage()

        /**
         * Check prone rotation and selected page.
         * - Compare menu and screen before+after rotation.
         */
        fun rotate() = with(MainPageTest()) {
            rotateRankPage()
            rotateNotesPage()
            rotateBinPage()
        }
    }

    /** Tests for [AddSheetDialog] (create note) in main screen. */
    interface DialogAdd {

        /** Check dialog close.
         * - With back button
         * - With swipe
         */
        fun close() = MainDialogAddTest().close()

        /**
         * Create available notes.
         * - Note screen opened and asserted
         */
        fun createNotes() = with(MainDialogAddTest()) {
            createTextNote()
            createRollNote()
        }

        /** Similar to [close], but with rotation before close itself. */
        fun rotateClose() = MainDialogAddTest().rotateClose()

        /** Similar to [createNotes], but with rotation before click item. */
        fun rotateWork() = MainDialogAddTest().rotateWork()
    }

    interface DialogHelp {

        /**
         * Check dialog close.
         * - With dialog button (back press is not available)
         */
        fun close() = MainDialogHelpTest().close()

        /** Check dialog (after closed) will not appear onResume screen state. */
        fun displayAfterResume() = MainDialogHelpTest().displayAfterResume()

        /** Button work - open app settings. */
        fun openSettings() = MainDialogHelpTest().openSettings()

        /** Button work - open eternal service channel settings. */
        fun openChannel() = MainDialogHelpTest().openChannel()

        /** Similar to [close], but with rotation before close itself. */
        fun rotateClose() = MainDialogHelpTest().rotateClose()

        /** Similar to [openSettings]/[openChannel], but with rotation before click button. */
        fun rotateWork() = MainDialogHelpTest().rotateWork()
    }

}
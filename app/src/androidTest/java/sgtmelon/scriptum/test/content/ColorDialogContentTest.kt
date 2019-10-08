package sgtmelon.scriptum.test.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.dialog.ColorDialogUi

/**
 * Test for [ColorDialogUi.Item]
 */
@RunWith(AndroidJUnit4::class)
class ColorDialogContentTest : ParentUiTest() {

    @Test fun lightTheme() = startTest(Theme.LIGHT)

    @Test fun darkTheme() = startTest(Theme.DARK)

    private fun startTest(@Theme theme: Int) = data.createText().let {
        iPreferenceRepo.theme = theme

        launch {
            mainScreen {
                addDialog { createText(it) { controlPanel { onColor { onAssertAll() } } } }
            }
        }
    }

}
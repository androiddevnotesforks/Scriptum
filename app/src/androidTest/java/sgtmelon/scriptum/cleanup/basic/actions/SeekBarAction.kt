package sgtmelon.scriptum.cleanup.basic.actions

import android.view.View
import android.widget.SeekBar
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher

/**
 * Custom action for change progress inside [SeekBar].
 */
class SeekBarAction(private val progress: Int) : ViewAction {

    override fun perform(uiController: UiController?, view: View?) {
        (view as? SeekBar)?.progress = progress
    }

    override fun getDescription(): String = "Set progress ($progress) on SeekBar"

    override fun getConstraints(): Matcher<View> = isAssignableFrom(SeekBar::class.java)

}
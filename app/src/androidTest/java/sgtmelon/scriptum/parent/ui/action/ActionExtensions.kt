package sgtmelon.scriptum.parent.ui.action

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.test.cappuccino.automator.CommandAutomator
import sgtmelon.test.cappuccino.utils.longClick

// TODO add lint for use this extension over simple longClick
fun Matcher<View>.longClick(automator: CommandAutomator) = automator.smallLongPress { longClick() }
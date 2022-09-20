package sgtmelon.scriptum.cleanup.ui.screen.preference.help

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.help.HelpDisappearActivity
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.scriptum.cleanup.ui.part.toolbar.SimpleToolbar
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withCard
import sgtmelon.test.cappuccino.utils.withMenuDrawable
import sgtmelon.test.cappuccino.utils.withMenuTitle
import sgtmelon.test.cappuccino.utils.withText

/**
 * Class for UI control of [HelpDisappearActivity].
 */
class HelpDisappearScreen : ParentUi() {

    //region Views

    private val parentContainer = getViewById(R.id.parent_container)

    private val toolbar = SimpleToolbar(
        R.string.pref_title_help_notification_disappear,
        withBack = true
    )
    private val videoItem = getViewById(R.id.item_video_lesson)

    private val contentCard = getViewById(R.id.content_card)
    private val guideText = getViewById(R.id.content_text)

    private val buttonCard = getViewById(R.id.button_card)
    private val settingsButton = getViewById(R.id.settings_button)

    //endregion

    fun onClickClose() {
        toolbar.getToolbarButton().click()
    }

    fun openVideo() {
        videoItem.click()
    }

    fun openSettings() {
        TODO("test will crash next ones")
        settingsButton.click()
    }

    fun assert() = apply {
        parentContainer.isDisplayed()

        toolbar.contentContainer
            .withMenuDrawable(R.id.item_video_lesson, R.drawable.ic_video, R.attr.clContent)
            .withMenuTitle(R.id.item_video_lesson, R.string.menu_video_lesson)
        videoItem.isDisplayed()

        contentCard.isDisplayed().withCard(
            R.attr.clBackgroundView,
            R.dimen.text_card_radius,
            R.dimen.text_card_elevation
        )
        guideText.isDisplayed().withText(
            R.string.help_notification_disappear,
            R.attr.clContent,
            R.dimen.text_18sp
        )

        buttonCard.isDisplayed().withCard(
            R.attr.clBackgroundView,
            R.dimen.text_card_radius,
            R.dimen.text_card_elevation
        )
        settingsButton.isDisplayed().withText(
            R.string.help_settings_button,
            R.attr.clAccent,
            R.dimen.button_borderless_text
        )
    }

    companion object {
        operator fun invoke(func: HelpDisappearScreen.() -> Unit): HelpDisappearScreen {
            return HelpDisappearScreen().assert().apply(func)
        }
    }
}
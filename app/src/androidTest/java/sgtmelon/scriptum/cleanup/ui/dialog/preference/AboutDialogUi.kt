package sgtmelon.scriptum.cleanup.ui.dialog.preference

import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.dialog.AboutDialog
import sgtmelon.scriptum.cleanup.ui.IDialogUi
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withDrawable
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withText

/**
 * Class for UI control of [AboutDialog].
 */
class AboutDialogUi : ParentUi(), IDialogUi {

    //region Views

    private val parentContainer = getViewById(R.id.about_parent_container)
    private val logoImage = getViewById(R.id.about_logo_image)
    private val appText = getViewById(R.id.about_app_text)
    private val versionText = getViewById(R.id.about_version_text)
    private val developerText = getViewById(R.id.about_developer_text)
    private val designerText = getViewById(R.id.about_designer_text)
    private val emailText = getViewById(R.id.about_email_text)

    //endregion

    fun assert() = apply {
        parentContainer.isDisplayed()

        logoImage.isDisplayed()
            .withSize(R.dimen.icon_128dp, R.dimen.icon_128dp)
            .withDrawable(R.mipmap.img_logo)

        appText.isDisplayed()
            .withText(R.string.app_name, R.attr.clContent, R.dimen.text_18sp)
        versionText.isDisplayed()
            .withText(BuildConfig.VERSION_NAME, R.attr.clContentSecond, R.dimen.text_14sp)
        developerText.isDisplayed()
            .withText(R.string.dialog_about_developer, R.attr.clContent, R.dimen.text_16sp)
        designerText.isDisplayed()
            .withText(R.string.dialog_about_logo_designer, R.attr.clContent, R.dimen.text_16sp)
        emailText.isDisplayed()
            .withText(R.string.dialog_about_email, R.attr.clContentSecond, R.dimen.text_14sp)
    }

    companion object {
        operator fun invoke(func: AboutDialogUi.() -> Unit): AboutDialogUi {
            return AboutDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}
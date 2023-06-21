package sgtmelon.scriptum.source.ui.screen.dialogs.preference

import android.view.ViewGroup
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.dialog.AboutDialog
import sgtmelon.scriptum.source.ui.feature.DialogUi
import sgtmelon.scriptum.source.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withBackgroundDrawable
import sgtmelon.test.cappuccino.utils.withDrawable
import sgtmelon.test.cappuccino.utils.withParent
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withSizeCode
import sgtmelon.test.cappuccino.utils.withText

/**
 * Class for UI control of [AboutDialog].
 */
class AboutDialogUi : UiPart(),
    DialogUi {

    private val parentContainer = getView(R.id.about_parent_container)
    private val contentContainer = getView(R.id.about_content_container)

    private val logoImage = getView(R.id.about_logo_image)
    private val appText = getView(R.id.about_app_text)
    private val versionText = getView(R.id.about_version_text)
    private val developerText = getView(R.id.about_developer_text)
    private val designerText = getView(R.id.about_designer_text)
    private val emailText = getView(R.id.about_email_text)

    fun assert() = apply {
        parentContainer.isDisplayed()

        contentContainer.isDisplayed()
            .withParent(parentContainer)
            .withBackgroundDrawable(R.drawable.bg_dialog)

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
            .withText(R.string.dialog_about_email, R.attr.clAccent, R.dimen.text_16sp)
    }

    companion object {
        inline operator fun invoke(func: AboutDialogUi.() -> Unit): AboutDialogUi {
            return AboutDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}
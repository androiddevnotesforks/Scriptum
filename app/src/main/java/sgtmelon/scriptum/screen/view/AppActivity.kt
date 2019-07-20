package sgtmelon.scriptum.screen.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import sgtmelon.idling.AppIdlingResource
import sgtmelon.scriptum.R
import sgtmelon.scriptum.screen.callback.IAppActivity
import sgtmelon.scriptum.screen.callback.IAppViewModel
import sgtmelon.scriptum.screen.vm.AppViewModel

/**
 * Родительская activity приложения, от которой нужно наследоваться при необходимости обработки темы
 *
 * @author SerjantArbuz
 */
abstract class AppActivity : AppCompatActivity(), IAppActivity {

    private val iViewModel: IAppViewModel by lazy {
        ViewModelProviders.of(this).get(AppViewModel::class.java).apply {
            callback = this@AppActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iViewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()
        checkThemeChange()
    }

    override fun onStart() {
        super.onStart()
        AppIdlingResource.worker.stopHardWork()
    }

    override fun onDestroy() {
        super.onDestroy()
        iViewModel.onDestroy()
    }

    fun checkThemeChange() {
        if (!iViewModel.isThemeChange()) return

        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        startActivity(intent)
    }

}
package sgtmelon.scriptum.screen.view.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.screen.callback.notification.AlarmCallback
import sgtmelon.scriptum.screen.view.AppActivity

/**
 * Экран с уведомлением запущенным по таймеру
 *
 * @author SerjantArbuz
 */
class AlarmActivity : AppActivity(), AlarmCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
    }

    companion object {
        fun getInstance(context: Context) = Intent(context, AlarmActivity::class.java)
    }

}
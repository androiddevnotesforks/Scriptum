package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.IAlarmViewModel

/**
 * ViewModel for [AlarmActivity]
 *
 * @author SerjantArbuz
 */
class AlarmViewModel(application: Application) : ParentViewModel<IAlarmActivity>(application),
        IAlarmViewModel {

    private var id: Long = NoteData.Default.ID
    private var color: Int = iPreferenceRepo.defaultColor

    private lateinit var noteModel: NoteModel

    private var melodyPlayer: MediaPlayer? = null
    private val longWaitHandler = Handler()

    override fun onSetup() {
        callback?.setupView(iPreferenceRepo.theme)
    }

    // TODO #RELEASE Обработка id = -1
    // TODO #RELEASE Убирать уведомление из бд при старте (чтобы не было индикатора на заметке) и потом уже обрабатывать остановку приложения, нажатие на кнопки
    override fun onSetupData(bundle: Bundle?) {
        if (bundle != null) {
            id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
            color = bundle.getInt(NoteData.Intent.COLOR, iPreferenceRepo.defaultColor)
        }

        if (!::noteModel.isInitialized) {
            noteModel = iRoomRepo.getNoteModel(id)
        }

        melodyPlayer = MediaPlayer.create(context, Uri.parse(iPreferenceRepo.getMelodyList()[0].uri))
        melodyPlayer?.start()

        longWaitHandler.postDelayed({ callback?.finish() }, 15000)

        callback?.notifyDataSetChanged(noteModel)
    }

    override fun onStart() {
        callback?.let {
            val theme = iPreferenceRepo.theme
            it.startRippleAnimation(theme, context.getAppSimpleColor(color,
                    if (theme == Theme.light) ColorShade.ACCENT else ColorShade.DARK
            ))

            it.startControlFadeAnimation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        longWaitHandler.removeCallbacksAndMessages(null)
        melodyPlayer?.stop()
    }

    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putLong(NoteData.Intent.ID, id)
        putInt(NoteData.Intent.COLOR, color)
    }

    // TODO убираем уведомление из бд
    override fun onClickNote() {
        callback?.apply {
            startActivity(with(noteModel.noteEntity) { NoteActivity.getInstance(context, type, id) })
            finish()
        }
    }

    override fun onClickDisable() {
        context.showToast(text = "CLICK DISABLE")
        callback?.finish()
    }

    override fun onClickPostpone() {
        context.showToast(text = "CLICK POSTPONE")
        callback?.finish()
    }

}
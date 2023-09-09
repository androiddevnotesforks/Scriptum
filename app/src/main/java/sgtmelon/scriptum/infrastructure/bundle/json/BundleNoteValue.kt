package sgtmelon.scriptum.infrastructure.bundle.json

import sgtmelon.extensions.decode
import sgtmelon.extensions.encode
import sgtmelon.scriptum.infrastructure.bundle.BundleJsonValue
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Key
import sgtmelon.scriptum.infrastructure.model.init.NoteInit

class BundleNoteValue : BundleJsonValue<NoteInit>(Key.INIT) {
    override fun decode(string: String?): NoteInit? = string?.decode()
    override fun encode(): String? = value.encode()
}
package sgtmelon.handynotes.office.annot.def;

import androidx.annotation.StringDef;

@StringDef({DefNote.ID, DefNote.TYPE, DefNote.CREATE, DefNote.OPEN})
public @interface DefNote {

    String ID = "NOTE_ID";
    String TYPE = "NOTE_TYPE";
    String CREATE = "NOTE_CREATE";

    String OPEN = "NOTE_OPEN";

}

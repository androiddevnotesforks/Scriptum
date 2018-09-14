package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.StringDef;

@StringDef({DefNote.ID, DefNote.TYPE, DefNote.CREATE, DefNote.OPEN})
public @interface DefNote {

    String ID = "NT_ID";
    String TYPE = "NT_TYPE";
    String CREATE = "NT_CREATE";

    String OPEN = "NT_OPEN";

}

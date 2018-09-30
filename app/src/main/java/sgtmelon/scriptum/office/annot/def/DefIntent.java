package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.StringDef;

@StringDef({
        DefIntent.STATE_PAGE,
        DefIntent.STATE_OPEN,
        DefIntent.STATE_NOTE,

        DefIntent.STATUS_OPEN,

        DefIntent.NOTE_ID,
        DefIntent.NOTE_TYPE
})
public @interface DefIntent {

    String STATE_PAGE = "INTENT_STATE_PAGE",
            STATE_OPEN = "INTENT_STATE_OPEN",
            STATE_NOTE = "INTENT_STATE_NOTE";

    String STATUS_OPEN = "INTENT_STATUS_OPEN";

    String NOTE_ID = "INTENT_NOTE_ID",
            NOTE_TYPE = "INTENT_NOTE_TYPE";


}

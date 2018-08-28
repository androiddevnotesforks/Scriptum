package sgtmelon.handynotes.office.annot;

import androidx.annotation.StringDef;

/**
 * Ключи для нахождения фрагментов после поворота экрана
 */
@StringDef({
        Frg.RANK,
        Frg.NOTES,
        Frg.BIN,

        Frg.TEXT,
        Frg.ROLL
})
public @interface Frg {

    //ActMain
    String RANK = "FRG_RANK",
            NOTES = "FRG_NOTES",
            BIN = "FRG_BIN";

    //ActNote
    String TEXT = "FRG_TEXT",
            ROLL = "FRG_ROLL";

}

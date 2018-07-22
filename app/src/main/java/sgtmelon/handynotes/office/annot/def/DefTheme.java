package sgtmelon.handynotes.office.annot.def;

import androidx.annotation.IntDef;

@IntDef({DefTheme.light, DefTheme.dark})
public @interface DefTheme {

    int light = 0, dark = 1;

}

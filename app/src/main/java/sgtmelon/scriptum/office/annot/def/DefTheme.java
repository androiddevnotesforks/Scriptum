package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

/**
 * Ключи темы приложения
 */
@IntDef({
        DefTheme.light,
        DefTheme.dark
})
public @interface DefTheme {

    int light = 0, dark = 1;

}

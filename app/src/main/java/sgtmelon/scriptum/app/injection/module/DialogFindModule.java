package sgtmelon.scriptum.app.injection.module;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import javax.inject.Named;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.safedialog.library.ColorDialog;
import sgtmelon.safedialog.library.MessageDialog;
import sgtmelon.safedialog.library.MultiplyDialog;
import sgtmelon.safedialog.library.OptionsDialog;
import sgtmelon.safedialog.library.RenameDialog;
import sgtmelon.safedialog.library.SheetDialog;
import sgtmelon.safedialog.library.SingleDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.ArchScope;
import sgtmelon.scriptum.element.InfoDialog;
import sgtmelon.scriptum.element.SortDialog;
import sgtmelon.scriptum.office.annot.ColorAnn;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.ThemeDef;
import sgtmelon.scriptum.office.utils.PrefUtils;

@Module
public final class DialogFindModule {

    @Provides
    @ArchScope
    RenameDialog provideRenameDialog(Context context, FragmentManager fm) {
        RenameDialog renameDialog = (RenameDialog) fm.findFragmentByTag(DialogDef.RENAME);
        if (renameDialog == null) {
            renameDialog = new RenameDialog();
        }

        final Resources.Theme theme = context.getTheme();
        final TypedValue attrs = new TypedValue();

        theme.resolveAttribute(R.attr.clText, attrs, true);
        renameDialog.setColorText(attrs.data);

        theme.resolveAttribute(R.attr.clSecond, attrs, true);
        renameDialog.setColorHint(attrs.data);

        renameDialog.setTextHint(context.getString(R.string.hint_enter_rank_rename));
        renameDialog.setTextLength(context.getResources().getInteger(R.integer.length_note_name));

        return renameDialog;
    }

    @Provides
    @ArchScope
    SheetDialog provideSheetDialog(FragmentManager fm) {
        SheetDialog sheetDialogAdd = (SheetDialog) fm.findFragmentByTag(DialogDef.SHEET);
        if (sheetDialogAdd == null) {
            sheetDialogAdd = new SheetDialog();
        }
        return sheetDialogAdd;
    }

    @Provides
    @ArchScope
    OptionsDialog provideOptionsDialog(FragmentManager fm) {
        OptionsDialog optionsDialog = (OptionsDialog) fm.findFragmentByTag(DialogDef.OPTIONS);
        if (optionsDialog == null) {
            optionsDialog = new OptionsDialog();
        }

        return optionsDialog;
    }

    @Provides
    @ArchScope
    @Named(DialogDef.CLEAR_BIN)
    MessageDialog provideClearBinDialog(Context context, FragmentManager fm) {
        MessageDialog dlgClearBin = (MessageDialog) fm.findFragmentByTag(DialogDef.CLEAR_BIN);
        if (dlgClearBin == null) {
            dlgClearBin = new MessageDialog();
        }

        dlgClearBin.setTitle(context.getString(R.string.dialog_title_clear_bin));
        dlgClearBin.setMessage(context.getString(R.string.dialog_text_clear_bin));

        return dlgClearBin;
    }

    @Provides
    @ArchScope
    @Named(DialogDef.CONVERT)
    MessageDialog provideConvertDialog(Context context, FragmentManager fm) {
        MessageDialog dlgConvert = (MessageDialog) fm.findFragmentByTag(DialogDef.CONVERT);
        if (dlgConvert == null) {
            dlgConvert = new MessageDialog();
        }

        dlgConvert.setTitle(context.getString(R.string.dialog_title_convert));

        return dlgConvert;
    }

    @Provides
    @ArchScope
    @Named(DialogDef.RANK)
    MultiplyDialog provideRankDialog(Context context, FragmentManager fm) {
        MultiplyDialog dlgRank = (MultiplyDialog) fm.findFragmentByTag(DialogDef.RANK);
        if (dlgRank == null) {
            dlgRank = new MultiplyDialog();
        }

        dlgRank.setTitle(context.getString(R.string.dialog_title_rank));

        return dlgRank;
    }

    @Provides
    @ArchScope
    ColorDialog provideColorDialog(Context context, FragmentManager fm) {
        ColorDialog colorDialog = (ColorDialog) fm.findFragmentByTag(DialogDef.COLOR);
        if (colorDialog == null) {
            colorDialog = new ColorDialog();
        }

        switch (PrefUtils.getTheme(context)) {
            case ThemeDef.light:
                colorDialog.setFillColor(ColorAnn.cl_light);
                colorDialog.setStrokeColor(ColorAnn.cl_dark);
                colorDialog.setCheckColor(ColorAnn.cl_dark);
                break;
            case ThemeDef.dark:
                colorDialog.setFillColor(ColorAnn.cl_dark);
                colorDialog.setStrokeColor(ColorAnn.cl_dark);
                colorDialog.setCheckColor(ColorAnn.cl_light);
                break;
        }

        colorDialog.setColumnCount(context.getResources().getInteger(R.integer.column_color));

        return colorDialog;
    }

    @Provides
    @ArchScope
    SortDialog provideSortDialog(FragmentManager fm) {
        SortDialog sortDialog = (SortDialog) fm.findFragmentByTag(DialogDef.SORT);
        if (sortDialog == null) {
            sortDialog = new SortDialog();
        }

        return sortDialog;
    }

    @Provides
    @ArchScope
    @Named(DialogDef.SAVE_TIME)
    SingleDialog provideSaveTimeDialog(Context context, FragmentManager fm) {
        SingleDialog dlgSaveTime = (SingleDialog) fm.findFragmentByTag(DialogDef.SAVE_TIME);
        if (dlgSaveTime == null) {
            dlgSaveTime = new SingleDialog();
        }

        dlgSaveTime.setTitle(context.getString(R.string.pref_save_time_title));
        dlgSaveTime.setRows(context.getResources().getStringArray(R.array.pref_save_time_text));

        return dlgSaveTime;
    }

    @Provides
    @ArchScope
    @Named(DialogDef.THEME)
    SingleDialog provideThemeDialog(Context context, FragmentManager fm) {
        SingleDialog dlgTheme = (SingleDialog) fm.findFragmentByTag(DialogDef.THEME);
        if (dlgTheme == null) {
            dlgTheme = new SingleDialog();
        }

        dlgTheme.setTitle(context.getString(R.string.pref_theme_title));
        dlgTheme.setRows(context.getResources().getStringArray(R.array.pref_theme_text));

        return dlgTheme;
    }

    @Provides
    @ArchScope
    InfoDialog provideInfoDialog(FragmentManager fm) {
        InfoDialog infoDialog = (InfoDialog) fm.findFragmentByTag(DialogDef.INFO);
        if (infoDialog == null) {
            infoDialog = new InfoDialog();
        }

        return infoDialog;
    }

}

package sgtmelon.scriptum.app.injection.module;

import android.content.Context;

import javax.inject.Named;

import androidx.fragment.app.FragmentManager;
import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.ArchScope;
import sgtmelon.scriptum.element.ColorDialog;
import sgtmelon.scriptum.element.InfoDialog;
import sgtmelon.scriptum.element.RenameDialog;
import sgtmelon.scriptum.element.SortDialog;
import sgtmelon.scriptum.element.common.MessageDialog;
import sgtmelon.scriptum.element.common.MultiplyDialog;
import sgtmelon.scriptum.element.common.OptionsDialog;
import sgtmelon.scriptum.element.common.SheetDialog;
import sgtmelon.scriptum.element.common.SingleDialog;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.ColorAnn;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.ThemeDef;

@Module
public final class DialogFindModule {

    @Provides
    @ArchScope
    RenameDialog provideRenameDialog(FragmentManager fm) {
        RenameDialog renameDialog = (RenameDialog) fm.findFragmentByTag(DialogDef.RENAME);
        if (renameDialog == null) renameDialog = new RenameDialog();

        return renameDialog;
    }

    @Provides
    @ArchScope
    SheetDialog provideSheetDialog(FragmentManager fm) {
        SheetDialog sheetDialogAdd = (SheetDialog) fm.findFragmentByTag(DialogDef.SHEET);
        if (sheetDialogAdd == null) sheetDialogAdd = new SheetDialog();
        return sheetDialogAdd;
    }

    @Provides
    @ArchScope
    OptionsDialog provideOptionsDialog(FragmentManager fm) {
        OptionsDialog optionsDialog = (OptionsDialog) fm.findFragmentByTag(DialogDef.OPTIONS);
        if (optionsDialog == null) optionsDialog = new OptionsDialog();

        return optionsDialog;
    }

    @Provides
    @ArchScope
    @Named(DialogDef.CLEAR_BIN)
    MessageDialog provideClearBinDialog(Context context, FragmentManager fm) {
        MessageDialog dlgClearBin = (MessageDialog) fm.findFragmentByTag(DialogDef.CLEAR_BIN);
        if (dlgClearBin == null) dlgClearBin = new MessageDialog();

        dlgClearBin.setTitle(context.getString(R.string.dialog_title_clear_bin));
        dlgClearBin.setMessage(context.getString(R.string.dialog_text_clear_bin));

        return dlgClearBin;
    }

    @Provides
    @ArchScope
    @Named(DialogDef.CONVERT)
    MessageDialog provideConvertDialog(Context context, FragmentManager fm) {
        MessageDialog dlgConvert = (MessageDialog) fm.findFragmentByTag(DialogDef.CONVERT);
        if (dlgConvert == null) dlgConvert = new MessageDialog();

        dlgConvert.setTitle(context.getString(R.string.dialog_title_convert));

        return dlgConvert;
    }

    @Provides
    @ArchScope
    @Named(DialogDef.RANK)
    MultiplyDialog provideRankDialog(Context context, FragmentManager fm) {
        MultiplyDialog dlgRank = (MultiplyDialog) fm.findFragmentByTag(DialogDef.RANK);
        if (dlgRank == null) dlgRank = new MultiplyDialog();

        dlgRank.setTitle(context.getString(R.string.dialog_title_rank));

        return dlgRank;
    }

    @Provides
    @ArchScope
    ColorDialog provideColorDialog(Context context, FragmentManager fm) {
        ColorDialog colorDialog = (ColorDialog) fm.findFragmentByTag(DialogDef.COLOR);
        if (colorDialog == null) colorDialog = new ColorDialog();

        switch (Help.Pref.getTheme(context)) {
            case ThemeDef.light:
                colorDialog.setIcons(ColorAnn.ic_light);
                colorDialog.setColors(ColorAnn.cl_dark);
                break;
            case ThemeDef.dark:
                colorDialog.setIcons(ColorAnn.ic_dark);
                colorDialog.setColors(ColorAnn.cl_light);
                break;
        }

        return colorDialog;
    }

    @Provides
    @ArchScope
    SortDialog provideSortDialog(FragmentManager fm) {
        SortDialog sortDialog = (SortDialog) fm.findFragmentByTag(DialogDef.SORT);
        if (sortDialog == null) sortDialog = new SortDialog();

        return sortDialog;
    }

    @Provides
    @ArchScope
    @Named(DialogDef.SAVE_TIME)
    SingleDialog provideSaveTimeDialog(Context context,FragmentManager fm) {
        SingleDialog dlgSaveTime = (SingleDialog) fm.findFragmentByTag(DialogDef.SAVE_TIME);
        if (dlgSaveTime == null) dlgSaveTime = new SingleDialog();

        dlgSaveTime.setTitle(context.getString(R.string.pref_save_time_title));
        dlgSaveTime.setRows(context.getResources().getStringArray(R.array.pref_save_time_text));

        return dlgSaveTime;
    }

    @Provides
    @ArchScope
    @Named(DialogDef.THEME)
    SingleDialog provideThemeDialog(Context context, FragmentManager fm) {
        SingleDialog dlgTheme = (SingleDialog) fm.findFragmentByTag(DialogDef.THEME);
        if (dlgTheme == null) dlgTheme = new SingleDialog();

        dlgTheme.setTitle(context.getString(R.string.pref_theme_title));
        dlgTheme.setRows(context.getResources().getStringArray(R.array.pref_theme_text));

        return dlgTheme;
    }

    @Provides
    @ArchScope
    InfoDialog provideInfoDialog(FragmentManager fm) {
        InfoDialog infoDialog = (InfoDialog) fm.findFragmentByTag(DialogDef.INFO);
        if (infoDialog == null) infoDialog = new InfoDialog();

        return infoDialog;
    }

}

package sgtmelon.scriptum.app.view.frg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.component.ComPref;
import sgtmelon.scriptum.app.injection.component.DaggerComPref;
import sgtmelon.scriptum.app.injection.module.ModBlankPref;
import sgtmelon.scriptum.app.view.act.ActDevelop;
import sgtmelon.scriptum.app.view.act.ActSettings;
import sgtmelon.scriptum.element.dialog.DlgColor;
import sgtmelon.scriptum.element.dialog.DlgInfo;
import sgtmelon.scriptum.element.dialog.DlgSort;
import sgtmelon.scriptum.element.dialog.common.DlgSingle;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefSort;
import sgtmelon.scriptum.office.st.StOpen;

public class FrgSettings extends PreferenceFragment {

    //region Variable
    private static final String TAG = "FrgSettings";

    @Inject
    ActSettings activity;
    @Inject
    FragmentManager fm;

    @Inject
    SharedPreferences pref;

    @Inject
    StOpen stOpen;
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            View list = view.findViewById(android.R.id.list);
            list.setPadding(0, 0, 0, 0);
        }
        return view;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        Log.i(TAG, "onCreate");

        ComPref comPref = DaggerComPref.builder()
                .modBlankPref(new ModBlankPref(this))
                .build();
        comPref.inject(this);

        if (savedInstanceState != null) stOpen.setOpen(savedInstanceState.getBoolean(DefDlg.OPEN));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");

        setupNotePref();
        setupSavePref();
        setupAppPref();
    }

    private Preference prefSort;
    private String valSort;
    @Inject
    DlgSort dlgSort;

    private Preference prefColor;
    private int valColor;
    @Inject
    DlgColor dlgColor;

    private void setupNotePref() {
        Log.i(TAG, "setupNotePref");

        prefSort = findPreference(getString(R.string.pref_key_sort));
        valSort = pref.getString(getString(R.string.pref_key_sort), DefSort.def);
        prefSort.setSummary(Help.Pref.getSortSummary(activity, pref.getString(getString(R.string.pref_key_sort), DefSort.def)));
        prefSort.setOnPreferenceClickListener(preference -> {
            if (!stOpen.isOpen()) {
                stOpen.setOpen(true);

                dlgSort.setArguments(valSort);
                dlgSort.show(fm, DefDlg.SORT);
            }
            return true;
        });

        dlgSort.setPositiveListener((dialogInterface, i) -> {
            valSort = dlgSort.getKeys();
            pref.edit().putString(getString(R.string.pref_key_sort), valSort).apply();

            String summary = Help.Pref.getSortSummary(activity, valSort);
            prefSort.setSummary(summary);
        });
        dlgSort.setNeutralListener((dialogInterface, i) -> {
            valSort = DefSort.def;
            pref.edit().putString(getString(R.string.pref_key_sort), valSort).apply();

            String summary = Help.Pref.getSortSummary(activity, valSort);
            prefSort.setSummary(summary);
        });
        dlgSort.setDismissListener(dialogInterface -> stOpen.setOpen(false));

        prefColor = findPreference(getString(R.string.pref_key_color));
        valColor = pref.getInt(getString(R.string.pref_key_color), getResources().getInteger(R.integer.pref_default_color));
        prefColor.setSummary(getResources().getStringArray(R.array.pref_text_color)[valColor]);
        prefColor.setOnPreferenceClickListener(preference -> {
            if (!stOpen.isOpen()) {
                stOpen.setOpen(true);

                dlgColor.setArguments(valColor);
                dlgColor.show(fm, DefDlg.COLOR);
            }
            return true;
        });

        dlgColor.setTitle(getString(R.string.pref_title_color));
        dlgColor.setPositiveListener((dialogInterface, i) -> {
            valColor = dlgColor.getCheck();

            pref.edit().putInt(getString(R.string.pref_key_color), valColor).apply();
            prefColor.setSummary(getResources().getStringArray(R.array.pref_text_color)[valColor]);
        });
        dlgColor.setDismissListener(dialogInterface -> stOpen.setOpen(false));
    }

    private Preference prefSaveTime;
    private int valSaveTime;
    @Inject
    @Named(DefDlg.SAVE_TIME)
    DlgSingle dlgSaveTime;

    private void setupSavePref() {
        Log.i(TAG, "setupSavePref");

        prefSaveTime = findPreference(getString(R.string.pref_key_save_time));
        valSaveTime = pref.getInt(getString(R.string.pref_key_save_time), getResources().getInteger(R.integer.pref_default_save_time));
        prefSaveTime.setSummary(getResources().getStringArray(R.array.pref_text_save_time)[valSaveTime]);
        prefSaveTime.setOnPreferenceClickListener(preference -> {
            if (!stOpen.isOpen()) {
                stOpen.setOpen(true);

                dlgSaveTime.setArguments(valSaveTime);
                dlgSaveTime.show(fm, DefDlg.SAVE_TIME);
            }
            return true;
        });

        dlgSaveTime.setTitle(getString(R.string.pref_title_save_time));
        dlgSaveTime.setName(getResources().getStringArray(R.array.pref_text_save_time));
        dlgSaveTime.setPositiveListener((dialogInterface, i) -> {
            valSaveTime = dlgSaveTime.getCheck();

            pref.edit().putInt(getString(R.string.pref_key_save_time), valSaveTime).apply();
            prefSaveTime.setSummary(dlgSaveTime.getName()[valSaveTime]);
        });
        dlgSaveTime.setDismissListener(dialogInterface -> stOpen.setOpen(false));

        CheckBoxPreference prefAutoSave = (CheckBoxPreference) findPreference(getString(R.string.pref_key_auto_save));
        prefAutoSave.setOnPreferenceChangeListener((preference, newValue) -> {
            prefSaveTime.setEnabled((Boolean) newValue);
            return true;
        });

        prefSaveTime.setEnabled(prefAutoSave.isChecked());
    }

    private Preference prefTheme;
    private int valTheme;
    @Inject
    @Named(DefDlg.THEME)
    DlgSingle dlgTheme;

    @Inject
    DlgInfo dlgInfo;

    private void setupAppPref() {
        Log.i(TAG, "setupAppPref");

        prefTheme = findPreference(getString(R.string.pref_key_theme));
        valTheme = pref.getInt(getString(R.string.pref_key_theme), getResources().getInteger(R.integer.pref_default_theme));
        prefTheme.setSummary(getResources().getStringArray(R.array.pref_text_theme)[valTheme]);
        prefTheme.setOnPreferenceClickListener(preference -> {
            if (!stOpen.isOpen()) {
                stOpen.setOpen(true);

                dlgTheme.setArguments(valTheme);
                dlgTheme.show(fm, DefDlg.THEME);
            }
            return true;
        });

        dlgTheme.setTitle(getString(R.string.pref_title_theme));
        dlgTheme.setName(getResources().getStringArray(R.array.pref_text_theme));
        dlgTheme.setPositiveListener(((dialogInterface, i) -> {
            valTheme = dlgTheme.getCheck();

            pref.edit().putInt(getString(R.string.pref_key_theme), valTheme).apply();
            prefTheme.setSummary(dlgTheme.getName()[valTheme]);

            activity.isThemeChange();
        }));
        dlgTheme.setDismissListener(dialogInterface -> stOpen.setOpen(false));

        Preference prefRate = findPreference(getString(R.string.pref_key_rate));
        prefRate.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            final String packageName = activity.getPackageName();

            try {
                intent.setData(Uri.parse("market://details?id=" + packageName));
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException exception) {
                intent.setData(Uri.parse("https://play.google.com/sore/apps/details?id=" + packageName));
                startActivity(intent);
            }
            return true;
        });

        Preference prefOtherAbout = findPreference(getString(R.string.pref_key_about));
        prefOtherAbout.setOnPreferenceClickListener(preference -> {
            if (!stOpen.isOpen()) {
                stOpen.setOpen(true);

                dlgInfo.show(fm, DefDlg.INFO);
            }
            return true;
        });

        dlgInfo.setLogoClick(view -> {
            Intent intent = new Intent(activity, ActDevelop.class);
            startActivity(intent);
        });
        dlgInfo.setDismissListener(dialogInterface -> stOpen.setOpen(false));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(DefDlg.OPEN, stOpen.isOpen());
    }

}
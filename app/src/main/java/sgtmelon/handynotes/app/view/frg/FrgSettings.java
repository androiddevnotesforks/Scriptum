package sgtmelon.handynotes.app.view.frg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.view.act.ActDevelop;
import sgtmelon.handynotes.app.view.act.ActSettings;
import sgtmelon.handynotes.element.dialog.DlgColor;
import sgtmelon.handynotes.element.dialog.DlgInfo;
import sgtmelon.handynotes.element.dialog.DlgSort;
import sgtmelon.handynotes.element.dialog.common.DlgSingle;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.Dlg;
import sgtmelon.handynotes.office.annot.def.DefSort;

public class FrgSettings extends PreferenceFragment {

    //region Variable
    private static final String TAG = "FrgSettings";

    private ActSettings activity;
    private FragmentManager fm;

    private SharedPreferences pref;
    //endregion

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        addPreferencesFromResource(R.xml.preference);

        activity = (ActSettings) getActivity();
        fm = activity.getSupportFragmentManager();

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        setupSortPref();
        setupNotePref();
        setupOtherPref();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            view.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorBackground));

            View list = view.findViewById(android.R.id.list);
            list.setPadding(0, 0, 0, 0);
        }
        return view;
    }

    private Preference prefSort;
    private String valSort;
    private DlgSort dlgSort;

    private void setupSortPref() {
        Log.i(TAG, "setupSortPref");

        prefSort = findPreference(getString(R.string.pref_key_sort));
        valSort = pref.getString(getString(R.string.pref_key_sort), DefSort.def);
        prefSort.setSummary(Help.Pref.getSortSummary(activity, pref.getString(getString(R.string.pref_key_sort), DefSort.def)));
        prefSort.setOnPreferenceClickListener(preference -> {
            if (!dlgSort.isVisible()) {
                dlgSort.setArguments(valSort);
                dlgSort.show(fm, Dlg.SORT);
            }
            return true;
        });

        dlgSort = (DlgSort) fm.findFragmentByTag(Dlg.SORT);
        if (dlgSort == null) dlgSort = new DlgSort();
        dlgSort.setPositiveButton((dialogInterface, i) -> {
            valSort = dlgSort.getKeys();
            pref.edit().putString(getString(R.string.pref_key_sort), valSort).apply();

            String summary = Help.Pref.getSortSummary(activity, valSort);
            prefSort.setSummary(summary);
        });
        dlgSort.setNeutralButton((dialogInterface, i) -> {
            valSort = DefSort.def;
            pref.edit().putString(getString(R.string.pref_key_sort), valSort).apply();

            String summary = Help.Pref.getSortSummary(activity, valSort);
            prefSort.setSummary(summary);
        });

    }

    private Preference prefColor;
    private int valColor;
    private DlgColor dlgColor;

    private Preference prefSaveTime;
    private int valSaveTime;
    private DlgSingle dlgSaveTime;

    private void setupNotePref() {
        Log.i(TAG, "setupNotePref");

        prefColor = findPreference(getString(R.string.pref_key_color));
        valColor = pref.getInt(getString(R.string.pref_key_color), getResources().getInteger(R.integer.pref_default_color));
        prefColor.setSummary(getResources().getStringArray(R.array.pref_text_color)[valColor]);
        prefColor.setOnPreferenceClickListener(preference -> {
            if (!dlgColor.isVisible()) {
                dlgColor.setArguments(valColor);
                dlgColor.show(fm, Dlg.COLOR);
            }
            return true;
        });

        dlgColor = (DlgColor) fm.findFragmentByTag(Dlg.COLOR);
        if (dlgColor == null) dlgColor = new DlgColor();
        dlgColor.setTitle(getString(R.string.pref_title_color));
        dlgColor.setPositiveButton((dialogInterface, i) -> {
            valColor = dlgColor.getCheck();

            pref.edit().putInt(getString(R.string.pref_key_color), valColor).apply();
            prefColor.setSummary(getResources().getStringArray(R.array.pref_text_color)[valColor]);
        });

        prefSaveTime = findPreference(getString(R.string.pref_key_save_time));
        valSaveTime = pref.getInt(getString(R.string.pref_key_save_time), getResources().getInteger(R.integer.pref_default_save_time));
        prefSaveTime.setSummary(getResources().getStringArray(R.array.pref_text_save_time)[valSaveTime]);
        prefSaveTime.setOnPreferenceClickListener(preference -> {
            if (!dlgSaveTime.isVisible()) {
                dlgSaveTime.setArguments(valSaveTime);
                dlgSaveTime.show(fm, Dlg.SAVE_TIME);
            }
            return true;
        });

        dlgSaveTime = (DlgSingle) fm.findFragmentByTag(Dlg.SAVE_TIME);
        if (dlgSaveTime == null) dlgSaveTime = new DlgSingle();

        dlgSaveTime.setTitle(getString(R.string.pref_title_save_time));
        dlgSaveTime.setName(getResources().getStringArray(R.array.pref_text_save_time));
        dlgSaveTime.setPositiveButton((dialogInterface, i) -> {
            valSaveTime = dlgSaveTime.getCheck();

            pref.edit().putInt(getString(R.string.pref_key_save_time), valSaveTime).apply();
            prefSaveTime.setSummary(dlgSaveTime.getName()[valSaveTime]);
        });

        CheckBoxPreference prefAutoSave = (CheckBoxPreference) findPreference(getString(R.string.pref_key_auto_save));
        prefAutoSave.setOnPreferenceChangeListener((preference, newValue) -> {
            prefSaveTime.setEnabled((Boolean) newValue);
            return true;
        });

        prefSaveTime.setEnabled(prefAutoSave.isChecked());
    }

    private Preference prefTheme;
    private int valTheme;
    private DlgSingle dlgTheme;

    private DlgInfo dlgInfo;

    private void setupOtherPref() {
        Log.i(TAG, "setupOtherPref");

        prefTheme = findPreference(getString(R.string.pref_key_theme));
        valTheme = pref.getInt(getString(R.string.pref_key_theme), getResources().getInteger(R.integer.pref_default_theme));
        prefTheme.setSummary(getResources().getStringArray(R.array.pref_text_theme)[valSaveTime]);
        prefTheme.setOnPreferenceClickListener(preference -> {
            if (!dlgTheme.isVisible()) dlgTheme.show(fm, Dlg.THEME);
            return true;
        });

        dlgTheme = (DlgSingle) fm.findFragmentByTag(Dlg.THEME);
        if (dlgTheme == null) dlgTheme = new DlgSingle();

        dlgTheme.setTitle(getString(R.string.pref_title_theme));
        dlgTheme.setName(getResources().getStringArray(R.array.pref_text_theme));
        dlgTheme.setPositiveButton(((dialogInterface, i) -> {
            valTheme = dlgTheme.getCheck();

            pref.edit().putInt(getString(R.string.pref_key_theme), valTheme).apply();
            prefTheme.setSummary(dlgTheme.getName()[valTheme]);
        }));

        dlgInfo = (DlgInfo) fm.findFragmentByTag(Dlg.INFO);
        if (dlgInfo == null) dlgInfo = new DlgInfo();
        dlgInfo.setLogoClick(view -> {
            Intent intent = new Intent(activity, ActDevelop.class);
            startActivity(intent);
        });

        Preference prefOtherAbout = findPreference(getString(R.string.pref_key_about));
        prefOtherAbout.setOnPreferenceClickListener(preference -> {
            if (!dlgInfo.isVisible()) dlgInfo.show(fm, Dlg.INFO);
            return true;
        });
    }

}
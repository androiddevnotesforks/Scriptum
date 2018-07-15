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

    //TODO смена темы

    private static final String TAG = "FrgSettings";

    private ActSettings activity;
    private FragmentManager fm;

    private SharedPreferences pref;

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
    private String sort;
    private DlgSort dlgSort;

    private void setupSortPref() {
        Log.i(TAG, "setupSortPref");

        prefSort = findPreference(getString(R.string.pref_key_sort));
        sort = pref.getString(getString(R.string.pref_key_sort), DefSort.def);
        prefSort.setSummary(Help.Pref.getSortSummary(activity, pref.getString(getString(R.string.pref_key_sort), DefSort.def)));
        prefSort.setOnPreferenceClickListener(preference -> {
            dlgSort.setArguments(sort);
            dlgSort.show(fm, Dlg.SORT);
            return true;
        });

        dlgSort = (DlgSort) fm.findFragmentByTag(Dlg.SORT);
        if (dlgSort == null) dlgSort = new DlgSort();
        dlgSort.setPositiveButton((dialogInterface, i) -> {
            sort = dlgSort.getKeys();
            pref.edit().putString(getString(R.string.pref_key_sort), sort).apply();

            String summary = Help.Pref.getSortSummary(activity, sort);
            prefSort.setSummary(summary);
        });
        dlgSort.setNeutralButton((dialogInterface, i) -> {
            sort = DefSort.def;
            pref.edit().putString(getString(R.string.pref_key_sort), sort).apply();

            String summary = Help.Pref.getSortSummary(activity, sort);
            prefSort.setSummary(summary);
        });

    }

    private Preference prefColorCreate;
    private int colorCreate;
    private DlgColor dlgColor;

    private Preference prefAutoSaveTime;
    private int autoSaveTime;
    private DlgSingle dlgSaveTime;

    private void setupNotePref() {
        Log.i(TAG, "setupNotePref");

        prefColorCreate = findPreference(getString(R.string.pref_key_color_create));
        colorCreate = pref.getInt(getString(R.string.pref_key_color_create), getResources().getInteger(R.integer.pref_default_color_create));
        prefColorCreate.setSummary(getResources().getStringArray(R.array.pref_text_color_create)[colorCreate]);
        prefColorCreate.setOnPreferenceClickListener(preference -> {
            dlgColor.setArguments(colorCreate);
            dlgColor.show(fm, Dlg.COLOR);
            return true;
        });

        dlgColor = (DlgColor) fm.findFragmentByTag(Dlg.COLOR);
        if (dlgColor == null) dlgColor = new DlgColor();
        dlgColor.setTitle(getString(R.string.pref_title_color_create));
        dlgColor.setPositiveButton((dialogInterface, i) -> {
            colorCreate = dlgColor.getCheck();

            pref.edit().putInt(getString(R.string.pref_key_color_create), colorCreate).apply();
            prefColorCreate.setSummary(getResources().getStringArray(R.array.pref_text_color_create)[colorCreate]);
        });

        prefAutoSaveTime = findPreference(getString(R.string.pref_key_auto_save_time));
        autoSaveTime = pref.getInt(getString(R.string.pref_key_auto_save_time), getResources().getInteger(R.integer.pref_default_auto_save_time));
        prefAutoSaveTime.setSummary(getResources().getStringArray(R.array.pref_text_save_time)[autoSaveTime]);
        prefAutoSaveTime.setOnPreferenceClickListener(preference -> {
            dlgSaveTime.setArguments(autoSaveTime);
            dlgSaveTime.show(fm, Dlg.SINGLE);
            return true;
        });

        dlgSaveTime = (DlgSingle) fm.findFragmentByTag(Dlg.SINGLE);
        if (dlgSaveTime == null) dlgSaveTime = new DlgSingle();

        dlgSaveTime.setTitle(getString(R.string.pref_title_auto_save_time));
        dlgSaveTime.setName(getResources().getStringArray(R.array.pref_text_save_time));
        dlgSaveTime.setPositiveButton((dialogInterface, i) -> {
            autoSaveTime = dlgSaveTime.getCheck();

            pref.edit().putInt(getString(R.string.pref_key_auto_save_time), autoSaveTime).apply();
            prefAutoSaveTime.setSummary(dlgSaveTime.getName()[autoSaveTime]);
        });

        CheckBoxPreference prefAutoSave = (CheckBoxPreference) findPreference(getString(R.string.pref_key_auto_save));
        prefAutoSave.setOnPreferenceChangeListener((preference, newValue) -> {
            prefAutoSaveTime.setEnabled((Boolean) newValue);
            return true;
        });

        prefAutoSaveTime.setEnabled(prefAutoSave.isChecked());
    }

    private DlgInfo dlgInfo;

    private void setupOtherPref() {
        Log.i(TAG, "setupOtherPref");

        dlgInfo = (DlgInfo) fm.findFragmentByTag(Dlg.INFO);
        if (dlgInfo == null) dlgInfo = new DlgInfo();
        dlgInfo.setLogoClick(view -> {
            Intent intent = new Intent(activity, ActDevelop.class);
            startActivity(intent);
        });

        Preference prefOtherAbout = findPreference(getString(R.string.pref_key_about));
        prefOtherAbout.setOnPreferenceClickListener(preference -> {
            dlgInfo.show(fm, Dlg.INFO);
            return true;
        });
    }

}
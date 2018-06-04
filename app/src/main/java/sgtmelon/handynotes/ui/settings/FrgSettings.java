package sgtmelon.handynotes.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.service.Help;
import sgtmelon.handynotes.view.alert.AlertColor;
import sgtmelon.handynotes.view.alert.AlertSort;

public class FrgSettings extends PreferenceFragment {

    //TODO смена темы

    private ActSettings activity;
    private SharedPreferences pref;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FrgPreference", "onCreate");

        addPreferencesFromResource(R.xml.preference);

        activity = (ActSettings) getActivity();
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        setupSortPref();
        setupNotePref();
        setupOtherPref();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("FrgSettings", "onCreateView");

        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            view.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorBackground));

            View lv = view.findViewById(android.R.id.list);
            lv.setPadding(0, 0, 0, 0);
        }
        return view;
    }

    private Preference prefSortNt;

    private void setupSortPref() {
        Log.i("FrgPreference", "setupSortPref");

        Preference.OnPreferenceClickListener preferenceClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                alertSort(preference.getKey());
                return true;
            }
        };

        prefSortNt = findPreference(getString(R.string.pref_key_sort));
        prefSortNt.setSummary(Help.Pref.getSortSummary(activity, pref.getString(getString(R.string.pref_key_sort), Help.Pref.getSortDefault())));
        prefSortNt.setOnPreferenceClickListener(preferenceClickListener);

    }

    private void alertSort(final String prefKey) {
        Log.i("FrgPreference", "alertColorNtDef");

        final AlertSort alert = new AlertSort(activity, pref.getString(prefKey, Help.Pref.getSortDefault()), R.style.AppTheme_AlertDialog);
        alert.setTitle(getString(R.string.dialog_title_sort))
                .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String sortKeys = alert.getSortKeys();
                        pref.edit().putString(prefKey, sortKeys).apply();

                        String summary = Help.Pref.getSortSummary(activity, sortKeys);
                        prefSortNt.setSummary(summary);

                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNeutralButton(getString(R.string.dialog_btn_reset), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sortKeys = Help.Pref.getSortDefault();
                        pref.edit().putString(prefKey, sortKeys).apply();

                        String summary = Help.Pref.getSortSummary(activity, sortKeys);
                        prefSortNt.setSummary(summary);

                        dialogInterface.cancel();
                    }
                })
                .setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private Preference prefNoteColorDef, prefNoteSaveTime;
    private int noteColorDef, noteSaveTime;

    private void setupNotePref() {
        Log.i("FrgPreference", "setupNotePref");

        prefNoteColorDef = findPreference(getString(R.string.pref_key_color_create));
        noteColorDef = pref.getInt(getString(R.string.pref_key_color_create), getResources().getInteger(R.integer.pref_default_color_create));
        prefNoteColorDef.setSummary(getResources().getStringArray(R.array.pref_text_color_create)[noteColorDef]);
        prefNoteColorDef.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                alertColorNtDef();
                return true;
            }
        });

        prefNoteSaveTime = findPreference(getString(R.string.pref_key_auto_save_time));
        noteSaveTime = pref.getInt(getString(R.string.pref_key_auto_save_time), getResources().getInteger(R.integer.pref_default_auto_save_time));
        prefNoteSaveTime.setSummary(getResources().getStringArray(R.array.pref_text_save_time)[noteSaveTime]);
        prefNoteSaveTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                alertNoteSaveTime();
                return true;
            }
        });

        CheckBoxPreference prefNoteSaveCh = (CheckBoxPreference) findPreference(getString(R.string.pref_key_auto_save));
        prefNoteSaveCh.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                prefNoteSaveTime.setEnabled((Boolean) newValue);
                return true;
            }
        });

        if (prefNoteSaveCh.isChecked()) prefNoteSaveTime.setEnabled(true);
        else prefNoteSaveTime.setEnabled(false);
    }

    private void alertColorNtDef() {
        Log.i("FrgPreference", "alertColorNtDef");

        final AlertColor alert = new AlertColor(activity, noteColorDef, R.style.AppTheme_AlertDialog);
        alert.setTitle(getString(R.string.pref_title_color_create))
                .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        noteColorDef = alert.getCheckPosition();
                        pref.edit().putInt(getString(R.string.pref_key_color_create), noteColorDef).apply();
                        prefNoteColorDef.setSummary(getResources().getStringArray(R.array.pref_text_color_create)[noteColorDef]);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void alertNoteSaveTime() {
        Log.i("FrgPreference", "alertNoteSaveTime");

        String[] checkName = getResources().getStringArray(R.array.pref_text_save_time);
        final int[] noteSaveTimeAlert = new int[1];

        AlertDialog.Builder alert = new AlertDialog.Builder(activity, R.style.AppTheme_AlertDialog);
        alert.setTitle(getString(R.string.pref_title_auto_save_time))
                .setSingleChoiceItems(checkName, noteSaveTime, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        noteSaveTimeAlert[0] = i;
                    }
                })
                .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        noteSaveTime = noteSaveTimeAlert[0];
                        pref.edit().putInt(getString(R.string.pref_key_auto_save_time), noteSaveTime).apply();
                        prefNoteSaveTime.setSummary(getResources().getStringArray(R.array.pref_text_save_time)[noteSaveTime]);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setCancelable(true);

        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void setupOtherPref() {
        Log.i("FrgPreference", "setupOtherPref");

        Preference prefOtherAbout = findPreference(getString(R.string.pref_key_about));
        prefOtherAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                View view = LayoutInflater.from(activity).inflate(R.layout.view_about, null);
                ImageView appLogo = view.findViewById(R.id.iView_viewAbout_logo);

                appLogo.setOnClickListener(new View.OnClickListener() {

                    private int click = 0;
                    private int clickShow = 9;

                    @Override
                    public void onClick(View view) {
                        click++;
                        if (click == clickShow) {
                            click = 0;
                            Intent intent = new Intent(activity, ActDevelop.class);
                            startActivity(intent);
                        }
                    }
                });

                AlertDialog.Builder alert = new AlertDialog.Builder(activity, R.style.AppTheme_AlertDialog);
                alert.setView(view).setCancelable(true);
                AlertDialog dialog = alert.create();
                dialog.show();
                return true;
            }
        });
    }

}

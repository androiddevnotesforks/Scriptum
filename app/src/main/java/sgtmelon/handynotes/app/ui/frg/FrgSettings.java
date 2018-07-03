package sgtmelon.handynotes.app.ui.frg;

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
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.app.ui.act.ActDevelop;
import sgtmelon.handynotes.app.ui.act.ActSettings;
import sgtmelon.handynotes.app.view.alert.AlertColor;
import sgtmelon.handynotes.app.view.alert.AlertSort;
import sgtmelon.handynotes.office.annot.def.DefSort;

public class FrgSettings extends PreferenceFragment {

    //TODO смена темы

    final String TAG = "FrgSettings";

    private ActSettings activity;
    private SharedPreferences pref;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        addPreferencesFromResource(R.xml.preference);

        activity = (ActSettings) getActivity();
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

    private void setupSortPref() {
        Log.i(TAG, "setupSortPref");

        Preference.OnPreferenceClickListener preferenceClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                alertSort(preference.getKey());
                return true;
            }
        };

        prefSort = findPreference(getString(R.string.pref_key_sort));
        prefSort.setSummary(Help.Pref.getSortSummary(activity, pref.getString(getString(R.string.pref_key_sort), DefSort.def)));
        prefSort.setOnPreferenceClickListener(preferenceClickListener);

    }

    private void alertSort(final String prefKey) {
        Log.i(TAG, "alertSort");

        final AlertSort alert = new AlertSort(activity, pref.getString(prefKey, DefSort.def), R.style.AppTheme_AlertDialog);

        alert.setTitle(getString(R.string.dialog_title_sort))
                .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String keys = alert.getKeys();
                        pref.edit().putString(prefKey, keys).apply();

                        String summary = Help.Pref.getSortSummary(activity, keys);
                        prefSort.setSummary(summary);

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
                        String keys = DefSort.def;
                        pref.edit().putString(prefKey, keys).apply();

                        String summary = Help.Pref.getSortSummary(activity, keys);
                        prefSort.setSummary(summary);

                        dialogInterface.cancel();
                    }
                })
                .setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private Preference prefColorCreate, prefAutoSaveTime;
    private int colorCreate, autoSaveTime;

    private void setupNotePref() {
        Log.i(TAG, "setupNotePref");

        prefColorCreate = findPreference(getString(R.string.pref_key_color_create));
        colorCreate = pref.getInt(getString(R.string.pref_key_color_create), getResources().getInteger(R.integer.pref_default_color_create));
        prefColorCreate.setSummary(getResources().getStringArray(R.array.pref_text_color_create)[colorCreate]);
        prefColorCreate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                alertColorCreate();
                return true;
            }
        });

        prefAutoSaveTime = findPreference(getString(R.string.pref_key_auto_save_time));
        autoSaveTime = pref.getInt(getString(R.string.pref_key_auto_save_time), getResources().getInteger(R.integer.pref_default_auto_save_time));
        prefAutoSaveTime.setSummary(getResources().getStringArray(R.array.pref_text_save_time)[autoSaveTime]);
        prefAutoSaveTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                alertAutoSaveTime();
                return true;
            }
        });

        CheckBoxPreference prefAutoSave = (CheckBoxPreference) findPreference(getString(R.string.pref_key_auto_save));
        prefAutoSave.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                prefAutoSaveTime.setEnabled((Boolean) newValue);
                return true;
            }
        });

        prefAutoSaveTime.setEnabled(prefAutoSave.isChecked());
    }

    private void alertColorCreate() {
        Log.i(TAG, "alertColorCreate");

        final AlertColor alert = new AlertColor(activity, colorCreate, R.style.AppTheme_AlertDialog);
        alert.setTitle(getString(R.string.pref_title_color_create))
                .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        colorCreate = alert.getCheck();

                        pref.edit().putInt(getString(R.string.pref_key_color_create), colorCreate).apply();
                        prefColorCreate.setSummary(getResources().getStringArray(R.array.pref_text_color_create)[colorCreate]);

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

    private void alertAutoSaveTime() {
        Log.i(TAG, "alertAutoSaveTime");

        final String[] name = getResources().getStringArray(R.array.pref_text_save_time);
        final int[] value = new int[1];

        AlertDialog.Builder alert = new AlertDialog.Builder(activity, R.style.AppTheme_AlertDialog);
        alert.setTitle(getString(R.string.pref_title_auto_save_time))
                .setSingleChoiceItems(name, autoSaveTime, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        value[0] = i;
                    }
                })
                .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        autoSaveTime = value[0];

                        pref.edit().putInt(getString(R.string.pref_key_auto_save_time), autoSaveTime).apply();
                        prefAutoSaveTime.setSummary(name[autoSaveTime]);

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
        Log.i(TAG, "setupOtherPref");

        Preference prefOtherAbout = findPreference(getString(R.string.pref_key_about));
        prefOtherAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                View view = LayoutInflater.from(activity).inflate(R.layout.view_about, null);
                ImageView logo = view.findViewById(R.id.viewAbout_iv_logo);

                logo.setOnClickListener(new View.OnClickListener() {

                    private int click = 0;
                    private final int show = 9;

                    @Override
                    public void onClick(View view) {
                        if (++click == show) {
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

package sgtmelon.scriptum.app.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.component.ComponentPreference;
import sgtmelon.scriptum.app.injection.component.DaggerComponentPreference;
import sgtmelon.scriptum.app.injection.module.blank.ModuleBlankPreference;
import sgtmelon.scriptum.app.view.activity.DevelopActivity;
import sgtmelon.scriptum.app.view.activity.PreferenceActivity;
import sgtmelon.scriptum.element.dialog.DlgColor;
import sgtmelon.scriptum.element.dialog.DlgInfo;
import sgtmelon.scriptum.element.dialog.DlgSort;
import sgtmelon.scriptum.element.dialog.common.DlgSingle;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.annot.def.DefSort;
import sgtmelon.scriptum.office.st.StOpen;

public final class PreferenceFragment extends android.preference.PreferenceFragment {

    private static final String TAG = PreferenceFragment.class.getSimpleName();

    private final StOpen stOpen = new StOpen();

    @Inject
    FragmentManager fm;

    @Inject
    DlgSort dlgSort;
    @Inject
    DlgColor dlgColor;
    @Inject
    @Named(DefDlg.SAVE_TIME)
    DlgSingle dlgSaveTime;
    @Inject
    @Named(DefDlg.THEME)
    DlgSingle dlgTheme;
    @Inject
    DlgInfo dlgInfo;

    private PreferenceActivity activity;
    private Context context;

    private SharedPreferences pref; // TODO: 02.10.2018 синглтон

    private Preference prefSort;
    private String valSort;

    private Preference prefColor;
    private int valColor;

    private Preference prefSaveTime;
    private int valSaveTime;

    private Preference prefTheme;
    private int valTheme;

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);

        activity = (PreferenceActivity) getActivity();
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        ComponentPreference componentPreference = DaggerComponentPreference.builder()
                .moduleBlankPreference(new ModuleBlankPreference(activity))
                .build();
        componentPreference.inject(this);

        pref = PreferenceManager.getDefaultSharedPreferences(context);

        if (savedInstanceState != null) {
            stOpen.setOpen(savedInstanceState.getBoolean(DefIntent.STATE_OPEN));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        setupNotePref();
        setupSavePref();
        setupAppPref();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putBoolean(DefIntent.STATE_OPEN, stOpen.isOpen());
    }

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
        valColor = pref.getInt(getString(R.string.pref_key_color), getResources().getInteger(R.integer.pref_color_default));
        prefColor.setSummary(getResources().getStringArray(R.array.pref_color_text)[valColor]);
        prefColor.setOnPreferenceClickListener(preference -> {
            if (!stOpen.isOpen()) {
                stOpen.setOpen(true);

                dlgColor.setArguments(valColor);
                dlgColor.show(fm, DefDlg.COLOR);
            }
            return true;
        });

        dlgColor.setTitle(getString(R.string.pref_color_title));
        dlgColor.setPositiveListener((dialogInterface, i) -> {
            valColor = dlgColor.getCheck();

            pref.edit().putInt(getString(R.string.pref_key_color), valColor).apply();
            prefColor.setSummary(getResources().getStringArray(R.array.pref_color_text)[valColor]);
        });
        dlgColor.setDismissListener(dialogInterface -> stOpen.setOpen(false));
    }

    private void setupSavePref() {
        Log.i(TAG, "setupSavePref");

        prefSaveTime = findPreference(getString(R.string.pref_key_save_time));
        valSaveTime = pref.getInt(getString(R.string.pref_key_save_time), getResources().getInteger(R.integer.pref_save_time_default));
        prefSaveTime.setSummary(getResources().getStringArray(R.array.pref_save_time_text)[valSaveTime]);
        prefSaveTime.setOnPreferenceClickListener(preference -> {
            if (!stOpen.isOpen()) {
                stOpen.setOpen(true);

                dlgSaveTime.setArguments(valSaveTime);
                dlgSaveTime.show(fm, DefDlg.SAVE_TIME);
            }
            return true;
        });

        dlgSaveTime.setTitle(getString(R.string.pref_save_time_title));
        dlgSaveTime.setName(getResources().getStringArray(R.array.pref_save_time_text));
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

    private void setupAppPref() {
        Log.i(TAG, "setupAppPref");

        prefTheme = findPreference(getString(R.string.pref_key_theme));
        valTheme = pref.getInt(getString(R.string.pref_key_theme), getResources().getInteger(R.integer.pref_theme_default));
        prefTheme.setSummary(getResources().getStringArray(R.array.pref_theme_text)[valTheme]);
        prefTheme.setOnPreferenceClickListener(preference -> {
            if (!stOpen.isOpen()) {
                stOpen.setOpen(true);

                dlgTheme.setArguments(valTheme);
                dlgTheme.show(fm, DefDlg.THEME);
            }
            return true;
        });

        dlgTheme.setTitle(getString(R.string.pref_theme_title));
        dlgTheme.setName(getResources().getStringArray(R.array.pref_theme_text));
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
            Intent intent = new Intent(activity, DevelopActivity.class);
            startActivity(intent);
        });
        dlgInfo.setDismissListener(dialogInterface -> stOpen.setOpen(false));
    }

}
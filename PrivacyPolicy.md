## Scriptum: Privacy policy

Welcome to the Scriptum app for Android!

This is an open source project developed by Alexey Kuznetsov. The source code of the project is
transparent and available on [GitHub](https://github.com/SerjantArbuz/Scriptum). The app is also
available for download
in [Google Play](https://play.google.com/store/apps/details?id=sgtmelon.scriptum) store.

In this application, information comes first, and I perfectly understand that your thoughts are the
most secret thing in this world. That's why your trust is very important for me!

All information created by yourself are stored exclusively on your device in the database and are
not transmitted to outsiders. Application don't collect data about users.

### Explanation of permissions requested in the app

The list of permissions required by this app can be found in
the [AndroidManifest.xml](https://github.com/SerjantArbuz/Scriptum/blob/master/app/src/main/AndroidManifest.xml)
file:

| Permission | Explanation |
| :---: | --- |
| `android.permission.RECEIVE_BOOT_COMPLETED` | Required to track your device restart and needed for update alarms and displaying of notifications in status bar. Permission automatically granted by the system; can't be revoked by user. |
| `android.permission.VIBRATE` | Required to vibrate the device when alarm is ringing. Permission automatically granted by the system; can't be revoked by user. But you can disable this kind of user notification inside app settings. |
| `android.permission.WAKE_LOCK` | Required to show alarm screen when alarm is ringing. Permission automatically granted by the system; can't be revoked by user. |
| `android.permission.FOREGROUND_SERVICE` | Enables the app to create foreground services that will ring the alarm, keep it snoozed and keep your status bar notifications up-to-date and always visible (even after application closed). Permission automatically granted by the system; can't be revoked by user. |
| `android.permission.WRITE_EXTERNAL_STORAGE` | The only sensitive permission that the app requests, and can be revoked by the system or the user at any time. This is required in case if you want to set a custom alarm ring, if you will use backup notes features (to save/load data from your device). |

In addition, this project uses analytic library which collect information only about how you
interact with this app, about bugs and crash situations.

If you still have any questions, feel free to ask via email: lex.kuznet@gmail.com.

Sincerely,\
Alexey Kuznetsov.
package de.aw.klackreminder.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import de.aw.awlib.R;
import de.aw.klackreminder.gv.Reminder;

import static android.content.Intent.EXTRA_TEXT;

public class ReminderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        long mCalendarID = prefs.getLong(getString(R.string.pref_calendar_id), -1);
        if (mCalendarID != -1) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String reminderBody = extras.getString(EXTRA_TEXT);
                if (reminderBody != null) {
                    String text = new Reminder(this).insertReminder(mCalendarID, reminderBody);
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Erst Kalender in der App festlegen!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}

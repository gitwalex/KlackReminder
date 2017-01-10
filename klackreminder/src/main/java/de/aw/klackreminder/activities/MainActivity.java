package de.aw.klackreminder.activities;

import android.os.Bundle;
import android.widget.Toast;

import de.aw.awlib.activities.AWActivityMainScreen;
import de.aw.klackreminder.fragments.ReminderFragment;
import de.aw.klackreminder.gv.Reminder;

import static android.content.Intent.EXTRA_TEXT;

public class MainActivity extends AWActivityMainScreen {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String reminderBody = extras.getString(EXTRA_TEXT);
                if (reminderBody != null) {
                    String text = new Reminder(this).insertReminder(reminderBody);
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
            }
            ReminderFragment f = new ReminderFragment();
            getSupportFragmentManager().beginTransaction().add(container, f).commit();
        }
    }
}

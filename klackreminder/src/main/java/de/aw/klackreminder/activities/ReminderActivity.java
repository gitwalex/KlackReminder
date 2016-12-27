package de.aw.klackreminder.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import de.aw.klackreminder.gv.Reminder;

import static android.content.Intent.EXTRA_TEXT;

public class ReminderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String reminderBody = extras.getString(EXTRA_TEXT);
            if (reminderBody != null) {
                String text = new Reminder(this).insertReminder(reminderBody);
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}

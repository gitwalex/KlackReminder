package de.aw.klackreminder.activities;

import android.os.Bundle;

import de.aw.awlib.activities.AWActivityMainScreen;
import de.aw.klackreminder.fragments.ReminderFragment;

public class MainActivity extends AWActivityMainScreen {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReminderFragment f = new ReminderFragment();
        getSupportFragmentManager().beginTransaction().add(container, f).commit();
    }
}

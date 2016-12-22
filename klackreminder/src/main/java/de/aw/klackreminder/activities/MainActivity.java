package de.aw.klackreminder.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import de.aw.awlib.R;
import de.aw.awlib.activities.AWActivityMainScreen;
import de.aw.awlib.fragments.AWFragment;
import de.aw.awlib.fragments.AWFragmentCalendar;
import de.aw.awlib.recyclerview.AWOnCursorRecyclerViewListener;
import de.aw.klackreminder.fragments.ReminderFragment;
import de.aw.klackreminder.gv.Reminder;

import static android.content.Intent.EXTRA_TEXT;

public class MainActivity extends AWActivityMainScreen implements AWOnCursorRecyclerViewListener {
    private long mCalendarID;
    private String reminderBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        long mCalendarID = prefs.getLong(getString(R.string.pref_calendar_id), -1);
        if (savedInstanceState == null) {
            AWFragment f;
            if (mCalendarID == -1) {
                f = new AWFragmentCalendar();
                setSubTitle(R.string.selectCalendar);
            } else {
                f = new ReminderFragment();
            }
            getSupportFragmentManager().beginTransaction().add(container, f).commit();
        }
    }

    @Override
    public void onRecyclerItemClick(RecyclerView parent, View view, int position, final long id,
                                    @LayoutRes int viewHolderLayoutID) {
        final TextView calendarName = (TextView) view.findViewById(R.id.tvCalendarName);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(calendarName.getText().toString());
        builder.setTitle(R.string.calendarSelected);
        builder.setPositiveButton(R.string.awlib_btnAccept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCalendarID = id;
                SharedPreferences.Editor editor =
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putLong(getString(R.string.pref_calendar_id), id)
                        .putString(getString(R.string.pref_calendar_name),
                                calendarName.getText().toString()).apply();
                reminderBody = args.getString(EXTRA_TEXT);
                if (reminderBody != null) {
                    String text = new Reminder(MainActivity.this)
                            .insertReminder(mCalendarID, reminderBody);
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                }
                AWFragment f = new ReminderFragment();
                getSupportFragmentManager().beginTransaction().replace(container, f).commit();
            }
        });
        builder.setNegativeButton(R.string.awlib_btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        Dialog dlg = builder.create();
        dlg.show();
    }

    @Override
    public boolean onRecyclerItemLongClick(RecyclerView recyclerView, View view, int position,
                                           long id, @LayoutRes int viewHolderLayoutID) {
        return false;
    }
}

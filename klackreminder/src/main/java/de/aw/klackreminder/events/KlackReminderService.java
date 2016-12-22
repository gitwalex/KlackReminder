package de.aw.klackreminder.events;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateUtils;

import java.util.Calendar;

import de.aw.awlib.gv.AWApplicationGeschaeftsObjekt;
import de.aw.awlib.gv.CalendarReminder;
import de.aw.klackreminder.R;
import de.aw.klackreminder.database.DBDefinition;
import de.aw.klackreminder.database.DBHelper;
import de.aw.klackreminder.gv.Reminder;

/**
 * Created by alex on 22.12.2016.
 */
public class KlackReminderService extends IntentService {
    public KlackReminderService() {
        super("KlackReminderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DBDefinition tbd = DBDefinition.KlackEvents;
        String[] projection =
                tbd.columnNames(R.string.column_eventID, R.string.column_eventInserted);
        Cursor c = getContentResolver().query(tbd.getUri(), projection, null, null, null);
        try {
            if (c.moveToFirst()) {
                CalendarReminder reminder = new CalendarReminder(getApplicationContext());
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(Calendar.HOUR_OF_DAY, 1);
                cal.set(Calendar.MINUTE, 0);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                do {
                    long eventID = c.getLong(0);
                    long eventInserted = c.getLong(1);
                    long diff = (cal.getTimeInMillis() - eventInserted) / DateUtils.DAY_IN_MILLIS;
                    if (diff > 20) {
                        long id = c.getLong(3);
                        try {
                            if (reminder.deleteEvent(eventID)) {
                                new Reminder(getApplicationContext(), id)
                                        .delete(DBHelper.getInstance());
                            }
                        } catch (AWApplicationGeschaeftsObjekt.LineNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        reminder.updateEventDate(eventID, cal.getTime());
                    }
                } while (c.moveToNext());
            }
        } finally {
            c.close();
            EventSetAlarm.setAlarm(getApplicationContext());
        }
    }
}

package de.aw.klackreminder.gv;

import android.content.Context;
import android.os.Parcel;

import java.sql.Date;

import de.aw.awlib.gv.AWApplicationGeschaeftsObjekt;
import de.aw.awlib.gv.CalendarReminder;
import de.aw.klackreminder.R;
import de.aw.klackreminder.database.DBDefinition;
import de.aw.klackreminder.database.DBHelper;

/**
 * Created by alex on 21.12.2016.
 */
public class Reminder extends AWApplicationGeschaeftsObjekt {
    private static final DBDefinition tbd = DBDefinition.KlackEvents;

    public Reminder(Context context, Long id) throws LineNotFoundException {
        super(context, tbd, id);
    }

    public String insertReminder(long calendarID, String reminderBody) {
        String[] newHeader = reminderBody.split(linefeed);
        CalendarReminder reminder = new CalendarReminder(getContext());
        long id = reminder.createDailyEvent(calendarID, "KlackReminder: " + newHeader[1],
                reminderBody);
        String text;
        if (id != -1) {
            put(R.string.column_eventID, id);
            put(R.string.column_eventTitle, newHeader[1]);
            put(R.string.column_eventBody, reminderBody);
            put(R.string.column_eventInserted, new Date(System.currentTimeMillis()));
            insert(DBHelper.newInstance());
            text = "Erinnerung erstellt " + newHeader[1];
        } else {
            text = "Erinnerung erstellen fehlgeschlagen";
        }
        return text;
    }

    public Reminder(Context context) {
        super(context, tbd);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected Reminder(Parcel in) {
        super(in);
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel source) {
            return new Reminder(source);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}

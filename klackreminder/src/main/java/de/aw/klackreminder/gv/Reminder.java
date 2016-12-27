package de.aw.klackreminder.gv;

import android.content.Context;
import android.os.Parcel;

import de.aw.awlib.gv.AWApplicationGeschaeftsObjekt;
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

    public String insertReminder(String reminderBody) {
        String[] newHeader = reminderBody.split(linefeed);
        put(R.string.column_eventTitle, newHeader[1]);
        put(R.string.column_eventBody, reminderBody);
        insert(DBHelper.getInstance());
        return "Erinnerung erstellt " + newHeader[1];
    }

    public Reminder(Context context) {
        super(context, tbd);
    }

    protected Reminder(Parcel in) {
        super(DBHelper.getInstance().getApplicationContext(), in);
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

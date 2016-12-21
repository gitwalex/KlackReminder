package de.aw.klackreminder.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.aw.awlib.gv.AWApplicationGeschaeftsObjekt;
import de.aw.awlib.gv.CalendarReminder;
import de.aw.awlib.recyclerview.AWCursorRecyclerViewFragment;
import de.aw.klackreminder.R;
import de.aw.klackreminder.database.DBDefinition;
import de.aw.klackreminder.database.DBHelper;
import de.aw.klackreminder.gv.Reminder;

/**
 * Created by alex on 21.12.2016.
 */
public class ReminderFragment extends AWCursorRecyclerViewFragment {
    private static final DBDefinition tbd = DBDefinition.KlackEvents;
    private static final int layout = R.layout.awlib_default_recycler_view;
    private static final int viewHolderLayout = R.layout.reminders;
    private static final int[] fromResIDs =
            new int[]{R.string.column_eventTitle, R.string.column_eventBody};
    private static final int[] viewResIDs = new int[]{R.id.eventTitle, R.id.eventBody};

    @Override
    public boolean onRecyclerItemLongClick(RecyclerView recyclerView, View view, int position,
                                           long id) {
        try {
            Reminder reminder = new Reminder(getContext(), id);
            Long eventID = reminder.getAsLong(R.string.column_eventID);
            CalendarReminder calendarReminder = new CalendarReminder(getContext());
            if (calendarReminder.deleteEvent(eventID)) {
                reminder.delete(DBHelper.getInstance());
            }
            return true;
        } catch (AWApplicationGeschaeftsObjekt.LineNotFoundException e) {
            e.printStackTrace();
        }
        return super.onRecyclerItemLongClick(recyclerView, view, position, id);
    }

    @Override
    protected void setInternalArguments(Bundle args) {
        super.setInternalArguments(args);
        args.putParcelable(DBDEFINITION, tbd);
        args.putInt(LAYOUT, layout);
        args.putInt(VIEWHOLDERLAYOUT, viewHolderLayout);
        args.putIntArray(FROMRESIDS, fromResIDs);
        args.putIntArray(VIEWRESIDS, viewResIDs);
    }
}

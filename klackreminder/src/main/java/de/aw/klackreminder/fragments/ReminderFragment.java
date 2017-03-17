package de.aw.klackreminder.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import de.aw.awlib.adapters.AWBaseAdapter;
import de.aw.awlib.gv.AWApplicationGeschaeftsObjekt;
import de.aw.awlib.recyclerview.AWCursorRecyclerViewFragment;
import de.aw.awlib.recyclerview.AWLibViewHolder;
import de.aw.klackreminder.R;
import de.aw.klackreminder.database.DBDefinition;
import de.aw.klackreminder.database.DBHelper;
import de.aw.klackreminder.gv.Reminder;

import static de.aw.klackreminder.R.id.webView;

/**
 * Anzeige der vorliegenden Reminder
 */
public class ReminderFragment extends AWCursorRecyclerViewFragment
        implements AWBaseAdapter.OnSwipeListener {
    private static final DBDefinition tbd = DBDefinition.KlackEvents;
    private static final int layout = R.layout.awlib_default_recycler_view;
    private static final int viewHolderLayout = R.layout.reminders;
    private static final int[] fromResIDs =
            new int[]{R.string.column_eventTitle, R.string.column_eventBody,
                    R.string.column_webcontent};
    private static final int[] viewResIDs =
            new int[]{R.id.eventTitle, R.id.eventBody, R.id.webView};

    @Override
    protected boolean onBindView(AWLibViewHolder holder, View view, int resID, Cursor cursor,
                                 int cursorPosition) {
        switch (resID) {
            case webView:
                view.setVisibility(View.GONE);
                if (!cursor.isNull(cursorPosition)) {
                    WebView webView = (WebView) view;
                    webView.loadData(cursor.getString(cursorPosition), "text/html; charset=utf-8",
                            "utf-8");
                }
                return true;
            default:
                return super.onBindView(holder, view, resID, cursor, cursorPosition);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnSwipeListener(this);
    }

    @Override
    public void onItemDismiss(long itemID, int position) {
        try {
            Reminder reminder = new Reminder(getContext(), itemID);
            reminder.delete(DBHelper.getInstance());
        } catch (AWApplicationGeschaeftsObjekt.LineNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRecyclerItemClick(View view, int position, long id) {
        View webView = view.findViewById(R.id.webView);
        webView.setVisibility(webView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        super.onRecyclerItemClick(view, position, id);
    }

    @Override
    public void onSwiped(AWLibViewHolder viewHolder, int direction, int position, long id) {
        getAdapter().setPendingDeleteItemPosition(position);
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

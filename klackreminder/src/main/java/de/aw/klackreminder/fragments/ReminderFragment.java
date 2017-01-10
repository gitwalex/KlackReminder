package de.aw.klackreminder.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;

import de.aw.awlib.adapters.AWCursorDragDropRecyclerViewAdapter;
import de.aw.awlib.gv.AWApplicationGeschaeftsObjekt;
import de.aw.awlib.recyclerview.AWDragSwipeRecyclerViewFragment;
import de.aw.awlib.recyclerview.AWLibViewHolder;
import de.aw.awlib.recyclerview.AWSimpleItemTouchHelperCallback;
import de.aw.klackreminder.R;
import de.aw.klackreminder.database.DBDefinition;
import de.aw.klackreminder.database.DBHelper;
import de.aw.klackreminder.gv.Reminder;

import static de.aw.klackreminder.R.id.webView;

/**
 * Anzeige der vorliegenden Reminder
 */
public class ReminderFragment extends AWDragSwipeRecyclerViewFragment
        implements View.OnClickListener {
    private static final DBDefinition tbd = DBDefinition.KlackEvents;
    private static final int layout = R.layout.awlib_default_recycler_view;
    private static final int viewHolderLayout = R.layout.reminders;
    private static final int[] fromResIDs =
            new int[]{R.string.column_eventTitle, R.string.column_eventBody,
                    R.string.column_webcontent};
    private static final int[] viewResIDs =
            new int[]{R.id.eventTitle, R.id.eventBody, R.id.webView, R.id.btnDetail};

    @NonNull
    @Override
    protected AWSimpleItemTouchHelperCallback getItemTouchCallback(
            AWCursorDragDropRecyclerViewAdapter mAdapter) {
        return new AWSimpleItemTouchHelperCallback(mAdapter) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position,
                                 long id) {
                try {
                    Reminder reminder = new Reminder(getContext(), id);
                    reminder.delete(DBHelper.getInstance());
                } catch (AWApplicationGeschaeftsObjekt.LineNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
    }

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
            case R.id.btnDetail:
                view.setOnClickListener(this);
                return true;
            default:
                return super.onBindView(holder, view, resID, cursor, cursorPosition);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDetail:
                getView().findViewById(R.id.webView).setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIsSwipeable(true);
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

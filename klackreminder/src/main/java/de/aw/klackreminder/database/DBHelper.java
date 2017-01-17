package de.aw.klackreminder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import de.aw.awlib.database.AWAbstractDBDefinition;
import de.aw.awlib.database.AWDBAlterHelper;
import de.aw.awlib.database.AbstractDBHelper;
import de.aw.klackreminder.R;

/**
 * DBHelper fuer KlackReminder
 */
public class DBHelper extends AbstractDBHelper {
    private static DBHelper dbHelper;

    public static AbstractDBHelper getInstance() {
        return dbHelper;
    }

    public DBHelper(Context context, SQLiteDatabase.CursorFactory cursorFactory) {
        super(context, cursorFactory);
        dbHelper = this;
    }

    @Override
    protected void doCreate(SQLiteDatabase database, AWDBAlterHelper dbhelper) {
    }

    @Override
    protected void doUpgrade(SQLiteDatabase database, AWDBAlterHelper dbhelper, int oldVersion,
                             int newVersion) {
    }

    @Override
    public AWAbstractDBDefinition[] getAllDBDefinition() {
        return DBDefinition.values();
    }

    @Override
    public AWAbstractDBDefinition getDBDefinition(String tablename) {
        return DBDefinition.valueOf(tablename);
    }

    @NonNull
    @Override
    public int[][] getNonTextColumnItems() {
        return new int[][]{{R.string._id, 'N'}//
                , {R.string.column_eventInserted, 'D'}//
                , {R.string.column_eventID, 'N'}}//
                ;
    }
}

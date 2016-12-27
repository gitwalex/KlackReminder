package de.aw.klackreminder.application;

import android.content.Context;
import android.support.annotation.NonNull;

import de.aw.awlib.application.AWApplication;
import de.aw.awlib.database.AWAbstractDBDefinition;
import de.aw.awlib.database.AbstractDBHelper;
import de.aw.klackreminder.database.DBDefinition;
import de.aw.klackreminder.database.DBHelper;

/**
 * Created by alex on 21.12.2016.
 */
public class KlackReminderApplication extends AWApplication {
    private DBHelper dbhelper;

    @NonNull
    @Override
    protected AbstractDBHelper createDBHelper(Context context) {
        if (dbhelper == null) {
            dbhelper = new DBHelper(this, null);
        }
        return dbhelper;
    }

    @Override
    public String getAuthority() {
        return "de.aw.klackremindercontentprovider";
    }

    @Override
    protected AWAbstractDBDefinition[] getDBDefinitionValues() {
        return DBDefinition.values();
    }

    @Override
    public String theApplicationDirectory() {
        return "/klackreminder";
    }

    @Override
    public int theDatenbankVersion() {
        return 1;
    }
}

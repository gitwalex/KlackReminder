/*
 * MonMa: Eine freie Android-App fuer Verwaltung privater Finanzen
 *
 * Copyright [2015] [Alexander Winkler, 23730 Neustadt/Germany]
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, see <http://www.gnu.org/licenses/>.
 */
package de.aw.klackreminder.database;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import de.aw.awlib.database.AWAbstractDBDefinition;
import de.aw.awlib.database.AWDBAlterHelper;
import de.aw.klackreminder.R;

/**
 * @author Alexander Winkler
 *         <p/>
 *         Aufzaehlung der Tabellen der Datenbank. 1. Parameter ist ein Integer-Array der resIds
 *         (R.string.xxx)der Tabellenspalten
 */
@SuppressWarnings("unused")
public enum DBDefinition implements Parcelable, AWAbstractDBDefinition {
    KlackEvents() {
        @Override
        public int[] getTableItems() {
            return new int[]{R.string._id//
                    , R.string.column_eventTitle//
                    , R.string.column_eventBody//
                    , R.string.column_webcontent//
            };
        }

        @Override
        public int[] getOrderByItems() {
            return new int[]{R.string.column_eventTitle};
        }
    };
    public static final Creator<DBDefinition> CREATOR = new Creator<DBDefinition>() {
        @Override
        public DBDefinition createFromParcel(Parcel in) {
            return DBDefinition.values()[in.readInt()];
        }

        @Override
        public DBDefinition[] newArray(int size) {
            return new DBDefinition[size];
        }
    };
    private static String mAuthority;
    private Uri mUri;

    @Override
    public String columnName(int resID) {
        return DBHelper.getInstance().columnName(resID);
    }

    @Override
    public String[] columnNames(int... resIDs) {
        return DBHelper.getInstance().columnNames(resIDs);
    }

    /**
     * Wird beim Erstellen der DB Nach Anlage aller Tabellen und Indices gerufen. Hier koennen noch
     * Nacharbeiten durchgefuehrt werden
     *
     * @param helper
     *         AWDBAlterHelper database
     */
    public void createDatabase(AWDBAlterHelper helper) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @return den String fuer den Aubau einer View (ohne CREATE View AS name). Muss bei Views
     * ueberscheiben werden. Standard: null
     */
    public String getCreateViewSQL() {
        return null;
    }

    /**
     * Liste der fuer eine sinnvolle Sortierung notwendigen Spalten.
     *
     * @return ResId der Spalten, die zu einer Sortierung herangezogen werden sollen.
     */
    public int[] getOrderByItems() {
        return new int[]{getTableItems()[0]};
    }

    /**
     * Liefert ein Array der Columns zurueck, nach den sortiert werden sollte,
     *
     * @return Array der Columns, nach denen sortiert werden soll.
     */
    public String[] getOrderColumns() {
        int[] columItems = getOrderByItems();
        return columnNames(columItems);
    }

    /**
     * OrderBy-String - direkt fuer SQLITE verwendbar.
     *
     * @return OrderBy-String, wie in der Definition der ENUM vorgegeben
     */
    @Override
    public String getOrderString() {
        String[] orderColumns = getOrderColumns();
        StringBuilder order = new StringBuilder(orderColumns[0]);
        for (int i = 1; i < orderColumns.length; i++) {
            order.append(", ").append(orderColumns[i]);
        }
        return order.toString();
    }

    /**
     * @return TableItems der Tabelle. Koennen von den echten TableItems gemaess CreateTableItems ()
     * abweichen, z.B. wenn weitere Items dazugejoint werden.
     */
    public abstract int[] getTableItems();

    /**
     * @return liefert die URI zu der Tabelle zuruck.
     */
    public Uri getUri() {
        if (mUri == null) {
            mUri = Uri.parse("content://" + mAuthority + "/" + name());
        }
        return mUri;
    }

    /**
     * Indicator, ob DBDefinition eine View ist. Default false
     *
     * @return false. Wenn DBDefintion eine View ist, muss dies zwingend ueberschreiben werden,
     * sonst wirds in DBHelper als Tabelle angelegt.
     */
    public boolean isView() {
        return false;
    }

    @Override
    public void setAuthority(String authority) {
        mAuthority = authority;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(ordinal());
    }
}
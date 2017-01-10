package de.aw.klackreminder.gv;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.util.Patterns;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import de.aw.awlib.application.AWApplication;
import de.aw.awlib.gv.AWApplicationGeschaeftsObjekt;
import de.aw.awlib.utils.AWHTTPDownLoader;
import de.aw.klackreminder.R;
import de.aw.klackreminder.database.DBDefinition;
import de.aw.klackreminder.database.DBHelper;

/**
 * Geschaeftvorfall fuer Reminder. Derzeit nur fuer Klack konfiguriert. Alle anderen Reminder
 * liefern undefinierte Ergebnis.
 */
public class Reminder extends AWApplicationGeschaeftsObjekt
        implements AWHTTPDownLoader.HTTPDownLoadResultListener {
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
    private static final DBDefinition tbd = DBDefinition.KlackEvents;

    public Reminder(Context context, Long id) throws LineNotFoundException {
        super(context, tbd, id);
    }

    public Reminder(Context context) {
        super(context, tbd);
    }

    protected Reminder(Parcel in) {
        super(DBHelper.getInstance().getApplicationContext(), in);
    }

    public String insertReminder(String reminderBody) {
        String[] newHeader = reminderBody.split(linefeed);
        put(R.string.column_eventTitle, newHeader[1]);
        for (String text : newHeader) {
            if (Patterns.WEB_URL.matcher(text).matches()) {
                AWApplication.Log("Url: " + text);
                Uri.Builder builder = Uri.parse(text).buildUpon();
                try {
                    URI uri = new URI("http://" + builder.build().toString());
                    final URL url = uri.toURL();
                    new AWHTTPDownLoader(this).execute(url);
                } catch (URISyntaxException | MalformedURLException e) {
                    e.printStackTrace();
                    return "Fehler bei Erstellung!";
                }
            }
        }
        put(R.string.column_eventBody, reminderBody);
        insert(DBHelper.getInstance());
        return "Erinnerung erstellt " + newHeader[1];
    }

    /**
     * Stellt das Ergebnis des HTTPDownloads in die Spalte R.string.column_webcontent ein.
     *
     * @param result
     *         {@link de.aw.awlib.utils.AWHTTPDownLoader.HTTPDownloadResult}, welches vom {@link
     *         AWHTTPDownLoader} geliefert wird.
     */
    @Override
    public void onHTTPDownLoadResult(AWHTTPDownLoader.HTTPDownloadResult result) {
        if (result.isErfolgreich()) {
            try {
                put(R.string.column_webcontent, result.getStringResult());
                if (isInserted()) {
                    update(DBHelper.getInstance());
                } else {
                    insert(DBHelper.getInstance());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}

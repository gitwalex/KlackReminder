/*
 * MonMa: Eine freie Android-Application fuer die Verwaltung privater Finanzen
 *
 * Copyright [2015] [Alexander Winkler, 2373 Dahme/Germany]
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
package de.aw.klackreminder.events;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import de.aw.awlib.application.AWApplication;
import de.aw.klackreminder.activities.KlackReminderInterface;

/**
 * BroadcastReceiver fuer Alarme. Beim Booten wird automatisch der naecchste HBCI-Alarm
 * (Umsatzabrufe) gesetzt.
 */
public class EventSetAlarm extends BroadcastReceiver implements KlackReminderInterface {
    private static final String ALARMTIME = "ALARMTIME";
    private static final int ALARM_TYPE = AlarmManager.RTC_WAKEUP;

    public EventSetAlarm() {
    }

    /**
     * Setzt den naechsten HBCI-Alarm und ruft arbeitstaeglich die Umsaetze ab, wenn in Preferences
     * entsprechend eingestellt.
     *
     * @param context
     *         context
     */
    public static void setAlarm(Context context) {
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar nextAlarm = Calendar.getInstance();
        nextAlarm.setTimeInMillis(System.currentTimeMillis());
        nextAlarm.set(Calendar.HOUR_OF_DAY, 1);
        nextAlarm.set(Calendar.MINUTE, 0);
        nextAlarm.add(Calendar.DAY_OF_MONTH, 1);
        Intent newIntent = new Intent(context, KlackReminderService.class);
        newIntent.setAction(MOVE_ONE_DAY);
        newIntent.putExtra(ALARMTIME, nextAlarm);
        PendingIntent newAlarmIntent =
                PendingIntent.getService(context, 0, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmManager.set(ALARM_TYPE, nextAlarm.getTimeInMillis(), newAlarmIntent);
    }

    /**
     * Wird beim booten gerufen und setzt den naechsten HBCI-Alarm
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case Intent.ACTION_BOOT_COMPLETED:
                    // Set the alarm here.
                    AWApplication.Log("Boot completed - Alarm setting");
                    setAlarm(context);
            }
        }
    }
}

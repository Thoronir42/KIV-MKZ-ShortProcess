package cz.zcu.kiwi.shortprocess.model;


import android.support.annotation.NonNull;

import java.util.Locale;

public class Interval {
    public static final int SECOND = 0;
    public static final int MINUTE = 1;
    public static final int HOUR = 2;
    public static final int DAY = 3;

    private final int[] parts;

    /**
     * Normalizes provided interval units into second
     * Usage: <code>joinInterval(int seconds [, int minutes [, int hours [, int days]]]</code>
     *
     * @param parts
     */
    public Interval(@NonNull int... parts) {
        this(joinParts(parts));
    }

    public Interval(long interval) {
        long seconds = interval % 60;
        long minutes = interval / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        this.parts = new int[]{
                (int) seconds,
                (int) (minutes % 60),
                (int) (hours % 24),
                (int) days
        };
    }

    public static long joinParts(int... parts) {
        if (parts.length < 1 || parts.length > 4) {
            throw new IllegalArgumentException("Method takes 1 to 4 arguments, " + parts.length + " given");
        }
        long interval = 0; // interval [days]
        if (parts.length > DAY) {
            interval += parts[DAY];
        }
        interval *= 24;    // interval [hours]

        if (parts.length > HOUR) {
            interval += parts[HOUR];
        }
        interval *= 60;    // interval [minutes]

        if (parts.length > MINUTE) {
            interval += parts[MINUTE];
        }
        interval *= 60;    // interval [seconds]

        interval += parts[SECOND];

        return interval;
    }

    public int getSeconds() {
        return this.parts[SECOND];
    }

    public int getMinutes() {
        return this.parts[MINUTE];
    }

    public int getHours() {
        return this.parts[HOUR];
    }

    public int getDays() {
        return this.parts[DAY];
    }

    public String toString() {
        return String.format(Locale.getDefault(), "%d / %02d:%02d:%02d",
                parts[DAY], parts[HOUR], parts[MINUTE], parts[SECOND]);
    }

    public long toSeconds() {
        return joinParts(parts);
    }
}

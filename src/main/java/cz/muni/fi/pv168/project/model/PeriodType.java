package cz.muni.fi.pv168.project.model;

import java.time.temporal.ChronoUnit;

public enum PeriodType {
    DAILY("daily", ChronoUnit.DAYS),
    WEEKLY("weekly", ChronoUnit.WEEKS),
    MONTHLY("monthly", ChronoUnit.MONTHS),
    YEARLY("yearly", ChronoUnit.YEARS);

    private final String name;
    private final ChronoUnit unit;

    PeriodType(String name, ChronoUnit unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public ChronoUnit getChronoUnit() {
        return unit;
    }
}
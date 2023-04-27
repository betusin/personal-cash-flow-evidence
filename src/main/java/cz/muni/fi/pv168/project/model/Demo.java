package cz.muni.fi.pv168.project.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Demo {
    private static final Random RANDOM = new Random(69);

    public static List<Category> DEMO_CATEGORIES = List.of(
            new Category("Zahradka"),
            new Category("Kvetiny"),
            new Category("Alkoholis"),
            new Category("Jedlo")
    );

    public static List<Transaction> DEMO_ITEMS = List.of(
            new OneTimeTransaction(getRandomDate(), 1500, DEMO_CATEGORIES.get(0), "Jahody"),
            new OneTimeTransaction(getRandomDate(), 1500, DEMO_CATEGORIES.get(1), "Kolace"),
            new OneTimeTransaction(getRandomDate(), 1500, DEMO_CATEGORIES.get(0), "Maliny"),
            new OneTimeTransaction(getRandomDate(), 1500, DEMO_CATEGORIES.get(3), "Hrusky"),
            new OneTimeTransaction(getRandomDate(), 1500, DEMO_CATEGORIES.get(2), "Jahody"),
            new OneTimeTransaction(getRandomDate(), 1500, DEMO_CATEGORIES.get(0), "Vodka")
    );

    public static List<PeriodicTransaction> DEMO_PERIODIC_ITEMS = List.of(
            new PeriodicTransaction(250, DEMO_CATEGORIES.get(0), "Rent", getRandomDate(), null, PeriodType.MONTHLY, 2),
            new PeriodicTransaction(250, DEMO_CATEGORIES.get(2), "Alimenty", LocalDate.now().minus(1, ChronoUnit.YEARS), null, PeriodType.DAILY, 1)
    );

    public static List<Transaction> DEMO_ITEMS_BOTH = mergeTransactions(DEMO_ITEMS, DEMO_PERIODIC_ITEMS);

    public static List<Transaction> mergeTransactions(List<Transaction> oneTime, List<PeriodicTransaction> periodic) {
        var periodicMapped = periodic.stream()
                        .map(periodicTransaction -> (Transaction) periodicTransaction);
        return Stream.concat(oneTime.stream(), periodicMapped).collect(Collectors.toList());
    }

    public static LocalDate getRandomDate() {
        long days = RANDOM.nextLong();
        days %= 150;
        return LocalDate.now().plus(days, ChronoUnit.DAYS);
    }
}

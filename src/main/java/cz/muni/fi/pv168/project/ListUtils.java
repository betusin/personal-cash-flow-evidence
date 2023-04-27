package cz.muni.fi.pv168.project;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class ListUtils {

    private ListUtils() {

    }

    public static <T> List<T> concat(List<? extends T> a, List<? extends T> b) {
        return Stream.concat(a.stream(), b.stream()).collect(Collectors.toList());
    }

    public static <T> void removeItemsAtIndices(List<T> items, Collection<Integer> indices) {
        var offset = 0;
        for (var index : indices.stream().sorted().collect(Collectors.toList())) {
            items.remove(index - offset);
            offset++;
        }
    }

    public static <T> int indexOfFirst(List<T> items, Predicate<T> item) {
        return IntStream.range(0, items.size())
                .filter(value -> item.test(items.get(value)))
                .findFirst()
                .orElse(-1);
    }

    public static <T> List<Integer> indicesOf(List<T> items, Predicate<T> item) {
        return IntStream.range(0, items.size())
                .filter(value -> item.test(items.get(value)))
                .boxed()
                .collect(Collectors.toList());
    }
}

package idatt2105.peakquizbackend.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

/**
 * Service class for converting sorting parameters into Spring Data Sort orders.
 */
public class SortingService {

    /**
     * Converts an array of sort parameters into a list of Sort orders. Each sort parameter should be in the format
     * "field:direction", e.g., "createdAt:desc".
     * 
     * @param sort
     *            The array of sort parameters
     * @return A list of Sort orders
     */
    public static List<Order> convertToOrder(String[] sort) {
        return Stream.of(sort).map(s -> {
            String[] split = s.split(":");
            return new Order(Sort.Direction.fromString(split[1]), split[0]);
        }).collect(Collectors.toList());
    }
}

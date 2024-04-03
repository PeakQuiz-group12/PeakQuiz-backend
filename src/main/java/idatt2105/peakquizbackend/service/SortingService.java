package idatt2105.peakquizbackend.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class SortingService {

  public static List<Order> convertToOrder(String[] sort) {
    return Stream.of(sort)
        .map(s -> {
          String[] split = s.split(",");
          return new Sort.Order(Sort.Direction.fromString(split[1]), split[0]);
        })
        .collect(Collectors.toList());
  }
}

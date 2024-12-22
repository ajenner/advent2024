package days;

import templates.DayTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day22 extends DayTemplate {

    private int price(Long currentSecret) {
        return (int) (currentSecret % 10);
    }

    private Map<List<Integer>, Integer> findSequenceToFirstPrice(Long currentSecret) {
        var sequenceToPrice = new HashMap<List<Integer>, Integer>();
        var differences = new ArrayList<Integer>();
        int previousPrice = price(currentSecret);
        for (int i = 0; i < 2000; i++) {
            currentSecret = nextSecret(currentSecret);
            var price = price(currentSecret);
            var difference = price - previousPrice;
            differences.add(difference);
            if (differences.size() == 4) {
                sequenceToPrice.putIfAbsent(differences, price);
                differences = new ArrayList<>(differences.subList(1, differences.size()));
            }
            previousPrice = price;
        }
        return sequenceToPrice;
    }

    private Map<Long, Map<List<Integer>, Integer>> findSequencesToPrices(List<Long> secrets) {
        var sequencesToPrices = new HashMap<Long, Map<List<Integer>, Integer>>();
        for (long secret : secrets) {
            var sequence = findSequenceToFirstPrice(secret);
            sequencesToPrices.put(secret, sequence);
        }
        return sequencesToPrices;
    }

    private Integer bestBanana(List<Long> secrets, Map<Long, Map<List<Integer>, Integer>> sequencesToPrices) {
        var allSequences = sequencesToPrices.values().stream().flatMap(s -> s.keySet().stream()).collect(Collectors.toSet());
        int max = 0;
        for (List<Integer> sequence : allSequences) {
            int bananas = 0;
            for (Long secret : secrets) {
                var sequences = sequencesToPrices.get(secret);
                bananas += sequences.getOrDefault(sequence, 0);
            }
            max = Math.max(max, bananas);
        }
        return max;
    }

    private Long mix(Long currentSecret, Long givenValue) {
        return currentSecret ^ givenValue;
    }

    private Long prune(Long currentSecret) {
        return currentSecret % 16777216L;
    }

    private Long nextSecret(Long currentSecret) {
        long result = mix(currentSecret, currentSecret * 64);
        result = prune(result);
        result = mix(result, result / 32L);
        result = prune(result);
        result = mix(result, result * 2048L);
        return prune(result);
    }

    public Long part1(ArrayList<String> inputs) {
        var total = 0L;
        for (String input : inputs) {
            var currentSecret = Long.parseLong(input);
            for (int i = 0; i < 2000; i++) {
                currentSecret = nextSecret(currentSecret);
            }
            total += currentSecret;
        }
        return total;
    }

    public String part2(ArrayList<String> inputs) {
        var secrets = inputs.stream().map(Long::parseLong).toList();
        return String.valueOf(bestBanana(secrets, findSequencesToPrices(secrets)));
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        return (part1)? part1(inputs).toString() : part2(inputs);
    }
}

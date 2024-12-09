package days;

import templates.DayTemplate;

import java.util.ArrayList;

public class Day09 extends DayTemplate {

    private record Entry(String ID, int size) {}

    ArrayList<Entry> expandedDisk = new ArrayList<>();

    private void expandDisk(ArrayList<String> inputs) {
        expandedDisk = new ArrayList<>();
        var input = inputs.getFirst();
        var parts = input.split("");

        for (int i = 0; i < parts.length; i++) {
            if (Integer.parseInt(parts[i]) > 0 && i % 2 == 0) {
                expandedDisk.add(new Entry(i/2 + "", Integer.parseInt(parts[i])));
            }
            if (Integer.parseInt(parts[i]) > 0 && i % 2 == 1) {
                expandedDisk.add(new Entry(".", Integer.parseInt(parts[i])));
            }
        }
    }

    private void compactDisk(boolean part1) {
        for (int i = expandedDisk.size() - 1; i >= 0; i--) {
            var bitToMove = expandedDisk.get(i);
            if (bitToMove.ID.equals(".")) {
                continue;
            }
            for (int j = 0; j < i; j++) {
                var potentialSpace = expandedDisk.get(j);
                if (!potentialSpace.ID.equals(".")) {
                    continue;
                }
                if (potentialSpace.size == bitToMove.size) {
                    expandedDisk.set(j, new Entry(bitToMove.ID, potentialSpace.size));
                    expandedDisk.set(i, new Entry(potentialSpace.ID, bitToMove.size));
                    break;
                } else if (potentialSpace.size > bitToMove.size) {
                    expandedDisk.set(j, new Entry(bitToMove.ID, bitToMove.size));
                    expandedDisk.set(i, new Entry(potentialSpace.ID, bitToMove.size));
                    expandedDisk.add(j + 1, new Entry(potentialSpace.ID, potentialSpace.size - bitToMove.size));
                    i++;
                    break;
                } else if (part1){
                    expandedDisk.set(j, new Entry(bitToMove.ID, potentialSpace.size));
                    expandedDisk.set(i, new Entry(potentialSpace.ID, potentialSpace.size));
                    expandedDisk.add(i, new Entry(bitToMove.ID, bitToMove.size - potentialSpace.size));
                    i++;
                    break;
                }
            }
        }
    }

    private Long calculateChecksum() {
        long count = 0L;
        int trueIndex = 0;
        for (Entry entry : expandedDisk) {
            if (!entry.ID.equals(".")) {
                for (int j = 0; j < entry.size; j++) {
                    count += (long) (trueIndex + j) * Integer.parseInt(entry.ID);
                }
            }
            trueIndex += entry.size;
        }
        return count;
    }

    private Long part1(ArrayList<String> inputs) {
        expandDisk(inputs);
        compactDisk(true);
        return calculateChecksum();
    }

    private Long part2(ArrayList<String> inputs) {
        expandDisk(inputs);
        compactDisk(false);
        return calculateChecksum();
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        return (part1)? part1(inputs).toString() : part2(inputs).toString();
    }
}

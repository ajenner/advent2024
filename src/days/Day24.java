package days;

import templates.DayTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Day24 extends DayTemplate {

    HashSet<Wire> wires = new HashSet<>();
    static HashSet<Gate> gates = new HashSet<>();

    public List<String> findSuspiciousGates() {
        ArrayList<String> susGates = new ArrayList<>();
        ArrayList<Gate> extraSusGates = new ArrayList<>();

        var inputGates = gates.stream().filter(Gate::isInput).filter(gate -> gate.gateType.equals("XOR")).toList();
        for (Gate g : inputGates) {
            if (g.isFirst()) {
                if (!g.output.name.equals("z00")) {
                    susGates.add(g.output.name);
                }
                continue;
            } else if (g.output.name.equals("z00")) {
                susGates.add(g.output.name);
            }

            if (g.isOutput()) {
                susGates.add(g.output.name);
            }
        }

        var outputXORGates = gates.stream().filter(gate -> !gate.isInput()).filter(gate -> gate.gateType.equals("XOR")).toList();
        for (Gate g : outputXORGates) {
            if (!g.isOutput()) {
                susGates.add(g.output.name);
            }
        }

        var outputGates = gates.stream().filter(Gate::isOutput).toList();
        for (Gate g : outputGates) {
            if (!g.gateType.equals("XOR") && !g.output.name.equals("z45")) {
                susGates.add(g.output.name);
            }
        }

        for (Gate g : inputGates) {
            if (susGates.contains(g.output.name) || g.output.name.equals("z00")) {
                continue;
            }
            if (outputXORGates.stream().noneMatch(gate -> gate.input1.name.equals(g.output.name) || gate.input2.name.equals(g.output.name))) {
                susGates.add(g.output.name);
                extraSusGates.add(g);
            }
        }

        for (Gate g : extraSusGates) {
            var expectedOutput = "z" + g.input1.name.substring(1);
            var matchingGates = outputXORGates.stream().filter(gate -> gate.output.name.equals(expectedOutput)).toList();
            var firstMatch = matchingGates.get(0);
            var orGates = gates.stream().filter(gate -> gate.gateType.equals("OR"))
                  .filter(gate -> gate.output.name.equals(firstMatch.input1.name)
                          || gate.output.name.equals(firstMatch.input2.name)).toList();
            var matchingORGateOutput = orGates.get(0).output.name;
            var correctOutput = firstMatch.input1.name.equals(matchingORGateOutput)? firstMatch.input2.name: firstMatch.input1.name;
            susGates.add(correctOutput);
        }

        return susGates;
    }

    public String part2(ArrayList<String> inputs) {
        return findSuspiciousGates().stream().sorted().collect(Collectors.joining(","));
    }

    @Override
    public boolean runSamples(boolean part1) {
        return false;
    }


    private void buildInitialWires(ArrayList<String> inputs) {
        wires = new HashSet<>();
        gates = new HashSet<>();
        for (int i = 0; i < inputs.size(); i++) {
            if (inputs.get(i).contains(":")) {
                var parts = inputs.get(i).split(": ");
                wires.add(new Wire(parts[0], Integer.parseInt(parts[1])));
            } else if (inputs.get(i).contains("->")) {
                var parts = inputs.get(i).split(" ");
                Wire wire1;
                if (wires.stream().anyMatch(wire -> wire.name.equals(parts[0]))) {
                    wire1 = wires.stream().filter(wire -> wire.name.equals(parts[0])).findFirst().get();
                } else {
                    wire1 = new Wire(parts[0], -1);
                    wires.add(wire1);
                }
                Wire wire2;
                if (wires.stream().anyMatch(wire -> wire.name.equals(parts[2]))) {
                    wire2 = wires.stream().filter(wire -> wire.name.equals(parts[2])).findFirst().get();
                } else {
                    wire2 = new Wire(parts[2], -1);
                    wires.add(wire2);
                }
                Wire wire3;
                if (wires.stream().anyMatch(wire -> wire.name.equals(parts[4]))) {
                    wire3 = wires.stream().filter(wire -> wire.name.equals(parts[4])).findFirst().get();
                } else {
                    wire3 = new Wire(parts[4], -1);
                    wires.add(wire3);
                }
                gates.add(new Gate(wire1, wire2, parts[1], wire3));
            }
        }
    }
    
    private Long determineGateValues() {
        boolean unsettledGates = gates.stream().anyMatch(gate -> gate.output.value == -1);
        while (unsettledGates) {
            var gateToSettle = gates.stream().filter(gate -> gate.output.value == -1 && gate.input1.value != -1 && gate.input2.value != -1).findFirst().get();
            settleGate(gateToSettle);
            unsettledGates = gates.stream().anyMatch(gate -> gate.output.value == -1);
        }
        return determineBinaryOutput();
    }

    private Long determineBinaryOutput() {
        var sb = new StringBuilder();
        for (Gate g : gates.stream().filter(gate -> gate.output.name.contains("z")).sorted().toList()) {
            sb.append(g.output.value);
        }
        return Long.parseLong(sb.toString(), 2);
    }

    
    private void settleGate(Gate g) {
        gates.remove(g);
        gates.add(g.computeOutput());
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        buildInitialWires(inputs);
        return (part1) ? determineGateValues().toString() : part2(inputs).toString();
    }

    record Wire(String name, int value) {
    }

    record Gate(Wire input1, Wire input2, String gateType, Wire output) implements Comparable<Gate> {
        public Gate computeOutput() {
            if (output.value != -1) {
                System.out.println("oops");
                return new Gate(input1, input2, gateType, output);
            }
            var outputValue = -1;
            switch (gateType) {
                case "AND":
                    if (input1.value == 1 && input2.value == 1) {
                        outputValue = 1;
                    } else {
                        outputValue = 0;
                    }
                    break;
                case "OR":
                    if (input1.value == 1 || input2.value == 1) {
                        outputValue = 1;
                    } else {
                        outputValue = 0;
                    }
                    break;
                case "XOR":
                    if (input1.value != input2.value) {
                        outputValue = 1;
                    } else {
                        outputValue = 0;
                    }
                    break;
            }

            var maybeMatchingGates = gates.stream().filter(gate -> gate.input1.name.equals(output.name) && gate.input1.value == -1).toList();
            for (Gate matchingGate : maybeMatchingGates) {
                gates.remove(matchingGate);
                gates.add(new Gate(new Wire(matchingGate.input1.name, outputValue), matchingGate.input2, matchingGate.gateType, matchingGate.output));
            }

            maybeMatchingGates = gates.stream().filter(gate -> gate.input2.name.equals(output.name) && gate.input2.value == -1).toList();
            for (Gate matchingGate : maybeMatchingGates) {
                gates.remove(matchingGate);
                gates.add(new Gate(matchingGate.input1, new Wire(matchingGate.input2.name, outputValue), matchingGate.gateType, matchingGate.output));
            }

            return new Gate(input1, input2, gateType, new Wire(output.name, outputValue));
        }

        public boolean isInput() {
            return input1.name.startsWith("x") || input2.name.startsWith("x");
        }

        public boolean isOutput() {
            return output.name.startsWith("z");
        }

        public boolean isFirst() {
            return input1.name.equals("x00") || input2.name.equals("x00");
        }

        @Override
        public int compareTo(Gate o) {
            return Integer.compare(Integer.parseInt(o.output.name.replaceAll("[^\\d.]", "")),
                    Integer.parseInt(output.name.replaceAll("[^\\d.]", "")));
        }
    }
}


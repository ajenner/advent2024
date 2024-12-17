package days;

import templates.DayTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day17 extends DayTemplate {

    public String part1(ArrayList<String> inputs) {
        var registerA = Integer.parseInt(inputs.get(0).split("[^\\d-]+")[1]);
        var registerB = Integer.parseInt(inputs.get(1).split("[^\\d-]+")[1]);
        var registerC = Integer.parseInt(inputs.get(2).split("[^\\d-]+")[1]);
        var operations = Arrays.stream(inputs.get(4).split("[^\\d-]+")).filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        var computer = new Computer(registerA, registerB, registerC, operations);
        computer.runComputer();
        return computer.returnOutput();
    }


    public Long part2(ArrayList<String> inputs) {
        var initialB = Integer.parseInt(inputs.get(1).split("[^\\d-]+")[1]);
        var initialC = Integer.parseInt(inputs.get(2).split("[^\\d-]+")[1]);
        var operations = Arrays.stream(inputs.get(4).split("[^\\d-]+")).filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        List<Integer> program = new ArrayList<>();
        List<Integer> remainingOperations = new ArrayList<>(operations);
        long a = 0L;
        while (!remainingOperations.isEmpty()) {
            program.addFirst(remainingOperations.removeLast());
            var programString = String.join(",", program.stream().map(Long::toString).toList());
            Computer computer;
            a--;
            do {
                computer = new Computer(++a, initialB, initialC, operations);
                computer.runComputer(true, program);
            } while (!computer.returnOutput().equals(programString));
            if (!remainingOperations.isEmpty()) {
                a = a << 3;
            }
        }
        return a;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        return (part1) ? part1(inputs) : part2(inputs).toString();
    }

    private static class Computer {
        long registerA = 0;
        long registerB = 0;
        long registerC = 0;

        List<Integer> operations;
        List<Integer> output = new ArrayList<>();

        public Computer(long a, long b, long c, List<Integer> operations) {
            registerA = a;
            registerB = b;
            registerC = c;
            this.operations = operations;
        }

        public void runComputer() {
            runComputer(false, List.of());
        }

        /**
         * 2,4, B = A % 8
         * 1,1, B = B ^ 1
         * 7,5, C = A >> B
         * 1,5, B = B ^ 5
         * 4,3, B = B ^ C
         * 5,5, print ( B % 8 )
         * 0,3, A = A >> 3
         * 3,0  if (A != 0) restart
         */
        public void runComputer(boolean stopEarly, List<Integer> expectedOutput) {
            boolean halted = false;
            int currentPointer = 0;
            long comboOperand;
            while (!halted) {
                int instruction = operations.get(currentPointer);
                boolean jumped = false;
                switch (instruction) {
                    case 0:
                        comboOperand = comboOperandValue(operations.get(currentPointer + 1));
                        registerA = (long) (registerA / (Math.pow(2, comboOperand)));
                        break;
                    case 1:
                        registerB = registerB ^ operations.get(currentPointer + 1);
                        break;
                    case 2:
                        registerB = comboOperandValue(operations.get(currentPointer + 1)) % 8;
                        break;
                    case 3:
                        if (registerA != 0) {
                            currentPointer = operations.get(currentPointer + 1);
                            jumped = true;
                        }
                        break;
                    case 4:
                        registerB = registerB ^ registerC;
                        break;
                    case 5:
                        output.add((int) (comboOperandValue(operations.get(currentPointer + 1)) % 8));
                        if (stopEarly && !outputValid(expectedOutput)) {
                            return;
                        }
                        break;
                    case 6:
                        comboOperand = comboOperandValue(operations.get(currentPointer + 1));
                        registerB = (long) (registerA / (Math.pow(2, comboOperand)));
                        break;
                    case 7:
                        comboOperand = comboOperandValue(operations.get(currentPointer + 1));
                        registerC = (long) (registerA / (Math.pow(2, comboOperand)));
                        break;
                    default:
                        System.out.println("INVALID!!!!");
                }
                if (!jumped) {
                    currentPointer += 2;
                }
                if (currentPointer >= operations.size() - 1) {
                    halted = true;
                }
            }
        }

        private long comboOperandValue(int comboOperand) {
            if (comboOperand >= 0 && comboOperand <= 3) {
                return comboOperand;
            }
            if (comboOperand == 4) {
                return registerA;
            }
            if (comboOperand == 5) {
                return registerB;
            }
            if (comboOperand == 6) {
                return registerC;
            }
            System.out.println("INVALID!!!!");
            return -1;
        }

        public boolean outputValid(List<Integer> target) {
            if (output.size() > target.size()) {
                return false;
            }
            for (int i = 0; i < output.size(); i++) {
                if (!output.get(i).equals(target.get(i))) {
                    return false;
                }
            }
            return true;
        }

        public String returnOutput() {
            return String.join(",", output.stream().map(Long::toString).toList());
        }

    }

}

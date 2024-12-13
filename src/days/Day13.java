package days;

import templates.DayTemplate;

import java.util.ArrayList;

public class Day13 extends DayTemplate {

    /**
     * Cramer's rule:
     * Linear system | axm + bxn = cx
     *               | aym + byn = cy
     * | cx bx |                          | ax cx |
     * | cy by |    cx*by - bx*cy         | ay cy |     ax*cy - cx*ay
     * --------- == ------------- == m ,  --------  ==  ------------- == n
     * | ax bx |    ax*by - bx*ay         | ax bx |     ax*by - bx*ay
     * | ay by |                          | ay by |
     */
    private Long cramer(long aX, long bX, long aY, long bY, long cX, long cY) {
        var determinant = (aX * bY - bX * aY);
        if (determinant == 0) {
            return 0L;
        }
        var m = (cX * bY - bX * cY) / determinant;
        var n = (aX * cY - cX * aY) / determinant;
        if ((aX * m + bX * n ) != cX || (aY * m + bY * n ) != cY) {
            return 0L;
        }
        return 3 * m + n;
    }

    public Long transformInputAndSolve(ArrayList<String> inputs, boolean part1) {
        long count = 0;
        for (int i = 0; i < inputs.size(); i++) {
            var buttonA = inputs.get(i++).split("\\D+");
            var aX = Long.parseLong(buttonA[1]);
            var aY = Long.parseLong(buttonA[2]);
            var buttonB = inputs.get(i++).split("\\D+");
            var bX = Long.parseLong(buttonB[1]);
            var bY = Long.parseLong(buttonB[2]);
            var goal = inputs.get(i++).split("\\D+");
            var goalX = Long.parseLong(goal[1]);
            var goalY = Long.parseLong(goal[2]);

            if (!part1) {
                goalX += 10000000000000L;
                goalY += 10000000000000L;
            }

            count += cramer(aX, bX, aY, bY, goalX, goalY);
        }
        return count;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {

        return transformInputAndSolve(inputs, part1).toString();
    }
}

package com.github.maxpointonline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class Point {
    int x;
    int y;

    private List<Angle> angleHistory = new ArrayList<>();

    Point() {
        x = 0;
        y = 0;
    }

    Point(int a, int b) {
        x = a;
        y = b;
    }

    public boolean hasSameHistoryWith(Point anotherPoint) {
        for (int i = 0; i < angleHistory.size(); i++) {
            if (angleHistory.get(i).equals(anotherPoint.angleHistory.get(i))) {
                return true;
            }
        }

        return false;
    }

    public void addAngleHistory(Angle angle) {
        angleHistory.add(angle);
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}

class Angle {
    private int numerator;
    private int denominator;

    public Angle(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Angle angle = (Angle) o;

        if (numerator != angle.numerator) return false;
        return denominator == angle.denominator;

    }

    @Override
    public int hashCode() {
        int result = numerator;
        result = 31 * result + denominator;
        return result;
    }
}

class Vertical extends Angle {

    public Vertical() {
        super(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
}

class Horizontal extends Angle {

    public Horizontal() {
        super(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
}

class Same extends Angle {
    public Same() {
        super(0, 0);
    }
}

class Segment {
    private final Point a;
    private final Point b;

    private Angle angle;

    public Segment(Point a, Point b) {
        this.a = a;
        this.b = b;

        initAngle();
    }

    public Angle getAngle() {
        return angle;
    }

    private void initAngle() {
        int xDelta = a.x - b.x;
        int yDelta = a.y - b.y;

        if (xDelta == 0 && yDelta == 0) {
            angle = new Same();
        } else if (xDelta == 0) {
            angle = new Vertical();
        } else if (yDelta == 0) {
            angle = new Horizontal();
        } else {
            int gcd = GCD(xDelta, yDelta);
            angle = new Angle(yDelta / gcd, xDelta / gcd);
        }
        b.addAngleHistory(angle);
    }

    private int GCD(int a, int b) {
        if (b == 0) return a;
        return GCD(b, a % b);
    }
}

public class Solution {
    public int maxPoints(Point[] points) {
        if (points.length == 0) {
            return 0;
        }

        int max = 1;

        for (int i = 0; i < points.length; i++) {
            Point a = points[i];
            List<Segment> segmentsStartWithA = new ArrayList<>();

            for (int j = i + 1; j < points.length; j++) {
                Point b = points[j];
                Segment seg = new Segment(a, b);
                if (!a.hasSameHistoryWith(b)) {
                    segmentsStartWithA.add(seg);
                }
            }

            Map<Angle, List<Segment>> sameAngleSegGroups = segmentsStartWithA.stream()
                    .collect(Collectors.groupingBy(Segment::getAngle));

            final int samePointSegCount;
            if (sameAngleSegGroups.containsKey(new Same())) {
                samePointSegCount = sameAngleSegGroups.get(new Same()).size();
            } else {
                samePointSegCount = 0;
            }

            Optional<Integer> maxNumOptional = sameAngleSegGroups.values().stream().map(group -> {
                if (group.get(0).getAngle() instanceof Same) {
                    return samePointSegCount + 1;
                }
                return group.size() + 1 + samePointSegCount;
            }).max((i1, i2) -> i1 - i2);

            if (maxNumOptional.isPresent()) {
                int maxNum = maxNumOptional.get();

                if (maxNum > max) {
                    max = maxNum;
                }
            }
        }
        return max;
    }
}

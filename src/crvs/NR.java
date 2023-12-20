package crvs;

import java.util.Arrays;

public class NR {
    private static final int[] primes = {
            0x8888, 0x888A, 0x8892, 0x8894, 0x88A2, 0x88A4, 0x8912, 0x8914, 0x8922,
            0x8924, 0x8A8A, 0x8AAA, 0x9292, 0x92AA, 0x94AA, 0x952A, 0x8282, 0x828A,
            0x8292, 0x82A2, 0x8484, 0x848A, 0x8492, 0x8494, 0x84A2, 0x84A4, 0x850A,
            0x8512, 0x8514, 0x8522, 0x8524, 0x8544
    };

    private int prime;
    private int mask;
    private int factor;
    private int ix;

    public NR(int prime, int mask, int factor) {
        this.prime = prime;
        this.mask = mask;
        this.factor = factor;
        this.ix = 0;
    }

    public boolean next(int prime, int mask, int factor) {
        int p = prime;
        int m = mask;
        int f = factor;

        p = p % 32;
        if (p < 0) {
            p = 32 + p;
        }

        int rhythm = primes[p];

        f = f % 17;
        if (f < 0) {
            f = 17 + f;
        }

        switch (m) {
            case 1:
                rhythm = rhythm & 0x0F0F;
                break;
            case 2:
                rhythm = rhythm & 0xF003;
                break;
            case 3:
                rhythm = rhythm & 0x1F0;
                break;
            case 0:
                break;
            default:
                rhythm = rhythm & m;
        }

        int modified = rhythm * f;
        int finalPattern = (modified & 0xFFFF) | (modified >>> 16);
        boolean bitStatus = ((finalPattern >>> (15 - ix)) & 1) == 1;

        ix = (ix + 1) % 16;

        return bitStatus;
    }

    public boolean next() {
        return next(prime, mask, factor);
    }

    public boolean next(int factor) {
        return next(prime, mask, factor);
    }

    public boolean peek() {
        ix = ix - 1;
        if (ix < 0) {
            ix = 0;
        }
        return next();
    }

    public void reset() {
        ix = 0;
    }

    public int[] toTable(int numSteps) {
        reset();
        int[] pattern = new int[numSteps];
        for (int step = 0; step < numSteps; step++) {
            pattern[step] = next() ? 1 : 0;
        }
        reset();
        return pattern;
    }

    public void print() {
        int[] pattern = toTable(16);
        System.out.println(Arrays.toString(pattern));
    }
}
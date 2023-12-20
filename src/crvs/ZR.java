package crvs;

import java.util.Arrays;

public class ZR {

    public enum BankName {
        motorik_1, motorik_2, motorik_3,
        pop_1, pop_2, pop_3, pop_4,
        funk_1, funk_2, funk_3,
        sequence,
        prime_2, prime_322,
        king_1, king_2,
        kroboto,
        vodou_1, vodou_2, vodou_3,
        gahu, clave, rhumba,
        jhaptal_1, jhaptal_2,
        chacar, mata, pashto, prime_232
    }

    public static class Banks {

        public static int[][] motorik_1 = {
                {1,0,1,1,0,0,0,0,0,0,1,1,0,0,0,0,1,0,1,1,0,0,0,0,0,0,1,1,0,0,0,0},
                {1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,1,0,1,0,0,0,0,1,0,0,1,0,1,0},
                {0,0,0,0,1,0,1,1,0,0,0,0,1,0,1,1,0,0,0,0,1,0,1,1,0,0,0,0,1,0,1,1},
                {1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0},
        };
        public static int[][] motorik_2 = {
                {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0},
                {0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1},
                {1,0,1,0,0,0,1,0,1,0,1,0,0,0,1,0,1,0,1,0,0,0,1,0,1,0,1,0,0,0,0,0},
                {1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0},
        };
        public static int[][] motorik_3 = {
            {1,1,0,0,0,0,0,0,1,0,1,0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,1,0,0,0,0,0},
            {0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,0},
            {1,0,1,1,1,0,0,1,1,0,1,1,1,0,0,1,1,0,1,1,1,0,0,1,1,0,1,1,1,0,0,1},
            {1,0,0,0,0,0,0,0,1,0,1,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        };
        public static int[][] pop_1 = {
            {1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,1,0},
            {0,0,1,0,0,1,0,0,1,0,1,1,0,0,0,0,0,0,1,0,0,1,0,0,1,0,1,1,0,0,1,0},
            {0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1},
            {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1},
        };
        public static int[][] pop_2 = {
            {0,0,1,0,1,0,1,1,1,0,1,0,0,0,1,1,1,0,1,0,0,0,1,1,1,0,1,0,0,0,1,0},
            {0,0,1,0,1,0,1,1,1,0,1,0,0,0,1,1,1,0,1,0,0,0,1,1,1,0,1,0,0,0,1,0},
            {0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0},
            {0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0},
        };
        public static int[][] pop_3 = {
            {1,0,1,0,1,1,1,1,1,0,1,0,0,0,1,1,1,0,1,0,0,0,1,1,1,0,1,0,1,0,0,0},
            {1,0,1,0,1,1,1,1,1,0,1,0,0,0,1,1,1,0,1,0,0,0,1,1,1,0,1,0,1,0,0,0},
            {0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,1,0,1,0,0,0,0,0,1,0,1,0,0,0,1,0},
            {0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,1,0,1,0,0,0,0,0,1,0,1,0,0,0,1,0},
        };
        public static int[][] pop_4 = {
            {1,0,1,1,0,1,1,0,0,0,0,0,1,1,1,1,1,0,1,1,0,1,1,0,0,0,0,0,1,1,1,1},
            {1,0,1,1,0,1,1,0,0,0,0,0,1,1,1,1,1,0,1,1,0,1,1,0,0,0,0,0,1,1,1,1},
            {1,1,1,1,1,0,1,0,0,0,1,0,1,1,1,1,1,0,1,0,0,0,1,1,1,0,1,0,1,1,0,0},
            {1,1,1,1,1,0,1,0,0,0,1,0,1,1,1,1,1,0,1,0,0,0,1,1,1,0,1,0,1,1,0,0},
        };
        public static int[][] funk_1 = {
            {1,0,0,1,0,1,0,0,1,0,0,1,0,1,0,0},
            {0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0},
            {0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
            {0,1,0,0,0,1,0,0,0,1,0,0,0,1,1,1},
        };
        public static int[][] funk_2 = {
            {0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0},
            {1,0,1,1,0,1,1,1,1,0,1,0,0,1,1,1},
            {0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
            {0,1,0,0,0,1,0,0,0,1,0,0,0,1,1,1},
        };
        public static int[][] funk_3 = {
            {1,0,1,1,0,1,1,1,1,0,1,0,0,1,1,1},
            {1,0,0,1,1,0,0,1,1,0,0,1,1,0,1,0},
            {0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
            {0,1,0,0,0,1,0,0,0,1,0,0,0,1,1,1},
        };
        public static int[][] post = {
            {1,0,0,1,0,1,0,1,1,1,0,0,1,0,1,0,1,0,1,0},
            {1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,1,0,0,0,0,0,0,0,0,1,0,1,0,1,0,1,0},
            {1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0},
        };
        public static int[][] sequence = {
            {1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0},
            {0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0},
            {0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0},
            {0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1},
        };
        public static int[][] prime_2 = {
            {1,0,0,0,0,0,0,0,0,0,0,0},
            {1,0,0,0,0,0,1,0,0,0,0,0},
            {1,0,0,1,0,0,1,0,0,1,0,0},
            {1,1,1,1,1,1,1,1,1,1,1,1},
        };
        public static int[][] prime_322 = {
            {1,0,0,0,0,0,0,0,0,0,0,0},
            {1,0,0,0,0,0,1,0,0,0,0,0},
            {1,0,0,1,0,0,1,0,0,1,0,0},
            {1,1,1,1,1,1,1,1,1,1,1,1},
        };
        public static int[][] king_1 = {
            {1,0,1,0,1,1,0,1,0,1,0,1},
            {1,0,1,0,1,1,0,1,0,1,0,1},
            {1,0,1,1,0,1,0,0,1,1,0,0},
            {1,0,1,1,0,1,0,0,1,1,0,0},
        };
        public static int[][] king_2 = {
            {1,0,1,1,0,1,0,0,1,1,0,0},
            {1,0,1,1,0,1,0,0,0,1,0,0},
            {1,0,1,0,1,1,0,1,0,1,0,1},
            {1,0,1,0,1,1,0,1,0,1,0,1},
        };
        public static int[][] kroboto = {
            {0,0,1,0,1,1,0,0,1,0,1,1},
            {0,0,1,0,1,1,0,0,1,0,1,1},
            {1,0,0,0,0,0,1,0,0,1,0,0},
            {1,0,1,0,1,1,0,1,0,1,0,1},
        };
        public static int[][] vodou_1 = {
            {1,0,1,0,1,0,1,1,0,1,0,1},
            {1,0,1,0,1,0,1,1,0,1,0,1},
            {1,0,0,0,0,0,1,0,0,1,0,0},
            {0,0,0,0,1,1,0,0,0,0,1,1},
        };
        public static int[][] vodou_2 = {
            {0,1,1,0,1,1,0,1,1,0,1,1},
            {0,1,1,0,1,1,0,1,1,0,1,1},
            {1,0,1,0,1,0,1,1,0,1,0,1},
            {0,0,0,0,1,1,0,0,0,0,1,1},
        };
        public static int[][] vodou_3 = {
            {1,0,0,0,0,0,1,0,0,1,0,0},
            {1,0,0,0,0,0,1,0,0,1,0,0},
            {0,1,1,0,1,1,0,1,1,0,1,1},
            {0,0,0,0,1,1,0,0,0,0,1,1},
        };
        public static int[][] gahu = {
            {1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0},
            {1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1,0,0,1,0,0,0,1,0,0,0,1,0,1,0,0},
        };
        public static int[][] clave = {
            {1,0,0,1,0,0,1,0,0,0,1,0,1,0,0,0},
            {1,0,0,1,0,0,1,0,0,0,1,0,1,0,0,0},
            {1,0,1,1,0,1,0,1,1,0,1,0,1,1,0,1},
            {0,0,1,1,0,0,1,1,0,0,1,0,0,0,1,1},
        };
        public static int[][] rhumba = {
            {1,0,0,1,0,0,0,1,0,0,1,0,1,0,0,0},
            {1,0,0,1,0,0,0,1,0,0,1,0,1,0,0,0},
            {1,0,1,1,0,1,0,1,1,0,1,0,1,1,0,1},
            {0,0,1,1,0,0,1,1,0,0,1,0,0,0,1,1},
        };
        public static int[][] jhaptal_1 = {
            {0,1,0,0,1,1,1,0,0,1},
            {0,1,0,0,1,1,1,0,0,1},
            {1,0,1,1,0,0,0,1,1,0},
            {1,0,0,0,0,1,0,0,0,0},
        };
        public static int[][] jhaptal_2 = {
            {1,0,0,0,0,0,0,0,0,0},
            {1,0,1,1,0,0,0,1,1,0},
            {1,0,1,1,0,0,0,1,1,0},
            {1,0,0,0,0,1,0,0,0,0},
        };
        public static int[][] chacar = {
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0},
            {0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
        };
        public static int[][] mata = {
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0},
            {1,0,0,0,1,0,1,0,0,0,0,0,1,0,0,1,0,0},
            {1,0,0,0,1,0,1,0,0,0,0,0,1,1,0,1,0,1},
            {0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,1},
        };
        public static int[][] pashto = {
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,1,1,0,0,0,0,0,0,1,0},
            {0,0,0,0,1,1,0,0,0,0,0,0,1,0},
            {1,0,0,0,0,0,1,0,0,0,1,0,0,0},
        };
        public static int[][] prime_232 = {
            {1,0,0,0,0,0,0,0,0,0,0,0},
            {1,0,0,0,0,0,1,0,0,0,0,0},
            {1,0,1,0,1,0,1,0,1,0,1,0},
            {1,1,1,1,1,1,1,1,1,1,1,1},
        };

    }

    private BankName bank;
    private int child;
    private int offset;
    private int ix;

    public ZR(BankName bank, int child, int offset) {
        this.bank = bank;
        this.child = child;
        this.offset = offset;
        this.ix = 0;
    }

    public boolean next(BankName bankName, int child, int offset) {
        // Retrieve the pattern using the getPattern method
        int[] pattern = getPattern(bankName, child);

        int length = pattern.length;
        int offset_ix = ((this.ix + offset) % length);
        this.ix = (this.ix + 1) % length;

        return pattern[offset_ix] == 1;
    }

    public boolean next(int offset) {
        return next(this.bank, this.child, offset);
    }

    public boolean next() {
        return next(this.bank, this.child, this.offset);
    }

    public boolean peek(BankName bankName, int child, int offset) {
        boolean step = next(bankName, child, offset);
        this.ix--;
        if (this.ix < 0) this.ix = 0;
        return step;
    }

    public boolean peek(int offset) {
        return peek(this.bank, this.child, offset);
    }

    public boolean peek() {
        return peek(this.bank, this.child, this.offset);
    }

    public void reset() {
        this.ix = 0;
    }

    public int[] to_table(BankName bankName, int child, int offset, int numSteps) {
        reset();
        int[] pattern = new int[numSteps];
        for (int step = 0; step < numSteps; step++) {
            pattern[step] = next(bankName, child, offset) ? 1 : 0;
        }
        reset();
        return pattern;
    }

    public int[] to_table(int numSteps) {
       return to_table(this.bank, this.child, this.offset, numSteps);
    }


    public void print() {
        int[] pattern = to_table(bank, child, offset, 32);
        System.out.println(Arrays.toString(pattern));
    }

    private int[][] getBank(BankName bankName) {
        switch (bankName) {
            case motorik_2:
                return Banks.motorik_2;
            case motorik_3:
                return Banks.motorik_3;
            case pop_1:
                return Banks.pop_1;
            case pop_2:
                return Banks.pop_2;
            case pop_3:
                return Banks.pop_3;
            case pop_4:
                return Banks.pop_4;
            case funk_1:
                return Banks.funk_1;
            case funk_2:
                return Banks.funk_2;
            case funk_3:
                return Banks.funk_3;
            case sequence:
                return Banks.sequence;
            case prime_2:
                return Banks.prime_2;
            case prime_322:
                return Banks.prime_322;
            case king_1:
                return Banks.king_1;
            case king_2:
                return Banks.king_2;
            case kroboto:
                return Banks.kroboto;
            case vodou_1:
                return Banks.vodou_1;
            case vodou_2:
                return Banks.vodou_2;
            case vodou_3:
                return Banks.vodou_3;
            case gahu:
                return Banks.gahu;
            case clave:
                return Banks.clave;
            case rhumba:
                return Banks.rhumba;
            case jhaptal_1:
                return Banks.jhaptal_1;
            case jhaptal_2:
                return Banks.jhaptal_2;
            case chacar:
                return Banks.chacar;
            case mata:
                return Banks.mata;
            case pashto:
                return Banks.pashto;
            case prime_232:
                return Banks.prime_232;
            default:
                return Banks.motorik_1; // default to motorik_1
        }
    }

    private int[] getPattern(BankName bankName, int child) {
        int[][] bank = getBank(bankName);
        return bank[child % 4];
    }

}


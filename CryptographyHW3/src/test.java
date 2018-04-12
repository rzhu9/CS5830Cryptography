public class test {
    public static void main(String args[]) {
        long a = 18;
        long b = 85;
        long c = 145;
        System.out.println(encrypt(a, b, c));
    }

    private static long encrypt(long x, long e, long N) {
        return modPow(x, e, N);
    }

    // used for computing (a^b) % c
    private static long modPow(long a, long b, long c) {
        long res = 1;
        for (int i = 0; i < b; i++)
        {
            res *= a;
            //System.out.println(res);
            res %= c;
            //System.out.println(res);
        }
        return res % c;
    }
}

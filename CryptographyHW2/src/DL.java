import java.math.BigInteger;
import java.util.*;

public class DL {

    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = new BigInteger("2");
    private static final BigInteger THREE = new BigInteger("3");

    private static Integer p; // a random prime with bit length no more than n
    private static Integer q; // a random prime with bit length no more than n
    private static Integer x; // message waiting to be encrypted
    private static Integer y; // ciphertext that is encrypted from x
    private static List<Integer> Z; // the set of Z*p

    public static void main(String args[]) {
        // provide the value n as the input
        System.out.print("please enter the value of n: ");
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        // generate a prime number q with # of bits = n
        q = GEN_q(n);
        // picking a random n-bit safe prime
        // multiply q by 2, then plus 1
        // check if the p is a prime or not,
        // if no, generate another prime number q
        p = 2 * q + 1;
        BigInteger P = new BigInteger(Integer.toString(p));
        while (!isProbablePrime(P, 6) || (p > (int)Math.pow(2, n) - 1)){
            q = GEN_q(n);
            p = 2 * q + 1;
            P = new BigInteger(Integer.toString(p));
        }
        p = Integer.parseInt(P.toString());
        System.out.println("p: " + p);
        // picking a generator g for Zp∗.
        int g = GEN_g(p);
        System.out.println("g: " + g);
        Random rand = new Random();
        int index = rand.nextInt(Z.size());
        x = Z.get(index);
        System.out.println("x: " + x);
        y = encrypt(x, g, p);
        System.out.println("y: " + y);
        int x_prime = decrpyt(y, g, p);
        System.out.println("x_prime: " + x_prime);
        if (x_prime == x) {
            System.out.println("The DL algorithm is functioning correctly");
        }
    }
    private static int GEN_q(int n) {
        Random rand = new Random();
        int num = rand.nextInt((int)Math.pow(2, n) - 2) + 2;
        String p = Integer.toString(num);
        BigInteger P = new BigInteger(p);
        while (!isProbablePrime(P, 6)) {
            num = rand.nextInt((int)Math.pow(2, n) - 2) + 2;
            p = Integer.toString(num);
            P = new BigInteger(p);
        }
        return Integer.parseInt(P.toString());
    }

    private static int GEN_g(int p){
        Z = new ArrayList<>();
        for (int i = 1; i < p; i++) {
            Z.add(i);
        }
        int g = 0;
        System.out.println("Z: " + Z);
        for (int i = 0; i < Z.size(); i++) {
            g = Z.get(i);
            if (verify_g(g)) {
                return g;
            }
        }
        return g;
    }

    private static boolean verify_g(int g) {
        List<Integer> prime_factor = new ArrayList<>();
        for (int i = 1; i < p; i++) {
            if ((p-1) % i == 0 && isProbablePrime(new BigInteger(Integer.toString(i)),6)) {
                prime_factor.add(i);
            }
        }

        for (int i = 0; i < prime_factor.size(); i++) {
            int q = prime_factor.get(i);
            if ((int)Math.pow(g, (p-1)/ q)% p == 1 % p) return false;
        }
        return true;
    }

    // using the Miller-Rabin algorithm to check if the number is prime or not
    private static boolean isProbablePrime(BigInteger n, int k) {
        if (n.compareTo(ONE) == 0)
            return false;
        if (n.compareTo(THREE) < 0)
            return true;
        int s = 0;
        BigInteger d = n.subtract(ONE);
        while (d.mod(TWO).equals(ZERO)) {
            s++;
            d = d.divide(TWO);
        }
        for (int i = 0; i < k; i++) {
            BigInteger a = uniformRandom(TWO, n.subtract(ONE));
            BigInteger x = a.modPow(d, n);
            if (x.equals(ONE) || x.equals(n.subtract(ONE)))
                continue;
            int r = 0;
            for (; r < s; r++) {
                x = x.modPow(TWO, n);
                if (x.equals(ONE))
                    return false;
                if (x.equals(n.subtract(ONE)))
                    break;
            }
            if (r == s) // None of the steps made x equal n-1.
                return false;
        }
        return true;
    }

    private static BigInteger uniformRandom(BigInteger bottom, BigInteger top) {
        Random rnd = new Random();
        BigInteger res;
        do {
            res = new BigInteger(top.bitLength(), rnd);
        } while (res.compareTo(bottom) < 0 || res.compareTo(top) > 0);
        return res;
    }

    private static int encrypt(int x, int g, int p) {
        return (int) modPow((long) g, (long)x, (long)p);
    }

    private static int decrpyt(int y, int g, int p) {
        for (int i = 1; i < p; i++) {
            if (modPow((long)g, (long)i, (long)p) == (long)y) {
                return i;
            }
        }
        return -1;
    }

    // used for computing (a^b) % c
    private static long modPow(long a, long b, long c) {
        long res = 1;
        for (int i = 0; i < b; i++)
        {
            res *= a;
            res %= c;
        }
        return res % c;
    }
}

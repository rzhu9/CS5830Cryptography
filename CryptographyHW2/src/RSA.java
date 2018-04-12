import java.math.BigInteger;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class RSA {

    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = new BigInteger("2");
    private static final BigInteger THREE = new BigInteger("3");

    private static Long p;
    private static Long q;
    private static Long N;
    private static Long phi_N;
    private static Long e;
    private static Long d;
    private static List<Long> Z;
    private static Long x;

    public static void main(String args[]) {
        // provide the value n as the input
        System.out.print("please enter the value of n: ");
        Scanner in = new Scanner(System.in);
        Long n = in.nextLong();
        // generate a prime number p
        p = GEN_pq(n);
        System.out.println("p: " + p);
        // generate a prime number q
        do {
            q = GEN_pq(n);
        } while(p==q);

        System.out.println("q: " + q);
        // compute the value of N
        N = p * q;
        System.out.println("N: " + N);
        // compute the value of phi(N)
        phi_N = (p - 1) * (q - 1);
        System.out.println("phi(N) : " + phi_N);
        // generate the exponential number e (e must be smaller than phi(N)
        // and it must be relative prime with phi(N))
        e = GEN_e(phi_N);
        System.out.println("e: " + e);
        // the trapdoor for RSA: d = (k * phi(N) + 1) / e
        d = GEN_d(phi_N);
//        d = (2 * (phi_N) + 1) / e;
        System.out.println("d: " + d);
        // find and add all possible values into set Zn*
        Z = new ArrayList<Long>();
        for (long i = 1; i < N; i++) {
            if (gcd(i, N) == 1) {
                Z.add(i);
            }
        }
        // randomly select an element from the set Zn*
        Random rand = new Random();
        int index = rand.nextInt(Z.size() - 1);
        x = Z.get(index);
        System.out.println("x: " + x);
        long y = encrypt(x, e, N);
        System.out.println("y: " + y);
        long x_prime = decrpyt(y, d, N);
        System.out.println("decrypted x: " + x_prime);
        if (x_prime == x) System.out.println("The RSA algorithm is functioning correctly");
    }

    private static long GEN_pq(long n) {
        Random rand = new Random();
        int num = rand.nextInt((int)Math.pow(2, n) - 2) + 2;
        String p = Long.toString(num);
        BigInteger P = new BigInteger(p);
        while (!isProbablePrime(P, 6)) {
            num = rand.nextInt((int)Math.pow(2, n) - 2) + 2;
            p = Long.toString(num);
            P = new BigInteger(p);
        }
        return Long.parseLong(P.toString());
    }

    private static long GEN_e(long phi_N) {
        Random rand = new Random();
        long e = rand.nextInt(((int)phi_N - 1)) + 1;
        if (e % 2 == 0) e --;
        while (gcd(e, phi_N) != 1) {
            e = rand.nextInt((int)phi_N - 1) + 1;
            if (e % 2 == 0) e --;
        }
        return e;
    }

    private static long GEN_d(long phi_N) {
        int k = 1;
        boolean done = false;
        while (done == false) {
            if ((k * (phi_N) + 1) % e == 0 && (k * (phi_N) + 1) / e > 0) {
                done = true;
            }
            else {
                k ++;
            }
        }
        return (k * (phi_N) + 1) / e;
    }

    private static long gcd(long a, long b) {
        long t;
        while(b != 0){
            t = a;
            a = b;
            b = t%b;
        }
        return a;
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

    private static long encrypt(long x, long e, long N) {
        return modPow(x, e, N);
    }

    private static long decrpyt(long y, long d, long N) {
        return modPow(y, d, N);
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

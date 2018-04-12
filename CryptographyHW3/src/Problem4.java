import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class Problem4 {

    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = new BigInteger("2");
    private static final BigInteger THREE = new BigInteger("3");


    public static void main(String args[]) {
        while (true) {
        System.out.println("*****************Now start running program for problem 4.1*****************");
        System.out.println("RSA algorithm is used to build pseudoRandom Generator");
        System.out.print("please enter the value of n (# of bits for seed): ");
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        long seed = GEN_seed(n); // randomly selected a seed with n bits at most
        System.out.println("seed: " + seed);
        System.out.print("please enter the value of m (# of bits for expanded seed): ");
        int m = in.nextInt(); // entered expanding length
        System.out.println("length after expanding is : " + m);
        String pseudoRandomSequence = problem4_1(seed, n, m); // generate pseudoRandom sequence
        System.out.println("PseudoRandom Sequence with length " + pseudoRandomSequence.length() + ": " + pseudoRandomSequence);
        System.out.println();

        System.out.println("*****************Now start running program for problem 4.2*****************");
        System.out.print("please enter the value of n (# of bits for seed): ");
        n = in.nextInt();
        seed = GEN_seed(n);
        System.out.println("seed: " + seed);
        String cipherText = problem4_2(seed, n);
        System.out.println("Private Key Encryption result is " + cipherText);
        System.out.println();
        System.out.println("*****************Now start running program for problem 4.3*****************");
        System.out.println("Set up n = 7, seed = 15, p = 71, q = 89, N = 6319, e = 89");
        seed = 15;
        System.out.println("seed: " + seed);
        Double time_slow = problem4_3();
        time_slow = time_slow * 2.7778e-13; // convert from nanosecond to hour
        System.out.println("Total amount of time to encrypt 10MB file with above parameter is approximately " + time_slow + " hours");
        System.out.println();
        System.out.println("*****************Now start running program for problem 4.4*****************");
        System.out.println("Set up n = 7, seed = 15, p = 71, q = 89, N = 6319, e = 89");
        seed = 15;
        System.out.println("seed: " + seed);
        Double time_fast = problem4_4();
        time_fast = time_fast * 2.7778e-13; // convert from nanosecond to hour
        System.out.println("Total amount of time to encrypt 10MB file with above parameter is approximately " + time_fast + " hours");
        System.out.println();
        System.out.println("*****************Now start running program for problem 4.5*****************");
        System.out.print("please enter the value of n (# of bits for seed): ");
        n = in.nextInt();
        seed = GEN_seed(n);
        System.out.println("seed: " + seed);
        long index = GEN_seed(n);
        System.out.println("index: " + index);
        String pseudoRandomFunctionResult = problem4_5(seed, n, index);
        System.out.println("Output of PseudoRandom Function is " + pseudoRandomFunctionResult);
        System.out.println();
        System.out.println("*****************Now start running program for problem 4.6*****************");
        System.out.print("please enter the value of n (# of bits for seed): ");
        n = in.nextInt();
        seed = GEN_seed(n);
        System.out.println("seed: " + seed);
        index = GEN_seed(n);
        System.out.println("index: " + index);
        String cipherMultiText = problem4_6(seed, n, index);
        System.out.println("Multi-message Private Key Encryption is " + cipherMultiText);
        }
    }

    private static String problem4_1(long seed, int n, int m) {
        if (n == 1) return Long.toBinaryString((long)Math.pow(2, m) - 1);
        long p, q, N, phi_N, e;
        do {
            p = GEN_pq(n);
            System.out.println("p: " + p);
            // generate a prime number q
            do {
                q = GEN_pq(n);
            } while (p == q);

            System.out.println("q: " + q);
            N = p * q;
            System.out.println("N: " + N);
            e = GEN_e(N);
            System.out.println("e: " + e);
        } while ((seed != 1) && (seed != 0) && (encrypt(seed, e, N) == seed) && (encrypt(seed, e, N) == 0));
        // do while loop is used to check output of RSA algorithm, if output == input, that's too bad
        // we need to regenerate those parameters
        long current_s = seed;
        long next_s = 0;
        String result = "";
        while (result.length() < m) {
            //System.out.println("current s is " + current_s);
            String hard_core = extractHard_core(current_s);
            result = result + hard_core;
            next_s = encrypt(current_s, e, N);
            //System.out.println("next s is : " + next_s);
            current_s = next_s;
        }
        result = result.substring(0, m);
        //System.out.println("pseudoRadom Sequence: " + result);
        return result;
    }

    private static String problem4_2(long seed, int n) {
        Random rand = new Random();
        long message = rand.nextLong();
        while (Long.toBinaryString(message).length() < n || message < 0) {
            message = rand.nextLong();
        }
        System.out.println("the binary form of message for encryption is " + Long.toBinaryString(message));
        int messageLength = Long.toBinaryString(message).length();
        System.out.println("# of bits for message: " + messageLength);
        String key = problem4_1(seed, n, messageLength);
        System.out.println("expanded key to length " + key.length() + " gets " + key);
        return Long.toBinaryString(Long.parseLong(key, 2) ^ message);
    }

    private static double problem4_3() {
        int p, q, N, phi_N, e;
        double startTime = System.nanoTime();
        Random rand = new Random();
        long message = rand.nextLong();
        while (Long.toBinaryString(message).length() < 7 || message < 0) {
            message = rand.nextLong();
        }
        //System.out.println("the binary form of message for encryption is " + Long.toBinaryString(message));
        int message_Length = Long.toBinaryString(message).length();
        System.out.println("# of bits for message: " + message_Length);
        p = 71;
        q = 89;
        N = p * q;
        e = 89;
        long current_s = 15; // seed is set up to be 15
        long next_s = 0;
        String result = "";
        while (result.length() < message_Length) {
            //System.out.println("current s is " + current_s);
            result = result + extractHard_core(current_s);
            next_s = encrypt(current_s, e, N);
            //System.out.println("next s is : " + next_s);
            current_s = next_s;
        }
        result = result.substring(0, message_Length);
        //System.out.println("pseudoRadom Sequence: " + result);
        String tmp = Long.toBinaryString(Long.parseLong(result, 2) ^ message);
        double endTime = System.nanoTime();
        double totalTime = endTime - startTime;
        totalTime = 10 * 1024 * 1024 * totalTime / ((double)message_Length / (double)8);
        //System.out.println("expanded key to length " + key.length() + " gets " + key);
        return totalTime;
    }

    private static double problem4_4() {
        int p, q, N, phi_N, e;
        double startTime = System.nanoTime();
        Random rand = new Random();
        long message = rand.nextLong();
        while (Long.toBinaryString(message).length() < 7 || message < 0) {
            message = rand.nextLong();
        }
        //System.out.println("the binary form of message for encryption is " + Long.toBinaryString(message));
        int message_Length = Long.toBinaryString(message).length();
        System.out.println("# of bits for message: " + message_Length);
        //String key = problem4_1(message_Length);
        p = 71;
        q = 89;
        N = p * q;
        e = 89;
        long current_s = 15; // seed is set up to be 15
        long next_s = 0;
        String result = "";
        while (result.length() < message_Length) {
            //System.out.println("current s is " + current_s);
            String tmp = Long.toBinaryString(current_s);
            char hard_core = tmp.charAt(tmp.length()/2);
            result = result + hard_core;
            next_s = encrypt(current_s, e, N);
            //System.out.println("next s is : " + next_s);
            current_s = next_s;
        }
        result = result.substring(0, message_Length);
        //System.out.println("pseudoRadom Sequence: " + result);
        String tmp = Long.toBinaryString(Long.parseLong(result, 2) ^ message);
        double endTime = System.nanoTime();
        double totalTime = endTime - startTime;
        totalTime = 10 * 1024 * 1024 * totalTime / ((double)message_Length / (double)8);
        //System.out.println("expanded key to length " + key.length() + " gets " + key);
        return totalTime;
    }

    private static String problem4_5(long seed, int n, long index) {
        String index_binary = Long.toBinaryString(index);
        System.out.println("# of bits for index is " + index_binary.length());
        int difference = n - index_binary.length();
        for (int i = 0; i < difference; i++) {
            index_binary = "0" + index_binary;
        }
        System.out.println("index after zero extending is " + index_binary + " with length " + index_binary.length());
        int level = n;
        for (int i = 0; i < level; i ++) {
            char current_bit = index_binary.charAt(i);
            System.out.println("current seed is: " + seed);
            String expand = problem4_1(seed, n, n * 2);
            System.out.println("after expanding, the pseudoRandom sequence becomes " + expand + " with length " + expand.length());
            if (current_bit == '0') {
                seed = Long.parseLong(expand.substring(0, n), 2);
                System.out.println("selected half of expanded seed is " + seed);
            }
            else {
                seed = Long.parseLong(expand.substring(n, n * 2), 2);
                System.out.println("selected half of expanded seed is " + seed);
            }
        }
        String result = Long.toBinaryString(seed);
        difference = n - result.length();
        for (int i = 0; i < difference; i++) {
            result = "0" + result;
        }
        return result;
    }

    private static String problem4_6(long seed, int n, long index) {
        Random rand = new Random();
        long message = rand.nextInt((int)Math.pow(2, n));
        String key = problem4_5(seed, n, index);
        return Long.toBinaryString(index) + Long.toBinaryString(message ^ Long.parseLong(key, 2));
    }

    private static int GEN_seed(int n) {
        if (n == 1) return 1;
        Random rand = new Random();
        // I do not want the seed to be 1
        int num = rand.nextInt((int)Math.pow(2, n) - 1 ) + 1;
        while (num == 1) {
            num = rand.nextInt((int)Math.pow(2, n) - 1 ) + 1;
        }
        return num;
    }

    private static long GEN_pq(int n) {
        if (n == 1) return 1;
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

    private static long GEN_e(long N) {
        if (N == 1) return 1;
        Random rand = new Random();
        // if phi_N is too big, the randomly selected e will not be small
        // this could take too much time on calculation, so I set up a threshold
        // to control the value of e
        if (N > 1000) {
            long e = rand.nextInt(98) + 2;
//            while (gcd(e, N) != 1 || (e == 1) || (e % 2 == 0)) {
//                e = rand.nextInt(99) + 1;
//            }
            return e;
        }
        else {
            long e = rand.nextInt((int)N - 1) + 1;
//            if (e % 2 == 0) e --;
//            while (gcd(e, N) != 1 || ((e == 1) && (N > 3)) || (e % 2 == 0)) {
//                e = rand.nextInt((int)N - 1) + 1;
//            }
            return e;
        }
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

    private static String extractHard_core(long s0) {
        String b =  Long.toBinaryString(s0);
        //System.out.println("binary form of current s [" + s0 + "] is: " + b);
        if (b.length() == 1) {
            return b;
        }
        long length = b.length();
        int start = (int)(length * 0.25);
        int end = (int) (length * 0.75);
        String result = "";
        int choice = 0;
        // if length is not divisible by four, we have two possible solution for mid half
        // just randomly select one
        if (b.length() % 4 != 0) {
            Random rand = new Random();
            choice = rand.nextInt(2);
        }
        if (choice == 0) {
            result = b.substring(start, end);
        }
        else {
            start ++;
            end ++;
            result = b.substring(start, end);
        }
        return result;
    }
}

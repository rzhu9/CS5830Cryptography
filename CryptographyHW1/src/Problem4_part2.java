import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Problem4_part2 {
    private static List<String> expectedcipher = new ArrayList<>();
    private static List<String> expectedplain = new ArrayList<>();
    private static List<Integer> order = new ArrayList<>();
    private static int[] position = new int[10];
    private static int[] length = new int[10];
    private static String alphabets = "2T3O4HNM5LRGIPCVEZDBSYFXAWJ6UQK7";
    private static int[] r0 = new int[47];
    private static int[] r1 = new int[53];
    private static int[] r2 = new int[59];
    private static int[] r3 = new int[61];
    private static int[] r4 = new int[64];
    private static int[] r5 = new int[65];
    private static int[] r6 = new int[67];
    private static int[] r7 = new int[69];
    private static int[] r8 = new int[71];
    private static int[] r9 = new int[73];

    public static void main(String args[]) throws FileNotFoundException {
        readcipherText();
        readplainText();
        configureOrder();
        configureRotor();
        configurePosition();
        configureLength();

        int index = 0;

        for (int i = 0; i < expectedcipher.size(); i++) {
            String cipherText = expectedcipher.get(i);
            for (int j = 0; j < cipherText.length(); j++) {
                char cipherChar = cipherText.charAt(j);
                char plainChar = expectedplain.get(i).charAt(j);
                if (cipherChar == '2' || cipherChar == '7') {
                    int result = alphabets.indexOf(cipherChar) ^ alphabets.indexOf(plainChar);
                    int b4 = result % 2;
                    result = result / 2;
                    int b3 = result % 2;
                    result = result / 2;
                    int b2 = result % 2;
                    result = result / 2;
                    int b1 = result % 2;
                    result = result / 2;
                    int b0 = result % 2;
                    r0[position[0]] = b0;
                    r1[position[1]] = b1;
                    r2[position[2]] = b2;
                    r3[position[3]] = b3;
                    r4[position[4]] = b4;
                }
                updatePosition();
            }
        }

        configurePosition();
        for (int i = 0; i < expectedcipher.size(); i++) {
            String plainText = expectedplain.get(i);
            for (int j = 0; j < plainText.length(); j++) {
                char cipherChar = plainText.charAt(j);
                char plainChar = expectedplain.get(i).charAt(j);
                int b0 = r0[position[0]];
                int b1 = r1[position[1]];
                int b2 = r2[position[2]];
                int b3 = r3[position[3]];
                int b4 = r4[position[4]];
                int c_total = alphabets.indexOf(plainChar);

                int c4 = c_total % 2;
                c_total = c_total / 2;
                int c3 = c_total % 2;
                c_total = c_total / 2;
                int c2 = c_total % 2;
                c_total = c_total / 2;
                int c1 = c_total % 2;
                c_total = c_total / 2;
                int c0 = c_total % 2;

                c0 ^= b0;
                c1 ^= b1;
                c2 ^= b2;
                c3 ^= b3;
                c4 ^= b4;

                int tmpc0 = c0;
                int tmpc1 = c1;
                int tmpc2 = c2;
                int tmpc3 = c3;
                int tmpc4 = c4;
                int fakeb5 = 0;
                int fakeb6 = 0;
                int fakeb7 = 0;
                int fakeb8 = 0;
                int fakeb9 = 0;

                for (int trial = 0; trial < 32; trial++) {

                    int fakeb56789 = trial;
                    fakeb9 = fakeb56789 % 2;
                    fakeb56789 = fakeb56789 / 2;
                    fakeb8 = fakeb56789 % 2;
                    fakeb56789 = fakeb56789 / 2;
                    fakeb7 = fakeb56789 % 2;
                    fakeb56789 = fakeb56789 / 2;
                    fakeb6 = fakeb56789 % 2;
                    fakeb56789 = fakeb56789 / 2;
                    fakeb5 = fakeb56789 % 2;

                    int tmp = 0;
                    if (fakeb5 == 1){
                        tmp = tmpc0;
                        tmpc0 = tmpc4;
                        tmpc4 = tmp;
                    }
                    if (fakeb6 == 1){
                        tmp = tmpc0;
                        tmpc0 = tmpc1;
                        tmpc1 = tmp;
                    }
                    if (fakeb7 == 1){
                        tmp = tmpc1;
                        tmpc1 = tmpc2;
                        tmpc2 = tmp;
                    }
                    if (fakeb8 == 1){
                        tmp = tmpc2;
                        tmpc2 = tmpc3;
                        tmpc3 = tmp;
                    }
                    if (fakeb9 == 1){
                        tmp = tmpc3;
                        tmpc3 = tmpc4;
                        tmpc4 = tmp;
                    }

                    int fakeoutput = tmpc0 * 16 + tmpc1 * 8 + tmpc2 * 4 + tmpc3 * 2 + tmpc4;
                    char fakeencryption = alphabets.charAt(fakeoutput);
                    if (fakeencryption == cipherChar) {
                        r5[position[5]] = fakeb5;
                        r6[position[6]] = fakeb6;
                        r7[position[7]] = fakeb7;
                        r8[position[8]] = fakeb8;
                        r9[position[9]] = fakeb9;
                    }
                    else {
                        tmpc0 = c0;
                        tmpc1 = c1;
                        tmpc2 = c2;
                        tmpc3 = c3;
                        tmpc4 = c4;
                    }
                }
                updatePosition();
            }
        }
        String str = "";
        for (int i = 0; i < length[0]; i++) {
            str += r0[i];
        }
        System.out.println(str);
        str = "";
        for (int i = 0; i < length[1]; i++) {
            str += r1[i];
        }
        System.out.println(str);
        str = "";
        for (int i = 0; i < length[2]; i++) {
            str += r2[i];
        }
        System.out.println(str);
        str = "";
        for (int i = 0; i < length[3]; i++) {
            str += r3[i];
        }
        System.out.println(str);
        str = "";
        for (int i = 0; i < length[4]; i++) {
            str += r4[i];
        }
        System.out.println(str);
        str = "";
        for (int i = 0; i < length[5]; i++) {
            str += r5[i];
        }
        System.out.println(str);
        str = "";
        for (int i = 0; i < length[6]; i++) {
            str += r6[i];
        }
        System.out.println(str);
        str = "";
        for (int i = 0; i < length[7]; i++) {
            str += r7[i];
        }
        System.out.println(str);
        str = "";
        for (int i = 0; i < length[8]; i++) {
            str += r8[i];
        }
        System.out.println(str);
        str = "";
        for (int i = 0; i < length[9]; i++) {
            str += r9[i];
        }
        System.out.println(str);
    }

    private static void readcipherText() throws FileNotFoundException {
        File f = new File("part_2/ciphertext.txt");
        Scanner filein = new Scanner(f);
        while (filein.hasNextLine()) {
            String ciphertext = filein.nextLine();
            expectedcipher.add(ciphertext);
        }
    }

    private static void readplainText() throws FileNotFoundException{
        File f = new File("part_2/plaintext.txt");
        Scanner filein = new Scanner(f);
        while (filein.hasNextLine()) {
            String plaintext = filein.nextLine();
            expectedplain.add(plaintext);
        }
    }

    private static void configureOrder() {
        for (int i = 0; i < 10; i++) {
            order.add(i);
        }
    }

    private static void configureRotor() {
        for (int i = 0; i < 47; i++) {
            r0[i] = 2;
        }
        for (int i = 0; i < 53; i++) {
            r1[i] = 2;
        }
        for (int i = 0; i < 59; i++) {
            r2[i] = 2;
        }
        for (int i = 0; i < 61; i++) {
            r3[i] = 2;
        }
        for (int i = 0; i < 64; i++) {
            r4[i] = 2;
        }
        for (int i = 0; i < 65; i++) {
            r5[i] = 2;
        }
        for (int i = 0; i < 67; i++) {
            r6[i] = 2;
        }
        for (int i = 0; i < 69; i++) {
            r7[i] = 2;
        }
        for (int i = 0; i < 71; i++) {
            r8[i] = 2;
        }
        for (int i = 0; i < 73; i++) {
            r9[i] = 2;
        }
    }

    private static void configurePosition() {
        for (int i = 0; i < position.length; i++) {
            position[i] = 0;
        }
    }

    private static void configureLength() {
        length[0] = 47;
        length[1] = 53;
        length[2] = 59;
        length[3] = 61;
        length[4] = 64;
        length[5] = 65;
        length[6] = 67;
        length[7] = 69;
        length[8] = 71;
        length[9] = 73;

    }
    private static void updatePosition() {
        for (int i = 0; i < 10; i++) {
            int tmp = position[i];
            position[i] = tmp + 1;
            if (position[i] == length[i]) {
                position[i] = 0;
            }
        }
    }
}

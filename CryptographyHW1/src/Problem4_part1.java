import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Problem4_part1 {
    private static List<Integer> order = new ArrayList<>();
    private static List<Integer> offset = new ArrayList<>();
    private static List<String> rotors = new ArrayList<>();
    private static List<String> expectedcipher = new ArrayList<>();
    private static List<String> expectedplain = new ArrayList<>();
    private static String alphabets = "2T3O4HNM5LRGIPCVEZDBSYFXAWJ6UQK7";

    public static void main(String args[]) throws FileNotFoundException {
        // read the order and offset informtion, and store them in list
        readKey();
        // read the cipher informtion, and store them in list
        readcipherText();
        // read the plain informtion, and store them in list
        readplainText();
        System.out.println("4.1********************Start encryption test:********************");
        int correct = 0;
        for (int i = 0; i < expectedplain.size(); i++) {
            String plaintext = expectedplain.get(i);
            // encrypt one line
            String predictedcipherText = EncryptionLine(plaintext);
            String expectedcipherText = expectedcipher.get(i);
            System.out.println("Expected: " + expectedcipherText);
            System.out.println("Actual:   " + predictedcipherText);
            if (predictedcipherText.equals(expectedcipherText)) {
                System.out.println("Encryption of message " + i + ": " + "is CORRECT");
                correct ++;
            }
            else {
                System.out.println("Encryption of message " + i + ": " + "is INCORRECT");
            }
            System.out.println();
        }
        System.out.println("Overall encryption result is " + correct/expectedplain.size()*100 + "% CORRECT!");
        System.out.println();
        System.out.println();
        System.out.println();


        System.out.println("4.2********************Start decryption test:********************");
        correct = 0;
        for (int i = expectedcipher.size() - 1; i >= 0; i--) {
            String ciphertext = expectedcipher.get(i);
            String predictedplainText = DecryptionLine(ciphertext);
            String expectedplainText = expectedplain.get(i);
            System.out.println("Expected: " + expectedplainText);
            System.out.println("Actual:   " + predictedplainText);
            if (predictedplainText.equals(expectedplainText)) {
                System.out.println("Decryption of cipher " + i + ": " + "is CORRECT");
                correct ++;
            }
            else {
                System.out.println("Decryption of cipher " + i + ": " + "is INCORRECT");
            }
            System.out.println();
        }
        System.out.println("Overall decryption result is " + correct/expectedcipher.size()*100 + "% CORRECT!");

    }

    private static void readKey() throws FileNotFoundException {
        File f = new File("part_1/key.txt");
        Scanner filein = new Scanner(f);
        String[] line1 = filein.nextLine().split(" ");
        for (int i = 1; i < line1.length; i++) {
            order.add(Integer.parseInt(line1[i]));
        }
        String[] line2 = filein.nextLine().split(" ");
        for (int i = 1; i < line2.length; i++) {
            offset.add(Integer.parseInt(line2[i]));
        }
        f = new File("part_1/rotors.txt");
        filein = new Scanner(f);
        while (filein.hasNextLine()) {
            rotors.add(filein.nextLine().split(" ")[2]);
        }
    }

    private static void readcipherText() throws FileNotFoundException{
        File f = new File("part_1/ciphertext.txt");
        Scanner filein = new Scanner(f);
        while (filein.hasNextLine()) {
            String ciphertext = filein.nextLine();
            expectedcipher.add(ciphertext);
        }
    }

    private static void readplainText() throws FileNotFoundException{
        File f = new File("part_1/plaintext.txt");
        Scanner filein = new Scanner(f);
        while (filein.hasNextLine()) {
            String plaintext = filein.nextLine();
            expectedplain.add(plaintext);
        }
    }
    // encrypt one line, for each char in one line, encrypt them separately
    private static String EncryptionLine(String message) {
        String outputLine = "";
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            char outputChar = Encryption(c);
            outputLine = outputLine + outputChar;
        }
        return outputLine;
    }

    private static char Encryption(char message) {
        // get the value of b0~b9 based on information in order and offset
        int b0 = rotors.get(order.get(0)).charAt(offset.get(0)) - '0';
        int b1 = rotors.get(order.get(1)).charAt(offset.get(1)) - '0';
        int b2 = rotors.get(order.get(2)).charAt(offset.get(2)) - '0';
        int b3 = rotors.get(order.get(3)).charAt(offset.get(3)) - '0';
        int b4 = rotors.get(order.get(4)).charAt(offset.get(4)) - '0';
        int b5 = rotors.get(order.get(5)).charAt(offset.get(5)) - '0';
        int b6 = rotors.get(order.get(6)).charAt(offset.get(6)) - '0';
        int b7 = rotors.get(order.get(7)).charAt(offset.get(7)) - '0';
        int b8 = rotors.get(order.get(8)).charAt(offset.get(8)) - '0';
        int b9 = rotors.get(order.get(9)).charAt(offset.get(9)) - '0';

        // get the corresponding number (0~31) for the plainChar
        int c_total = alphabets.indexOf(message);
        // get the c0~c4
        int c4 = c_total % 2;
        c_total = c_total / 2;
        int c3 = c_total % 2;
        c_total = c_total / 2;
        int c2 = c_total % 2;
        c_total = c_total / 2;
        int c1 = c_total % 2;
        c_total = c_total / 2;
        int c0 = c_total % 2;
        // ci = ci xor bi
        c0 ^= b0;
        c1 ^= b1;
        c2 ^= b2;
        c3 ^= b3;
        c4 ^= b4;
        // swap bits based on b5~b9
        int tmp = 0;
        if (b5 == 1){
            tmp = c0;
            c0 = c4;
            c4 = tmp;
        }
        if (b6 == 1){
            tmp = c0;
            c0 = c1;
            c1 = tmp;
        }
        if (b7 == 1){
            tmp = c1;
            c1 = c2;
            c2 = tmp;
        }
        if (b8 == 1){
            tmp = c2;
            c2 = c3;
            c3 = tmp;
        }
        if (b9 == 1){
            tmp = c3;
            c3 = c4;
            c4 = tmp;
        }
        // get the value of cipherChar
        int output = c0 * 16 + c1 * 8 + c2 * 4 + c3 * 2 + c4;
        // convert the value back to the character
        char encryption = alphabets.charAt(output);
        // update the position of each wheel
        for (int i = 0; i < offset.size(); i++) {
            int tmp2 = offset.get(i);
            tmp2  = tmp2 + 1;
            if (tmp2 == rotors.get(order.get(i)).length()) tmp2 = 0;
            offset.set(i, tmp2);
        }
        return encryption;
    }

    private static String DecryptionLine(String cipher) {
        String outputLine = "";
        for (int i = cipher.length() - 1; i >= 0; i--) {
            char c = cipher.charAt(i);
            char outputChar = Decryption(c);
            outputLine = outputChar + outputLine;
        }
        return outputLine;
    }

    private static char Decryption(char cipher) {
        // TODO
        // move wheel back by one position
        for (int i = 0; i < offset.size(); i++) {
            int tmp2 = offset.get(i);
            tmp2 = tmp2 - 1;
            if (tmp2 == -1) tmp2 = rotors.get(order.get(i)).length() - 1;
            offset.set(i, tmp2);
        }
        // convert cipher to number between 0 ~ 32
        int output = alphabets.indexOf(cipher);
        // convert number to binary, get c0, c1, c2, c3, c4
        int c4 = output % 2;
        output = output / 2;
        int c3 = output % 2;
        output = output / 2;
        int c2 = output % 2;
        output = output / 2;
        int c1 = output % 2;
        output = output / 2;
        int c0 = output % 2;

        int b0 = rotors.get(order.get(0)).charAt(offset.get(0)) - '0';
        int b1 = rotors.get(order.get(1)).charAt(offset.get(1)) - '0';
        int b2 = rotors.get(order.get(2)).charAt(offset.get(2)) - '0';
        int b3 = rotors.get(order.get(3)).charAt(offset.get(3)) - '0';
        int b4 = rotors.get(order.get(4)).charAt(offset.get(4)) - '0';
        int b5 = rotors.get(order.get(5)).charAt(offset.get(5)) - '0';
        int b6 = rotors.get(order.get(6)).charAt(offset.get(6)) - '0';
        int b7 = rotors.get(order.get(7)).charAt(offset.get(7)) - '0';
        int b8 = rotors.get(order.get(8)).charAt(offset.get(8)) - '0';
        int b9 = rotors.get(order.get(9)).charAt(offset.get(9)) - '0';

        int tmp = 0;
        if (b9 == 1) {
            tmp = c3;
            c3 = c4;
            c4 = tmp;
        }
        if (b8 == 1){
            tmp = c2;
            c2 = c3;
            c3 = tmp;
        }
        if (b7 == 1){
            tmp = c1;
            c1 = c2;
            c2 = tmp;
        }
        if (b6 == 1){
            tmp = c0;
            c0 = c1;
            c1 = tmp;
        }
        if (b5 == 1){
            tmp = c0;
            c0 = c4;
            c4 = tmp;
        }
        c0 ^= b0;
        c1 ^= b1;
        c2 ^= b2;
        c3 ^= b3;
        c4 ^= b4;

        int c_total = c0 * 16 + c1 * 8 + c2 * 4 + c3 * 2 + c4;
        char plain = alphabets.charAt(c_total);
        return plain;
    }
}

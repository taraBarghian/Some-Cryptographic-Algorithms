import java.util.Scanner;

public class Q1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String k = scanner.next();
        String p = scanner.next();


        AES enc = new AES();
        System.out.println(enc.encrypt(p, k));

//        String hex ;
//        long b=Integer.parseInt("01111010101000000000011111100000000", 2);
//        hex = Long.toString(b,16);
//        System.out.println(hex);

    }


}

class AES {
    // some function to convert data-types
    public static byte[] stringToByteArray(String binaryString) {
        byte[] output = new byte[16];
        for (int i = 0; i < 16; i++) {
            String part = binaryString.substring(i * 2, (i + 1) * 2);
            output[i] = (byte) Integer.parseInt(part, 16);
        }
        return output;
    }

    public static byte[] matrixToArray(byte[][] matrix) {
        byte[] array = new byte[16];
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                array[k] = matrix[j][i];
                k++;
            }
        }
        return array;
    }

    public static byte[][] arrayToMatrix(byte[] array) {
        byte[][] hexMatrix = new byte[4][4];
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                hexMatrix[j][i] = array[k];
                k++;
            }
        }
        return hexMatrix;
    }

    public static String arrayToString(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 16; j++) {
            Byte b = array[j];
            sb.append(byteToString(b));
        }
        return sb.toString();
    }

    public static byte[] copyByteArray(byte[] toCopy) {
        byte[] output = new byte[toCopy.length];
        for (int i = 0; i < toCopy.length; i++) {
            output[i] = toCopy[i];
        }
        return output;
    }

    public static byte[][] copyByteMatrix(byte[][] toCopy) {
        byte[][] output = new byte[toCopy.length][toCopy[0].length];
        for (int i = 0; i < toCopy.length; i++) {
            for (int j = 0; j < toCopy[0].length; j++)
                output[i][j] = toCopy[i][j];
        }
        return output;
    }

    public static String matrixToString(byte[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) { // i = row #
            for (int j = 0; j < 4; j++) { // j = column #
                Byte b = matrix[j][i];
                sb.append(byteToString(b));

            }

        }
        return sb.toString();
    }

    public static String byteToString(byte b) {
        String value = Integer.toBinaryString(Byte.toUnsignedInt(b));
        int missingZeros = 8 - value.length();
        for (int i = 0; i < missingZeros; i++) {
            value = "0" + value;
        }
        return value;
    }

    public static String binaryToHex(String binary) {
        StringBuilder hex = new StringBuilder();

        for (int i = 0; i < binary.length() / 4; i++) {
            int b = Integer.parseInt(binary.substring(i * 4, (i + 1) * 4), 2);
            hex.append(Integer.toString(b, 16));
        }
        // int b=Integer.parseInt(binary, 2);
        //hex = Integer.toString(b,16);

        return hex.toString();
    }

    public String encrypt(String plainText, String key) {

        byte[] plainTextByteArray = stringToByteArray(plainText);
        byte[] keyBytes = stringToByteArray(key);

        byte[][][] encrypted = encryptByteArray(plainTextByteArray, keyBytes);
        String encryptedText[] = new String[11];
        for (int round = 0; round < 11; round++) {
            encryptedText[round] = matrixToString(encrypted[round]);
        }

        String cipherText = arrayToString(matrixToArray(encrypted[10]));
        return binaryToHex(cipherText);
    }




    public byte[][][] encryptByteArray(byte[] inputArray, byte[] key) {
        byte[][] input = arrayToMatrix(inputArray);


        AddRoundKey roundKeyAdder = new AddRoundKey(copyByteArray(key));

        SubstituteBytes byteSubstituter = new SubstituteBytes();
        ShiftRows rowShifter = new ShiftRows();
        MixColumns columnMixer = new MixColumns();

        byte[][][] output = new byte[11][input.length][input[0].length];

        output[0] = copyByteMatrix(roundKeyAdder.addRoundKey(input, 0));


        for (int round = 1; round < 11; round++) {
            byte[][] tmp;
            //= new byte[input.length][input[0].length]
            tmp = copyByteMatrix(output[round - 1]);
            tmp = byteSubstituter.substituteBytes(tmp);
            tmp = rowShifter.shiftRows(tmp);

            if (round != 10)
                tmp = columnMixer.mixColumns(tmp);

            tmp = copyByteMatrix(roundKeyAdder.addRoundKey(tmp, round));
            output[round] = copyByteMatrix(tmp);
        }

        return output;
    }


}


class MixColumns {

    private static byte[] gal = {0x03, 0x01, 0x01, 0x02};

    public byte[][] mixColumns(byte[][] input) {
        int[] temp = new int[4];
        for (int i = 0; i < 4; i++) {
            temp[0] = multiply(gal[3], input[0][i]) ^ multiply(gal[0], input[1][i])
                    ^ multiply(gal[1], input[2][i]) ^ multiply(gal[2], input[3][i]);
            temp[1] = multiply(gal[2], input[0][i]) ^ multiply(gal[3], input[1][i])
                    ^ multiply(gal[0], input[2][i]) ^ multiply(gal[1], input[3][i]);
            temp[2] = multiply(gal[1], input[0][i]) ^ multiply(gal[2], input[1][i])
                    ^ multiply(gal[3], input[2][i]) ^ multiply(gal[0], input[3][i]);
            temp[3] = multiply(gal[0], input[0][i]) ^ multiply(gal[1], input[1][i])
                    ^ multiply(gal[2], input[2][i]) ^ multiply(gal[3], input[3][i]);
            for (int j = 0; j < 4; j++)
                input[j][i] = (byte) (temp[j]);
        }
        return input;
    }

    private byte multiply(byte a, byte b) {
        byte returnValue = 0;
        byte temp = 0;
        while (a != 0) {
            if ((a & 1) != 0)
                returnValue = (byte) (returnValue ^ b);
            temp = (byte) (b & 0x80);
            b = (byte) (b << 1);
            if (temp != 0)
                b = (byte) (b ^ 0x1b);
            a = (byte) ((a & 0xff) >> 1);
        }
        return returnValue;
    }


}

class ShiftRows {

    public byte[][] shiftRows(byte[][] input) {
        byte[] temp = new byte[4];
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                temp[j] = input[i][(j + i) % 4];

            }
            for (int j = 0; j < 4; j++)
                input[i][j] = temp[j];
        }
        return input;

    }
}

class SubstituteBytes {

    //sbox from wikipedia

    public static final char[] sbox = {
            0x63, 0x7c, 0x77, 0x7b, 0xf2,
            0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
            0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2,
            0xaf, 0x9c, 0xa4, 0x72, 0xc0, 0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f,
            0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15, 0x04,
            0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2,
            0xeb, 0x27, 0xb2, 0x75, 0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a,
            0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84, 0x53, 0xd1,
            0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a,
            0x4c, 0x58, 0xcf, 0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85,
            0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8, 0x51, 0xa3, 0x40,
            0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff,
            0xf3, 0xd2, 0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4,
            0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73, 0x60, 0x81, 0x4f, 0xdc,
            0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b,
            0xdb, 0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3,
            0xac, 0x62, 0x91, 0x95, 0xe4, 0x79, 0xe7, 0xc8, 0x37, 0x6d, 0x8d,
            0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,
            0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74,
            0x1f, 0x4b, 0xbd, 0x8b, 0x8a, 0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03,
            0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e, 0xe1,
            0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9,
            0xce, 0x55, 0x28, 0xdf, 0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42,
            0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16
    };

    public byte[][] substituteBytes(byte[][] input) {
        char[] matrix = sbox;

        byte[][] output = input;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                output[i][j] = (byte) matrix[input[i][j] & 0xFF];
        return output;
    }
}

class AddRoundKey {

    private final byte[][] subkeys;

    public AddRoundKey(byte[] key) {
        byte[][] temp = generateSubkeys(key);
        subkeys = new byte[temp.length][temp[0].length];
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++)
                subkeys[i][j] = temp[i][j];
        }
    }

    private static byte[][] generateSubkeys(byte[] key) {
        final byte[][] _subkeys = new byte[11][16];
        _subkeys[0] = key;

        byte[] keys = new byte[176];

        // set first 16 bytes
        for (int i = 0; i < 16; i++) {
            keys[i] = key[i];
        }

        int round = 1;
        int i = 16; // first 16 bytes already set
        byte[] temp = new byte[4];

        while (i < 176) { // need 176 bytes of keys

            // set temp bytes
            for (int j = 0; j < 4; j++) {
                temp[j] = keys[j + i - 4];
            }

            // if new key, do main calculations
            if (i % 16 == 0) {

                // Rotate tempBytes
                temp = rotate(temp);

                // Apply S-box
                for (int j = 0; j < 4; j++) {
                    // We always want the encryption sbox. for decryption we use
                    // the same keys, but in inverse order.
                    temp[j] = applySbox(temp[j]);
                }

                // XOR 1st bit of temp with round constant
                temp[0] ^= rcon(round);
                round++;
            }

            byte newValue = 0;

            // assign temp bytes to keys
            for (int j = 0; j < 4; j++) {

                // set new value
                newValue = (byte) (temp[j] ^ keys[i - 16]);

                // store new value
                keys[i] = newValue;

                i++;
            }
        }

        // convert keys into subkeys arrays
        int total = 16;

        for (int rnd = 1; rnd < 11; rnd++) {
            final byte[] newRoundKey = new byte[16];

            // set new key value
            for (int j = 0; j < 16; j++) {
                newRoundKey[j] = keys[total];
                total++;
            }

            // set subkeys to new key value

            //_subkeys[rnd] = new byte[newRoundKey.length];
            for (int x = 0; x < newRoundKey.length; x++) {
                _subkeys[rnd][x] = newRoundKey[x];
            }


        }
        return _subkeys;
    }

    private static byte applySbox(byte input) {
        char[] matrix = SubstituteBytes.sbox;
        return (byte) matrix[input & 0xFF];
    }

    private static int rcon(int input) {
        int x = 1;
        // if input is 0, return 0
        if (input == 0) {
            x = 0;
        } else {
            // until input = 1, multiply x by 2
            while (input != 1) {
                x = multiply((byte) x, (byte) 2);
                input--;
            }
        }
        return x;
    }

    // Multiplies two bytes in garlois field 2^8
    private static byte multiply(byte a, byte b) {
        byte returnValue = 0;
        byte temp = 0;
        while (a != 0) {
            if ((a & 1) != 0)
                returnValue = (byte) (returnValue ^ b);
            temp = (byte) (b & 0x80);
            b = (byte) (b << 1);
            if (temp != 0)
                b = (byte) (b ^ 0x1b);
            a = (byte) ((a & 0xff) >> 1);
        }
        return returnValue;
    }

    public byte[][] addRoundKey(byte[][] matrix, int round) {
        // String action = "Encrypt";

        byte[] key = subkeys[round];
        // System.out.println(action + " with key: "
        // + Helper.matrixToHexString(Helper.arrayToMatrix(key))
        // + " in round: " + round);
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                matrix[j][i] = (byte) (matrix[j][i] ^ key[i * 4 + j]);
        return matrix;
    }

    private static byte[] rotate(byte[] input) {
        byte[] output = new byte[input.length];
        byte a = input[0];
        for (int i = 0; i < 3; i++)
            output[i] = input[i + 1];
        output[3] = a;
        return output;
    }

}


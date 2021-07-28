import java.math.BigInteger;
import java.util.Scanner;

public class DH {

    static BigInteger p, g, a, b;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        p = scanner.nextBigInteger();
        g = scanner.nextBigInteger();
        a = scanner.nextBigInteger();
        b = scanner.nextBigInteger();


        BigInteger A = g.modPow(a,p);
        System.out.print(A+" ");
        BigInteger B = g.modPow(b,p);
        System.out.print(B+" ");

        BigInteger X = A.modPow(b,p);
        System.out.print(X);


    }


}


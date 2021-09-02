import java.io.IOException;
import java.util.Scanner;

/**
 * The entry point to run the compiler.
 * @author : Ishanu Dhar (ID: 1222326326, idhar@asu.edu)
 * @author : Pritam De (ID: 1219491988, pritamde@asu.edu)
 * @see {@link Compiler}
 */

public class Main {

    public static void main(String args[]) {
        Compiler cs = new Compiler();
        Scanner sc= new Scanner(System.in);
        System.out.println("Enter the absolute filepath (ex- C:\\filename.txt):");
        String filePath = sc.nextLine();
        try {
            cs.readTextFile(filePath);
            cs.splitWords();
            cs.translate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}

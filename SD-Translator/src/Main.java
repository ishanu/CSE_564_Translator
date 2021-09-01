import java.io.IOException;

public class Main {
    public static void main(String args[]) {
        String text = null;
        Compiler cs = new Compiler();
        try {
            text = cs.readTextFile("C:\\Users\\ishan\\IdeaProjects\\SD-Translator\\src\\input1.txt");
            cs.splitWords(text);
            cs.translate();
            System.out.println("successfully done");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}

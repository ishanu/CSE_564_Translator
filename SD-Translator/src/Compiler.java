import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class provides APIs to parse a file, create tokens and then translate them.
 * The APIs may throw exceptions in case of any discrepancies like parsing errors or  syntax error.
 *
 * @author : Ishanu Dhar (ID-1222326326, idhar@asu.edu)
 * @author : Pritam De (ID: 1219491988, pritamde@asu.edu)
 * @see {@link Token}
 */
public class Compiler {
    List<Token> tokens = new ArrayList<>();
    Stack<String> storeDelimiters = new Stack<>();
    String fileContent = "";
    int index;

    /**
     * This method takes a file path as an argument and returns a string containing the file content
     * @param path: contains the absolute path of the file
     * @throws IOException
     */
    public void readTextFile(String path) throws IOException {
        File file = new File(path);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String str;
            while ((str = br.readLine()) != null) {
                fileContent = fileContent + str + "$";
            }
        } catch (Exception exc) {
            System.out.println("Error occurred while extracting string from file:" + exc.getMessage());
        } finally {
            br.close();
        }
    }

    /**
     * this method creates the tokens from the string containing the contents of the file.
     * The tokens can be method names, keywords, simple instructions, braces and parentheses
     */
    public void splitWords() {
        String token = "";
        int i = 0;
        while (i < fileContent.length()) {
            char c = fileContent.charAt(i);
            if ((c + "").equals("(")) {
                tokens.add(new Token("(","delimiter"));
                i++;
            } else if ((c + "").equals(")")) {
                tokens.add(new Token(")", "delimiter"));
                i++;
            } else if ((c + "").equals("{")) {
                tokens.add(new Token("{", "delimiter"));
                i++;
            } else if ((c + "").equals("}")) {
                tokens.add(new Token("}", "delimiter"));
                i++;
            } else {
                if (Character.isLetter(c)) {
                    token = createWord(fileContent, i, c);
                    i = i + token.length();
                    tokens.add(new Token(token, "word"));
                } else {
                    i++;
                }
            }
        }
    }

    /**
     * This method first checks if the Tokens are syntactically correct. If they are correct then they are translated.
     * @throws Exception
     */
    public void translate() throws Exception {
        index = 0;
        while (index < tokens.size()) {
            checkSyntax();
        }
        if(!storeDelimiters.isEmpty()) {
            throw new Exception("Syntax Error - 1");
        }
        printTranslation();
    }

    /**
     * This is a util method consumed by translator method to check if the tokens are in order or syntactically correct.
     * Will throw exception otherwise.
     * @throws Exception
     */
    public void checkSyntax() throws Exception {
        if (checkIf()) {
            storeDelimiters.add("{");
            index = index + 4;
            checkSyntax();
        } else if (checkWhile()) {
            storeDelimiters.add("{");
            index = index + 4;
            checkSyntax();
        } else if (checkMethod()) {
            storeDelimiters.add("{");
            index = index + 4;
            checkSyntax();

        } else if (checkWord()) {
            index = index + 1;

        } else if (tokens.get(index).getData().equals("}")) {
            index++;
            if (storeDelimiters.isEmpty()) {
                throw new Exception("Syntax Error - 3");
            } else {
                if (storeDelimiters.peek().equals("{")) {
                    storeDelimiters.pop();
                }
            }
            } else {
                throw new Exception();
            }
    }

    /**
     * This method prints the final translation
     */
    public void printTranslation() {
        index = 0;
        Stack<String> storeBraces = new Stack<>();
        String translatedString = "";
        while (index < tokens.size()) {
             if (checkIf()) {
                 storeBraces.add("<");
                 translatedString  += "< ";
                 index = index + 4;
            } else if (checkWhile()) {
                 storeBraces.add("(");
                 translatedString  += "( ";
                 index = index + 4;
            } else if (checkMethod()) {
                 storeBraces.add("[");
                 translatedString  += "[ ";
                 index = index + 4;
             } else if (checkWord()) {
                 translatedString += "- ";
                 index = index + 1;
            }  else if (tokens.get(index).getData().equals("}")) {
                 translatedString = matchDelimiters(storeBraces, translatedString);
                 index ++;
            }
        }
        System.out.println(translatedString);
    }

    /**
     * This is a util method which parses the text file to create word token
     * @param text - the string that needs to be parsed
     * @param i - index from which the parsing begins
     * @param c - the character that needs to be checked
     * @return - a fully formed word token
     */
    public String createWord(String text, int i, char c) {
        int j = i;
        String token = "";
        while (Character.isLetter(c)) {
            token += c;
            j++;
            c = text.charAt(j);
        }
        return token;
    }

    /**
     * This method checks if the token is an If type
     * @return - a boolean
     */
    public boolean checkIf() {
        return tokens.get(index).getData().equalsIgnoreCase("if")
                && tokens.get(index + 1).getData().equals("(")
                && tokens.get(index + 2).getData().equals(")")
                && tokens.get(index + 3).getData().equals("{");
    }

    /**
     * This method checks if the token is a While type
     * @return a booelean
     */
    public boolean checkWhile() {
        return tokens.get(index).getData().equalsIgnoreCase("while")
                && tokens.get(index + 1).getData().equals("(")
                && tokens.get(index + 2).getData().equals(")")
                && tokens.get(index + 3).getData().equals("{");
    }

    /**
     * This method checks if the token is a Method type
     * @return a boolean
     */
    public boolean checkMethod() {
        return tokens.get(index).getType().equals("word")
                && tokens.get(index + 1).getData().equals("(")
                && tokens.get(index + 2).getData().equals(")")
                && tokens.get(index + 3).getData().equals("{");
    }

    /**
     * This method checks if the token is an Instruction type
     * @return a boolean
     */
    public boolean checkWord() {
        return tokens.get(index).getType().equals("word");
    }

    /**
     * This method matches the delimiters in order to print the correct translation
     * @param storeDelimiters - takes a stack of delimiters to match the closing delimiters
     * @param translatedString - translated string that needs to be printed
     * @return translated string
     */
    public static String matchDelimiters(Stack<String> storeDelimiters, String translatedString) {
        switch (storeDelimiters.pop()) {
            case "<": translatedString += "> ";
                break;
            case "(": translatedString += ") ";
                break;
            case "[": translatedString += "] ";
                break;
        }
        return  translatedString;
    }

}

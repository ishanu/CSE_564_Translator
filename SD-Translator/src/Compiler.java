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
 * @see {@link Type}
 */
public class Compiler {
    List<Token> tokens = new ArrayList<>();
    Stack<String> braceChecker = new Stack<>();
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
                tokens.add(new Token("(", Type.OPEN_PAREN));
                i++;
            } else if ((c + "").equals(")")) {
                tokens.add(new Token(")", Type.CLOSE_PAREN));
                i++;
            } else if ((c + "").equals("{")) {
                tokens.add(new Token("{", Type.OPEN_CURLY));
                i++;
            } else if ((c + "").equals("}")) {
                tokens.add(new Token("}", Type.CLOSE_CURLY));
                i++;
            } else {
                if (Character.isLetter(c)) {
                    token = createWord(fileContent, i, c);
                    i = i + token.length();
                    tokens.add(new Token(token, Type.WORD));
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
        if(!braceChecker.isEmpty()) {
            throw new Exception("Syntax Error - 1");
        }
        printTranslation();
    }






    private void checkSyntax() throws Exception {
        if (checkIf()) {
            braceChecker.add("{");
            index = index + 4;
            checkSyntax();
        } else if (checkWhile()) {
            braceChecker.add("{");
            index = index + 4;
            checkSyntax();
        } else if (checkMethod()) {
            braceChecker.add("{");
            index = index + 4;
            checkSyntax();
            return;
        } else if (checkWord()) {
            index = index + 1;
            return;
        } else if (tokens.get(index).getData().equals("}")) {
            index++;
            if (braceChecker.isEmpty()) {
                throw new Exception("Syntax Error - 3");
            } else {
                if (braceChecker.peek().equals("{")) {
                    braceChecker.pop();
                }
            }
            return;
        } else {
            throw new Exception();
        }
    }


    private void printTranslation() {
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
                translatedString = checkBraces(storeBraces, translatedString);
                index ++;
            }

        }
        System.out.println(translatedString);
    }




    private String createWord(String text, int i, char c) {
        int j = i;
        String token = "";
        while (Character.isLetter(c)) {
            token += c;
            j++;
            c = text.charAt(j);
        }
        return token;
    }

    private boolean checkIf() {
        return tokens.get(index).getData().equalsIgnoreCase("if")
                && tokens.get(index + 1).getData().equals("(")
                && tokens.get(index + 2).getData().equals(")")
                && tokens.get(index + 3).getData().equals("{");
    }

    private boolean checkWhile() {
        return tokens.get(index).getData().equalsIgnoreCase("while")
                && tokens.get(index + 1).getData().equals("(")
                && tokens.get(index + 2).getData().equals(")")
                && tokens.get(index + 3).getData().equals("{");
    }

    private boolean checkMethod() {
        return tokens.get(index).getType().equals(Type.WORD)
                && tokens.get(index + 1).getData().equals("(")
                && tokens.get(index + 2).getData().equals(")")
                && tokens.get(index + 3).getData().equals("{");
    }

    private boolean checkWord() {
        return tokens.get(index).getType().equals(Type.WORD);
    }

    private String checkBraces(Stack<String> storeBraces, String translatedString) {
        switch (storeBraces.pop()) {
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 */
public class Compiler {
    List<Token> tokens = new ArrayList<>();
    Stack<String> braceChecker = new Stack<>();
    int index;

    public String readTextFile(String path) throws IOException {
        File file = new File(path);
        BufferedReader br = null;
        String text = "";
        try {
            br = new BufferedReader(new FileReader(file));
            String str;
            while ((str = br.readLine()) != null) {
                text = text + str + "$";
            }
            return text;
        } catch (Exception exc) {
            System.out.println("Error occurred while extracting string from file:" + exc.getMessage());
            return "";
        } finally {
            br.close();
        }
    }

    public void splitWords(String text) {
        String token = "";
        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);
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
                    token = createWord(text, i, c);
                    i = i + token.length();
                    tokens.add(new Token(token, Type.WORD));
                } else {
                    i++;
                }
            }
        }

    }

    public void translate() throws Exception {
        index = 0;
        while (index < tokens.size()) {
            checkSyntax();
        }
        if(!braceChecker.isEmpty()) {
            throw new Exception("Syntax Error - 1");
        }
        print();
    }

    public void checkSyntax() throws Exception {
        if (tokens.get(index).getData().equalsIgnoreCase("if")
                && tokens.get(index + 1).getData().equals("(")
                && tokens.get(index + 2).getData().equals(")")
                && tokens.get(index + 3).getData().equals("{")) {
            braceChecker.add("{");
            index = index + 4;
            checkSyntax();
        } else if (tokens.get(index).getData().equalsIgnoreCase("while")
                && tokens.get(index + 1).getData().equals("(")
                && tokens.get(index + 2).getData().equals(")")
                && tokens.get(index + 3).getData().equals("{")) {
            braceChecker.add("{");
            index = index + 4;
            checkSyntax();
        } else if (tokens.get(index).getType().equals(Type.WORD)
                && tokens.get(index + 1).getData().equals("(")
                && tokens.get(index + 2).getData().equals(")")
                && tokens.get(index + 3).getData().equals("{")) {
            braceChecker.add("{");
            index = index + 4;
            checkSyntax();
            return;
        } else if (tokens.get(index).getType().equals(Type.WORD)) {
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


    private void print() {
        index = 0;
        Stack<String> storeBraces = new Stack<>();
        String translatedString = "";
        while (index < tokens.size()) {
             if (tokens.get(index).getData().equalsIgnoreCase("if")
                    && tokens.get(index + 1).getData().equals("(")
                    && tokens.get(index + 2).getData().equals(")")
                    && tokens.get(index + 3).getData().equals("{")) {
                storeBraces.add("<");
                translatedString  += "< ";
                index = index + 4;
            } else if (tokens.get(index).getData().equalsIgnoreCase("while")
                    && tokens.get(index + 1).getData().equals("(")
                    && tokens.get(index + 2).getData().equals(")")
                    && tokens.get(index + 3).getData().equals("{")) {
                storeBraces.add("(");
                translatedString  += "( ";
                index = index + 4;
            } else if (tokens.get(index).getType().equals(Type.WORD)
                     && tokens.get(index + 1).getData().equals("(")
                     && tokens.get(index + 2).getData().equals(")")
                     && tokens.get(index + 3).getData().equals("{")) {
                 storeBraces.add("[");
                 translatedString  += "[ ";
                 index = index + 4;
             } else if (tokens.get(index).getType().equals(Type.WORD)) {
                translatedString += "- ";
                index = index + 1;
            }  else if (tokens.get(index).getData().equals("}")) {
                switch (storeBraces.pop()) {
                    case "<": translatedString += "> ";
                        break;
                    case "(": translatedString += ") ";
                        break;
                    case "[": translatedString += "] ";
                        break;

                }
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
}

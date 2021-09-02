/**
 * Data model to hold the tokens parsed from the file.
 * @author : Ishanu Dhar (ID-1222326326, idhar@asu.edu)
 * @author : Pritam De (ID: 1219491988, pritamde@asu.edu)
 */
public class Token {
    private String data;
    private String type;

    public Token(String data, String type) {
        this.data = data;
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public String getType() {
        return type;
    }
}

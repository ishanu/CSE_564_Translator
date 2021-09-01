/**
 * Data model to hold the tokens parsed from the file.
 * @author : Ishanu Dhar (ID-1222326326, idhar@asu.edu)
 * @author : Pritam De (ID: 1219491988, pritamde@asu.edu)
 * @see {@link Type}
 */
public class Token {
    private String data;
    private Type type;

    public Token(String data, Type type) {
        this.data = data;
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

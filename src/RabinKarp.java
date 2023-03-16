public class RabinKarp {

    private final int CONST = 10;
    private int previousHash;
    private int hashFirst;
    private int length;

    //mb static
    protected int hashing(String word) {
        // H = ci*A^(l-i), l -> length

        word = word.toLowerCase();


        length = word.length();
        int code = 0;

        for (int i = 0; i < length; i++) {
            code += word.charAt(i) * Math.pow(CONST, (length - i - 1));
        }
        previousHash = code;
        hashFirst = (int) (word.charAt(0) * Math.pow(CONST, (length - 1)));

        return code;
    }

    protected int rollingHash(String word, char addChar) {

        word = word.toLowerCase();
        int code = (previousHash - hashFirst) * CONST + addChar;
        previousHash = code;
        hashFirst = (int) (word.charAt(0) * Math.pow(CONST, (length - 1)));

        return code;
    }
    public void setHashFirst(int hashFirst) {
        this.hashFirst = hashFirst;
    }

}

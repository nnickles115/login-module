package login;

/**
 * COP 4078 Exercise: 5
 * File Name: Cryptographer.java
 * 
 * The Cryptographer class uses Vigenere encryption to mask
 * the username and password of each user in the database.
 * Also provides decryption functions to return the original
 * input if needed.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @apiNote Added in version 1.2.
 * @apiNote Moved around variables, modified comments in verison 1.4.
 */
public class Cryptographer {
    //#region CONSTANTS
    private final String ALPHAKEY = "ARGOSROCK";
    private final String NUMBERKEY = "1963"; 
    private final int[][] VIGENERE_TABLE;
    //#endregion CONSTANTS

    //#region SINGLETON PATTERN
    private static Cryptographer _instance = null;
    
    private Cryptographer() {
        VIGENERE_TABLE = GenerateTable();
    }

    public static Cryptographer GetInstance() {
        if(_instance == null) {
            _instance = new Cryptographer();
        }
        return _instance;
    }
    //#endregion SINGLETON PATTERN

    //#region GETTERS
    public String GetAlphakey()  { return ALPHAKEY;  }
    public String GetNumberkey() { return NUMBERKEY; }
    //#endregion GETTERS

    //#region FUNCTIONS
    /**
     * Generates the Vigenere table from the exercise 3 instructions.
     * 
     * @return Generated Vingenre table.
     */
    private int[][] GenerateTable() {
        int[][] table = new int[10][10];
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                table[i][j] = (10 + j - i) % 10;
            }
        }
        return table;
    }
    //#endregion FUNCTIONS

    //#region HELPER FUNCTIONS
    /**
     * Helper function to encrypt a single digit based on the Vigenere table.
     * 
     * @param plaintextDigit Digit from user input.
     * @param keyDigit Digit in associated place value from key.
     * @return The ecrypted digit.
     */
    private int EncryptDigit(int plaintextDigit, int keyDigit) {
        return VIGENERE_TABLE[plaintextDigit][keyDigit];
    }

    /**
     * Reverse looks up the row where the keyDigit corresponds to the ciphertextDigit
     * to return the original digit.
     * 
     * @param ciphertextDigit Encrypted digit from database. 
     * @param keyDigit Digit in associated place value from key.
     * @return The decrypted digit.
     */
    private int DecryptDigit(int ciphertextDigit, int keyDigit) {
        for(int i = 0; i < 10; i++) {
            if(VIGENERE_TABLE[i][keyDigit] == ciphertextDigit) {
                return i; // Return the index which is the plaintext digit.
            }
        }
        return -1; // Error case, should never get here.
    }

    /**
     * Helper method to encrypt uppercase or lowercase chars 
     * by shifting right by the provided key.
     * 
     * @param charToEncrypt Char from user input.
     * @param keyChar Char in associated place value from key.
     * @return The encrypted char.
     */
    private char EncryptChar(char charToEncrypt, char keyChar) {
        if(Character.isUpperCase(charToEncrypt)) {
            return (char)((charToEncrypt - 'A' + (keyChar - 'A')) % 26 + 'A');
        }
        return (char)((charToEncrypt - 'a' + (keyChar - 'A')) % 26 + 'a');
    }

    /**
     * Helper method to decrypt uppercase or lowercase chars 
     * by shifting left by provided key.
     * 
     * @param charToDecrypt Encrypted char from database.
     * @param keyChar Char in associated place value from key.
     * @return The decrypted char.
     */
    private char DecryptChar(char charToDecrypt, char keyChar) {
        if(Character.isUpperCase(charToDecrypt)) {
            return (char)((charToDecrypt - 'A' - (keyChar - 'A') + 26) % 26 + 'A');
        }
        return (char)((charToDecrypt - 'a' - (keyChar - 'A') + 26) % 26 + 'a');
    }
    //#endregion HELPER FUNCTIONS

    //#region ENCRYPTION FUNCTIONS
    /**
     * Encrypts the password by shifting letters with the alphakey and numbers with the numberkey.
     * 
     * @param plaintext The user-entered password.
     * @param alphakey The key used to shift alphabetic characters.
     * @param numberkey The key used to shift numeric characters.
     * @return The encrypted password.
     */
    public String EncryptVigenere(String plaintext) {
        StringBuilder encryptedPassword = new StringBuilder();
        int alphakeyIndex = 0;
        int numkeyIndex = 0;

        for(int i = 0; i < plaintext.length(); i++) {
            char currentChar = plaintext.charAt(i);

            if(Character.isLetter(currentChar)) {
                // Encrypt letters using the alphakey.
                char encryptedChar = EncryptChar(
                    currentChar, 
                    ALPHAKEY.charAt(alphakeyIndex % ALPHAKEY.length())
                );
                //if(Config.DEBUG) System.out.println("Encryption: " + currentChar + " -> " + encryptedChar);
                encryptedPassword.append(encryptedChar);
                alphakeyIndex++;
            } 
            else if(Character.isDigit(currentChar)) {
                // Encrypt digits using the numberkey.
                int encryptedDigit = EncryptDigit(
                    Character.getNumericValue(currentChar), 
                    Character.getNumericValue(NUMBERKEY.charAt(numkeyIndex % NUMBERKEY.length()))
                );
                //if(Config.DEBUG) System.out.println("Encryption: " + currentChar + " -> " + encryptedDigit);
                encryptedPassword.append(encryptedDigit);
                numkeyIndex++;
            }
        }
        return encryptedPassword.toString();
    }

    /**
     * Decrypts the password by shifting letters with the alphakey and numbers with the numberkey.
     * 
     * @param encryptedPassword The encrypted password to decrypt.
     * @param alphakey The key used to shift alphabetic characters.
     * @param numberkey The key used to shift numeric characters.
     * @return The decrypted password.
     */
    public String DecryptVigenere(String encryptedPassword) {
        StringBuilder decryptedPassword = new StringBuilder();
        int alphakeyIndex = 0;
        int numkeyIndex = 0;
    
        for(int i = 0; i < encryptedPassword.length(); i++) {
            char currentChar = encryptedPassword.charAt(i);

            if(Character.isLetter(currentChar)) {
                // Decrypt letters using the alphakey.
                char decryptedChar = DecryptChar(
                    currentChar, 
                    ALPHAKEY.charAt(alphakeyIndex % ALPHAKEY.length())
                );
                //if(Config.DEBUG) System.out.println("Decryption: " + currentChar + " -> " + decryptedChar);
                decryptedPassword.append(decryptedChar);
                alphakeyIndex++;
            } 
            else if(Character.isDigit(currentChar)) {
                // Decrypt digits using the numberkey.
                int decryptedDigit = DecryptDigit(
                    Character.getNumericValue(currentChar), 
                    Character.getNumericValue(NUMBERKEY.charAt(numkeyIndex % NUMBERKEY.length()))
                );
                //if(Config.DEBUG) System.out.println("Decryption: " + currentChar + " -> " + decryptedDigit);
                decryptedPassword.append(decryptedDigit);
                numkeyIndex++;
            }
        }
        return decryptedPassword.toString();
    }
    //#endregion ENCRYPTION FUNCTIONS
}
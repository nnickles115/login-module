package login;

//#region IMPORTS
import java.security.SecureRandom;
//#endregion IMPORTS

/**
 * COP 4078 Exercise: 5
 * File Name: DefaultPassword.java
 * 
 * The DefaultPassword class is used to generate a random default password
 * for a User if too many manual attempts to set the new password have failed.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @apiNote Added in version 1.3.
 * @apiNote Rewrote CreateDefaultPassword function and moved to 
 * PasswordHandler class in version 1.4.
 * Updated minor logic and code formattting.
 */
public class DefaultPassword {
    //#region CONSTANTS
    private final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private final String DIGITS    = "0123456789";
    private final String ALPHA_NUM = UPPERCASE + LOWERCASE + DIGITS;
    private final int MIN_PASSWORD_LENGTH = 8;
    private final int MAX_PASSWORD_LENGTH = 12;
    //#endregion CONSTANTS

    //#region SERVICES
    private final SecureRandom random;
    //#endregion SERVICES

    //#region SINGLETON PATTERN
    private static DefaultPassword _instance = null;

    private DefaultPassword() {
        random = new SecureRandom();
    }

    public static DefaultPassword GetInstance() {
        if(_instance == null) {
            _instance = new DefaultPassword();
        }
        return _instance;
    }
    //#endregion SINGLETON PATTERN

    //#region FUNCTIONS
    /**
     * Generates a random password and shuffles it for more randomness.
     * 
     * @return Newly generated and shuffled password from {@link #ShufflePassword(String)}.
     * @apiNote Added in verison 1.3.
     * @apiNote Updated in version 1.4 to handle debug message printing.
     */
    public String GeneratePassword() {
        // Pick random length between 8-12 chars (included).
        int passwordLength = random.nextInt(MAX_PASSWORD_LENGTH - MIN_PASSWORD_LENGTH + 1) + MIN_PASSWORD_LENGTH;

        StringBuilder password = new StringBuilder(passwordLength);

        // Ensure the password contains at least one character from each required pool.
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));

        // Fill the rest of the password with random characters from the combined pool.
        for(int i = 3; i < passwordLength; i++) {
            password.append(ALPHA_NUM.charAt(random.nextInt(ALPHA_NUM.length())));
        }

        // Shuffle the password to randomize the positions of characters.
        String suffeledPassword = ShufflePassword(password.toString());

        // Print password since email is not setup.
        if(App.DEBUG) {
            PrintPassword(suffeledPassword);
        }

        return suffeledPassword;
    }

    /**
     * Shuffles around generated password.
     * 
     * @param password Randomly generated password from {@link #GeneratePassword()}.
     * @return Shuffled randomly generated password.
     * @apiNote Added in verison 1.3.
     */
    private String ShufflePassword(String password) {
        char[] newPassword = password.toCharArray();
        for(int i = 0; i < newPassword.length; i++) {
            // Pick random index to move a char to.
            int randomIndex = random.nextInt(newPassword.length);

            // Swap.
            char temp = newPassword[i];
            newPassword[i] = newPassword[randomIndex];
            newPassword[randomIndex] = temp;
        }

        return new String(newPassword);
    }

    /**
     * Debug function for printing the generated password to the console
     * since the program currently doesn't support sending an email
     * with the generated password to the user.
     * 
     * @param newPassword Newly generated password from {@link #GeneratePassword()}
     * @apiNote Added in version 1.4.
     */
    private void PrintPassword(String newPassword) {
        System.out.println("[DEBUG] Generated Password: " + newPassword);
    }
    //#endregion FUNCTIONS
}
package login;

import login.exception_handlers.DefaultPasswordException;
import login.exception_handlers.PasswordValidationException;

/**
 * COP 4078 Exercise: 5
 * File Name: PasswordHandler.java
 * 
 * This class handles the authentication of the password for a User.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @see Cryptographer
 * @see Database
 * @see DefaultPassword
 * @see Validation
 * @apiNote Added in version 1.4.
 */
public class PasswordHandler {
    //#region SERVICES
    private final Cryptographer _cryptographer = Cryptographer.GetInstance();
    private final Database _database = Database.GetInstance();
    private final DefaultPassword _defaultPassword = DefaultPassword.GetInstance();
    private final Validation _validation = Validation.GetInstance();
    //#endregion SERVICES

    //#region SINGLETON PATTERN
    private static PasswordHandler _instance = null;

    private PasswordHandler() {}

    public static PasswordHandler GetInstance() {
        if(_instance == null) {
            _instance = new PasswordHandler();
        }
        return _instance;
    }
    //#endregion SINGLETON PATTERN

    //#region FUNCTIONS
    /**
     * Authenticates the user input against the stored password in the Database.
     * 
     * @param username Username of the associated User object.
     * @return {@code true} if password exists and matches, {@code false} if otherwise.
     * @see Cryptographer
     * @see Database
     * @see Validation
     * @see User
     * @apiNote Added in version 1.0.
     * @apiNote Updated in version 1.1 to work with new User class and
     * password obfuscation methods. Was called AuthenticateUser.
     * @apiNote Reverted in version 1.2 to use record class getter functions.
     * @apiNote Reverted again in version 1.3 to use normal getter functions.
     * @apiNote Rewrote and moved from UserService class which no longer exists in version 1.4.
     */
    public boolean AuthenticatePassword(String username, char[] passwordChars) {
        User user = _database.GetUserByUsername(username);
        if(user == null) return false;

        String password = new String(passwordChars);
        String encryptedPassword = _cryptographer.EncryptVigenere(password);
        
        // Check if password failed validation checks.
        try {
            _validation.ValidatePassword(password);
        }
        catch(PasswordValidationException e) {
            MessageHandler.PrintMessage(MessageHandler.INCORRECT_INPUT, "Password");
            return false;
        }

        return user.GetPassword().equals(encryptedPassword);
    }

    /**
     * Checks if the password exists or is {@code null} given the
     * username of the user.
     * 
     * @param username Username of the associated User object.
     * @return {@code true} if the password exists, {@code false} if otherwise.
     * @see Database
     * @see User
     * @apiNote Added in version 1.3.
     * @apiNote Rewrote and moved from UserService class which no longer exists in version 1.4.
     */
    public boolean DoesPasswordExist(String username) {
        User user = _database.GetUserByUsername(username);
        return user.GetPassword() != null;
    }

    /**
     * Takes in input from Login class.
     * Validates new password then encrypts and stores it in the related User object.
     * 
     * @param username Username of the User object to associate the password with.
     * @param newPasswordChars User-entered password.
     * @return {@code true} if new password is valid and was set, {@code false} if otherwise.
     * @see Cryptographer
     * @see Database
     * @see User
     * @see Validation
     * @apiNote Added in version 1.3.
     * @apiNote Rewrote and moved from UserService class which no longer exists in version 1.4.
     */
    public boolean CreateNewPassword(String username, char[] newPasswordChars) {
        User user = _database.GetUserByUsername(username);
        if(user == null) return false;

        String newPassword = new String(newPasswordChars);
        try {
            _validation.ValidatePassword(newPassword);
            user.SetPassword(_cryptographer.EncryptVigenere(newPassword));
        }
        catch(PasswordValidationException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Uses the DefaultPassword class to generate a default password for the user.
     * Validates the password and sets it for the User object.
     * Prints confirmation message.
     * 
     * @param username Username of the User object to associate the password with.
     * @see Cryptographer
     * @see Database
     * @see DefaultPassword
     * @see Validation
     * @see User
     * @apiNote Added in version 1.4.
     */
    public void CreateDefaultPassword(String username) throws DefaultPasswordException {
        User user = _database.GetUserByUsername(username);
        String generatedPassword;
        while(true) {
            generatedPassword = _defaultPassword.GeneratePassword();
            try {
                _validation.ValidatePassword(generatedPassword);
                break;
            }
            catch(PasswordValidationException e) {
                // This should never happen...
                throw new DefaultPasswordException("Default password failed to generate.");
            }
        }
        user.SetPassword(_cryptographer.EncryptVigenere(generatedPassword));
        System.out.print(
            """
            The password has been set to a default password.
            You will recieve a secure email containing the password.
            """
        );
    }
    //#endregion FUNCTIONS
}
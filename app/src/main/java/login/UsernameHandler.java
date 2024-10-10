package login;

/**
 * COP 4078 Exercise: 5
 * File Name: UsernameHandler.java
 * 
 * This class handles the authentication of the username for a User.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @see Cryptographer
 * @see Database
 * @see Validation
 * @apiNote Added in version 1.4.
 */
public class UsernameHandler {
    //#region SERVICES
    private final Cryptographer _cryptographer = Cryptographer.GetInstance();
    private final Database _database = Database.GetInstance();
    private final Validation _validation = Validation.GetInstance();
    //#endregion SERVICES

    //#region SINGLETON PATTERN
    private static UsernameHandler _instance = null;

    private UsernameHandler() {}

    public static UsernameHandler GetInstance() {
        if(_instance == null) {
            _instance = new UsernameHandler();
        }
        return _instance;
    }
    //#endregion SINGLETON PATTERN

    //#region FUNCTIONS
    /**
     * Authenticates the user input against the stored username in the Database.
     * 
     * @param username User input from the Login class.
     * @return {@code true} if username exists and matches, {@code false} if otherwise.
     * @see Cryptographer
     * @see Database
     * @see Validation
     * @see User
     * @apiNote Added in version 1.0.
     * @apiNote Updated in version 1.1 to use new getter functions.
     * @apiNote Reverted in version 1.2 to use record class getter functions.
     * @apiNote Reverted again in version 1.3 to use normal getter functions.
     * @apiNote Rewrote logic and moved from UserService class which no longer exists in version 1.4.
     */
    public boolean AuthenticateUsername(String username) {
        // Validate the user input.
        if(!_validation.ValidateUsername(username)) {
            return false;
        }
        
        // Grab user object from the Database.
        User user = _database.GetUserByUsername(username);
        if(user == null) return false;

        // Verify the input matches the User object's username.
        String encryptedUsername = _cryptographer.EncryptVigenere(username);
        return user.GetUsername().equals(encryptedUsername);
    }
    //#endregion FUNCTIONS
}
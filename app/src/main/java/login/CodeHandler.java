package login;

/**
 * COP 4078 Exercise: 5
 * File Name: CodeHandler.java
 * 
 * This class handles the authentication of the MFA Code for a User.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @see Database
 * @see Validation
 * @apiNote Added in version 1.4.
 */
public class CodeHandler {
    //#region SERVICES
    private final Database _database = Database.GetInstance();
    private final Validation _validation = Validation.GetInstance();
    //#endregion SERVICES

    //#region SINGLETON PATTERN
    private static CodeHandler _instance = null;

    private CodeHandler() {}

    public static CodeHandler GetInstance() {
        if(_instance == null) {
            _instance = new CodeHandler();
        }
        return _instance;
    }
    //#endregion SINGLETON PATTERN

    //#region FUNCTIONS
    /**
     * Authenticates the user input against the stored MFA Code in the Database.
     * 
     * @param username Username of the associated User object.
     * @param code User-entered code to validate against the User object's code.
     * @return {@code true} if code exists and is valid, {@code false} otherwise.
     * @see Database
     * @see Validation
     * @see User
     * @apiNote Added in version 1.4.
     */
    public boolean AuthenticateCode(String username, String code) {
        User user = _database.GetUserByUsername(username);
        if(user == null) return false;

        return _validation.ValidateCode(code);
    }
    //#endregion FUNCTIONS
}
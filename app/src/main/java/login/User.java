package login;

/**
 * COP 4078 Exercise: 5
 * File Name: User.java
 * 
 * The User class is a model for a User object in the database. 
 * Users have a username, password, and 10-digit MFA code.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @apiNote Added in version 1.0.
 * @apiNote Changed in version 1.1 from a record class to a normal class
 * in order to gain more control over boilerplate code (getters).
 * @apiNote Reverted to record class in version 1.2 due to new Cryptographer class.
 * @apiNote Reverted to normal class again in version 1.3 due to new DefaultPassword class.
 * @apiNote Renamed variables and functions, and adjusted formatting in version 1.4.
 * @implNote Can take in plaintext variables, but preferred to use Cryptographer class if possible.
 */
public class User {
    //#region VARIABLES
    private final String _username;
    private String _password;
    private final int _code;
    //#endregion VARIABLES

    //#region CONSTRUCTORS
    /**
     * Constructs a User object with the username and MFA Code, sets the password to null.
     * 
     * @param username Username of the User.
     * @param code MFA Code of the User.
     */
    public User(String username, int code) {
        _username = username;
        _password = null;
        _code = code;
    }

    /**
     * Constructs a User object with the username, password, and MFA Code.
     * 
     * @param username Username of the User.
     * @param password Password of the User.
     * @param code MFA Code of the User.
     */
    public User(String username, String password, int code) {
        _username = username;
        _password = password;
        _code = code;
    }
    //#endregion CONSTRUCTORS

    //#region GETTERS
    public String GetUsername() { return _username; }
    public String GetPassword() { return _password; }
    public int GetCode()        { return _code;     }
    //#endregion GETTERS

    //#region SETTERS
    public void SetPassword(String password) { _password = password; }
    //#endregion SETTERS
}
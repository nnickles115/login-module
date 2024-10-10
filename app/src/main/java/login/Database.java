package login;

//#region IMPORTS
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//#endregion IMPORTS

/**
 * COP 4078 Exercise: 5
 * File Name: Database.java
 * 
 * This class handles interaction with the "database".
 * It populates the database, and provides functions for querying.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @see Cryptographer
 * @see User
 * @apiNote Added in version 1.4. UserService class was refactored into this class in version 1.4.
 */
public class Database {
    //#region SERVICES
    private final Cryptographer _cryptographer = Cryptographer.GetInstance();
    //#endregion SERVICES

    //#region CONSTANTS
    /**
     * Stores a list of User objects to function as a database.
     */
    private final List<User> _users;
    //#endregion CONSTANTS

    //#region SINGLETON PATTERN
    private static Database _instance = null;
    
    /**
     * Constructs a new ArrayList to store the Users and populates the database.
     */
    private Database() {
        _users = new ArrayList<>();
        PopulateDatabase();
    }

    public static Database GetInstance() {
        if(_instance == null) {
            _instance = new Database();
        }
        return _instance;
    }
    //#endregion SINGLETON PATTERN

    //#region QUERY FUNCTIONS
    /**
     * Queries the database by username.
     * 
     * @param username Username of the User object to find.
     * @return The found {@code User} in the database, {@code null} if not found.
     * @apiNote Added in version 1.0.
     * @apiNote Updated in version 1.1 to use new getter functions.
     * @apiNote Reverted in version 1.2 to use record class getter functions.
     * Also checks based on encrypted username now.
     * @apiNote Reverted again in version 1.3 to use normal gettter functions.
     */
    public User GetUserByUsername(String username) {
        String encryptedUsername = _cryptographer.EncryptVigenere(username);

        return _users.stream()
                     .filter(user -> user.GetUsername().equals(encryptedUsername))
                     .findFirst()
                     .orElse(null);
    }
    //#endregion QUERY FUNCTIONS

    //#region FUNCTIONS
    /**
     * Populates the database {@link #_users} with provided usernames and MFA codes.
     * 
     * @see Cryptographer
     * @see User
     * @apiNote Updated in version 1.2 to use EncryptVigenere method from
     * Cryptographer class. Removed characters other than A-Z, a-z, 0-9
     * from passwords to comply with new password policies.
     * @apiNote Updated in version 1.3 to remove all hard coded passwords
     * and replace them with null values. Passwords are set during login now.
     * @apiNote Updated in version 1.4 to use overloaded constructor which takes
     * only the username and MFA Code and sets password to null by default.
     */
    private void PopulateDatabase() {
        _users.add(new User(
                _cryptographer.EncryptVigenere("scientist"), 
                1374628910
            )
        );
        _users.add(new User(
                _cryptographer.EncryptVigenere("engineer"), 
                2039485712
            )
        );
        _users.add(new User(
                _cryptographer.EncryptVigenere("security"), 
                1748392023
            )
        );
    }

    /**
     * Creates/updates a text file to print out the usernames and hashed passwords.
     * 
     * @apiNote Added in version 1.0.
     * @apiNote Updated in version 1.1 to use new getter functions.
     * @apiNote Reverted in version 1.2 to use record class getter functions like version 1.0.
     * @apiNote Renamed from OutputDatabaseToFile to GenerateFile in version 1.4.
     */
    public void GenerateFile() {
        try(FileWriter writer = new FileWriter("user_info.txt")) {
            for(User user : _users) {
                writer.write(user.GetUsername() + ":" + user.GetPassword() + "\n");
            }
        }
        catch(IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    //#endregion FUNCTIONS
}
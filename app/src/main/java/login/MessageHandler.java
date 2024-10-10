package login;

//#region IMPORTS
import java.util.HashMap;
import java.util.Map;
//#endregion IMPORTS

/**
 * COP 4078 Exercise: 5
 * File Name: MessageHandler.java
 * 
 * This class defines a series of error messages.
 * These messages can be globally accessed and called to print
 * to the console at any time.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @apiNote Added in version 1.1 to manage and display error messages
 * in a more robust way.
 * @apiNote Made keys constants to avoid mispelling in validation functions in version 1.2.
 * @apiNote Modified debug messages in version 1.3 to have [DEBUG] tags to help differentiate
 * them in the console output.
 * @apiNote Removed BuildErrorMessage() function in version 1.4. Renamed class from ErrorMessages
 * to MessageHandler. Removed and reformatted various messages.
 */
public class MessageHandler {
    //#region VARIABLES
    /**
     * HashMap for storing error messages.
     */
    private static final Map<String, String> _messages = new HashMap<>();
    //#endregion VARIABLES

    //#region KEYS
    public static final String DEFAULT_PASSWORD = "DEFAULT_PASSWORD";
    public static final String EMPTY_INPUT      = "EMPTY_INPUT";
    public static final String INCORRECT_INPUT  = "INCORRECT_INPUT";
    public static final String INVALID_INPUT    = "INVALID_INPUT";
    public static final String NO_MORE_ATTEMPTS = "NO_MORE_ATTEMPTS";
    public static final String POLICY_FAILED    = "POLICY_FAILED";
    public static final String SQL_INJECTION    = "SQL_INJECTION";
    public static final String TOO_MANY_FAILS   = "TOO_MANY_FAILS";
    //#endregion KEYS

    //#region STATIC INITIALIZER
    static {
        _messages.put(DEFAULT_PASSWORD, "Too many failed attempts, creating default password.");
        _messages.put(EMPTY_INPUT,      "%s cannot be empty.");
        _messages.put(INCORRECT_INPUT,  "%s is incorrect or not found.");
        _messages.put(INVALID_INPUT,    "%s contains invalid input.");
        _messages.put(NO_MORE_ATTEMPTS, "No attempts remaining.");
        _messages.put(POLICY_FAILED,    "%s failed to meet one or more requirements.");
        _messages.put(SQL_INJECTION,    "Input contains invalid characters.");
        _messages.put(TOO_MANY_FAILS,   "Too many failed attempts, returning to start.");
    }
    //#endregion STATIC INITIALIZER

    //#region FUNCTIONS
    /**
     * Matches the error message key to the related error message 
     * and prints it out to the console.
     * 
     * @param key Key to match with related error message in {@link #_messages}
     * @apiNote Added in version 1.1.
     */
    public static void PrintMessage(String key) {
        System.out.println(_messages.getOrDefault(key, "Unknown Error"));
    }

    /**
     * Matches the error message key to the related error message 
     * and prints it out to the console.
     * Takes in value to apply to placeholder '%s'.
     * 
     * @param key Key to match with related error message in {@link #_messages}
     * @param value Value to replace placeholder string with.
     * @apiNote Added in version 1.4 for placeholder messages.
     */
    public static void PrintMessage(String key, String value) {
        String message = _messages.get(key);
        if(message != null) {
            System.out.println(String.format(message, value));
        }
        else {
            System.out.println("Unknown Error");
        }
    }
    //#endregion FUNCTIONS
}
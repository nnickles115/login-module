package login;

//#region IMPORTS
import java.io.Console;
//#endregion IMPORTS

/**
 * COP 4078 Exercise: 5
 * File Name: Login.java
 * 
 * The Login class handles the login flow of the program.
 * All console inputs are taken in here and processed
 * with their relevant functions and classes.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @see CodeHandler
 * @see Database
 * @see PasswordHandler
 * @see UsernameHandler
 * @apiNote Moved IsUsernameValid, IsPasswordValid methods into Validation class
 * in version 1.1.
 * @apiNote Updated methods to work with Cryptographer class in version 1.2.
 * Added lazy loading to services in all classes that contain services.
 * @apiNote Added {@link #PromptNewPassword(String)} in version 1.3 to ask for a new password if
 * there is not already one associated with the User object (specified by the entered username).
 * @apiNote Completely rewrote all logic and most functions in version 1.4.
 */
public class Login {
    //#region SERVICES
    private final CodeHandler _codeHandler = CodeHandler.GetInstance();
    private final Database _database = Database.GetInstance();
    private final PasswordHandler _passwordHandler = PasswordHandler.GetInstance();
    private final UsernameHandler _usernameHandler = UsernameHandler.GetInstance();
    //#endregion SERVICES
    
    //#region CONSTANTS
    private final Console CONSOLE = System.console();
    private final int ONE_SECOND = 1000;
    //#endregion CONSTANTS

    //#region VARIABLES
    /**
     * Counts the number of correctly entered credential information.
     */
    private int _correctCredentials;
    //#endregion VARIABLES

    //#region SINGLETON PATTERN
    private static Login _instance = null;

    private Login() {
        // Ensure the console exists.
        if(CONSOLE == null) {
            System.err.println(
                "No console available. Please run this program from the command line or terminal."
            );
            System.exit(1);
        }

        // Start the program loop.
        Run();
    }

    public static Login GetInstance() {
        if(_instance == null) {
            _instance = new Login();
        }
        return _instance;
    }
    //#endregion SINGLETON PATTERN

    //#region FUNCTIONS
    /**
     * Main program loop.
     * Calls functions to gather login information from the user.
     * Loops until all credentials are correctly entered.
     * Calls function to generate login info file.
     * Prints welcome message after finishing the login.
     * @apiNote Updated in version 1.1 to handle displaying the welcome
     * message or login failed message.
     * @apiNote Rewrote all logic in version 1.4.
     */
    private void Run() {
        String username = "";
        while(_correctCredentials < 3) {
            // Reset number of correct credentials on each loop.
            _correctCredentials = 0;
            
            // Gather login information.
            username = ReadUsername();
            ReadPassword(username);
            if(_correctCredentials >= 2) {
                ReadCode(username);
            }

            // Should end after ReadCode(), if not print failed message and loop again.
            System.out.println("Login failed, returning to start.");
            AddDelay(ONE_SECOND);
        }
        // Print encrypted login info to text file and display welcome message.
        _database.GenerateFile();
        System.out.println("Login successful, welcome " + username + "!");
    }

    /**
     * Prompts the user for a username.
     * Attempts to authenticate the username and prompts again if failed.
     * 
     * @return {@code username} if client is authenticated.
     * @see UsernameHandler
     * @apiNote Updated logic in version 1.1 to work with new validation methods.
     * Moved welcome message to {@link #Run()}.
     * @apiNote Updated logic in version 1.4 to use new UsernameHandler class for
     * authentication.
     */
    private String ReadUsername() {
        String username;
        while(true) {
            username = CONSOLE.readLine("Username: ");
            if(_usernameHandler.AuthenticateUsername(username)) {
                _correctCredentials++;
                return username;
            }
            // Print error and delay to prevent brute force.
            MessageHandler.PrintMessage(MessageHandler.INCORRECT_INPUT, "Username");
            AddDelay(ONE_SECOND);
        }
    }

    /**
     * Prompts the user for a password.
     * Attempts to authenticate the password and prompts again if failed.
     * If input fails more times than the number of attempts, the loop in {@link #Run()}
     * will jump back to {@link #ReadUsername()}.
     * It calls {@link #ReadNewPassword(String)} if the password is {@code null} with the associated
     * username.
     *
     * @param username The username associated with the password.
     * @see PasswordHandler
     * @apiNote Updated logic in version 1.1 to work with new validation methods.
     * @apiNote Rewrote logic in version 1.4 and updated to work with new PasswordHandler class.
     */
    private void ReadPassword(String username) {
        char[] passwordChars;
        int attempts = 2;

        // Create new password if one doesn't already exist.
        if(!_passwordHandler.DoesPasswordExist(username)) {
            ReadNewPassword(username);
        }

        // Prompt user for password.
        while(attempts > 0) {
            passwordChars = CONSOLE.readPassword("Password: ");
            if(_passwordHandler.AuthenticatePassword(username, passwordChars)) {
                _correctCredentials++;
                break;
            }
            // Print remaining attempts and delay to prevent brute force.
            MessageHandler.PrintMessage(MessageHandler.INCORRECT_INPUT, "Password");
            attempts = RemainingAttempts(attempts);
            AddDelay(ONE_SECOND);
        }

        // Return user back to start of program after too many fails.
        if(attempts <= 0) {
            MessageHandler.PrintMessage(MessageHandler.TOO_MANY_FAILS);
        }
    }

    /**
     * Prompts the user to create a new password.
     * If user fails to create a new password, it uses the PasswordHandler to
     * call the DefaultPassword class and generate a random default password.
     *
     * @param username The username associated with the password.
     * @see PasswordHandler
     * @apiNote Updated logic in version 1.1 to work with new validation methods.
     * @apiNote Rewrote logic in version 1.4 and updated to work with new PasswordHandler class.
     * Renamed function from PromptNewPassword to ReadNewPassword.
     */
    private void ReadNewPassword(String username) {
        char[] passwordChars;
        int attempts = 2;

        while(attempts > 0) {
            passwordChars = CONSOLE.readPassword("Create a new password: ");
            if(_passwordHandler.CreateNewPassword(username, passwordChars)) {
                break;
            }
            // Print remaining attempts and delay to prevent brute force.
            attempts = RemainingAttempts(attempts);
            AddDelay(ONE_SECOND);
        }

        if(attempts <= 0) {
            MessageHandler.PrintMessage(MessageHandler.DEFAULT_PASSWORD);
            _passwordHandler.CreateDefaultPassword(username);
        }
    }

    /**
     * Prompts the user for a MFA Code.
     * Attempts to authenticate the code and prompts again if failed.
     * If input fails more times than the number of attempts, the loop in {@link #Run()}
     * will jump back to {@link #ReadUsername()}.
     *
     * @param username The username associated with the MFA Code.
     * @see CodeHandler
     * @apiNote Updated logic in version 1.1 to work with new validation methods.
     * @apiNote Rewrote logic in version 1.4 and updated to work with new CodeHandler class.
     * Renamed function from ReadMFA to ReadCode.
     */
    private void ReadCode(String username) {
        String code;
        int attempts = 2;
        while(attempts > 0) {
            code = CONSOLE.readLine("MFA Code: ");
            if(_codeHandler.AuthenticateCode(username, code)) {
                _correctCredentials++;
                break;
            }
            // Print remaining attempts and delay to prevent brute force.
            MessageHandler.PrintMessage(MessageHandler.INCORRECT_INPUT, "MFA Code");
            attempts = RemainingAttempts(attempts);
            AddDelay(ONE_SECOND);
        }
    }
    //#endregion FUNCTIONS

    //#region HELPER FUNCTIONS
    /**
     * Counts the number of remaining attempts for user input.
     * Prints to console number of remaining attempts.
     * 
     * @param attempts Current number of remaining attempts from caller function.
     * @return Decremented number of attempts.
     * @apiNote Added in version 1.0.
     * @apiNote Rewrote logic in verison 1.4. Renamed function from AttemptCounter to RemainingAttempts.
     */
    private int RemainingAttempts(int attempts) {
        attempts--;
        System.out.println("Remaining Attempts: " + attempts);
        return attempts;
    }

    /**
     * Makes the caller thread sleep for the time set by the parameter.
     * 
     * @param millis Time in milliseconds.
     * @apiNote Added in version 1.0.
     * @apiNote Moved from Utils class which no longer exists in version 1.4.
     */
    private void AddDelay(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch(IllegalArgumentException e){
            System.err.println("Parameter 'millis' must be >= 0.");
        }
        catch(InterruptedException e) {
            InterruptThread();
        }
    }

    /**
     * Helper function to avoid nesting try-catch in {@link #AddDelay(long)}.
     * 
     * @apiNote Added in version 1.0.
     * @apiNote Moved from Utils class which no longer exists in version 1.4.
     */
    private void InterruptThread() {
        try{
            Thread.currentThread().interrupt();
        }
        catch(SecurityException e) {
            System.err.println(e.getMessage());
        }
    }
    //#endregion HELPER FUNCTIONS
}
package login;

/**
 * COP 4078 Exercise: 5
 * File Name: App.java
 * 
 * The App class is the main point of entry for the program.
 * This class grabs an instance of Login.
 * Since the Login instance is null, it calls the constructor 
 * which executes the Run() function to start the main program loop.
 * 
 * @author Noah Nickles
 * @version 1.4
 * @see Login
 * @apiNote Added in version 1.0.
 * @apiNote Rewrote 90% of the entire program in version 1.4 due to cluttered logic.
 * @implNote Currently {@link #DEBUG} is set to {@code true} so that generated password
 * can be printed to the console since sending emails is not a feature (yet).
 */
public class App {
    /**
     * Constant value to be manually set at edit time.
     * If set to {@code true}, it enables the output of 
     * debug messages to the console.
     */
    public static final boolean DEBUG = true;

    /**
     * Main method. 
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Login.GetInstance();
        System.exit(0);
    }
}
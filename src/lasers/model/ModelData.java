package lasers.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Use this class to customize the data you wish to send from the model
 * to the view when the model changes state.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class ModelData {
    /** user output messages use this */
    private PrintWriter userOut;
    /** user input messages are read with this */
    private BufferedReader userIn;

    /**
     * Get the square size of the board.  This is the first message
     * communicated to the user.
     *
     * @return the board dimensions
     *
     * @throws NumberFormatException a bad message or board dimension was received
     */
    public int getBoardDIM() throws NumberFormatException, IOException {
        // Error handling? For user input of row and col
        String cmd = this.userIn.readLine();
        String[] coords = cmd.split("\\s+", 2);
        if (coords.length != 2) {
            this.userOut.println("Enter two digits: row col");
        }
        int row = 0;
        int col = 0;
        try {
            row = Integer.parseInt(coords[0]);
            col = Integer.parseInt(coords[1]);
        } catch (NumberFormatException nfe) {
            this.userOut.println("Enter two digits: row col");
        }
        return row + col;
    }
}

package lasers.backtracking;

import lasers.model.LasersModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The class represents a single configuration of a safe.  It is
 * used by the backtracker to generate successors, check for
 * validity, and eventually find the goal.
 *
 * This class is given to you here, but it will undoubtedly need to
 * communicate with the model.  You are free to move it into the lasers.model
 * package and/or incorporate it into another class.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class SafeConfig implements Configuration {
    //The UI's connection to the model
    private LasersModel model;
    //the empty tile
    private final static char EMPTY = '.';
    //the laser command character
    public final static char LASER = 'L';
    //the laser beam character
    public final static char LASER_BEAM = '*';
    //the pillar value
    private final static char PILLAR = 'X';
    //use for numbered pillars
    public final static List<Character> PILLARS = new ArrayList<Character>(Arrays.asList('0', '1', '2', '3', '4', 'X'));

    //
    private char[][] board;
    private int row;
    private int col;
    private int cursorRow;
    private int cursorCol;


    public SafeConfig(String filename) throws FileNotFoundException {
        try {
            // create a scanner tied to the input file
            Scanner in = new Scanner(new File(filename));
            // read the square dimension of the board
            this.row = in.nextInt();
            this.col = in.nextInt();
            // create the board
            this.board = new char[row][col];

            // loop to fill in the board with info
            for (int n = 0; n < this.row; n++) {
                for (int j = 0; j < this.col; j++) {
                    // if it is not a dot, store the value
                    board[n][j] = in.next().charAt(0);
                }
            }
            this.cursorRow = 0;
            this.cursorCol =-1;
            // close the scanner/file
            in.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copy construct a config.
     *
     * @param other the config to copy from
     */
    private SafeConfig(SafeConfig other) {
        // copy over dimension
        this.row = other.row;
        this.col = other.col;
        //copy over cursor
        this.cursorRow = other.cursorRow;
        this.cursorCol = other.cursorCol;
        // create the new board
        this.board = new char[row][col];
        // copy over the rows from the old board to the new one
        for (int j = 0; j < this.col; j++) {
            System.arraycopy(other.board[j], 0, this.board[j], 0, this.row);

        }
        // add laser and its beams to the board
        addLaser(cursorRow, cursorCol);

    }

    /**
     * Overall Rules:
     * - Laser beams can cross, but they cannot point at each other
     * - LASER = [row][col]
     * - LASER_BEAM:all 4 directions
     */
    public void addLaser(int row, int col) {
        if(board[row][col] != EMPTY && board[row][col] != LASER_BEAM) {
            System.out.println("ERROR adding laser at: (" + row + ", " + col + ")");
            return ;
        }
            this.board[row][col] = LASER;
            // if (EMPTY == '.') {    //if dot add otherwise don't

            // NORTH
            for (int r = row - 1; r >= 0; r--) {
                if (PILLARS.contains(board[r][col]) || board[r][col] == LASER) {   // at a pillar
                    break;
                }
                board[r][col] = LASER_BEAM;
            }

            // SOUTH
            for (int r = row + 1; r < this.row; r++) {   // this.col = DIM
                if (PILLARS.contains(board[r][col]) || board[r][col] == LASER) {   // at a pillar
                    break;
                }
                board[r][col] = LASER_BEAM;
            }

            // EAST
            for (int c = col + 1; c < this.col; c++) {
                if (PILLARS.contains(board[row][c]) || board[row][c] == LASER) {   // at a pillar
                    break;
                }
                board[row][c] = LASER_BEAM;
            }

            // WEST
            for (int c = col - 1; c >= 0; c--) {
                if (PILLARS.contains(board[row][c]) || board[row][c] == LASER) {   // at a pillar
                    break;
                }
                board[row][c] = LASER_BEAM;
            }
    }

    /**
     * Returns the two-dimensional char array that represents the Board
     *
     * @return char[][]
     */
    public char[][] getBoard() {
        return this.board;
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        // create empty collection of successors
        ArrayList<Configuration> successors = new ArrayList<>();
        this.cursorCol++;

        if(cursorCol == this.col){
            cursorRow++;
            cursorCol = 0;
        }
        if(cursorRow == this.row){
            return successors;
        }
        //Each tile that isn't a pillar can be populated with two successors
        if (PILLARS.contains(board[cursorRow][cursorCol]) || this.board[cursorRow][cursorCol] == PILLAR) {   // at a pillar
            SafeConfig pillarConfig = new SafeConfig(this);
            successors.add(pillarConfig);
            return successors;
        }
        SafeConfig laserConfig = new SafeConfig(this);
        SafeConfig emptyConfig = new SafeConfig(this);
        //add laser successor
        successors.add(laserConfig);
        //add empty tile successor
        successors.add(emptyConfig);
        return successors;
    }

    @Override
    public boolean isValid() {
        // step from left to right checking in all 3 directions
        for (int r = 0; r < this.row; ++r) {
            for (int c = 0; c < this.col; ++c) {
                //Two lasers may not shine beams directly into each other
                if (board[r][c] == LASER) {
                    if (!verifyLaser(r, c)) {
                        return false;
                    }
                }
                //Any pillar that is numbered must have exactly that number of lasers placed in the directly adjacent cardinal directions (N/S/E/W).
                else if (board[r][c] == '0') {
                    int returnedCounter = verifyPillar(r, c);
                    if (returnedCounter != 0) {
                        return false;
                    }
                }
                else if (board[r][c] == '1') {
                    int returnedCounter = verifyPillar(r, c);
                    if (returnedCounter != 1) {
                        return false;
                    }
                }
                else if (board[r][c] == '2') {
                    int returnedCounter = verifyPillar(r, c);
                    if (returnedCounter != 2) {
                        return false;
                    }
                }
                else if (board[r][c] == '3') {
                    int returnedCounter = verifyPillar(r, c);
                    if (returnedCounter != 3) {
                        return false;
                    }
                }
                else if (board[r][c] == '4') {
                    int returnedCounter = verifyPillar(r, c);
                    if (returnedCounter != 4) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean verifyLaser ( int row, int col){
        // NORTH
        for (int r = row - 1; r >= 0; r--) {
            if (PILLARS.contains(board[r][col])) {   // at a pillar
                break;
            }
            if (board[r][col] == LASER) {
                return false;
            }
        }

        // SOUTH
        for (int r = row + 1; r < this.row; r++) {   // this.col = DIM
            if (PILLARS.contains(board[r][col])) {   // at a pillar
                break;
            }

            if (board[r][col] == LASER) {
                return false;
            }
        }

        // EAST
        for (int c = col + 1; c < this.col; c++) {
            if (PILLARS.contains(board[row][c])) {   // at a pillar
                break;
            }

            if (board[row][c] == LASER) {
                return false;
            }
        }

        // WEST
        for (int c = col - 1; c >= 0; c--) {
            if (PILLARS.contains(board[row][c])) {   // at a pillar
                break;
            }

            if (board[row][c] == LASER) {
                return false;
            }
        }

        return true;
    }

    /**
     * Purpose: To give restrictions as to how many lasers can be adjacent to certain pillars
     * */
    public int verifyPillar ( int row, int col){
        // value x: any number
        // other possible values: 0-4

        // func check if char is a int or x, then = pillar, and break
        // board[r][col] == 'X'
        int counter = 0;


        if (row > 0) { //NORTH
            if (board[row - 1][col] == LASER) {
                counter++;
            }
        }

        if (row < this.row - 1) { //SOUTH
            if (board[row + 1][col] == LASER) {
                counter++;
            }
        }

        if (col < this.col - 1) { //EAST
            if (board[row][col + 1] == LASER) {
                counter++;
            }
        }

        if (col > 0) { //WEST
            if (board[row][col - 1] == LASER) {
                counter++;
            }
        }

        return counter;
    }


    @Override
    public boolean isGoal () {
        int count = 0;
        for (int r = 0; r < this.row; ++r) {
            for (int c = 0; c < this.col; ++c) {
                //All tiles must be "covered" by either a pillar, laser or laser beam.
                if (this.board[r][c] == EMPTY ) {
                    count++;
                }
                //Any pillar that is numbered must have exactly that number of lasers placed in the directly adjacent cardinal directions (N/S/E/W).
                else if (board[r][c] == '0') {
                    int returnedCounter = verifyPillar(r, c);
                    if (returnedCounter != 0) {
                        return false;
                    }
                }
                else if (board[r][c] == '1') {
                    int returnedCounter = verifyPillar(r, c);
                    if (returnedCounter != 1) {
                        return false;
                    }
                }
                else if (board[r][c] == '2') {
                    int returnedCounter = verifyPillar(r, c);
                    if (returnedCounter != 2) {
                        return false;
                    }
                }
                else if (board[r][c] == '3') {
                    int returnedCounter = verifyPillar(r, c);
                    if (returnedCounter != 3) {
                        return false;
                    }
                }
                else if (board[r][c] == '4') {
                    int returnedCounter = verifyPillar(r, c);
                    if (returnedCounter != 4) {
                        return false;
                    }
                }
            }
        }
        return count <= 0;
    }

    /**
     * Returns a string representation of the Safe
     * @return String
     */
    public String toString () {
        // build the column numbers
        String returnString = "  ";
        for (int col = 0; col < this.col; ++col) {
            returnString += col;
        }

        returnString += "\n";

        // build the rows with number and values
        for (int row = 0; row < this.row; ++row) {
            String values = "";
            for (int col = 0; col < this.col; ++col) {
                values = values + (this.board[row][col]);
            }
            returnString += "\n";
            returnString += "" + row + "|" + values;

        }
        return returnString;
    }
}

package lasers.backtracking;

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
    private final static char EMPTY = '.';
    private final static char PILLAR = 'X';
    /** the laser command */
    public final static char LASER = 'L';
    /** the laser beam command */
    public final static char LASER_BEAM = '*';
    private char[][] board;
    private int row;
    private int col;
    /** use for pillars **/
    public final static List<Character> PILLARS = new ArrayList<Character>(Arrays.asList('0', '1', '2', '3', '4', 'X'));


    public SafeConfig(String filename) throws FileNotFoundException {
        // create a scanner tied to the input file
        Scanner in = new Scanner(new File(filename));
        // read the square dimension of the board
        this.row = in.nextInt();
        this.col = in.nextInt();
        // create the board
        this.board = new char[row][col];

        // loop to fill in the board with info
        for (int n=0; n<this.row; n++) {
            for (int j=0; j<this.col; j++) {
                // if it is not a dot, store the value
                board[n][j] = in.next().charAt(0);
            }
        }

        // close the scanner/file
        in.close();
    }

    /**
     * Copy construct a config.
     *
     * @param other the config to copy from
     */
    private SafeConfig(SafeConfig other, int row, int col) {
        // copy over dimension
        this.row = other.row;
        this.col = other.col;
        // create the new board
        this.board = new char[other.row][other.col];
        // copy over the rows from the old board to the new one
        for (int r=0; r<this.row; r++) {
            System.arraycopy(other.board[r], 0, this.board[r], 0, this.row);

        }
        for(int r = 0; r < this.row; ++r) {
            for (int c = 0; c < this.col; ++c) {
                //Two lasers may not shine beams directly into each other
                if(board[r][c] == LASER) {
                    if (!verifyLaser(r, c)) {
                        System.out.println("ERROR verifying at: (" + r + ", " + c + ")");
                        return ;
                    }
            }

    }

    /**
     * Returns the two-dimensional char array that represents the Board
     * @return char[][]
     */
    public char[][] getBoard(){
        return this.board;
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        // create empty collection of successors
        List<Configuration> successors = new LinkedList<Configuration>();
        for (int row=0; row<this.row; row++) {
            // advance the cursor in the new child config to the next column
            SafeConfig child = new SafeConfig(this, row, this.col+1);
            successors.add(child);
        }
        return successors;
    }

    @Override
    public boolean isValid() {
        // TODO
        return false;
    }

    @Override
    public boolean isGoal() {
        // TODO
        return false;
    }

    /**
     * Returns a string representation of the Safe
     * @return String
     */
    public String toString(){
        char[][] grid = this.makeCopy();
        StringBuilder str = new StringBuilder("  ");
        for (int col = 0; col < this.col; col++) {
            if (col >= 10) {
                int col2 = col;
                while (col2 >= 10) {
                    col2 -= 10;
                }
                str.append(col2).append(" ");
            }
            else {
                str.append(col).append(" ");
            }
        }
        str.append("\n  ");
        str.append("-".repeat(Math.max(0, ((this.col * 2) - 1))));
        str.append("\n");
        for (int i = 0; i < this.row; i++) {
            if(i >= 10){
                int row = i;
                while(row >= 10){
                    row -= 10;
                }
                str.append(row).append("|");
            }
            else {
                str.append(i).append("|");
            }
            for (int j = 0; j < this.col; j++) {
                str.append(grid[j][i]).append(" ");
            }
            if(i != this.row-1) {
                str.append("\n");
            }
        }
        return str.toString();
    }
}

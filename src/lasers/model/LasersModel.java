package lasers.model;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.util.*;

/**
 * The model of the lasers safe.  You are free to change this class however
 * you wish, but it should still follow the MVC architecture.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class LasersModel {
    /** the observers who are registered with this model */
    private List<Observer<LasersModel, ModelData>> observers;
    private final static char EMPTY = '.';
    private final static char PILLAR = 'X';
    private char[][] board;
    private int row;
    private int col;
    /** the laser command */
    public final static char LASER = 'L';
    /** the laser beam command */
    public final static char LASER_BEAM = '*';
    public final static List<Character> PILLARS = new ArrayList<Character>(Arrays.asList('0', '1', '2', '3', '4', 'X'));

    /**
     * Constructor for LaserModel. This is called by the main program to
     * read in the initial configuration from a file.
     *
     * @param filename the name of the file with the numbers
     * @throws FileNotFoundException if the file cannot be found
     */
    public LasersModel(String filename) throws FileNotFoundException, NullPointerException {
        this.observers = new LinkedList<>();
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
     * Purpose: To make sure what is being displayed is valid or follows the rules of pillars and lasers
     * */
    public void Verify(){

        // step from left to right checking in all 3 directions
        for(int r = 0; r < this.row; ++r) {
            for (int c = 0; c < this.col; ++c) {
                if(board[r][c] == EMPTY) {
                    System.out.println("ERROR verifying at: (" + r + ", " + c + ")");
                    return ;
                }

                if(board[r][c] == LASER) {
                    if (!verifyLaser(r, c)) {
                        System.out.println("ERROR verifying at: (" + r + ", " + c + ")");
                        return ;
                    }
                }

                if(board[r][c] == '0') {
                    int returnedCounter = verifyPillar(r, c);
                    if(returnedCounter != 0) {
                        System.out.println("ERROR verifying at: (" + r + ", " + c + ")");
                        return ;
                    }
                }
                if(board[r][c] == '1') {
                    int returnedCounter = verifyPillar(r, c);
                    if(returnedCounter != 1) {
                        System.out.println("ERROR verifying at: (" + r + ", " + c + ")");
                        return ;
                    }
                }
                if(board[r][c] == '2') {
                    int returnedCounter = verifyPillar(r, c);
                    if(returnedCounter != 2) {
                        System.out.println("ERROR verifying at: (" + r + ", " + c + ")");
                        return ;
                    }
                }
                if(board[r][c] == '3') {
                    int returnedCounter = verifyPillar(r, c);
                    if(returnedCounter != 3) {
                        System.out.println("ERROR verifying at: (" + r + ", " + c + ")");
                        return ;
                    }
                }
                if(board[r][c] == '4') {
                    int returnedCounter = verifyPillar(r, c);
                    if(returnedCounter != 4) {
                        System.out.println("ERROR verifying at: (" + r + ", " + c + ")");
                        return ;
                    }
                }
            }
        }
        System.out.println("Safe is fully verified");

    }

    /**
     * Overall Rules:
     *  - Laser beams can cross, but they cannot point at each other
     *  - LASER = [row][col]
     *  - LASER_BEAM:all 4 directions
     * */
    public void addLaser(int row, int col) {
        if(board[row][col] != EMPTY && board[row][col] != LASER_BEAM) {
            System.out.println("ERROR adding laser at: (" + row + ", " + col + ")");
            return ;
        }
        this.board[row][col] = LASER;
        // if (EMPTY == '.') {    //if dot add otherwise don't

        // NORTH
        for(int r = row - 1; r >= 0; r--) {
            if(PILLARS.contains(board[r][col]) || board[r][col] == LASER) {   // at a pillar
                break;
            }

            board[r][col] = LASER_BEAM;
        }

        // SOUTH
        for(int r = row + 1; r < this.row; r++) {   // this.col = DIM
            if(PILLARS.contains(board[r][col]) || board[r][col] == LASER) {   // at a pillar
                break;
            }

            board[r][col] = LASER_BEAM;
        }

        // EAST
        for(int c = col + 1; c < this.col; c++) {
            if(PILLARS.contains(board[row][c]) || board[row][c] == LASER) {   // at a pillar
                break;
            }

            board[row][c] = LASER_BEAM;
        }

        // WEST
        for(int c = col - 1; c >= 0; c--) {
            if(PILLARS.contains(board[row][c]) || board[row][c] == LASER) {   // at a pillar
                break;
            }

            board[row][c] = LASER_BEAM;
        }
    }

    public boolean verifyLaser(int row, int col) {
        // if (EMPTY == '.') {    //if dot add otherwise don't

        // NORTH
        for(int r = row - 1; r >= 0; r--) {
            if(PILLARS.contains(board[r][col])) {   // at a pillar
                break;
            }
            if(board[r][col] == LASER) {
                return false;
            }
        }

        // SOUTH
        for(int r = row + 1; r < this.row; r++) {   // this.col = DIM
            if(PILLARS.contains(board[r][col])) {   // at a pillar
                break;
            }

            if(board[r][col] == LASER) {
                return false;
            }
        }

        // EAST
        for(int c = col + 1; c < this.col; c++) {
            if(PILLARS.contains(board[row][c])) {   // at a pillar
                break;
            }

            if(board[row][c] == LASER) {
                return false;
            }
        }

        // WEST
        for(int c = col - 1; c >= 0; c--) {
            if(PILLARS.contains(board[row][c])) {   // at a pillar
                break;
            }

            if(board[row][c] == LASER) {
                return false;
            }
        }

        return true;
    }

    /**
     * Overall Rules:
     *  - Once a Laser is removed, the "L" and its "*"s would be removed.
     *  - As a result, it would be just EMPTY (.)
     * */
    public void removeLaser(int row, int col) {
        // consider crossing beams

        this.board[row][col] = EMPTY;
        // if (EMPTY == '.') {    //if dot add otherwise don't
        // NORTH
        for(int r = row - 1; r >= 0; r++) {
            if (board[r][col] == LASER_BEAM) {
                board[r][col] = EMPTY;
            }
        }

        // SOUTH
        for(int r = row + 1; r < this.col; r++) {   // this.col = DIM
            if (board[r][col] == LASER_BEAM) {
                board[r][col] = EMPTY;
            }
        }

        // EAST
        for(int c = col + 1; c < this.col; c++) {
            if (board[row][c] == LASER_BEAM) {
                board[row][c] = EMPTY;
            }
        }

        // WEST
        for(int c = col - 1; c >= 0; c++) {
            if (board[row][c] == LASER_BEAM) {
                board[row][c] = EMPTY;
            }
        }
    }

    /**
     * Purpose: To give restrictions as to how many lasers can be adjacent to certain pillars
     * */
    public int verifyPillar(int row, int col) {
        // value x: any number
        // other possible values: 0-4

        // func check if char is a int or x, then = pillar, and break
        // board[r][col] == 'X'
        int counter = 0;


        if(row > 0) { //NORTH
            if(board[row-1][col] == LASER) {
                counter++;
            }
        }

        if(row < this.row - 1) { //SOUTH
            if(board[row+1][col] == LASER) {
                counter++;
            }
        }

        if(col < this.col-1) { //EAST
            if(board[row][col+1] == LASER) {
                counter++;
            }
        }

        if(col > 0) { //WEST
            if(board[row][col-1] == LASER) {
                counter++;
            }
        }

        return counter;
    }

    /***
     * Purpose: To display what is updated as of now
     */
    public void Display(){
        // build the column numbers
        for (int col = 0; col < this.col; ++col) {
            System.out.print(col);
        }

        System.out.println("");

        for (int col = 0; col < this.col; ++col) {
            System.out.print(" " + "-");
        }
        System.out.println("");

        // build the rows with number and values
        for (int row = 0; row < this.row; ++row) {
            String values = "";
            for (int col = 0; col < this.col; ++col) {
                values = values+(board[row][col]);
            }
            System.out.println(" " + row +"|" + values);

        }
    }

    /**
     * Add a new observer.
     *
     * @param observer the new observer
     */
    public void addObserver(Observer<LasersModel, ModelData > observer) {
        this.observers.add(observer);
    }

    /**
     * Notify observers the model has changed.
     *
     * @param data optional data the model can send to the view
     */
    private void notifyObservers(ModelData data){
        for (Observer<LasersModel, ModelData> observer: observers) {
            observer.update(this, data);
        }
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public char getValue(int row, int col) {
        return this.board[this.row][this.col];
    }
}

package lasers.ptui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import lasers.model.LasersModel;
import lasers.model.ModelData;
import lasers.model.Observer;

/**
 * This class represents the view portion of the plain text UI.  It
 * is initialized first, followed by the controller (ControllerPTUI).
 * You should create the model here, and then implement the update method.
 *
 * @author Sean Strout @ RIT CS
 * @author YOUR NAME HERE
 */
public class LasersPTUI implements Observer<LasersModel, ModelData> {
    /** The UI's connection to the model */
    private LasersModel model;
    /** The controller*/
    private ControllerPTUI controller;
    /** user output messages use this */
    private PrintWriter userOut;
    /** user input messages are read with this */
    private BufferedReader userIn;

    /** the add command */
    public final static char ADD_CMD = 'a';
    /** the display command */
    public final static char DISPLAY_CMD = 'd';
    /** the help command */
    public final static char HELP_CMD = 'h';
    /** the quit command */
    public final static char QUIT_CMD = 'q';
    /** the remove command */
    public final static char REMOVE_CMD = 'r';
    /** the verify command */
    public final static char VERIFY_CMD = 'v';

    /** the command prompt */
    public final static String PROMPT = "> ";
    /** the laser command */
    public final static String LASER = "L";
    /** the laser beam command */
    public final static String LASER_BEAM = "*";

    /**
     * Construct the PTUI.  Create the lasers.lasers.model and initialize the view.
     * @param filename the safe file name
     * @throws FileNotFoundException if file not found
     */
    public LasersPTUI(String filename) throws FileNotFoundException {
        try {
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    /**
     * Accessor for the model the PTUI create.
     *
     * @return the model
     */
    public LasersModel getModel() { return this.model; }

    /** The "main loop" that handles user input and output while the game is active. */
    public void go(Scanner in) { // pass scanner as args to go()
        model.Display();
        System.out.print("> ");
        while (in.hasNextLine()) {
            // convert String coordinates to char
            String line = in.nextLine();
            String[] x = line.split("\\s+");
            char cmd = line.charAt(0);
            int row;
            int col;

            switch (cmd) {
                case ADD_CMD:
                    //somehow save row and col
                    row = Integer.parseInt(String.valueOf(x[1]));
                    col = Integer.parseInt(String.valueOf(x[2]));
                    //add laser at that (r,c)
                    this.model.addLaser(row, col);
                    model.Display();
                    break;
                case DISPLAY_CMD:
                    model.Display();
                    break;
                case HELP_CMD:
                    System.out.println("a|add r c: Add laser to (r,c)");
                    System.out.println("d|display: Display safe");
                    System.out.println("h|help: Print this help message");
                    System.out.println("q|quit: Exit program");
                    System.out.println("r|remove r c: Remove laser from (r,c)");
                    System.out.println("v|verify: Verify safe correctness");
                    break;
                case QUIT_CMD:
                    //causes program to silently exit no output
                    in.close();
                    break;
                case REMOVE_CMD:
                    //somehow save row and col
                    row = Integer.parseInt(String.valueOf(x[1]));
                    col = Integer.parseInt(String.valueOf(x[2]));
                    //remove laser at that (r,c)
                    this.model.removeLaser(row, col);
                    break;
                case VERIFY_CMD:
                    this.model.Verify();
                    break;
            }
            System.out.print("> ");
        }
    }

    @Override
    public void update(LasersModel model, ModelData data) {
        // TODO
    }
}

package lasers.ptui;

import lasers.model.LasersModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * This class represents the controller portion of the plain text UI.
 * It takes the model from the view (LasersPTUI) so that it can perform
 * the operations that are input in the run method.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class ControllerPTUI  {
    /** The UI's connection to the lasers.lasers.model */
    private LasersModel model;

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


    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param model The laser model
     */
    public ControllerPTUI(LasersModel model) throws FileNotFoundException {
            this.model = model;
            //initialize view
            //this.controller = new ControllerPTUI(this.model);
    }

    /**
     * Run the main loop.  This is the entry point for the controller
     * @param inputFile The name of the input command file, if specified
     */
    public void run(String inputFile) throws FileNotFoundException {
        model.Display();
        System.out.print("> ");
        Scanner in = new Scanner(System.in);
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
                    model.Display();
                    break;
                case VERIFY_CMD:
                    this.model.Verify();
                    break;
            }
            System.out.print("> ");
        }
    }
}

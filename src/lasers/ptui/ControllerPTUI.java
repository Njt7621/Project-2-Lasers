package lasers.ptui;

import lasers.model.LasersModel;

import java.io.FileNotFoundException;
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
    /** The controller*/
    private ControllerPTUI controller;

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
        LasersPTUI ptui = new LasersPTUI(inputFile);
        Scanner systemIn = new Scanner(System.in);  // from command line
        ptui.go(systemIn);
    }
}

package de.steilerdev.whatToStudy;

import de.steilerdev.whatToStudy.Functionalities.Help;
import de.steilerdev.whatToStudy.Functionalities.Version;
import de.steilerdev.whatToStudy.Interface.Functionality;

/**
 * This class is used to manage the basic terminal interface for the application.
 * It is starting the appropriate classes and functions based on the user input handed over using parameters.
 */
public class Main {

    /**
     * This function is called, at the startup of the application.
     * It creates a new object, that is handling the terminal interface.
     * @param args
     */
    public static void main(String[] args)
    {
        Functionality program = null;

        if (args.length == 1)
        {
            if(args[0].equals("-h"))
            {
                program = new Help();
            } else if(args[0].equals("-v"))
            {
                program = new Version();
            } else if(args[0].equals("-p"))
            {

            }
        } else if(args.length == 2)
        {
            if(args[0].equals("-e"))
            {

            } else if(args[0].equals("-l"))
            {

            } else if(args[0].equals("-t"))
            {

            }
        }

        if(program == null)
        {
            System.out.println("Unsuitable amount or unrecognised arguments.");
            program = new Help();
        }
        program.run(args);
    }
}

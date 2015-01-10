package de.steilerdev.whatToStudy;

import de.steilerdev.whatToStudy.Exception.WhatToStudyException;
import de.steilerdev.whatToStudy.Functionalities.*;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * This class is used to manage the basic terminal interface for the application.
 * It is starting the appropriate classes and functions based on the user input handed over using parameters.
 */
public class Main {

    //General properties for the program

    /**
     * Floating point numbers with "," as delimiter, change to US if using ".".
     */
    public static final NumberFormat localizedNumberFormat = NumberFormat.getInstance(Locale.GERMAN);

    /**
     * The fully qualified path of the internal file.
     */
    public static final String internalFile = "de/steilerdev/whatToStudy/Network/StudyNetwork_new.dne";

    /**
     * This function is called, at the startup of the application.
     * It creates a new object, that is handling the terminal interface.
     * @param args The command line arguments stated during the call of the application.
     */
    public static void main(String[] args)
    {
        Functionality program = null;

        //Checking command line arguments and selecting correct functionality.
        if(args.length == 0)
        {
            program = new Interactive();
        }
        if(args.length == 1)
        {
            if(args[0].equals("-h"))
            {
                program = new Help();
            } else if(args[0].equals("-v"))
            {
                program = new Version();
            } else if(args[0].equals("-d"))
            {
                program = new Draw();
            }
        } else if(args.length == 2)
        {
            if(args[0].equals("-e"))
            {
                program = new Evaluate();
            } else if(args[0].equals("-l"))
            {
                program = new Learn();
            } else if(args[0].equals("-t"))
            {
                program = new Test();
            } else if(args[0].equals("-d"))
            {
                program = new Draw();
            }
        } else if(args.length == 3)
        {
            if(args[0].equals("-e"))
            {
                program = new Evaluate();
            } else if(args[0].equals("-t"))
            {
                program = new Test();
            }
        }

        //If no functionality applied to the command line arguments, the help dialog is shown.
        if(program == null)
        {
            System.out.println("Unsuitable amount or unrecognised arguments.");
            program = new Help();
        }

        //Starting the selected functionality and properly handling the error.
        try
        {
            program.run(args);
        } catch(WhatToStudyException e)
        {
            System.err.println("Error occurred during runtime: " + e.getMessage());
        }
    }
}

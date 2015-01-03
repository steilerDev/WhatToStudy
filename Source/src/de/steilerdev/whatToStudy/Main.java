package de.steilerdev.whatToStudy;

import de.steilerdev.whatToStudy.Exception.WhatToStudyException;
import de.steilerdev.whatToStudy.Functionalities.*;
import norsys.netica.NeticaException;

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
                program = new Evaluate();
            } else if(args[0].equals("-l"))
            {
                program = new Learn();
            } else if(args[0].equals("-t"))
            {

            }
        }

        if(program == null)
        {
            System.out.println("Unsuitable amount or unrecognised arguments.");
            program = new Help();
        }
        try
        {
            program.run(args);
        } catch (NeticaException e)
        {
            System.err.println("Error occurred during runtime: " + e.getMessage());
        } catch(WhatToStudyException e)
        {
            System.err.println("Error occurred during runtime: " + e.getMessage());
        }
    }
}

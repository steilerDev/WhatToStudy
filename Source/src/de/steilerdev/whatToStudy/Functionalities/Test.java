/**
 * Copyright (C) 2015 Frank Steiler <frank@steilerdev.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.steilerdev.whatToStudy.Functionalities;

import de.steilerdev.whatToStudy.Exception.WhatToStudyException;
import de.steilerdev.whatToStudy.Utility.CSVStreamer;
import de.steilerdev.whatToStudy.Utility.Case.*;
import de.steilerdev.whatToStudy.Utility.Case.State;
import norsys.netica.*;

/**
 * This functionality is testing the quality of the network using a set of data.
 */
public class Test implements Functionality
{
    private static String internalFile = "de/steilerdev/whatToStudy/Network/StudyNetwork_new.dne";

    /**
     * This functionality is testing the quality of the network using a set of data.
     * @param args The command line arguments stated during the call of the application.
     * @throws WhatToStudyException If an error occurs.
     */
    @Override
    public void run(String[] args) throws WhatToStudyException
    {
        if(true)
        {
            throw new WhatToStudyException("Unfortunately this functionality is not available due to an unresolved bug in NeticaJ.");
        }
        //Instantiating variables, to finalize them later.
        Environ env = null;
        Net net = null;
        Caseset testCases = null;
        NetTester tester = null;
        try
        {
            System.out.println("Starting to test the network.");

            //Creating a new environment that is used as default environment later.
            env = new Environ (null);

            if(args.length == 2)
            {   //If there is no network file use the internal file instead.
                System.out.println("Loading network from internal file");
                net = new Net(new Streamer(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(internalFile), //Getting the network as java.io.InputStream from the Netica file
                        "StudyNetwork", //Giving the Network a name
                        env)); //Handling over the Environ
            } else if (args.length == 3)
            {   //Load user specified network
                System.out.println("Loading network from user specified file.");
                net = new Net(new Streamer(args[2]));
            } else
            {
                throw new WhatToStudyException("Unable to load network!");
            }

            //The node list containing all nodes, that are going to be tested
            NodeList testNodes = new NodeList(net);

            //Getting the node that you want to test
            Node finalGrade = net.getNode(FinalGrade.getHeader());

            // Adding the test node to the node list
            testNodes.add(finalGrade);

            System.out.println("Compiling network.");
            //Clear the network before compiling
            net.retractFindings();
            net.compile();

            System.out.println("Creating a new net tester.");
            tester = new NetTester(testNodes, testNodes, -1);

            //Load test cases
            System.out.println("Loading test cases.");
            Streamer inStream = CSVStreamer.getNeticaStream(args[1], "TestingStream", env);
            testCases = new Caseset();
            testCases.addCases(inStream, 1.0, null);

            System.out.println("Testing network using cases.");
            tester.testWithCaseset(testCases);

            printConfusionMatrix(tester, finalGrade);
            System.out.println("      Error rate for " + finalGrade.getName() + " = " + tester.getErrorRate(finalGrade));
            System.out.println("Logarithmic loss for " + finalGrade.getName() + " = " + tester.getLogLoss(finalGrade));
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally
        {
            if(testCases != null)
            {
                try
                {
                    testCases.finalize();
                } catch (NeticaException e)
                {
                    throw new WhatToStudyException("A Netica based error occurred during the finalization of the test cases.");
                }
            }
            if(tester != null)
            {
                try
                {
                    tester.finalize();
                } catch (NeticaException e)
                {
                    throw new WhatToStudyException("A Netica based error occurred during the finalization of the test cases.");
                }
            }
            if(net != null)
            {
                try
                {
                    net.finalize();
                } catch (NeticaException e)
                {
                    throw new WhatToStudyException("A Netica based error occurred during the finalization of the net.");
                }
            }
            if(env != null)
            {
                try
                {
                    env.finalize();
                } catch (NeticaException e)
                {
                    throw new WhatToStudyException("A Netica based error occurred during the finalization of the net.");
                }
            }
        }
    }

    /*
     * Print a confusion matrix table.
     */
    public static void printConfusionMatrix (NetTester nt, Node node) throws NeticaException {
        int numStates = node.getNumStates();
        System.out.println("\nConfusion matrix for " + node.getName() + ":");

        for (int i=0;  i < numStates;  ++i){
            System.out.print ("\t" + node.state(i).getName());
        }
        System.out.println ("\tActual");

        for (int a=0;  a < numStates;  ++a){
            for (int p=0;  p < numStates;  ++p){
                System.out.print ("\t" + (int) (nt.getConfusion(node, p, a)));
            }
            System.out.println ("\t" + node.state(a).getName());
        }
    }
}

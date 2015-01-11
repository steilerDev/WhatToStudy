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
import de.steilerdev.whatToStudy.Main;
import de.steilerdev.whatToStudy.Utility.CSVStreamer;
import de.steilerdev.whatToStudy.Utility.Case.*;
import norsys.netica.*;

import java.util.ArrayList;

/**
 * This functionality is testing the quality of the network using a set of data.
 */
public class Test implements Functionality
{

    /**
     * Amount of cases where the prediction encourages to study the course, and the person achieved a very good or good grade
     */
    private static int truePositive = 0;

    /**
     * Amount of cases where the prediction encourages to study the course, and the person did not achieve a very good or good grade
     */
    private static int falsePositive = 0;

    /**
     * Amount of cases where the prediction discourages to study the course, and the person did not achieve a very good or good grade
     */
    private static int trueNegative = 0;

    /**
     * Amount of cases where the prediction discourages to study the course, and the person achieved a very good or good grade
     */
    private static int falseNegative = 0;

    /**
     * Amount of cases where the prediction could neither discourage nor encourage the person
     */
    private static int noResult = 0;

    private static Net net = null;

    /**
     * This functionality is testing the quality of the network using a set of data.
     * @param args The command line arguments stated during the call of the application.
     * @throws WhatToStudyException If an error occurs.
     */
    @Override
    public void run(String[] args) throws WhatToStudyException
    {
        //Instantiating variables, to finalize them later.
        Environ env = Environ.getDefaultEnviron();
        Evaluate evaluate = new Evaluate();
        try
        {
            System.out.println("Starting to test the network.");

            //Creating a new environment that is used as default environment later.
            if(env == null)
            {
                env = new Environ(null);
            }

            if(args.length == 2)
            {   //If there is no network file use the internal file instead.
                System.out.println("Loading network from internal file");
                net = new Net(new Streamer(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(Main.internalFile), //Getting the network as java.io.InputStream from the Netica file
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

            ArrayList<Case> testCases = CSVStreamer.getCaseList(args[1]);

            System.out.println("Compiling network");
            net.compile();

            System.out.println("Starting to test the accuracy");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            testCases.stream().forEach(currentCase -> {
                try
                {
                    net.retractFindings();
                    FinalGrade testResult = (FinalGrade) evaluate.evaluateCase(currentCase, net, Environ.getDefaultEnviron());
                    if (testResult == FinalGrade.VERY_GOOD) //Test recommends studying
                    {
                        if (currentCase.getFinalGrade() == FinalGrade.VERY_GOOD || currentCase.getFinalGrade() == FinalGrade.GOOD)
                        {
                            truePositive++;
                        } else
                        {
                            falsePositive++;
                        }
                    } else if (testResult == FinalGrade.FAILED)
                    {
                        if (currentCase.getFinalGrade() == FinalGrade.VERY_GOOD || currentCase.getFinalGrade() == FinalGrade.GOOD)
                        {
                            falseNegative++;
                        } else
                        {
                            truePositive++;
                        }
                    } else
                    {
                        noResult++;
                    }
                } catch (NeticaException | WhatToStudyException e)
                {
                    System.err.println("An error occurred, the result might not be correct. Please try again.");
                    System.err.println("Details:");
                    System.err.println(e.getMessage());
                }
            });
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            System.out.println();
            System.out.println("Finished!");
            System.out.println("Results:");

            System.out.println("                                     |   Person achieves a Very_Good or Good Grade   |  Person fails or receives a satisfying grade     |");
            System.out.println(" Test encourages studying the course |                      " + truePositive + "                       |                      " + falsePositive + "                           |");
            System.out.println("Test discourages studying the course |                      " + falseNegative + "                       |                      " + trueNegative + "                          |");

            double errorRate = (falseNegative + falsePositive)/(falseNegative + falsePositive + truePositive + trueNegative);
            System.out.println("Error rate: " + errorRate);
            System.out.println("Unable to evaluate " + noResult + " cases, because their result was not obvious");
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally
        {
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
}

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
import norsys.netica.*;

/**
 * This class is used to learn the CPT's of the network stored network.
 */
public class Learn implements Functionality
{
    private static int learningAlgorithm = Learner.EM_LEARNING;
    private static int learningIterations = 200;
    private static String fileName = "Learned.dne";
    private static String internalFile = "de/steilerdev/whatToStudy/Network/StudyNetwork_new.dne";

    @Override
    public void run(String[] args) throws WhatToStudyException
    {
        Learner learner = null;
        Caseset cases = null;
        Net net = null;
        Environ env = null;
        try
        {
            env = new Environ(null);

            //Getting the network from the file
            System.out.println("Loading network.");
            net = new Net(new Streamer(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(internalFile), //Getting the network as java.io.InputStream from the Netica file
                    "StudyNetwork", //Giving the Network a name
                    Environ.getDefaultEnviron())); //Handling over the Environ
            NodeList nodes = net.getNodes();

            //Deleting all previously stored CPT's
            System.out.println("Clearing current CPT.");
            nodes.parallelStream().forEach(node -> {
                try
                {
                    ((Node) node).deleteTables();
                } catch (NeticaException ignoreExceptionInsideLambda) {}
            });

            // Read in the case file into a case set
            System.out.println("Loading cases from file");
            cases = new Caseset();
            Streamer caseFile = CSVStreamer.getNeticaStream(args[1], "LearningStreamer", env);
            cases.addCases(caseFile, 1.0, null);

            //Configuring learning
            switch (learningAlgorithm)
            {
                case 1:
                    System.out.println("Starting learning process, using case counting learning algorithm with " + learningIterations + " iterations.");
                    break;
                case 3:
                    System.out.println("Starting learning process, using EM (Expectation Maximization) learning algorithm with " + learningIterations + " iterations.");
                    break;
                case 4:
                    System.out.println("Starting learning process, using Gradient Descent learning algorithm with " + learningIterations + " iterations.");
                    break;
                default:
                    System.out.println("Starting learning process, using an unknown learning algorithm with " + learningIterations + " iterations.");
            }
            learner = new Learner(learningAlgorithm);
            learner.setMaxIterations(learningIterations);

            //Starting learning
            learner.learnCPTs(nodes, cases, 1.0);

            //Saving the learned CPT's
            System.out.println("Writing file \"" + fileName + "\"");
            net.write(new Streamer(fileName));
            System.out.println("Finished learning!");
        } catch (NeticaException e)
        {
            throw new WhatToStudyException("A Netica based error occurred: " + e.getMessage());
        } finally
        {
            // Closing all resources, not necessary, but a good habit.
            if (learner != null)
            {
                try
                {
                    learner.finalize();
                } catch (NeticaException e)
                {
                    throw new WhatToStudyException("A Netica based error occurred during the finalization of the learner.");
                }
            }
            if (cases != null)
            {
                try
                {
                    cases.finalize();
                } catch (NeticaException e)
                {
                    throw new WhatToStudyException("A Netica based error occurred during the finalization of the case set.");
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
                    throw new WhatToStudyException("A Netica based error occurred during the finalization of the environment.");
                }
            }
        }
    }
}

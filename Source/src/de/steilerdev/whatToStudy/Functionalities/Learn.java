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

    @Override
    public void run(String[] args) throws WhatToStudyException, NeticaException
    {
        Environ env = new Environ(null);

        //Getting the network from the file
        System.out.println("Loading network.");
        Net net = new Net(new Streamer(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream("de/steilerdev/whatToStudy/Network/StudyNetwork_new.dne"), //Getting the network as java.io.InputStream from the Netica file
                "StudyNetwork", //Giving the Network a name
                env)); //Handling over the Environ
        NodeList nodes = net.getNodes();

        //Deleting all previously stored CPT's
        System.out.println("Clearing current CPT.");
        nodes.parallelStream().forEach(node -> {
            try
            {
                ((Node) node).deleteTables();
            } catch (NeticaException ignoreException)
            {
            }
        });

        // Read in the case file into a case set
        System.out.println("Loading cases from file");
        Caseset cases = new Caseset ();
        Streamer caseFile = CSVStreamer.getNeticaStream(args[1], "LearningStreamer", env);
        cases.addCases ( caseFile, 1.0, null);

        //Configuring and starting learning process
        System.out.println("Starting learning process, using " + learningAlgorithm + " with " + learningIterations + " iterations");
        Learner learner = new Learner (learningAlgorithm);
        learner.setMaxIterations (learningIterations);
        learner.learnCPTs ( nodes, cases, 1.0);

        //Saving the learned CPT's
        System.out.println("Writing file");
        net.write(new Streamer("Learned.dne"));
        System.out.println("Finished learning!");

        // Closing all resources, not neccessary, but a good habit.
        learner.finalize();
        cases.finalize();
        net.finalize();
    }
}

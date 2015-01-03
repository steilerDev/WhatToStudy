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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Learn implements Functionality
{
    @Override
    public void run(String[] args) throws WhatToStudyException, NeticaException
    {
        Environ env = new Environ(null);

        Net net = new Net(new Streamer(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream("de/steilerdev/whatToStudy/Network/StudyNetwork_new.dne"), //Getting the network as java.io.InputStream from the Netica file
                "StudyNetwork", //Giving the Network a name
                env)); //Handling over the Environ

        NodeList nodes = net.getNodes();
        for (int n=0; n<nodes.size(); n++) {
            nodes.getNode(n).deleteTables();
        }

        System.out.println(System.getProperty("user.dir"));

        System.out.println("Loading example data file.");
        try
        {
            File initialFile = new File(args[1]);
            InputStream targetStream = new FileInputStream(initialFile);
            System.out.println("Learning CPT");
            net.reviseCPTsByCaseFile(CSVStreamer.getNeticaStream(targetStream, "LearningStream", env), nodes, 1.0);
        } catch (FileNotFoundException e)
        {
            throw new WhatToStudyException(e);
        }

        // clear current findings, calculated from learning

        for (int n=0; n<nodes.size(); n++) {
            nodes.getNode(n).finding().clear();
        }

        System.out.println("Writing file");
        net.write(new Streamer("Learned.dne"));
        System.out.println("Finished");
        net.finalize();
    }
}

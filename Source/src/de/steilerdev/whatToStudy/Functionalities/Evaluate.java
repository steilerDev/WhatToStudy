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

import norsys.netica.*;

/**
 * This class is using the stored network to evaluate if the system can recommend the user to study a specific course
 * The evaluation is done using inference.
 */
public class Evaluate implements Functionality
{
    /**
     * This function is loading the stored network and evaluates the given data against it. As a result the likeness of the course, the student should study is presented.
     * @param args The command line arguments stated during the call of the application. In this case it should be -e and the path to a CSV file, that needs to be evaluated.
     * @throws NeticaException If an error occurs.
     */
    @Override
    public void run(String[] args) throws NeticaException
    {
        Environ env = new Environ(null);
        Net net = new Net(new Streamer(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream("de/steilerdev/whatToStudy/Network/StudyNetwork.dne"), //Getting the network as java.io.InputStream from the Netica file
                "StudyNetwork", //Giving the Network a name
                env)); //Handling over the Environ

        //Getting nodes, to be able to set them later
        Node visitAsia    = net.getNode( "VisitAsia"    );
        Node tuberculosis = net.getNode( "Tuberculosis" );
        Node xRay         = net.getNode( "XRay"         );

        net.compile();

        xRay.finding().enterState ("abnormal");
        double belief = tuberculosis.getBelief ("present");
        System.out.println ("\nGiven an abnormal X-ray,\n"+
                "the probability of tuberculosis is " + belief);

        net.finalize();
    }
}

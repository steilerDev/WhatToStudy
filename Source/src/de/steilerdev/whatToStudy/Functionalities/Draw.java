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

import javax.swing.*;
import norsys.netica.*;
import norsys.netica.Net;
import norsys.netica.gui.NetPanel;
import norsys.netica.gui.NodePanel;

/**
 * This class is drawing a user selected network, or the internal stored one.
 */
public class Draw extends JFrame implements Functionality
{
    private static String internalFile = "de/steilerdev/whatToStudy/Network/StudyNetwork_new.dne";

    /**
     * Showing the selected network.
     * @param args The command line arguments stated during the call of the application.
     */
    @Override
    public void run(String[] args) throws WhatToStudyException
    {
        try
        {
            Environ env = new Environ(null);
            Net net;
            if(args.length == 2)
            {   //If there is no network file use the internal file instead.
                System.out.println("Loading network from internal file");
                net = new Net(new Streamer(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(internalFile), //Getting the network as java.io.InputStream from the Netica file
                        "StudyNetwork", //Giving the Network a name
                        env)); //Handling over the Environ
                this.setTitle("Bayesian Network for \"" + internalFile +  "\"");
            } else if (args.length == 3)
            {   //Load user specified network
                System.out.println("Loading network from user specified file.");
                net = new Net(new Streamer(args[2]));
                this.setTitle("Bayesian Network for \"" + args[2] + "\"");
            } else
            {
                throw new WhatToStudyException("Unable to load network!");
            }

            //Optional compiling network, could reveal possible errors.
            System.out.println("Compiling network.");
            net.compile();

            //Drawing panel.
            System.out.println("Drawing network.");
            NetPanel netPanel = new NetPanel(net, NodePanel.NODE_STYLE_AUTO_SELECT);
            getContentPane().add(new JScrollPane(netPanel)); //adds the NetPanel to the JPanel
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 500); // Using a pre-defined size, frame is going to be resizable
            setVisible(true);
        } catch (NeticaException e)
        {
            throw new WhatToStudyException("A Netica based error occurred while drawing the net.");
        } catch (Exception e)
        {
            throw new WhatToStudyException("An unknown error occurred while drawing the net.");
        }
    }
}
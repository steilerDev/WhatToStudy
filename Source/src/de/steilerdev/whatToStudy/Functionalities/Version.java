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

/**
 * This functionality of the application is printing the version information of the program.
 */
public class Version implements Functionality
{
    /**
     * Printing out the version information.
     * @param args The command line arguments stated during the call of the application.
     */
    @Override
    public void run(String[] args)
    {
        System.out.println("##################################################################################");
        System.out.println("WhatToStudy v0.1a1");
        System.out.println("By Frank Steiler & Lisa Mischer");
        System.out.println("Licensed using a GNU GPL v2.0 license");
        System.out.println("http://www.github.com/steilerDev/WhatToStudy");
        System.out.println();
        System.out.println("Created as part of the lecture \"Knowledge based systems\" at the DHBW Stuttgart");
        System.out.println("##################################################################################");
    }
}

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

import java.io.Console;

/**
 * This class is requesting the information of the user through an interactive command line interface.
 */
public class Interactive implements Functionality
{
    /**
     * This function is executing the interactive mode, reading the input and evaluating it.
     * @param args The command line arguments stated during the call of the application.
     */
    @Override
    public void run(String[] args) throws WhatToStudyException
    {
        Console console = System.console();
        String input = console.readLine("Enter input:");

        System.out.println("##################################################################################");
        System.out.println("Welcome to the interactive mode of WhatToStudy");
        System.out.println("http://www.github.com/steilerDev/WhatToStudy");
        System.out.println("##################################################################################");
        System.out.println();
        System.out.println();
    }
}

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
 * This functionality of the application is printing the help of the program to the terminal.
 */
public class Help implements Functionality
{
    /**
     * Printing out the help page.
     * @param args The command line arguments stated during the call of the application.
     */
    @Override
    public void run(String args[])
    {
        System.out.println("Usage:");
        System.out.println("\t-e <<student file>>:\tEvaluate student using the source file.");
        System.out.println("\t\t\t\t\t\t\tThe file should be a CSV.");
        System.out.println("\t-h:\t\t\t\t\t\tPrint help (This message).");
        System.out.println("\t-v:\t\t\t\t\t\tPrint version.");
        System.out.println("\t-l <<source file>>:\t\tLearn CPT using the source file.");
        System.out.println("\t\t\t\t\t\t\tThe file should be a CSV.");
        System.out.println("\t-p:\t\t\t\t\t\tShow current bayesian network.");
        System.out.println("\t-t <<test file>>:\t\tTest current bayesian network using the provided test file.");
        System.out.println("\t\t\t\t\t\t\tThe file should be a CSV.");
    }
}

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
        //General usage
        System.out.println("Usage:");
        System.out.println("\t-e <<student file>>:\tEvaluate student using the source file. Optional: Pass the Network file as second argument.");
        System.out.println("\t\t\t\t\t\t\tThe file should be a CSV.");
        System.out.println("\t-h:\t\t\t\t\t\tPrint help (This message).");
        System.out.println("\t-v:\t\t\t\t\t\tPrint version.");
        System.out.println("\t-l <<source file>>:\t\tLearn CPT using the source file.");
        System.out.println("\t\t\t\t\t\t\tThe file should be a CSV.");
        System.out.println("\t-d:\t\t\t\t\t\tShow current bayesian network.");
        System.out.println("\t-t <<test file>>:\t\tTest current bayesian network using the provided test file.");
        System.out.println("\t\t\t\t\t\t\tThe file should be a CSV.");

        //Input file specification
        System.out.println();
        System.out.println("Note: Every input file needs to fit the following specifications:");
        System.out.println("\t- Please remove all mutations from the files ('ä', 'ö', 'ü', etc.)");
        System.out.println("\t- Floating point numbers should be in the German format (Decimal point is a \",\")");
        System.out.println("\t- The file needs to be a CSV, where the \";\" character is the delimiter.");
        System.out.println("\t- The file needs to have a valid header and valid values. The following specification apply:");
        System.out.println("\t\t- 1st column: Qualifikation (Abitur, Techniker, FH Reife)");
        System.out.println("\t\t- 2nd column: Schnitt (A floating point number within the range 1.0 to 6.0)");
        System.out.println("\t\t- 3rd column: Bundesland (Baden-Wuerttemberg, Baden-Württemberg, Bayern, Nordrhein-Westfalen, Bremen, Sachsen, Thueringen, Thüringen, Hessen, Mecklenburg-Vorpommern, Berlin, Rheinland-Pfalz, Hamburg, Sachsen-Anhalt, Niedersachsen, Brandenburg, Saarland, Schleswig-Holstein)");
        System.out.println("\t\t- 4th column: Mathe (A floating point number within the range 1.0 to 6.0, or keine)");
        System.out.println("\t\t- 5th column: Physik (A floating point number within the range 1.0 to 6.0, or keine)");
        System.out.println("\t\t- 6th column: Deutsch (A floating point number within the range 1.0 to 6.0, or keine)");
        System.out.println("\t\t- 7th column: Schultyp (Allgemeinbildendes Gymnasium, Gesamtschule, Technisches Gymnasium, Wirtschaftsgymnasium, n.a.)");
        System.out.println("\t\t- 8th column: OLT-Mathe (An integer between 0 and 100)");
        System.out.println("\t\t- 9th column: OLT-Deutsch (An integer between 0 and 100)");
        System.out.println("\t\t- 10th column: Studierfaehigkeitstest or Studierfähigkeitstest (An integer between 0 and 1000 or n.a.)");
        System.out.println("\t\t- 11th column: Alter (An integer bigger than 0)");
        System.out.println("\t\t- 12th column: Geschlecht (m, w)");
        System.out.println("\t\t- 13th column: Jahreseinkommen der Eltern (An integer bigger than 0)");
        System.out.println("\t\t- 14th column: Staatsbuergerschaft or Staatsbürgerschaft (deutsch, EU Buerge, EU Bürger, Non European)");
        System.out.println("\t\t- 15th column: Studiengang (Elektrotechnik, Informatik, Maschinenbau, Soziale Arbeit, Wirtschaftswissenschaften)");
        System.out.println("\t\t- 16th column: Zwischenkalk (A floating point number within the range 1.0 to 6.0; Should match Abschluss)");
        System.out.println("\t\t- 17th column: Abschluss (A floating point number within the range 1.0 to 4.0, or abgebrochen; Should match Abschluss)");


        //Network input (Cleaned data format)
        System.out.println();
        System.out.println("\t- The network is using the following (cleaned) data format:");
        System.out.println("\t\t- 1st column: Qualification (Abitur, Techniker, FH_Reife)");
        System.out.println("\t\t- 2nd column: Qualification_Average (Very_Good, Good, Satisfying, Failed)");
        System.out.println("\t\t- 3rd column: State (BW, BY, BE, BB, HB, HH, HE, MV, NI, NW, RP, SL, SN, ST, SH, TH)");
        System.out.println("\t\t- 4th column: Math (Very_Good, Good, Satisfying, Failed, NA)");
        System.out.println("\t\t- 5th column: Physics (Very_Good, Good, Satisfying, Failed, NA)");
        System.out.println("\t\t- 6th column: German (Very_Good, Good, Satisfying, Failed, NA)");
        System.out.println("\t\t- 7th column: School_Type (A_Gymnasium, T_Gymnasium, W_Gymnasium, Gesamtschule, NA)");
        System.out.println("\t\t- 8th column: OLT_Math (Very_Good, Good, Satisfying, Failed)");
        System.out.println("\t\t- 9th column: OLT_German (Very_Good, Good, Satisfying, Failed)");
        System.out.println("\t\t- 10th column: Study_Ability_Test (Very_Good, Good, Satisfying, Failed, NA)");
        System.out.println("\t\t- 11th column: Age (Young, Average, Old)");
        System.out.println("\t\t- 12th column: Sex (M, W)");
        System.out.println("\t\t- 13th column: Parental_Income (Low, Low_Middle, High_Middle, High)");
        System.out.println("\t\t- 14th column: Nationality (German, EU, Non_EU)");
        System.out.println("\t\t- 15th column: Course (E_Engineering, C_Science, Engineering, S_Work, Economics)");
        System.out.println("\t\t- 16th column: Final_Grade (Very_Good, Good, Satisfying, Failed)");
    }
}

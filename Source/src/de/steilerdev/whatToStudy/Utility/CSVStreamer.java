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
package de.steilerdev.whatToStudy.Utility;

import de.steilerdev.whatToStudy.Exception.WhatToStudyException;
import norsys.netica.Environ;
import norsys.netica.NeticaException;
import norsys.netica.Streamer;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * This class converts a input stream with an CSV file to a file readable by Netica (Respecting Netica Constraints and converting continuous values into discrete ones).
 * The class is also adjusting the stream according to the needs of the network, using the following specification.
 * - 1st column: Qualification (Abitur, Techniker, FH_Reife)
 * - 2nd column: Qualification_Average (Very_Good, Good, Satisfying, Failed)
 * - 3rd column: State (BW, BY, BE, BB, HB, HH, HE, MV, NI, NW, RP, SL, SN, ST, SH, TH)
 * - 4th column: Math (Very_Good, Good, Satisfying, Failed, NA)
 * - 5th column: Physics (Very_Good, Good, Satisfying, Failed, NA)
 * - 6th column: German (Very_Good, Good, Satisfying, Failed, NA)
 * - 7th column: School_Type (A_Gymnasium, T_Gymnasium, W_Gymnasium, Gesamtschule, NA)
 * - 8th column: OLT_Math (Very_Good, Good, Satisfying, Failed)
 * - 9th column: OLT_German (Very_Good, Good, Satisfying, Failed)
 * - 10th column: Study_Ability_Test (Very_Good, Good, Satisfying, Failed, NA)
 * - 11th column: Age (Young, Average, Old)
 * - 12th column: Sex (M, W)
 * - 13th column: Parental_Income (Low, Low_Middle, High_Middle, High)
 * - 14th column: Nationality (German, EU, Non_EU)
 * - 15th column: Course (E_Engineering, C_Science, Engineering, S_Work, Economics)
 * - 16th column: Final_Grade (Very_Good, Good, Satisfying, Failed)
 *
 * Input file specification:
 * - 1st column: Qualifikation (Abitur, Techniker, FH Reife)
 * - 2nd column: Schnitt (A floating point number within the range 1.0 to 6.0)
 * - 3rd column: Bundesland (Baden-Wuerttemberg, Baden-Württemberg, Bayern, Nordrhein-Westfalen, Bremen, Sachsen, Thueringen, Thüringen, Hessen, Mecklenburg-Vorpommern, Berlin, Rheinland-Pfalz, Hamburg, Sachsen-Anhalt, Niedersachsen, Brandenburg, Saarland, Schleswig-Holstein
 * - 4th column: Mathe (A floating point number within the range 1.0 to 6.0, or keine)
 * - 5th column: Physik (A floating point number within the range 1.0 to 6.0, or keine)
 * - 6th column: Deutsch (A floating point number within the range 1.0 to 6.0, or keine)
 * - 7th column: Schultyp (Allgemeinbildendes Gymnasium, Gesamtschule, Technisches Gymnasium, Wirtschaftsgymnasium, n.a.)
 * - 8th column: OLT-Mathe (An integer between 0 and 100)
 * - 9th column: OLT-Deutsch (An integer between 0 and 100)
 * - 10th column: Studierfaehigkeitstest or Studierfähigkeitstest (An integer between 0 and 1000 or n.a.)
 * - 11th column: Alter (An integer bigger than 0)
 * - 12th column: Geschlecht (m, w)
 * - 13th column: Jahreseinkommen der Eltern (An integer bigger than 0)
 * - 14th column: Staatsbuergerschaft or Staatsbürgerschaft (deutsch, EU Buerge, EU Bürger, Non European)
 * - 15th column: Studiengang (Elektrotechnik, Informatik, Maschinenbau, Soziale Arbeit, Wirtschaftswissenschaften)
 * - 16th column: Zwischenkalk (A floating point number within the range 1.0 to 6.0; Should match Abschluss)
 * - 17th column: Abschluss (A floating point number within the range 1.0 to 4.0, or abgebrochen; Should match Abschluss)
 */
public class CSVStreamer
{
    /**
     * The String that is separating the CSV,
     */
    private static String cvsSplitBy = ";";

    /**
     * Counting current line number of input file for sophisticated error message.
     */
    private static int currentLineNumber = 0;

    /**
     * Floating point numbers with "," as delimiter, change to US if using ".".
     */
    private static NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);

    /**
     * This function is converting a file specified by the exercise, cleans the values and returns a unified Streamer, usable by NeticaJ.
     * @param inputStream The input file as stream.
     * @param streamName The name of the Streamer used by Netica.
     * @param env The current Environment.
     * @return A Streamer object usable by NeticaJ
     * @throws Exception If an error occurs.
     */
    public static Streamer getNeticaStream(InputStream inputStream, String streamName, Environ env) throws WhatToStudyException
    {
        PrintStream fileStream = null;
        BufferedReader br = null;
        try
        {
            //Temp file, storing the converted information.
            File file = new File(".tempData");
            if (!file.exists())
            {
                file.createNewFile();
            }
            file.deleteOnExit();

            //The print stream used to write to the file
            fileStream = new PrintStream(file);

            //The line that is currently read.
            String line;

            //Reading the input stream (input file)
            br = new BufferedReader(new InputStreamReader(inputStream));

            //Parsing and validating header
            if ((line = br.readLine()) != null)
            {
                currentLineNumber++;
                String[] header = line.split(cvsSplitBy);
                if (!validateHeader(header))
                {
                    throw new WhatToStudyException("The header of the specific file does not meet the stated requirements. See help page for more information");
                } else
                {
                    //Printing the cleaned header
                    fileStream.println("Qualification " +
                            "Qualification_Average " +
                            "State " +
                            "Math " +
                            "Physics " +
                            "German " +
                            "School_Type " +
                            "OLT_Math " +
                            "OLT_German " +
                            "Study_Ability_Test " +
                            "Age " +
                            "Sex " +
                            "Parental_Income " +
                            "Nationality " +
                            "Course " +
                            "Final_Grade");
                }
            }

            while ((line = br.readLine()) != null)
            {
                currentLineNumber++;
                String[] lineArray = line.split(cvsSplitBy);

                if (lineArray.length != 17)
                {
                    throw new NeticaException("Invalid number of columns in line " + currentLineNumber);
                } else
                {
                    fileStream.print(cleanQualification(lineArray[0].trim()) + " ");
                    fileStream.print(cleanAverage(lineArray[1].trim()) + " ");
                    fileStream.print(cleanState(lineArray[2].trim()) + " ");
                    fileStream.print(cleanMath(lineArray[3].trim()) + " ");
                    fileStream.print(cleanPhysics(lineArray[4].trim()) + " ");
                    fileStream.print(cleanGerman(lineArray[5].trim()) + " ");
                    fileStream.print(cleanSchoolType(lineArray[6].trim()) + " ");
                    fileStream.print(cleanOLTMath(lineArray[7].trim()) + " ");
                    fileStream.print(cleanOLTGerman(lineArray[8].trim()) + " ");
                    fileStream.print(cleanStudyAbilityTest(lineArray[9].trim()) + " ");
                    fileStream.print(cleanAge(lineArray[10].trim()) + " ");
                    fileStream.print(cleanSex(lineArray[11].trim()) + " ");
                    fileStream.print(cleanIncome(lineArray[12].trim()) + " ");
                    fileStream.print(cleanNationality(lineArray[13].trim()) + " ");
                    fileStream.print(cleanCourse(lineArray[14].trim()) + " ");
                    fileStream.print(cleanFinalGrade(lineArray[15].trim(), lineArray[16]) + " ");
                    fileStream.print("\n");
                }
            }

            fileStream.flush();

            FileInputStream in = new FileInputStream(file);

            return new Streamer(in, streamName, env);
        } catch (IOException e)
        {
            throw new WhatToStudyException(e);
        } catch(NeticaException e)
        {
            throw new WhatToStudyException(e);
        } finally
        {
            try
            {
                if(fileStream != null)
                {
                    fileStream.close();
                }
                if(br != null)
                {
                    br.close();
                }
            } catch (IOException e)
            {
                throw new WhatToStudyException(e);
            }
        }
    }

    /**
     * This function is checking if the header of the CSV fits the specifications.
     * @param header An array consisting of the header columns.
     * @return True if it is a valid header, false otherwise.
     */
    private static boolean validateHeader(String[] header)
    {
        if(!header[0].equals("Qualifikation"))
        {
            return false;
        }
        if(!header[1].equals("Schnitt"))
        {
            return false;
        }
        if(!header[2].equals("Bundesland"))
        {
            return false;
        }
        if(!header[3].equals("Mathe"))
        {
            return false;
        }
        if(!header[4].equals("Physik"))
        {
            return false;
        }
        if(!header[5].equals("Deutsch"))
        {
            return false;
        }
        if(!header[6].equals("Schultyp"))
        {
            return false;
        }
        if(!header[7].equals("OLT-Mathe"))
        {
            return false;
        }
        if(!header[8].equals("OLT-Deutsch"))
        {
            return false;
        }
        if(!(header[9].equals("Studierfaehigkeitstest") || header[9].equals("Studierfähigkeitstest")))
        {
            return false;
        }
        if(!header[10].equals("Alter"))
        {
            return false;
        }
        if (!header[11].equals("Geschlecht"))
        {
            return false;
        }
        if (!header[12].equals("Jahreseinkommen der Eltern"))
        {
            return false;
        }
        if ((!header[13].equals("Staatsbuergerschaft") || header[9].equals("Staatsbürgerschaft")))
        {
            return false;
        }
        if (!header[14].equals("Studiengang"))
        {
            return false;
        }
        if (!header[15].equals("Zwischenkalk"))
        {
            return false;
        }
        if (!header[16].equals("Abschluss"))
        {
            return false;
        }
        return true;
    }

    /**
     * This function is cleaning and validating the classification column.
     * @param qualification The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanQualification(String qualification) throws WhatToStudyException
    {
        if(qualification.equals("Abitur") || qualification.equals("Techniker"))
        {
            return qualification;
        } else if(qualification.equals("FH Reife"))
        {
            return "FH_Reife";
        } else
        {
            throw new WhatToStudyException("Error validating and cleaning qualification column in line " + currentLineNumber);
        }
    }

    /**
     * This function is cleaning and validating the average column.
     * @param averageString The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanAverage(String averageString) throws WhatToStudyException
    {
        try
        {
            double average = nf.parse(averageString).doubleValue();
            if(average < 2.0)
            {
                return "Very_Good";
            } else if(average < 3.0)
            {
                return "Good";
            } else if(average < 4.0)
            {
                return "Satisfying";
            } else if(average >= 4.0)
            {
                return "Failed";
            } else
            {
                throw new WhatToStudyException("Unable to parse average in line " + currentLineNumber);
            }
        } catch (ParseException e)
        {
            throw new WhatToStudyException("Unable to parse average in line " + currentLineNumber);
        }
    }

    /**
     * This function is cleaning and validating the state column.
     * @param state The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanState(String state) throws WhatToStudyException
    {
        if(state.equals("Baden-Wuerttemberg") || state.equals("Baden-Württemberg"))
        {
            return "BW";
        } else if(state.equals("Bayern"))
        {
            return "BY";
        } else if(state.equals("Nordrhein-Westfalen"))
        {
            return "NRW";
        } else if(state.equals("Bremen"))
        {
            return "HB";
        } else if(state.equals("Sachsen"))
        {
            return "SN";
        } else if(state.equals("Thueringen") || state.equals("Thüringen"))
        {
            return "TH";
        } else if(state.equals("Hessen"))
        {
            return "HE";
        } else if(state.equals("Mecklenburg-Vorpommern"))
        {
            return "MV";
        } else if(state.equals("Berlin"))
        {
            return "BE";
        } else if(state.equals("Rheinland-Pfalz"))
        {
            return "RP";
        } else if(state.equals("Hamburg"))
        {
            return "HH";
        } else if(state.equals("Sachsen-Anhalt"))
        {
            return "ST";
        } else if(state.equals("Niedersachsen"))
        {
            return "NI";
        } else if(state.equals("Brandenburg"))
        {
            return "BB";
        } else if(state.equals("Saarland"))
        {
            return "SL";
        } else if(state.equals("Schleswig-Holstein"))
        {
            return "SH";
        } else
        {
            throw new WhatToStudyException("Error validating and cleaning state column in line " + currentLineNumber);
        }
    }

    /**
     * This function is cleaning and validating the math column.
     * @param mathString The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanMath(String mathString) throws WhatToStudyException
    {
        if(mathString.equals("keine"))
        {
            return "NA";
        } else
        {
            try
            {
                double math = nf.parse(mathString).doubleValue();
                if (math < 2.0)
                {
                    return "Very_Good";
                } else if (math < 3.0)
                {
                    return "Good";
                } else if (math < 4.0)
                {
                    return "Satisfying";
                } else if (math >= 4.0)
                {
                    return "Failed";
                } else
                {
                    throw new WhatToStudyException("Unable to parse math grade in line " + currentLineNumber);
                }
            } catch (ParseException e)
            {
                throw new WhatToStudyException("Unable to parse math grade in line " + currentLineNumber);
            }
        }
    }

    /**
     * This function is cleaning and validating the physic column.
     * @param physicsString The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanPhysics(String physicsString) throws WhatToStudyException
    {
        if(physicsString.equals("keine"))
        {
            return "NA";
        } else
        {
            try
            {
                double physics = nf.parse(physicsString).doubleValue();
                if (physics < 2.0)
                {
                    return "Very_Good";
                } else if (physics < 3.0)
                {
                    return "Good";
                } else if (physics < 4.0)
                {
                    return "Satisfying";
                } else if (physics >= 4.0)
                {
                    return "Failed";
                } else
                {
                    throw new WhatToStudyException("Unable to parse physics grade in line " + currentLineNumber);
                }
            } catch (ParseException e)
            {
                throw new WhatToStudyException("Unable to parse physics grade in line " + currentLineNumber);
            }
        }
    }

    /**
     * This function is cleaning and validating the German column.
     * @param germanString The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanGerman(String germanString) throws WhatToStudyException
    {
        if(germanString.equals("keine"))
        {
            return "NA";
        } else
        {
            try
            {
                double german = nf.parse(germanString).doubleValue();
                if (german < 2.0)
                {
                    return "Very_Good";
                } else if (german < 3.0)
                {
                    return "Good";
                } else if (german < 4.0)
                {
                    return "Satisfying";
                } else if (german >= 4.0)
                {
                    return "Failed";
                } else
                {
                    throw new WhatToStudyException("Unable to parse German grade in line " + currentLineNumber);
                }
            } catch (ParseException e)
            {
                throw new WhatToStudyException("Unable to parse German grade in line " + currentLineNumber);
            }
        }
    }

    /**
     * This function is cleaning and validating the school type column.
     * @param schoolType The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanSchoolType(String schoolType) throws WhatToStudyException
    {
        if(schoolType.equals("Allgemeinbildendes Gymnasium"))
        {
            return "A_Gymnasium";
        } else if(schoolType.equals("Gesamtschule"))
        {
            return "Gesamtschule";
        } else if(schoolType.equals("Technisches Gymnasium"))
        {
            return "T_Gymnasium";
        } else if(schoolType.equals("Wirtschaftsgymnasium"))
        {
            return "W_Gymnasium";
        } else if(schoolType.equals("n.a."))
        {
            return "NA";
        } else
        {
            throw new WhatToStudyException("Error validating and cleaning school type column in line " + currentLineNumber);
        }
    }

    /**
     * This function is cleaning and validating the online test - math column.
     * The points are converted to grades using the following pattern: 50% == 4.0, 100% == 1.0
     * @param oltMath The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanOLTMath(String oltMath) throws WhatToStudyException
    {
        try
        {
            int math = Integer.parseInt(oltMath);
            if (math > 84)
            {
                return "Very_Good";
            } else if (math > 67)
            {
                return "Good";
            } else if (math > 50)
            {
                return "Satisfying";
            } else if (math <= 50)
            {
                return "Failed";
            } else
            {
                throw new WhatToStudyException("Unable to parse online test math grade in line " + currentLineNumber);
            }
        } catch (NumberFormatException e)
        {
            throw new WhatToStudyException("Unable to parse online test math grade in line " + currentLineNumber);
        }
    }

    /**
     * This function is cleaning and validating the online test - German column.
     * The points are converted to grades using the following pattern: 50% == 4.0, 100% == 1.0
     * @param oltGerman The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanOLTGerman(String oltGerman) throws WhatToStudyException
    {
        try
        {
            int german = Integer.parseInt(oltGerman);
            if (german > 84)
            {
                return "Very_Good";
            } else if (german > 67)
            {
                return "Good";
            } else if (german > 50)
            {
                return "Satisfying";
            } else if (german <= 50)
            {
                return "Failed";
            } else
            {
                throw new WhatToStudyException("Unable to parse online test German grade in line " + currentLineNumber);
            }
        } catch (NumberFormatException e)
        {
            throw new WhatToStudyException("Unable to parse online test German grade in line " + currentLineNumber);
        }
    }

    /**
     * This function is cleaning and validating the study ability test column.
     * The points are converted to grades using the following pattern: 50% == 4.0, 100% == 1.0
     * @param studyAbilityTest The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanStudyAbilityTest(String studyAbilityTest) throws WhatToStudyException
    {
        if(studyAbilityTest.equals("n.a."))
        {
            return "NA";
        } else
        {
            try
            {
                int studyAbility = Integer.parseInt(studyAbilityTest);
                if (studyAbility > 840)
                {
                    return "Very_Good";
                } else if (studyAbility > 670)
                {
                    return "Good";
                } else if (studyAbility > 500)
                {
                    return "Satisfying";
                } else if (studyAbility <= 500)
                {
                    return "Failed";
                } else
                {
                    throw new WhatToStudyException("Unable to parse study ability test grade in line " + currentLineNumber);
                }
            } catch (NumberFormatException e)
            {
                throw new WhatToStudyException("Unable to parse study ability test grade in line " + currentLineNumber);
            }
        }
    }

    /**
     * This function is cleaning and validating the age column.
     * The age is converted using the following pattern: young == < 18, average == < 23, old == > 23
     * @param ageString The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanAge(String ageString) throws WhatToStudyException
    {
        try
        {
            int age = Integer.parseInt(ageString);
            if (age < 18)
            {
                return "Young";
            } else if (age < 23)
            {
                return "Average";
            } else if (age >= 23)
            {
                return "Old";
            } else
            {
                throw new WhatToStudyException("Unable to parse age in line " + currentLineNumber);
            }
        } catch (NumberFormatException e)
        {
            throw new WhatToStudyException("Unable to parse age in line " + currentLineNumber);
        }
    }

    /**
     * This function is cleaning and validating the sex column.
     * @param sex The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanSex(String sex) throws WhatToStudyException
    {
        if(sex.equalsIgnoreCase("m"))
        {
            return "M";
        } else if(sex.equalsIgnoreCase("w"))
        {
            return "W";
        } else
        {
            throw new WhatToStudyException("Error validating and cleaning sex type column in line " + currentLineNumber);
        }
    }

    /**
     * This function is cleaning and validating the income column.
     * The income is converted according the World Bank analysis of 2013: Low == < 12.540, Low_Middle == < 49.500 High_Middle == < 152.940, High == > 152.940
     * @param incomeString The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanIncome(String incomeString) throws WhatToStudyException
    {
        try
        {
            int income = Integer.parseInt(incomeString);
            if (income < 12540)
            {
                return "Low";
            } else if (income < 49500)
            {
                return "Low_Middle";
            } else if (income < 152940)
            {
                return "High_Middle";
            } else if (income >= 152940)
            {
                return "High";
            } else
            {
                throw new WhatToStudyException("Unable to parse income in line " + currentLineNumber);
            }
        } catch (NumberFormatException e)
        {
            throw new WhatToStudyException("Unable to parse income in line " + currentLineNumber);
        }
    }

    /**
     * This function is cleaning and validating the nationality column.
     * @param nationality The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanNationality(String nationality) throws WhatToStudyException
    {
        if(nationality.equals("deutsch"))
        {
            return "German";
        } else if(nationality.equals("EU Buerger") || nationality.equals("EU Bürger"))
        {
            return "EU";
        } else if(nationality.equals("Non European"))
        {
            return "Non_EU";
        } else
        {
            throw new WhatToStudyException("Error validating and cleaning nationality column in line " + currentLineNumber);
        }
    }

    /**
     * This function is cleaning and validating the course column.
     * @param course The input String.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanCourse(String course) throws WhatToStudyException
    {
        if(course.equals("Elektrotechnik"))
        {
            return "E_Engineering";
        } else if(course.equals("Informatik"))
        {
            return "C_Science";
        } else if(course.equals("Maschinenbau"))
        {
            return "Engineering";
        } else if(course.equals("Soziale Arbeit"))
        {
            return "S_Work";
        } else if(course.equals("Wirtschaftswissenschaften"))
        {
            return "Economics";
        } else
        {
            throw new WhatToStudyException("Error validating and cleaning course column in line " + currentLineNumber);
        }
    }

    /**
     * This function is cleaning and validating the final grade column.
     * @param calc The input String from the 16th column.
     * @param grade The input String from the 17th column.
     * @return The cleaned String.
     * @throws NeticaException If the input does not fit the requirements.
     */
    private static String cleanFinalGrade(String calc, String grade) throws WhatToStudyException
    {
        try
        {
            double finalGrade = nf.parse(calc).doubleValue();
            if (finalGrade < 2.0)
            {
                return "Very_Good";
            } else if (finalGrade < 3.0)
            {
                return "Good";
            } else if (finalGrade <= 4.0)
            {
                return "Satisfying";
            } else if (finalGrade > 4.0 && grade.equals("abgebrochen"))
            {
                return "Failed";
            } else
            {
                throw new WhatToStudyException("Unable to parse final grade in line " + currentLineNumber + ", maybe the last two columns do not match");
            }
        } catch (ParseException e)
        {
            throw new WhatToStudyException("Unable to parse final grade in line " + currentLineNumber);
        }
    }
}

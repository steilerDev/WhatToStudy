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
import de.steilerdev.whatToStudy.Utility.Case.*;
import de.steilerdev.whatToStudy.Utility.Case.Math;
import norsys.netica.Environ;
import norsys.netica.NeticaException;
import norsys.netica.Streamer;

import java.io.*;
import java.util.ArrayList;

/**
 * This class converts a input stream, or a CSV file to be usable by Netica (Respecting Netica Constraints and converting continuous values into discrete ones).
 * The input and output specifications are given by the respective enumeration classes.<br>
 * The columns need to be in the following order (Depending on which function is used the 16th and 17th column can be dropped):
 * <ol>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.Qualification Qualification}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.QualificationAverage Qualification average}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.State State}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.Math Math}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.Physics Physics}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.German German}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.SchoolType School type}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.OLTMath Online test - Math}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.OLTGerman Online test - German}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.StudyAbilityTest Study ability test}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.Age Age}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.Sex Sex}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.ParentalIncome Parental income}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.Nationality Nationality}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.Course Course}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.FinalGrade Intermediate Calculation}</li>
 *      <li>{@link de.steilerdev.whatToStudy.Utility.Case.FinalGrade Final Grade}</li>
 * </ol>
 */
public class CSVStreamer
{
    //Specific options for the CVS file.
    /**
     * The String that is separating the CSV,
     */
    public static String cvsSplitBy = ";";

    /**
     * Counting current line number of input file for sophisticated error message.
     */
    public static int currentLineNumber = 0;

    /**
     * This function is converting the file format specified by the exercise, cleans the values and returns a unified Streamer, usable by NeticaJ.<br>
     * The first line needs to be a valid header specified within the {@link de.steilerdev.whatToStudy.Utility.Case case enumerations}. The order is specified within the class description of this class. <br>
     * The values are validated and converted according to the specification stated in the {@link de.steilerdev.whatToStudy.Utility.Case case enumerations} and the order is specified within the class description of this class.
     * @param inputFile The string representing the path to the input file.
     * @param streamName The name of the Streamer used by Netica.
     * @param env The current Environment.
     * @return A Streamer object usable by NeticaJ
     * @throws WhatToStudyException If an error occurs.
     */
    public static Streamer getNeticaStream(String inputFile, String streamName, Environ env) throws WhatToStudyException
    {
        try
        {
            File initialFile = new File(inputFile);
            InputStream targetStream = new FileInputStream(initialFile);
            return getNeticaStream(targetStream, streamName, env);
        } catch (FileNotFoundException e)
        {
            throw new WhatToStudyException("Unable to find the specified file " + inputFile);
        }
    }

    /**
     * This function is converting the file format specified by the exercise, cleans the values and returns a unified Streamer, usable by NeticaJ. <br>
     * The first line needs to be a valid header specified within the {@link de.steilerdev.whatToStudy.Utility.Case case enumerations}. The order is specified within the class description of this class. <br>
     * The values are validated and converted according to the specification stated in the {@link de.steilerdev.whatToStudy.Utility.Case case enumerations} and the order is specified within the class description of this class.
     * @param inputStream The input file as stream.
     * @param streamName The name of the Streamer used by Netica.
     * @param env The current Environment.
     * @return A Streamer object usable by NeticaJ
     * @throws WhatToStudyException If an error occurs.
     */
    public static Streamer getNeticaStream(InputStream inputStream, String streamName, Environ env) throws WhatToStudyException
    {
        PrintStream fileStream = null;
        BufferedReader br = null;
        try
        {
            //Temp file, storing the converted information. (Workaround for creating an InputStream from an OutputStream)
            File file = new File(".tempData");
            if (!file.exists())
            {
                file.createNewFile();
            }
            //Deleting temp file after finishing.
            file.deleteOnExit();

            //The print stream used to write to the temp file
            fileStream = new PrintStream(file);

            //Printing the cleaned header
            fileStream.print(Qualification.getHeader() + " ");
            fileStream.print(QualificationAverage.getHeader() + " ");
            fileStream.print(State.getHeader() + " ");
            fileStream.print(Math.getHeader() + " ");
            fileStream.print(Physics.getHeader() + " ");
            fileStream.print(German.getHeader() + " ");
            fileStream.print(SchoolType.getHeader() + " ");
            fileStream.print(OLTMath.getHeader() + " ");
            fileStream.print(OLTGerman.getHeader() + " ");
            fileStream.print(StudyAbilityTest.getHeader() + " ");
            fileStream.print(Age.getHeader() + " ");
            fileStream.print(Sex.getHeader() + " ");
            fileStream.print(ParentalIncome.getHeader() + " ");
            fileStream.print(Nationality.getHeader() + " ");
            fileStream.print(Course.getHeader() + " ");
            fileStream.print(FinalGrade.getHeader() + "\n");

            //Getting a list of all cases of the file
            ArrayList<Case> cases = getCaseList(inputStream);

            //Adding a new line for each case
            for(Case currentCase: cases)
            {
                fileStream.println(currentCase.toString());
            }

            fileStream.flush();

            FileInputStream in = new FileInputStream(file);

            return new Streamer(in, streamName, env);
        } catch (IOException e)
        {
            throw new WhatToStudyException("An IO related error occurred during the creation of the Netica stream.");
        } catch(NeticaException e)
        {
            throw new WhatToStudyException("A Netica related error occurred during the creation of the Netica stream.");
        } finally
        {
            if(fileStream != null)
            {
                fileStream.close();
            }
            if(br != null)
            {
                try
                {
                    br.close();
                } catch (IOException e)
                {
                    throw new WhatToStudyException("An IO related error occurred during the creation of the Netica stream.");
                }
            }
        }
    }

    /**
     * This function is creating a list of cases from an input file.<br>
     * The first line needs to be a valid header specified within the {@link de.steilerdev.whatToStudy.Utility.Case case enumerations}. The order is specified within the class description of this class. <br>
     * The values are validated and converted according to the specification stated in the {@link de.steilerdev.whatToStudy.Utility.Case case enumerations} and the order is specified within the class description of this class.
     * @param inputFile The file name of the input file
     * @return A list of cases.
     * @throws WhatToStudyException If an error occurs.
     */
    public static ArrayList<Case> getCaseList(String inputFile) throws WhatToStudyException
    {
        try
        {
            File initialFile = new File(inputFile);
            InputStream targetStream = new FileInputStream(initialFile);
            return getCaseList(targetStream);
        } catch (FileNotFoundException e)
        {
            throw new WhatToStudyException("Unable to find the specified file " + inputFile);
        }
    }

    /**
     * This function is creating a list of cases from an Input Stream.<br>
     * The first line needs to be a valid header specified within the {@link de.steilerdev.whatToStudy.Utility.Case case enumerations}. The order is specified within the class description of this class. <br>
     * The values are validated and converted according to the specification stated in the {@link de.steilerdev.whatToStudy.Utility.Case case enumerations} and the order is specified within the class description of this class.
     * @param inputStream The input stream that is read by the function
     * @return A list of cases.
     * @throws WhatToStudyException If an error occurs.
     */
    public static ArrayList<Case> getCaseList(InputStream inputStream) throws WhatToStudyException
    {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)))
        {
            ArrayList<Case> caseList = new ArrayList<>();
            String line;

            //Parsing and validating header
            if ((line = br.readLine()) != null)
            {
                currentLineNumber++;
                String[] header = line.split(cvsSplitBy);
                if (!validateHeader(header, false))
                {
                    throw new WhatToStudyException("The header of the specific file does not meet the stated requirements. See help page for more information");
                }
            }

            while ((line = br.readLine()) != null)
            {
                currentLineNumber++;
                caseList.add(getCase(line));
            }
            return caseList;
        } catch (IOException e)
        {
            throw new WhatToStudyException("Error occurred while reading the input stream.");
        }
    }

    /**
     * This function creates a case using the case line. The line needs to have all 17 columns separated by the cvsSplitBy character.<br>
     * The values are validated and converted according to the specification stated in the {@link de.steilerdev.whatToStudy.Utility.Case case enumerations} and the order is specified within the class description of this class.
     * @param caseLine The source line used to create a case.
     * @return The case described inside the case line.
     * @throws WhatToStudyException If an error occurs.
     */
    public static Case getCase(String caseLine) throws WhatToStudyException
    {
        return getCase(caseLine, false);
    }

    /**
     * This function reads a file, that contains a case used for evaluation, concluding the 16th and 17th column are not mandatory.<br>
     * The values are validated and converted according to the specification stated in the {@link de.steilerdev.whatToStudy.Utility.Case case enumerations} and the order is specified within the class description of this class.<br>
     * If the are more than one case within the file, only the first case is returned.
     * @param inputFile The file name of the source file.
     * @return The Case object storing the evaluation case.
     * @throws WhatToStudyException If an error occurs.
     */
    public static Case getEvaluationCase(String inputFile) throws WhatToStudyException
    {
        try
        {
            File initialFile = new File(inputFile);
            InputStream inputStream = new FileInputStream(initialFile);

            try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)))
            {
                String line;

                //Parsing and validating header
                if ((line = br.readLine()) != null)
                {
                    currentLineNumber++;
                    String[] header = line.split(cvsSplitBy);
                    if (!validateHeader(header, true))
                    {
                        throw new WhatToStudyException("The header of the specific file does not meet the stated requirements. See help page for more information");
                    }
                }

                if ((line = br.readLine()) != null)
                {
                    currentLineNumber++;
                    return getCase(line, true);
                } else
                {
                    throw new WhatToStudyException("No line to read from.");
                }
            } catch (IOException e)
            {
                throw new WhatToStudyException("Error reading input stream.");
            }
        } catch (FileNotFoundException e)
        {
            throw new WhatToStudyException("Unable to find the specified file " + inputFile);
        }
    }

    /**
     * This function creates a case using the case line.<br>
     * The values are validated and converted according to the specification stated in the {@link de.steilerdev.whatToStudy.Utility.Case case enumerations} and the order is specified within the class description of this class.<br>
     * The line either needs to have all 17 columns separated by the cvsSplitBy character, or only the first 15 if the skipNonMandatory flag is set.
     * @param caseLine The source line used to create a case.
     * @param skipNonMandatory If true only the first 15 columns are required, otherwise all 17 are mandatory (Order specified above)
     * @return The case described inside the case line.
     * @throws WhatToStudyException If an error occurs.
     */
    private static Case getCase(String caseLine, boolean skipNonMandatory) throws WhatToStudyException
    {
        String[] lineArray = caseLine.split(cvsSplitBy);
        Case currentCase = new Case();

        if (!(lineArray.length == 17 || (skipNonMandatory && (lineArray.length == 14 || lineArray.length == 15))))
        {
            throw new WhatToStudyException("Invalid number of columns in line " + currentLineNumber);
        }

        // If all columns are mandatory
        if (lineArray.length == 17)
        {
            currentCase.setCourse(Course.clean(lineArray[14].trim()));
            currentCase.setFinalGrade(FinalGrade.clean(lineArray[15].trim(), lineArray[16].trim()));
        } else if (lineArray.length == 15)
        {
            currentCase.setCourse(Course.clean(lineArray[14].trim()));
            currentCase.setFinalGrade(null);
        } else
        {
            currentCase.setCourse(null);
            currentCase.setFinalGrade(null);
        }

        try
        {
            currentCase.setQualification(Qualification.clean(lineArray[0].trim()));
            currentCase.setQualificationAverage(QualificationAverage.clean(lineArray[1].trim()));
            currentCase.setState(State.clean(lineArray[2].trim()));
            currentCase.setMath(Math.clean(lineArray[3].trim()));
            currentCase.setPhysics(Physics.clean(lineArray[4].trim()));
            currentCase.setGerman(German.clean(lineArray[5].trim()));
            currentCase.setSchoolType(SchoolType.clean(lineArray[6].trim()));
            currentCase.setOLTMath(OLTMath.clean(lineArray[7].trim()));
            currentCase.setOLTGerman(OLTGerman.clean(lineArray[8].trim()));
            currentCase.setStudyAbilityTest(StudyAbilityTest.clean(lineArray[9].trim()));
            currentCase.setAge(Age.clean(lineArray[10].trim()));
            currentCase.setSex(Sex.clean(lineArray[11].trim()));
            currentCase.setParentalIncome(ParentalIncome.clean(lineArray[12].trim()));
            currentCase.setNationality(Nationality.clean(lineArray[13].trim()));
        } catch (WhatToStudyException e)
        {
            throw new WhatToStudyException(e.getMessage() + " in line " + currentLineNumber);
        }

        return currentCase;
    }

    /**
     * This function is checking if the header of the CSV fits the specifications, stated within the enumerations.
     * @param header An array consisting of the header columns.
     * @param skipNonMandatory Skips the last two columns if they are non mandatory.
     * @return True if it is a valid header, false otherwise.
     */
    private static boolean validateHeader(String[] header, boolean skipNonMandatory)
    {
        if (!(header.length == 17 || (skipNonMandatory && (header.length == 14 || header.length == 15))))
        {
            return false;
        }
        if(!Qualification.validateHeader(header[0]))
        {
            return false;
        }
        if(!QualificationAverage.validateHeader(header[1]))
        {
            return false;
        }
        if(!State.validateHeader(header[2]))
        {
            return false;
        }
        if(!Math.validateHeader(header[3]))
        {
            return false;
        }
        if(!Physics.validateHeader(header[4]))
        {
            return false;
        }
        if(!German.validateHeader(header[5]))
        {
            return false;
        }
        if(!SchoolType.validateHeader(header[6]))
        {
            return false;
        }
        if(!OLTMath.validateHeader(header[7]))
        {
            return false;
        }
        if(!OLTGerman.validateHeader(header[8]))
        {
            return false;
        }
        if(!StudyAbilityTest.validateHeader(header[9]))
        {
            return false;
        }
        if(!Age.validateHeader(header[10]))
        {
            return false;
        }
        if (!Sex.validateHeader(header[11]))
        {
            return false;
        }
        if (!ParentalIncome.validateHeader(header[12]))
        {
            return false;
        }
        if (!Nationality.validateHeader(header[13]))
        {
            return false;
        }
        if (!(skipNonMandatory || Course.validateHeader(header[14])))
        {
            return false;
        }
        if (!(skipNonMandatory || FinalGrade.validateHeader(header[15])))
        {
            return false;
        }
        if (!(skipNonMandatory || FinalGrade.validateHeader(header[16])))
        {
            return false;
        }
        return true;
    }
}

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
package de.steilerdev.whatToStudy.Utility.Case;

import de.steilerdev.whatToStudy.Exception.WhatToStudyException;
import de.steilerdev.whatToStudy.Main;

import javax.swing.text.html.Option;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Optional;

/**
 * This enumeration contains all specification for the final grade column of a case.
 */
public enum FinalGrade
{
    VERY_GOOD
    {
        /**
         * A nice formatted output handed over to Netica to evaluate it.
         * @return The name of this enum constant used by Netica
         */
        @Override
        public String toString()
        {
            return "Very_Good";
        }
    },
    GOOD
    {
        /**
         * A nice formatted output handed over to Netica to evaluate it.
         * @return The name of this enum constant used by Netica
         */
        @Override
        public String toString()
        {
            return "Good";
        }
    },
    SATISFYING
    {
        /**
         * A nice formatted output handed over to Netica to evaluate it.
         * @return The name of this enum constant used by Netica
         */
        @Override
        public String toString()
        {
            return "Satisfying";
        }
    },
    FAILED
    {
        /**
         * A nice formatted output handed over to Netica to evaluate it.
         * @return The name of this enum constant used by Netica
         */
        @Override
        public String toString()
        {
            return "Failed";
        }
    };

    /**
     * Creates the header for the final grade column used by Netica.
     * @return The header for the final grade column used by Netica
     */
    public static String getHeader()
    {
        return "Final_Grade";
    }

    /**
     * A list of valid headers accepted from an input file.
     * These include: Zwischenkalk, Abschluss, Final_Grade
     * @return An array of Strings that are considered as a valid header.
     */
    public static String[] getValidHeaders()
    {
        return new String[]{"Zwischenkalk", "Abschluss", getHeader()};
    }

    /**
     * Validates the stated string against the stored valid header strings.
     * @param header The header read from a file
     * @return True if the header is considered as valid, false otherwise.
     */
    public static boolean validateHeader(String header)
    {
        return Arrays.stream(getValidHeaders()).anyMatch(value -> value.equals(header));
    }

    /**
     * This function is cleaning and validating a String for the course property, to enable its use within the network.
     * The two strings need to be equal or only the second needs to be null
     * @param calc The input String from the Zwischenkalk column, being a floating point number within the range 1.0 to 6.0, or one of the following: Very_Good, Good, Satisfying, Failed (grade is ignored if calc is a non-floating point String)
     * @param grade The input String from the Abschluss column, being a floating point number within the range 1.0 to 6.0, abgebrochen or null. If non-null it needs to match calc.
     * @return The appropriate enumeration.
     * @throws WhatToStudyException If the input does not fit the requirements.
     */
    public static FinalGrade clean(String calc, String grade) throws WhatToStudyException
    {
        try
        {
            double finalGrade = Main.localizedNumberFormat.parse(calc).doubleValue();
            if((grade == null && calc != null) || (calc != null && calc.equals(grade)))
            {
                if (finalGrade < 2.0)
                {
                    return FinalGrade.VERY_GOOD;
                } else if (finalGrade < 3.0)
                {
                    return FinalGrade.GOOD;
                } else if (finalGrade <= 4.0)
                {
                    return FinalGrade.SATISFYING;
                } else if (finalGrade > 4.0)
                {
                    return FinalGrade.FAILED;
                } else
                {
                    throw new WhatToStudyException("Unable to parse final grade");
                }
            } else if (finalGrade > 4.0 && grade.equals("abgebrochen"))
            {
                return FinalGrade.FAILED;
            }  else
            {
                throw new WhatToStudyException("Unable to parse final grade");
            }
        } catch (ParseException e)
        {
            //Check if the input is already a cleaned value
            Optional<FinalGrade> currentValue;
            if((currentValue = Arrays.stream(FinalGrade.values()).parallel().filter(value -> value.toString().equals(calc)).findFirst()).isPresent())
            {
                return currentValue.get();
            } else
            {
                throw new WhatToStudyException("Unable to parse final grade");
            }
        }
    }
}

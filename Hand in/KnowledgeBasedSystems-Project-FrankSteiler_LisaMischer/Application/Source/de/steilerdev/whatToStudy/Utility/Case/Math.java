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

import java.text.ParseException;
import java.util.Arrays;
import java.util.Optional;

/**
 * This enumeration contains all specification for the math column of a case.
 */
public enum Math
{
    /**
     * This value is representing a very good grade (1.0 - 2.0) in math and is converted to the Netica compliant String "Very_Good".
     */
    VERY_GOOD
    {
        @Override
        public String toString()
        {
            return "Very_Good";
        }
    },
    /**
     * This value is representing a good grade (2.0 - 3.0) in math and is converted to the Netica compliant String "Good".
     */
    GOOD
    {
        @Override
        public String toString()
        {
            return "Good";
        }
    },
    /**
     * This value is representing a satisfying grade (3.0 - 4.0) in math and is converted to the Netica compliant String "Satisfying".
     */
    SATISFYING
    {
        @Override
        public String toString()
        {
            return "Satisfying";
        }
    },
    /**
     * This value is representing a failed (&gt; 4.0) math grade and is converted to the Netica compliant String "Failed".
     */
    FAILED
    {
        @Override
        public String toString()
        {
            return "Failed";
        }
    },
    /**
     * This value is representing a math grade that is not available and is converted to the Netica compliant String "NA".
     */
    NA
    {
        @Override
        public String toString()
        {
            return "NA";
        }
    };

    /**
     * Creates the header for the math column used by Netica.
     * @return The header used by Netica: "Math"
     */
    public static String getHeader()
    {
        return "Math";
    }

    /**
     * A list of valid headers accepted from an input .
     * These include: Mathe, Math
     * @return An array of Strings that are considered as a valid header.
     */
    public static String[] getValidHeaders()
    {
        return new String[]{"Mathe", getHeader()};
    }

    /**
     * Validates the stated String against the specified {@link #getValidHeaders valid header strings}.
     * @see #getValidHeaders
     * @param header The header read from a file
     * @return True if the header is considered valid, false otherwise.
     */
    public static boolean validateHeader(String header)
    {
        return Arrays.stream(getValidHeaders()).anyMatch(value -> value.equals(header));
    }

    /**
     * This function is cleaning and validating a String for the math property, to enable its use within the network.
     * @param mathString The input String, being an floating point number within the range 1.0 to 6.0, "keine" or one of the following: Very_Good, Good, Satisfying, Failed, NA.
     * @return The appropriate enumeration.
     * @throws WhatToStudyException If the input does not fit the requirements.
     */
    public static Math clean(String mathString) throws WhatToStudyException
    {
        if(mathString.equals("keine"))
        {
            return Math.NA;
        } else
        {
            try
            {
                double math = Main.localizedNumberFormat.parse(mathString).doubleValue();
                if (math < 2.0)
                {
                    return Math.VERY_GOOD;
                } else if (math < 3.0)
                {
                    return Math.GOOD;
                } else if (math < 4.0)
                {
                    return Math.SATISFYING;
                } else if (math >= 4.0)
                {
                    return Math.FAILED;
                } else
                {
                    throw new WhatToStudyException("Unable to parse math grade");
                }
            } catch (ParseException e)
            {
                //Check if the input is already a cleaned value
                Optional<Math> currentValue;
                if((currentValue = Arrays.stream(Math.values()).parallel().filter(value -> value.toString().equals(mathString)).findFirst()).isPresent())
                {
                    return currentValue.get();
                } else
                {
                    throw new WhatToStudyException("Unable to parse math grade");
                }
            }
        }
    }
}

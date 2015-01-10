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
 * This enumeration contains all specification for the qualification average column of a case.
 */
public enum QualificationAverage
{
    /**
     * This value is representing a very good grade (1.0 - 2.0) in the German online test and is converted to the Netica compliant String "Very_Good".
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
     * This value is representing a good grade (2.0 - 3.0) in the German online test and is converted to the Netica compliant String "Good".
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
     * This value is representing a satisfying grade (3.0 - 4.0) in the German online test and is converted to the Netica compliant String "Satisfying".
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
     * This value is representing a failed (&gt; 4.0) German online test and is converted to the Netica compliant String "Failed".
     */
    FAILED
    {
        @Override
        public String toString()
        {
            return "Failed";
        }
    };

    /**
     * Creates the header for the qualification average column used by Netica.
     * @return The header used by Netica: "Qualification_Average"
     */
    public static String getHeader()
    {
        return "Qualification_Average";
    }

    /**
     * A list of valid headers accepted from an input.
     * These include: Schnitt, Qualification_Average
     * @return An array containing Strings that are considered as valid headers.
     */
    public static String[] getValidHeaders()
    {
        return new String[]{"Schnitt", getHeader()};
    }

    /**
     * Validates the Stated string against the specified {@link #getValidHeaders valid header strings}.
     * @see #getValidHeaders
     * @param header The header read from a file
     * @return True if the header is considered valid, false otherwise.
     */
    public static boolean validateHeader(String header)
    {
        return Arrays.stream(getValidHeaders()).anyMatch(value -> value.equals(header));
    }

    /**
     * This function is cleaning and validating a String for the qualification average property, to enable its use within the network.
     * @param qualificationAverageString The input String, being an floating point number within the range 1.0 to 6.0, or one of the following: Very_Good, Good, Satisfying, Failed.
     * @return The appropriate enumeration.
     * @throws WhatToStudyException If the input does not fit the requirements.
     */
    public static QualificationAverage clean(String qualificationAverageString) throws WhatToStudyException
    {
        try
        {
            double qualificationAverage = Main.localizedNumberFormat.parse(qualificationAverageString).doubleValue();
            if (qualificationAverage < 2.0)
            {
                return QualificationAverage.VERY_GOOD;
            } else if (qualificationAverage < 3.0)
            {
                return QualificationAverage.GOOD;
            } else if (qualificationAverage < 4.0)
            {
                return QualificationAverage.SATISFYING;
            } else if (qualificationAverage >= 4.0)
            {
                return QualificationAverage.FAILED;
            } else
            {
                throw new WhatToStudyException("Unable to parse the qualification average");
            }
        } catch (ParseException e)
        {
            //Check if the input is already a cleaned value
            Optional<QualificationAverage> currentValue;
            if((currentValue = Arrays.stream(QualificationAverage.values()).parallel().filter(value -> value.toString().equals(qualificationAverageString)).findFirst()).isPresent())
            {
                return currentValue.get();
            } else
            {
                throw new WhatToStudyException("Unable to parse the qualification average");
            }
        }
    }
}

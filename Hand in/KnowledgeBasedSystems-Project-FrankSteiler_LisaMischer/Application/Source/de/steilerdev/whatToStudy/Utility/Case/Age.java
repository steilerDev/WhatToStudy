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

import java.util.Arrays;
import java.util.Optional;

/**
 * This enumeration contains all specification for the age column of a case.
 */
public enum Age
{
    /**
     * This value is representing a young person (In this context: Younger than 18) and is converted to the Netica compliant String "Young".
     */
    YOUNG
    {
        @Override
        public String toString()
        {
            return "Young";
        }
    },
    /**
     * This value is representing an average old person (In this context: Older than 18 and younger than 23) and is converted to the Netica compliant String "Average".
     */
    AVERAGE
    {
        @Override
        public String toString()
        {
            return "Average";
        }
    },
    /**
     * This value is representing an old person (In this context: Older than 22) and is converted to the Netica compliant String "Old".
     */
    OLD
    {
        @Override
        public String toString()
        {
            return "Old";
        }
    };

    /**
     * Creates the header for the Age column used by Netica.
     * @return The header used by Netica: "Age"
     */
    public static String getHeader()
    {
        return "Age";
    }

    /**
     * A list of valid headers accepted from an input.
     * These include: Alter, Age
     * @return An array of Strings that are considered as a valid header.
     */
    public static String[] getValidHeaders()
    {
        return new String[]{"Alter", getHeader()};
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
     * This function is cleaning and validating a String for the age property, to enable its use within the network.
     * Every integer smaller than 18 is considered young, smaller than 23 is considered average and bigger or equal 23 is considered old.
     * @param ageString The input String, being one of the following: Young, Average, Old or an integer bigger than 0
     * @return The appropriate enumeration.
     * @throws WhatToStudyException If the input does not fit the requirements.
     */
    public static Age clean(String ageString) throws WhatToStudyException
    {
        Optional<Age> currentValue;
        //Check if the input is already a cleaned value
        if((currentValue = Arrays.stream(Age.values()).parallel().filter(value -> value.toString().equals(ageString)).findFirst()).isPresent())
        {
            return currentValue.get();
        } else
        {
            try
            {
                int age = Integer.parseInt(ageString);
                if (age < 18)
                {
                    return Age.YOUNG;
                } else if (age < 23)
                {
                    return Age.AVERAGE;
                } else if (age >= 23)
                {
                    return Age.OLD;
                } else
                {
                    throw new WhatToStudyException("Unable to parse age");
                }
            } catch (NumberFormatException e)
            {
                throw new WhatToStudyException("Unable to parse age");
            }
        }
    }
}

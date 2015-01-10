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
 * This enumeration contains all specification for the sex column of a case.
 */
public enum Sex
{
    /**
     * This value is representing a male person and is converted to the Netica compliant String "M".
     */
    MALE
    {
        @Override
        public String toString()
        {
            return "M";
        }
    },
    /**
     * This value is representing a female person and is converted to the Netica compliant String "F".
     */
    FEMALE
    {
        @Override
        public String toString()
        {
            return "F";
        }
    };

    /**
     * Creates the header for the sex column used by Netica.
     * @return The header used by Netica: "Sex"
     */
    public static String getHeader()
    {
        return "Sex";
    }

    /**
     * A list of valid headers accepted from an input.
     * These include: Geschlecht, Sex
     * @return An array containing Strings that are considered as valid headers.
     */
    public static String[] getValidHeaders()
    {
        return new String[]{"Geschlecht", getHeader()};
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
     * This function is cleaning and validating a String for the sex property, to enable its use within the network.
     * @param sex The input String, being one of the following: m, w, F, M.
     * @return The appropriate enumeration.
     * @throws WhatToStudyException If the input does not fit the requirements.
     */
    public static Sex clean(String sex) throws WhatToStudyException
    {
        Optional<Sex> currentValue;
        if(sex.equalsIgnoreCase("m"))
        {
            return Sex.MALE;
        } else if(sex.equalsIgnoreCase("w"))
        {
            return Sex.FEMALE;
        } else if((currentValue = Arrays.stream(Sex.values()).parallel().filter(value -> value.toString().equals(sex)).findFirst()).isPresent())
        {   //Check if the input is already a cleaned value
            return currentValue.get();
        } else
        {
            throw new WhatToStudyException("Error validating and cleaning sex type column");
        }
    }
}

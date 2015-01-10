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
 * This enumeration contains all specification for the qualification column of a case.
 */
public enum Qualification
{
    /**
     * This value is representing an Abitur qualification and is converted to the Netica compliant String "Abitur".
     */
    ABITUR
    {
        @Override
        public String toString()
        {
            return "Abitur";
        }
    },
    /**
     * This value is representing a qualification from a Fachhochschule and is converted to the Netica compliant String "FH".
     */
    FH
    {
        @Override
        public String toString()
        {
            return "FH";
        }
    },
    /**
     * This value is representing a Techniker qualification and is converted to the Netica compliant String "Techniker".
     */
    TECHNIKER
    {
        @Override
        public String toString()
        {
            return "Techniker";
        }
    };

    /**
     * Creates the header for the qualification column used by Netica.
     * @return The header used by Netica: "Qualification"
     */
    public static String getHeader()
    {
        return "Qualification";
    }

    /**
     * A list of valid headers accepted from an input.
     * These include: Qualifikation, Qualification
     * @return An array of Strings that are considered as a valid header.
     */
    public static String[] getValidHeaders()
    {
        return new String[]{"Qualifikation", getHeader()};
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
     * This function is cleaning and validating a String for the qualification property, to enable its use within the network.
     * @param qualification The input String, being one of the following: Abitur, Techniker, FH Reife, FH.
     * @return The appropriate enumeration.
     * @throws WhatToStudyException If the input does not fit the requirements.
     */
    public static Qualification clean(String qualification) throws WhatToStudyException
    {
        Optional<Qualification> currentValue;
        if(qualification.equals("Abitur"))
        {
            return Qualification.ABITUR;
        } else if (qualification.equals("Techniker"))
        {
            return Qualification.TECHNIKER;
        } else if(qualification.equals("FH Reife"))
        {
            return Qualification.FH;
        } else if((currentValue = Arrays.stream(Qualification.values()).parallel().filter(value -> value.toString().equals(qualification)).findFirst()).isPresent())
        {   //Check if the input is already a cleaned value
            return currentValue.get();
        } else
        {
            throw new WhatToStudyException("Error validating and cleaning qualification");
        }
    }
}

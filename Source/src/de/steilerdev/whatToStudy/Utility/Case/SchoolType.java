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
 * This enumeration contains all specification for the school type column of a case.
 */
public enum SchoolType
{
    /**
     * This value is representing an "Allgemeines Gymnasium" and is converted to the Netica compliant String "A_Gymnasium".
     */
    A_GYMNASIUM
    {
        @Override
        public String toString()
        {
            return "A_Gymnasium";
        }
    },
    /**
     * This value is representing a "Technisches Gymnasium" and is converted to the Netica compliant String "T_Gymnasium".
     */
    T_GYMNASIUM
    {
        @Override
        public String toString()
        {
            return "T_Gymnasium";
        }
    },
    /**
     * This value is representing a "Wirtschafts Gymnasium" and is converted to the Netica compliant String "W_Gymnasium".
     */
    W_GYMNASIUM
    {
        @Override
        public String toString()
        {
            return "W_Gymnasium";
        }
    },
    /**
     * This value is representing a "Gesamtschule" and is converted to the Netica compliant String "Gesamtschule".
     */
    GESAMTSCHULE
    {
        @Override
        public String toString()
        {
            return "Gesamtschule";
        }
    },
    /**
     * This value is representing the fact that the school type is not available, and is converted to the Netica compliant String "NA".
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
     * Creates the header for the school type column used by Netica.
     * @return The header used by Netica: "School_Type"
     */
    public static String getHeader()
    {
        return "School_Type";
    }

    /**
     * A list of valid headers accepted from an input.
     * These include: Schultyp, School_Type
     * @return An array containing Strings that are considered as valid headers.
     */
    public static String[] getValidHeaders()
    {
        return new String[]{"Schultyp", getHeader()};
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
     * This function is cleaning and validating a String for the school type property, to enable its use within the network.
     * @param schoolType The input String, being one of the following: Allgemeinbildendes Gymnasium, Gesamtschule, Technisches Gymnasium, Wirtschaftsgymnasium, n.a., A_Gymnasium, T_Gymnasium, W_Gymnasium, Gesamtschule, NA.
     * @return The appropriate enumeration.
     * @throws WhatToStudyException If the input does not fit the requirements.
     */
    public static SchoolType clean(String schoolType) throws WhatToStudyException
    {
        Optional<SchoolType> currentValue;
        if(schoolType.equals("Allgemeinbildendes Gymnasium"))
        {
            return SchoolType.A_GYMNASIUM;
        } else if(schoolType.equals("Gesamtschule"))
        {
            return SchoolType.GESAMTSCHULE;
        } else if(schoolType.equals("Technisches Gymnasium"))
        {
            return SchoolType.T_GYMNASIUM;
        } else if(schoolType.equals("Wirtschaftsgymnasium"))
        {
            return SchoolType.W_GYMNASIUM;
        } else if(schoolType.equals("n.a."))
        {
            return SchoolType.NA;
        } else if((currentValue = Arrays.stream(SchoolType.values()).parallel().filter(value -> value.toString().equals(schoolType)).findFirst()).isPresent())
        {   //Check if the input is already a cleaned value
            return currentValue.get();
        } else
        {
            throw new WhatToStudyException("Error validating and cleaning school type column");
        }
    }
}

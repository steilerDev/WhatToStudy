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
 * This enumeration contains all specification for the nationality column of a case.
 */
public enum Nationality
{
    /**
     * This value is representing a German nationality and is converted to the Netica compliant String "German".
     */
    GERMAN
    {
        @Override
        public String toString()
        {
            return "German";
        }
    },
    /**
     * This value is representing a EU nationality and is converted to the Netica compliant String "EU".
     */
    EU
    {
        @Override
        public String toString()
        {
            return "EU";
        }
    },
    /**
     * This value is representing a Non EU nationality and is converted to the Netica compliant String "Non_EU".
     */
    NON_EU
    {
        @Override
        public String toString()
        {
            return "Non_EU";
        }
    };

    /**
     * Creates the header for the nationality column used by Netica.
     * @return The header used by Netica: "Nationality"
     */
    public static String getHeader()
    {
        return "Nationality";
    }

    /**
     * A list of valid headers accepted from an input.
     * These include: Staatsb&uuml;rgerschaft, Staatsbuergerschaft, Nationality
     * @return An array containing Strings that are considered as valid headers.
     */
    public static String[] getValidHeaders()
    {
        return new String[]{"Staatsbürgerschaft", "Staatsbuergerschaft", getHeader()};
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
     * This function is cleaning and validating a String for the nationality property, to enable its use within the network.
     * @param nationality The input String, being one of the following: German, EU, Non_EU, deutsch, EU Buerge, EU B&uuml;rger, Non European
     * @return The appropriate enumeration.
     * @throws WhatToStudyException If the input does not fit the requirements.
     */
    public static Nationality clean(String nationality) throws WhatToStudyException
    {
        Optional<Nationality> currentValue;
        if(nationality.equals("deutsch"))
        {
            return Nationality.GERMAN;
        } else if(nationality.equals("EU Buerger") || nationality.equals("EU Bürger"))
        {
            return Nationality.EU;
        } else if(nationality.equals("Non European"))
        {
            return Nationality.NON_EU;
        } else if((currentValue = Arrays.stream(Nationality.values()).parallel().filter(value -> value.toString().equals(nationality)).findFirst()).isPresent())
        {   //Check if the input is already a cleaned value
            return currentValue.get();
        } else
        {
            throw new WhatToStudyException("Error validating and cleaning nationality column");
        }
    }
}

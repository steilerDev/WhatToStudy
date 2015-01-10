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
 * This enumeration contains all specification for the parental income column of a case.
 */
public enum ParentalIncome
{
    /**
     * This value is representing a low parental income and is converted to the Netica compliant String "Low".
     */
    LOW
    {
        @Override
        public String toString()
        {
            return "Low";
        }
    },
    /**
     * This value is representing a low/middle parental income and is converted to the Netica compliant String "Low_Middle".
     */
    LOW_MIDDLE
    {
        @Override
        public String toString()
        {
            return "Low_Middle";
        }
    },
    /**
     * This value is representing a high/middle parental income and is converted to the Netica compliant String "High_Middle".
     */
    HIGH_MIDDLE
    {
        @Override
        public String toString()
        {
            return "High_Middle";
        }
    },
    /**
     * This value is representing a high parental income and is converted to the Netica compliant String "High".
     */
    HIGH
    {
        @Override
        public String toString()
        {
            return "High";
        }
    };

    /**
     * Creates the header for the parental income column used by Netica.
     * @return The header used by Netica: "Parental_Income"
     */
    public static String getHeader()
    {
        return "Parental_Income";
    }

    /**
     * A list of valid headers accepted from an input.
     * These include: Jahreseinkommen der Eltern, Parental_Income
     * @return An array containing Strings that are considered as valid headers.
     */
    public static String[] getValidHeaders()
    {
        return new String[]{"Jahreseinkommen der Eltern", getHeader()};
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
     * The income is converted according the World Bank analysis of 2013: Low == &lt; 12.540, Low_Middle == &lt; 49.500 High_Middle == &lt; 152.940, High == &gt; 152.940
     * @param incomeString The input String, being an integer bigger than 0, or one of the following: Low, Low_Middle, High_Middle, High
     * @return The cleaned String.
     * @throws WhatToStudyException If the input does not fit the requirements.
     */
    public static ParentalIncome clean(String incomeString) throws WhatToStudyException
    {
        try
        {
            int income = Integer.parseInt(incomeString);
            if (income < 12540)
            {
                return ParentalIncome.LOW;
            } else if (income < 49500)
            {
                return ParentalIncome.LOW_MIDDLE;
            } else if (income < 152940)
            {
                return ParentalIncome.HIGH_MIDDLE;
            } else if (income >= 152940)
            {
                return ParentalIncome.HIGH;
            } else
            {
                throw new WhatToStudyException("Unable to parse the parental income");
            }
        } catch (NumberFormatException e)
        {
            //Check if the input is already a cleaned value
            Optional<ParentalIncome> currentValue;
            if((currentValue = Arrays.stream(ParentalIncome.values()).parallel().filter(value -> value.toString().equals(incomeString)).findFirst()).isPresent())
            {
                return currentValue.get();
            } else
            {
                throw new WhatToStudyException("Unable to parse the parental income");
            }
        }
    }
}

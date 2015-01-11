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
 * This enumeration contains all specification for the course column of a case.
 */
public enum Course
{
    E_ENGINEERING
    {
        /**
         * A nice formatted output handed over to Netica to evaluate it.
         * @return The name of this enum constant used by Netica
         */
        @Override
        public String toString()
        {
            return "E_Engineering";
        }
    },
    C_SCIENCE
    {
        /**
         * A nice formatted output handed over to Netica to evaluate it.
         * @return The name of this enum constant used by Netica
         */
        @Override
        public String toString()
        {
            return "C_Science";
        }
    },
    ENGINEERING
    {
        /**
         * A nice formatted output handed over to Netica to evaluate it.
         * @return The name of this enum constant used by Netica
         */
        @Override
        public String toString()
        {
            return "Engineering";
        }
    },
    S_WORK
    {
        /**
         * A nice formatted output handed over to Netica to evaluate it.
         * @return The name of this enum constant used by Netica
         */
        @Override
        public String toString()
        {
            return "S_Work";
        }
    },
    ECONOMICS
    {
        /**
         * A nice formatted output handed over to Netica to evaluate it.
         * @return The name of this enum constant used by Netica
         */
        @Override
        public String toString()
        {
            return "Economics";
        }
    };

    /**
     * Creates the header for the course column used by Netica.
     * @return The header for the course column used by Netica
     */
    public static String getHeader()
    {
        return "Course";
    }

    /**
     * A list of valid headers accepted from an input file.
     * These include: Studiengang, Course
     * @return An array of Strings that are considered as a valid header.
     */
    public static String[] getValidHeaders()
    {
        return new String[]{"Studiengang", getHeader()};
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
     * The function is accepting a String
     * @param course The input String, being one of the following: E_Engineering, C_Science, Engineering, S_Work, Economics, Elektrotechnik, Informatik, Maschinenbau, Soziale Arbeit, Wirtschaftswissenschaften
     * @return The appropriate enumeration.
     * @throws WhatToStudyException If the input does not fit the requirements.
     */
    public static Course clean(String course) throws WhatToStudyException
    {
        Optional<Course> currentValue;
        if(course.equals("Elektrotechnik"))
        {
            return Course.E_ENGINEERING;
        } else if(course.equals("Informatik"))
        {
            return Course.C_SCIENCE;
        } else if(course.equals("Maschinenbau"))
        {
            return Course.ENGINEERING;
        } else if(course.equals("Soziale Arbeit"))
        {
            return Course.S_WORK;
        } else if(course.equals("Wirtschaftswissenschaften"))
        {
            return Course.ECONOMICS;
        } else if((currentValue = Arrays.stream(Course.values()).parallel().filter(value -> value.toString().equals(course)).findFirst()).isPresent())
        {   //Check if the input is already a cleaned value
            return currentValue.get();
        } else
        {
            throw new WhatToStudyException("Error validating and cleaning course");
        }
    }
}

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
 * This enumeration contains all specification for the study ability test column of a case.
 */
public enum StudyAbilityTest
{
    /**
     * This value is representing a very good grade (1.0 - 2.0) in the study ability test and is converted to the Netica compliant String "Very_Good".
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
     * This value is representing a good grade (2.0 - 3.0) in the study ability test and is converted to the Netica compliant String "Good".
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
     * This value is representing a satisfying grade (3.0 - 4.0) in the study ability test and is converted to the Netica compliant String "Satisfying".
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
     * This value is representing a failed (&gt; 4.0) the study ability test and is converted to the Netica compliant String "Failed".
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
     * This value is representing a the study ability test that is not available and is converted to the Netica compliant String "NA".
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
     * Creates the header for the study ability test column used by Netica.
     * @return The header used by Netica: "Study_Ability_Test"
     */
    public static String getHeader()
    {
        return "Study_Ability_Test";
    }

    /**
     * A list of valid headers accepted from an input.
     * These include: Studierfaehigkeitstest, Studierf&auml;higkeitstest, Study_Ability_Test
     * @return An array containing Strings that are considered as valid headers.
     */
    public static String[] getValidHeaders()
    {
        return new String[]{"Studierfaehigkeitstest", "Studierfähigkeitstest", getHeader()};
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
     * This function is cleaning and validating a String for the study ability property, to enable its use within the network.
     * @param studyAbilityTest The input String, being an integer between 0 and 1000, or one of the following: Very_Good, Good, Satisfying, Failed, n.a., NA.
     *                         The points are converted to grades using the following pattern: 50% == 4.0, 100% == 1.0
     * @return The appropriate enumeration.
     * @throws WhatToStudyException If the input does not fit the requirements.
     */
    public static StudyAbilityTest clean(String studyAbilityTest) throws WhatToStudyException
    {
        if(studyAbilityTest.equals("n.a."))
        {
            return StudyAbilityTest.NA;
        } else
        {
            try
            {
                int studyAbility = Integer.parseInt(studyAbilityTest);
                if (studyAbility > 840)
                {
                    return StudyAbilityTest.VERY_GOOD;
                } else if (studyAbility > 670)
                {
                    return StudyAbilityTest.GOOD;
                } else if (studyAbility > 500)
                {
                    return StudyAbilityTest.SATISFYING;
                } else if (studyAbility <= 500)
                {
                    return StudyAbilityTest.FAILED;
                } else
                {
                    throw new WhatToStudyException("Unable to parse study ability test grade");
                }
            } catch (NumberFormatException e)
            {
                //Check if the input is already a cleaned value
                Optional<StudyAbilityTest> currentValue;
                if((currentValue = Arrays.stream(StudyAbilityTest.values()).parallel().filter(value -> value.toString().equals(studyAbilityTest)).findFirst()).isPresent())
                {
                    return currentValue.get();
                } else
                {
                    throw new WhatToStudyException("Unable to parse study ability test grade");
                }
            }
        }
    }
}

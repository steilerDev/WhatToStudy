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

/**
 * An enumeration for the course column of a case.
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
    }
}

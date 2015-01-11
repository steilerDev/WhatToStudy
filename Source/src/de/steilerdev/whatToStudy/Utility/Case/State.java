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
 * This enumeration contains all specification for the state column of a case.
 */
public enum State
{
    /**
     * This value is representing the German state of Baden W&uuml;rttemberg and is converted to the Netica compliant String "BW".
     */
    BADEN_WUERTTEMBERG {
        @Override
        public String toString()
        {
            return "BW";
        }
    },
    /**
     * This value is representing the German state of Bayern and is converted to the Netica compliant String "BY".
     */
    BAYERN {
        @Override
        public String toString()
        {
            return "BY";
        }
    },
    /**
     * This value is representing the German state of Berlin and is converted to the Netica compliant String "BE".
     */
    BERLIN {
        @Override
        public String toString()
        {
            return "BE";
        }
    },
    /**
     * This value is representing the German state of Brandenburg and is converted to the Netica compliant String "BB".
     */
    BRANDENBURG {
        @Override
        public String toString()
        {
            return "BB";
        }
    },
    /**
     * This value is representing the German state of Bremen and is converted to the Netica compliant String "HB".
     */
    BREMEN {
        @Override
        public String toString() {
            return "HB";
        }
    },
    /**
     * This value is representing the German state of Hamburg and is converted to the Netica compliant String "Hh".
     */
    HAMBURG {
        @Override
        public String toString()
        {
            return "HH";
        }
    },
    /**
     * This value is representing the German state of Hessen and is converted to the Netica compliant String "HE".
     */
    HESSEN {
        @Override
        public String toString()
        {
            return "HE";
        }
    },
    /**
     * This value is representing the German state of Mecklenburg-Vorpommern and is converted to the Netica compliant String "MV".
     */
    MECKLENBURG_VORPOMMERN {
        @Override
        public String toString()
        {
            return "MV";
        }
    },
    /**
     * This value is representing the German state of Niedersachsen and is converted to the Netica compliant String "NI".
     */
    NIEDERSACHSEN {
        @Override
        public String toString()
        {
            return "NI";
        }
    },
    /**
     * This value is representing the German state of Nordrhein-Westfalen and is converted to the Netica compliant String "NW".
     */
    NORDRHEIN_WESTFALEN {
        @Override
        public String toString()
        {
            return "NW";
        }
    },
    /**
     * This value is representing the German state of Rheinland-Pfalz and is converted to the Netica compliant String "RP".
     */
    RHEINLAND_PFALZ {
        @Override
        public String toString()
        {
            return "RP";
        }
    },
    /**
     * This value is representing the German state of Saarland and is converted to the Netica compliant String "SL".
     */
    SAARLAND {
        @Override
        public String toString()
        {
            return "SL";
        }
    },
    /**
     * This value is representing the German state of Sachsen and is converted to the Netica compliant String "SN".
     */
    SACHSEN {
        @Override
        public String toString()
        {
            return "SN";
        }
    },
    /**
     * This value is representing the German state of Sachsen Anhalt and is converted to the Netica compliant String "ST".
     */
    SACHSEN_ANHALT {
        @Override
        public String toString()
        {
            return "ST";
        }
    },
    /**
     * This value is representing the German state of Schleswig Holstein and is converted to the Netica compliant String "SH".
     */
    SCHLESWIG_HOLSTEIN {
        @Override
        public String toString()
        {
            return "SH";
        }
    },
    /**
     * This value is representing the German state of Th&uuml;ringen and is converted to the Netica compliant String "TH".
     */
    THUERINGEN {
        @Override
        public String toString()
        {
            return "TH";
        }
    };

    /**
     * Creates the header for the state column used by Netica.
     * @return The header used by Netica: "State"
     */
    public static String getHeader()
    {
        return "State";
    }

    /**
     * A list of valid headers accepted from an input.
     * These include: Bundesland, State
     * @return An array containing Strings that are considered as valid headers.
     */
    public static String[] getValidHeaders()
    {
        return new String[]{"Bundesland", getHeader()};
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
     * This function is cleaning and validating a String for the nationality property, to enable its use within the network.
     * @param state The input String, being one of the following: Baden-Wuerttemberg, Baden-W&uuml;rttemberg, Bayern,
     *              Nordrhein-Westfalen, Bremen, Sachsen, Thueringen, Th&uuml;ringen, Hessen, Mecklenburg-Vorpommern,
     *              Berlin, Rheinland-Pfalz, Hamburg, Sachsen-Anhalt, Niedersachsen, Brandenburg, Saarland,
     *              Schleswig-Holstein, BW, BY, BE, BB, HB, HH, HE, MV, NI, NW, RP, SL, SN, ST, SH, TH
     * @return The appropriate enumeration.
     * @throws WhatToStudyException If the input does not fit the requirements.
     */
    public static State clean(String state) throws WhatToStudyException
    {
        Optional<State> currentValue;
        if(state.equals("Baden-Wuerttemberg") || state.equals("Baden-Württemberg"))
        {
            return State.BADEN_WUERTTEMBERG;
        } else if(state.equals("Bayern"))
        {
            return State.BAYERN;
        } else if(state.equals("Nordrhein-Westfalen"))
        {
            return State.NORDRHEIN_WESTFALEN;
        } else if(state.equals("Bremen"))
        {
            return State.BREMEN;
        } else if(state.equals("Sachsen"))
        {
            return State.SACHSEN;
        } else if(state.equals("Thueringen") || state.equals("Thüringen"))
        {
            return State.THUERINGEN;
        } else if(state.equals("Hessen"))
        {
            return State.HESSEN;
        } else if(state.equals("Mecklenburg-Vorpommern"))
        {
            return State.MECKLENBURG_VORPOMMERN;
        } else if(state.equals("Berlin"))
        {
            return State.BERLIN;
        } else if(state.equals("Rheinland-Pfalz"))
        {
            return State.RHEINLAND_PFALZ;
        } else if(state.equals("Hamburg"))
        {
            return State.HAMBURG;
        } else if(state.equals("Sachsen-Anhalt"))
        {
            return State.SACHSEN_ANHALT;
        } else if(state.equals("Niedersachsen"))
        {
            return State.NIEDERSACHSEN;
        } else if(state.equals("Brandenburg"))
        {
            return State.BRANDENBURG;
        } else if(state.equals("Saarland"))
        {
            return State.SAARLAND;
        } else if(state.equals("Schleswig-Holstein"))
        {
            return State.SCHLESWIG_HOLSTEIN;
        } else if((currentValue = Arrays.stream(State.values()).parallel().filter(value -> value.toString().equals(state)).findFirst()).isPresent())
        {   //Check if the input is already a cleaned value
            return currentValue.get();
        } else
        {
            throw new WhatToStudyException("Error validating and cleaning state column");
        }
    }
}

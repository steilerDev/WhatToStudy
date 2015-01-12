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
package de.steilerdev.whatToStudy.Functionalities;

import de.steilerdev.whatToStudy.Exception.WhatToStudyException;
import de.steilerdev.whatToStudy.Utility.Case.*;
import de.steilerdev.whatToStudy.Utility.Case.Math;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class is requesting the information of the user through an interactive command line interface.
 */
public class Interactive implements Functionality
{
    /**
     * This function is executing the interactive mode, reading the input and evaluating it.
     * @param args The command line arguments stated during the call of the application.
     *             This array should be empty.
     */
    @Override
    public void run(String[] args) throws WhatToStudyException
    {
        try(BufferedReader console = new BufferedReader(new InputStreamReader(System.in)))
        {
            Case currentCase = new Case();
            String currentLine;

            System.out.println("##################################################################################");
            System.out.println("Welcome to the interactive mode of WhatToStudy");
            System.out.println("http://www.github.com/steilerDev/WhatToStudy");
            System.out.println("##################################################################################");
            System.out.println();
            System.out.println("Please enter the requested information.");
            System.out.println("If you don't want to enter a specific information, or you can't say anything within the topic just press enter.");
            System.out.println("Note: Every missing information is decreasing the accuracy of the program.");
            System.out.println();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Personal information");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println();

            //Reading age until it is valid
            System.out.println("Enter your age");
            System.out.println("(Allowed: An integer bigger than 0, or one of the following: Young, Average, Old)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setAge(null);
                        currentCase.setAge(Age.clean(currentLine));
                    } else
                    {
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An IO related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            //Reading the sex until it is valid
            System.out.println("Enter your gender");
            System.out.println("(Allowed: Male, Female, m, w, F, M)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setSex(Sex.clean(currentLine));
                    } else
                    {
                        currentCase.setSex(null);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            //Reading the nationality until it is valid
            System.out.println("Enter your nationality");
            System.out.println("(Allowed: German, EU, Non_EU, deutsch, EU Buerger, EU Bürger, Non European)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setNationality(Nationality.clean(currentLine));
                    } else
                    {
                        currentCase.setNationality(null);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            //Reading the state until it is valid
            System.out.println("Enter your state");
            System.out.println("(Allowed:\tBaden-Wuerttemberg, Baden-Württemberg, Bayern, Nordrhein-Westfalen,");
            System.out.println("\t\t\tBremen, Sachsen, Thueringen, Thüringen, Hessen, Mecklenburg-Vorpommern,");
            System.out.println("\t\t\tBerlin, Rheinland-Pfalz, Hamburg, Sachsen-Anhalt, Niedersachsen, Brandenburg, Saarland,");
            System.out.println("\t\t\tSchleswig-Holstein, BW, BY, BE, BB, HB, HH, HE, MV, NI, NW, RP, SL, SN, ST, SH, TH)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setState(State.clean(currentLine));
                    } else
                    {
                        currentCase.setState(null);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            //Reading the parental income until it is valid
            System.out.println("Enter the annual salary of your parents");
            System.out.println("(Allowed: An integer bigger than 0, or one of the following: Low, Low_Middle, High_Middle, High)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setParentalIncome(ParentalIncome.clean(currentLine));
                    } else
                    {
                        currentCase.setParentalIncome(null);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            System.out.println();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Educational information");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println();

            //Reading the school type until it is valid
            System.out.println("Enter the type of the school you attended");
            System.out.println("(Allowed: Allgemeinbildendes Gymnasium, Gesamtschule, Technisches Gymnasium, Wirtschaftsgymnasium, n.a., A_Gymnasium, T_Gymnasium, W_Gymnasium, Gesamtschule, NA)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setSchoolType(SchoolType.clean(currentLine));
                    } else
                    {
                        currentCase.setSchoolType(SchoolType.NA);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            //Reading the type of qualification until it is valid
            System.out.println("Enter the type of your qualification");
            System.out.println("(Allowed: Abitur, Techniker, FH Reife, FH)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setQualification(Qualification.clean(currentLine));
                    } else
                    {
                        currentCase.setQualification(null);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            //Reading the average grade of the qualification until it is valid
            System.out.println("Enter the average grade of your earlier stated qualification");
            System.out.println("(Allowed: A floating point number within the range 1.0 to 6.0, or one of the following: Very_Good, Good, Satisfying, Failed)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setQualificationAverage(QualificationAverage.clean(currentLine));
                    } else
                    {
                        currentCase.setQualificationAverage(null);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            //Reading the German grade until it is valid
            System.out.println("Enter your German grade");
            System.out.println("(Allowed: A floating point number within the range 1.0 - 6.0, or one of the following: Very_Good, Good, Satisfying, Failed, NA., keine)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setGerman(German.clean(currentLine));
                    } else
                    {
                        currentCase.setGerman(German.NA);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            //Reading the math grade until it is valid
            System.out.println("Enter your math grade");
            System.out.println("(Allowed: A floating point number within the range 1.0 - 6.0, or one of the following: Very_Good, Good, Satisfying, Failed, NA., keine)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setMath(Math.clean(currentLine));
                    } else
                    {
                        currentCase.setMath(Math.NA);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            //Reading the physics grade until it is valid
            System.out.println("Enter your physics grade");
            System.out.println("(Allowed: A floating point number within the range 1.0 - 6.0, or one of the following: Very_Good, Good, Satisfying, Failed, NA., keine)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setPhysics(Physics.clean(currentLine));
                    } else
                    {
                        currentCase.setPhysics(Physics.NA);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            System.out.println();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Higher educational information");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println();

            //Reading the OLT German results until it is valid
            System.out.println("Enter your German online test results");
            System.out.println("(Allowed: An integer between 0 and 100, or one of the following: Very_Good, Good, Satisfying, Failed)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setOLTGerman(OLTGerman.clean(currentLine));
                    } else
                    {
                        currentCase.setOLTGerman(null);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            //Reading the OLT Math results until it is valid
            System.out.println("Enter your Math online test results");
            System.out.println("(Allowed: An integer between 0 and 100, or one of the following: Very_Good, Good, Satisfying, Failed)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setOLTMath(OLTMath.clean(currentLine));
                    } else
                    {
                        currentCase.setOLTMath(null);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            //Reading the result of the study ability test until it is valid
            System.out.println("Enter the results of your study ability test");
            System.out.println("(Allowed: An integer between 0 and 1000, or one of the following: Very_Good, Good, Satisfying, Failed, n.a., NA)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setStudyAbilityTest(StudyAbilityTest.clean(currentLine));
                    } else
                    {
                        currentCase.setStudyAbilityTest(StudyAbilityTest.NA);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }
            System.out.println("~~~~~~~~~");
            System.out.println();

            //Reading the course until it is valid
            System.out.println("Enter the course you want to study");
            System.out.println("(Allowed: E_Engineering, C_Science, Engineering, S_Work, Economics, Elektrotechnik, Informatik, Maschinenbau, Soziale Arbeit, Wirtschaftswissenschaften)");
            while (true)
            {
                try
                {
                    currentLine = console.readLine().trim();
                    if(!currentLine.isEmpty())
                    {
                        currentCase.setCourse(Course.clean(currentLine));
                    } else
                    {
                        currentCase.setCourse(null);
                        System.out.println("Skipped.");
                    }
                    break;
                } catch (WhatToStudyException e)
                {
                    System.err.println(e.getMessage());
                    System.out.println("Please try again.");
                    continue;
                } catch (IOException e)
                {
                    System.err.println("An I/O related error occurred");
                    System.out.println("Please try again.");
                }
            }

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println();

            Evaluate evaluateFunctionality = new Evaluate();
            evaluateFunctionality.evaluateCase(currentCase);
        } catch (IOException e)
        {
            throw new WhatToStudyException("An I/O related error occurred");
        }
    }
}

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
import de.steilerdev.whatToStudy.Utility.CSVStreamer;
import de.steilerdev.whatToStudy.Utility.Case.*;
import de.steilerdev.whatToStudy.Utility.Case.Math;
import de.steilerdev.whatToStudy.Utility.Case.State;
import norsys.netica.*;

/**
 * This class is using the stored network or a user defined one to evaluate if the system can recommend the user to study a specific course.
 * The evaluation is done using inference.
 */
public class Evaluate implements Functionality
{
    private static String internalFile = "de/steilerdev/whatToStudy/Network/StudyNetwork_new.dne";

    /**
     * This function is loading the stored network or a user specified one and evaluates the given data against it. As a result the likeness of a very good or good grade is given.
     * @param args The command line arguments stated during the call of the application. In this case it should be -e and the path to a CSV file, that needs to be evaluated.
     * @throws NeticaException If an error occurs.
     */
    @Override
    public void run(String[] args) throws WhatToStudyException
    {
        Net net = null;
        Environ env = null;
        try
        {
            env = new Environ(null);

            if(args.length == 2)
            {   //If there is no network file use the internal file instead.
                System.out.println("Loading network from internal file");
                net = new Net(new Streamer(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(internalFile), //Getting the network as java.io.InputStream from the Netica file
                        "StudyNetwork", //Giving the Network a name
                        env)); //Handling over the Environ
            } else if (args.length == 3)
            {   //Load user specified network
                System.out.println("Loading network from user specified file.");
                net = new Net(new Streamer(args[2]));
            } else
            {
                throw new WhatToStudyException("Unable to load network!");
            }

            //Gets the provided case
            Case evaluationCase = CSVStreamer.getEvaluationCase(args[1]);

            //Getting all nodes to set their values and calculating the belief.
            Node age = net.getNode(Age.getHeader());
            Node course = net.getNode(Course.getHeader());
            Node german = net.getNode(German.getHeader());
            Node math = net.getNode(Math.getHeader());
            Node nationality = net.getNode(Nationality.getHeader());
            Node oltGerman = net.getNode(OLTGerman.getHeader());
            Node oltMath = net.getNode(OLTMath.getHeader());
            Node physics = net.getNode(Physics.getHeader());
            Node qualification = net.getNode(Qualification.getHeader());
            Node qualificationAverage = net.getNode(QualificationAverage.getHeader());
            Node schoolType = net.getNode(SchoolType.getHeader());
            Node sex = net.getNode(Sex.getHeader());
            Node state = net.getNode(State.getHeader());
            Node studyAbilityTest = net.getNode(StudyAbilityTest.getHeader());

            //Currently not in the network:
            //Node parentalIncome = net.getNode(ParentalIncome.getHeader());

            System.out.println("Compiling network.");
            net.compile();

            //Setting all values read from the file
            age.finding().enterState(evaluationCase.getAge().toString());
            course.finding().enterState(evaluationCase.getCourse().toString());
            german.finding().enterState(evaluationCase.getGerman().toString());
            math.finding().enterState(evaluationCase.getMath().toString());
            nationality.finding().enterState(evaluationCase.getNationality().toString());
            oltGerman.finding().enterState(evaluationCase.getOLTGerman().toString());
            oltMath.finding().enterState(evaluationCase.getOLTMath().toString());
            physics.finding().enterState(evaluationCase.getPhysics().toString());
            qualification.finding().enterState(evaluationCase.getQualification().toString());
            qualificationAverage.finding().enterState(evaluationCase.getQualificationAverage().toString());
            schoolType.finding().enterState(evaluationCase.getSchoolType().toString());
            sex.finding().enterState(evaluationCase.getSex().toString());
            state.finding().enterState(evaluationCase.getState().toString());
            studyAbilityTest.finding().enterState(evaluationCase.getStudyAbilityTest().toString());

            //Currently not in the network:
            //parentalIncome.finding().enterState(evaluationCase.getParentalIncome().toString());

            System.out.println("Getting belief.");
            //Getting the final grade value
            Node finalGrade = net.getNode(FinalGrade.getHeader());

            //Calculating belief for the final grade
            double belief = finalGrade.getBelief(FinalGrade.GOOD.toString()) + finalGrade.getBelief(FinalGrade.VERY_GOOD.toString());
            System.out.println ("\nProbability of a very good or good final grade " + belief);
        } catch(NeticaException e)
        {
            throw new WhatToStudyException("A Netica based error occurred: " + e.getMessage());
        } finally
        {
            if(net != null)
            {
                try
                {
                    net.finalize();
                } catch (NeticaException e)
                {
                    throw new WhatToStudyException("A Netica based error occurred during the finalization of the net.");
                }
            }
            if(env != null)
            {
                try
                {
                    env.finalize();
                } catch (NeticaException e)
                {
                    throw new WhatToStudyException("A Netica based error occurred during the finalization of the net.");
                }
            }
        }
    }
}

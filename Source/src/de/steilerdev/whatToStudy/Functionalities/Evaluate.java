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
import de.steilerdev.whatToStudy.Main;
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
    /**
     * This function is loading the stored network or a user specified one and evaluates the given data against it. As a result the likeness of a very good or good grade is given.
     * @param args The command line arguments stated during the call of the application. In this case it should be -e and the path to a CSV file, that needs to be evaluated.
     * @throws WhatToStudyException If an error occurs.
     */
    @Override
    public void run(String[] args) throws WhatToStudyException
    {
        Net net = null;
        Environ env = null;
        try
        {
            System.out.println("Starting to evaluate the stated case.");

            //Creating a new environment that is used as default environment later.
            env = new Environ(null);

            if(args.length == 2)
            {   //If there is no network file use the internal file instead.
                System.out.println("Loading network from internal file");
                net = new Net(new Streamer(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(Main.internalFile), //Getting the network as java.io.InputStream from the Netica file
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
            Node age                    = net.getNode(Age.getHeader());
            Node course                 = net.getNode(Course.getHeader());
            Node german                 = net.getNode(German.getHeader());
            Node math                   = net.getNode(Math.getHeader());
            Node nationality            = net.getNode(Nationality.getHeader());
            Node oltGerman              = net.getNode(OLTGerman.getHeader());
            Node oltMath                = net.getNode(OLTMath.getHeader());
            Node physics                = net.getNode(Physics.getHeader());
            Node qualification          = net.getNode(Qualification.getHeader());
            Node qualificationAverage   = net.getNode(QualificationAverage.getHeader());
            Node schoolType             = net.getNode(SchoolType.getHeader());
            Node sex                    = net.getNode(Sex.getHeader());
            Node state                  = net.getNode(State.getHeader());
            Node studyAbilityTest       = net.getNode(StudyAbilityTest.getHeader());
            Node parentalIncome         = net.getNode(ParentalIncome.getHeader());

            System.out.println("Compiling network.");
            net.compile();

            //Setting all values read from the file
            if(age != null){
                age.finding().enterState(evaluationCase.getAge().toString());
            }
            if(course != null){
                course.finding().enterState(evaluationCase.getCourse().toString());
            }
            if(german != null){
                german.finding().enterState(evaluationCase.getGerman().toString());
            }
            if(math != null){
                math                .finding().enterState(evaluationCase.getMath().toString());
            }
            if(nationality != null){
                nationality         .finding().enterState(evaluationCase.getNationality().toString());
            }
            if(oltGerman != null){
                oltGerman           .finding().enterState(evaluationCase.getOLTGerman().toString());
            }
            if(oltMath != null){
                oltMath             .finding().enterState(evaluationCase.getOLTMath().toString());
            }
            if(physics != null){
                physics             .finding().enterState(evaluationCase.getPhysics().toString());
            }
            if(qualification != null){
                qualification       .finding().enterState(evaluationCase.getQualification().toString());
            }
            if(qualificationAverage != null){
                qualificationAverage.finding().enterState(evaluationCase.getQualificationAverage().toString());
            }
            if(schoolType != null){
                schoolType.finding().enterState(evaluationCase.getSchoolType().toString());
            }
            if(sex != null){
                sex.finding().enterState(evaluationCase.getSex().toString());
            }
            if(state != null){
                state.finding().enterState(evaluationCase.getState().toString());
            }
            if(studyAbilityTest != null){
                studyAbilityTest.finding().enterState(evaluationCase.getStudyAbilityTest().toString());
            }
            if(parentalIncome != null){
                parentalIncome.finding().enterState(evaluationCase.getParentalIncome().toString());
            }

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

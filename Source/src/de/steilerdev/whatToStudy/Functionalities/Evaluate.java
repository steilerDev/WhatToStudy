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

import java.util.Arrays;

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
            System.out.println("Starting to evaluate the stated case");
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
                System.out.println("Loading network from user specified file");
                net = new Net(new Streamer(args[2]));
            } else
            {
                throw new WhatToStudyException("Unable to load network!");
            }

            //Gets the provided case
            Case currentCase = CSVStreamer.getEvaluationCase(args[1]);

            //Evaluating the case
            evaluateCase(currentCase, net, env);

        } catch (NeticaException e)
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

    /**
     * Evaluates the stated case using the internal network and standard values.
     * @param currentCase The case that is going to be evaluated
     * @throws WhatToStudyException If an error occurs.
     */
    public void evaluateCase(Case currentCase) throws WhatToStudyException
    {
        Net net = null;
        Environ env = null;
        try
        {
            System.out.println("Starting to evaluate the stated case");
            //Creating a new environment that is used as default environment later.
            env = new Environ(null);

            System.out.println("Loading network from internal file");
            net = new Net(new Streamer(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(Main.internalFile), //Getting the network as java.io.InputStream from the Netica file
                    "StudyNetwork", //Giving the Network a name
                    env)); //Handling over the Environ

            //Evaluating the case
            evaluateCase(currentCase, net, env);

        } catch (NeticaException e)
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

    /**
     * Evaluates the case using variable values.
     * @param currentCase The case that is going to be evaluated
     * @param net The net that is going to be used to evaluate the network
     * @param env The environment used to evaluate the case
     * @throws WhatToStudyException If an error occurs.
     */
    private void evaluateCase(Case currentCase, Net net, Environ env) throws WhatToStudyException
    {
        try
        {
            //Getting all nodes to set their values and calculating the belief.
            Node age                    = net.getNode(Age.getHeader());
            Node course                 = net.getNode(Course.getHeader());
            Node german                 = net.getNode(German.getHeader());
            Node math                   = net.getNode(Math.getHeader());
            Node nationality            = net.getNode(Nationality.getHeader());
            Node oltGerman              = net.getNode(OLTGerman.getHeader());
            Node oltMath                = net.getNode(OLTMath.getHeader());
            Node parentalIncome         = net.getNode(ParentalIncome.getHeader());
            Node physics                = net.getNode(Physics.getHeader());
            Node qualification          = net.getNode(Qualification.getHeader());
            Node qualificationAverage   = net.getNode(QualificationAverage.getHeader());
            Node schoolType             = net.getNode(SchoolType.getHeader());
            Node sex                    = net.getNode(Sex.getHeader());
            Node state                  = net.getNode(State.getHeader());
            Node studyAbilityTest       = net.getNode(StudyAbilityTest.getHeader());

            System.out.println("Compiling network.");
            net.compile();

            //Setting all values read from the file
            if(age != null && currentCase.getAge() != null){
                age.finding().enterState(currentCase.getAge().toString());
            }
            if(course != null && currentCase.getCourse() != null){
                course.finding().enterState(currentCase.getCourse().toString());
            }
            if(german != null && currentCase.getGerman() != null){
                german.finding().enterState(currentCase.getGerman().toString());
            }
            if(math != null && currentCase.getMath() != null){
                math.finding().enterState(currentCase.getMath().toString());
            }
            if(nationality != null && currentCase.getNationality() != null){
                nationality.finding().enterState(currentCase.getNationality().toString());
            }
            if(oltGerman != null && currentCase.getOLTGerman() != null){
                oltGerman.finding().enterState(currentCase.getOLTGerman().toString());
            }
            if(oltMath != null && currentCase.getOLTMath() != null){
                oltMath.finding().enterState(currentCase.getOLTMath().toString());
            }
            if(physics != null && currentCase.getPhysics() != null){
                physics.finding().enterState(currentCase.getPhysics().toString());
            }
            if(qualification != null && currentCase.getQualification() != null){
                qualification.finding().enterState(currentCase.getQualification().toString());
            }
            if(qualificationAverage != null && currentCase.getQualificationAverage() != null){
                qualificationAverage.finding().enterState(currentCase.getQualificationAverage().toString());
            }
            if(schoolType != null && currentCase.getSchoolType() != null){
                schoolType.finding().enterState(currentCase.getSchoolType().toString());
            }
            if(sex != null && currentCase.getSex() != null){
                sex.finding().enterState(currentCase.getSex().toString());
            }
            if(state != null && currentCase.getState() != null){
                state.finding().enterState(currentCase.getState().toString());
            }
            if(studyAbilityTest != null && currentCase.getStudyAbilityTest() != null){
                studyAbilityTest.finding().enterState(currentCase.getStudyAbilityTest().toString());
            }
            if(parentalIncome != null && currentCase.getParentalIncome() != null){
                parentalIncome.finding().enterState(currentCase.getParentalIncome().toString());
            }

            //Getting the decision values
            Node decision = net.getNode("Decision");
            float[] utils = decision.getExpectedUtils();

            for(int i = 0; i < utils.length; i++)
            {
                if(currentCase.getCourse() == null || currentCase.getCourse().toString().equals(decision.state(i)))
                System.out.println("The expected utility for the course " + decision.state(i) + " is " + utils[i]);
            }
        } catch(NeticaException e)
        {
            throw new WhatToStudyException("A Netica based error occurred: " + e.getMessage());
        }
    }
}

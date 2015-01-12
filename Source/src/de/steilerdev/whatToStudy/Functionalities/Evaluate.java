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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class is using the stored network or a user defined one to evaluate if the system can recommend the user to study a specific course.
 * The evaluation is done using inference.
 */
public class Evaluate implements Functionality
{
    //Escape characters for font options
    private static String boldFont = (char)27 +"[1m";
    private static String redFont = (char)27 +"[31m";
    private static String resetFont = (char)27 +"[0m";

    /**
     * This function is loading the stored network or a user specified one and evaluates the given data against it. As a result the likeness of a very good or good grade is given.
     * @param args The command line arguments stated during the call of the application. In this case it should be -e and the path to a CSV file, that needs to be evaluated.
     * @throws WhatToStudyException If an error occurs.
     */
    @Override
    public void run(String[] args) throws WhatToStudyException
    {
        Net net = null;
        Environ env = Environ.getDefaultEnviron();
        try
        {
            System.out.println("Starting to evaluate the stated case");
            //Creating a new environment that is used as default environment later.
            if(env == null)
            {
                env = new Environ(null);
            }

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
     * @return The recommended enumeration (Either a {@link de.steilerdev.whatToStudy.Utility.Case.Course Course} or a {@link de.steilerdev.whatToStudy.Utility.Case.FinalGrade FinalGrade} (Recommendation for with FinalGrade.Very_Good, against FinalGrade.Failed or neither FinalGrade.Satisfying).
     * @throws WhatToStudyException If an error occurs.
     */
    public Enum evaluateCase(Case currentCase) throws WhatToStudyException
    {
        Net net = null;
        Environ env = Environ.getDefaultEnviron();
        try
        {
            System.out.println("Starting to evaluate the stated case");
            //Creating a new environment that is used as default environment later.
            if(env == null)
            {
                env = new Environ(null);
            }

            System.out.println("Loading network from internal file");
            net = new Net(new Streamer(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(Main.internalFile), //Getting the network as java.io.InputStream from the Netica file
                    "StudyNetwork", //Giving the Network a name
                    env)); //Handling over the Environ

            //Evaluating the case
            return evaluateCase(currentCase, net, env);

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
     * @return The recommended enumeration (Either a {@link de.steilerdev.whatToStudy.Utility.Case.Course Course} or a {@link de.steilerdev.whatToStudy.Utility.Case.FinalGrade FinalGrade} (Recommendation for with FinalGrade.Very_Good, against FinalGrade.Failed or neither FinalGrade.Satisfying).
     * @throws WhatToStudyException If an error occurs.
     */
    public Enum evaluateCase(Case currentCase, Net net, Environ env) throws WhatToStudyException
    {
        try
        {
            //Getting all nodes to set their values and calculating the belief.
            Node age                    = net.getNode(Age.getHeader());
            Node course                 = net.getNode(Course.getHeader());
            Node finalGrade             = net.getNode(FinalGrade.getHeader());
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

            //Setting all stated values
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


            if(currentCase.getCourse() != null) //If the user stated a course, give him a recommendation for or against the course
            {
                //Recommend the course if it is going to be a good or a very good grade
                double recommendationFor = getBeliefForRecommendation(finalGrade);
                //Recommend against the course if it is going to be a satisfying of failed grade
                double recommendationAgainst = getBeliefAgainstRecommendation(finalGrade);

                System.out.println();
                System.out.println("~~~~~~~~~");
                if(recommendationAgainst > recommendationFor)
                {
                    System.out.println("Based on the stated information we " + boldFont + redFont + "can not recommend" + resetFont + " the student to attend the selected course (" + currentCase.getCourse().toString() + ")");
                    System.out.println("~~~~~~~~~");
                    return FinalGrade.FAILED;
                } else if(recommendationFor > recommendationAgainst)
                {
                    System.out.println("Based on the stated information we " + boldFont + redFont + "can recommend" + resetFont + " the student to attend the selected course (" + currentCase.getCourse().toString() + ")");
                    System.out.println("~~~~~~~~~");
                    return FinalGrade.VERY_GOOD;
                } else
                {
                    System.out.println("Based on the stated information we " + boldFont + redFont + "can neither recommend nor discourage" + resetFont + " the student to attend the selected course (" + currentCase.getCourse().toString() + ")");
                    System.out.println("~~~~~~~~~");
                    return FinalGrade.SATISFYING;
                }
            } else //Else check which course would have the highest belief to get a recommendation
            {
                //Creating a sorted ArrayList for all courses (descending order)
                Course recommendedCourse;
                ArrayList<Course> bestCourseList = Arrays.stream(Course.values())
                        .sorted((course1, course2) -> {
                            try
                            {
                                return Double.compare(getBeliefForCourse(course2, course, finalGrade), getBeliefForCourse(course1, course, finalGrade));
                            } catch (NeticaException e)
                            {
                                System.err.println("A Netica based error occurred, the result might not be correct. Please try again.");
                                System.err.println("Details:");
                                System.err.println(e.getMessage());
                                //Say both are equal if an error occurs
                                return 0;
                            }
                        }).collect(Collectors.toCollection(ArrayList::new));
                recommendedCourse = bestCourseList.get(0);

                System.out.println();
                System.out.println("~~~~~~~~~");
                System.out.println("Based on the stated information we recommend the course " + boldFont + redFont + recommendedCourse.toString() + resetFont);
                System.out.println("The belief for a very good or good grade within the course is " + getBeliefForCourse(recommendedCourse, course, finalGrade));
                System.out.println("~~~~~~~~~");
                System.out.println();
                System.out.println("The remaining course and beliefs in descending order are listed below:");
                bestCourseList.remove(0);
                bestCourseList.stream().forEach(nextCourse -> {
                    try
                    {
                        System.out.println(nextCourse.toString() + "\t\tBelief: " + getBeliefForCourse(nextCourse, course, finalGrade));
                    } catch (NeticaException e)
                    {
                        System.err.println("Unable to get belief for " + nextCourse.toString());
                    }
                });
                return recommendedCourse;
            }
        } catch(NeticaException e)
        {
            throw new WhatToStudyException("A Netica based error occurred: " + e.getMessage());
        }
    }

    /**
     * This function returns the belief for a very good or good grade using the selected course.
     * @param course The selected course
     * @param courseNode The course node within the network
     * @param finalGrade The final grade node within the network
     * @return The belief of having a very good or good grade with the selected course.
     * @throws NeticaException If an error occurs
     */
    private double getBeliefForCourse(Course course, Node courseNode, Node finalGrade) throws NeticaException
    {
        courseNode.finding().clear();
        courseNode.finding().enterState(course.toString());
        return getBeliefForRecommendation(finalGrade);
    }

    /**
     * This function returns the belief for recommending studying using the information of the current network
     * @param finalGrade The final grade node within the network
     * @return The belief of having a very good or good grade with the selected course.
     * @throws NeticaException If an error occurs
     */
    private double getBeliefForRecommendation(Node finalGrade) throws NeticaException
    {
        //return finalGrade.getBelief(FinalGrade.VERY_GOOD.toString()) + finalGrade.getBelief(FinalGrade.GOOD.toString());
        return finalGrade.getBelief(FinalGrade.VERY_GOOD.toString()) + (2*finalGrade.getBelief(FinalGrade.GOOD.toString())/3);
    }

    /**
     * This function returns the belief for a satisfying of failed grade using the selected course.
     * @param course The selected course
     * @param courseNode The course node within the network
     * @param finalGrade The final grade node within the network
     * @return The belief of having a satisfying of failed grade with the selected course.
     * @throws NeticaException If an error occurs
     */
    private double getBeliefAgainstCourse(Course course, Node courseNode, Node finalGrade) throws NeticaException
    {
        courseNode.finding().clear();
        courseNode.finding().enterState(course.toString());
        return getBeliefAgainstRecommendation(finalGrade);
    }

    /**
     * This function returns the belief for not recommending studying using the information of the current network
     * @param finalGrade The final grade node within the network
     * @return The belief of having a satisfying of failed grade with the selected course.
     * @throws NeticaException If an error occurs
     */
    private double getBeliefAgainstRecommendation(Node finalGrade) throws NeticaException
    {
        //return finalGrade.getBelief(FinalGrade.SATISFYING.toString()) + finalGrade.getBelief(FinalGrade.FAILED.toString());
        return (finalGrade.getBelief(FinalGrade.GOOD.toString())/3) + finalGrade.getBelief(FinalGrade.SATISFYING.toString()) + finalGrade.getBelief(FinalGrade.FAILED.toString());
    }
}

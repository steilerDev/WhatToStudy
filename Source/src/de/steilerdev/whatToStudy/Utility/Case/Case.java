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
 * This class is representing a case used by the netica network.
 */
public class Case
{
    private Age age;
    private Course course;
    private FinalGrade finalGrade;
    private German german;
    private Math math;
    private Nationality nationality;
    private OLTGerman oltGerman;
    private OLTMath oltMath;
    private ParentalIncome parentalIncome;
    private Physics physics;
    private Qualification qualification;
    private QualificationAverage qualificationAverage;
    private SchoolType schoolType;
    private Sex sex;
    private State state;
    private StudyAbilityTest studyAbilityTest;

    private static String delimiter = " ";

    /**
     * Creating the correct line for the specific case using the delimiter and appending a \n at the end of the string.
     * @return
     */
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(qualification.toString() + delimiter);
        buffer.append(qualificationAverage.toString() + delimiter);
        buffer.append(state.toString() + delimiter);
        buffer.append(math.toString() + delimiter);
        buffer.append(physics.toString() + delimiter);
        buffer.append(german.toString() + delimiter);
        buffer.append(schoolType.toString() + delimiter);
        buffer.append(oltMath.toString() + delimiter);
        buffer.append(oltGerman.toString() + delimiter);
        buffer.append(studyAbilityTest.toString() + delimiter);
        buffer.append(age.toString() + delimiter);
        buffer.append(sex.toString() + delimiter);
        buffer.append(parentalIncome.toString() + delimiter);
        buffer.append(nationality.toString() + delimiter);
        buffer.append(course.toString() + delimiter);
        buffer.append(finalGrade.toString());
        return buffer.toString();
    }

    public Age getAge()
    {
        return age;
    }

    public void setAge(Age age)
    {
        this.age = age;
    }

    public Course getCourse()
    {
        return course;
    }

    public void setCourse(Course course)
    {
        this.course = course;
    }

    public FinalGrade getFinalGrade()
    {
        return finalGrade;
    }

    public void setFinalGrade(FinalGrade finalGrade)
    {
        this.finalGrade = finalGrade;
    }

    public German getGerman()
    {
        return german;
    }

    public void setGerman(German german)
    {
        this.german = german;
    }

    public Math getMath()
    {
        return math;
    }

    public void setMath(Math math)
    {
        this.math = math;
    }

    public Nationality getNationality()
    {
        return nationality;
    }

    public void setNationality(Nationality nationality)
    {
        this.nationality = nationality;
    }

    public OLTGerman getOLTGerman()
    {
        return oltGerman;
    }

    public void setOLTGerman(OLTGerman oltGerman)
    {
        this.oltGerman = oltGerman;
    }

    public OLTMath getOLTMath()
    {
        return oltMath;
    }

    public void setOLTMath(OLTMath oltMath)
    {
        this.oltMath = oltMath;
    }

    public ParentalIncome getParentalIncome()
    {
        return parentalIncome;
    }

    public void setParentalIncome(ParentalIncome parentalIncome)
    {
        this.parentalIncome = parentalIncome;
    }

    public Physics getPhysics()
    {
        return physics;
    }

    public void setPhysics(Physics physics)
    {
        this.physics = physics;
    }

    public Qualification getQualification()
    {
        return qualification;
    }

    public void setQualification(Qualification qualification)
    {
        this.qualification = qualification;
    }

    public QualificationAverage getQualificationAverage()
    {
        return qualificationAverage;
    }

    public void setQualificationAverage(QualificationAverage qualificationAverage)
    {
        this.qualificationAverage = qualificationAverage;
    }

    public SchoolType getSchoolType()
    {
        return schoolType;
    }

    public void setSchoolType(SchoolType schoolType)
    {
        this.schoolType = schoolType;
    }

    public Sex getSex()
    {
        return sex;
    }

    public void setSex(Sex sex)
    {
        this.sex = sex;
    }

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    public StudyAbilityTest getStudyAbilityTest()
    {
        return studyAbilityTest;
    }

    public void setStudyAbilityTest(StudyAbilityTest studyAbilityTest)
    {
        this.studyAbilityTest = studyAbilityTest;
    }
}

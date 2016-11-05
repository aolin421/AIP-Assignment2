package au.edu.uts.aip.ams.assignment;

import au.edu.uts.aip.ams.member.Member;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

/**
 * Entity for assignment table
 * @author admin
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name="readAssignment",
        query="select a from Assignment a"),
    @NamedQuery(
        name="readAssignmentByUsername",
        query="select a from Assignment a where a.member.username=:username"),
    @NamedQuery(
        name="readAssignmentBySubjectName",
        query="select a from Assignment a where a.subject=:subject")
})
public class Assignment implements Serializable {
    
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description;
    private String dueDate;
    private String subject;
    private String mark;
    
    @ManyToOne
    private Member member;
    
    @Transient
    private int daysLeft;

    
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDueDate() {
        return dueDate;
    }


    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }


    public String getSubject() {
        return subject;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getMark() {
        return mark;
    }


    public void setMark(String mark) {
        this.mark = mark;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

 
    public Member getMember() {
        return member;
    }


    public void setMember(Member member) {
        this.member = member;
    }


    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }
}

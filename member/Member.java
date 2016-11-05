package au.edu.uts.aip.ams.member;

import au.edu.uts.aip.ams.assignment.Assignment;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * Entity for member table
 * @author admin
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name="readMember",
        query="select m from Member m"),
    @NamedQuery(
        name="readMemberByUsername",
        query="select m from Member m where m.username=:username")
})
public class Member implements Serializable {
    
    @Id
    @GeneratedValue
    private int id;
    private String username;
    private String password;
    private String email;
    private String usergroup;
    private boolean emailVarified;
    
    @OneToMany
    private List<Assignment> assignments;
    
    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

 
    public int getId() {
        return id;
    }

 
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

 
    public void setPassword(String password) throws NoSuchAlgorithmException {
        this.password = password;
    }

   
    public String getEmail() {
        return email;
    }

 
    public void setEmail(String email) {
        this.email = email;
    }

   
    public List<Assignment> getAssignments() {
        return assignments;
    }

    
    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public String getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(String usergroup) {
        this.usergroup = usergroup;
    }
    
    public boolean isEmailVarified() {
        return emailVarified;
    }

    public void setEmailVarified(boolean emailVarified) {
        this.emailVarified = emailVarified;
    }
    
}

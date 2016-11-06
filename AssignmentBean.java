package au.edu.uts.aip.ams.assignment;

import au.edu.uts.aip.ams.member.Member;
import au.edu.uts.aip.ams.model.DaysLeft;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

/**
 * EJB & DAO for assignment table
 * @author admin
 */
@Stateless
public class AssignmentBean {
    
    @PersistenceContext
    private EntityManager em;
    
    public static List<Assignment> cachedAssignments;
    
    /**
     * Done
     * Write assignment with associated member id to database
     * @param assignment
     * @param memberId 
     */
    public void write(Assignment assignment, int memberId) {
        Member member = em.getReference(Member.class, memberId);
        member.getAssignments().add(assignment);
        assignment.setMember(member);
        em.persist(assignment);
        em.persist(member);
        AssignmentBean.cachedAssignments = read();
    }
    
    /**
     * DONE
     * Read whole list of assignment
     * @return whole list of assignment
     */
    public List<Assignment> read() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Assignment> query = builder.createQuery(Assignment.class);
        List<Assignment> listFromDB = em.createQuery(query).getResultList();
        for(Assignment assignment : listFromDB) {
            AssignmentBean.cachedAssignments.add(assignment);
    }
        return AssignmentBean.cachedAssignments;
    }
    
    
    /**
     * DONE
     * delete the assignment for the normal users, it can only delete their own 
     * assignment. 
     * @param assignment
     */
    public void deleteAssignment(Assignment assignment){
        Assignment managed = em.find(Assignment.class, assignment.getId());
        
        managed.getMember().getAssignments().remove(managed);
        
        if (managed != assignment) {
            assignment.getMember().getAssignments().remove(assignment);
        }
        
        em.remove(managed);
        
    }
    /**
     * DONE
     * delete the assignment, whoever the assignment belongs to, used for admin
     * @param id 
     */
    public void deleteAssignmentAdmin(int id){   
        Assignment ass = em.find(Assignment.class, id);
        em.remove(ass);
    }
    /**
     * !!Will work it out later 
     * @param assignment
     */
    public void updateAssignment(Assignment assignment) {  
     em.merge(assignment);
    }
    
    public Assignment findMemberById(int id) {
        return em.find(Assignment.class, id);
    }
    
    /**
     * DONE!! MAY NOT BE USED
     * find the assignments by using the member name
     * @param id
     * @return the assignment which belongs to the user
     */
    public List<Assignment> findAssignmentByMemberId(int id){
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Assignment> query = builder.createQuery(Assignment.class);
        Root<Assignment> from = query.from(Assignment.class);
        query.where(builder.equal(from.get(Assignment_.member), id));
        return em.createQuery(query).getResultList();
    }
    /**
     * DONE
     * find the assignment by calling the PK which is the assignment id.
     * @param id
     * @return the assignment with the id
     */
    public Assignment findById(int id){
        return em.find(Assignment.class, id);
    }
    /**
     * DONE
     * @param subjectName
     * @return 
     */
    public List<Assignment> readAssignmentBySubjectName(String subjectName) {
        
        // Check cahed list of assignmet
        if(cachedAssignments != null && !cachedAssignments.isEmpty()) {
            
            // Final result list, empty first
            List<Assignment> resultList = new ArrayList<>();
            
            // Add assignments to result list if subject name is same from database
            cachedAssignments.stream().filter((assignment) -> (assignment.getSubject().equals(subjectName))).forEach((assignment) -> {
                resultList.add(assignment);
            });
            
            // Sort final result list and return it
            return sort(resultList);           
        } else {
            
            // if cached assignmet is null or empty return null
            return null;          
        }
    }
    /**
     * DONE
     * @param username
     * @return 
     */
    public List<Assignment> readByUsername(String username) {
        TypedQuery<Assignment> query = em.createNamedQuery("readAssignmentByUsername", Assignment.class);
        query.setParameter("username", username);
        AssignmentBean.cachedAssignments = query.getResultList();
        return sort(AssignmentBean.cachedAssignments);
    }
    /**
     * DONE
     * @param assignmentList
     * @return 
     */
    public List<Assignment> sort(List<Assignment> assignmentList) {
        Assignment[] assignments = null;
        if(assignmentList.size() > 1) {
            assignments = new Assignment[assignmentList.size()];
            int counter = 0;
            for(Assignment assignment : assignmentList) {
                assignments[counter] = assignment;
                counter++;
            }
        }
        DaysLeft daysLeft = new DaysLeft();
        Assignment temp = null;
        boolean flag = true;
        if(assignmentList.size() > 1) {
            while(flag) {
                flag = false;
                for(int i = 0; i < (assignments.length - 1); i++) {
                    daysLeft.calDaysLeft(assignments[i].getDueDate());
                    int dueDate1 = (int) daysLeft.getLeftDay();
                    assignments[i].setDaysLeft(dueDate1);
                    daysLeft.calDaysLeft(assignments[(i+1)].getDueDate());
                    int dueDate2 = (int) daysLeft.getLeftDay();
                    assignments[i+1].setDaysLeft(dueDate2);
                    if(dueDate1 > dueDate2) {
                        temp = assignments[i];
                        assignments[i] = assignments[i+1];
                        assignments[i+1] = temp;
                        flag = true;
                    }
                }
            }
            assignmentList.clear();
            for(int i = 0; i < assignments.length; i++) {
                assignmentList.add(assignments[i]);
            }
        } else {
            if(!assignmentList.isEmpty()) {
                daysLeft.calDaysLeft(assignmentList.get(0).getDueDate());
                assignmentList.get(0).setDaysLeft((int)daysLeft.getLeftDay());
            }
        }
        return assignmentList;
    }
    
}

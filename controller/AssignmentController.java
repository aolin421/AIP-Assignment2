/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uts.aip.ams.controller;

import au.edu.uts.aip.ams.assignment.Assignment;
import au.edu.uts.aip.ams.assignment.AssignmentBean;
import au.edu.uts.aip.ams.member.Member;
import au.edu.uts.aip.ams.member.MemberBean;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author admin
 */
@Named
@RequestScoped
public class AssignmentController {
    
    @EJB
    private AssignmentBean assignmentBean;
    
    @EJB
    private MemberBean memberBean;

    private int memberId;
    
    private Member member =  new Member();
    private Assignment ass = new Assignment();
    
    private String ajaxSearchString;
    
    private List<Assignment> assignmentList;
        
    public String add() {
        Assignment assignment = new Assignment();
        assignment.setName(ass.getName());
        assignment.setDescription(ass.getDescription());
        assignment.setDueDate(ass.getDueDate());
        assignment.setSubject(ass.getSubject());
        assignment.setMark(ass.getMark());
        assignmentBean.write(assignment, getMemberId());    
        return "/index?faces-redirect=true";
    }
    
    /**
     * load the assignment by calling the findById method in the assginemntbean class
     * assign the value to the ass
     * @param id 
     */
    public void loadAss(int id)
    {
        
        ass = assignmentBean.findById(id);
    }
    /**
     * by calling the write method to add the assignment to the user
     * @return to the pervious page
     */
    public String addAssToMember() {
        
        assignmentBean.write(ass, loadMember().getId());
        return "/secret/welcome?faces-redirect=true";
    }
    /**
     * by calling the deleteass method to delete the assignment of the user
     * can be used to normal users only
     * @param assg
     * @return to the pervious page
     */
    public String deleteAss(Assignment assg)
    {
        assignmentBean.deleteAss(assg);
        return "/secret/welcome?faces-redirect=true";
    }
    /**
     * by calling the deleteass method to delete the assignment of the user
     * can be used to admins only
     * @param ass
     * @return to the pervious page
     */
    public String deleteAssAdmin(Assignment ass)
    {
        assignmentBean.deleteAssAdmin(ass.getId());
        return "/admin/assignment_list?faces-redirect=true";
    }
    /**
     * just return to the next page by taking a param of assginment id to next 
     * page
     * @param id
     * @return 
     */
    public String editeAssPage(int id)
    {
        return "/admin/editAssignment?faces-redirect=true&id=" + id;
    }
    /**
     * just return to the next page by taking a param of assginment id to next 
     * page
     * @param id
     * @return 
     */
    public String addAssPage(int id)
    {
        return "/admin/add_assignment?faces-redirect=true&id=" + id;
    }
    /**
     * just return to the next page by taking a param of assginment id to next 
     * page
     * @param id
     * @return 
     */
    public String modifyAss(int id)
    {
        return "/secret/modifyAssignment?faces-redirect=true&id=" + id;
    }
    public List<Assignment> findAssByMember(String name)
    {
        return assignmentBean.findAssByMember(name);
    }
    /**
     * update the assignment record by calling the updateAss method in assignmentBean calss
     * @param id
     * @return to the pervious 
     */
    public String updateAss()
    {
        member = assignmentBean.findMemberById(ass.getId()).getMember();
        
        assignmentBean.updateAss(ass);
        Assignment assi = assignmentBean.findById(ass.getId());
        if(member!=null)
         System.out.println(member.getId());
        else
            return null;
        
        if(assi.getMember()==null)
            assignmentBean.deleteAssAdmin(assi.getId());
        else
            return null;
        System.out.println(member.getPassword());
        assignmentBean.write(assi, member.getId() );
        
        return "/admin/assignment_list?faces-redirect=true";
        
    }
    
    
    
    public List<Assignment> read() {
        return assignmentBean.read();
    }
    public List<Assignment> allAss()
    {
        return assignmentBean.findAllAssignment();
    }
    
    public List<Assignment> readByUsername() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession session = request.getSession();
        if(ajaxSearchString != null && !ajaxSearchString.isEmpty()) {
            assignmentList = assignmentBean.readAssignmentBySubjectName(ajaxSearchString);
        } else {
            assignmentList = assignmentBean.readByUsername((String)session.getAttribute("user"));
        }
        if(assignmentList == null || assignmentList.isEmpty()) {
            assignmentList = new ArrayList<>();
        }
        return assignmentList;
    }

    public Member loadMember()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession session = request.getSession();
        
        return memberBean.readByName((String)session.getAttribute("user"));
    }
    
    
    
    public String getAjaxSearchString() {
        return ajaxSearchString;
    }

    /**
     * @param ajaxSearchString the ajaxSearchString to set
     */
    public void setAjaxSearchString(String ajaxSearchString) {
        this.ajaxSearchString = ajaxSearchString;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Assignment getAss() {
        return ass;
    }

    public void setAss(Assignment ass) {
        this.ass = ass;
    }

    public int getMemberId() {
        return memberId;
    }
    
}

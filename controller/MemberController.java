/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uts.aip.ams.controller;

import au.edu.uts.aip.ams.jaxrs.Email;
import au.edu.uts.aip.ams.member.Member;
import au.edu.uts.aip.ams.member.MemberBean;
import au.edu.uts.aip.ams.assignment.*;
import static au.edu.uts.aip.ams.member.Sha.hash256;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author admin
 */
@Named
@RequestScoped
public class MemberController {
    
    @EJB
    private MemberBean memberBean;

    
    @Inject
    SessionController sessionController;
   
    private List<Member> members;
    
    private Member member = new Member();
    private Assignment ass = new Assignment();
    
    public String init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        String uid = request.getParameter("uid");
        if (uid != null) {
            int id = Integer.parseInt(uid);
            member = memberBean.readById(id);
            member.setEmailVarified(true);
            memberBean.updateMember(member);
            login();
        }
        return "login.xhtml";
    }
public String initpwd() {
    FacesContext context=FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        String uid = request.getParameter("uid");
        if (uid == null) {
            
            context.addMessage(null, new FacesMessage("wrong username"));
        return "login.xhtml";
        }
        return "";
    }
    
    public void sendEmailNotification() {
        Email mailgun = new Email();
        List<Member> members = memberBean.read();
        for(Member member : members) {
            mailgun.send(member.getEmail());
        }
    }
    /**
     * the register method by calling the createMember in memberBean calls, 
     * and it will check the usernames 
     * @return null if the usernames if already in the database and return pervious page
     * if registered
     * @throws java.security.NoSuchAlgorithmException
     */
    public String register() throws NoSuchAlgorithmException 
    {
        for(Member a : memberBean.findAllMember())
        { 
            if(a.getUsername().equals(member.getUsername()))
            {
                showError("The account exist, please change the uername.");
                return null;
            }
            
        }
        String oldPassword = member.getPassword();
        String newPassword = hash256(oldPassword);
        member.setPassword(newPassword);
        memberBean.createMember(member);
        
        return "login?faces-redirect=true";
        
    }
    
    
    /**
     * update the member record by calling the updateMember in memberBean calls
     */
    public void updateMember()
    {
        memberBean.updateMember(member);
    }
    /**
     * delete the member record by calling the deleteMember in memberBean calls
     * @param member
     */
    public void deleteMember(Member member)
    {
        memberBean.deleteMember(member.getId());
    }
    /**
     * load the member, assign the value to the member
     * @param id
     */
    public void loadMember(int id)
    {
        member = memberBean.readById(id);
    }
    /**
     * 
     * @param id
     * @return to the editmember page with the member id param
     */
    public String editMemberPage(int id)
    {
        return "/admin/editMember?faces-redirect=true&id=" + id;
    }

    
    
    public List<Member> read() {
        return memberBean.read();
    }
    
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession session = request.getSession();
        try {
            request.login(member.getUsername(), member.getPassword());
            session.setAttribute("user", member.getUsername());
            if(memberBean.readByName(member.getUsername()).getUsergroup().equals("admins")) {
                session.setAttribute("admin", "true");
            }
        } catch(ServletException e) {
            context.addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
        return "/secret/welcome.xhtml?faces-redirect=true";
    }
    
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession session = request.getSession();
        try {
            request.logout();
            session.removeAttribute("user");
            session.removeAttribute("admin");
            sessionController.setUsername(null);
            sessionController.setAdmin(null);
            session.invalidate();
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
        return "/index?faces-redirect=true";
    }
    
    public List<Member> checkUsername() {

        setMembers(memberBean.readByUsernamepassword(member.getUsername()));
        return getMembers();
    }

    public String resetpassword() {
        String emails = "";
        int memid=0;
        FacesContext context = FacesContext.getCurrentInstance();
        checkUsername();
        if (getMembers().isEmpty()) {
            context.addMessage(null, new FacesMessage("wrong username"));
        }
        else{
        for (Member obj : getMembers()) {
            emails = (String) obj.getEmail();
            memid=(int) obj.getId();
        }
        memberBean.sendEmailforpwd(emails, memid);
        context.addMessage(null, new FacesMessage("Email Send Sucessfully"));
        }
        return "resetpassword.xhtml";
    }

    public String newPassword() throws NoSuchAlgorithmException{
         FacesContext context = FacesContext.getCurrentInstance();
        String oldPassword = member.getPassword();
        String newPassword = hash256(oldPassword);
        member.setPassword(newPassword);
        memberBean.updatePassword(member.getPassword(),member.getId());
        context.addMessage(null, new FacesMessage("password Sucessfully changed"));
        return "login.xhtml";
    }

    private void showError(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(message));
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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
    
}

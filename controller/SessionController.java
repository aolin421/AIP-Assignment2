package au.edu.uts.aip.ams.controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Named
@RequestScoped
public class SessionController {
    
    private String username;
    private String admin;
    
    public SessionController() {
        init();
    }
    
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession session = request.getSession();
        if(session.isNew()) {
            setUsername(null);
        } else {
            setUsername((String) session.getAttribute("user"));
            String adminAttribute = (String) session.getAttribute("admin");
            if(adminAttribute != null) {
                admin = adminAttribute;
            } else {
                admin = null;
            }
        }
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the admin
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(String admin) {
        this.admin = admin;
    }
    
}

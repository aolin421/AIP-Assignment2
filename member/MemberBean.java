package au.edu.uts.aip.ams.member;

import au.edu.uts.aip.ams.realm.RealmGroup;
import au.edu.uts.aip.ams.realm.RealmUser;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.*;
import au.edu.uts.aip.ams.assignment.*;
import au.edu.uts.aip.ams.assignment.share.DataStoreException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * EJB & DAO for member table
 * @author admin
 */
@Stateless
public class MemberBean {
    
    @PersistenceContext
    private EntityManager em;
    
    public void write(Member member) throws DataStoreException {
        try {
            String password = Sha.hash256(member.getPassword());
            member.setPassword(password);
            em.persist(member);
            sendEmail(member.getEmail(), member.getId(),member.getUsername());
        } catch (NoSuchAlgorithmException ex) {
            throw new DataStoreException(ex) ;
        }   
    }
    
    public void sendEmail(String email, int id,String uname)
    {
        // Recipient's email ID needs to be mentioned.
        String to = email;//change accordingly

      // Sender's email ID needs to be mentioned
     String from = "singharmaan452@gmail.com";//change accordingly
      final String username = "singharmaan452@gmail.com";//change accordingly
      final String password = "believeinme";

      
      // Assuming you are sending email through relay.jangosmtp.net
      String host = "smtp.gmail.com";

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "587");

      // Get the Session object.
      Session session = Session.getInstance(props,
      new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
         }
      });

      try {
         // Create a default MimeMessage object.
         Message message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.setRecipients(Message.RecipientType.TO,
         InternetAddress.parse(to));

         // Set Subject: header field
         message.setSubject("AMS Email Verification");

         // Now set the actual message
         message.setContent("Hello '"+uname+"', Please click on below link "
            + "<a href='http://localhost:8080/AMS-war/faces/login.xhtml?uid="+id+"'>Click Here</a>", "text/html"); 

         // Send message
         Transport.send(message);


      } catch (MessagingException e) {
            throw new RuntimeException(e);
      }
    }
    
    public List<Member> read() {
        TypedQuery<Member> query = em.createNamedQuery("readMember", Member.class);
        return query.getResultList();
    }
    /**
     * find the member by calling the member id.
     * @param id
     * @return the member with the param id
     */
    public Member readById(int id) {
        return em.find(Member.class, id);
    }
    /**
     * find all of the members, used for the admin to be able to view all the members
     * @return a list of all members
     */
    public List<Member> findAllMember()
    {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = builder.createQuery(Member.class);
        return em.createQuery(query).getResultList();
    }
    /**
     * create a member, used for register.
     * @param mem 
     */
    public void createMember(Member member)
    {   
        
        em.persist(member);
        sendEmail(member.getEmail(), member.getId(),member.getUsername());
    } 
    /**
     * update the member, used for both users and admins
     * @param mem 
     */
    public void updateMember(Member mem)
    {   
        
        em.merge(mem);
    
    }
    /**
     * delete the member by calling the member id, used for both admins and users
     * @param id 
     */
    public void deleteMember(int id)
    {
        Member member = em.find(Member.class, id);
        em.remove(member);
    }
    
    
    public Member readByName(String name)
    {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = builder.createQuery(Member.class);
        Root<Member> from = query.from(Member.class);
        query.where(builder.equal(from.get(Member_.username), name));
        return em.createQuery(query).getSingleResult();
    }
    
    public boolean isDuplicate(String username) {
        for(Member member : read()) {
            if(member.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    public Member readByUsername(String username) {
        TypedQuery<Member> query = em.createNamedQuery("readMemberByUsername", Member.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }
    
    public List<Member> readByUsernamepassword(String username) {
        TypedQuery<Member> query = em.createNamedQuery("readMemberByUsername", Member.class);
        query.setParameter("username", username);
     
        
       
       return query.getResultList();
           
    }
    
    public void sendEmailforpwd(String email, int id)
    {
         // Recipient's email ID needs to be mentioned.
      String to = email;//change accordingly

      // Sender's email ID needs to be mentioned
     String from = "singharmaan452@gmail.com";//change accordingly
      final String username = "singharmaan452@gmail.com";//change accordingly
      final String password = "believeinme";

      
      // Assuming you are sending email through relay.jangosmtp.net
      String host = "smtp.gmail.com";

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "587");

      // Get the Session object.
      Session session = Session.getInstance(props,
      new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
         }
      });

      try {
         // Create a default MimeMessage object.
         Message message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.setRecipients(Message.RecipientType.TO,
         InternetAddress.parse(to));

         // Set Subject: header field
         message.setSubject("AMS Email Verification");

         // Now set the actual message
         message.setContent("Hello, Please click on below link "
            + "<a href='http://localhost:8080/AMS-war/faces/newpassword.xhtml?uid="+id+"'>Click Here</a>", "text/html"); 

         // Send message
         Transport.send(message);


      } catch (MessagingException e) {
            throw new RuntimeException(e);
      }
    }
    
    public void updatePassword(String pwd,int id){
         
         Query query = em.createQuery("update Member m set m.password = :name where m.id = :id",Member.class);
         query.setParameter("name", pwd);
         query.setParameter("id", id);
         query.executeUpdate();
      
     }
    
}

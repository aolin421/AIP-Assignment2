package au.edu.uts.aip.ams.member;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.Stateless;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class MemberBean {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * DONE
     * @return 
     */
    public List<Member> read() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = builder.createQuery(Member.class);
        return em.createQuery(query).getResultList();
    }
    
    /**
     * DONE
     * @param id
     * @return 
     */
    public Member readById(int id) {
        return em.find(Member.class, id);
    }
    
    /**
     * DONE
     * @param member
     * @return 
     */
    public boolean write(Member member) {
        try {
            String password = Sha.hash256(member.getPassword());
            member.setPassword(password);
            em.persist(member);
            sendEmail(member.getEmail(), member.getId(),member.getUsername());
            return true;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.toString());
            return false;
        }   
    }
    
    /**
     * DUPLICATE CODE!!! boolean write(Member member)
     * @param member 
     */
    public void createMember(Member member) {   
        em.persist(member);
        sendEmail(member.getEmail(), member.getId(),member.getUsername());
    }
    
    /**
     * DONE
     * @param id 
     */
    public void deleteMember(int id) {
        Member member = em.find(Member.class, id);
        em.remove(member);
    }
    
    /**
     * DONE
     * @param mem 
     */
    public void updateMember(Member mem) {   
        em.merge(mem);
    }
    
    /**
     * TODO: Need to be changed in criteria query
     * @param password
     * @param memberId 
     */
    public void updatePassword(String password, int memberId) {
        Query query = em.createQuery("update Member m set m.password = :name where m.id = :id",Member.class);
        query.setParameter("name", password);
        query.setParameter("id", memberId);
        query.executeUpdate();
    }
    
    /**
     * TODO: ERROR
     * @param name
     * @return 
     */
    public Member readByName(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = builder.createQuery(Member.class);
        Root<Member> from = query.from(Member.class);
        // ERROR -> query.where(builder.equal(senderEmail.get(Member.), name));
        return em.createQuery(query).getSingleResult();
    }
    
    /**
     * DONE
     * @param username
     * @return 
     */
    public boolean isDuplicate(String username) {
        for(Member member : read()) {
            if(member.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * TODO: MAKE ANOTHER CLASS
     * @param receiverEmail
     * @param recieverId
     * @param recieverUsername 
     */
    public void sendEmail(String receiverEmail, int recieverId, String recieverUsername) {
        // Recipient's receiverEmail ID needs to be mentioned.
        String to = receiverEmail;//change accordingly

        // Sender's receiverEmail ID needs to be mentioned
        String senderEmail = "singharmaan452@gmail.com";//change accordingly
        final String sender = "singharmaan452@gmail.com";//change accordingly
        final String senderPassword = "believeinme";

      
        // Assuming you are sending receiverEmail through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Get the Session object.
        Session session = Session.getInstance(props,
        new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(sender, senderPassword);
            }
        });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(senderEmail));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("AMS Email Verification");

            // Now set the actual message
            message.setContent("Hello '"+sender+"', Please click on below link "
               + "<a href='http://localhost:8080/AMS-war/faces/login.xhtml?uid=" + recieverId + "'>Click Here</a>", "text/html"); 

            // Send message
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * TODO: MAKE ANOTHER CLASS
     * @param email
     * @param memberId 
     */
    public void sendEmailForChangePassword(String email, int memberId)
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
            @Override
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
                    + "<a href='http://localhost:8080/AMS-war/faces/newpassword.xhtml?uid="+memberId+"'>Click Here</a>", "text/html"); 

            // Send message
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

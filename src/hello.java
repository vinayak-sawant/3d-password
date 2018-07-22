import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
 
/**
 * @author Vinayak
 * 
 */
 
public class hello {
 
	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;
 
	//public static void main(String args[]) throws AddressException, MessagingException {
		public hello()  throws AddressException, MessagingException
                        {
                generateAndSendEmail();
		JOptionPane.showMessageDialog(null,"Recovery email has been sent successfully!!");
	}
 public static int rid=0;
 static String ex="java.sql.SQLException: ResultSet closed";
	public static void generateAndSendEmail() throws AddressException, MessagingException {
             String un,ps,kw;
             Connection conn =javaconnect.ConnecrDb();
             PreparedStatement pst;
             ResultSet rs;
             String em=JOptionPane.showInputDialog("Enter Email:");
             try
        {
            
            MessageDigest m = MessageDigest.getInstance("MD5");
                        m.reset();
                        m.update(em.getBytes());
                        byte[] digest = m.digest();
                        BigInteger bigInt = new BigInteger(1,digest);
                        String hashtext = bigInt.toString(16);
                        // Now we need to zero pad it if you actually want the full 32 chars.
                        while(hashtext.length() < 32 )
                        {
                         hashtext = "0"+hashtext;
                        } 
            
            
            pst=conn.prepareStatement("select * from EmployeeInfo where email=?");
            pst.setString(1,hashtext);
            rs=pst.executeQuery();
            if(rs.next())
            {
		// Step1
		//System.out.println("\n 1st ===> setup Mail Server Properties..");
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		//System.out.println("Mail Server Properties have been setup successfully..");
 
		// Step2
		//System.out.println("\n\n 2nd ===> get Mail Session..");
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(em));
		//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("test2@crunchify.com"));
		generateMailMessage.setSubject("3D Password RECOVERY");
		String emailBody = "Ignore this E-mail if you have not requested Password Recovery. You will shortly be contacted by our System Administrator.<br><br> Regards, <br>3D PASSWORD Admin<br><br><b>Note: </b>Do not reply to this Email.";
		generateMailMessage.setContent(emailBody, "text/html");
            //	System.out.println("Mail Session has been created successfully..");
 
		// Step3
	//	System.out.println("\n\n 3rd ===> Get Session and Send mail");
		Transport transport = getMailSession.getTransport("smtp");
 
		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.gmail.com", "YOUREMAIL", "PASSOWRD");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
            }}
             catch(Exception e2)
        {
            
            String s=e2.toString();
           if(s.equals(ex))
           {
               JOptionPane.showMessageDialog(null,"Error: This email address is not matching with any user");
           }
           else
              JOptionPane.showMessageDialog(null,"Error: Make sure you are connected to the internet");
                System.exit(1);
	}
        }
}
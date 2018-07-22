
import java.sql.PreparedStatement;
import java.sql.Connection;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import java.math.BigInteger;
import java.security.MessageDigest;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/** Sample 8 - how to let the user pick (select) objects in the scene 
 * using the mouse or key presses. Can be used for shooting, opening doors, etc. */
public class Env3D_reg extends SimpleApplication {

  Env3D_reg()
 {
//app.setShowSettings(false);
     setShowSettings(false);
AppSettings settings = new AppSettings(true);
settings.put("Width", 1400);
settings.put("Height", 768);
settings.put("color depth", 24);
settings.put("Title", "3D Password-Registration wizard");
settings.put("VSync", true);
settings.put("Anti-alising", false);
//app.setSettings(settings);
setSettings(settings);

//app.start();
start();
 }

  private Node shootables;
  private Geometry mark;

  @Override
  public void simpleInitApp() {
    initCrossHairs(); // a "+" in the middle of the screen to help aiming
    initKeys();       // load custom key mappings
    initMark(); 
    setUpLight(); 
   
    viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    flyCam.setMoveSpeed(50);

    shootables = new Node("Shootables");
    rootNode.attachChild(shootables);
    shootables.attachChild(makeCharacter());
  }
  /** Declaring the "Shoot" action and mapping to its triggers. */
  
  String un1=null;
  private void initKeys() {
    
   inputManager.addMapping("Shoot",
      new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
      new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
    inputManager.addListener(actionListener, "Shoot");
  
  }
  
  
  /** A red ball that marks the last spot that was "hit" by the "shot". */
  protected void initMark() {
    Sphere sphere = new Sphere(30, 30, 0.2f);
    mark = new Geometry("BOOM!", sphere);
    Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mark_mat.setColor("Color", ColorRGBA.randomColor());
    mark.setMaterial(mark_mat);
  }

  /** A centred plus sign to help the player aim. */
  protected void initCrossHairs() {
    setDisplayStatView(false);
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText ch = new BitmapText(guiFont, false);
    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    ch.setText("+"); // crosshairs
    ch.setColor(ColorRGBA.Black);
    ch.setLocalTranslation( // center
      settings.getWidth() / 2 - ch.getLineWidth()/2, settings.getHeight() / 2 + ch.getLineHeight()/2, 0);
    guiNode.attachChild(ch);
  }

  protected Spatial makeCharacter() {
      assetManager.registerLocator("town.zip", ZipLocator.class);
    Spatial sceneModel = assetManager.loadModel("main.scene");
    sceneModel.setLocalScale(2f);
    sceneModel.setLocalTranslation(0, -4, -5);
    return sceneModel;
  }
  
   
  
  
  private void setUpLight() {
    // We add light so we see the scene
    AmbientLight al = new AmbientLight();
    al.setColor(ColorRGBA.White.mult(1.3f));
    rootNode.addLight(al);
    DirectionalLight dl = new DirectionalLight();
    dl.setColor(ColorRGBA.White);
    dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
    rootNode.addLight(dl);
  }
   private int v=0;
  
  /** Defining the "Shoot" action: Determine what was hit and how to respond. */
  private ActionListener actionListener = new ActionListener() {

    public void onAction(String name, boolean keyPressed, float tpf) {
          
        Connection conn =javaconnect.ConnecrDb();
        PreparedStatement pst;
        try{
                
            if (name.equals("Shoot") && !keyPressed && v==0) {
        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        // 3. Collect intersections between Ray and Shootables in results list.
        shootables.collideWith(ray, results);
        // 4. Print the results
        System.out.println("----- Collisions? " + results.size() + "-----");
      
       
      
        Vector3f[] pt=new Vector3f[10];
        float[] dist= new float[10];
        
        int x1=0,y1=0,z1=0,x2=0,y2=0,z2=0;
        for (int i = 0; i < results.size(); i++) {
           
          // For each hit, we know distance, impact point, name of geometry.
          dist[i] = results.getCollision(i).getDistance();
          pt[i] = results.getCollision(i).getContactPoint();
        }
         System.out.println("  You shot Environment at " + pt[0] + ", " + dist[0] + " wu away.");
         
   
          x1=Math.round(pt[0].x);

          y1=Math.round(pt[0].y);
     
         z1=Math.round(pt[0].z);
         
         JPanel panelc=new JPanel();
        JLabel labelc=new JLabel("Continue to registration?");
        panelc.add(labelc);
        String[] optionsc = new String[]{"OK","Cancel"}; 
        int optionc = JOptionPane.showOptionDialog(null,panelc,"The title",JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,optionsc,optionsc[1]);
        if(optionc==0)
        {
            String em=JOptionPane.showInputDialog("Enter E-mail address");
               String un = JOptionPane.showInputDialog("Enter Username");
                un1=un.toString();
        
                
                JPanel panel=new JPanel();
        JLabel label=new JLabel("Enter Your Password:");
        JPasswordField pass=new JPasswordField(10);
        panel.add(label);
        panel.add(pass);
        String[] options = new String[]{"OK","Cancel"}; 
        JOptionPane.showOptionDialog(null,panel,"The title",JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[1]);
            
                char[] password=pass.getPassword();
                String ps=new String(password);     
                     
     JPanel panel2=new JPanel();
        JLabel label2=new JLabel("Re-enter Your Password:");
        JPasswordField pass2=new JPasswordField(10);
        panel2.add(label2);
        panel2.add(pass2);
        String[] options2 = new String[]{"OK","Cancel"}; 
        JOptionPane.showOptionDialog(null,panel2,"The title",JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options2,options2[1]);
            
                char[] password1=pass2.getPassword();
                String ps1=new String(password1);
                
                if( ps.equals(ps1))
                {
                    try{
                        MessageDigest m = MessageDigest.getInstance("MD5");
                        m.reset();
                        m.update(ps.getBytes());
                        byte[] digest = m.digest();
                        BigInteger bigInt = new BigInteger(1,digest);
                        String hashtext = bigInt.toString(16);
                        // Now we need to zero pad it if you actually want the full 32 chars.
                        while(hashtext.length() < 32 )
                        {
                         hashtext = "0"+hashtext;
                        }    
                        
                        
                        MessageDigest m1 = MessageDigest.getInstance("MD5");
                        m1.reset();
                        m1.update(em.getBytes());
                        byte[] digest1 = m1.digest();
                        BigInteger bigInt1 = new BigInteger(1,digest1);
                        String hashtext1 = bigInt1.toString(16);
                        // Now we need to zero pad it if you actually want the full 32 chars.
                        while(hashtext1.length() < 32 )
                        {
                         hashtext1 = "0"+hashtext1;
                        }  
                        
                        
                      
                 pst = conn.prepareStatement("insert into EmployeeInfo(username,password,x1,y1,z1,email) values(?,?,?,?,?,?)");
                pst.setString(1,un);
                pst.setString(2,hashtext);
              pst.setInt(3, x1);
               pst.setInt(4, y1);
                pst.setInt(5, z1);
                pst.setString(6, hashtext1);
               pst.executeUpdate();
                conn.close();
                JOptionPane.showMessageDialog(null, "Saved successfully");
                JOptionPane.showMessageDialog(null, "Please set a keyword to make the system secure");
                v++;
                }
               
    catch(Exception e2)
        {
            JOptionPane.showMessageDialog(null,e2);
        }   }
                else{
                    JOptionPane.showMessageDialog(null,"Passwords not Matching! Try Again!!");
                    }
        }   
            
         // 5. Use the results (we mark the hit object)
        if (results.size() > 0) {
          // The closest collision point is what was truly hit:
          CollisionResult closest = results.getClosestCollision();
          // Let's interact - we mark the hit with a red dot.
          mark.setLocalTranslation(closest.getContactPoint());
          rootNode.attachChild(mark);
        }
        else {
          // No hits? Then remove the red mark.
          rootNode.detachChild(mark);
        }
     }
            else
                if (name.equals("Shoot") && !keyPressed && v==1) {
        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        // 3. Collect intersections between Ray and Shootables in results list.
        shootables.collideWith(ray, results);
        // 4. Print the results
        System.out.println("----- Collisions? " + results.size() + "-----");
      
       
      
        Vector3f[] pt=new Vector3f[10];
        float[] dist= new float[10];
        
        int x1=0,y1=0,z1=0;
        for (int i = 0; i < results.size(); i++) {
           
          // For each hit, we know distance, impact point, name of geometry.
          dist[i] = results.getCollision(i).getDistance();
          pt[i] = results.getCollision(i).getContactPoint();
        }
         System.out.println("  You shot Environment at " + pt[0] + ", " + dist[0] + " wu away.");
         
   
          x1=Math.round(pt[0].x);

          y1=Math.round(pt[0].y);
     
         z1=Math.round(pt[0].z);

          JPanel panelc=new JPanel();
        JLabel labelc=new JLabel("Continue to enter Keyword?");
        panelc.add(labelc);
        String[] optionsc = new String[]{"OK","Cancel"}; 
        int optionc = JOptionPane.showOptionDialog(null,panelc,"The title",JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,optionsc,optionsc[1]);
        if(optionc==0){
              
               JPanel panel=new JPanel();
        JLabel label=new JLabel("Enter Your Keyword:");
        JPasswordField kw=new JPasswordField(10);
        panel.add(label);
        panel.add(kw);
        String[] options = new String[]{"OK","Cancel"}; 
        JOptionPane.showOptionDialog(null,panel,"The title",JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[1]);
            
                char[] password=kw.getPassword();
                String kw1=new String(password);     
                     
     JPanel panel2=new JPanel();
        JLabel label2=new JLabel("Re-enter Your Keyword:");
        JPasswordField kw2=new JPasswordField(10);
        panel2.add(label2);
        panel2.add(kw2);
        String[] options2 = new String[]{"OK","Cancel"}; 
        JOptionPane.showOptionDialog(null,panel2,"The title",JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options2,options2[1]);
            
                char[] password1=kw2.getPassword();
                String kw3=new String(password1);
                
                
                if( kw1.equals(kw3))
                {
                    try{
                        MessageDigest m = MessageDigest.getInstance("MD5");
                        m.reset();
                        m.update(kw1.getBytes());
                        byte[] digest = m.digest();
                        BigInteger bigInt = new BigInteger(1,digest);
                        String hashtext = bigInt.toString(16);
                        // Now we need to zero pad it if you actually want the full 32 chars.
                        while(hashtext.length() < 32 )
                        {
                         hashtext = "0"+hashtext;
                        } 
                        
                        
                 pst = conn.prepareStatement("insert into Secure(keyword,x,y,z) values(?,?,?,?)");
                pst.setString(1,hashtext);
                pst.setInt(2,x1);
                pst.setInt(3,y1);
                pst.setInt(4,z1);
                pst.executeUpdate();
                conn.close();
                JOptionPane.showMessageDialog(null, "Saved successfully");
                conn.close();
                    JOptionPane.showMessageDialog(null, "Thank you for using 3D Password System-Made by Vinayak");
                    System.exit(1);
                }
               
    catch(Exception e2)
        {
            JOptionPane.showMessageDialog(null,e2);
        }   }
                else{
                    JOptionPane.showMessageDialog(null,"Keywords not Matching! Try Again!!");
                    }
    
        }
         // 5. Use the results (we mark the hit object)
        if (results.size() > 0) {
          // The closest collision point is what was truly hit:
          CollisionResult closest = results.getClosestCollision();
          // Let's interact - we mark the hit with a red dot.
          mark.setLocalTranslation(closest.getContactPoint());
          rootNode.attachChild(mark);
        }
        else {
          // No hits? Then remove the red mark.
          rootNode.detachChild(mark);
        }
     }
    
    }
         catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,"Warning!! Click on 3D objects only");
        } 
    }
   
  };    
}
                  
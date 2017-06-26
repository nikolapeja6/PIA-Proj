/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.util;

import org.hibernate.Session;
import data.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Peja
 */
public class Helper {

    
    private static Field fields[];

    static {

        fields = new Field[9];

        String names[] = {
            "електротехника и рачунарско инжењерство",
            "архитектура",
            "грађевина и геодезија",
            "машинство и индустријско инжењерство",
            "медицина",
            "физичко-хемијске науке",
            "биолошке науке",
            "заштита животне средине"
        };

        for (int i = 1; i < 9; i++) {
            fields[i] = new Field(names[i-1], i-1);
        }
        
        fields[0] = new Field("Select one", -1);
    } 
    
    SessionFactory sf = null;

    public Helper() {
        sf = HibernateUtil.getSessionFactory();
    }

    public User getUser(String username) {
        List<User> users = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = sf.openSession();
            tx = session.beginTransaction();
            Query q = session.createQuery("from User as user where user.username='" + username + "'");
            users = (List<User>) q.list();
            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        if (users == null || users.size() == 0) {
            return null;
        }

        return users.get(0);

    }

    public boolean changePassword(String username, String newPassword) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        if (tx == null) {
            return false;
        }

        try {
            User user = (User) session.load(data.User.class, username);

            if (user == null) {
                return false;
            }

            user.setPasswrd(newPassword);

            session.save(user);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }

        return true;

    }

    public boolean register(String username, String password, String firstName, String lastName,
            String institution, String email, String gender, String t_size, String linkedin, byte[] picture) {

        if (username == null || password == null || firstName == null || lastName == null
                || institution == null || email == null || gender == null || t_size == null) 
            return false;
        

         if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()
                || institution.isEmpty() || email.isEmpty() || gender.isEmpty() || t_size.isEmpty()) 
            return false;
         
         User user = new User();
         
         if (user == null) 
            return false;
        
         
         user.setUsername(username);
         user.setPasswrd(password);
         user.setFirstName(firstName);
         user.setLastName(lastName);
         user.setInstitution(institution);
         user.setEmail(email);
         user.setGender(gender);
         user.setTSize(t_size);
         user.setLinkedin(linkedin.isEmpty()?null:linkedin);
         user.setProfilePicture(picture);
         user.setType("u");
        
        

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        if (tx == null) {
            return false;
        }

        try {

            session.save(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }

        return true;
    }
    
    public ArrayList<data.Conferences> getConferencesToday(){
        ArrayList<data.Conferences> ret = new ArrayList<>();
        
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        
        java.sql.Date timeNow = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        
        try{
            
            List<data.Conferences> results = (List<data.Conferences>) session.createQuery( "select s from Conferences s" ).list();
            
            
            
            for(data.Conferences c:results){
                Set<data.Sessions> ses = c.getSessionses();
                for(data.Sessions s:ses)
                    if(s.getDate().toString().equals(timeNow.toString())){
                        ret.add(c);
                        break;
                    }
                    else System.out.println(s.getDate()+" is not today,today is"+timeNow);
            }
        
            }catch(Exception e){
                e.printStackTrace();
            }finally{
            if(tx != null)
                tx.commit();
            session.close();
        }
        
        return ret;
    }
    
    public ArrayList<data.Conferences> getConferencesThisMonth(){
         ArrayList<data.Conferences> ret = new ArrayList<>();
        
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        
        //System.out.println("week "+week);
        
        try{
            
            List<data.Conferences> results = (List<data.Conferences>) session.createQuery( "select s from Conferences s" ).list();
            
            
            
            for(data.Conferences c:results){
                Set<data.Sessions> ses = c.getSessionses();
                for(data.Sessions s:ses){
                    java.util.Date d = new java.util.Date(s.getDate().getTime());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                     int m = cal.get(Calendar.MONTH);
                     int y = cal.get(Calendar.YEAR);
                    
                    if(m == month && y == year){
                        ret.add(c);
                        break;
                    }
                    else System.out.println(s.getDate()+" is not today,today is");
                }
            }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
            if(tx != null)
                tx.commit();
            session.close();
        }
        
        return ret;
    }
    
    
    public List<String> names_with(String query){
        ArrayList<String> ret = new ArrayList<>();
        
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        
        Pattern search = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        
        try{
        for(data.Conferences c: (List<data.Conferences>) session.createCriteria(data.Conferences.class).list()){
            Matcher matcher = search.matcher(c.getName());
            if(matcher.find())
                ret.add(c.getName());
        }
        
        }catch(Exception e){
            e.printStackTrace();
           
        }
        finally{
         if(tx != null)
                tx.commit();
            session.close();
        }
        
        return ret;
    }
    
    public Field[] fields(){
        return fields;
    }
    
    public ArrayList<data.Conferences> search(String search_field, Date search_from, Date search_to, String search_name, String search_place){
        ArrayList<data.Conferences> ret = new ArrayList<>();
        
         System.out.println("fields = "+search_field);
            System.out.println("search from = "+search_from);
            System.out.println("search to = "+search_to);
            System.out.println("name = "+search_name);
            System.out.println("place = "+search_place);
            System.out.println("aaa");
        
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        
        int field = -1;
        for(int i=0; i<fields.length; i++)
            if(fields[i].getName().equals(search_field)){
                field =fields[i].getId();
                break;
            }
        
        
        try{
        
        for(data.Conferences con:(List<data.Conferences>) session.createCriteria(data.Conferences.class).list()){
            
           
            
            if(field != -1 && con.getField() != field){
                continue;
            }
            
            if( search_name != null && !search_name.equals("") && !con.getName().startsWith(search_name))
                continue;
            
            if( search_place != null && !search_place.equals("") ){
                
                boolean found = false;
                for(data.Sessions s:(Set<data.Sessions>)con.getSessionses()){
                    if(s.getHalls().getPlaces().getCity().equals(search_place))
                    {
                        found = true;
                        break;
                    }
                }
                
                if(!found)
                    continue;
            }
            
            if(search_from != null){
                 boolean found = false;
                for(data.Sessions s:(Set<data.Sessions>)con.getSessionses()){
                    if(s.getDate().after(search_from))
                    {
                        found = true;
                        break;
                    }
                }
                
                if(!found)
                    continue;
            }
            
               if(search_to!= null){
                 boolean found = false;
                for(data.Sessions s:(Set<data.Sessions>)con.getSessionses()){
                    if(s.getDate().before(search_to))
                    {
                        found = true;
                        break;
                    }
                }
                
                if(!found)
                    continue;
            }
               
               ret.add(con);
              
            
        }
            
            }catch(Exception e){
                e.printStackTrace();
            }
        finally{
            if(tx!= null)
                tx.commit();
            session.close();
        }
        
        return ret;
    }
    
    public data.Conferences getConference(int id){
        
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {
            return (data.Conferences) session.load(data.Conferences.class, id);

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();

        }
    }
    
    public void apply(String username, int id){
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {
            Set<data.Applied> a = (Set<data.Applied>)((data.Conferences) session.load(data.Conferences.class, id)).getApplieds();
            
            for(data.Applied ap:a)
                if(ap.getUser().getUsername().equals(username))
                    return;
            
            data.Conferences con = (data.Conferences) session.load(data.Conferences.class, id);
            data.User user = (data.User) session.load(data.User.class, username);
            
            data.Applied new_application = new Applied(con, user);
            session.save(new_application);

        } catch (Exception e) {
            e.printStackTrace();
            

        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();

        }
    }
    
    public String[] getUsers(){
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {
           
            List<data.User> users = (List<data.User>) session.createCriteria(data.User.class).list();
            
             String ret []= new String[users.size()];
            
             int i=0;
             for(data.User u: users)
                 ret[i++] = u.getUsername();
          
             
             return ret;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();

        }
    
    }
    
    
    public void send_message(String from, String to, String message){
    
         data.User sender = getUser(from);
         data.User receiver = getUser(to);
         
         Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        
        try {
           
           //int id = ((List<data.Messages>) session.createCriteria(data.Messages.class).list()).size();
            
            
            data.Messages m = new data.Messages();
            m.setText(message);
            m.setUserByReceiver(receiver);
            m.setUserBySender(sender);
            m.setTimestamp(new Date(Calendar.getInstance().getTimeInMillis()));
            //m.setId(id);
            
            session.save(m);
         
            

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();

        }
    }
    
    
    public Set<data.Messages> getMessagesForUser(String username){
  
         
         Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        
        try {
           
           //int id = ((List<data.Messages>) session.createCriteria(data.Messages.class).list()).size();
            
            data.User user = (User) session.load(data.User.class, username);
            
            Set<data.Messages> ret = user.getMessagesesForReceiver();
            ret.addAll(user.getMessagesesForSender());
            
            return ret;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();

        }
    }


}

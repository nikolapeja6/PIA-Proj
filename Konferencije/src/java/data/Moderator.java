package data;
// Generated Jul 7, 2017 7:28:47 PM by Hibernate Tools 4.3.1



/**
 * Moderator generated by hbm2java
 */
public class Moderator  implements java.io.Serializable {


     private Integer id;
     private Conferences conferences;
     private User user;

    public Moderator() {
    }

    public Moderator(Conferences conferences, User user) {
       this.conferences = conferences;
       this.user = user;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Conferences getConferences() {
        return this.conferences;
    }
    
    public void setConferences(Conferences conferences) {
        this.conferences = conferences;
    }
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }




}



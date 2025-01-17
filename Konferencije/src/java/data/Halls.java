package data;
// Generated Jul 7, 2017 7:28:47 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Halls generated by hbm2java
 */
public class Halls  implements java.io.Serializable {


     private Integer id;
     private Places places;
     private String name;
     private Set sessionses = new HashSet(0);

    public Halls() {
    }

	
    public Halls(Places places, String name) {
        this.places = places;
        this.name = name;
    }
    public Halls(Places places, String name, Set sessionses) {
       this.places = places;
       this.name = name;
       this.sessionses = sessionses;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Places getPlaces() {
        return this.places;
    }
    
    public void setPlaces(Places places) {
        this.places = places;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Set getSessionses() {
        return this.sessionses;
    }
    
    public void setSessionses(Set sessionses) {
        this.sessionses = sessionses;
    }




}



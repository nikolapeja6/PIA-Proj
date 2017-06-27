/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import data.Conferences;
import data.Lectures;
import data.Messages;
import data.util.Helper;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import java.util.Date;
import java.util.Set;
import javax.faces.bean.ApplicationScoped;

/**
 *
 * @author Peja
 */
@ManagedBean(name = "user")
@SessionScoped
public class User implements Serializable {

    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    private String institution;
    private String gender;
    private String t_size;
    private String linkedin;
    private UploadedFile file;
    private String type;
    private byte[] picture;

    private String newPassword;

    private String search_name;
    private Date search_from;
    private Date search_to;
    private String search_place = "";
    private String search_field;

    private String return_to;

    private String apply_password;

    private String send_to;
    private String message_text;

    private int selected_conference_id;

    public int getSelected_conference_id() {
        return selected_conference_id;
    }

    public void setSelected_conference_id(int selected_conference_id) {
        this.selected_conference_id = selected_conference_id;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getSend_to() {
        return send_to;
    }

    public void setSend_to(String send_to) {
        this.send_to = send_to;
    }

    public String getApply_password() {
        return apply_password;
    }

    public void setApply_password(String apply_password) {
        this.apply_password = apply_password;
    }

    public String getReturn_to() {
        return return_to;
    }

    public void setReturn_to(String return_to) {
        this.return_to = return_to;
    }

    public String getSearch_field() {
        return search_field;
    }

    public void setSearch_field(String search_field) {
        this.search_field = search_field;
    }

    public String getSearch_place() {
        return search_place;
    }

    public void setSearch_place(String search_place) {
        this.search_place = search_place;
    }

    public Date getSearch_from() {
        return search_from;
    }

    public void setSearch_from(Date search_from) {
        this.search_from = search_from;
    }

    public Date getSearch_to() {
        return search_to;
    }

    public void setSearch_to(Date search_to) {
        this.search_to = search_to;
    }

    public String getSearch_name() {
        return search_name;
    }

    public void setSearch_name(String search_name) {
        this.search_name = search_name;
    }

    private ArrayList<data.Conferences> search_results;

    public ArrayList<Conferences> getSearch_results() {
        return search_results;
    }

    public void setSearch_results(ArrayList<Conferences> search_results) {
        this.search_results = search_results;
    }

    public String register() {

        System.out.println("Register started");

        data.User user = helper.getUser(username);

        if (user != null) { // username occupied
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle labels = context.getApplication().evaluateExpressionGet(context, "#{labels}", ResourceBundle.class);
            String label = "The sername '" + username + "' is registered";                //labels.getString("invalid_username");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username", label));
            return "";
        }

        if (file != null) {
            picture = file.getContents();
        }

        if (helper.register(username, password, first_name, last_name, institution, email, gender, t_size, linkedin, picture)) {
            loggedIn = true;
            type = "u";

            if (return_to != null && !return_to.isEmpty()) {
                return return_to;
            } else {
                return "guestMain.xhtml";
            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle labels = context.getApplication().evaluateExpressionGet(context, "#{labels}", ResourceBundle.class);
            String label = "An unknown error occured during the registration";                //labels.getString("invalid_username");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", label));
            return "";
        }

    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
        picture = file.getContents();
    }

    public void changePassword() {

        System.out.println("change password called");

        data.User user = helper.getUser(username);

        if (user == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle labels = context.getApplication().evaluateExpressionGet(context, "#{labels}", ResourceBundle.class);
            String label = labels.getString("invalid_username");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", label));
            return;

        } else if (!password.equals(user.getPasswrd())) {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle labels = context.getApplication().evaluateExpressionGet(context, "#{labels}", ResourceBundle.class);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", labels.getString("invalid_password")));
            return;
        }

        FacesContext context = FacesContext.getCurrentInstance();

        if (helper.changePassword(username, newPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Password successfully changed"));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password was not changed"));
        }

    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    private Helper helper = new Helper();

    public void init() {
        /*
        Locale locale = new Locale("Serbian (Latin)");
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
         */

    }

    public String login() {

        System.out.println("login called");

        // check if username is in DB and if password is valid
        data.User user = helper.getUser(username);

        if (user == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle labels = context.getApplication().evaluateExpressionGet(context, "#{labels}", ResourceBundle.class);
            String label = labels.getString("invalid_username");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", label));
            return "";

        } else if (!password.equals(user.getPasswrd())) {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle labels = context.getApplication().evaluateExpressionGet(context, "#{labels}", ResourceBundle.class);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", labels.getString("invalid_password")));
            return "";
        }

        first_name = user.getFirstName();
        last_name = user.getLastName();
        email = user.getEmail();
        institution = user.getInstitution();
        gender = user.getGender();
        t_size = user.getTSize();
        linkedin = user.getLinkedin();
        type = user.getType();
        picture = user.getProfilePicture();
        loggedIn = true;

        System.out.println("login finished");

        return "guestMain.xhtml";
    }

    public String getType() {
        return type;
    }

    public boolean isAdmin() {
        System.out.println("isAdmin called");
        return loggedIn && "a".equals(type);
    }

    public boolean isRegisteredUser() {
        System.out.println("isUser called");

        System.out.println("type = " + type);
        System.out.println("uername = " + username);
        return loggedIn && ("u".equals(type) || "m".equals(type));
    }

    public void setType(String type) {
        this.type = type;
    }

    private boolean loggedIn = false;

    public boolean isLoggedIn() {
        System.out.println("isLoggedIn called");
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getT_size() {
        return t_size;
    }

    public void setT_size(String t_size) {
        this.t_size = t_size;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linedin) {
        this.linkedin = linedin;
    }

    /*
   public void handleFileUpload(FileUploadEvent event) {
        profile_picture = event.getFile();
        FacesMessage message  = null;
        if(profile_picture != null){
            picture = profile_picture.getContents();
            message = new FacesMessage("Succesful", profile_picture.getFileName() + " is uploaded.");
        }
        else
            message = new FacesMessage("Error", "File was not uploaded");
        
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
     */
    public User(String username, String password, String first_name, String last_name, String email, String institution, String gender, String t_size, String linedin, UploadedFile profile_picture) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.institution = institution;
        this.gender = gender;
        this.t_size = t_size;
        this.linkedin = linedin;
        this.file = profile_picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }

    public String logout() {
        System.out.println("logut called");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index";
    }

    public ArrayList<data.Conferences> getConferencesToday() {
        return helper.getConferencesToday();
    }

    public ArrayList<data.Conferences> getConferencesThisMonth() {
        return helper.getConferencesThisMonth();
    }

    /*
    public void handleFileUpload(FileUploadEvent event) {
        profile_picture = event.getFile();
    }
     */
    public List<String> autocomplete_name(String query) {
        return helper.names_with(query);
    }

    public data.util.Field[] getFields() {
        return helper.fields();
    }

    public void search() throws IOException {
        System.out.println("search called");
        search_results = helper.search(search_field, search_from, search_to, search_name, search_place);
    }

    public String apply(data.Conferences con) throws IOException {
        System.out.println("id = " + con.getId());

        if (!loggedIn) {
            return_to = "search.xhtml";
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml#register");
            return null;
        }

        Date date = new Date(Calendar.getInstance().getTimeInMillis());

        if (con.getApplicationDueDate().after(date)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Application due date already finished", "The due date for applying to this conference was already finished"));
            return "";
        }

        helper.apply(username, con.getId());

        return "";
    }

    public String[] getUsers() {
        return helper.getUsers();
    }

    public void send() throws IOException {
        helper.send_message(username, send_to, message_text);

    }

    class Message_Comparator implements Comparator<data.Messages> {

        @Override
        public int compare(data.Messages m1, data.Messages m2) {
            if (m1.getTimestamp().after(m2.getTimestamp())) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public List<data.Messages> myMessages() {

        List<data.Messages> ret = new ArrayList<>();
        Set<Messages> tmp = helper.getMessagesForUser(username);
        if(tmp != null){
                for(data.Messages m:tmp)
                    ret.add(m);
        }

        ret.sort(new Message_Comparator());
        return ret;
    }

    public ArrayList<data.Conferences> myConferences() {
        return helper.getMyConferences(username);
    }

    public String agenda(int conference_id) {

        selected_conference_id = conference_id;

        return "agenda.xhtml";
    }

    public String my_agenda(int conference_id) {

        return "";
    }

    public String gallerie(int conference_id) {

        return "";
    }

    public String attending(int conference_id) {

        selected_conference_id = conference_id;
        
        return "attending.xhtml";
    }

    public String getConferenceData() {
        data.Conferences con = helper.getConference(selected_conference_id);

        return "Conference data [not working]"/*con.getName() + " ["+helper.fields()[con.getField()]+"]"*/;
    }

    public List<data.Sessions> getSessions() {
        return helper.getSessions(selected_conference_id);
    }

    class LectureComparator implements Comparator<data.Lectures> {

        @Override
        public int compare(Lectures o1, Lectures o2) {
            if (o1.getStart().before(o2.getStart())) {
                return -1;
            } else {
                return 1;
            }
        }

    }

    public List<data.Lectures> sortLectures(int id) {
        ArrayList<data.Lectures> ret = helper.getLectures(id);

        ret.sort(new LectureComparator());

        return ret;
    }

    
    
    public List<data.Author> getAuthors(int id){
    
         return helper.getAuthors(id);
    }
    
    public void addToMyAgenda(int id){
        helper.addToMyAgenda(username, id, selected_conference_id);
        
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Session successfully added to MyAgenda"));
    }
    
    public List<data.User> getUsersForConference(){
        return helper.getUsersForConference(selected_conference_id);
    }
    
    public String message_user(String username){
        send_to = username;
        return "messages.xhtml";
    }
    
    public Set<String> getAvailableUsers(){
        return helper.getAvailableUsers(username);
    }
    
    public void favourite(String user){
        FacesContext context = FacesContext.getCurrentInstance();
     
        if(helper.addFavourite(username, user))
               context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User "+user+" successfully added to Favourites"));
        else
               context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "An error occured while adding the user "+user+"to Favourites"));
    }
}

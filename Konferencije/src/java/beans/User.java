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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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
    private String type;
    private UploadedFile file;

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

    private java.util.Date selected_date = null;
    private int selected_session_id = -1;

    private int selected_agenda_id = -1;
    private int liked;
    private String comment;
    private int rating;
    private List<UploadedFile> pictures;

    private String addConferenceName;
    private java.util.Date addConferenceStart;
    private java.util.Date addConferenceEnd;
    private int addConferenceLocation;
    private java.util.Date addConferenceDue;
    private String addConferenceField;

    private String selectedModerator;
    private int selectedConference;

    private String newSessionName;
    private int newSessionHall;

    private String newLectureName;
    private java.util.Date newLectureStartTime;
    private int newLectureDuration;

    private int selected_lecture_id;
    private String selected_user;
    private String selected_date_string;

    public String getSelected_date_string() {
        return selected_date_string;
    }

    public void setSelected_date_string(String selected_date_string) {
        this.selected_date_string = selected_date_string;
    }

    public int getSelected_lecture_id() {
        return selected_lecture_id;
    }

    public void setSelected_lecture_id(int selected_lecture_id) {
        this.selected_lecture_id = selected_lecture_id;
    }

    public String getSelected_user() {
        return selected_user;
    }

    public void setSelected_user(String selected_user) {
        this.selected_user = selected_user;
    }

    public String getNewLectureName() {
        return newLectureName;
    }

    public void setNewLectureName(String newLectureName) {
        this.newLectureName = newLectureName;
    }

    public Date getNewLectureStartTime() {
        return newLectureStartTime;
    }

    public void setNewLectureStartTime(Date newLectureStartTime) {
        this.newLectureStartTime = newLectureStartTime;
    }

    public int getNewLectureDuration() {
        return newLectureDuration;
    }

    public void setNewLectureDuration(int newLectureDuration) {
        this.newLectureDuration = newLectureDuration;
    }

    public String getNewSessionName() {
        return newSessionName;
    }

    public void setNewSessionName(String newSessionName) {
        this.newSessionName = newSessionName;
    }

    public int getNewSessionHall() {
        return newSessionHall;
    }

    public void setNewSessionHall(int newSessionHall) {
        this.newSessionHall = newSessionHall;
    }

    public String getSelectedModerator() {
        return selectedModerator;
    }

    public void setSelectedModerator(String selectedModerator) {
        this.selectedModerator = selectedModerator;
    }

    public int getSelectedConference() {
        return selectedConference;
    }

    public void setSelectedConference(int selectedConference) {
        this.selectedConference = selectedConference;
    }

    public String getAddConferenceField() {
        return addConferenceField;
    }

    public void setAddConferenceField(String addConferenceField) {
        this.addConferenceField = addConferenceField;
    }

    public Date getAddConferenceDue() {
        return addConferenceDue;
    }

    public void setAddConferenceDue(Date addConferenceDue) {
        this.addConferenceDue = addConferenceDue;
    }

    public String getAddConferenceName() {
        return addConferenceName;
    }

    public void setAddConferenceName(String addConferenceName) {
        this.addConferenceName = addConferenceName;
    }

    public Date getAddConferenceStart() {
        return addConferenceStart;
    }

    public void setAddConferenceStart(Date addConferenceStart) {
        this.addConferenceStart = addConferenceStart;
    }

    public Date getAddConferenceEnd() {
        return addConferenceEnd;
    }

    public void setAddConferenceEnd(Date addConferenceEnd) {
        this.addConferenceEnd = addConferenceEnd;
    }

    public int getAddConferenceLocation() {
        return addConferenceLocation;
    }

    public void setAddConferenceLocation(int addConferenceLocation) {
        this.addConferenceLocation = addConferenceLocation;
    }

    public int getSelected_agenda_id() {
        return selected_agenda_id;
    }

    public void setSelected_agenda_id(int selected_agenda_id) {
        this.selected_agenda_id = selected_agenda_id;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getSelected_session_id() {
        return selected_session_id;
    }

    public void setSelected_session_id(int selected_session_id) {
        this.selected_session_id = selected_session_id;
    }

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

        if (type.equals("u")) {
            return "guestMain.xhtml";
        } else {
            return "adminMain";
        }
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
        return loggedIn && "u".equals(type);
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

    public List<data.Conferences> search() {
        System.out.println("search called");
        search_results = helper.search(search_field, search_from, search_to, search_name, search_place);
        return search_results;
    }

    public void onlySearch() {
        search();
    }

    public String apply(data.Conferences con) throws IOException {
        System.out.println("id = " + con.getId());

        if (!loggedIn) {
            return_to = "search.xhtml";
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml#register");
            return null;
        }

        Date date = new Date(Calendar.getInstance().getTimeInMillis());

        if (con.getApplicationDueDate().before(date)) {
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
        if (tmp != null) {
            for (data.Messages m : tmp) {
                ret.add(m);
            }
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

        selected_conference_id = conference_id;

        return "myagenda";
    }

    public String gallerie(int conference_id) {
        selected_conference_id = conference_id;
        System.out.println("selected_conference_id = " + selected_conference_id);
        return "gallery.xhtml";
    }

    public String attending(int conference_id) {

        selected_conference_id = conference_id;

        return "attending.xhtml";
    }

    public String getConferenceData() {
        data.Conferences con = helper.getConference(selected_conference_id);

        return /*"Conference data [not working]"*/ con.getName() + " [" + (Helper.fields[con.getField()].getName()) + "]";
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

    public List<data.Author> getAuthors(int id) {

        return helper.getAuthors(id);
    }

    public void addToMyAgenda(int id) {
        helper.addToMyAgenda(username, id, selected_conference_id);

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Session successfully added to MyAgenda"));
    }

    public List<data.User> getUsersForConference() {
        return helper.getUsersForConference(selected_conference_id);
    }

    public String message_user(String username) {
        send_to = username;
        return "messages.xhtml";
    }

    public Set<String> getAvailableUsers() {
        return helper.getAvailableUsers(username);
    }

    public void favourite(String user) {
        FacesContext context = FacesContext.getCurrentInstance();

        if (helper.addFavourite(username, user)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User " + user + " successfully added to Favourites"));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "An error occured while adding the user " + user + "to Favourites"));
        }
    }

    public void file_upload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        picture = event.getFile().getContents();
    }

    public List<java.util.Date> getConferenceDays() {
        return helper.getConferenceDays(selected_conference_id);
    }

    public List<data.Sessions> getSessionForDay(java.util.Date date) {
        return helper.getSessionForDay(selected_conference_id, date);
    }

    public String getConferenceName() {

        if (selected_conference_id <= 0) {
            return "";
        }
        String name = helper.getConference(selected_conference_id).getName();
        System.out.println("Name = '" + name + "'");
        return name;
    }

    public String conrefenceDisplay() {
        selected_date = null;
        selected_session_id = -1;

        return "";
    }

    public String dayDisplay() {
        selected_session_id = -1;

        return "";
    }

    public Date getSelected_date() {
        return selected_date;
    }

    public void setSelected_date(Date selected_date) {
        this.selected_date = selected_date;
        selected_session_id = -1;
    }

    public String getSessionName() {
        data.Sessions s = helper.getSession(selected_session_id);
        return s != null ? s.getName() : "";
    }

    public boolean displatSessionPanel() {
        System.out.println("selected_session_id = " + selected_session_id);
        return (selected_session_id == -1 || selected_session_id == 0) && selected_date != null;
    }

    public boolean displayImages() {
        return selected_session_id > 0 && selected_date != null;
    }

    public List<StreamedContent> getImages() {
        return helper.getImages(selected_session_id);
    }

    public List<data.Agenda> getMyAgendaA() {
        return helper.getMyAgendaA(username);
    }

    public String getConferenceName(int id) {
        return helper.getConference(id).getName();
    }

    public String editSessionA(int agenda_id, int session_id) {
        selected_agenda_id = agenda_id;
        selected_session_id = session_id;

        data.Agenda a = helper.getAgenda(agenda_id);
        liked = a.getLiked();
        comment = a.getComment();
        rating = a.getGrade();

        pictures = new ArrayList<>();

        return "edit_agenda";
    }

    public String getSessionNameA() {
        return helper.getSession(selected_session_id).getName();
    }

    public String submitEditAgenda() {

        helper.updateAgenda(selected_agenda_id, liked, rating, comment);

        for (UploadedFile f : pictures) {
            helper.addPicture(selected_session_id, f.getContents());
        }

        pictures.clear();

        FacesMessage message = new FacesMessage("Succesfully updated (uploaded) session review.");
        FacesContext.getCurrentInstance().addMessage(null, message);

        return "myagenda";

    }

    public void upload_pictures(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        pictures.add(event.getFile());
    }

    public List<data.Conferences> getConferences() {
        return helper.getConferences();
    }

    public String removeConference(int id) {
        FacesMessage message = new FacesMessage("Succesfully removed conference " + helper.getConference(id).getName());

        helper.removeConference(id, username);

        FacesContext.getCurrentInstance().addMessage(null, message);

        return "";
    }

    public String addConference() {
        helper.addConference(addConferenceName, addConferenceStart, addConferenceEnd, addConferenceLocation, addConferenceDue, addConferenceField);

        FacesMessage message = new FacesMessage("Succesfully added conference " + addConferenceName);

        addConferenceEnd = addConferenceStart = addConferenceDue = null;
        addConferenceName = null;
        addConferenceLocation = 0;
        addConferenceField = "";

        FacesContext.getCurrentInstance().addMessage(null, message);

        return "adminMain";

    }

    public List<data.Places> getLocations() {
        return helper.getLocations();
    }

    public void addModerator() {
        helper.addModerator(selectedConference, selectedModerator);
        FacesMessage message = new FacesMessage("Succesfully made " + selectedModerator + " moderator of " + helper.getConference(selectedConference).getName() + " conference");
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public boolean isModerator() {
        return helper.isModerator(username);
    }

    public List<data.Conferences> moderating() {
        return helper.moderating(username);
    }

    public String edit_agenda(int id) {
        selected_conference_id = id;

        return "editAgenda";
    }

    public String upload(int id) {
        selected_conference_id = id;
        pictures = new ArrayList<>();

        return "gallery";
    }

    public void uploadPictures() {
        for (UploadedFile f : pictures) {
            helper.addPicture(selected_session_id, f.getContents());
        }

        pictures.clear();

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Succesfully uploaded pictures to gallery.");
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public List<data.Halls> getHalls() {
        return helper.getHalls(selected_conference_id);
    }

    public void addOpeningSession() {
        FacesMessage message = null;

        if (helper.addSession(selected_conference_id, newSessionName, newSessionHall, 1)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Succesfully added item to agenda.");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error occured while adding item to agenda.");
        }

        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public void addClosingSession() {
        FacesMessage message = null;

        if (helper.addSession(selected_conference_id, newSessionName, newSessionHall, 2)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Succesfully added item to agenda.");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error occured while adding item to agenda.");
        }

        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public List<data.Sessions> getUnknownSessions() {
        return helper.getUnknownSessions(selected_conference_id);
    }

    public void addLectureSession() {
        FacesMessage message = null;

        if (helper.addLectureSession(selected_conference_id, selected_session_id, newSessionName, newSessionHall)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Succesfully added item to agenda.");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error occured while adding item to agenda.");
        }

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void addLecture() {
        FacesMessage message = null;

        if (helper.addLecture(selected_session_id, newLectureName, newLectureStartTime, newLectureDuration)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Succesfully added item to agenda.");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error occured while adding item to agenda.");
        }

        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public void addWorkshop() {
        FacesMessage message = null;

        if (helper.addWorkshop(selected_conference_id, selected_session_id, newSessionName, newLectureStartTime, newLectureDuration, newSessionHall)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Succesfully added item to agenda.");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error occured while adding item to agenda.");
        }

        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public List<SelectItem> getLectures() {

        List<data.Sessions> ses = helper.getSessions(selected_conference_id);

        //cars
        List<SelectItem> ret = new ArrayList<>();

        for (data.Sessions s : ses) {
            SelectItemGroup g1 = new SelectItemGroup(s.getName());

            List<data.Lectures> list = helper.getLectures(s.getId());
            SelectItem[] select = new SelectItem[list.size()];

            int i = 0;
            for (data.Lectures l : list) {
                select[i++] = new SelectItem(l.getId(), l.getTitle());
            }

            g1.setSelectItems(select);

            ret.add(g1);
        }
        return ret;
    }

    public void addAuthor() {
        FacesMessage message = null;
        if (helper.addAuthor(selected_lecture_id, selected_user)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Succesfully added item to agenda.");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error occured while adding item to agenda.");
        }

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getTwitterString(int id) {
        data.Conferences con = helper.getConference(id);
        String text = "There is an interesting event " + con.getName() + " at " + con.getPlaces().getName() + ", "
                + con.getPlaces().getAddress() + ", " + con.getPlaces().getCity() + ", " + con.getPlaces().getState() + ". Check it out.";
        try {
            URL url = new URL("https://twitter.com/intent/tweet?text=" + text);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            return uri.toString();
        } catch (Exception e) {
            return "https://twitter.com/intent/tweet?text=Error";
        }
    }

    public void addBlankSession() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            selected_date = formatter.parse(selected_date_string);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        helper.addBlankSession(selected_conference_id, selected_date);

    }

    public List<data.Sessions> getSessionsForLectures() {
        List<data.Sessions> ret = getSessions();

        for (int i = 0; i < ret.size();) {
            if (ret.get(i).getType() == 3 || ret.get(i).getType() == 4) {
                ret.remove(i);
            } else {
                i++;
            }
        }

        return ret;
    }

    public StreamedContent file(byte[] data, String title, String author, char type) {
        DefaultStreamedContent c = new DefaultStreamedContent(new ByteArrayInputStream(data));
        //c.setName(getConferenceName()+" - "+getSessionName()+" - "+title+" ("+author+")."+extension);
        String extension = type == 'd' ? "pdf" : "ppt";
        c.setName(title + " (" + author + ") (" + extension + ")");
        c.setContentType("application/" + extension);
        return c;
    }

    public void authorIsPrezenting(int id) {
        helper.isPrezenting(id);
    }

    public boolean isAuthor(data.Author a) {

        data.User user = a.getUser();

        if (user == null || !user.getUsername().equals(username)) {
            return false;
        }

        return true;
    }
    
    private int author_id;

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }
    
    byte[] material;
    boolean material_type = false;
 
    public void material_upload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        material = event.getFile().getContents();
        material_type = (event.getFile().getFileName().endsWith(".pdf")?true:false);
    }
    

    public String goToUpload(data.Author a) {
        if (a.getPdf() != null && a.getPpt() != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "User already uploaded both ppt and pdf.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "";
        }
        
        author_id = a.getId();
        
        material = null;
        
        return "uploadMaterials";
        
    }
    
    public String uploadMaterials(){
      
        if(material == null)
            return "agenda";
        
        if(!helper.addMaterial(author_id, material, material_type)){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "User already uploaded that type.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
            
        return "agenda";
        
           
    }

}

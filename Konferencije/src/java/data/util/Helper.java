/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.util;

import org.hibernate.Session;
import data.*;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Peja
 */
public class Helper {

    public static Field fields[];

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
            fields[i] = new Field(names[i - 1], i - 1);
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
                || institution == null || email == null || gender == null || t_size == null) {
            return false;
        }

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()
                || institution.isEmpty() || email.isEmpty() || gender.isEmpty() || t_size.isEmpty()) {
            return false;
        }

        User user = new User();

        if (user == null) {
            return false;
        }

        user.setUsername(username);
        user.setPasswrd(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setInstitution(institution);
        user.setEmail(email);
        user.setGender(gender);
        user.setTSize(t_size);
        user.setLinkedin(linkedin.isEmpty() ? null : linkedin);
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

    public ArrayList<data.Conferences> getConferencesToday() {
        ArrayList<data.Conferences> ret = new ArrayList<>();

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        java.sql.Date timeNow = new java.sql.Date(Calendar.getInstance().getTimeInMillis());

        try {

            List<data.Conferences> results = (List<data.Conferences>) session.createQuery("select s from Conferences s").list();

            for (data.Conferences c : results) {
                Set<data.Sessions> ses = c.getSessionses();
                for (data.Sessions s : ses) {
                    if (s.getDate().toString().equals(timeNow.toString())) {
                        ret.add(c);
                        break;
                    } else {
                        System.out.println(s.getDate() + " is not today,today is" + timeNow);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();
        }

        return ret;
    }

    public ArrayList<data.Conferences> getConferencesThisMonth() {
        ArrayList<data.Conferences> ret = new ArrayList<>();

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        //System.out.println("week "+week);
        try {

            List<data.Conferences> results = (List<data.Conferences>) session.createQuery("select s from Conferences s").list();

            for (data.Conferences c : results) {
                Set<data.Sessions> ses = c.getSessionses();
                for (data.Sessions s : ses) {
                    java.util.Date d = new java.util.Date(s.getDate().getTime());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    int m = cal.get(Calendar.MONTH);
                    int y = cal.get(Calendar.YEAR);

                    if (m == month && y == year) {
                        ret.add(c);
                        break;
                    } else {
                        System.out.println(s.getDate() + " is not today,today is");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();
        }

        return ret;
    }

    public List<String> names_with(String query) {
        ArrayList<String> ret = new ArrayList<>();

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        Pattern search = Pattern.compile(query, Pattern.CASE_INSENSITIVE);

        try {
            Query q = session.createQuery("from Conferences as conferences");
            List<data.Conferences> con = (List<data.Conferences>) q.list();

            for (data.Conferences c : con/*(List<data.Conferences>) session.createCriteria(data.Conferences.class).list()*/) {
                Matcher matcher = search.matcher(c.getName());
                if (matcher.find()) {
                    ret.add(c.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();
        }

        return ret;
    }

    public Field[] fields() {
        return fields;
    }

    public ArrayList<data.Conferences> search(String search_field, Date search_from, Date search_to, String search_name, String search_place) {
        ArrayList<data.Conferences> ret = new ArrayList<>();

        System.out.println("fields = " + search_field);
        System.out.println("search from = " + search_from);
        System.out.println("search to = " + search_to);
        System.out.println("name = " + search_name);
        System.out.println("place = " + search_place);
        System.out.println("aaa");

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        int field = -1;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(search_field)) {
                field = fields[i].getId();
                break;
            }
        }

        try {

            Query q = session.createQuery("from Conferences as conferences");
            List<data.Conferences> conf = (List<data.Conferences>) q.list();

            // iterating for all conferences
            for (data.Conferences con : conf /*(List<data.Conferences>) session.createCriteria(data.Conferences.class).list()*/) {

                // check if field is required and if match
                if (field != -1 && con.getField() != field) {
                    continue;
                }

                // check if name is required and if name is match
                if (search_name != null && !search_name.equals("") && !con.getName().startsWith(search_name)) {
                    continue;
                }

                // check if place is required and if place is match
                if (search_place != null && !search_place.equals("")) {

                    boolean found = false;
                    for (data.Sessions s : (Set<data.Sessions>) con.getSessionses()) {
                        if (s.getHalls().getPlaces().getCity().equals(search_place)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        continue;
                    }
                }

                // check if from date is required and if from is a match
                if (search_from != null) {
                    boolean found = false;
                    for (data.Sessions s : (Set<data.Sessions>) con.getSessionses()) {
                        if (s.getDate().after(search_from)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        continue;
                    }
                }

                if (search_to != null) {
                    boolean found = false;
                    for (data.Sessions s : (Set<data.Sessions>) con.getSessionses()) {
                        if (s.getDate().before(search_to)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        continue;
                    }
                }

                // obtain the last day of the conference
                java.util.Date last_day = null;
                for (data.Sessions s : (Set<data.Sessions>) con.getSessionses()) {
                    if (last_day == null || s.getDate().after(last_day)) {
                        last_day = s.getDate();
                    }
                }

                // if the last day of the conference is finished (before today), skip
                if (last_day != null && last_day.before(new Date(Calendar.getInstance().getTimeInMillis()))) {
                    continue;
                }

                // add conference
                ret.add(con);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();
        }

        return ret;
    }

    public data.Conferences getConference(int id) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        System.out.println("getCOnference called with " + id + " as id");

        try {
            Query q = session.createQuery("from Conferences as conferences where conferences.id='" + id + "'");
            List<data.Conferences> conf = (List<data.Conferences>) q.list();

            data.Conferences con = conf.get(0);//(data.Conferences) session.load(data.Conferences.class, id);            
            return con;

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

    public data.Sessions getSession(int id) {

        if (id <= 0) {
            return null;
        }

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {
            Query q = session.createQuery("from Sessions as sessions where sessions.id='" + id + "'");
            List<data.Sessions> conf = (List<data.Sessions>) q.list();

            data.Sessions s = conf.get(0);//(data.Conferences) session.load(data.Conferences.class, id);            
            return s;

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

    public void apply(String username, int id) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {
            Set<data.Applied> a = (Set<data.Applied>) ((data.Conferences) session.load(data.Conferences.class, id)).getApplieds();

            for (data.Applied ap : a) {
                if (ap.getUser().getUsername().equals(username)) {
                    return;
                }
            }

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

    public String[] getUsers() {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            List<data.User> users = (List<data.User>) session.createCriteria(data.User.class).list();

            String ret[] = new String[users.size()];

            int i = 0;
            for (data.User u : users) {
                ret[i++] = u.getUsername();
            }

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

    public void send_message(String from, String to, String message) {

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

    public Set<data.Messages> getMessagesForUser(String username) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            //int id = ((List<data.Messages>) session.createCriteria(data.Messages.class).list()).size();
            data.User user = (User) session.load(data.User.class, username);

            Set<data.Messages> ret = user.getMessagesesForReceiver();
            if (ret != null) {
                ret.addAll(user.getMessagesesForSender());
            } else {
                ret = user.getMessagesesForSender();
            }

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

    class Session_Comparator implements Comparator<data.Sessions> {

        @Override
        public int compare(data.Sessions m1, data.Sessions m2) {
            if (m1.getConferences().getName().compareTo(m2.getConferences().getName()) != 0) {
                return m1.getConferences().getName().compareTo(m2.getConferences().getName());
            } else if (m1.getDate().before(m2.getDate())) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public List<data.Sessions> getMyAgenda(String username) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            //int id = ((List<data.Messages>) session.createCriteria(data.Messages.class).list()).size();
            data.User user = (User) session.load(data.User.class, username);

            List<data.Sessions> ret = new ArrayList<>();

            for (data.Applied a : (Set<data.Applied>) user.getApplieds()) {
                ret.addAll(a.getConferences().getSessionses());
            }

            ret.sort(new Session_Comparator());
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

    public ArrayList<data.Conferences> getMyConferences(String username) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            //int id = ((List<data.Messages>) session.createCriteria(data.Messages.class).list()).size();
            Query q = session.createQuery("from User as user where user.username='" + username + "'");
            List<data.User> users = (List<data.User>) q.list();
            data.User user = users.get(0);//(data.User) session.load(data.User.class, username);
            ArrayList<data.Conferences> ret = new ArrayList<>();

            System.out.println(user.getUsername());

            for (data.Applied a : (Set<data.Applied>) user.getApplieds()) {
                ret.add((data.Conferences) a.getConferences());
            }

            System.out.println("applied for " + ret.size());
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

    public List<data.Sessions> getSessions(int id) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            //int id = ((List<data.Messages>) session.createCriteria(data.Messages.class).list()).size();
            data.Conferences con = (data.Conferences) session.load(data.Conferences.class, id);

            ArrayList<data.Sessions> ret = new ArrayList<>();

            ret.addAll(con.getSessionses());

            ret.sort(new Session_Comparator());
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

    public ArrayList<data.Lectures> getLectures(int id) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            //int id = ((List<data.Messages>) session.createCriteria(data.Messages.class).list()).size();
            data.Sessions s = (data.Sessions) session.load(data.Sessions.class, id);

            ArrayList<data.Lectures> ret = new ArrayList<>();

            ret.addAll(s.getLectureses());

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

    public ArrayList<data.Author> getAuthors(int id) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            //int id = ((List<data.Messages>) session.createCriteria(data.Messages.class).list()).size();
            data.Lectures l = (data.Lectures) session.load(data.Lectures.class, id);
            ArrayList<data.Author> ret = new ArrayList<>();
            ret.addAll(l.getAuthors());
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

    public void addToMyAgenda(String username, int id, int conf) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            //int id = ((List<data.Messages>) session.createCriteria(data.Messages.class).list()).size();
            data.Sessions s = (data.Sessions) session.load(data.Sessions.class, id);

            for (data.Agenda a : (List<data.Agenda>) session.createCriteria(data.Agenda.class).list()) {
                if (a.getSessions().getId() == id && a.getApplied().getUser().getUsername().equals(username)) {
                    return;
                }
            }

            for (data.Applied a : (List<data.Applied>) session.createCriteria(data.Applied.class).list()) {
                if (a.getUser().getUsername().equals(username) && a.getConferences().getId() == conf) {
                    data.Agenda agenda = new data.Agenda();
                    agenda.setApplied(a);
                    agenda.setSessions(s);

                    session.save(agenda);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();

        }
    }

    public List<data.User> getUsersForConference(int conf) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            //int id = ((List<data.Messages>) session.createCriteria(data.Messages.class).list()).size();
            ArrayList<data.User> ret = new ArrayList<>();

            Query q = session.createQuery("from Applied as applied");
            List<data.Applied> applied = (List<data.Applied>) q.list();

            for (data.Applied a : applied/*(List<data.Applied>) session.createCriteria(data.Applied.class).list()*/) {
                if (a.getConferences().getId() == conf) {
                    ret.add(a.getUser());
                }
            }

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

    public Set<String> getAvailableUsers(String username) {

        List<data.Conferences> conf = getMyConferences(username);

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            HashSet<String> ret = new HashSet<>();

            for (data.Applied a : (List<data.Applied>) session.createCriteria(data.Applied.class).list()) {
                for (data.Conferences c : conf) {
                    if (c.getId() == a.getConferences().getId()) {
                        ret.add(a.getUser().getUsername());
                        break;
                    }
                }
            }

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

    public boolean addFavourite(String username, String user) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.User u = (data.User) session.load(data.User.class, username);
            data.User c = (data.User) session.load(data.User.class, user);

            Set<data.Favourite> f = (Set<data.Favourite>) u.getFavouritesForUser();

            for (data.Favourite x : f) {
                if (x.getUserByContact().getUsername().equals(user)) {
                    return false;
                }
            }

            data.Favourite fav = new Favourite();
            fav.setUserByContact(c);
            fav.setUserByUser(u);

            session.save(fav);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();

        }
    }

    public List<java.util.Date> getConferenceDays(int id) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Conferences c = (data.Conferences) session.load(data.Conferences.class, id);

            Set<data.Sessions> s = (Set<data.Sessions>) c.getSessionses();

            Set<java.util.Date> dates = new HashSet<>();
            for (data.Sessions x : s) {
                if (!dates.contains(x.getDate())) {
                    dates.add(x.getDate());
                }
            }

            List<java.util.Date> ret = new ArrayList<>();
            ret.addAll(dates);

            Collections.sort(ret);

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

    public List<data.Sessions> getSessionForDay(int id, java.util.Date date) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Conferences c = (data.Conferences) session.load(data.Conferences.class, id);

            Set<data.Sessions> s = (Set<data.Sessions>) c.getSessionses();

            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

            List<data.Sessions> ret = new ArrayList<>();
            for (data.Sessions x : s) {
                if (fmt.format(date).equals(fmt.format(x.getDate()))) {
                    ret.add(x);
                }
            }

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

    public List<StreamedContent> getImages(int session_id) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        List<StreamedContent> ret = new ArrayList<>();

        try {

            Query q = session.createQuery("from Gallery as gallery");
            List<data.Gallery> gallery = (List<data.Gallery>) q.list();

            for (data.Gallery x : gallery) {
                if (x.getSessions().getId() == session_id) {
                    StreamedContent c = new DefaultStreamedContent(new ByteArrayInputStream(x.getPicture()));
                    ret.add(c);
                }
            }

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

    class AgendaSroter implements Comparator<data.Agenda> {

        @Override
        public int compare(Agenda o1, Agenda o2) {
            if (o1.getApplied().getConferences().getId() < o2.getApplied().getConferences().getId()) {
                return -1;
            } else if (o1.getApplied().getConferences().getId() > o2.getApplied().getConferences().getId()) {
                return 1;
            } else if (o1.getSessions().getDate().before(o2.getSessions().getDate())) {
                return -1;
            } else {
                return 1;
            }
        }

    }

    public List<data.Agenda> getMyAgendaA(String username) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        List<data.Agenda> ret = new ArrayList<>();

        try {

            Query q = session.createQuery("from Agenda as agenda");
            List<data.Agenda> agendas = (List<data.Agenda>) q.list();

            for (data.Agenda x : agendas) {
                if (x.getApplied().getUser().getUsername().equals(username)) {
                    ret.add(x);
                }
            }

            ret.sort(new AgendaSroter());

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

    public data.Agenda getAgenda(int agenda_id) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            Query q = session.createQuery("from Agenda as agenda where agenda.id='" + agenda_id + "'");
            List<data.Agenda> agendas = (List<data.Agenda>) q.list();

            return agendas.get(0);
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

    public void updateAgenda(int id, int liked, int rating, String comment) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {
            data.Agenda agenda = (data.Agenda) session.load(data.Agenda.class, id);

            agenda.setComment(comment);
            agenda.setGrade((byte) rating);
            agenda.setLiked((byte) liked);

            session.save(agenda);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();

        } finally {
            session.close();
        }

    }

    public void addPicture(int session_id, byte[] content) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Gallery g = new data.Gallery();

            Query q = session.createQuery("from Sessions as sessions where sessions.id='" + session_id + "'");
            List<data.Sessions> conf = (List<data.Sessions>) q.list();

            data.Sessions s = conf.get(0);//(data.Conferences) session.load(data.Conferences.class, id);      

            g.setPicture(content);
            g.setSessions(s);

            session.save(g);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();
        }
    }

    public List<data.Conferences> getConferences() {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            Query q = session.createQuery("from Conferences as conferences");
            return (List<data.Conferences>) q.list();

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

    public void removeConference(int id, String username) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.User sender = (data.User) session.load(data.User.class, username);

            data.Conferences conf = ((data.Conferences) session.load(data.Conferences.class, id));

            String message = "Sorry for the inconvenience, but the conference " + conf.getName() + " has been canceled.";

            Query q = session.createQuery("from Applied as applied");
            for (data.Applied a : (List<data.Applied>) q.list()) {
                if (a.getConferences().getId() == id) {
                    data.Messages m = new data.Messages();

                    m.setText(message);
                    m.setUserByReceiver(a.getUser());
                    m.setUserBySender(sender);
                    m.setTimestamp(new Date(Calendar.getInstance().getTimeInMillis()));

                    session.save(m);
                }
            }

            session.delete(conf);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();
        }
    }

    public void addConference(String addConferenceName, java.util.Date addConferenceStart, java.util.Date addConferenceEnd, int addConferenceLocation, java.util.Date addConferenceDue, String addConferenceField) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Places p = (data.Places) session.load(data.Places.class, addConferenceLocation);

            int f = 0;
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getName().equals(addConferenceField)) {
                    f = i;
                    break;
                }
            }

            data.Conferences conf = new data.Conferences();
            conf.setApplicationDueDate(addConferenceDue);
            conf.setName(addConferenceName);
            conf.setField(f);
            conf.setPlaces(p);

            session.save(conf);

            data.Halls hall = (data.Halls) session.load(data.Halls.class, 1);

            Query q = session.createQuery("from Conferences as conferences");

            for (data.Conferences c : (List<data.Conferences>) q.list()) {
                if (c.getField() == f && c.getName().equals(addConferenceName) && c.getApplicationDueDate().toString().equals(addConferenceDue.toString())) {
                    conf = c;
                    break;
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");

            GregorianCalendar gcal = new GregorianCalendar();
            gcal.setTime(addConferenceStart);

            while (!gcal.getTime().after(addConferenceEnd)) {
                Date d = gcal.getTime();
                System.out.println(d);
                gcal.add(Calendar.DAY_OF_YEAR, 1);

                data.Sessions s = new data.Sessions();

                s.setConferences(conf);
                s.setDate(d);
                s.setHalls(hall);
                s.setName("Unknown");
                s.setType((byte) 0);

                session.save(s);
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();
        }
    }

    public List<data.Places> getLocations() {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            Query q = session.createQuery("from Places as places");

            return (List<data.Places>) q.list();

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

    public void addModerator(int selectedConference, String selectedModerator) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.User user = (data.User) session.load(data.User.class, selectedModerator);

            data.Conferences con = (data.Conferences) session.load(data.Conferences.class, selectedConference);

            data.Moderator m = new data.Moderator();
            m.setUser(user);
            m.setConferences(con);

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

    public boolean isModerator(String username) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            Query q = session.createQuery("from Moderator as moderator");
            for (data.Moderator m : (List<data.Moderator>) q.list()) {
                if (m.getUser().getUsername().equals(username)) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();
        }

    }

    public List<data.Conferences> moderating(String username) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        List<data.Conferences> ret = new ArrayList<>();

        try {

            Query q = session.createQuery("from Moderator as moderator");
            for (data.Moderator m : (List<data.Moderator>) q.list()) {
                if (m.getUser().getUsername().equals(username)) {
                    ret.add(m.getConferences());
                }
            }

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

    public List<data.Halls> getHalls(int id) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Conferences con = (data.Conferences) session.load(data.Conferences.class, id);

            List<data.Halls> ret = new ArrayList<>();

            Query q = session.createQuery("from Halls as halls");
            for (data.Halls h : (List<data.Halls>) q.list()) {
                if (h.getPlaces().getId() == con.getPlaces().getId()) {
                    ret.add(h);
                }
            }

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

    public boolean addSession(int selected_conference_id, String newSessionName, int newSessionHall, int type) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Conferences con = (data.Conferences) session.load(data.Conferences.class, selected_conference_id);
            data.Halls hall = (data.Halls) session.load(data.Halls.class, newSessionHall);

            data.Sessions ses = null;

            Query q = session.createQuery("from Sessions as sessions");

            switch (type) {
                case 1: // opening
                    for (data.Sessions s : (List<data.Sessions>) q.list()) {
                        if (s.getConferences().getId() == selected_conference_id && (ses == null || ses.getDate().after(s.getDate()))) {
                            ses = s;
                        }
                    }

                    break;

                case 2: // closing
                    for (data.Sessions s : (List<data.Sessions>) q.list()) {
                        if (s.getConferences().getId() == selected_conference_id && (ses == null || ses.getDate().before(s.getDate()))) {
                            ses = s;
                        }
                    }

                    break;

            }

            if (ses == null) {
                return false;
            }

            if (!ses.getName().equals("Unknown")) {
                return false;
            }

            ses.setName(newSessionName);
            ses.setHalls(hall);
            ses.setType((byte) type);

            session.save(ses);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();
        }
    }

    public List<data.Sessions> getUnknownSessions(int id) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Conferences con = (data.Conferences) session.load(data.Conferences.class, id);

            List<data.Sessions> ret = new ArrayList<>();

            for (data.Sessions h : (Set<data.Sessions>) con.getSessionses()) {
                if (h.getName().equals("Unknown")) {
                    ret.add(h);
                }
            }

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

    public boolean addLectureSession(int selected_conference_id, int selected_session_id, String newSessionName, int newSessionHall) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Conferences con = (data.Conferences) session.load(data.Conferences.class, selected_conference_id);
            data.Halls hall = (data.Halls) session.load(data.Halls.class, newSessionHall);

            data.Sessions ses = (data.Sessions) session.load(data.Sessions.class, selected_session_id);

            if (ses == null) {
                return false;
            }

            if (!ses.getName().equals("Unknown")) {
                return false;
            }

            ses.setName(newSessionName);
            ses.setHalls(hall);
            ses.setType((byte) 0);

            session.save(ses);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();
        }
    }

    // public boolean addWorkshop(int selected_conference_id, int selected_session_id, String newSessionName, int newSessionHall,  ){}
    public boolean addLecture(int selected_session_id, String newLectureName, java.util.Date newLectureStartTime, int newLectureDuration) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Sessions ses = (data.Sessions) session.load(data.Sessions.class, selected_session_id);

            if (ses == null) {
                return false;
            }

            if (ses.getName().equals("Unknown") || ses.getType() == 3 || ses.getType() == 4) {
                return false;
            }

            if (newLectureDuration > 30) {
                return false;
            }

            java.util.Date end = new java.util.Date(newLectureStartTime.getTime() + 1000 * 60 * newLectureDuration);

            Query q = session.createQuery("from Lectures as lectures");

            for (data.Lectures l : (List<data.Lectures>) q.list()) {
                java.util.Date lend = new java.util.Date(l.getStart().getTime() + l.getDuration() * 1000 * 60);

                if (l.getSessions().getHalls().getId() == ses.getHalls().getId() && l.getSessions().getDate().toString().equals(ses.getDate().toString())
                        && (l.getStart().after(newLectureStartTime) && l.getStart().before(end)
                        || (lend.after(newLectureStartTime) && lend.before(end))
                        || (l.getStart().before(newLectureStartTime) && lend.after(end)))) {
                    return false;
                }
                
                if(l.getTitle().equals(newLectureName) && l.getSessions().getConferences().getId() == ses.getConferences().getId())
                    return false;

            }

            java.util.Date sessionStart = null;
            java.util.Date sessionEnd = null;
            
            boolean hasGuestLecture = false;

            for (data.Lectures l : (Set<data.Lectures>) ses.getLectureses()) {
                if (sessionStart == null || sessionStart.after(l.getStart())) {
                    sessionStart = l.getStart();
                }

                if (sessionEnd == null || sessionEnd.before(new Date(l.getStart().getTime() + 1000 * 60 * l.getDuration()))) {
                    sessionEnd = new Date(l.getStart().getTime() + 1000 * 60 * l.getDuration());
                }
                
                if(l.getDuration() > 20)
                    hasGuestLecture = true;

            }

            if (sessionStart == null || sessionStart.after(newLectureStartTime)) 
                sessionStart = newLectureStartTime;
            if(sessionEnd == null || sessionEnd.before(new Date(sessionStart.getTime() + 1000 * 60 * newLectureDuration)))
                sessionEnd = new Date(sessionStart.getTime() + 1000 * 60 * newLectureDuration);
            

            switch (ses.getType()) {
                case 1:
                case 2:
                    if ((sessionEnd.getTime() - sessionStart.getTime()) / (1000 * 60) > 60) {
                        return false;
                    }
                    break;
                    
                case 0:
                    /*
                    if ((sessionEnd.getTime() - sessionStart.getTime()) / (1000 * 60) > 3*60) {
                        return false;
                    }
                    */
                    if(hasGuestLecture && newLectureDuration > 20)
                        return false;

                    break;
                    
                default:
                    return false;
                    
            }

            data.Lectures l = new data.Lectures();
            l.setDuration(newLectureDuration);
            l.setSessions(ses);
            l.setStart(newLectureStartTime);
            l.setTitle(newLectureName);

            session.save(l);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (tx != null) {
                tx.commit();
            }
            session.close();
        }
    }

    public boolean addWorkshop(int selected_conference_id, int selected_session_id, String newSessionName, java.util.Date newLectureStartTime, int newLectureDuration, int newSessionHall) {

        if(newLectureDuration > 180)
            return false;
        
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Conferences con = (data.Conferences) session.load(data.Conferences.class, selected_conference_id);
            data.Halls hall = (data.Halls) session.load(data.Halls.class, newSessionHall);

            data.Sessions ses = (data.Sessions) session.load(data.Sessions.class, selected_session_id);

            if (ses == null) {
                return false;
            }

            if (!ses.getName().equals("Unknown")) {
                return false;
            }

            ses.setName(newSessionName);
            ses.setHalls(hall);
            ses.setType((byte) 3);

            session.save(ses);

            java.util.Date end = new java.util.Date(newLectureStartTime.getTime() + 1000 * 60 * newLectureDuration);

            Query q = session.createQuery("from Lectures as lectures");

            for (data.Lectures l : (List<data.Lectures>) q.list()) {
                java.util.Date lend = new java.util.Date(l.getStart().getTime() + l.getDuration() * 1000 * 60);

                if (l.getSessions().getHalls().getId() == ses.getHalls().getId() && l.getSessions().getDate().toString().equals(ses.getDate().toString())
                        && (l.getStart().after(newLectureStartTime) && l.getStart().before(end)
                        || (lend.after(newLectureStartTime) && lend.before(end))
                        || (l.getStart().before(newLectureStartTime) && lend.after(end)))) {
                    return false;
                }

            }

            data.Lectures l = new data.Lectures();
            l.setDuration(newLectureDuration);
            l.setSessions(ses);
            l.setStart(newLectureStartTime);
            l.setTitle(newSessionName);

            session.save(l);

            if (tx != null) {
                tx.commit();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            return false;
        } finally {

            session.close();
        }

    }

    public boolean addAuthor(int id, String user) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.User u = (data.User) session.get(data.User.class, user);

            data.Lectures l = (data.Lectures) session.load(data.Lectures.class, id);

            if (((Set<data.Author>) l.getAuthors()).size() == 4) {
                return false;
            }

            data.Author a = new data.Author();
            a.setLectures(l);
            if (u != null) {
                a.setUser(u);
                a.setName(u.getFirstName() + " " + u.getLastName() + " (" + u.getUsername() + ")");
            } else {
                a.setName(user);
            }

            session.save(a);
            
            if(u != null){
            
            Query q = session.createQuery("from Favourite as favourite");
            
            for(data.Favourite f: (List<data.Favourite>)q.list())
                if(f.getUserByContact().getUsername().equals(u.getUsername())){
                    data.Messages m = new data.Messages();
                    m.setUserBySender(u);
                    m.setUserByReceiver(f.getUserByUser());
                    m.setText("***Automated message***\nI am having a new lecture "+l.getTitle()+" which might be interesting for you");
                    session.save(m);
                }
          
                
                
                }

            if (tx != null) {
                tx.commit();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            return false;
        } finally {

            session.close();
        }

    }

    public void addBlankSession(int selected_conference_id, java.util.Date date) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Conferences con = (data.Conferences) session.load(data.Conferences.class, selected_conference_id);

            data.Halls hall = (data.Halls) session.load(data.Halls.class, 1);

            data.Sessions s = new data.Sessions();

            s.setConferences(con);
            s.setDate(date);
            s.setHalls(hall);
            s.setName("Unknown");
            s.setType((byte) 0);

            session.save(s);

            if (tx != null) {
                tx.commit();
            }

        } catch (Exception e) {

            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }

        } finally {

            session.close();
        }
    }
    
    public void isPrezenting(int id){
      Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Author a = (data.Author)session.load(data.Author.class, id);
            
            a.setIsPrezenting((byte)1);

            session.save(a);

            if (tx != null) {
                tx.commit();
            }

        } catch (Exception e) {

            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }

        } finally {

            session.close();
        }
    }
    
    public boolean addMaterial(int author_id, byte[] material, boolean material_type){
     Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        try {

            data.Author a = (data.Author)session.load(data.Author.class, author_id);
            
            if(material_type){
                if(a.getPdf() != null)
                    return false;
                a.setPdf(material);
            }
            else{
                if(a.getPpt() != null)
                    return false;
                a.setPpt(material);
            }

            session.save(a);

            if (tx != null) {
                tx.commit();
            }
            
            return true;

        } catch (Exception e) {

            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            
            return false;

        } finally {

            session.close();
        }
    }

}

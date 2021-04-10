package com.comp3004.educationmanager.db;

import com.comp3004.educationmanager.ServerState;
import com.comp3004.educationmanager.accounts.Admin;
import com.comp3004.educationmanager.accounts.Professor;
import com.comp3004.educationmanager.accounts.Student;
import com.comp3004.educationmanager.accounts.User;
import com.comp3004.educationmanager.db.repositories.*;
import com.comp3004.educationmanager.factory.AdminCreator;
import com.comp3004.educationmanager.observer.CourseData;
import com.comp3004.educationmanager.observer.SystemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class H2 implements Database {

    @Autowired
    AdminRepository ar;

    @Autowired
    StudentRepository sr;

    @Autowired
    ProfessorRepository pr;

    public H2() {

    }

    @Override
    public boolean addUser(User user) {
        //insert this user based on its type
        User entry = null;
        if(user.getClass() == Student.class)  entry = sr.save((Student) user);
        else if(user.getClass() == Professor.class) entry = pr.save((Professor) user);
        else if(user.getClass() == Admin.class) entry = ar.save((Admin) user);
        else System.out.println("HoW dId I gEt hErE?1/?");
        return !(entry == null);
    }

    @Override
    public boolean auth(String username, String password) {
        username = username == null ? "" : username;
        password = password == null ? "" : password;
        Student s = sr.findByUsername(username);
        Professor p = pr.findByUsername(username);
        return (p != null && p.getPassword().equals(password)) || (s != null && s.getPassword().equals(password) || (username.equals("admin") && password.equals("pass")));
    }
    @Override
    public void print() {
        System.out.println("---ADMINS---");
        printAdmin();
        System.out.println("---STUDENTS---");
        printStudents();
        System.out.println("---PROFS---");
        printProfessors();

    }

    public void printAdmin() {
        List<Admin> admins = ar.findAll();
        for(Admin a : admins)
            System.out.println(a.getUserId() + " | " + a.getName() + " | " + a.getPassword());
    }
    public void printStudents() {
        List<Student> students = sr.findAll();
        for(Student s : students)
            System.out.println(s.getUserId() + " | " + s.getName() + " | " + s.getPassword());
    }
    public void printProfessors() {
        List<Professor> profs = pr.findAll();
        for(Professor p : profs)
            System.out.println(p.getUserId() + " | " + p.getName() + " | " + p.getPassword());
    }

    public User getUser(String username){
        Student s = sr.findByUsername(username);
        Professor p = pr.findByUsername(username);

        if (s != null && s.getName().equals(username)){
            return (User) s;
        } else if(p != null && p.getName().equals(username)) {
            return (User) p;
        } else if(username.equals("admin")) {
            Admin a = ar.findByUsername(username);
            return (User) a;
        }
        return null;

    }
}

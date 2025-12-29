// src/com/example/StudentServlet.java
package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/register", "/show_all"})
public class StudentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Handle registration (POST /register)
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String yearStr = req.getParameter("year");

        // Basic validation
        if (name == null || email == null || yearStr == null ||
                name.trim().isEmpty() || email.trim().isEmpty() || yearStr.trim().isEmpty()) {
            req.setAttribute("error", "All fields are required.");
            RequestDispatcher rd = req.getRequestDispatcher("index.html");
            rd.forward(req, resp);
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Year must be a number.");
            RequestDispatcher rd = req.getRequestDispatcher("index.html");
            rd.forward(req, resp);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Insert student
            String sql = "INSERT INTO students (name, email, year) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name.trim());
            ps.setString(2, email.trim());
            ps.setInt(3, year);
            ps.executeUpdate();

            // Forward to success page
            req.setAttribute("message", "Student registered successfully!");
            RequestDispatcher rd = req.getRequestDispatcher("success.jsp");
            rd.forward(req, resp);

        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                req.setAttribute("error", "Email already exists.");
            } else {
                req.setAttribute("error", "Database error: " + e.getMessage());
            }
            RequestDispatcher rd = req.getRequestDispatcher("index.html");
            rd.forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Handle view all (GET /show_all)
        List<Student> students = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, name, email, year FROM students ORDER BY id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setYear(rs.getInt("year"));
                students.add(s);
            }

            req.setAttribute("students", students);
            RequestDispatcher rd = req.getRequestDispatcher("show_all.jsp");
            rd.forward(req, resp);

        } catch (Exception e) {
            resp.getWriter().println("Error retrieving students: " + e.getMessage());
        }
    }
}

// Simple POJO for student data
class Student {
    private int id;
    private String name;
    private String email;
    private int year;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}
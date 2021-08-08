package com.innoscripta.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// @WebServlet("/upload")
// @MultipartConfig
public final class FileUploadServlet extends HttpServlet {

   private static final long serialVersionUID = 1L;

    public FileUploadServlet() {}

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        // ServletFil
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("<h1> Hello, servlet </h1>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}

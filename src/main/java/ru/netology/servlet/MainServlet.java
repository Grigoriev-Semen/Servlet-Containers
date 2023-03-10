package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;
    final String baseURL = "/api/posts";
    final String idURL = "/api/posts/\\d+";

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing

            if (method.equals("GET") && path.equals(baseURL)) {
                controller.all(resp);
                return;
            }
            if (method.equals("GET") && path.matches(idURL)) {
                // easy way
                final var id = getIdFromPath(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals("POST") && path.equals(baseURL)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals("DELETE") && path.matches(idURL)) {
                // easy way
                final var id = getIdFromPath(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private static long getIdFromPath(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}


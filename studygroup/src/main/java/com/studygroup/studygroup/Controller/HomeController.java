package com.studygroup.studygroup.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return """
            <h1>StudyGroup API</h1>
            <p>Welcome to the StudyGroup application!</p>
            <h2>Available Endpoints:</h2>
            <ul>
                <li><strong>POST /api/auth/register</strong> - Register a new user</li>
                <li><strong>POST /api/auth/login</strong> - Login user</li>
                <li><strong>GET /api/groups/**</strong> - View study groups (public access)</li>
            </ul>
            <p><em>Note: Other endpoints require authentication</em></p>
            """;
    }
}

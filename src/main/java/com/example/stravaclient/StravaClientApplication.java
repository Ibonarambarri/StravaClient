package com.example.stravaclient;

import com.example.stravaclient.client.swing.SwingController;
import com.example.stravaclient.client.swing.swingGUI.Login;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.swing.*;

@SpringBootApplication(scanBasePackages = "com.example.stravaclient")

public class StravaClientApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login(new SwingController()));


    }

}

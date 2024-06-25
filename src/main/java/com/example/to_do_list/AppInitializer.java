package com.example.to_do_list;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class AppInitializer extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        DBConnection object = DBConnection.getInstance();
        Connection connection = object.getConnection();

        System.out.println(connection);

        FXMLLoader fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("LoginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("login form");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }


}

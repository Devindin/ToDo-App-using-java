package com.example.to_do_list;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public AnchorPane root;
    public TextField txtUserName;
    public PasswordField txtPassword;


   public static String enteredID;
   public static String enteredUserName;



    public void btnCreateNewAccountOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("NewUserForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage primaryStage = (Stage)root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create new Account");
        primaryStage.centerOnScreen();

    }

    public void btnLoginOnAction(ActionEvent actionEvent) {

        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement =    connection.prepareStatement("select * from user where user_name =? and password =?");
            preparedStatement.setObject(1,userName);
            preparedStatement.setObject(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
            {
                enteredID = resultSet.getString(1);
                enteredUserName = userName;
                Parent parent = FXMLLoader.load(this.getClass().getResource("ToDoListForm.fxml"));
                Scene scene = new Scene(parent);

                Stage primaryStage =(Stage) root.getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.setTitle("ToDo Form");
                primaryStage.centerOnScreen();
            }
            else {
                new Alert(Alert.AlertType.ERROR,"Invalid user name or password").showAndWait();
                txtUserName.clear();
                txtPassword.clear();

                txtUserName.requestFocus();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

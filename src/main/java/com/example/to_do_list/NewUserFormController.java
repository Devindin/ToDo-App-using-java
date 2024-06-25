package com.example.to_do_list;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class NewUserFormController {
    public Label lblID;
    public TextField txtusername;
    public TextField txtemail;
    public PasswordField txtpassword;
    public PasswordField txtconfirmpassword;
    public Button btnregister;
    public Label lblPasswordNotMatch1;
    public Label lblPasswordNotMatch2;
    public AnchorPane root;

    public void initialize()
    {
        txtusername.setDisable(true);
        txtemail.setDisable(true);
        txtpassword.setDisable(true);
        txtconfirmpassword.setDisable(true);
        btnregister.setDisable(true);
        lblPasswordNotMatch1.setVisible(false);
        lblPasswordNotMatch2.setVisible(false);
    }

    public void btnAddNewUserOnAction(ActionEvent actionEvent) {

        autoGenerateID();

        txtusername.setDisable(false);
        txtemail.setDisable(false);
        txtpassword.setDisable(false);
        txtconfirmpassword.setDisable(false);
        btnregister.setDisable(false);
    }

    public void autoGenerateID(){
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.createStatement();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select user_id from user order by user_id desc limit 1");
            boolean isExist = resultSet.next();

            if(isExist)
            {
               String oldId = resultSet.getString(1);

               int length = oldId.length();
               String id = oldId.substring(1 ,length);

               int intId = Integer.parseInt(id);
               intId = intId +1 ;

               lblID.setText("U00" + intId);

               if(intId <10)
               {
                  lblID.setText("U00" + intId);
               }
               else if( intId <100){
                   lblID.setText("U0"+ intId);
               }
               else {
                   lblID.setText("U" + intId);
               }
            }
            else
            {
                lblID.setText("U001");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void btnRegisterOnAction(ActionEvent actionEvent) {

        String newPassword = txtpassword.getText();
        String confirmPassword = txtconfirmpassword.getText();

        if(newPassword.equals(confirmPassword)){
            txtpassword.setStyle("-fx-border-color :transparent");
            txtconfirmpassword.setStyle("-fx-border-color :transparent");

            lblPasswordNotMatch1.setVisible(false);
            lblPasswordNotMatch2.setVisible(false);

            register();
        }
        else
        {
            txtpassword.setStyle("-fx-border-color :red");
            txtconfirmpassword.setStyle("-fx-border-color :red");

            lblPasswordNotMatch1.setVisible(true);
            lblPasswordNotMatch2.setVisible(true);
            txtpassword.requestFocus();
        }

    }

    public void register()
    {
        String id = lblID.getText();
        String userName = txtusername.getText();
        String email = txtemail.getText();
        String password = txtconfirmpassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedstatement = connection.prepareStatement("insert into user values(?,?,?,?)");
            preparedstatement.setObject(1,id);
            preparedstatement.setObject(2,userName);
            preparedstatement.setObject(3,email);
            preparedstatement.setObject(4,password);

            int i = preparedstatement.executeUpdate();

            if(i != 0)
            {
                new Alert(Alert.AlertType.CONFIRMATION,"Success.....").showAndWait();

                Parent parent = FXMLLoader.load(this.getClass().getResource("LoginForm.fxml"));
                Scene scene = new Scene(parent);

                Stage primaryStage =(Stage) root.getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.setTitle("Login form");
                primaryStage.centerOnScreen();
            }
            else {
                new Alert(Alert.AlertType.ERROR,"Some thing went wrong.....").showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

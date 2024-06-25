package com.example.to_do_list;

import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class ToDoFormController {
    public Label lblID;
    public Label lblTitle;
    public AnchorPane root;
    public AnchorPane subroot;
    public TextField txtToDo;
    public ListView<TodoTM> lstTodos;
    public TextField txtselectedtodo;
    public Button btndelete;
    public Button btnupdate;

    String id;

    public void initialize() {
        lblTitle.setText("Hello " + LoginFormController.enteredUserName + " Welcome to To-Do_List");
        lblID.setText(LoginFormController.enteredID);
        subroot.setVisible(false);
        btndelete.setDisable(true);
        btnupdate.setDisable(true);
        txtselectedtodo.setDisable(true);

        loadList();

        lstTodos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoTM>() {
            @Override
            public void changed(ObservableValue<? extends TodoTM> observable, TodoTM oldValue, TodoTM newValue) {
                btndelete.setDisable(false);
                btnupdate.setDisable(false);
                txtselectedtodo.setDisable(false);
                subroot.setVisible(false);

                TodoTM selectedItem = lstTodos.getSelectionModel().getSelectedItem();

                if(selectedItem == null)
                {
                    return;
                }

                subroot.setVisible(false);
                txtselectedtodo.setText(selectedItem.getDescription());

                id = selectedItem.getId();
            }
        });
    }


    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to log out..?" ,  ButtonType.YES ,  ButtonType.NO);

        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get().equals(ButtonType.YES))
        {
            Parent parent = FXMLLoader.load(this.getClass().getResource("LoginForm.fxml"));
            Scene scene = new Scene(parent);

            Stage primaryStage =(Stage) root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Form");
            primaryStage.centerOnScreen();

        }

    }

    public void btnAddNewToDo(ActionEvent actionEvent) {
        subroot.setVisible(true);

        btndelete.setDisable(true);
        btnupdate.setDisable(true);
        txtselectedtodo.setDisable(true);

        lstTodos.getSelectionModel().clearSelection();
    }

    public void btnAddToListOnAction(ActionEvent actionEvent) {

        String id = autoGenerateID();
        String description = txtToDo.getText();
        String user_id = lblID.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into todo values(?,?,?)");
            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,description);
            preparedStatement.setObject(3,user_id);

            int i = preparedStatement.executeUpdate();

            txtToDo.clear();
            subroot.setVisible(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadList();

    }

    public String autoGenerateID()
    {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.createStatement();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from todo order by id desc limit 1");
            boolean isExist = resultSet.next();

            if(isExist)
            {
                String oldId = resultSet.getString(1);

                int length = oldId.length();
                String id = oldId.substring(1 ,length);

                int intId = Integer.parseInt(id);
                intId = intId +1 ;

                if(intId <10)
                {
                    return "T00" + intId ;
                }
                else if( intId <100){
                    return "T0" + intId ;
                }
                else {
                    return "T" + intId ;
                }
            }
            else
            {
                return "T001";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadList()
    {
        ObservableList<TodoTM> todos = lstTodos.getItems();
        todos.clear();

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from todo where user_id =?");
            preparedStatement.setObject(1,lblID.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                String id = resultSet.getString(1);
                String description = resultSet.getString(2);
                String user_id = resultSet.getString(3);

                TodoTM object = new TodoTM(id,description,user_id);
                todos.add(object);
            }
            lstTodos.refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete this todo...?",ButtonType.YES,ButtonType.NO);

        Optional<ButtonType> buttonType =alert.showAndWait();

        if(buttonType.get().equals(ButtonType.YES))
        {
            Connection connection = DBConnection.getInstance().getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("delete from todo where id=?");
                preparedStatement.setObject(1,id);
                preparedStatement.executeUpdate();
                loadList();
                txtselectedtodo.clear();
                btndelete.setDisable(true);
                btnupdate.setDisable(true);
                txtselectedtodo.setDisable(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {

        Connection connection = DBConnection.getInstance().getConnection();


        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update todo set description = ? where id =?");
            preparedStatement.setObject(1,txtselectedtodo.getText());
            preparedStatement.setObject(2,id);
            preparedStatement.executeUpdate();
            loadList();
            txtselectedtodo.clear();
            btndelete.setDisable(true);
            btnupdate.setDisable(true);
            txtselectedtodo.setDisable(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

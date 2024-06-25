module com.example.to_do_list {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.to_do_list to javafx.fxml;
    exports com.example.to_do_list;
}
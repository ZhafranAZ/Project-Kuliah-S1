// File: MainController.java
// package application;
// import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private Button viewdata;

    public static void showMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("MainMenu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Menu Utama");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleRead() {
        try {
            Stage st = (Stage) viewdata.getScene().getWindow();
            st.close();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Read.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("View Data");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleEdit() {
        try {
            Stage st = (Stage) viewdata.getScene().getWindow();
            st.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Edit.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Edit Data");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

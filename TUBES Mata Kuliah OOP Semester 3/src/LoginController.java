// File: LoginController.java


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private List<String> user = Arrays.asList("Zhafran", "Meisya", "Agus");

    public void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if((user.contains(username) && password.equals("admin123"))) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Selamat datang, " + username + "!");
            alert.showAndWait();

            // Pindah ke halaman utama
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
            MainController.showMainMenu();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Gagal");
            alert.setHeaderText(null);
            alert.setContentText("Username atau password salah!");
            alert.showAndWait();
        }
    }
// Niatnya mau pakai data users di database
    // private boolean validateLogin(String username, String password) {
    //     try (Connection conn = DatabaseHelper.connect()) {
    //         String query = "SELECT * FROM users WHERE username = ? AND password = ?";
    //         PreparedStatement stmt = conn.prepareStatement(query);
    //         stmt.setString(1, username);
    //         stmt.setString(2, password);
    //         ResultSet rs = stmt.executeQuery();

    //         return rs.next();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return false;
    // }
}

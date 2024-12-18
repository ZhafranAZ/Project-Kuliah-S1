// package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
// import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

// import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditController {

    @FXML
    private TextField studioNameField;
    @FXML
    private TableView<Studio> studioTable;
    @FXML
    private TableColumn<Studio, Integer> studioIdColumn;
    @FXML
    private TableColumn<Studio, String> studioNameColumn;

    @FXML
    private TextField filmTitleField;
    @FXML
    private TextField filmPosterField;
    @FXML
    private TextField filmRatingField;
    @FXML
    private TextField filmActorsField;
    @FXML
    private TextField filmDirectorField;
    @FXML
    private TextField filmDateField;
    @FXML
    private TextField filmCinemaField;
    @FXML
    private TextField filmGenreField;
    @FXML
    private ChoiceBox<String> filmStatusField;
    @FXML
    private TableView<Film> filmTable;
    @FXML
    private TableColumn<Film, Integer> filmIdColumn;
    @FXML
    private TableColumn<Film, String> filmTitleColumn;
    @FXML
    private TableColumn<Film, Double> filmRatingColumn;
    @FXML
    private TableColumn<Film, String> filmStatusColumn;
    @FXML
    private TableColumn<Film, String> filmGenreColumn;
    @FXML
    private ImageView posterImageView;

    private ObservableList<Studio> studioList = FXCollections.observableArrayList();
    private ObservableList<Film> filmList = FXCollections.observableArrayList();
    // private TextInputControl filmGenreField;
    
    @FXML
    public void initialize() {
        // Konfigurasi kolom tabel
        studioIdColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        studioNameColumn.setCellValueFactory(data -> data.getValue().namaStudioProperty());
        filmIdColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        filmTitleColumn.setCellValueFactory(data -> data.getValue().judulProperty());
        filmRatingColumn.setCellValueFactory(data -> data.getValue().ratingProperty().asObject());
        filmStatusColumn.setCellValueFactory(data -> data.getValue().statusTayangProperty());
        filmGenreColumn.setCellValueFactory(data -> data.getValue().genreProperty());

        // Load data awal
        loadStudios();
        filmStatusField.setItems(FXCollections.observableArrayList("Selesai Tayang", "Sedang Tayang", "Belum Tayang"));

        studioTable.setOnMouseClicked(this::handleStudioSelection);
        filmTable.setOnMouseClicked(this::handleFilmSelection);
    }
    
    private void loadStudios() {
        studioList.clear();
        try (Connection connection = DatabaseHelper.connect()) {
            String query = "SELECT * FROM studio";
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String namaStudio = resultSet.getString("namaStudio");
                studioList.add(new Studio(id, namaStudio));
            }
            studioTable.setItems(studioList);
        } catch (Exception e) {
            showErrorDialog("Error loading studios", e.getMessage());
        }
    }
    
    private void loadFilms(int studioId) {
        filmList.clear();
        try (Connection connection = DatabaseHelper.connect()) {
            String query = "SELECT * FROM film WHERE studio_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studioId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String judul = resultSet.getString("judul");
                String poster = resultSet.getString("poster");
                double rating = resultSet.getDouble("rating");
                String aktor = resultSet.getString("aktor");
                    
                String sutradara = resultSet.getString("sutradara");
                String tanggalTayang = resultSet.getString("tanggalTayang");
                String bioskop = resultSet.getString("bioskop");
                String statusTayang = resultSet.getString("statusTayang");
                String genre = resultSet.getString("genre");  
        
                filmList.add(new Film(id, judul, poster, rating, aktor, sutradara, tanggalTayang, bioskop, statusTayang, genre));
            }
                filmTable.setItems(filmList);
        } catch (Exception e) {
            showErrorDialog("Error loading films", e.getMessage());
        }
    }
        
    
    @FXML
    private void handleCreateStudio() {
        String namaStudio = studioNameField.getText();
        if (namaStudio.isEmpty()) {
            showErrorDialog("Validation Error", "Nama studio tidak boleh kosong.");
            return;
        }
    
        try (Connection connection = DatabaseHelper.connect()) {
            String query = "INSERT INTO studio (namaStudio) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, namaStudio);
            statement.executeUpdate();

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Create Success", "Studio berhasil dibuat.");
            loadStudios();  
            } else {
            showAlert(Alert.AlertType.ERROR, "Create Failed", "Studio gagal dibuat.");
            }
        } catch (SQLException e) {
            showErrorDialog("Error creating studio", e.getMessage());
        }
    }
    
    @FXML
    private void handleUpdateStudio() {
        Studio selectedStudio = studioTable.getSelectionModel().getSelectedItem();
        if (selectedStudio == null) {
            showErrorDialog("Validation Error", "Pilih studio yang akan diperbarui.");
            return;
        }
    
        String namaStudio = studioNameField.getText();
        if (namaStudio.isEmpty()) {
            showErrorDialog("Validation Error", "Nama studio tidak boleh kosong.");
            return;
        }
    
        try (Connection connection = DatabaseHelper.connect()) {
            String query = "UPDATE studio SET namaStudio = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, namaStudio);
            statement.setInt(2, selectedStudio.getId());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Update Success", "Studio berhasil diperbarui.");
            loadStudios();  // Memuat ulang daftar studio setelah pembaruan
            } else {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Studio gagal diperbarui.");
            }
        } catch (SQLException e) {
            showErrorDialog("Error updating studio", e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteStudio() {
        Studio selectedStudio = studioTable.getSelectionModel().getSelectedItem();
        if (selectedStudio == null) {
            showErrorDialog("Validation Error", "Pilih studio yang akan dihapus.");
            return;
        }

        try (Connection connection = DatabaseHelper.connect()) {
        // Delete studio jika tidak ada film terkait
            String query = "DELETE FROM studio WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, selectedStudio.getId());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Delete Success", "Studio berhasil dihapus.");
                loadStudios();  // Memuat ulang daftar studio
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Failed", "Studio gagal dihapus.");
            }
        } catch (SQLException e) {
            showErrorDialog("Error deleting studio", e.getMessage());
        }
    }

    
    @FXML
    private void handleCreateFilm() {
        String judul = filmTitleField.getText();
        String poster = filmPosterField.getText();
        double rating;
        try {
            rating = Double.parseDouble(filmRatingField.getText());
        } catch (NumberFormatException e) {
            showErrorDialog("Validation Error", "Rating harus berupa angka.");
            return;
        }
        String aktor = filmActorsField.getText();
        String sutradara = filmDirectorField.getText();
        String tanggalTayang = filmDateField.getText();
        String bioskop = filmCinemaField.getText();
        String statusTayang = filmStatusField.getValue();
        String genre = filmGenreField.getText();  
        
        Studio selectedStudio = studioTable.getSelectionModel().getSelectedItem();
        if (selectedStudio == null) {
            showErrorDialog("Validation Error", "Pilih studio untuk menambahkan film.");
            return;
        }
        
        try (Connection connection = DatabaseHelper.connect()) {
            String query = "INSERT INTO film (judul, poster, rating, aktor, sutradara, tanggalTayang, bioskop, statusTayang, genre, studio_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, judul);
            statement.setString(2, poster);
            statement.setDouble(3, rating);
            statement.setString(4, aktor);
            statement.setString(5, sutradara);
            statement.setString(6, tanggalTayang);
            statement.setString(7, bioskop);
            statement.setString(8, statusTayang);
            statement.setString(9, genre);  // Memasukkan genre ke dalam query
            statement.setInt(10, selectedStudio.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // Menampilkan pesan berhasil
                showAlert(Alert.AlertType.INFORMATION, "Create Success", "Film berhasil ditambahkan.");
                loadFilms(selectedStudio.getId());
            } else {
                // Menampilkan pesan gagal
                showAlert(Alert.AlertType.ERROR, "Create Failed", "Film gagal ditambahkan.");
            }
        } catch (SQLException e) {
            showErrorDialog("Error creating film", e.getMessage());
        }
    }
        
    
    @FXML
    private void handleUpdateFilm() {
        Film selectedFilm = filmTable.getSelectionModel().getSelectedItem();
        Studio selectedStudio = studioTable.getSelectionModel().getSelectedItem();
        
    if (selectedFilm == null || selectedStudio == null) {
        showErrorDialog("Validation Error", "Pilih studio dan film yang akan diperbarui.");
            return;
        }
    
        String judul = filmTitleField.getText();
        String poster = filmPosterField.getText();
        double rating;
        try {
            rating = Double.parseDouble(filmRatingField.getText());
        } catch (NumberFormatException e) {
            showErrorDialog("Validation Error", "Rating harus berupa angka.");
            return;
        }
        String aktor = filmActorsField.getText();
        String sutradara = filmDirectorField.getText();
        String tanggalTayang = filmDateField.getText();
        String bioskop = filmCinemaField.getText();
        String statusTayang = filmStatusField.getValue();
        String genre = filmGenreField.getText(); 
    
        try (Connection connection = DatabaseHelper.connect()) {
            String query = "UPDATE film SET judul = ?, poster = ?, rating = ?, aktor = ?, sutradara = ?, tanggalTayang = ?, bioskop = ?, statusTayang = ?, genre = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, judul);
            statement.setString(2, poster);
            statement.setDouble(3, rating);
            statement.setString(4, aktor);
            statement.setString(5, sutradara);
            statement.setString(6, tanggalTayang);
            statement.setString(7, bioskop);
            statement.setString(8, statusTayang);
            statement.setString(9, genre);
            statement.setInt(10, selectedFilm.getId());
            statement.executeUpdate();

            int rowsAffected = statement.executeUpdate();  // Menyimpan jumlah baris yang terpengaruh

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Update Success", "Film berhasil diperbarui.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Film gagal diperbarui.");
            }

            loadFilms(selectedStudio.getId());

        } catch (SQLException e) {
            showErrorDialog("Error updating film", e.getMessage());
        }
    }
    

    @FXML
    private void handleDeleteFilm() {
        Film selectedFilm = filmTable.getSelectionModel().getSelectedItem();
        Studio selectedStudio = studioTable.getSelectionModel().getSelectedItem();
        
    if (selectedFilm == null || selectedStudio == null) {
        showErrorDialog("Validation Error", "Pilih studio dan film yang akan dihapus.");
            return;
        }
    
        try (Connection connection = DatabaseHelper.connect()) {
            String query = "DELETE FROM film WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, selectedFilm.getId());
            int rowsAffected = statement.executeUpdate();  // Menyimpan jumlah baris yang terpengaruh

            if (rowsAffected > 0) {
            showAlert(Alert.AlertType.INFORMATION, "Delete Success", "Film berhasil diDelete.");
            } else {
            showAlert(Alert.AlertType.ERROR, "Delete Failed", "Film gagal diDelete.");
            }

            loadFilms(selectedStudio.getId());
        } catch (SQLException e) {
            showErrorDialog("Error deleting film", e.getMessage());
        }
    }
    
    private void handleStudioSelection(MouseEvent event) {
        Studio selectedStudio = studioTable.getSelectionModel().getSelectedItem();
        if (selectedStudio != null) {
            studioNameField.setText(selectedStudio.getNamaStudio());
            loadFilms(selectedStudio.getId());
        }
    }
    
    private void handleFilmSelection(MouseEvent event) {
        Film selectedFilm = filmTable.getSelectionModel().getSelectedItem();
        if (selectedFilm != null) {
            filmTitleField.setText(selectedFilm.getJudul());
            filmPosterField.setText(selectedFilm.getPoster());
            filmRatingField.setText(String.valueOf(selectedFilm.getRating()));
            filmActorsField.setText(selectedFilm.getAktor());
            filmDirectorField.setText(selectedFilm.getSutradara());
            filmDateField.setText(selectedFilm.getTanggalTayang());
            filmCinemaField.setText(selectedFilm.getBioskop());
            filmStatusField.setValue(selectedFilm.getStatusTayang());
            filmGenreField.setText(selectedFilm.getGenre());

            // Load poster image
            try {
                Image image = new Image(selectedFilm.getPoster());
                posterImageView.setImage(image);
                
            } catch (Exception e) {
                posterImageView.setImage(null);
            }
    
        } else {
            clearFilmDetails();
        }
    }
    
    
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        Stage st = (Stage) filmActorsField.getScene().getWindow();
        st.close();
        MainController.showMainMenu();
    }
    
    public void clearFilmDetails() {
        filmTitleField.clear();
        filmRatingField.clear();
        filmActorsField.clear();
        filmDirectorField.clear();
        filmCinemaField.clear();
        filmStatusField.setValue(null);
        filmPosterField.clear();
        filmDateField.clear();
        filmGenreField.clear();
        posterImageView.setImage(null);
}

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

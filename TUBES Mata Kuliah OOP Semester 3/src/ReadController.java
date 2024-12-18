// package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReadController {

    @FXML
    private TableView<Studio> studioTable;
    @FXML
    private TableColumn<Studio, Integer> studioIdColumn;
    @FXML
    private TableColumn<Studio, String> studioNameColumn;
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
    @FXML
    private Label judulLabel;
    @FXML
    private Label aktorLabel;
    @FXML
    private Label sutradaraLabel;
    @FXML
    private Label tanggalTayangLabel;
    @FXML
    private Label bioskopLabel;
    @FXML
    private Label statusTayangLabel;
    @FXML
    private Label GenreLabel;

    private ObservableList<Studio> studioList = FXCollections.observableArrayList();
    private ObservableList<Film> filmList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Konfigurasi kolom tabel studio
        studioIdColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        studioNameColumn.setCellValueFactory(data -> data.getValue().namaStudioProperty());

        // Konfigurasi kolom tabel film
        filmIdColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        filmTitleColumn.setCellValueFactory(data -> data.getValue().judulProperty());
        filmRatingColumn.setCellValueFactory(data -> data.getValue().ratingProperty().asObject());
        filmStatusColumn.setCellValueFactory(data -> data.getValue().statusTayangProperty());

        // Konfigurasi kolom genre
        filmGenreColumn.setCellValueFactory(data -> data.getValue().genreProperty());

        // Load data studio ke tabel
        loadStudios();

        // Listener untuk memilih studio
        studioTable.setOnMouseClicked(this::handleStudioSelection);
    }

    private void loadStudios() {
        studioList.clear();
        try (Connection connection = DatabaseHelper.connect()) {
            String query = "SELECT * FROM studio";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
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

            // Listener untuk memilih film
            filmTable.setOnMouseClicked(this::handleFilmSelection);

        } catch (Exception e) {
            showErrorDialog("Error loading films", e.getMessage());
        }
    }

    private void handleStudioSelection(MouseEvent event) {
        Studio selectedStudio = studioTable.getSelectionModel().getSelectedItem();
        if (selectedStudio != null) {
            loadFilms(selectedStudio.getId());
        }
    }

    private void handleFilmSelection(MouseEvent event) {
        Film selectedFilm = filmTable.getSelectionModel().getSelectedItem();
        if (selectedFilm != null) {
            displayFilmDetails(selectedFilm);
        } else {
            clearFilmDetails();
        }
    }

    private void displayFilmDetails(Film film) {
        judulLabel.setText(film.getJudul());
        aktorLabel.setText(film.getAktor());
        sutradaraLabel.setText(film.getSutradara());
        tanggalTayangLabel.setText(film.getTanggalTayang());
        bioskopLabel.setText(film.getBioskop());
        statusTayangLabel.setText(film.getStatusTayang());
        GenreLabel.setText(film.getGenre());


        // Load poster image
        try {
            Image image = new Image(film.getPoster());
            posterImageView.setImage(image);
            
        } catch (Exception e) {
            posterImageView.setImage(null);
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
        Stage st = (Stage) aktorLabel.getScene().getWindow();
        st.close();
        MainController.showMainMenu();
    }

    public void clearFilmDetails() {
        judulLabel.setText("");
        aktorLabel.setText("");
        sutradaraLabel.setText("");
        tanggalTayangLabel.setText("");
        bioskopLabel.setText("");
        statusTayangLabel.setText("");
        GenreLabel.setText("");
        posterImageView.setImage(null);
}
}

// package application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Film {
    private SimpleIntegerProperty id;
    private SimpleStringProperty judul;
    private SimpleStringProperty poster;
    private SimpleDoubleProperty rating;
    private SimpleStringProperty aktor;
    private SimpleStringProperty sutradara;
    private SimpleStringProperty tanggalTayang;
    private SimpleStringProperty bioskop;
    private SimpleStringProperty statusTayang;
    private SimpleStringProperty genre;

    public Film(int id, String judul, String poster, double rating, String aktor, String sutradara,
                String tanggalTayang, String bioskop, String statusTayang, String genre) {
        this.id = new SimpleIntegerProperty(id);
        this.judul = new SimpleStringProperty(judul);
        this.poster = new SimpleStringProperty(poster);
        this.rating = new SimpleDoubleProperty(rating);
        this.aktor = new SimpleStringProperty(aktor);
        this.sutradara = new SimpleStringProperty(sutradara);
        this.tanggalTayang = new SimpleStringProperty(tanggalTayang);
        this.bioskop = new SimpleStringProperty(bioskop);
        this.statusTayang = new SimpleStringProperty(statusTayang);
        this.genre = new SimpleStringProperty(genre);  // Ubah genre2 menjadi genre
    }


    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getJudul() {
        return judul.get();
    }

    public SimpleStringProperty judulProperty() {
        return judul;
    }

    public String getPoster() {
        return poster.get();
    }

    public SimpleStringProperty posterProperty() {
        return poster;
    }

    public double getRating() {
        return rating.get();
    }

    public SimpleDoubleProperty ratingProperty() {
        return rating;
    }

    public String getAktor() {
        return aktor.get();
    }

    public SimpleStringProperty aktorProperty() {
        return aktor;
    }

    public String getSutradara() {
        return sutradara.get();
    }

    public SimpleStringProperty sutradaraProperty() {
        return sutradara;
    }

    public String getTanggalTayang() {
        return tanggalTayang.get();
    }

    public SimpleStringProperty tanggalTayangProperty() {
        return tanggalTayang;
    }

    public String getBioskop() {
        return bioskop.get();
    }

    public SimpleStringProperty bioskopProperty() {
        return bioskop;
    }

    public String getStatusTayang() {
        return statusTayang.get();
    }

    public SimpleStringProperty statusTayangProperty() {
        return statusTayang;
    }

    public String getGenre() {
        return genre.get();
    }

    public SimpleStringProperty genreProperty() {
        return genre;
    }

    
}

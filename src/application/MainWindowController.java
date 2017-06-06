/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.Mapa;

public class MainWindowController implements Initializable {
    
    File wynik;
    File selectedFile;
    @FXML
    ImageView inputImage;
    @FXML
    ImageView outputImage;
    
    @FXML
    private void handleGenerate(ActionEvent event) {
        BufferedImage mapa;
        try {
            mapa = ImageIO.read(selectedFile);
            Mapa mapka = new Mapa(mapa, this);
            wynik = new File("result.png");
            ImageIO.write(mapka.getColoredImage(), "png", wynik);
            Image img = new Image(wynik.toURI().toString());
            outputImage.setImage(img);
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("B³¹d");
            alert.setHeaderText("Nieprawid³owe u¿ycie kolorowanki ;p");
            alert.setContentText("Proszê najpierw wybraæ mapê");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleSelect(ActionEvent event) {
        
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Obrazy", "*.png", "*.jpg", "*.gif", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Open Graphic File");
        selectedFile = fileChooser.showOpenDialog(Main.getStage());
        
        if (selectedFile != null) {
            Image img = new Image(selectedFile.toURI().toString());
            inputImage.setImage(img);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}

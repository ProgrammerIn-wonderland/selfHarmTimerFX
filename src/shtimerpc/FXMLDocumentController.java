/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shtimerpc;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Dialog;


/**
 *
 * @author Neal
 */
public class FXMLDocumentController implements Runnable, Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Label Header;
    @FXML
    private Label footer;
    
    private Thread thread = null;
    private String time = "", month = "", day = "";
    private SimpleDateFormat format;
    private Date date;
    private Calendar calendar;
    
    private Long tor; 

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            String stor =  new String(Files.readAllBytes(Paths.get("lr")));
            tor = Long.parseLong(stor);
            thread = new Thread(this);
            thread.start();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void relapse() throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Reset Timer");
        alert.setHeaderText("Would you like to reset your timer?");
        alert.setContentText("this action is irreversable!");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()  == ButtonType.OK) {
            String path = "lr";
            tor = Calendar.getInstance().getTime().getTime()/1000;
            Files.write(Paths.get(path), tor.toString().getBytes());
            System.out.println("Relapsed");
        }

    }
    
    public void run() {
        try {
            while (true) {

                //Setting date format and variables:
                
                calendar = Calendar.getInstance();
                Long tsr = calendar.getTime().getTime()/1000 - tor;
                
                
                format = new SimpleDateFormat("hh:mm:ss a");
                date = calendar.getTime();
                time = format.format(date);

                format = new SimpleDateFormat("MMMM dd yyyy");
                date = calendar.getTime();
                month = format.format(date);

                format = new SimpleDateFormat("EEEE");
                date = calendar.getTime();
                day = format.format(date);

                //Setting elements to pane:

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Double days = Math.floor(tsr/86400);
                        Double hours = Math.floor((tsr/3600)- (days * 24) );
                        Double minutes = Math.floor(tsr/60 - (days * 1440) -  (hours * 60));
                        Double seconds =  Math.floor(tsr - (days * 86400) -  (hours * 3600) - (minutes * 60));;
                        Header.setText(days.longValue() + " Days");
                        footer.setText(hours.longValue() + " hours " + minutes.longValue() + " minutes " + seconds.longValue() + " seconds");

                    }
                });

                Thread.sleep(1000);
            }
        } catch (Exception e) { //Error check
            Header.setText("");
            Header.setText("Error occurred!!");
            Header.setText("");
        }
    }

}

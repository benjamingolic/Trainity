
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trainity;

import com.gluonhq.charm.glisten.application.MobileApplication;
import static com.trainity.EinheitSession.instanceE;
import static com.trainity.Trainity.EINHEIT_BEARBEITEN_VIEW;
import static com.trainity.Trainity.UEBUNG_ALL;
import static com.trainity.Uebung.printSQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author julian
 */
public class BoxDynamischBlauGroß extends HBox {

    private String name;
    private int duration;
    private String beschreibung;
    private int id; 
    
    private static final String DATABASE_URL = "jdbc:mysql://localhost:8889/Trainity?serverTimezone=" + TimeZone.getDefault().getID();
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "root";

    public BoxDynamischBlauGroß(String name, int duration, String beschreibung, int id) {

        this.setName(name);
        this.setDuration(duration);
        this.setBeschreibung(beschreibung);
        this.setId(id);
        addContent();

    }

    public void addContent() {

//-------------------------------------------------------------------------------------
//HBOX AUSSEN
        HBox hbox = new HBox();

        hbox.setPrefWidth(300);
        hbox.setPrefHeight(68);
        //Cursor
        hbox.setCursor(Cursor.HAND);

        //Background-Color
        //hbox.setBackground(new Background(new BackgroundFill(Color.rgb(64, 194, 17), CornerRadii.EMPTY, Insets.EMPTY)));
        hbox.setStyle("        -fx-border-color: rgb(33, 150, 243);"
                + "        -fx-border-radius: 5;" + "       -fx-background-radius: 5;" + "-fx-background-color: rgb(33, 150, 243);"
        );

        /*
        EventHandler<MouseEvent> mouseEventFilter = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              //  System.out.println("Mouse event filter has been called");
            }
        };
         */
        EventHandler<MouseEvent> mouseEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //  System.out.println("Mouse event handler has been called");
                
                instanceE = null;
                EinheitSession.getInstace(id);
                MobileApplication.getInstance().switchView(UEBUNG_ALL);
//UEBUNG_ALL
            }
        };

        hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandler);

//-------------------------------------------------------------------------------------
//Image View erstellen und als Children in HBox hinzufügen
        

        String imgName = "";
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT bildName FROM trainingseinheit WHERE  trainingseinheit_id = '" + id + "'")) {
            //preparedStatement.setString(1, searchString);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                imgName = rs.getString("bildName");

                ///___________------------------
                // Sout Ausgabe später löschen 
                //   System.out.println(trainingsuebung_id);
                // System.out.println(trainingsname);
                //System.out.println(rep);
                //   System.out.println(beschreibung);
                if (imgName == null) {

                    imgName = "051-athlete.png";

                }

            }

        } catch (SQLException e) {
            printSQLException(e);
        }

        String wholeName = "icon/" + imgName;

        Image image = new Image("" + wholeName + "");

        ImageView iv1 = new ImageView();

        iv1.setFitWidth(30);
        iv1.setFitHeight(30);

        iv1.setImage(image);

        hbox.getChildren().add(iv1);

//-------------------------------------------------------------------------------------
//Dünnes Pane erstellen    
        HBox pane = new HBox();

        pane.prefWidth(1);
        pane.prefHeight(30);

        //Nimmt die unten angeführten Methoden nicht an 
        //      pane.setStyle( "       -fx-background-color:white;");
        //  pane.setBorder(new Border(new BorderStroke(Color.WHITE, 
        //       BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        pane.setStyle("       -fx-background-color:white;" + "-fx-border-color:white;" + "  -fx-background-insets: 0 0 -1 0, 0, 1, 2;"
                + "  -fx-background-radius: 1px, 1px, 1px, 1px;");

        pane.setOpacity(0.67);

        hbox.getChildren().add(pane);

//-------------------------------------------------------------------------------------
//Dünne VBOX erstellen    
        VBox vbox = new VBox();

        vbox.setPrefWidth(140);
        vbox.setPrefHeight(32.5);

//-------------------------------------------------------------------------------------
//LABEL 1 erstellen     (Name)
        Label label = new Label(getName());

        label.setTextFill(Color.WHITE);

        // label.setFont(Font.font("Cambria", 32));
        vbox.getChildren().add(label);

        vbox.setMargin(label, new Insets(8, 0, 0, 10));

        hbox.getChildren().add(vbox);

//-------------------------------------------------------------------------------------      
        //HBOX
        HBox hb2 = new HBox();

        hb2.setPrefWidth(30);
        hb2.setPrefHeight(15);

//-------------------------------------------------------------------------------------
//Image View erstellen und als Children in HBox hinzufügen
        Image image2 = new Image("pictures/zukunft_icon_white.png");

        ImageView iv2 = new ImageView();

        iv2.setFitWidth(15);
        iv2.setFitHeight(15);

        iv2.setImage(image2);

        hb2.getChildren().add(iv2);

        vbox.getChildren().add(hb2);

//-------------------------------------------------------------------------------------
//LABEL 2 erstellen    (Rep)
        Label label2 = new Label(getDuration() + " min");

        label2.setTextFill(Color.WHITE);
        label2.setOpacity(0.80);

        hb2.getChildren().add(label2);

        hb2.setMargin(label2, new Insets(0, 0, 2, 2));

        hb2.setMargin(iv2, new Insets(0, 0, 4, 12));

//-------------------------------------------------------------------------------------
//Stift IMG     
        Image image3 = new Image("pictures/icons8-edit-384white_1.png");

        ImageView iv3 = new ImageView();

        iv3.setFitWidth(23);
        iv3.setFitHeight(26);

        iv3.setImage(image3);

        hbox.getChildren().add(iv3);
        hbox.setMargin(iv3, new Insets(20, 0, 0, 47));


        /* 
           EventHandler<MouseEvent> mouseEventHandler2 = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("EDIT THIS ....");
            }
        };
        iv3.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandler2);
             
         */
//-------------------------------------------------------------------------------------
//Margin       
        // Oben Rechts Unten Links
        hbox.setMargin(iv1, new Insets(18.5, 0, 0, 6));

        hbox.setMargin(pane, new Insets(11, 0, 11, 6));

        hbox.setMargin(vbox, new Insets(7, 0, 0, 1));

        this.getChildren().add(hbox);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }
    
    private void setId(int id) {
        this.id = id;
    }
}
package com.trainity.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.trainity.BoxDynamischBlauKlein;
import com.trainity.Trainity;
import static com.trainity.Trainity.LOGIN_VIEW;
import static com.trainity.views.WochenplanPresenter.printSQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javafx.scene.paint.Color;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.util.Date;
import java.util.Locale;
import javafx.scene.text.Text;

public class StartseitePresenter {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/trainity?zeroDateTimeBehavior=convertToNull";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "";

    private static final String SELECT_QUERY = "select status from ziel where benutzer_id = ? and datum BETWEEN ? and ?";

    @FXML
    private View startseite;

    @FXML
    private VBox mo;
    @FXML
    private VBox di;
    @FXML
    private VBox mi;
    @FXML
    private VBox don;
    @FXML
    private VBox fr;
    @FXML
    private VBox sa;
    @FXML
    private VBox so;
    @FXML
    private ImageView calendarMo;
    @FXML
    private ImageView checkMo;
    @FXML
    private ImageView calendarDi;
    @FXML
    private ImageView checkDi;
    @FXML
    private ImageView calendarMi;
    @FXML
    private ImageView checkMi;
    @FXML
    private ImageView calendarDo;
    @FXML
    private ImageView checkDo;
    @FXML
    private ImageView calendarFr;
    @FXML
    private ImageView checkFr;
    @FXML
    private ImageView calendarSa;
    @FXML
    private ImageView checkSa;
    @FXML
    private ImageView calendarSo;
    @FXML
    private ImageView checkSo;
    @FXML
    private VBox nextTraining;
    @FXML
    private VBox lastTraining;
    @FXML
    private Text moText;
    @FXML
    private Text diText;
    @FXML
    private Text miText;
    @FXML
    private Text doText;
    @FXML
    private Text frText;
    @FXML
    private Text saText;
    @FXML
    private Text soText;

    public void initialize() throws SQLException {
        startseite.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e
                        -> MobileApplication.getInstance().getDrawer().open()));
                appBar.setTitleText("Startseite");
                appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e
                        -> System.out.println("Search")));
            }
        });
        Date date = new Date();
        switch (date.getDay()) {
            case (1):
                setDay(mo, moText);
                break;
            case (2):
                setDay(di, diText);
                break;
            case (3):
                setDay(mi, miText);
                break;
            case (4):
                setDay(don, doText);
                break;
            case (5):
                setDay(fr, frText);
                break;
            case (6):
                setDay(sa, saText);
                break;
            case (7):
                setDay(so, soText);
                break;
            default:
                setDay(mo, moText);
                break;
        }
        //Nächstes Training aus DB holen 
        nextTraining.getChildren().add(new BoxDynamischBlauKlein("Nächste Trainingseinheit", 20, "Plan"));

        //Letzte Trainings aus DB holen
        lastTraining.getChildren().add(new BoxDynamischBlauKlein("Letzte Trainingseinheit", 20, "Plan"));

        
        //Geplante Tage aus Tabelle Ziel anzeigen 
        //Calendar c anlegen und ersten Tag der Woche setzen
        Calendar c = Calendar.getInstance(Locale.GERMAN);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        
        //Datum auf ersten Tag der Woche setzen 
        ImageView[] check = {checkMo, checkDi, checkMi, checkDo, checkFr, checkSa, checkSo};
        ImageView[] calendar = {calendarMo, calendarDi, calendarMi, calendarDo, calendarFr, calendarSa, calendarSo};
        
        String monday = c.get(Calendar.YEAR) + "-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DAY_OF_WEEK, 6);
        String sunday = c.get(Calendar.YEAR) + "-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
        
        try (
                Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
                // Step 2:Create a statement using connection object
                PreparedStatement selectStmt = connection.prepareStatement(SELECT_QUERY);
            ) {
                //Benutzer_ID setzen und überprüfen
                //Später ändern
                selectStmt.setInt(1, 1);
                //Datum für Abfrage speichern
                selectStmt.setString(2, monday);
                selectStmt.setString(3, sunday);
                // Step 3: Execute the query or update query
                ResultSet result = selectStmt.executeQuery();
                
                for (int i = 0; i < 7; i++){
                    result.next();
                    setPlanned(check[i], calendar[i], result.getString(1));
                    
                }
        } catch (SQLException e) {
            // print SQL exception information
            System.out.println(e.getMessage());
        }
        
        
    }

    public void setDay(VBox box, Text text) {
        box.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 5, 0, 0, 0);");
        text.setFill(Color.web("#2196f3"));
    }

    
    

    public void setPlanned(ImageView check, ImageView calendar, String result) {   
                    /*switch (result){
                        case "0": calendar.setVisible(false);
                            break;
                        case "1": check.setVisible(false);
                            break;
                        case "null": 
                            calendar.setVisible(false);
                        break;
                        default: check.setVisible(false);
                            break;
                    }*/
                    System.out.println(result);
                    if (result == null){
                        check.setVisible(false);
                        calendar.setVisible(false);
                    } else {
                        if (result.equals("1")) {
                            check.setVisible(false);
                        } else {
                            calendar.setVisible(false);
                        }
                    }
                }
        
     
    
    /**
     *
     * @param ex
     */
    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}

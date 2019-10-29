package sample;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

//import java.awt.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import sample.db.db_check_in;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import static sample.db.db_check_in.connect;

public class Controller implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**************** INITIALISATION ******************/

        /******************* COMBOBOX *********************/

        combo();

        /******************* PANE ********************/

        lbl_r.setVisible(false);
        lbl_r1.setVisible(false);
        participents_pan.setVisible(false);
        emails_pan.setVisible(false);
        events_pan.setVisible(false);

        /**************** TABEL_CHECK_IN ******************/
        update_table();


        id_p.setCellValueFactory(new PropertyValueFactory<>("id_p"));

        f_name_p.setCellValueFactory(new PropertyValueFactory<>("f_name_p"));

        l_name_p.setCellValueFactory(new PropertyValueFactory<>("l_name_p"));

        email_p.setCellValueFactory(new PropertyValueFactory<>("email_p"));

        nbr_p_p.setCellValueFactory(new PropertyValueFactory<>("nbr_p_p"));

        table.setItems(oblist);
    }

    /**************** PANE ******************/

    @FXML
    Pane checkin_pan, emails_pan, events_pan, participents_pan;

    /**************** LABEL ******************/

    @FXML
    Label lbl_r, lbl_r1;


    /**************** BUTTOn ******************/
    /************************ PARTICIPENTS BUTTON ***************************/
    @FXML
    Button add_p, update_p, delete_p;


    /**************** IMPUT ******************/
    /************************ PARTICIPENTS Imput ***************************/
    @FXML
    TextField t_id_p, t_nom_p, t_prenom_p, t_email_p, t_num_p, searchF;


    @FXML
    DatePicker begin_imput, end_imput;


    /**************** TABLE ******************/

    @FXML
    public TableView<ModelTable> table ;

    @FXML
    public TableColumn<ModelTable, Integer> id_p ;

    @FXML
    public TableColumn<ModelTable, String> f_name_p ;

    @FXML
    public TableColumn<ModelTable, String> l_name_p ;

    @FXML
    public TableColumn<ModelTable, String> email_p ;

    @FXML
    public TableColumn<ModelTable, Integer> nbr_p_p ;


    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();

    FilteredList filter = new FilteredList(oblist, e->true);

    Connection c;
    Statement st;
    ResultSet rs;



    /************************ COMBOBOX *****************************/
    @FXML
    public ComboBox event_combo, event_combo_p;


    /************************ Table Search ***************************/

    @FXML
    public void searchT(){

        searchF.textProperty().addListener((observable, oldValue, newValue)->{
            filter.setPredicate((Predicate<? super ModelTable>) (ModelTable mt)->{
                if(newValue.isEmpty() || newValue==null){
                    return true;
                }
                if(mt.getF_name_p().toLowerCase().contains(newValue))
                {
                    return true;
                }
                if(mt.getL_name_p().toLowerCase().contains(newValue)){
                    return true;
                }
                if(mt.getEmail_p().toLowerCase().contains(newValue)){
                    return true;
                }

                String str = String.valueOf(mt.getId_p());
                if(str.toLowerCase().contains(newValue)){
                    return true;
                }
                return false;
            });
        });

        SortedList sort=new SortedList(filter);
        sort.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sort);


    }

    /************************ Put Event Into Comboboxs ***************************/

    public void combo()
    {
        if ((event_combo_p.getItems().isEmpty()) && (event_combo.getItems().isEmpty())){
            Connection conn = null;
            try {
                conn = db_check_in.connect();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String req = "select event_name from events";
            String event_name;
            try {
                st = conn.createStatement();
                rs = st.executeQuery(req);

                while (rs.next())
                {
                    event_name = rs.getString("event_name");
                    event_combo.getItems().add(event_name);
                    event_combo_p.getItems().add(event_name);
                }

            }
            catch (SQLException e) {
                System.out.println("err : "+ e);
            }
        }
    }


    public void chechinVisible(){
        emails_pan.setVisible(false);
        events_pan.setVisible(false);
        participents_pan.setVisible(false);
        checkin_pan.setVisible(true);
    }

    public void eventsVisible(){
        emails_pan.setVisible(false);
        participents_pan.setVisible(false);
        checkin_pan.setVisible(false);
        events_pan.setVisible(true);
    }

    public void emailsVisible(){
        events_pan.setVisible(false);
        checkin_pan.setVisible(false);
        participents_pan.setVisible(false);
        emails_pan.setVisible(true);
    }

    public void participentsVisible(){
        emails_pan.setVisible(false);
        events_pan.setVisible(false);
        checkin_pan.setVisible(false);
        participents_pan.setVisible(true);
    }




    /********************* Get Participant Id *********************/
    public int getParticipant_id(String event, String nom , String prenom)
    {
        Connection conn = null;
        try {
            conn = db_check_in.connect();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String req = "select participants_id from participants where nom = '"+ nom +"' and prenom = '"+ prenom +"' and event_id ="+ getEvent_id(event);;
        int p_id = 0;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(req);

            while (rs.next())
            {
                p_id = rs.getInt("participants_id");
            }

        }
        catch (SQLException e) {
            System.out.println("Err :"+ e);
            //e.printStackTrace();
        }
        return p_id;
    }



    /********************* Get Event Id ************************/

    public int getEvent_id(String event)
    {
        Connection conn = null;
        try {
            conn = db_check_in.connect();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String req = "select event_id from events where event_name ='"+event+"' ";
        int e_id = 0;
        try {
            //conn.setAutoCommit(false);

            st = conn.createStatement();
            rs = st.executeQuery(req);

            while (rs.next())
            {
                e_id = rs.getInt("event_id");
            }

        }
        catch (SQLException e) {
            System.out.println("Err :"+ e);
            //e.printStackTrace();
        }
        return e_id;
    }

    /********************* Update Table View ***********************/

    public void update_table()
    {

        String selected_event = (String) event_combo.getValue();
        if (selected_event != null){

            int selected_event_id = getEvent_id(selected_event);

            Connection conn = null;
            try {
                conn = db_check_in.connect();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //Statement st = null;
            String req = "select * from participants where event_id = "+selected_event_id;
            String f_name, l_name, email;
            int id, nbr;
            try {
                //conn.setAutoCommit(false);

                st = conn.createStatement();
                rs = st.executeQuery(req);
                oblist.clear();
                while (rs.next())
                {
                    id = rs.getInt("participants_id");
                    f_name = rs.getString("nom");
                    l_name = rs.getString("prenom");
                    email = rs.getString("email");
                    nbr = rs.getInt("nbr_presence");

                    oblist.add(new ModelTable(
                                    id,
                                    f_name,
                                    l_name,
                                    email,
                                    nbr
                            )
                    );
                }

            }
            catch (SQLException e) {
                System.out.println("Err :"+ e);
                //e.printStackTrace();
            }
        }
    }


    /********************* Add new Participants Into a specific Event *********************/
    public void add_participants() throws SQLException {
        try {
            String nom, prenom, email, selected_event, num_tel;
            int selected_event_id = 0;

            nom = t_nom_p.getText();
            prenom = t_prenom_p.getText();
            email = t_email_p.getText();
            num_tel = t_num_p.getText();

            selected_event = (String) event_combo_p.getValue();
            selected_event_id = getEvent_id(selected_event);

            if (selected_event_id != 0)
            {
                if ((nom.length()!=0) && (prenom.length()!=0) && (email.length()!=0) && (num_tel.length()!=0))
                {
                    String req = "insert into participants (nom, prenom, email, num_tel, event_id) values " +
                            "('"+ nom +"','"+ prenom +"','"+ email +"','"+ num_tel +"',"+ selected_event_id +")";

                    int test = st.executeUpdate(req);
                    if (test == 1){
                        qrbn();
                        int participant_id = getParticipant_id(selected_event, nom, prenom);
                        System.out.println("Elhamdoulillah");
                        saveToFile(qrView, ""+ participant_id + "_"+ selected_event + "_"+ nom +"_"+ prenom);
                        t_nom_p.setText("");
                        t_prenom_p.setText("");
                        t_email_p.setText("");
                        t_num_p.setText("");
                        update_table();
                    }
                }
                else
                {
                    System.out.println("Fargha :)");
                }
            }
            else
            {
                System.out.println("baliz, select event");
            }
        } catch (SQLException e) {
            System.out.println("Err : "+e);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /********************* Update Participants *********************/
    public void update_participants()
    {
        /*try {
            String nom, prenom, email;
            String num_tel;

            nom = t_nom_p.getText();
            prenom = t_prenom_p.getText();
            email = t_email_p.getText();
            num_tel = t_num_p.getText();

            if ((nom.length()!=0) && (prenom.length()!=0))
            {
                String req = "insert into participants (nom, prenom, email, num_tel) values " +
                        "('"+ nom +"','"+ prenom +"','"+ email +"','"+ num_tel +"')";

                int test = st.executeUpdate(req);
                if (test == 1){
                    System.out.println("Elhamdoulillah_a");
                    t_nom_p.setText("");
                    t_prenom_p.setText("");
                    t_email_p.setText("");
                    t_num_p.setText("");
                    update_table();
                }
            }
            else
            {
                System.out.println("Fargha :)");
            }
        } catch (SQLException e) {
            System.out.println("Err : "+e);
        }*/
    }

    /********************* Delete new Participants *********************/
    public void delete_participants() throws SQLException {
        String id;
        id = t_id_p.getText();
        if ( id.length()!=0 )
        {
            String req = "delete from participants where participants_id =("+ id +")";
            int test = st.executeUpdate(req);
            if (test == 1){
                t_id_p.setText("");
                t_nom_p.setText("");
                t_prenom_p.setText("");
                t_email_p.setText("");
                t_num_p.setText("");
                update_table();
            }
        }
        else
        {
            System.out.println("Fargha :)");
        }
    }







    /************************************** QR_Code ****************************************/
    @FXML
    ImageView qrView;

    /****************** CREAT QRCode ********************/
    public  void qrbn()
    {
        String event, id, nom, prenom;
        int id_p;
        event = (String) event_combo_p.getValue();
        id = t_id_p.getText();
        nom = t_nom_p.getText();
        prenom = t_prenom_p.getText();
        id_p = getParticipant_id(event, nom, prenom);

        if((id.length() != 0) || (nom.length() != 0) || (prenom.length() != 0) || event != null)
        {
            //System.out.println(txt.getText());
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String myWeb = "Bismiallah";
            //myWeb = txt.getText();
            myWeb = "ID : "+ id_p +"  |  \n" +
                    "EVENT :  "+ event +"  |  \n" +
                    "NOM : "+ nom +"  |  \n" +
                    "PRENOM : "+ prenom;
            int width = 300;
            int height = 300;
            try {
                BitMatrix bitMatrix = qrCodeWriter.encode(myWeb, BarcodeFormat.QR_CODE, width, height);

                //Create a Canvas (a place to draw on), with a 2D Graphic (a kind of drawing)
                Canvas canvas = new Canvas(width, height);
                GraphicsContext gc2D = canvas.getGraphicsContext2D();

                //in white, paint a rectangle on it, with the full size
                gc2D.setFill(Color.WHITE);
                gc2D.fillRect(0, 0, width, height);

                //start painting in black: each bit/pixel set in the bitMatix
                gc2D.setFill(Color.BLUE);
                for (int v = 0; v < height; v++) {
                    for (int h = 0; h < width; h++) {
                        if (bitMatrix.get(v, h)) {
                            gc2D.fillRect(h, v, 1, 1);
                        }
                    }
                }
                //Take a snapshot of the canvas and set it as an image in the ImageView control
                qrView.setImage(canvas.snapshot(null, null));
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        else
        {
            File file = new File("src/assets/camera.png");
            Image image = new Image(file.toURI().toString());
            qrView.setImage(image);
            //qrView.setImage("../assets/camera.png");
            System.out.println("One or all of this imputs are empty");
        }

    }

    /***************************          QR_Code          ****************************/

    public static Webcam webcam; // For taking pictures
    public static boolean isCapture = false; // For stop & resume thread of camera

    @FXML
    private StackPane imgContainer;

    @FXML
    private ImageView imageView;

    private FileChooser fileChooser; // For select path of saving picture captured

    /***************** TYPING QR ******************/
    public void testkey()
    {
        qrbn();
    }

    /***************** Save Image ******************/

    private void saveToFile(ImageView i, String test) throws IOException {
        File fo = new File("src/QRCode/"+test+".png");
        //BufferedImage bi = SwingFXUtils.fromFXImage(i, null);
        BufferedImage bi = SwingFXUtils.fromFXImage(i.getImage(), null);
        ImageIO.write(bi, "png" , fo);
    }

    /****************** Reading QR *******************/

    private static String decodeQRCode(File qrCodeimage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("QR Err: "+e);
            return null;
        }
    }

    /***************** Save Cam ******************/

    @FXML
    private void btnReload() { // Resume camera capture
        isCapture = false;
        new VideoTacker().start();
    }

    @FXML
    private void btnScan() throws IOException {
        isCapture = true; // Stop taking pictures

        File fo = new File("src/QRCode/test.png");
        //BufferedImage bi = SwingFXUtils.fromFXImage(i, null);
        BufferedImage bi = SwingFXUtils.fromFXImage(imageView.getImage(), null);
        ImageIO.write(bi, "png" , fo);

        try {
            File file1 = new File("src/QRCode/test.png");
            String decodedText = decodeQRCode(file1);
            if(decodedText == null) {
                System.out.println("No QR Code found in the image");
            } else {
                System.out.println("Decoded text = " + decodedText);

                /**************** Put The OutPut of the QrCode into textView *****************/
                String s = decodedText;
                //String[] words = s.split("\\|");
                String[] id_info = s.split(" ");
                System.out.println(id_info[2]);
                searchF.setText(id_info[2]);
                int participants_id = Integer.parseInt(id_info[2]);
                System.out.println(participants_id);
                try {
                    check_in(participants_id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                selectfromQR();

            }
        } catch (IOException e) {
            System.out.println("Could not decode QR Code, IOException :: " + e.getMessage());
        }

        btnReload();
    }

    /***************** Open Cam ******************/

    @FXML
    public void openCam(){
        webcam = Webcam.getDefault();
        if(webcam == null) {
            System.out.println("Camera not found !");
            System.exit(-1);
        }
        webcam.setViewSize(new java.awt.Dimension(640, 480));
        webcam.open();

        new VideoTacker().start(); // Start camera capture

        /* Init save file chooser */
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images (*.png)", "*.png"));
        fileChooser.setTitle("Save Image");
        btnReload();
    }

    /***************** Close Cam ******************/
    @FXML
    private void btnCapture() { // Stop camera & taking picture
        isCapture = true;
        webcam.close();
        File file = new File("src/assets/cam.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }


    /***************** Close Cam ******************/
    public void selectfromQR()
    {
        Connection conn = null;
        try {
            conn = db_check_in.connect();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int id, nbr;
        String req = "select * from participants where participants_id = "+searchF.getText();
        String f_name, l_name, email;
        try {

            st = conn.createStatement();
            rs = st.executeQuery(req);
            oblist.clear();
            while (rs.next())
            {

                nbr = rs.getInt("nbr_presence");
                id = rs.getInt("participants_id");
                f_name = rs.getString("nom");
                l_name = rs.getString("prenom");
                email = rs.getString("email");
                //nbr = rs.getInt("nbr_presence");

                oblist.add(new ModelTable(
                                id,
                                f_name,
                                l_name,
                                email,
                                nbr
                        )
                );
            }

        }
        catch (SQLException e) {
            System.out.println("Err :"+ e);
        }
    }

    /******************** CHECK_IN *********************/

    public void check_in(int participants_id) throws SQLException {

        Connection conn = null;
        PreparedStatement pstmt = null;

        //int participants_id = 15;
        int nbr_participation = 0;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);

        String today = dateFormat.format(cal.getTime());
        try {
            conn = connect();
            String query = "insert into check_in (today, participants_id) values(?, ?)";

            pstmt = conn.prepareStatement(query); // create a statement
            pstmt.setDate(1, java.sql.Date.valueOf(today)); // set input parameter 1
            pstmt.setInt(2, participants_id); // set input parameter 2
            pstmt.executeUpdate(); // execute insert statement
        } catch (Exception e) {
            System.out.println("Err : "+e);
        } finally {
            pstmt.close();
            conn.close();
        }

        /****************** Get Nbr of Participation *******************/
        //Connection conn = null;
        try {
            conn = db_check_in.connect();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String req1 = "select count(*) as nbr from check_in where check_in.participants_id = "+participants_id;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(req1);

            while (rs.next())
            {
                nbr_participation = rs.getInt("nbr");
                System.out.println("test : "+ nbr_participation);
            }

        }
        catch (SQLException e) {
            System.out.println("err : "+ e);
        }

        /****************** Update Participation nbr *******************/


        try {
            conn = connect();
            String query = "update participants set nbr_presence=? where participants_id ="+participants_id;

            pstmt = conn.prepareStatement(query); // create a statement
            pstmt.setInt(1, nbr_participation); // set input parameter 2
            pstmt.executeUpdate(); // execute insert statement
            System.out.println("elhamdoulillah");
        } catch (Exception e) {
            System.out.println("Err : "+e.getMessage());
            //e.getMessage();
        } finally {
            pstmt.close();
            conn.close();
        }


    }

    /******************* VideoTacker Class ********************/

    class VideoTacker extends Thread {
        @Override
        public void run() {
            while (!isCapture) { // For each 30 millisecond take picture and set it in image view
                {
                    try {
                        imageView.setImage(SwingFXUtils.toFXImage(webcam.getImage(), null));
                        sleep(30);
                        //btnSave();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

}

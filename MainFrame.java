import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.awt.Point;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.*;

public class MainFrame extends JFrame{

    /*Global variables*/
    final static Font mainFont = new Font("Arial", Font.ROMAN_BASELINE, 18);
    final static Font subFont = new Font("Arial", Font.ITALIC, 14);
    final static Font buttonFont = new Font("Arial", Font.BOLD, 12);
    // static Color btnColor = new Color(168, 218, 220);

    
    static JFrame f;
    static JMenuBar menuBar;
    static Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
    static Hashtable<Integer, Integer> pageStaves = new Hashtable<Integer, Integer>();
    static Hashtable<String, BufferedImage> imageMap = new Hashtable<String, BufferedImage>();
    static Hashtable<Point, BufferedImage> symbolMap = new Hashtable<Point, BufferedImage>();
    static Integer numPages = 1;
    static Integer curPage = 1;
    static Integer defaultStaves = 4;
    static Integer numStaves;    
    static Integer numRows = 0;
    static JLabel statusBar = new JLabel("No control selected.");
    static JLabel stavesInfo  = new JLabel("Loading...");
    static JMenuItem newMenuItem, deleteMenuItem, addPageMenuItem, deletePageMenuItem, nextPageMenuItem, prevPageMenuItem;
    static JButton btnNew, btnDelete, btnAddPage, btnDeletePage, btnNextPage, btnPrevPage;    
    static JPanel content;
    static MusicView music;
    static boolean selectMode = false;
    // static ArrayList<Point> pointAdded = new ArrayList<>();
    // static ArrayList<BufferedImage> notesAdded = new ArrayList<>();
    static JSlider duration;
    static String symbol = "Note";
    static String imageSelection = "HalfNoteImage";
    static Point dragPoint = new Point(0, 0);
    static Integer idSymbolSelected = -1;
    static Integer offsetX = 0;
    static Integer offsetY = 0;
    static Integer lengthX = 10;
    static Integer lengthY = 30;


    static String[] columns = new String[] {
        "Id", "Name", "x", "y", "Duration"
    };
        
    static Object[][] data = new Object[][] {
    };

    static DefaultTableModel model = new DefaultTableModel(data, columns);
    static JTable symbolTable = new JTable(model);    

    ////Setting button style
    public static void setButtonProperties(JButton buttonName, String buttonDef) {
        buttonName.setText(buttonDef);
        buttonName.setFont(buttonFont);
        buttonName.setBackground(java.awt.Color.getHSBColor(182, 23, 86));
        buttonName.setHorizontalAlignment(SwingConstants.LEFT);  
    }

    ////Setting status bar text
    public static void setStatusText(String statusText) {
        statusBar.setText(statusText + " was selected last. ");
    }

    public static void setPitchStatusText(String statusText) {
        statusBar.setText(statusText + " is the pitch of the note added. ");
    }

    ////Setting central text
    public static void setCentraltext(){
        stavesInfo.setText("My Music Editor. Showing " + pageStaves.get(curPage) + " staves (Page " + curPage + " of " + numPages + ")");
    }

    public static void offsetSymbol(String durationNote){
        if (symbol == "Note"){
            if (durationNote == "Whole"){
                offsetX = -10;
                offsetY = -5;
            } else if (durationNote == "Half"){
                offsetX = -15;
                offsetY = -32;
            } else if (durationNote == "Quarter"){
                offsetX = -6;
                offsetY = -32;
            } else if (durationNote == "Eighth"){
                offsetX = -15;
                offsetY = -34;
            } else if (durationNote == "Sixteenth"){
                offsetX = -6;
                offsetY = -34;
            }
        } 
        if (symbol == "Rest"){
            if (durationNote == "Whole"){
                offsetX = -5;
                offsetY = -5;
            } else if (durationNote == "Half"){
                offsetX = -5;
                offsetY = -5;
            } else if (durationNote == "Quarter"){
                offsetX = -5;
                offsetY = -5;
            } else if (durationNote == "Eighth"){
                offsetX = -5;
                offsetY = -5;
            } else if (durationNote == "Sixteenth"){
                offsetX = -5;
                offsetY = -5;
            }
        } 
    }

    public static void lengthSymbol(String durationNote){
        if (symbol == "Note"){
            if (durationNote == "Whole"){
                lengthX = 19;
                lengthY = 11;
            } else if (durationNote == "Half"){
                lengthX = 30;
                lengthY = 40;
            } else if (durationNote == "Quarter"){
                lengthX = 13;
                lengthY = 40;
            } else if (durationNote == "Eighth"){
                lengthX = 40;
                lengthY = 40;
            } else if (durationNote == "Sixteenth"){
                lengthX = 22;
                lengthY = 40;
            }
        } 
        if (symbol == "Rest"){
            if (durationNote == "Whole"){
                lengthX = -5;
                lengthY = -5;
            } else if (durationNote == "Half"){
                lengthX = -5;
                lengthY = -5;
            } else if (durationNote == "Quarter"){
                lengthX = -5;
                lengthY = -5;
            } else if (durationNote == "Eighth"){
                lengthX = -5;
                lengthY = -5;
            } else if (durationNote == "Sixteenth"){
                lengthX = -5;
                lengthY = -5;
            }
        } 
    }

    ////Enables or disables delete staff buttons
    public static void checkDeleteStaff() {
        if (pageStaves.get(curPage) == 1){
            btnDelete.setEnabled(false);
            deleteMenuItem.setEnabled(false);
        } else {
            btnDelete.setEnabled(true);
            deleteMenuItem.setEnabled(true);
        }
    }

    ////Enables or disables delete page buttons
    public static void updateAddDelete() {
        if (numPages == 1){            
            deletePageMenuItem.setEnabled(false);
            btnDeletePage.setEnabled(false);
        }else {
            deletePageMenuItem.setEnabled(true);
            btnDeletePage.setEnabled(true);
        }
    }

    ////Enables or disables next-previous page buttons
    public static void updateNextPrev() {
        if (curPage == 1){            
            prevPageMenuItem.setEnabled(false);
            btnPrevPage.setEnabled(false);
        } else {
            prevPageMenuItem.setEnabled(true);
            btnPrevPage.setEnabled(true);
        }

        if (numPages > curPage){
            nextPageMenuItem.setEnabled(true);
            btnNextPage.setEnabled(true);
        }

        if (curPage == numPages){            
            nextPageMenuItem.setEnabled(false);
            btnNextPage.setEnabled(false);
        }
    }


    ////Methods for adding or deleting page
    public static void addPage() {
        numPages = numPages + 1;
        updateAddDelete();
        updateNextPrev();
        pageStaves.put(numPages, defaultStaves);
        setStatusText("Add page");
        setCentraltext();
    }

    public static void deletePage() {
        numPages = numPages - 1;

        pageStaves.forEach((key, value)
            -> {
                if (key >= curPage && key <= numPages) {
                    pageStaves.put(key, pageStaves.get(key+1));
                }
            }
        );
        updateAddDelete();
        updateNextPrev();
        if(numPages < curPage){
            prevPage();
        }
        // setStatusText("Delete page");
        statusBar.setText("val is" + pageStaves);
        setCentraltext();
    }

    ////Methods for next or previous page
    public static void nextPage() {
        curPage = curPage + 1;   
        checkDeleteStaff();         
        updateNextPrev();
        setCentraltext(); 
        setStatusText("Next page");  
        setSizeMusicView();
    }

    public static void prevPage() {
        curPage = curPage - 1;
        updateNextPrev();
        setCentraltext(); 
        setStatusText("Previous page");
        setSizeMusicView();
    }

    public static void setSizeMusicView(){
        int heightMusicView = (int) pageStaves.get(curPage)*104 + 156;
        music.setSize(1100, heightMusicView);
        // setPitchStatusText(pageStaves.get(curPage) + "staves" + heightMusicView);
        music.setPreferredSize(new Dimension(1100, heightMusicView));
        content.setBorder(BorderFactory.createEmptyBorder(52, 50, 52, 50));
    }


    static class MusicView extends JComponent implements MouseInputListener, KeyListener{   
        
        final BufferedImage trebleClef, commonTime, flatImage, sharpImage, naturalImage;
        final BufferedImage SixteenthNoteImage, EighthNoteImage, QuarterNoteImage, HalfNoteImage, WholeNoteImage;
        final BufferedImage SixteenthRestImage, EighthRestImage, QuarterRestImage, HalfRestImage, WholeRestImage;

        

        public MusicView() throws IOException{

            trebleClef = ImageIO.read(getClass().getResource("/images/trebleClef.png"));
            commonTime = ImageIO.read(getClass().getResource("/images/commonTime.png"));

            flatImage = ImageIO.read(getClass().getResource("/images/flat.png"));
            sharpImage = ImageIO.read(getClass().getResource("/images/sharp.png"));
            naturalImage = ImageIO.read(getClass().getResource("/images/natural.png"));

            SixteenthNoteImage = ImageIO.read(getClass().getResource("/images/sixteenthNote.png"));
            EighthNoteImage = ImageIO.read(getClass().getResource("/images/eighthNote.png"));
            QuarterNoteImage = ImageIO.read(getClass().getResource("/images/quarterNote.png"));
            HalfNoteImage = ImageIO.read(getClass().getResource("/images/halfNote.png"));
            WholeNoteImage = ImageIO.read(getClass().getResource("/images/wholeNote.png"));

            SixteenthRestImage = ImageIO.read(getClass().getResource("/images/sixteenthRest.png"));
            EighthRestImage = ImageIO.read(getClass().getResource("/images/eighthRest.png"));
            QuarterRestImage = ImageIO.read(getClass().getResource("/images/quarterRest.png"));
            HalfRestImage = ImageIO.read(getClass().getResource("/images/halfRest.png"));
            WholeRestImage = ImageIO.read(getClass().getResource("/images/wholeRest.png"));

            imageMap.put("SixteenthNoteImage", SixteenthNoteImage);
            imageMap.put("EighthNoteImage", EighthNoteImage);
            imageMap.put("QuarterNoteImage", QuarterNoteImage);
            imageMap.put("HalfNoteImage", HalfNoteImage);
            imageMap.put("WholeNoteImage", WholeNoteImage);

            imageMap.put("SixteenthRestImage", SixteenthRestImage);
            imageMap.put("EighthRestImage", EighthRestImage);
            imageMap.put("QuarterRestImage", QuarterRestImage);
            imageMap.put("HalfRestImage", HalfRestImage);
            imageMap.put("WholeRestImage", WholeRestImage);

            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.addKeyListener(this);

        }
               
        
        void draw(Graphics g, Integer y, Boolean last){
            g.drawLine(50, 0+y, 1050, 0+y);
            g.drawLine(50, 13+y, 1050, 13+y);
            g.drawLine(50, 26+y, 1050, 26+y);
            g.drawLine(50, 39+y, 1050, 39+y);
            g.drawLine(50, 52+y, 1050, 52+y);
            g.drawImage(trebleClef, 60, y-22, 50, 100, null);
            g.drawImage(commonTime, 110, y+8, 20, 38, null);
            g.drawLine(50, 0+y, 50, 52+y);
            g.drawLine(400, 0+y, 400, 52+y);
            g.drawLine(750, 0+y, 750, 52+y);

            if (last == true){
                g.drawLine(1040, 0+y, 1040, 52+y);
                ((Graphics2D) g).setStroke(new BasicStroke(8f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                g.drawLine(1050, 0+y, 1050, 52+y);
            } else {
                g.drawLine(1050, 0+y, 1050, 52+y);
            }
        }

        public void paintComponent(Graphics g){
            Integer ht = 104;
            Boolean last = false;
            
            g.setColor(java.awt.Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(java.awt.Color.black);
            for (int i = 0; i < pageStaves.get(curPage) ; i++){

                if ( i == pageStaves.get(curPage)-1 ){
                    last = true;
                }
                draw(g, 104 + i*ht, last);
            }            


            for (int i=0; i<numRows; i++){
                int drawX = (int) symbolTable.getModel().getValueAt(i, 2);
                int drawY = (int) symbolTable.getModel().getValueAt(i, 3);
                // g.translate(-10, -10);
                g.drawImage(imageMap.get(symbolTable.getModel().getValueAt(i, 1)), drawX, drawY,null);
                if (i == idSymbolSelected){
                    lengthSymbol((String) symbolTable.getModel().getValueAt(i, 4));
                    ((Graphics2D) g).setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                    g.setColor(java.awt.Color.red);
                    g.drawRect(drawX , drawY , lengthX, lengthY);
                }
            }

            if (!(dragPoint.x==0 && dragPoint.y==0)){
                if (selectMode == false){
                    g.drawImage(imageMap.get(labels.get(duration.getValue()).getText() + symbol + "Image"), dragPoint.x, dragPoint.y, null);                               
                } else if (idSymbolSelected != -1) {
    
                }    
            }

            // if (!(dragPoint.x==0 && dragPoint.y==0)){
            //     g.drawImage(imageMap.get(labels.get(duration.getValue()).getText() + symbol + "Image"), dragPoint.x, dragPoint.y, null);            
            // }
        }


        @Override
        public void mouseClicked(MouseEvent e) {
        }


        @Override
        public void mousePressed(MouseEvent e) {
            
            if (selectMode == false){
                offsetSymbol(labels.get(duration.getValue()).getText());
                dragPoint = new Point(e.getX() + offsetX, e.getY() + offsetY);
                repaint();
            } else {
                
                setStatusText("Pressed; looking for selecetd symbol.");
                idSymbolSelected = -1;
                for (int i=numRows-1; i>=0; i--){
                    offsetSymbol((String) symbolTable.getModel().getValueAt(i, 4));
                    int symbolX = (int) symbolTable.getModel().getValueAt(i, 2);
                    int symbolY = (int) symbolTable.getModel().getValueAt(i, 3);
                    int mouseX = e.getX() + offsetX;
                    int mouseY = e.getY() + offsetY;
                    // g.drawImage(imageMap.get(symbolTable.getModel().getValueAt(i, 1)), (int) symbolTable.getModel().getValueAt(i, 2), (int) symbolTable.getModel().getValueAt(i, 3),null);
                    if ( mouseX < symbolX + 50 && mouseX > symbolX - 50 && mouseY < symbolY + 50 && mouseY > symbolY -50 ){
                        idSymbolSelected = i;
                        setStatusText("selected " + i + " at "+ e.getX());
                        repaint();
                        break;
                    }
                }       
            }
        }


        @Override
        public void mouseReleased(MouseEvent e) {
            // symbolMap.put(new Point(e.getX(), e.getY()), imageMap.get(labels.get(duration.getValue()).getText() + symbol + "Image"));


            if (selectMode == false){
                int x = e.getX() + offsetX;
                int y = e.getY() + offsetY;
                numRows += 1;
                String durationNote = labels.get(duration.getValue()).getText();
                model.addRow(new Object[]{numRows, durationNote + symbol + "Image", x , y, durationNote});
                int actualY = 0;
                if (symbol == "Note"){
        
                    //E4, F4, G4, A5, B5, C5, D5, E5, and F5
                    if (actualY >= 101 || actualY <= 3){
                        setPitchStatusText("F5");
                    } else if (actualY >3 && actualY <= 10){
                        setPitchStatusText("E5");
                    } else if (actualY >10 && actualY <= 16){
                        setPitchStatusText("D5");
                    } else if (actualY >16 && actualY <= 23){
                        setPitchStatusText("C5");
                    } else if (actualY >23 && actualY <= 29){
                        setPitchStatusText("B5");
                    } else if (actualY >29 && actualY <= 36){
                        setPitchStatusText("A5");
                    } else if (actualY >36 && actualY <= 42){
                        setPitchStatusText("G4");
                    } else if (actualY >42 && actualY <= 49){
                        setPitchStatusText("F4");
                    } else if (actualY >49 && actualY <= 55){
                        setPitchStatusText("E4");
                    } else{
                        setPitchStatusText("The note is outside the staff area! The pitch cannot be identified.");
                    }
                }         
            }  else if (idSymbolSelected != -1){
                setStatusText("selected");
            }            
            // setStatusText("mouseclicked");
            repaint();
        }


        @Override
        public void mouseEntered(MouseEvent e) {

        }


        @Override
        public void mouseExited(MouseEvent e) {
            
        }


        @Override
        public void mouseDragged(MouseEvent e) {
            if (selectMode == false){
                // offsetSymbol(labels.get(duration.getValue()).getText());
                dragPoint = new Point(e.getX() + offsetX, e.getY() + offsetY);
                
                
            } else if (idSymbolSelected != -1) {
                // offsetSymbol((String) symbolTable.getModel().getValueAt(idSymbolSelected, 4));
                model.setValueAt(e.getX() + offsetX, idSymbolSelected, 2);
                model.setValueAt(e.getY() + offsetY, idSymbolSelected, 3);
            }    
            repaint();                 
        }


        @Override
        public void mouseMoved(MouseEvent e) {
            
        }


        @Override
        public void keyTyped(KeyEvent e) {
            
        }


        @Override
        public void keyPressed(KeyEvent e) {
            
        }


        @Override
        public void keyReleased(KeyEvent e) {
            if (idSymbolSelected != -1){
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE){
                    model.removeRow(idSymbolSelected);
                    repaint();
                }
            }
        }
    }

    ////Methods for adding or deleting staff
    public static void addStaff() {
        pageStaves.put(curPage, pageStaves.get(curPage) + 1);    
                        
        if(pageStaves.get(curPage) > 1){
            deleteMenuItem.setEnabled(true);
            btnDelete.setEnabled(true);
        }
        setStatusText("New Staff");
        setCentraltext(); 
        setSizeMusicView();
        music.repaint();
        
    }

    public static void deleteStaff() {
        pageStaves.put(curPage, pageStaves.get(curPage) - 1);    
                
        if(pageStaves.get(curPage)==1){
            deleteMenuItem.setEnabled(false);
            btnDelete.setEnabled(false);
        }
        setStatusText("Delete Staff");
        setCentraltext();    
        setSizeMusicView();
        music.repaint();     
    }

    /*Method to display the layout*/
    public static void viewAllContent() throws IOException{
        
        numStaves = pageStaves.get(curPage);

        /*Status bar*/
        statusBar.setFont(subFont);        


        /*Main Content Panel */
        content = new JPanel();          
        // content.setBackground(new Color(241, 250, 238));
        // content.setLayout((new GridLayout(1, 1)));  
        content.setLayout((new BoxLayout(content, BoxLayout.Y_AXIS)));     
        
        JScrollPane contentPane = new JScrollPane(content);   
        contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        contentPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contentPane.setOpaque(false); 
        
        music = new MusicView();
        content.add(music); 
        setSizeMusicView();
        music.setMinimumSize(new Dimension(50,50));        
                // stavesInfo.setHorizontalAlignment(JLabel.CENTER);
        // setCentraltext();  
        
        
        /*Icons for buttons */
        java.net.URL nextURL = MainFrame.class.getResource("images/next.png");
        ImageIcon nextIcon =  new ImageIcon(nextURL);
        java.net.URL prevURL = MainFrame.class.getResource("images/prev.png");        
        ImageIcon prevIcon =  new ImageIcon(prevURL);
        java.net.URL selectURL = MainFrame.class.getResource("images/select.png");
        ImageIcon selectIcon =  new ImageIcon(selectURL);
        java.net.URL penURL = MainFrame.class.getResource("images/pen.png");        
        ImageIcon penIcon =  new ImageIcon(penURL);
        java.net.URL newURL = MainFrame.class.getResource("images/new.png");
        ImageIcon newIcon =  new ImageIcon(newURL);
        java.net.URL deleteURL = MainFrame.class.getResource("images/delete.png");
        ImageIcon deleteIcon =  new ImageIcon(deleteURL);
        java.net.URL playURL = MainFrame.class.getResource("images/play.png");
        ImageIcon playIcon =  new ImageIcon(playURL);
        java.net.URL stopURL = MainFrame.class.getResource("images/stop.png");
        ImageIcon stopIcon =  new ImageIcon(stopURL);


        /*Buttons */
        btnAddPage = new JButton(newIcon);        
        setButtonProperties(btnAddPage, "Add Page");
        btnDeletePage = new JButton(deleteIcon);
        setButtonProperties(btnDeletePage, "Delete Page");
        btnNextPage = new JButton(nextIcon);        
        setButtonProperties(btnNextPage, "Next Page");
        btnPrevPage = new JButton(prevIcon);
        setButtonProperties(btnPrevPage, "Prev Page");
        JButton btnSelect = new JButton(selectIcon);
        setButtonProperties(btnSelect, "Select");
        JButton btnPen = new JButton(penIcon);
        setButtonProperties(btnPen, "Pen");        
        btnNew = new JButton(newIcon);        
        setButtonProperties(btnNew, "New Staff");
        btnDelete = new JButton(deleteIcon);
        setButtonProperties(btnDelete, "Delete staff");
        JButton btnPlay = new JButton(playIcon);
        setButtonProperties(btnPlay, "Play");
        JButton btnStop = new JButton(stopIcon);
        setButtonProperties(btnStop, "Stop");

        btnPrevPage.setEnabled(false);
        btnDeletePage.setEnabled(false);
        btnNextPage.setEnabled(false);

        
        /*Button Panel*/
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout((new GridLayout(5, 2, 15, 10)));
        buttonPanel.setPreferredSize(new Dimension(280, 180));
        buttonPanel.setMinimumSize(new Dimension(280, 180));        
        buttonPanel.setMaximumSize(new Dimension(280, 180));
        buttonPanel.add(btnAddPage);
        buttonPanel.add(btnDeletePage);
        buttonPanel.add(btnNextPage);
        buttonPanel.add(btnPrevPage);
        buttonPanel.add(btnSelect);
        buttonPanel.add(btnPen);
        buttonPanel.add(btnNew);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnPlay);
        buttonPanel.add(btnStop);
        buttonPanel.setOpaque(false);

        /*Selection of add page*/
        btnAddPage.addActionListener(e -> {
            addPage();
        }); 

        /*Selection of delete page*/
        btnDeletePage.addActionListener(e -> {
            deletePage();
        }); 

        /*Selection of next page*/
        btnNextPage.addActionListener(e -> {
            nextPage();            
        });
        
        /*Selection of previous page*/
        btnPrevPage.addActionListener(e -> {
            prevPage();
        }); 

        /*Selection of delete menu item */
        deleteMenuItem.addActionListener(e -> {              
            deleteStaff();
        });

        /*Selection of new menu item */
        newMenuItem.addActionListener(e -> {     
            addStaff();
        });

        /*Selection of delete button */
        btnDelete.addActionListener(e -> {  
            deleteStaff();
        });

        /*Selection of new button */
        btnNew.addActionListener(e -> {  
            addStaff();
        });

        /*Selection of select button*/
        btnSelect.addActionListener(e -> {
                setStatusText("Select Button");
                selectMode = true;
        });
        
        /*Selection of pen button*/
        btnPen.addActionListener(e -> {
                setStatusText("Pen Button");
                selectMode = false;
        });


        /*Selection of play button*/
        btnPlay.addActionListener(e -> {
                setStatusText("Play Button");
        });
        
        /*Selection of stop button*/
        btnStop.addActionListener(e -> {
                setStatusText("Stop Button");
        });


        /*Radio Panel */
        JRadioButton option1 = new JRadioButton("Note");
        JRadioButton option2 = new JRadioButton("Rest");
        JRadioButton option3 = new JRadioButton("Flat");
        JRadioButton option4 = new JRadioButton("Sharp");
        ButtonGroup notation = new ButtonGroup();
        JPanel radioPanel = new JPanel();

        radioPanel.setLayout((new GridLayout(4, 1, 0, 1)));
        radioPanel.setOpaque(false);
        option1.setOpaque(false);
        option2.setOpaque(false);
        option3.setOpaque(false);
        option4.setOpaque(false);
        
        notation.add(option1);
        notation.add(option2);
        notation.add(option3);
        notation.add(option4);
        
        option1.setSelected(true);

        radioPanel.add(option1);
        radioPanel.add(option2);
        radioPanel.add(option3);
        radioPanel.add(option4);

        /*Selection of option1*/
        option1.addActionListener(e -> {
            setStatusText("Note");
            symbol = "Note";
        });
        
        /*Selection of option2*/
        option2.addActionListener(e -> {
            setStatusText("Rest");
            symbol = "Rest";
        }); 

        /*Selection of option3*/
        option3.addActionListener(e -> {
            setStatusText("Flat");
            symbol = "Flat";
        });
        
        /*Selection of option4*/
        option4.addActionListener(e -> {
            setStatusText("Sharp");
            symbol = "Sharp";
        }); 


        /*Slider Panel */
        duration = new JSlider(JSlider.VERTICAL, 0, 4, 1);
        duration.setOpaque(false);
        duration.setMajorTickSpacing(1);
        duration.setPaintTicks(true);
        
        labels.put(0, new JLabel("Whole"));       
        labels.put(1, new JLabel("Half"));
        labels.put(2, new JLabel("Quarter"));
        labels.put(3, new JLabel("Eighth"));
        labels.put(4, new JLabel("Sixteenth"));
        duration.setLabelTable(labels);
        duration.setPaintLabels(true);
        
        duration.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                setStatusText(labels.get(duration.getValue()).getText());                
            }
            
        });


        /*Radio Button Panel (choicesPanel)*/
        JPanel choicesPanel = new JPanel();
        choicesPanel.setLayout((new GridLayout(1, 2, 0, 1)));
        choicesPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        choicesPanel.setOpaque(false);
        choicesPanel.add(radioPanel);
        choicesPanel.add(duration);


        /*Left side panel (toolPanel)*/
        JPanel toolPanel = new JPanel();
        toolPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));
        toolPanel.setOpaque(false);
        toolPanel.add(buttonPanel);
        toolPanel.add(choicesPanel);


        /*Main Panel */
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(toolPanel, BorderLayout.WEST);
        mainPanel.add(contentPane, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);       
        mainPanel.setBackground(java.awt.Color.WHITE);

        f.add(mainPanel);

    }



    /*Setup of JFrame and menubar*/
    private static void runGUI() throws IOException {

        /*JFrame */
        f = new JFrame();
        pageStaves.put(1,4);

                
        /*Menu Bar */
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu pagesMenu = new JMenu("Pages");
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(pagesMenu);
        
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);
        newMenuItem = new JMenuItem("New Staff");
        deleteMenuItem = new JMenuItem("Delete Staff");
        editMenu.add(newMenuItem);
        editMenu.add(deleteMenuItem);

        addPageMenuItem = new JMenuItem("Add Page");        
        deletePageMenuItem = new JMenuItem("Delete Page");
        pagesMenu.add(addPageMenuItem);
        pagesMenu.add(deletePageMenuItem);
        
        nextPageMenuItem = new JMenuItem("Next Page");        
        prevPageMenuItem = new JMenuItem("Previous Page");
        pagesMenu.add(nextPageMenuItem);
        pagesMenu.add(prevPageMenuItem);

        nextPageMenuItem.setEnabled(false);
        prevPageMenuItem.setEnabled(false);
        deletePageMenuItem.setEnabled(false);
        

        /*Selection of exit menu item */
        exitMenuItem.addActionListener(e -> {
            setStatusText("Exit");
            f.dispose();
        });

        /*Selection of add page*/
        addPageMenuItem.addActionListener(e -> {
            addPage();
        });
        
        /*Selection of delete page*/
        deletePageMenuItem.addActionListener(e -> {
            deletePage();
        }); 

        /*Selection of next page*/
        nextPageMenuItem.addActionListener(e -> {
            nextPage();            
        });
        
        /*Selection of previous page*/
        prevPageMenuItem.addActionListener(e -> {
            prevPage();
        }); 


        f.setJMenuBar(menuBar);
        viewAllContent();  
        
        class musicView extends JComponent{

        }

        musicView music = new musicView();
        content.add(music);

        /*Title etc */        
        f.setTitle("Music Editor");
        f.setSize(800, 600);
        f.setMinimumSize(new Dimension(550,530));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
                    runGUI();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
			}
		});
	}
}

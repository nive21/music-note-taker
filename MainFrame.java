import java.awt.*;
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
    static JLabel statusBar = new JLabel("No control selected.");
    static JLabel stavesInfo  = new JLabel("Loading...");
    static JMenuItem newMenuItem, deleteMenuItem, addPageMenuItem, deletePageMenuItem, nextPageMenuItem, prevPageMenuItem;
    static JButton btnNew, btnDelete, btnAddPage, btnDeletePage, btnNextPage, btnPrevPage;    
    static JPanel content;
    static MusicView music;
    // static ArrayList<Point> pointAdded = new ArrayList<>();
    // static ArrayList<BufferedImage> notesAdded = new ArrayList<>();
    static JSlider duration;
    static String symbol = "Note";
    static String imageSelection = "HalfNoteImage";
    static Point dragPoint = new Point(0, 0);
    


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

    ////Setting central text
    public static void setCentraltext(){
        stavesInfo.setText("My Music Editor. Showing " + pageStaves.get(curPage) + " staves (Page " + curPage + " of " + numPages + ")");
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
    }

    public static void prevPage() {
        curPage = curPage - 1;
        updateNextPrev();
        setCentraltext(); 
        setStatusText("Previous page");
    }


    static class MusicView extends JComponent implements MouseInputListener{   
        
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
            Integer ht = 120;
            Boolean last = false;
            
            g.setColor(java.awt.Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(java.awt.Color.black);
            for (int i = 0; i < pageStaves.get(curPage) ; i++){

                if ( i == pageStaves.get(curPage)-1 ){
                    last = true;
                }
                draw(g, 100 + i*ht, last);
            }            

            Enumeration<Point> e = symbolMap.keys();
            while (e.hasMoreElements()) {
                Point key = e.nextElement();
                g.drawImage(symbolMap.get(key), key.x, key.y,null);
            }


            if (!(dragPoint.x==0 && dragPoint.y==0)){
                g.drawImage(imageMap.get(labels.get(duration.getValue()).getText() + symbol + "Image"), dragPoint.x, dragPoint.y, null);            
            }
        }


        @Override
        public void mouseClicked(MouseEvent e) {
        }


        @Override
        public void mousePressed(MouseEvent e) {
            dragPoint = new Point(e.getX(), e.getY());
            // setStatusText("dragging");
            repaint();
        }


        @Override
        public void mouseReleased(MouseEvent e) {
            symbolMap.put(new Point(e.getX(), e.getY()), imageMap.get(labels.get(duration.getValue()).getText() + symbol + "Image"));
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
            dragPoint = new Point(e.getX(), e.getY());
            // setStatusText("dragging");
            repaint();
        }


        @Override
        public void mouseMoved(MouseEvent e) {
        }

        // Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        // labels.put(0, new JLabel("Whole"));       
        // labels.put(1, new JLabel("Half"));
        // labels.put(2, new JLabel("Quarter"));
        // labels.put(3, new JLabel("Eighth"));
        // labels.put(4, new JLabel("Sixteenth"));
        // duration.setLabelTable(labels);
        // duration.setPaintLabels(true);
        
        // duration.addChangeListener(new ChangeListener() {

        //     @Override
        //     public void stateChanged(ChangeEvent e) {
        //         setStatusText(labels.get(duration.getValue()).getText());                
        //     }
            
        // });


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
        music.setSize(1100, 1100);
        music.setPreferredSize(new Dimension(1100, 1100));
        music.setMinimumSize(new Dimension(50,50));        
        content.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
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
        });
        
        /*Selection of pen button*/
        btnPen.addActionListener(e -> {
                setStatusText("Pen Button");
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

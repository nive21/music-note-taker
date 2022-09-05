import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

public class MainFrame extends JFrame{

    /*Global variables*/
    final static Font mainFont = new Font("Arial", Font.ROMAN_BASELINE, 18);
    final static Font subFont = new Font("Arial", Font.ITALIC, 14);
    final static Font buttonFont = new Font("Arial", Font.BOLD, 12);
    
    static JFrame f;
    static JMenuBar menuBar;
    static Integer numStaves = 4;
    static Color btnColor = new Color(168, 218, 220);
    static JLabel statusBar = new JLabel("No control selected.");

    public static void setButtonProperties(JButton buttonName, String buttonDef) {
        buttonName.setText(buttonDef);
        buttonName.setFont(buttonFont);
        buttonName.setBackground(btnColor);
        buttonName.setHorizontalAlignment(SwingConstants.LEFT);  
    }

    public static void setStatusText(String statusText) {
        statusBar.setText(statusText + " was selected last.
        ");
    }

    public static void main(String[] args) {

        /*JFrame */
        f = new JFrame("Music Editor");


        /*Status bar*/
        statusBar.setFont(subFont);


        /*Main Content Panel */
        JPanel content = new JPanel();
        JScrollPane contentPane = new JScrollPane(content);
        JLabel stavesInfo = new JLabel("My Music Editor. Showing " + numStaves + " staves.");
        stavesInfo.setHorizontalAlignment(JLabel.CENTER);
        content.add(stavesInfo);

        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        contentPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contentPane.setOpaque(false);

        content.setBackground(new Color(241, 250, 238));
        content.setLayout((new GridLayout(1, 1)));
              
        
        /*Menu Bar */
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);
        JMenuItem newMenuItem = new JMenuItem("New Staff");
        JMenuItem deleteMenuItem = new JMenuItem("Delete Staff");
        editMenu.add(newMenuItem);
        editMenu.add(deleteMenuItem);

        f.setJMenuBar(menuBar);

        
        /*Icons for buttons */
        ImageIcon selectIcon =  new ImageIcon("images/select.png");
        ImageIcon penIcon =  new ImageIcon("images/pen.png");
        ImageIcon newIcon =  new ImageIcon("images/new.png");
        ImageIcon deleteIcon =  new ImageIcon("images/delete.png");
        ImageIcon playIcon =  new ImageIcon("images/play.png");
        ImageIcon stopIcon =  new ImageIcon("images/stop.png");


        /*Buttons */
        JButton btnSelect = new JButton(selectIcon);
        setButtonProperties(btnSelect, "Select");
        JButton btnPen = new JButton(penIcon);
        setButtonProperties(btnPen, "Pen");        
        JButton btnNew = new JButton(newIcon);        
        setButtonProperties(btnNew, "New Staff");
        JButton btnDelete = new JButton(deleteIcon);
        setButtonProperties(btnDelete, "Delete staff");
        JButton btnPlay = new JButton(playIcon);
        setButtonProperties(btnPlay, "Play");
        JButton btnStop = new JButton(stopIcon);
        setButtonProperties(btnStop, "Stop");

        
        /*Button Panel*/
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout((new GridLayout(3, 2, 15, 15)));
        buttonPanel.add(btnSelect);
        buttonPanel.add(btnPen);
        buttonPanel.add(btnNew);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnPlay);
        buttonPanel.add(btnStop);
        buttonPanel.setOpaque(false);


        /*Selection of exit option */
        exitMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                statusBar.setText("Exit was selected.");
                f.dispose();
            }
            
        });


        /*Selection of delete option */
        deleteMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              
                numStaves = numStaves - 1;
                setStatusText("Delete Staff");
                stavesInfo.setText("My Music Editor. Showing " + numStaves + " staves.");
                if(numStaves==1){
                    deleteMenuItem.setEnabled(false);
                    btnDelete.setEnabled(false);                    
                }

            }
            
        });


        /*Selection of new option */
        newMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                numStaves = numStaves + 1;
                if(numStaves>1){
                    deleteMenuItem.setEnabled(true);
                    btnDelete.setEnabled(false);
                }
                setStatusText("New Staff");
                
            }
            
        });


        /*Selection of delete option */
        btnDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                numStaves = numStaves - 1;
                if(numStaves==1){
                    deleteMenuItem.setEnabled(false);
                    btnDelete.setEnabled(false);
                }
                statusBar.setText("Delete Staff was selected.");
                stavesInfo.setText("My Music Editor. Showing " + numStaves + " staves.");
            }
            
        });


        /*Selection of new option */
        btnNew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                numStaves = numStaves + 1;
                if(numStaves>1){
                    deleteMenuItem.setEnabled(true);
                    btnDelete.setEnabled(true);
                }
                statusBar.setText("New Staff was selected.");
                stavesInfo.setText("My Music Editor. Showing " + numStaves + " staves.");
                
            }
            
        });


        /*Radio Panel */
        JRadioButton option1 = new JRadioButton();
        JRadioButton option2 = new JRadioButton();
        JRadioButton option3 = new JRadioButton();
        JRadioButton option4 = new JRadioButton();
        ButtonGroup notation = new ButtonGroup();
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout((new GridLayout(4, 1, 0, 1)));
        option1.setOpaque(false);
        option2.setOpaque(false);
        option3.setOpaque(false);
        option4.setOpaque(false);
        radioPanel.setOpaque(false);

        notation.add(option1);
        notation.add(option2);
        notation.add(option3);
        notation.add(option4);
        
        option1.setText("Note");
        option2.setText("Rest");
        option3.setText("Flat");
        option4.setText("Sharp");
        option1.setSelected(true);

        radioPanel.add(option1);
        radioPanel.add(option2);
        radioPanel.add(option3);
        radioPanel.add(option4);


        /*Slider Panel */
        JSlider duration = new JSlider(JSlider.VERTICAL, 0, 4, 1);
        duration.setOpaque(false);
        duration.setMajorTickSpacing(1);
        duration.setPaintTicks(true);
        
        Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        labels.put(0, new JLabel("Whole"));        
        labels.put(1, new JLabel("Half"));
        labels.put(2, new JLabel("Quarter"));
        labels.put(3, new JLabel("Eighth"));
        labels.put(4, new JLabel("Sixteenth"));
        duration.setLabelTable(labels);
        duration.setPaintLabels(true);


        /*Choices Panel */
        JPanel choicesPanel = new JPanel();
        choicesPanel.setLayout((new GridLayout(1, 2, 0, 1)));
        choicesPanel.setOpaque(false);
        choicesPanel.add(radioPanel);
        choicesPanel.add(duration);


        /*Tool panel */
        JPanel toolPanel = new JPanel();
        toolPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        toolPanel.setLayout((new GridLayout(3, 1, 30, 30)));
        toolPanel.setOpaque(false);
        toolPanel.add(buttonPanel);
        toolPanel.add(choicesPanel);


        /*Main Panel */
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(toolPanel, BorderLayout.WEST);
        mainPanel.add(contentPane, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);       
        mainPanel.setBackground(Color.WHITE);

        f.add(mainPanel);


        /*Title etc */        
        f.setTitle("Music Editor");
        f.setSize(800, 600);
        f.setMinimumSize(new Dimension(600,400));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

}
/**
 *  
 * ICS4U
 * Window Class
 * Conway's Game of Life
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import javax.swing.event.*;  // Needed for ActionListener
import java.io.*;

class Window extends JFrame
{
  static Colony colony = new Colony (0.5);
  static Timer t;
  public static final int MAX_DELAY = 200;
  private boolean started = false;
  private JSlider speed;
  private JSlider size;
  private JRadioButton pop, erad;
  private final JButton submit = new JButton("Submit");
  private final JFrame saveWin = new JFrame("Save");
  private final JTextField nameField = new JTextField(10);
  
  //======================================================== constructor
  public Window ()
  {
    // 1... Create/initialize components
    BtnListener btnListener = new BtnListener (); // listener for all buttons
    
    JButton simulateBtn = new JButton ("Simulate");
    simulateBtn.addActionListener (btnListener);
    
    submit.addActionListener(new BtnListener());
    // 2... Create content pane, set layout
    JPanel content = new JPanel ();        // Create a content pane
    content.setLayout (new BorderLayout ()); // Use BorderLayout for panel
    JPanel north = new JPanel ();
    north.setLayout (new FlowLayout ()); // Use FlowLayout for input area
    
    DrawArea board = new DrawArea (550, 550);
    
    // 3... Add the components to the input area.
    
    // Add a JSlider
    speed = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
    speed.setMajorTickSpacing(2);
    speed.setMinorTickSpacing(1);
    speed.setPaintTicks(true);
    speed.setPaintLabels(true);
    speed.addChangeListener(new SliderListener());
    north.add (new JLabel("Generations per second"));
    north.add (speed);
    
    north.add (new JLabel("               "));
    north.add (simulateBtn);
    
    JPanel south = new JPanel();
    ButtonGroup group = new ButtonGroup();
    pop = new JRadioButton ("Populate");
    pop.setSelected(true);
    erad = new JRadioButton ("Eradicate");
    group.add(pop);
    group.add(erad);
    
    south.add(pop);
    south.add(erad);
    south.add(new JLabel("         "));
    south.add(new JLabel("Populate/Eradicate Size"));
    
    // Add a JSlider
    size = new JSlider(JSlider.HORIZONTAL, 0, 30, 15);
    size.setMajorTickSpacing(5);
    size.setMinorTickSpacing(2);
    size.setPaintTicks(true);
    size.setPaintLabels(true);
    south.add(size);
    
    content.add (north, BorderLayout.PAGE_START); // Input area
    content.add (board, BorderLayout.CENTER); // Output area
    content.add (south, BorderLayout.PAGE_END);
    
    //add MenuBar
    JMenuBar bar = createMenuBar();
    setJMenuBar(bar);
    
    // 4... Set this window's attributes.
    setContentPane (content);
    pack ();
    setTitle ("Life Simulation");
    //setSize (510, 570);
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo (null);           // Center window.
  }
  
  public JMenuBar createMenuBar() {
    //The Objects of the JFrame
    JMenuBar bar = new JMenuBar();
    JMenuItem menuItem;
    
    //Second JMenu
    JMenu menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_F);
    bar.add(menu);
    
    //JMenuItem
    menuItem = new JMenuItem("Open...", KeyEvent.VK_O);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
    menuItem.addActionListener(new MenuListener());
    menu.add(menuItem);
    
    //JMenuItem
    menuItem = new JMenuItem("Save", KeyEvent.VK_S);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    menuItem.addActionListener(new MenuListener());
    menu.add(menuItem);
    
    //JMenuItem
    menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
    menuItem.addActionListener(new MenuListener());
    bar.add(menuItem);
    
    bar.add(new JMenuItem());
    
    return bar;
  }
  
  class MenuListener implements ActionListener
  {
    public void actionPerformed (ActionEvent e)
    {
      JMenuItem source = (JMenuItem) e.getSource();
      String s = source.getText();
      if(s.equals("Open..."))
      {
        JFileChooser fileChooser = new JFileChooser();
        int value = fileChooser.showOpenDialog(getContentPane());
        if(value == JFileChooser.APPROVE_OPTION)
        {
          File f = fileChooser.getSelectedFile();
          try{
            colony.load(f);
            repaint();
           // t.stop();
          }catch(Exception ex)
          {
            ex.printStackTrace();
          }
        }
      }else if(s.equals("Save"))
      { 
        JPanel content = new JPanel();
        content.add(new JLabel("Enter name of file: "));
        content.add(nameField);
        content.add(submit);
        
        saveWin.setContentPane (content);
        saveWin.pack ();
        //setSize (510, 570);
        saveWin.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        saveWin.setLocationRelativeTo (null);
        saveWin.setVisible(true);
      }else if(s.equals("Exit"))
      {
        System.exit(0);
      }
    }
  }
  
  class BtnListener implements ActionListener 
  {
    public void actionPerformed (ActionEvent e)
    {
      if (e.getActionCommand ().equals ("Simulate"))
      {
        if (!started)
        {
          Movement moveColony = new Movement (); // ActionListener for timer
          t = new Timer (MAX_DELAY, moveColony); // set up Movement to run every 200 milliseconds
          t.start (); // start simulation
          started = true;
        }
        t.start();
        if((int)speed.getValue() == 0)
        {
          t.stop();
        }else
        {
          t.setDelay(1000 / (int)speed.getValue());
        }
      }else if(e.getSource() == submit)
      {
        if(colony.save(nameField.getText()))
        {
          JOptionPane.showMessageDialog(saveWin, "File successfully saved.");
        }else{
          JOptionPane.showMessageDialog(saveWin, "File save unsuccessful.");
        }
        saveWin.setVisible(false);
      }
      repaint ();            // refresh display of colony
    }
  }
  
  class SliderListener implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
      JSlider source = (JSlider)e.getSource();
      if (!source.getValueIsAdjusting()) {
        if(source == speed)
        {
          t.start();
          if((int)source.getValue() == 0)
          {
            t.stop();
          }else
          {
            t.setDelay(1000 / (int)source.getValue());
          }
        }
      }    
    }
  }
  
  class DrawArea extends JPanel
  {
    public DrawArea (int width, int height)
    {
      this.setPreferredSize (new Dimension (width, height)); // size
      this.addMouseListener(new PopErad());
    }
    
    
    public void paintComponent (Graphics g)
    {
      colony.show (g); // display current state of colony
    }
    
    class PopErad implements MouseListener
    {
      public void mousePressed(MouseEvent e) {
      }
      
      public void mouseReleased(MouseEvent e) {
      }
      
      public void mouseEntered(MouseEvent e) {
      }
      
      public void mouseExited(MouseEvent e) {
      }
      
      public void mouseClicked(MouseEvent e) {
        if(started)
        {
          try{
            if(pop.isSelected())
            {
              colony.populate(e.getY() / Colony.BOX, e.getX() / Colony.BOX, (int)size.getValue());
            }else{
              colony.eradicate(e.getY() / Colony.BOX, e.getX() / Colony.BOX, (int)size.getValue());
            }
            t.stop();
            repaint();
          }catch(Exception ex)
          {
          }
        }
      }
    }
  }
  
  
  class Movement implements ActionListener
  {
    public void actionPerformed (ActionEvent event)
    {
      colony.advance (); // advance to the next time step
      repaint (); // refresh 
    }
  }
  
  
  //======================================================== method main
  public static void main (String[] args)
  {
    Window window = new Window ();
    window.setVisible (true);
  }
}

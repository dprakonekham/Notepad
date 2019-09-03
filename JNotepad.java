
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.*;
import java.net.URI;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class JNotepad extends JFontChooser
{
    JTextArea txtArea;
    Boolean documentChanged = false;
    Boolean doc = false;
    PageFormat pf;
    Font defaultFont = new Font("Courier New", Font.PLAIN,12);

    JNotepad()
    {
        JFrame frame = new JFrame("JNotepad");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        ImageIcon appicon = new ImageIcon("JNotepad.png");
        frame.setIconImage(appicon.getImage());

        frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
        String[] options = new String[3];
        options[0] = new String("Save");
        options[1] = new String("Don't Save");
        options[2] = new String("Cancel");
        WindowListener closeOperation = (new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                int confirm = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "JNotepad", 0, JOptionPane.PLAIN_MESSAGE, null, options, null);
                if (confirm == 1)
                {
                    System.exit(0);
                }
            }
        });

        if(doc==false)
        {
            txtArea = new JTextArea(null, "", 0, 0);
        }
        System.out.println(txtArea.getText());
        if(!txtArea.getText().equals("") && documentChanged)
        {
            frame.addWindowListener(closeOperation);
        }
        else if(!txtArea.getText().equals("") && !documentChanged)
        {
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        }
        else if(txtArea.getText().equals(""))
        {
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        }
        else if(!documentChanged)
        {
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        }
        // if the file was changed
        //frame.addDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        //else

        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic('f');

        JPopupMenu jpu = new JPopupMenu();
        JMenuItem cut = new JMenuItem("Cut",'t');
        cut.setAccelerator(KeyStroke.getKeyStroke("control X"));
        cut.addActionListener(ae ->
        {
            txtArea.cut();
        });
        JMenuItem copy = new JMenuItem("Copy",'c');
        copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
        copy.addActionListener(ae ->
        {
            txtArea.copy();
        });
        JMenuItem paste = new JMenuItem("Paste",'p');
        paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
        paste.addActionListener(ae ->
        {
            txtArea.paste();
        });
        jpu.add(cut);
        jpu.add(copy);
        jpu.add(paste);
        txtArea.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                if(e.isPopupTrigger())
                {
                    jpu.show(e.getComponent(),e.getX(),e.getY());
                }
            }
            public void mouseReleased(MouseEvent e)
            {
                if(e.isPopupTrigger())
                {
                    jpu.show(e.getComponent(),e.getX(),e.getY());
                }
            }
        });
        JMenuItem newFile = new JMenuItem("New",'n');
        newFile.setAccelerator(KeyStroke.getKeyStroke("control N"));
        newFile.addActionListener(ae ->
        {
            doc = false;
            if(txtArea.getText().trim().equals(""))
            {
                txtArea.setText("");
            }
            else
            {
                int confirm = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "JNotepad", 0, JOptionPane.PLAIN_MESSAGE, null, options, null);
                if (confirm == 0)
                {
                    JFileChooser jFileChooser = new JFileChooser(".");
                    jFileChooser.setSelectedFile(new File("*.txt"));
                    jFileChooser.setFileFilter(new FileNameExtensionFilter("Text Document(*.txt)", "txt"));
                    int result = jFileChooser.showSaveDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        String filename = jFileChooser.getSelectedFile().toString();
                        if (filename.endsWith(".txt"))
                        {
                            filename += ".txt";
                            try {
                                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                                writer.write(txtArea.getText());
                                writer.close();
                            } catch (IOException ioe) {

                            }
                        } else {
                            try {
                                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                                writer.write(txtArea.getText());
                                writer.close();
                            } catch (IOException ioe) {

                            }
                        }
                    }
                }
                else if (confirm == 1)
                {
                    txtArea.setText("");
                }
                else if (confirm == 2)
                {

                }
            }
        });

        JMenuItem open = new JMenuItem("Open...", 'o');
        open.setAccelerator(KeyStroke.getKeyStroke("control O"));
        open.addActionListener(ae ->
        {
            JFileChooser jFileChooser = new JFileChooser(".");
            FileFilter javaFF = new FileFilter()
            {
                public boolean accept(File f)
                {
                    if(f.getName().endsWith(".java")) return true;
                    if(f.isDirectory()) return true;

                    return false;
                }
                public String getDescription()
                {

                    return "Java Source Code Files (.java)";
                }
            };
            FileFilter txtFF = new FileFilter()
            {
                public boolean accept(File f)
                {
                    if(f.getName().endsWith(".txt")) return true;
                    if(f.isDirectory()) return true;

                    return false;
                }
                public String getDescription()
                {

                    return "Text Documents (.txt)";
                }
            };
            jFileChooser.setFileFilter(javaFF);
            jFileChooser.setFileFilter(txtFF);
            int result = jFileChooser.showOpenDialog(frame);
            if(result == JFileChooser.APPROVE_OPTION)
            {
                doc = true;
                File f = jFileChooser.getSelectedFile();
                try (BufferedReader myReader = new BufferedReader(new FileReader(f)))
                {
                    txtArea.read(myReader,null);
                }
                catch (IOException exp)
                {
                    System.out.println("Exception");
                }
            }
        });

        JMenuItem save = new JMenuItem("Save",'s');
        save.setAccelerator(KeyStroke.getKeyStroke("control S"));
        save.addActionListener(ae->
        {
            JFileChooser jFileChooser = new JFileChooser(".");
            jFileChooser.setSelectedFile(new File("*.txt"));
            jFileChooser.setFileFilter(new FileNameExtensionFilter("Text Document(*.txt)","txt"));
            int result = jFileChooser.showSaveDialog(frame);
            if(result == JFileChooser.APPROVE_OPTION)
            {
                String filename = jFileChooser.getSelectedFile().toString();
                if (filename .endsWith(".txt"))
                {
                    filename += ".txt";

                    try
                    {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                        writer.write(txtArea.getText());
                        writer.close();
                    }
                    catch(IOException ioe)
                    {

                    }
                }
                else
                {
                    try
                    {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                        writer.write(txtArea.getText());
                        writer.close();
                    }
                    catch(IOException ioe)
                    {

                    }
                }
            }
        });

        JMenuItem saveAs = new JMenuItem("Save As...");
        saveAs.setDisplayedMnemonicIndex(5);
        saveAs.addActionListener(ae ->
        {
            JFileChooser jFileChooser = new JFileChooser(".");
            jFileChooser.setSelectedFile(new File("*.txt"));
            jFileChooser.setFileFilter(new FileNameExtensionFilter("Text Document(*.txt)","txt"));
            int result = jFileChooser.showSaveDialog(frame);
            if(result == JFileChooser.APPROVE_OPTION)
            {
                String filename = jFileChooser.getSelectedFile().toString();
                if (filename .endsWith(".txt"))
                {
                    filename += ".txt";

                    try
                    {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                        writer.write(txtArea.getText());
                        writer.close();
                    }
                    catch(IOException ioe)
                    {

                    }
                }
                else
                {
                    try
                    {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                        writer.write(txtArea.getText());
                        writer.close();
                    }
                    catch(IOException ioe)
                    {

                    }
                }
            }
        });

        JMenuItem pageSetup = new JMenuItem("Page Setup...",'u');
        pageSetup.addActionListener(ae ->
        {
            PrinterJob job = PrinterJob.getPrinterJob();
            pf = job.pageDialog(job.defaultPage());

        });

        JMenuItem print = new JMenuItem("Print...",'p');
        print.setAccelerator(KeyStroke.getKeyStroke("control P"));
        print.addActionListener(ae ->
        {
            PrinterJob job = PrinterJob.getPrinterJob();
            Printable printable = txtArea.getPrintable(null,null);
                job.setPrintable(printable,pf);
                job.printDialog();
        });

        JMenuItem exit = new JMenuItem("Exit",'x');
        exit.addActionListener(ae ->
        {
            if(documentChanged)
            {
                JLabel exitText = new JLabel("Do you want to save changes?");
                exitText.setHorizontalAlignment(JLabel.CENTER);
                int result = JOptionPane.showConfirmDialog(frame, exitText, "Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                System.out.println(result);
                if (result == 0) {
                    System.exit(0);
                }
            }
            else
            {
                System.exit(0);
            }
        });

        file.add(newFile);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.addSeparator();
        file.add(pageSetup);
        file.add(print);
        file.addSeparator();
        file.add(exit);
        bar.add(file);

        JMenu edit = new JMenu("Edit");
        edit.setMnemonic('e');
        JMenuItem undo = new JMenuItem("Undo",'u');
        cut = new JMenuItem("Cut",'t');
        cut.setAccelerator(KeyStroke.getKeyStroke("control X"));
        cut.addActionListener(ae ->
        {
            txtArea.cut();
        });
        copy = new JMenuItem("Copy",'c');
        copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
        copy.addActionListener(ae ->
        {
            txtArea.copy();
        });
        paste = new JMenuItem("Paste",'p');
        paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
        paste.addActionListener(ae ->
        {
            txtArea.paste();
        });
        JMenuItem delete = new JMenuItem("Delete",'l');
        delete.setAccelerator(KeyStroke.getKeyStroke("Del"));
        delete.addActionListener(ae ->
        {

        });
        JMenuItem find = new JMenuItem("Find...",'f');
        find.setAccelerator(KeyStroke.getKeyStroke("control F"));
        find.addActionListener(ae ->
        {

        });
        JMenuItem findNext = new JMenuItem("Find Next");
        findNext.setDisplayedMnemonicIndex(5);
        findNext.addActionListener(ae ->
        {

        });
        JMenuItem replace = new JMenuItem("Replace...",'r');
        replace.setAccelerator(KeyStroke.getKeyStroke("control H"));
        JMenuItem goTo = new JMenuItem("Go To...",'g');
        goTo.setAccelerator(KeyStroke.getKeyStroke("control G"));

        JMenuItem selectAll = new JMenuItem("Select All",'a');
        selectAll.setAccelerator(KeyStroke.getKeyStroke("control A"));
        selectAll.addActionListener(ae ->
        {
            txtArea.selectAll();
        });

        JMenuItem timeDate = new JMenuItem("Time/Date",'d');
        timeDate.setAccelerator(KeyStroke.getKeyStroke("F5"));
        timeDate.addActionListener(ae ->
        {
            txtArea.append(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")));
            txtArea.append(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        });
        edit.add(undo);
        edit.addSeparator();
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(delete);
        edit.addSeparator();
        edit.add(find);
        edit.add(findNext);
        edit.add(replace);
        edit.add(goTo);
        edit.addSeparator();
        edit.add(selectAll);
        edit.add(timeDate);
        bar.add(edit);

        JMenu format = new JMenu("Format");
        format.setMnemonic('o');
        JCheckBoxMenuItem wordWrap = new JCheckBoxMenuItem("Word Wrap",true);
        wordWrap.setMnemonic('w');
        wordWrap.addItemListener(e ->
        {
            if(wordWrap.isSelected())
            {
                txtArea.setWrapStyleWord(true);
                txtArea.setLineWrap(true);
            }
            else
            {
                txtArea.setWrapStyleWord(false);
                txtArea.setLineWrap(false);
            }
        });

        JMenuItem font = new JMenuItem("Font...",'f');
        font.addActionListener(ae ->
        {
            Font fontSel = JFontChooser.showDialog(frame,defaultFont);
            if(fontSel != null)
            {
                defaultFont = fontSel;
            }
            txtArea.setFont(defaultFont);
        });
        format.add(wordWrap);
        format.add(font);
        bar.add(format);

        JMenu view = new JMenu("View");
        view.setMnemonic('v');
        JMenuItem statusBar = new JMenuItem("Status Bar",'s');
        view.add(statusBar);
        statusBar.setEnabled(false);
        bar.add(view);

        JMenu help = new JMenu("Help");
        help.setMnemonic('h');
        JMenuItem viewHelp = new JMenuItem("View Help",'h');
        viewHelp.addActionListener(ae ->
        {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE))
            {
                try
                {
                    Desktop.getDesktop().browse(new URI("https://www.bing.com/search?q=get+help+with+notepad+in+windows+10&filters=guid:%224466414-en-dia%22%20lang:%22en%22&form=T00032&ocid=HelpPane-BingIA"));
                }
                catch(Exception ex)
                {

                }
            }
            else
            {
                System.out.println("Not Supported");
            }
        });
        JMenuItem about = new JMenuItem("About JNotepad",'a');
        about.addActionListener(ae ->
        {
            JDialog aboutDialog = new JDialog(frame,"About",true);
            aboutDialog.setLayout(new BorderLayout());
            aboutDialog.setSize(360,240);
            aboutDialog.setIconImage(appicon.getImage());

            JLabel aboutText = new JLabel("(c)Danny Prakonekham");
            aboutText.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel button = new JPanel();
            JButton ok = new JButton("Ok");
            button.add(ok);
            ok.addActionListener(exitEvent ->
            {
                aboutDialog.dispose();
            });
            aboutDialog.add(aboutText,BorderLayout.CENTER);
            aboutDialog.add(button,BorderLayout.PAGE_END);
            aboutDialog.setLocationRelativeTo(frame);
            aboutDialog.setResizable(false);
            aboutDialog.setVisible(true);
        });
        help.add(viewHelp);
        help.addSeparator();
        help.add(about);
        bar.add(help);

        JScrollPane jsp = new JScrollPane(txtArea);
        jsp.setHorizontalScrollBarPolicy(jsp.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp.setVerticalScrollBarPolicy(jsp.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(jsp);
        frame.setJMenuBar(bar);
        frame.setVisible(true);
    }
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new JNotepad();
            }
        });
    }
}

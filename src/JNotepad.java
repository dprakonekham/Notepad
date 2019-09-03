// Description: This is a basic text editor that allows you to create, open, and also read different types of files.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.*;
import java.net.URI;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class JNotepad extends JFontChooser
{
    JTextArea txtArea;
    AtomicBoolean documentChanged = new AtomicBoolean();
    String currentFile;
    PageFormat pf;
    Font defaultFont = new Font("Courier", Font.PLAIN,12);
    JMenuItem statusBar;
    public static String parameter;

    JNotepad()
    {

        JFrame frame = new JFrame("JNotepad");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        ImageIcon appicon = new ImageIcon("JNotepad.png");
        frame.setIconImage(appicon.getImage());

        frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
        String[] options = new String[3]; //Making a custom close operation for when the document is changed
        options[0] = new String("Save");
        options[1] = new String("Don't Save");
        options[2] = new String("Cancel");
        WindowListener closeOperation = new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                int confirm = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "JNotepad", 0, JOptionPane.PLAIN_MESSAGE, null, options, null);
                if(confirm == 0)
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
                            catch(Exception ie)
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
                            catch(Exception ie)
                            {

                            }
                        }
                    }
                }
                else if (confirm == 1)
                {
                    System.exit(0);
                }
            }
        };
        txtArea = new JTextArea(null, "", 0, 0);

        //If there is a parameter
        currentFile = parameter;
        if(parameter != null)
        {
            File parameterFile = new File(parameter);
            String parameterPath = System.getProperty("user.dir");
            parameterPath = parameterPath + File.separator  + parameter;
            File newParameterFile = new File(parameterPath);
            System.out.println(parameterPath);
            if(parameterFile.exists())
            {
                try (BufferedReader br = new BufferedReader(new FileReader(newParameterFile)))
                {
                    txtArea.read(br, null);
                }
                catch (Exception e)
                {

                }
            }
            else
            {
                int result = JOptionPane.showOptionDialog(frame, "Do you to create a new file?", "JNotepad", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (result == 0) {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(parameter + ".txt"));
                        writer.write(txtArea.getText());
                        writer.close();
                    } catch (Exception ie) {

                    }
                }
            }
        }

        txtArea.getDocument().addDocumentListener(new DocumentListener()
        {
            public void changedUpdate(DocumentEvent e)
            {
                setAtomicBooleanTrue();
            }
            public void insertUpdate(DocumentEvent e)
            {
                setAtomicBooleanTrue();
            }
            public void removeUpdate(DocumentEvent e)
            {
                setAtomicBooleanTrue();
            }
            private void setAtomicBooleanTrue()
            {
                documentChanged.set(true);
                if(!txtArea.getText().equals(""))//If the txtArea is not blank and the document is changed
                {
                frame.addWindowListener(closeOperation);
                }
            }
        });
        if(txtArea.getText().equals(""))//If txtArea is blank
        {
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        }
        else if(!txtArea.getText().equals("") && !documentChanged.get())//If txtArea is not blank and the document is not changed
        {
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        }

        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic('f');

        JPopupMenu jpu = new JPopupMenu();//Creating a JPopupMenu
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
            if(txtArea.getText().trim().equals(""))//If txtArea is blank just set the txtArea to blank
            {
                txtArea.setText("");
            }
            else//Save Dialog
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
                            } catch (Exception e) {

                            }
                        } else {
                            try {
                                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                                writer.write(txtArea.getText());
                                writer.close();
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                else if (confirm == 1)
                {
                    txtArea.setText("");
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
                File f = jFileChooser.getSelectedFile();
                currentFile = jFileChooser.getSelectedFile().toString();
                try (BufferedReader br = new BufferedReader(new FileReader(f)))
                {
                    txtArea.read(br,null);
                }
                catch (Exception e)
                {

                }
            }
            txtArea.getDocument().addDocumentListener(new DocumentListener()
            {
                public void changedUpdate(DocumentEvent e)
                {
                    printMyLines();
                }
                public void insertUpdate(DocumentEvent e)
                {
                    printMyLines();
                }

                public void removeUpdate(DocumentEvent e)
                {
                    printMyLines();
                }

                private void printMyLines()
                {
                    documentChanged.set(true);
                    setCloseOperation();
                }

                private void setCloseOperation()
                {
                    if(!txtArea.getText().equals(""))
                    {
                        frame.addWindowListener(closeOperation);
                    }
                }
            });
        });

        JMenuItem save = new JMenuItem("Save",'s');
        save.setAccelerator(KeyStroke.getKeyStroke("control S"));
        save.addActionListener(ae->
        {
            if(txtArea.getDocument() == null)
            {
                JFileChooser jFileChooser = new JFileChooser(".");
                jFileChooser.setSelectedFile(new File("*.txt"));
                jFileChooser.setFileFilter(new FileNameExtensionFilter("Text Document(*.txt)", "txt"));
                int result = jFileChooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filename = jFileChooser.getSelectedFile().toString();
                    if (filename.endsWith(".txt")) {
                        filename += ".txt";

                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                            writer.write(txtArea.getText());
                            writer.close();
                        } catch (Exception e) {

                        }
                    } else {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                            writer.write(txtArea.getText());
                            writer.close();
                        } catch (Exception e) {

                        }
                    }
                }
            }
            else if(txtArea.getDocument() != null)
            {
                try
                {
                    if(currentFile == null)
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
                                catch(Exception e)
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
                                catch(Exception e)
                                {

                                }
                            }
                        }
                    }
                    else
                    {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile));
                        writer.write(txtArea.getText());
                        writer.close();
                    }

                } catch (Exception e) {

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
                    catch(Exception e)
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
                    catch(Exception e)
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
            if(pf == null)
            {
                try
                {
                    PrinterJob job = PrinterJob.getPrinterJob();
                    Printable printable = txtArea.getPrintable(null, null);
                    job.setPrintable(printable);
                    job.printDialog();
                    job.print();
                }
                catch(Exception e)
                {

                }
            }
            else
            {
                try
                {
                    PrinterJob job = PrinterJob.getPrinterJob();
                    Printable printable = txtArea.getPrintable(null, null);
                    job.setPrintable(printable, pf);
                    job.printDialog();
                    job.print();
                }
                catch(Exception e)
                {

                }
            }
        });

        JMenuItem exit = new JMenuItem("Exit",'x');
        exit.addActionListener(ae ->
        {
            if(documentChanged.get())
            {
                JLabel exitText = new JLabel("Do you want to save changes?");
                exitText.setHorizontalAlignment(JLabel.CENTER);
                int result = JOptionPane.showConfirmDialog(frame, exitText, "Exit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == 0)
                {
                    JFileChooser jFileChooser = new JFileChooser(".");
                    jFileChooser.setSelectedFile(new File("*.txt"));
                    jFileChooser.setFileFilter(new FileNameExtensionFilter("Text Document(*.txt)","txt"));
                    result = jFileChooser.showSaveDialog(frame);
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
                            catch(Exception e)
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
                            catch(Exception e)
                            {

                            }
                        }
                    }
                }
                else if(result == 1)
                {
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
            int currentCaretPosition = txtArea.getCaretPosition();
            txtArea.replaceRange(null,currentCaretPosition,currentCaretPosition);
        });
        JMenuItem find = new JMenuItem("Find...",'f');
        find.setAccelerator(KeyStroke.getKeyStroke("control F"));
        find.addActionListener(ae ->
        {
            JDialog findDialog = new JDialog(frame,"Find",false);
            findDialog.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            findDialog.setSize(330,130);

            JLabel findWhat = new JLabel("Find what:");
            JTextField input = new JTextField(20);

            JPanel button = new JPanel();
            JButton findNextButton = new JButton("Find Next");
            JButton cancel = new JButton("Cancel");

            JCheckBox matchCase = new JCheckBox("Match Case");
            button.add(findNextButton);
            findNextButton.addActionListener(exitEvent ->
            {
                if(matchCase.isSelected())
                {
                    try
                    {
                        Highlighter h = txtArea.getHighlighter();
                        h.removeAllHighlights();
                        String userInput = input.getText();
                        int pos = txtArea.getText().indexOf(userInput, txtArea.getCaretPosition());
                        txtArea.setCaretPosition(pos + userInput.length());
                        h.addHighlight(pos, pos + userInput.length(), DefaultHighlighter.DefaultPainter);
                    } catch (Exception e) {

                    }
                }
                else if(!matchCase.isSelected())
                {
                    try {
                        Highlighter h = txtArea.getHighlighter();
                        h.removeAllHighlights();
                        String userInput  = input.getText().toLowerCase();
                        int pos = txtArea.getText().toLowerCase().indexOf(userInput, txtArea.getCaretPosition());
                        txtArea.setCaretPosition(pos + userInput.length());
                        h.addHighlight(pos, pos + userInput.length(), DefaultHighlighter.DefaultPainter);
                    } catch (Exception e) {

                    }
                }
            });
            cancel.addActionListener(aae ->
            {
                findDialog.dispose();
            });
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            findDialog.add(findWhat,c);
            c.weightx = 0.0;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            findDialog.add(input,c);
            c.gridwidth = 2;
            c.gridx = 7;
            findDialog.add(findNextButton,c);
            c.gridy = 2;
            findDialog.add(cancel,c);
            c.gridx = 0;
            c.gridy = 2;
            findDialog.add(matchCase,c);

            findDialog.setLocationRelativeTo(frame);
            findDialog.setResizable(false);
            findDialog.setVisible(true);
        });
        JMenuItem findNext = new JMenuItem("Find Next");
        findNext.setDisplayedMnemonicIndex(5);
        findNext.addActionListener(ae ->
        {
            JDialog findDialog = new JDialog(frame,"Find",false);
            findDialog.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            findDialog.setSize(330,130);

            JLabel findWhat = new JLabel("Find what:");
            JTextField input = new JTextField(20);

            JPanel button = new JPanel();
            JButton findNextButton = new JButton("Find Next");
            JButton cancel = new JButton("Cancel");

            JCheckBox matchCase = new JCheckBox("Match Case");
            button.add(findNextButton);
            findNextButton.addActionListener(exitEvent ->
            {
                if(matchCase.isSelected())
                {
                    try
                    {
                        Highlighter h = txtArea.getHighlighter();
                        h.removeAllHighlights();
                        String userInput = input.getText();
                        int pos = txtArea.getText().indexOf(userInput, txtArea.getCaretPosition());
                        txtArea.setCaretPosition(pos + userInput.length());
                        h.addHighlight(pos, pos + userInput.length(), DefaultHighlighter.DefaultPainter);
                    } catch (Exception e) {

                    }
                }
                else if(!matchCase.isSelected())
                {
                    try {
                        Highlighter h = txtArea.getHighlighter();
                        h.removeAllHighlights();
                        String userInput  = input.getText().toLowerCase();
                        int pos = txtArea.getText().toLowerCase().indexOf(userInput, txtArea.getCaretPosition());
                        txtArea.setCaretPosition(pos + userInput.length());
                        h.addHighlight(pos, pos + userInput.length(), DefaultHighlighter.DefaultPainter);
                    } catch (Exception e) {

                    }
                }
            });
            cancel.addActionListener(aae ->
            {
                findDialog.dispose();
            });
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            findDialog.add(findWhat,c);
            c.weightx = 0.0;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            findDialog.add(input,c);
            c.gridwidth = 2;
            c.gridx = 7;
            findDialog.add(findNextButton,c);
            c.gridy = 2;
            findDialog.add(cancel,c);
            c.gridx = 0;
            c.gridy = 2;
            findDialog.add(matchCase,c);

            findDialog.setLocationRelativeTo(frame);
            findDialog.setResizable(false);
            findDialog.setVisible(true);

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
        statusBar = new JMenuItem("Status Bar",'s');
        statusBar.setEnabled(false);
        if(!statusBar.isEnabled())
        {
            wordWrap.addItemListener(e ->
            {
                if (wordWrap.isSelected())
                {
                    statusBar.setEnabled(false);
                    txtArea.setWrapStyleWord(true);
                    txtArea.setLineWrap(true);
                }
                else
                {
                    statusBar.setEnabled(true);
                    txtArea.setWrapStyleWord(false);
                    txtArea.setLineWrap(false);
                }
            });
        }

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
        if(!wordWrap.isSelected())
        {
            statusBar.setEnabled(true);
            statusBar.addActionListener(ae ->
            {

            });
        }
        else
        {
            statusBar.setEnabled(false);
        }
        view.add(statusBar);
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
                catch(Exception e)
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
                for(int i = 0;i<args.length;i++)//looping args
                {
                    if(!args[i].equals(""))//if it contains debug
                    {
                        parameter = args[i];
                    }
                    else
                    {
                        parameter = null;
                    }
                }
                new JNotepad();
            }
        });
    }
}

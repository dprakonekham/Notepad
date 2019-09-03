import javax.swing.*;
import java.awt.*;

public class JFontChooser
{
    public static Font showDialog(JFrame parent, Font defaultFont)
    {
        JDialog dlg = new JDialog(parent,"Font",true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(480,360);

        JLabel name = new JLabel("Font:");
        DefaultListModel fontLM = new DefaultListModel();
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for(String f : fonts)
        {
            fontLM.addElement(f);
        }
        JList nameList = new JList(fontLM);//Creating a JList with the fonts and putting it into a JScrollPane
        int index = 0;
        for(int i = 0; i<nameList.getModel().getSize(); i++)
        {
            if(nameList.getModel().getElementAt(i).equals(defaultFont.getName()))
            {
                nameList.setSelectedIndex(i);
                break;
            }
        }
        JScrollPane nameSP = new JScrollPane(nameList);
        nameSP.setPreferredSize(new Dimension(160,200));
        nameSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        nameList.ensureIndexIsVisible(nameList.getSelectedIndex());

        JLabel style = new JLabel("Style:");//Creating and adding the styles
        DefaultListModel styleLM = new DefaultListModel();
        styleLM.addElement("Regular");//This corresponds to the value 0
        styleLM.addElement("Bold");//This corresponds to the value 1
        styleLM.addElement("Italic");//This corresponds to the value 2

        JList styleList = new JList(styleLM);//Creating a JList with the styles and putting it into a JScrollPane
        for(int i = 0; i<styleList.getModel().getSize(); i++)
        {
            if(i == (defaultFont.getStyle()))
            {
                styleList.setSelectedIndex(i);
                break;
            }
        }
        JScrollPane styleSP = new JScrollPane(styleList);
        styleSP.setPreferredSize(new Dimension(160,150));
        styleSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel size = new JLabel("Size:");
        SpinnerNumberModel numSpinner = new SpinnerNumberModel(defaultFont.getSize(),6,72,1);//Initial value is 12 minimum is 6 max is 72 and step size is 1
        JSpinner numberSpinner = new JSpinner(numSpinner);
        numberSpinner.setPreferredSize(new Dimension(50,40));

        JPanel buttons = new JPanel(new FlowLayout());//Creating a JPanel for the buttons so I can add them both to the bottom of the dialog
        JButton ok = new JButton("Ok");
        JButton cancel = new JButton("Cancel");
        buttons.add(ok);
        buttons.add(cancel);

        ok.setActionCommand("Cancel");//This is basically a flag for the ternary operator
        ok.addActionListener(ae ->
        {
            try
            {
                String fontName = (String)nameList.getSelectedValue();
                int styleValue = styleList.getSelectedIndex();//0 for normal,1 for bold,2 for italic
                int sizeValue = (Integer)numberSpinner.getValue();
                ok.setActionCommand("Ok");
                dlg.dispose();
            }
            catch(NumberFormatException nf)
            {
                if(nameList.getSelectedIndex() < 0)
                {
                    throw new NumberFormatException("Font not selected");
                }
                else if(styleList.getSelectedIndex() < 0)
                {
                    throw new NumberFormatException("Style not selected");
                }
            }
        });
        cancel.addActionListener(ae ->
        {
            ok.setActionCommand("Cancel");
            dlg.dispose();
        });
        JPanel dialogContent = new JPanel();
        Box nameBox = Box.createVerticalBox();
        nameBox.add(name);
        nameBox.add(nameSP);
        dialogContent.add(nameBox);

        Box styleBox = Box.createVerticalBox();
        styleBox.add(style);
        styleBox.add(styleSP);
        dialogContent.add(styleBox);

        Box sizeBox = Box.createVerticalBox();
        sizeBox.add(size);
        sizeBox.add(numberSpinner);
        dialogContent.add(sizeBox);

        dlg.add(dialogContent,BorderLayout.CENTER);

        Box btnBox = Box.createHorizontalBox();
        btnBox.add(buttons);
        dlg.add(btnBox,BorderLayout.SOUTH);

        dlg.setLocationRelativeTo(parent);
        dlg.setResizable(false);
        dlg.setVisible(true);

        return ok.getActionCommand().equals("Ok") ? new Font((String)nameList.getSelectedValue(),styleList.getSelectedIndex(),(Integer)numberSpinner.getValue()): null;
        //This is a ternary operator that only returns a new font when the actionCommand is "ok", if it is "cancel" then it returns null
    }

}

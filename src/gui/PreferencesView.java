package gui;

import controller.PreferencesController;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import preferences.Constants;

public class PreferencesView extends JFrame {

    private final JPanel inputPanel;
    private final JPanel articleSizeOverallPanel;
    private final JLabel articleSizeInformationLabel;
    private final JPanel articleSizePanel;
    private final JPanel articleSizeLabelPanel;
    private final JLabel articleSizeTextLabel;
    private final JPanel articleSizeInuptPanel;
    private final JTextField articleSizeInput;
    private final JPanel updateFrequencyOverallPanel;
    private final JLabel updateInformationLabel;
    private final JPanel updateInformationPanel;
    private final JPanel updateFrequencyLabelPanel;
    private final JLabel updateTextLabel;
    private final JPanel updateFrequencyInputPanel;
    private final JTextField updateFrequencyInput;
    private final JPanel onlineOverallPanel;
    private final JLabel onlineInformationLabel;
    private final JPanel onlinePanel;
    private final JPanel onlineLabelPanel;
    private final JLabel onlineTextLabel;
    private final JPanel onlineChoicePanel;
    private final JCheckBox onlineCheckBoxYes;
    private final JCheckBox onlineCheckBoxNo;
    private final JPanel submitPanel;
    private final JButton submit;
    private final JButton cancel;

    public PreferencesView() {

        inputPanel = new JPanel();
        articleSizeOverallPanel = new JPanel();
        articleSizeInformationLabel = new JLabel("Change the value of the amount of articles that you want to display");
        articleSizePanel = new JPanel();
        articleSizeLabelPanel = new JPanel();
        articleSizeTextLabel = new JLabel("Article ammount");
        articleSizeInuptPanel = new JPanel();
        articleSizeInput = new JTextField(20);
        updateFrequencyOverallPanel = new JPanel();
        updateInformationLabel = new JLabel("Update time in seconds");
        updateInformationPanel = new JPanel();
        updateFrequencyLabelPanel = new JPanel();
        updateTextLabel = new JLabel("Update frequency");
        updateFrequencyInputPanel = new JPanel();
        updateFrequencyInput = new JTextField(20);
        onlineOverallPanel = new JPanel();
        onlineInformationLabel = new JLabel("Do you want to be online or not?");
        onlinePanel = new JPanel();
        onlineLabelPanel = new JPanel();
        onlineTextLabel = new JLabel("online");
        onlineChoicePanel = new JPanel();
        onlineCheckBoxYes = new JCheckBox("Yes");
        onlineCheckBoxNo = new JCheckBox("No");
        submitPanel = new JPanel();
        submit = new JButton("Submit");
        cancel = new JButton("Cancel");

        //add overall panel
        this.add(inputPanel, BorderLayout.NORTH);
        this.add(submitPanel, BorderLayout.SOUTH);
        inputPanel.add(articleSizeOverallPanel);
        inputPanel.add(updateFrequencyOverallPanel);
        inputPanel.add(onlineOverallPanel);

        submitPanel.add(submit);
        submitPanel.add(cancel);

        //textsize panel
        articleSizeOverallPanel.add(articleSizeInformationLabel);
        articleSizeOverallPanel.add(articleSizePanel);
        articleSizePanel.add(articleSizeLabelPanel);
        articleSizePanel.add(articleSizeInuptPanel);

        //text size label and input
        articleSizeLabelPanel.add(articleSizeTextLabel);
        articleSizeInuptPanel.add(articleSizeInput);
        articleSizeInput.setText(Integer.toString(Constants.feedAmount));

        //update frequency panel
        updateFrequencyOverallPanel.add(updateInformationLabel);
        updateFrequencyOverallPanel.add(updateInformationPanel);
        updateInformationPanel.add(updateFrequencyLabelPanel);
        updateInformationPanel.add(updateFrequencyInputPanel);

        //update label and input
        updateFrequencyLabelPanel.add(updateTextLabel);
        updateFrequencyInputPanel.add(updateFrequencyInput);
        updateFrequencyInput.setText(Float.toString(Constants.updateTime));

        //online panel
        onlineOverallPanel.add(onlineInformationLabel);
        onlineOverallPanel.add(onlinePanel);
        onlinePanel.add(onlineLabelPanel);
        onlinePanel.add(onlineChoicePanel);

        //online label and checkbox
        onlineLabelPanel.add(onlineTextLabel);
        onlineChoicePanel.add(onlineCheckBoxYes);
        onlineChoicePanel.add(onlineCheckBoxNo);

        //SET LAYOUTS
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        articleSizeOverallPanel.setLayout(new BoxLayout(articleSizeOverallPanel, BoxLayout.Y_AXIS));
        updateFrequencyOverallPanel.setLayout(new BoxLayout(updateFrequencyOverallPanel, BoxLayout.Y_AXIS));
        onlineOverallPanel.setLayout(new BoxLayout(onlineOverallPanel, BoxLayout.Y_AXIS));
        articleSizePanel.setLayout(new BoxLayout(articleSizePanel, BoxLayout.X_AXIS));
        updateInformationPanel.setLayout(new BoxLayout(updateInformationPanel, BoxLayout.X_AXIS));
        onlinePanel.setLayout(new BoxLayout(onlinePanel, BoxLayout.X_AXIS));

        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // centers screen
        this.setLocationRelativeTo(null);
    }

    /**
     * returns the integer value of the input of the text size
     *
     * @return i: integer
     */
    public int getArticleAmount() {
        int i = Integer.parseInt(articleSizeInput.getText());
        return i;
    }

    /**
     * returns the float value of the input of the update frequency
     *
     * @return i : float
     */
    public long getUpdateFrequency() {
        long i = 0;
        String string = updateFrequencyInput.getText();
        String labelTextID = null;
        if (string.contains(".")) {
            labelTextID = string.substring(0,1);
            System.out.println(labelTextID);
            int ints = Integer.parseInt(labelTextID);
            i = (long) ints;
        }
        return i;
    }

    public void addController(PreferencesController controller) {
        submit.addActionListener(controller);
        cancel.addActionListener(controller);
    }
}

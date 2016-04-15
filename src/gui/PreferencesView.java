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
    private final JPanel textSizeOverallPanel;
    private final JLabel textSizeInformationLabel;
    private final JPanel textSizePanel;
    private final JPanel textSizeLabelPanel;
    private final JLabel textSizeTextLabel;
    private final JPanel textSizeInuptPanel;
    private final JTextField textSizeInput;
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
        textSizeOverallPanel = new JPanel();
        textSizeInformationLabel = new JLabel("change the value to increase or defrease the size of the text for the program");
        textSizePanel = new JPanel();
        textSizeLabelPanel = new JPanel();
        textSizeTextLabel = new JLabel("Text size");
        textSizeInuptPanel = new JPanel();
        textSizeInput = new JTextField(20);
        updateFrequencyOverallPanel = new JPanel();
        updateInformationLabel = new JLabel("Update time in minutes");
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
        inputPanel.add(textSizeOverallPanel);
        inputPanel.add(updateFrequencyOverallPanel);
        inputPanel.add(onlineOverallPanel);

        submitPanel.add(submit);
        submitPanel.add(cancel);

        //textsize panel
        textSizeOverallPanel.add(textSizeInformationLabel);
        textSizeOverallPanel.add(textSizePanel);
        textSizePanel.add(textSizeLabelPanel);
        textSizePanel.add(textSizeInuptPanel);

        //text size label and input
        textSizeLabelPanel.add(textSizeTextLabel);
        textSizeInuptPanel.add(textSizeInput);
        textSizeInput.setText(Integer.toString(Constants.fontSize));

        //update frequenxy panel
        updateFrequencyOverallPanel.add(updateInformationLabel);
        updateFrequencyOverallPanel.add(updateInformationPanel);
        updateInformationPanel.add(updateFrequencyLabelPanel);
        updateInformationPanel.add(updateFrequencyInputPanel);

        //update label and input
        updateFrequencyLabelPanel.add(updateTextLabel);
        updateFrequencyInputPanel.add(updateFrequencyInput);
        updateFrequencyInput.setText(Integer.toString(Constants.feedAmount));

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
        textSizeOverallPanel.setLayout(new BoxLayout(textSizeOverallPanel, BoxLayout.Y_AXIS));
        updateFrequencyOverallPanel.setLayout(new BoxLayout(updateFrequencyOverallPanel, BoxLayout.Y_AXIS));
        onlineOverallPanel.setLayout(new BoxLayout(onlineOverallPanel, BoxLayout.Y_AXIS));
        textSizePanel.setLayout(new BoxLayout(textSizePanel, BoxLayout.X_AXIS));
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
     * @return i: int
     */
    public int getTextSize() {
        int i = Integer.parseInt(textSizeInput.getText());
        return i;
    }

    /**
     * returns the integer value of the input of the update frequency
     *
     * @return i : int
     */
    public int getUpdateFrequency() {
        int i = Integer.parseInt(updateFrequencyInput.getText());
        return i;
    }

    public void addController(PreferencesController controller) {
        submit.addActionListener(controller);
        cancel.addActionListener(controller);
    }
}

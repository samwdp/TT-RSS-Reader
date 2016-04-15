package gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.SubscribeController;

/**
 * View that allows a user to subscribe to a new feed
 *
 * @author Sam
 *
 */
public class SubscribeView extends JFrame {

    private JPanel URLPanel;
    private JPanel buttonPanel;
    private JTextField url;
    private JButton subscribe;

    public SubscribeView() {
        URLPanel = new JPanel();
        buttonPanel = new JPanel();
        url = new JTextField(30);
        subscribe = new JButton("Subscribe");
        this.setTitle("Subscribe");

        this.add(URLPanel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.SOUTH);

        URLPanel.add(url);
        buttonPanel.add(subscribe);

        this.pack();
        this.setVisible(true);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // sets size
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        this.setSize(xSize / 2, ySize / 2);
        // centers screen
        this.setLocationRelativeTo(null);
    }

    /**
     * gets the url that the user entered
     *
     * @return s: String
     */
    public String getURL() {
        String s = url.getText();
        return s;
    }

    /**
     * adds the action listener to the button that is a controller
     *
     * @param controller: SubscribeController
     */
    public void addController(SubscribeController controller) {
        subscribe.addActionListener(controller);
    }

    /**
     * hides the display with the dispose() method
     */
    public void hideDisplay() {
        this.dispose();
    }

}

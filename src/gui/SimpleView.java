package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import controller.ArticleController;
import controller.SimpleViewController;
import controller.listener.SimpleViewPreferencesActionListener;
import controller.listener.SimpleViewSubscribeActionListener;
import preferences.Constants;

/**
 * View that displays only the headlines and the articles
 *
 * @author Sam
 *
 */
public class SimpleView extends JFrame {

    private JPanel headlinePanel;
    private JPanel westPanel;
    private JPanel feedPanel;
    private JScrollPane headlineScrollPane;
    private JScrollPane feedScrollPanel;
    private JLabel headlineLabel;
    private JLabel feedLabel;
    private JLabel contentLabel;
    private ArrayList<JLabel> headlineArray;
    private JPanel btnPanel;
    private JButton subscribe;
    private JButton preferences;
    private FeedContextMenu feedContextMenu;

    public SimpleView() {
        westPanel = new JPanel();
        headlinePanel = new JPanel();
        feedPanel = new JPanel();
        feedScrollPanel = new JScrollPane(feedPanel);
        headlineScrollPane = new JScrollPane(headlinePanel);
        headlineArray = new ArrayList<JLabel>();
        btnPanel = new JPanel();
        subscribe = new JButton("Subscribe to feed");
        preferences = new JButton("Preferences");

        this.setTitle("Simply TT-RSS");

        //frame
        this.add(headlineScrollPane, BorderLayout.CENTER);
        this.add(westPanel, BorderLayout.WEST);
        
        //westpanel
        westPanel.add(feedScrollPanel);
        westPanel.add(btnPanel);

        //btn panel
        btnPanel.add(subscribe);
        btnPanel.add(preferences);
        
        //layouts
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        headlinePanel.setLayout(new BoxLayout(headlinePanel, BoxLayout.Y_AXIS));
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));

        subscribe.addActionListener(new SimpleViewSubscribeActionListener());
        preferences.addActionListener(new SimpleViewPreferencesActionListener());

        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // sets full screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        this.setSize(xSize, ySize);
    }

    /**
     * * used by the controller to set the headline text
     *
     * @param title: String
     * @param controller: SimpleViewController
     */
    public void setFeedText(String title, SimpleViewController controller) {
        JPanel feedPanel2 = new JPanel();
        feedContextMenu = new FeedContextMenu(controller);
        feedPanel.add(feedPanel2);
        feedLabel = new JLabel(title);
        feedPanel2.add(feedLabel);
        addController(controller);
        feedPanel.revalidate();
        feedPanel.repaint();
        this.validate();
    }

    /**
     * * used by the controller to set the text of the label
     *
     * @param headline: String
     * @param content: String
     * @param controller: ArticleController
     */
    public void setHeadlineText(String headline, String content, ArticleController controller) {
        // removeAllFromScrollPane();
        JPanel articlePanel = new JPanel();
        headlineLabel = new JLabel();
        contentLabel = new JLabel();
        addArticleController(controller);
        headlinePanel.add(articlePanel);
        articlePanel.setLayout(new BoxLayout(articlePanel, BoxLayout.Y_AXIS));
        articlePanel.add(headlineLabel, BorderLayout.NORTH);
        articlePanel.add(contentLabel, BorderLayout.SOUTH);

        articlePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        // SSystem.out.println(headline);
        headlinePanel.revalidate();
        headlinePanel.repaint();
        articlePanel.revalidate();
        articlePanel.repaint();
        headlineLabel.setText(headline);
        contentLabel.setText(content);
        this.validate();
    }

    /**
     * hides the display with the setVisible() method
     */
    public void hideDisplay() {
        this.setVisible(false);
    }

    /**
     * adds the mouse listener to the JLabel theat contains the feed
     *
     * @param conroller: SimpleViewController
     */
    public void addController(SimpleViewController conroller) {
        feedLabel.addMouseListener(conroller);
    }

    /**
     * removes all the elements from the scroll pane
     */
    public void removeAllFromScrollPane() {
        headlineScrollPane.removeAll();
    }

    /**
     * adds the mouse listener to the JLabel that contains the article title
     *
     * @param controller: ArticleController
     */
    public void addArticleController(ArticleController controller) {
        headlineLabel.addMouseListener(controller);
    }

    public void clearFeedPanel() {
        feedPanel.removeAll();
    }

    public void clearHeadlinePanel() {
        headlinePanel.removeAll();
    }

}

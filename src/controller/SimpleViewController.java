package controller;

import controller.listener.SimpleViewPreferencesActionListener;
import controller.listener.SimpleViewSubscribeActionListener;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import javax.swing.JLabel;

import gui.SimpleView;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import model.Article;
import model.Feed;
import preferences.Constants;

/**
 * Controller that deals with the simplified view of the system
 *
 * @author Sam
 *
 */
public class SimpleViewController implements MouseListener {

    public SimpleView view;
    private Set<Article> articles;
    private ArticleController articleController;
    private ScheduledExecutorService executor;
    private Set<Feed> feedsSet;
    private String labelTextID;
    private SimpleViewPreferencesActionListener preferencesAction;
    private SimpleViewSubscribeActionListener subscribeAction;

    /**
     * Creates a new view and gets the feeds from the TT-RSS server
     */
    public SimpleViewController() throws InterruptedException, UnsupportedEncodingException {
        executor = Executors.newSingleThreadScheduledExecutor();
        view = new SimpleView();
        Constants.simpleView = this;
        preferencesAction = new SimpleViewPreferencesActionListener();
        subscribeAction = new SimpleViewSubscribeActionListener();
        view.addSubscribeActionListener(subscribeAction);
        view.addPreferencesActionListener(preferencesAction);
        articleController = new ArticleController();
        Constants.feedAmount = 100;
        updateFeeds();
    }

    /**
     * Iterates through a Set of feeds to adds them to the feeds set in the
     * constants class
     *
     * @throws UnsupportedEncodingException if nothing works
     */
    public void getFeed() throws UnsupportedEncodingException, InterruptedException {
        view.clearFeedPanel();
        feedsSet = Constants.api.getFeeds();
        for (Feed d : feedsSet) {
            System.out.println(d.title);
            view.setFeedText(d.id + " " + d.title, this);
        }
    }

    public void getHeadlines() {
        view.clearHeadlinePanel();
        for (Article a : articles) {
            // System.out.println(a.title);
            view.setHeadlineText(a.title, a.content, articleController);
        }
    }

    /**
     * When label is clicked, this iterates through the feeds to find the match
     * and then gets the articles of that specific feed and outputs it to the
     * GUI
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

        if (SwingUtilities.isLeftMouseButton(e)) {
            view.clearHeadlinePanel();
            JLabel currentLabel = (JLabel) e.getComponent();
            for (Feed f : feedsSet) {
                String string = currentLabel.getText();
                labelTextID = null;
                if (string.contains(" ")) {
                    labelTextID = string.substring(0, string.indexOf(" "));
                    int labelID = Integer.parseInt(labelTextID);
                    if (f.id == labelID) {
                        try {
                            articles = Constants.api.getArticles(labelID, Constants.feedAmount, "all_articles", false, 0,
                                    null, null);
                            Constants.articles = articles;
                            updateTask();
                            // System.out.println("I am here");

                        } catch (UnsupportedEncodingException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }
            }

            for (Article a : articles) {
                // System.out.println(a.title);
                view.setHeadlineText(a.title, a.content, articleController);
            }
        }

        if (SwingUtilities.isRightMouseButton(e)) {
            JLabel currentLabel = (JLabel) e.getComponent();
            for (Feed f : feedsSet) {
                String string = currentLabel.getText();
                String labelTextID = null;
                if (string.contains(" ")) {
                    labelTextID = string.substring(0, string.indexOf(" "));
                    int labelID = Integer.parseInt(labelTextID);
                    if (f.id == labelID) {
                        try {
                            Constants.api.feedUnsubscribe(labelID);
                            getFeed();
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(SimpleViewController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SimpleViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * update thread that runs every x minutes and only runs if user defines the
     * program to be online
     */
    private void updateTask() {
        if (Constants.isOnline) {
            Runnable getFeedTask = new Runnable() {
                public void run() {
                    try {
                        // Invoke method(s) to do the work
                        int labelID = Integer.parseInt(labelTextID);
                        System.out.println(labelID);
                        articles = Constants.api.getArticles(labelID, Constants.feedAmount, "all_articles", false, 0,
                                null, null);
                        getHeadlines();
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(SimpleViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            executor.scheduleAtFixedRate(getFeedTask, 0, Constants.updateArticleTime, TimeUnit.SECONDS);
        }
    }

    private void updateFeeds() {
        if (Constants.isOnline) {
            Runnable getFeedTask = new Runnable() {
                public void run() {
                    try {
                        // Invoke method(s) to do the work
                        feedsSet = Constants.api.getFeeds();
                        getFeed();
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(SimpleViewController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SimpleViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            System.out.println(Constants.updateFeedTime);
            executor.scheduleAtFixedRate(getFeedTask, 0, Constants.updateFeedTime, TimeUnit.SECONDS);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        JLabel currentLabel = (JLabel) e.getComponent();
        currentLabel.setForeground(Color.BLUE);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        JLabel currentLabel = (JLabel) e.getComponent();
        currentLabel.setForeground(null);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}

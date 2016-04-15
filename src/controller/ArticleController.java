package controller;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

import javax.swing.JLabel;

import model.Article;
import preferences.Constants;

/**
 * Controller that deals with the mouse listeners for the articles
 * 
 * @author Sam
 *
 */
public class ArticleController implements MouseListener {

	private Set<Article> articles;

	public ArticleController() {

	}

	/**
	 * When a mouse is cliced the url of the article is opened in the users
	 * default browser
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLabel currentLabel = (JLabel) arg0.getComponent();
		for (Article a : Constants.articles) {
			if (currentLabel.getText() == a.title) {
				try {
					Desktop.getDesktop().browse(URI.create(a.url));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLabel currentLabel = (JLabel) arg0.getComponent();
		currentLabel.setForeground(Color.BLUE);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLabel currentLabel = (JLabel) arg0.getComponent();
		currentLabel.setForeground(null);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}

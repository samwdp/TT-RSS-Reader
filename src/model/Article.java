package model;

import java.util.Date;
import java.util.Set;

/**
 * Holds all the information that gois into an article so that it can be
 * displayed in the GUI
 * 
 * @author Sam
 *
 */
public class Article implements Comparable<Article> {

	public int id;
	public String title;
	public int feedId;
	public volatile boolean isUnread;
	public String url;
	public String commentUrl;
	public Date updated;
	public String content;
	public Set<String> attachments;
	public boolean isStarred;
	public boolean isPublished;
	public Set<Label> labels;
	public String author;
	public String feedTitle;
	public String note;

	@Override
	public int compareTo(Article ai) {
		return ai.updated.compareTo(this.updated);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof Article) {
			Article ac = (Article) o;
			return id == ac.id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id;
	}

	public enum ArticleField {
		id, title, unread, updated, feed_id, content, link, comments, attachments, marked, published, labels, is_updated, tags, feed_title, comments_count, comments_link, always_display_attachments, author, score, lang, note
	}

}
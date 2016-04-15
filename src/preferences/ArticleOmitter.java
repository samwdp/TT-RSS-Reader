package preferences;

import java.util.Set;

import model.Article;
import model.Article.ArticleField;

/**
 * Checks to make sure an article can be ommitted
 * 
 * @author Sam
 *
 */
public interface ArticleOmitter {

	/**
	 * this method should return {@code true} if given article should not be
	 * processed
	 *
	 * @param field
	 *            current article field added to article on this iteration
	 * @param a
	 *            article to test
	 * @return {@code true} if given article should be omitted, {@code false}
	 *         otherwise
	 */
	boolean omitArticle(Article.ArticleField field, Article a);

	/**
	 * Returns a list of articles that have been ignored in the last run.
	 *
	 * @return a list of article ids.
	 */
	Set<Integer> getOmittedArticles();

}

package cetli.utils;

import cetli.Database;
import cetli.framework.Article;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CopyData {

    private Database database1;
    private Database database2;
    private List<Article> allArticles = new ArrayList<>();
    private Set<Integer> copiedArticleIds = new HashSet<>();
    private Map<Integer, Integer> oldNewIds = new HashMap<>();

    private void run() throws SQLException {
        database1 = new Database("myarticles");
        database2 = new Database("cetli-szakdoga-sg");
        database2.clearTables();
        database2.createTables();

        List<Integer> allIds = database1.search("");
        for (int id : allIds) {
            allArticles.add(database1.getArticle(id));
        }
        copyFamily("Pszionikus képességek");
        copyFamily("Pszionikus hatásjellegek");
        copyFamily("Létsíkok");
        copyFamily("Devotions (basic ablities)");
        copyFamily("Sciences (superior abilities)");
        copyFamily("Könyvek");

    }

    private void copyFamily(String title) {
        Article startingArticle = null;
        for (Article article : allArticles) {
            if (article.getTitle().equals(title)) {
                startingArticle = article;
            }
        }
        if (startingArticle == null) {
            throw new RuntimeException("" + title + " not found.");
        }

        recursiveCopyFamily(startingArticle.getId(), null);
    }

    private void recursiveCopyFamily(Integer startingArticleId, Integer parentId) {
        
        if (database1.getArticle(startingArticleId).getTitle().equals("Sciences (superior abilities)")) {
            System.out.println("helo");
        }
        

        if (!copiedArticleIds.contains(startingArticleId)) {
            if (parentId != null) {
                database2.addRelationship(parentId, oldNewIds.get(startingArticleId));
            }
            return;
        }
        Article startingArticle = database1.getArticle(startingArticleId);
        int id = database2.newArticle(startingArticle.getTitle());
        
        oldNewIds.put(startingArticleId, id);
        Article article = new Article(id, startingArticle.getTitle(), startingArticle.getContent(), startingArticle.getUri());
        database2.updateArticle(article);
        copiedArticleIds.add(startingArticleId);
        if (parentId != null) {
            database2.addRelationship(parentId, id);
        }

        List<Integer> childrenIds = new ArrayList<>(database1.getChildren(startingArticleId));
        for (Integer childId : childrenIds) {
            recursiveCopyFamily(childId, id);
        }
    }

    public static void main(String[] args) throws SQLException {
        CopyData copyData = new CopyData();
        copyData.run();
    }
}

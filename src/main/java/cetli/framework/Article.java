package cetli.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import static cetli.Database.database;

public final class Article implements GraphNode<Article> {
    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleStringProperty title = new SimpleStringProperty();
    private final SimpleStringProperty content = new SimpleStringProperty();
    private final SimpleStringProperty uri = new SimpleStringProperty();
    
    private static Map<Integer, Article> articleCache = new HashMap<>();
    private static Map<Integer, Set<Article>> parentsCache = new HashMap<>();
    private static Map<Integer, Set<Article>> childrenCache = new HashMap<>();
    
    public Article() {}
        
    public static Article forId(int id) {
        if (articleCache.containsKey(id)) {
            return articleCache.get(id);
        } else {
            Article article = database.getArticle(id);
            articleCache.put(id, article);
            return article;
        }
    }
    
    public static Article newArticle(String title) {
        int id = database.newArticle(title);
        Article article = new Article(id, title, "", "");
        articleCache.put(id, article);
        return article;
    }    
    
    public Article(int id, String title, String content, String uri) {
        setId(id);
        setTitle(title);
        setContent(content);
        setUri(uri);        
    }
    
    public int getId() {
        return id.get();
    }
    
    public void setId(int id) {
        this.id.set(id);
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public SimpleStringProperty getTitleProperty() {
        return title;
    }
    
    public String getContent() {
        return content.get();
    }
    
    public void setContent(String content) {
        this.content.set(content);
    }
    
    public SimpleStringProperty getContentProperty() {
        return content;
    }
    
    public String getUri() {
        return uri.get();
    }
    
    public void setUri(String uri) {
        this.uri.set(uri);
    }
    
    public Set<Article> getParents() {
        if (parentsCache.containsKey(getId())) return new HashSet<>(parentsCache.get(getId()));
        Set<Article> ret = new HashSet<>();
        for (int id: database.getParents(getId())) {
            ret.add(Article.forId(id));
        }
        parentsCache.put(getId(), ret);
        return new HashSet<>(ret);
    }
    
    public Set<Article> getChildren() {
        if (childrenCache.containsKey(getId())) return new HashSet<>(childrenCache.get(getId()));
        Set<Article> ret = new HashSet<>();
        for (int id: database.getChildren(getId())) {
            ret.add(Article.forId(id));
        }
        childrenCache.put(getId(), ret);
        return new HashSet<>(ret);
    }
    
    public void addParent(Article parent) {
        if (parentsCache.containsKey(this.getId())) {
            parentsCache.get(this.getId()).add(parent);
        }
        if (childrenCache.containsKey(parent.getId())) {
            childrenCache.get(parent.getId()).add(this);
        }     
        database.addRelationship(parent.getId(), this.getId());
    }
    
    public void addChild(Article child) {
        if (childrenCache.containsKey(this.getId())) {
            childrenCache.get(this.getId()).add(child);
        }
        
        if (parentsCache.containsKey(child.getId())) {
            parentsCache.get(child.getId()).add(this);
        }
        database.addRelationship(this.getId(), child.getId());
    }
    
    public void removeParent(Article parent) {
        if (parentsCache.containsKey(this.getId())) {
            parentsCache.get(this.getId()).remove(parent);
        }
        if (childrenCache.containsKey(parent.getId())) {
            childrenCache.get(parent.getId()).remove(this);
        }
        database.removeRelationship(parent.getId(), this.getId());
    }
    
    public void removeChild(Article child) {
        if (childrenCache.containsKey(this.getId())) {
            childrenCache.get(this.getId()).remove(child);
        }
        
        if (parentsCache.containsKey(child.getId())) {
            parentsCache.get(child.getId()).remove(this);
        }
        database.removeRelationship(this.getId(), child.getId());
    }
    
    public void delete() {
        List<Article> parents = new ArrayList(getParents());
        for (Article parent : parents) {
            removeParent(parent);
        }
        
        List<Article> children = new ArrayList(getChildren());
        for (Article child : children) {
            removeChild(child);
        }
        
        articleCache.remove(this.getId());
        database.removeArticle(this.getId());
    }
            
    
    public void save() {
        database.updateArticle(this);
    }
    
    @Override
    public String toString() {
        return getTitle();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Article)) return false;
        Article other = (Article)o;
        return this.getId() == other.getId(); // change to .equals if id is changed to Integer
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.getId());
        return hash;
    }
}

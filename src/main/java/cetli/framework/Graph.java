package cetli.framework;

import java.util.HashSet;
import java.util.Set;


public class Graph <T extends GraphNode> {
    
    private T focusGraphNode;
    private Graph<T> prevGraph;
    public Set<T> articles = null;
    private Set<T> alternativeOrphans = null;
    
    private Graph(){}
    
    public Graph(Graph prevGraph) {
        this.prevGraph = prevGraph;
    }
    
    public Set<T> alternativeOrphans() {
        //if (alternativeOrphans != null) return alternativeOrphans;
        Set<T> ret = new HashSet<>();
        ret.addAll(cumulativeIntersectionOfDescendantsAndTheirAncestors());
        ret.removeAll(cumulativeUnionOfStraightFamilies());
        ret = findInnerOrphans(ret);
        alternativeOrphans = ret;
        return ret;
    }
    
    protected Set<T> cumulativeUnionOfStraightFamilies() {
        Set<T> ret = new HashSet<>();
        ret.addAll(prevGraph.cumulativeUnionOfStraightFamilies());
        ret.addAll(straightFamily());
        return ret;
    }
    
    protected Set<T> straightFamily() {
        Set<T> ret = new HashSet<>();
        ret.add(focusGraphNode);
        ret.addAll(ancestors(focusGraphNode));
        ret.addAll(descendants(focusGraphNode));
        return ret;
    }
    
    public Set<T> getParents(T article) {
        Set<T> ret = new HashSet<>();
        ret.addAll(article.getParents());
        ret.retainAll(articles());
        return ret;
    }
    
    public Set<T> getChildren(T article) {
        Set<T> ret = new HashSet<>();
        ret.addAll(article.getChildren());
        ret.retainAll(articles());
        return ret;
    }
    
    private Set<T> articles() {
        if (articles != null) return articles;
        Set<T> ret = new HashSet<>();
        for (T orphan: prevGraph.alternativeOrphans) {
            ret.add(orphan);
            ret.addAll(prevGraph.descendants(orphan));
        }
        ret.retainAll(prevGraph.
                cumulativeIntersectionOfDescendantsAndTheirAncestors());
        articles = ret;
        return articles;
    }
    
    protected Set<T> cumulativeIntersectionOfDescendantsAndTheirAncestors() {
        Set<T> ret = new HashSet<>();
        ret.addAll(prevGraph.cumulativeIntersectionOfDescendantsAndTheirAncestors());
        ret.retainAll(descendantsAndTheirAncestors());
        return ret;
    }
    
    private Set<T> ancestors(T a) {
        return collectAncestors(new HashSet<T>(), a);
    }
    
    private Set<T> collectAncestors(Set<T> collected, T article) {
        Set<T> parents = getParents(article);
        if (parents.isEmpty()) return collected;
        for (T p: parents) {
            if (collected.contains(p)) continue;
            collected.add(p);
            collected.addAll(collectAncestors(collected, p));
        }
        return collected;
    }
    
    private Set<T> descendants(T a) {
        return collectDescendants(new HashSet<T>(), a);
    }
    
    private Set<T> collectDescendants(Set<T> collected, T article) {
        Set<T> children = getChildren(article);
        if (children.isEmpty()) return collected;
        for (T p: children) {
            if (collected.contains(p)) continue;
            collected.add(p);
            collected.addAll(collectDescendants(collected, p));
        }
        return collected;
    }
    
    protected Set<T> descendantsAndTheirAncestors() {
        Set<T> ret = new HashSet<>();
        for (T d: descendants(focusGraphNode)) {
            ret.add(d);
            ret.addAll(ancestors(d));
        }
        return ret;
    }
    
    //Articles that have no parents among the argument set.
    private Set<T> findInnerOrphans(Set<T> all) {
        Set<T> ret = new HashSet<>();
        for (T a: all) {
            Set<T> parents = a.getParents();
            parents.retainAll(all);
            if (parents.isEmpty()) ret.add(a);
        }
        return ret;
    }
    
    public void setFocusGraphNode(T focusGraphNode) {
        this.focusGraphNode = focusGraphNode;
    }
    
    public static <T extends GraphNode> Graph<T> defaultGraph() {
        return new Graph<T>() {
            
            @Override
            protected Set<T> cumulativeIntersectionOfDescendantsAndTheirAncestors() {
                return descendantsAndTheirAncestors();
            }
            
            @Override
            protected Set<T> cumulativeUnionOfStraightFamilies() {
                return straightFamily();
            }
            
            @Override
            public Set<T> getChildren(T a) {
                return a.getChildren();
            }
            
            @Override
            public Set<T> getParents(T a) {
                return a.getParents();
            }
        };
    }
}

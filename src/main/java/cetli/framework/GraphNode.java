package cetli.framework;

import java.util.Set;

interface GraphNode <T extends GraphNode<T>> {
    Set<T> getParents();
    Set<T> getChildren();
}

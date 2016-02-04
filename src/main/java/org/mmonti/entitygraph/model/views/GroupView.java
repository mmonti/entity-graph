package org.mmonti.entitygraph.model.views;

import org.mmonti.entitygraph.model.Policy;

import java.util.Set;

/**
 * Created by mmonti on 2/4/16.
 */
public interface GroupView {

    String getId();
    String getName();
    String getDescription();
    Set<Policy> getPolicies();

}

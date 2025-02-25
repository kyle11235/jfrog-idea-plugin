package com.jfrog.ide.idea.ui.filters.filtermanager;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;
import com.jfrog.ide.idea.events.ApplicationEvents;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jfrog.build.extractor.scan.DependencyTree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Vector;

/**
 * @author yahavi
 */
@State(name = "CiFilterState")
public class CiFilterManager extends ConsistentFilterManager {

    public CiFilterManager(Project mainProject) {
        super(mainProject);
    }

    public static CiFilterManager getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, CiFilterManager.class);
    }

    public void collectBuildsInformation(DependencyTree root) {
        clearBuilds();
        root.getChildren().stream()
                .map(DefaultMutableTreeNode::getUserObject)
                .map(Object::toString)
                .forEach(this::addBuild);
    }

    @Override
    public Topic<ApplicationEvents> getSyncEvent() {
        return ApplicationEvents.ON_CI_FILTER_CHANGE;
    }
}

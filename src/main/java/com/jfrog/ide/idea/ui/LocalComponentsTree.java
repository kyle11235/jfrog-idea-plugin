package com.jfrog.ide.idea.ui;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import com.jfrog.ide.common.filter.FilterManager;
import com.jfrog.ide.common.utils.ProjectsMap;
import com.jfrog.ide.idea.events.ProjectEvents;
import com.jfrog.ide.idea.scan.ScanManagersFactory;
import com.jfrog.ide.idea.ui.filters.filtermanager.LocalFilterManager;
import org.jetbrains.annotations.NotNull;
import org.jfrog.build.extractor.scan.DependencyTree;

/**
 * @author yahavi
 */
public class LocalComponentsTree extends ComponentsTree {
    public LocalComponentsTree(@NotNull Project mainProject) {
        super(mainProject);
    }

    public static LocalComponentsTree getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, LocalComponentsTree.class);
    }

    @Override
    public void addOnProjectChangeListener(MessageBusConnection busConnection) {
        busConnection.subscribe(ProjectEvents.ON_SCAN_PROJECT_CHANGE, this::applyFilters);
    }

    public void applyFilters(ProjectsMap.ProjectKey projectKey) {
        DependencyTree project = projects.get(projectKey);
        if (project == null) {
            return;
        }
        FilterManager filterManager = LocalFilterManager.getInstance(mainProject);
        DependencyTree filteredRoot = filterManager.applyFilters(project);
        filteredRoot.setIssues(filteredRoot.processTreeIssues());
        appendProjectWhenReady(filteredRoot);
        DumbService.getInstance(mainProject).smartInvokeLater(() -> ScanManagersFactory.getInstance(mainProject).runInspectionsForAllScanManagers());
    }

}

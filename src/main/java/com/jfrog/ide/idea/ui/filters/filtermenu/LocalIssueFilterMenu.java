package com.jfrog.ide.idea.ui.filters.filtermenu;

import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;
import com.jfrog.ide.idea.events.ApplicationEvents;
import com.jfrog.ide.idea.ui.filters.filtermanager.LocalFilterManager;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Yahav Itzhak on 22 Nov 2017.
 */
public class LocalIssueFilterMenu extends IssueFilterMenu {

    public LocalIssueFilterMenu(@NotNull Project mainProject) {
        super(mainProject, LocalFilterManager.getInstance(mainProject));
    }

    @Override
    public Topic<ApplicationEvents> getSyncEvent() {
        return ApplicationEvents.ON_SCAN_FILTER_CHANGE;
    }
}
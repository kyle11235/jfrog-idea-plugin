package com.jfrog.ide.idea.ui.filters.builds;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.ComboboxSpeedSearch;
import com.jfrog.ide.idea.ui.filters.filtermanager.CiFilterManager;

/**
 * Created by Yahav Itzhak on 22 Nov 2017.
 */
public class BuildsMenu extends JBPopupMenu {

    private final ComboboxSpeedSearch buildsButton;
    private final Project mainProject;

    public BuildsMenu(Project mainProject) {
        this.mainProject = mainProject;
        this.buildsButton = new ComboboxSpeedSearch(new BuildsButton(mainProject));
    }

    public void refresh() {
        BuildsButton buildsButton = getBuildButton();

        // Remove all builds
        buildsButton.removeOnSelectBuildListener();
        buildsButton.removeAllItems();

        // Add builds from to the collected builds information in the last builds scan
        CiFilterManager.getInstance(mainProject).getSelectableBuilds().forEach(build -> {
            String item = build.getKey();
            buildsButton.addItem(item);
            if (build.getValue()) {
                buildsButton.setSelectedItem(item);
            }
        });
        buildsButton.setOnSelectBuildListener();
    }

    public BuildsButton getBuildButton() {
        return (BuildsButton) buildsButton.getComponent();
    }

}
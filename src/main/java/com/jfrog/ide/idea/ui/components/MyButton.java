package com.jfrog.ide.idea.ui.components;

import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBLabel;
import com.jfrog.ide.common.configuration.ServerConfig;
import com.jfrog.ide.idea.configuration.GlobalSettings;
import com.jfrog.ide.idea.log.Logger;
import com.jfrog.ide.idea.ui.AbstractJFrogToolWindow;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.ArtifactoryRequest;
import org.jfrog.artifactory.client.ArtifactoryResponse;
import org.jfrog.artifactory.client.impl.ArtifactoryRequestImpl;
import org.jfrog.build.extractor.scan.DependencyTree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jfrog.ide.idea.ui.configuration.Utils.setActiveForegroundColor;
import static com.jfrog.ide.idea.ui.configuration.Utils.setInactiveForegroundColor;

/**
 * @author yahavi
 **/
public class MyButton extends JBLabel {
    private MouseListener mouseListener;

    // kyle
    public static String GENERIC_REPO = "xray-project-generic-local";
    public static String WHITELIST_REQUEST_PATH = "whitelist_request";
    private ServerConfig config;
    private Artifactory artifactory;
    private Logger logger = Logger.getInstance();

    public MyButton(String text) {
        setToolTipText(text);
        setText(text);
        setInactiveForegroundColor(this);
    }

    public void init(AbstractJFrogToolWindow window) {

        config = GlobalSettings.getInstance().getServerConfig();
        artifactory = ArtifactoryClientBuilder.create()
                .setUrl(config.getArtifactoryUrl())
                .setUsername(config.getUsername())
                .setPassword(config.getPassword())
                .addInterceptorLast((request, httpContext) -> {
                    logger.info("Artifactory request: " + request.getRequestLine());
                })
                .build();

        removeMouseListener(mouseListener);
//        if (isBlank(link)) {
//            setIcon(null);
//            setText("");
//            return;
//        }
        setIcon(AllIcons.Ide.Gift);

        mouseListener = new BuildLogMouseAdapter(window);
        addMouseListener(mouseListener);
    }

    private class BuildLogMouseAdapter extends MouseAdapter {
        private final AbstractJFrogToolWindow window;

        private BuildLogMouseAdapter(AbstractJFrogToolWindow window) {
            this.window = window;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
//            BrowserUtil.browse(link);
            logger.info("my button clicked");
            setText(getToolTipText());

//            String pattern = "yyyy_MM_dd_HH_mm_ssZ";
            String pattern = "yyyy_MM_dd_HH_mm_ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String filePath = simpleDateFormat.format(new Date()) + "_" + config.getUsername();

            Boolean res = apiCreateFile(filePath);
            if(res){
                apiAddProperties(filePath);
                setText(getToolTipText() + " (Success)");
            } else {
                setText(getToolTipText() + " (Fail)");
            }

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setActiveForegroundColor(MyButton.this);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setInactiveForegroundColor(MyButton.this);
        }

        public Boolean apiCreateFile(String filePath){
            logger.info("apiCreateFile");
            try {
                // bug, only create folder
                // artifactory.repository(GENERIC_REPO).file(filePath).create();

                // use rest api directly
                ArtifactoryRequest repositoryRequest = new ArtifactoryRequestImpl().apiUrl(GENERIC_REPO + "/" + WHITELIST_REQUEST_PATH + "/" + filePath)
                        .method(ArtifactoryRequest.Method.PUT)
                        .responseType(ArtifactoryRequest.ContentType.JSON);
                ArtifactoryResponse response = null;
                response = artifactory.restCall(repositoryRequest);
                logger.info("api success=" + response.isSuccessResponse());
                // Get the response raw body
                String rawBody = response.getRawBody();
                logger.info("api rawBody=" + rawBody);

            } catch (IOException ioException) {
                ioException.printStackTrace();
                logger.error(ioException.getMessage());
                return false;
            }
            return true;
        }

        public void apiAddProperties(String filePath){
            logger.info("apiAddProperties");

            Map<String, String> properties = new HashMap<String, String>();

            List<DependencyTree> selectedNodes = window.getSelectedNodes();
            logger.info("selected size=" + selectedNodes.size());
            for(DependencyTree node : selectedNodes){
                String componentId = node.toString();
                logger.info("selected node=" + componentId);
                String key = componentId.replace(":", ".").replace("-","_");
                properties.put(key, componentId);
                this.extractComponents(node, properties);
            }

            artifactory.repository(GENERIC_REPO).file(WHITELIST_REQUEST_PATH + "/" + filePath).properties().addProperties(properties).doSet();
        }

        private void extractComponents(DependencyTree node, Map<String, String> properties) {
            for (DependencyTree child : node.getChildren()) {
                String componentId = child.toString();
                logger.info("child componentId=" + componentId);
                String key = componentId.replace(":", ".").replace("-","_");
                properties.put(key, componentId);
                extractComponents(child, properties);
            }
        }

        public void apiTest(){
            logger.info("apiTest");

            ArtifactoryRequest repositoryRequest = new ArtifactoryRequestImpl().apiUrl("api/repositories")
                    .method(ArtifactoryRequest.Method.GET)
                    .responseType(ArtifactoryRequest.ContentType.JSON);
            ArtifactoryResponse response = null;
            try {

                response = artifactory.restCall(repositoryRequest);
                logger.info("api success=" + response.isSuccessResponse());
                // Get the response raw body
                String rawBody = response.getRawBody();
                logger.info("api rawBody=" + rawBody);

            } catch (IOException ioException) {
                ioException.printStackTrace();
                logger.error(ioException.getMessage());
            }
        }
    }


}

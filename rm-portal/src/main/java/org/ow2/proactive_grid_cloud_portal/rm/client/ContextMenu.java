/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.ow2.proactive_grid_cloud_portal.rm.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ow2.proactive_grid_cloud_portal.common.client.Images;
import org.ow2.proactive_grid_cloud_portal.common.client.model.LogModel;
import org.ow2.proactive_grid_cloud_portal.common.client.model.LoginModel;
import org.ow2.proactive_grid_cloud_portal.rm.client.nodesource.edition.EditDynamicParametersWindow;
import org.ow2.proactive_grid_cloud_portal.rm.client.nodesource.edition.EditNodeSourceWindow;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.tree.Tree;


public class ContextMenu extends Menu {

    private String lockItemImageResource = RMImages.instance.node_add_16_locked().getSafeUri().asString();

    private String unlockItemImageResource = RMImages.instance.node_add_16().getSafeUri().asString();

    private String deployItemImageResource = RMImages.instance.nodesource_deployed().getSafeUri().asString();

    private String undeployItemImageResource = RMImages.instance.nodesource_undeployed().getSafeUri().asString();

    private String redeployItemImageResource = RMImages.instance.nodesource_redeploy().getSafeUri().asString();

    private String editItemImageResource = RMImages.instance.nodesource_edit().getSafeUri().asString();

    private String exportItemImageResource = RMImages.instance.nodesource_deployed().getSafeUri().asString();

    private NodeSource nodesource;

    private NodeSource.Host host;

    private NodeSource.Host.Node node;

    private MenuItem lockItem;

    private MenuItem unlockItem;

    private MenuItem removeItem;

    private MenuItem deployItem;

    private MenuItem undeployItem;

    private MenuItem redeployItem;

    private MenuItem editItem;

    private MenuItem exportNodeSourceItem;

    private MenuItem exportPolicyItem;

    private MenuItem exportInfrastructureItem;

    public static Menu createContextMenuFromTreeView(RMController controller, Object related, Tree tree) {

        MenuItem expandItem = new MenuItem("Expand all", Images.instance.expand_16().getSafeUri().asString());
        expandItem.addClickHandler(event17 -> tree.openAll());

        MenuItem collapseItem = new MenuItem("Collapse all", Images.instance.close_16().getSafeUri().asString());
        collapseItem.addClickHandler(event16 -> tree.closeAll());

        final Menu menu = createContextMenuFromCompactView(controller, related);

        final List<MenuItem> menuItems = new ArrayList<>();

        menuItems.add(expandItem);
        menuItems.add(collapseItem);
        menuItems.add(new MenuItemSeparator());
        menuItems.addAll(Arrays.asList(menu.getItems()));

        final MenuItem[] newMenuItems = menuItems.toArray(new MenuItem[menuItems.size()]);

        menu.setItems(newMenuItems);

        return menu;
    }

    public static Menu createContextMenuFromCompactView(RMController controller, Object related) {
        ContextMenu menu = new ContextMenu();

        menu.init(related);

        if (menu.node != null) {
            controller.selectNode(menu.node);
            menu.lockItemImageResource = menu.node.getIconLocked();
            menu.unlockItemImageResource = menu.node.getIconUnlocked();
        } else if (menu.host != null) {
            controller.selectHost(menu.host);
        } else if (menu.nodesource != null) {
            controller.selectNodeSource(menu.nodesource);
        }

        menu.setShowShadow(true);
        menu.setShadowDepth(10);

        CopyMenu copyMenu = new CopyMenu(menu).build();
        MenuItem copyItem = copyMenu.getCopyItem();

        LockMenu lockMenu = new LockMenu(controller, menu).build();
        menu.lockItem = lockMenu.getLockItem();
        menu.unlockItem = lockMenu.getUnlockItem();
        menu.removeItem = lockMenu.getRemoveItem();

        String nodeSourceName = menu.nodesource == null ? "" : menu.nodesource.getSourceName();

        NodeSourceMenu nodeSourceMenu = new NodeSourceMenu(controller, menu, nodeSourceName).build();
        menu.editItem = nodeSourceMenu.getEditItem();
        menu.undeployItem = nodeSourceMenu.getUndeployItem();
        menu.deployItem = nodeSourceMenu.getDeployItem();
        menu.redeployItem = nodeSourceMenu.getRedeployItem();

        ExportMenu exportMenu = new ExportMenu(controller, menu, nodeSourceName).build();
        menu.exportNodeSourceItem = exportMenu.getExportNodeSourceItem();
        menu.exportInfrastructureItem = exportMenu.getExportInfrastructureItem();
        menu.exportPolicyItem = exportMenu.getExportPolicyItem();

        MenuItem editTokens = new MenuItem("Edit Tokens");
        editTokens.addClickHandler(x -> {
            if (menu.node != null) {
                EditNodeTokens editNodeTokens = new EditNodeTokens(controller, menu.node);
                editNodeTokens.show();
            }
        });

        if (menu.node != null) {
            copyItem.setEnabled(true);
            if (menu.node.isLocked()) {
                menu.lockItem.setEnabled(false);
                menu.unlockItem.setEnabled(true);
            } else {
                menu.lockItem.setEnabled(true);
                menu.unlockItem.setEnabled(false);
            }
        } else {
            editTokens.setEnabled(false);
            copyItem.setEnabled(false);
        }

        if (menu.nodesource != null) {
            switch (menu.nodesource.getNodeSourceStatus()) {
                case NODES_DEPLOYED:
                    menu.editItem.setTitle(EditDynamicParametersWindow.WINDOW_TITLE);
                    menu.enableItems(menu.undeployItem);
                    menu.enableItems(menu.redeployItem);
                    menu.disableItems(menu.deployItem);
                    break;
                case NODES_UNDEPLOYED:
                    menu.editItem.setTitle(EditNodeSourceWindow.WINDOW_TITLE);
                    menu.enableItems(menu.deployItem);
                    menu.disableItems(menu.undeployItem);
                    menu.disableItems(menu.redeployItem);
                    break;
                default:
                    menu.disableItems(menu.deployItem, menu.undeployItem, menu.redeployItem, menu.editItem);
            }
        } else {
            menu.disableItems(menu.deployItem,
                              menu.undeployItem,
                              menu.redeployItem,
                              menu.editItem,
                              menu.exportNodeSourceItem,
                              menu.exportInfrastructureItem,
                              menu.exportPolicyItem);
        }

        List<MenuItem> items = new ArrayList<>();
        if (menu.node != null) {
            items.add(copyItem);
            items.add(new MenuItemSeparator());
        }

        if (menu.nodesource != null) {
            items.add(menu.deployItem);
            items.add(menu.undeployItem);
            items.add(menu.redeployItem);
            items.add(menu.editItem);
            items.add(new MenuItemSeparator());
        }

        items.add(menu.lockItem);
        items.add(menu.unlockItem);
        items.add(menu.removeItem);

        if (menu.nodesource != null) {
            items.add(new MenuItemSeparator());
            items.add(menu.exportNodeSourceItem);
            items.add(menu.exportInfrastructureItem);
            items.add(menu.exportPolicyItem);

        }

        if (menu.node != null) {
            items.add(new MenuItemSeparator());
            items.add(editTokens);
        }

        controller.checkPermissionOfContextMenuItems(menu);
        menu.setItems(items.toArray(new MenuItem[0]));

        return menu;
    }

    public void disableProviderItems(ContextMenu menu, String url) {
        LoginModel loginModel = LoginModel.getInstance();
        if (!loginModel.userHasProviderPermissionForNode(url)) {
            disableLockMenuItem(menu);
            disableUnlockMenuItem(menu);
            disableRemoveMenuItem(menu);
        } else {
            if (!loginModel.userHasPermissionToLockNodes()) {
                disableLockMenuItem(menu);
            }
            if (!loginModel.userHasPermissionToUnLockNodes()) {
                disableUnlockMenuItem(menu);
            }
            if (!hasMethodPermissionToRemoveItem(menu)) {
                disableRemoveMenuItem(menu);
            }
        }
    }

    public void disableAdminItems(ContextMenu menu, String url) {
        LoginModel loginModel = LoginModel.getInstance();
        if (!loginModel.userHasAdminPermissionForNodeSource(url)) {
            disableDeployItem(menu);
            disableUndeployItem(menu);
            disableRedeployItem(menu);
            disableEditItem(menu);
            disableRemoveMenuItem(menu);
        } else {
            if (!loginModel.userHasPermissionToDeployNodeSource()) {
                disableDeployItem(menu);
                disableRedeployItem(menu);
            }
            if (!loginModel.userHasPermissionToUndeployNodeSource()) {
                disableUndeployItem(menu);
                disableRedeployItem(menu);
            }
            if (!hasMethodPermissionToEditItem(menu.getNodesource())) {
                disableEditItem(menu);
            }
            if (!loginModel.userHasPermissionToRemoveNodeSource()) {
                disableRemoveMenuItem(menu);
            }
        }
    }

    private boolean hasMethodPermissionToRemoveItem(ContextMenu contextMenu) {
        LoginModel loginModel = LoginModel.getInstance();
        if (contextMenu.getNodesource() == null) {
            return loginModel.userHasPermissionToRemoveNode();
        } else {
            return loginModel.userHasPermissionToRemoveNodeSource();
        }
    }

    /**
     * Checks the method permissions for edit/update button
     * @param selectedNodeSource the current selected nodeSource
     */
    private boolean hasMethodPermissionToEditItem(NodeSource selectedNodeSource) {
        LoginModel loginModel = LoginModel.getInstance();
        if (selectedNodeSource != null && selectedNodeSource.getNodeSourceStatus() == NodeSourceStatus.NODES_DEPLOYED) {
            return loginModel.userHasPermissionToUpdateDynamicParameters() &&
                   loginModel.userHasPermissionToGetInfrasToPoliciesMapping() &&
                   loginModel.userHasPermissionToGetSupportedNodeSourceInfras();
        } else if (selectedNodeSource != null &&
                   selectedNodeSource.getNodeSourceStatus() == NodeSourceStatus.NODES_UNDEPLOYED) {
            return loginModel.userHasPermissionToEditNodeSource() &&
                   loginModel.userHasPermissionToGetInfrasToPoliciesMapping() &&
                   loginModel.userHasPermissionToGetSupportedNodeSourceInfras();
        }
        return true;
    }

    public void disableLockMenuItem(ContextMenu menu) {
        menu.lockItem.setEnabled(false);
        menu.redraw();
    }

    public void disableUnlockMenuItem(ContextMenu menu) {
        menu.unlockItem.setEnabled(false);
        menu.redraw();
    }

    public void disableRemoveMenuItem(ContextMenu menu) {
        menu.removeItem.setEnabled(false);
        menu.redraw();
    }

    public void disableDeployItem(ContextMenu menu) {
        menu.deployItem.setEnabled(false);
        menu.redraw();
    }

    public void disableUndeployItem(ContextMenu menu) {
        menu.undeployItem.setEnabled(false);
        menu.redraw();
    }

    public void disableRedeployItem(ContextMenu menu) {
        menu.redeployItem.setEnabled(false);
        menu.redraw();
    }

    public void disableEditItem(ContextMenu menu) {
        menu.editItem.setEnabled(false);
        menu.redraw();
    }

    public void disableExportNodeSourceItem(ContextMenu menu) {
        menu.exportNodeSourceItem.setEnabled(false);
        menu.redraw();
    }

    public void disableExportPolicyItem(ContextMenu menu) {
        menu.exportPolicyItem.setEnabled(false);
        menu.redraw();
    }

    public void disableExportInfrastructureItem(ContextMenu menu) {
        menu.exportInfrastructureItem.setEnabled(false);
        menu.redraw();
    }

    public NodeSource getNodesource() {
        return nodesource;
    }

    private void init(Object related) {
        if (related instanceof NodeSource) {
            nodesource = (NodeSource) related;
        } else if (related instanceof NodeSource.Host) {
            host = (NodeSource.Host) related;
        } else if (related instanceof NodeSource.Host.Node) {
            node = (NodeSource.Host.Node) related;
        } else {
            LogModel.getInstance().logCriticalMessage("Cannot show context menu, related object is wrong type: " +
                                                      related.getClass().getCanonicalName());
            throw new RuntimeException("Cannot show context menu, related object is wrong type: " +
                                       related.getClass().getCanonicalName());
        }
    }

    private void disableItems(MenuItem... items) {
        for (MenuItem item : items) {
            item.setEnabled(false);
        }
    }

    private void enableItems(MenuItem... items) {
        for (MenuItem item : items) {
            item.setEnabled(true);
        }
    }

    /*
     * The only way to copy to clipboard in GWT is to do it through native Javascript.
     */
    public static native void copyToClipboard(String nodeUrl) /*-{
                                                              var el = $doc.createElement('textarea');
                                                              el.value = nodeUrl;
                                                              $doc.body.appendChild(el);
                                                              el.select();
                                                              $doc.execCommand('copy');
                                                              $doc.body.removeChild(el);
                                                              }-*/;

    private static class CopyMenu {

        private ContextMenu menu;

        private MenuItem copyItem;

        private CopyMenu(ContextMenu menu) {
            this.menu = menu;
        }

        private MenuItem getCopyItem() {
            return this.copyItem;
        }

        private CopyMenu build() {
            this.copyItem = new MenuItem("Copy JMX Endpoint", Images.instance.close_16().getSafeUri().asString());
            this.copyItem.addClickHandler(click -> copyToClipboard(menu.node.getDefaultJMXUrl()));
            return this;
        }

    }

    private static class LockMenu {
        private RMController controller;

        private ContextMenu menu;

        private MenuItem removeItem;

        private MenuItem lockItem;

        private MenuItem unlockItem;

        private LockMenu(RMController controller, ContextMenu menu) {
            this.controller = controller;
            this.menu = menu;
        }

        private MenuItem getRemoveItem() {
            return this.removeItem;
        }

        private MenuItem getLockItem() {
            return this.lockItem;
        }

        private MenuItem getUnlockItem() {
            return this.unlockItem;
        }

        private LockMenu build() {
            this.removeItem = new MenuItem("Remove", RMImages.instance.node_remove_16().getSafeUri().asString());
            this.removeItem.addClickHandler(onClick -> this.controller.removeNodes());

            this.lockItem = new MenuItem("Lock", this.menu.lockItemImageResource);
            this.lockItem.addClickHandler(onClick -> this.controller.lockNodes());

            this.unlockItem = new MenuItem("Unlock", this.menu.unlockItemImageResource);
            this.unlockItem.addClickHandler(onClick -> this.controller.unlockNodes());
            return this;
        }

    }

    private static class NodeSourceMenu {
        private RMController controller;

        private ContextMenu menu;

        private String nodeSourceName;

        private MenuItem deployItem;

        private MenuItem undeployItem;

        private MenuItem redeployItem;

        private MenuItem editItem;

        private NodeSourceMenu(RMController controller, ContextMenu menu, String nodeSourceName) {
            this.controller = controller;
            this.menu = menu;
            this.nodeSourceName = nodeSourceName;
        }

        private MenuItem getDeployItem() {
            return this.deployItem;
        }

        private MenuItem getUndeployItem() {
            return this.undeployItem;
        }

        private MenuItem getRedeployItem() {
            return this.redeployItem;
        }

        private MenuItem getEditItem() {
            return this.editItem;
        }

        private NodeSourceMenu build() {
            this.deployItem = new MenuItem("Deploy", this.menu.deployItemImageResource);
            this.deployItem.addClickHandler(onClick -> this.controller.deployNodeSource());

            this.undeployItem = new MenuItem("Undeploy", this.menu.undeployItemImageResource);
            this.undeployItem.addClickHandler(onClick -> this.controller.undeployNodeSource());

            this.redeployItem = new MenuItem("Redeploy", this.menu.redeployItemImageResource);
            this.redeployItem.addClickHandler(onClick -> this.controller.redeployNodeSource());

            this.editItem = new MenuItem("Edit", this.menu.editItemImageResource);
            NodeSourceStatus nodeSourceStatus = this.menu.nodesource == null ? null
                                                                             : this.menu.nodesource.getNodeSourceStatus();
            this.editItem.addClickHandler(onClick -> this.controller.editNodeSource(this.nodeSourceName,
                                                                                    nodeSourceStatus));
            return this;
        }

    }

    private static class ExportMenu {

        private RMController controller;

        private ContextMenu menu;

        private String nodeSourceName;

        private MenuItem exportNodeSourceItem;

        private MenuItem exportInfrastructureItem;

        private MenuItem exportPolicyItem;

        private ExportMenu(RMController controller, ContextMenu menu, String nodeSourceName) {
            this.controller = controller;
            this.menu = menu;
            this.nodeSourceName = nodeSourceName;
        }

        private MenuItem getExportNodeSourceItem() {
            return this.exportNodeSourceItem;
        }

        private MenuItem getExportInfrastructureItem() {
            return this.exportInfrastructureItem;
        }

        private MenuItem getExportPolicyItem() {
            return this.exportPolicyItem;
        }

        private ExportMenu build() {
            this.exportNodeSourceItem = new MenuItem("Export Node Source", this.menu.exportItemImageResource);
            Menu exportNodeSourceSubItems = new Menu();
            MenuItem exportNodeSourceToFileItem = new MenuItem("To File", this.menu.exportItemImageResource);
            MenuItem exportNodeSourceToCatalogItem = new MenuItem("To Catalog", this.menu.exportItemImageResource);
            exportNodeSourceToFileItem.addClickHandler(onClick -> this.controller.exportNodeSourceToFile(this.nodeSourceName));
            exportNodeSourceToCatalogItem.addClickHandler(onClick -> this.controller.exportNodeSourceToCatalog(this.nodeSourceName));
            exportNodeSourceSubItems.setItems(exportNodeSourceToFileItem, exportNodeSourceToCatalogItem);
            this.exportNodeSourceItem.setSubmenu(exportNodeSourceSubItems);

            this.exportInfrastructureItem = new MenuItem("Export Infrastructure", this.menu.exportItemImageResource);
            Menu exportInfrastructureSubItems = new Menu();
            MenuItem exportInfrastructureToFileItem = new MenuItem("To File", this.menu.exportItemImageResource);
            MenuItem exportInfrastructureToCatalogItem = new MenuItem("To Catalog", this.menu.exportItemImageResource);
            exportInfrastructureToFileItem.addClickHandler(onClick -> this.controller.exportInfrastructureToFile(this.nodeSourceName));
            exportInfrastructureToCatalogItem.addClickHandler(onClick -> this.controller.exportInfrastructureToCatalog(this.nodeSourceName));
            exportInfrastructureSubItems.setItems(exportInfrastructureToFileItem, exportInfrastructureToCatalogItem);
            this.exportInfrastructureItem.setSubmenu(exportInfrastructureSubItems);

            this.exportPolicyItem = new MenuItem("Export Policy", this.menu.exportItemImageResource);
            Menu exportPolicySubItems = new Menu();
            MenuItem exportPolicyToFileItem = new MenuItem("To File", this.menu.exportItemImageResource);
            MenuItem exportPolicyToCatalogItem = new MenuItem("To Catalog", this.menu.exportItemImageResource);
            exportPolicyToFileItem.addClickHandler(onClick -> this.controller.exportPolicyToFile(this.nodeSourceName));
            exportPolicyToCatalogItem.addClickHandler(onClick -> this.controller.exportPolicyToCatalog(this.nodeSourceName));
            exportPolicySubItems.setItems(exportPolicyToFileItem, exportPolicyToCatalogItem);
            this.exportPolicyItem.setSubmenu(exportPolicySubItems);
            return this;
        }

    }

}

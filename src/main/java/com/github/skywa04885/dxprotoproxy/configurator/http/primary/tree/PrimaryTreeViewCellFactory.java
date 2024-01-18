package com.github.skywa04885.dxprotoproxy.configurator.http.primary.tree;

import com.github.skywa04885.dxprotoproxy.configurator.IDXTreeItem;
import javafx.scene.control.*;
import javafx.util.Callback;

public class PrimaryTreeViewCellFactory implements Callback<TreeView<IDXTreeItem>, TreeCell<IDXTreeItem>> {
    private final PrimaryTreeContextMenuFactory configuratorTreeContextMenuPicker;

    public PrimaryTreeViewCellFactory(PrimaryTreeContextMenuFactory configuratorTreeContextMenuPicker) {
        this.configuratorTreeContextMenuPicker = configuratorTreeContextMenuPicker;
    }

    @Override
    public TreeCell<IDXTreeItem> call(TreeView<IDXTreeItem> stringTreeView) {
        final var treeCell = new TreeCell<IDXTreeItem>() {
            @Override
            protected void updateItem(IDXTreeItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    textProperty().unbind();
                    setText(null);
                    setGraphic(null);
                } else {
                    textProperty().bind(item.treeItemText());
                    setGraphic(item.treeItemGraphic());
                }
            }
        };

        treeCell.setOnContextMenuRequested(event -> {
            final var contextmenu = configuratorTreeContextMenuPicker.getContextMenuForTreeCell(treeCell);
            contextmenu.setUserData(treeCell);
            contextmenu.show(treeCell, event.getScreenX(), event.getScreenY());
        });

        return treeCell;
    }
}

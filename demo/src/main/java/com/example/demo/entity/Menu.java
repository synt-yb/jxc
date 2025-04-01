package com.example.demo.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Menu {
    private String id;
    private String text;
    private String icon;
    private String url;
    private String pid;
    private String order;
    private String flag;
    private List<Menu> children;

    public Menu() {
        children = new ArrayList<>();
    }

    public static List<Menu> getTypeTree(List<Menu> typeList) {
        Map<String, Menu> idToNodeMap = new HashMap<>();
        for (Menu node : typeList) {
            idToNodeMap.put(node.getId(), node);
        }
        List<Menu> rootNodes = new ArrayList<>();
        for (Menu node : typeList) {
            if (node.getPid() == null) {
                rootNodes.add(node);
            } else {
                Menu parent = idToNodeMap.get(node.getPid());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        return rootNodes;
    }
}

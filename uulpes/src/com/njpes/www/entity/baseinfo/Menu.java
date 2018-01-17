package com.njpes.www.entity.baseinfo;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.collect.Lists;

public class Menu implements Serializable {
    private Long id;
    private String name;
    private String icon;
    private String url;
    private int isleaf;

    private List<Menu> children;

    public Menu(Long id, String name, String icon, String url, int isleaf) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.url = url;
        this.isleaf = isleaf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Menu> getChildren() {
        if (children == null) {
            children = Lists.newArrayList();
        }
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    /**
     * @return
     */
    public boolean isHasChildren() {
        return !getChildren().isEmpty();
    }

    public int getIsleaf() {
        return isleaf;
    }

    public void setIsleaf(int isleaf) {
        this.isleaf = isleaf;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String str = null;
        try {
            str = mapper.writeValueAsString(this);
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
        // "Menu{" +
        // "id=" + id +
        // ", name='" + name + '\'' +
        // ", icon='" + icon + '\'' +
        // ", url='" + url + '\'' +
        // ", children=" + children +
        // '}';
    }
}

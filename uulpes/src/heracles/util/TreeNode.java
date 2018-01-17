package heracles.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

public class TreeNode {
    public static void main(String[] args) {
        TreeNode node = new TreeNode();
        TreeNode node1 = new TreeNode();

        node1.addChild(new TreeNode());
        node1.addChild(new TreeNode());
        node1.addChild(new TreeNode());

        node.addChild(new TreeNode());
        node.addChild(new TreeNode());
        node.addChild(new TreeNode());
        node.addChild(new TreeNode());

        node.addChild(node1);
    }

    private List<TreeNode> children = new ArrayList<TreeNode>();
    private List<Object> values = new ArrayList<Object>();

    private boolean isFirstChild = false;

    private String label;
    private int numOfChild;

    public TreeNode() {

    }

    public TreeNode(String label) {
        this.setLabel(label);
    }

    public TreeNode(String id, String label) {
        super();
        this.label = label;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    /**
     * 比较值（参考值），用来辅助value的作用
     */
    private double reference;

    public double getReference() {
        return reference;
    }

    public void setReference(double reference) {
        this.reference = reference;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TreeNode addChild(TreeNode node) {
        if (children.size() == 0)
            node.isFirstChild = true;
        children.add(node);
        return node;
    }

    public boolean isLeaf() {
        return getChildren().size() == 0;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public TreeNode getaLeaf() {
        if (children.size() == 0) {
            return this;
        } else {
            for (int i = 0; i < children.size(); ++i) {
                TreeNode node = children.get(i);
                return node.getaLeaf();
            }
        }
        return null;
    }

    public boolean childHasLabel() {
        for (int i = 0; i < children.size(); ++i) {
            TreeNode node = children.get(i);
            if (node.label != null)
                return true;
        }
        return false;
    }

    public TreeNode findChild(String id) {
        if (id.equalsIgnoreCase(id)) {
            return this;
        } else {
            for (int i = 0; i < children.size(); ++i) {
                TreeNode node = children.get(i);
                return node.findChild(id);
            }
        }
        return null;
    }

    /**
     * 找到一个有具有值的叶子
     * 
     * @return
     */
    public TreeNode getaLeafWithValues() {
        if (getChildren().size() == 0) {
            return this;
        } else {
            for (int i = 0; i < getChildren().size(); ++i) {
                TreeNode node = children.get(i);
                return node.getaLeaf();
            }
        }
        return null;
    }

    public int countLeaf() {
        if (children.size() == 0) {
            return 1;
        } else {
            int ivals[] = new int[children.size()];
            for (int i = 0; i < children.size(); ++i) {
                TreeNode node = children.get(i);
                ivals[i] = node.countLeaf();
            }
            int sum = 0;
            for (int i = 0; i < ivals.length; ++i) {
                sum += ivals[i];
            }
            return sum;
        }
    }

    public int getNumOfChilds() {
        numOfChild = 0;
        TreeVisitor visitor = new TreeVisitor() {
            public void access(TreeNode tn) {
                numOfChild++;
            }
        };
        visit(visitor);
        return numOfChild - 1; // ��ȥ�Լ�
    }

    public void visit(TreeVisitor visitor) {
        visitor.access(this);
        for (TreeNode node : children) {
            node.visit(visitor);
        }
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values.clear();
        this.values.addAll(values);
    }

    public void setValues(Object[] values) {
        this.values.clear();
        this.values.addAll(Arrays.asList(values));
    }

    public void addValue(Object element) {
        this.values.add(element);
    }

    public void setValue(int index, Object element) {
        this.values.add(index, element);
    }

    public Object getValue(int index) {
        return this.values.get(index);
    }

    /**
     * @return
     */
    public int getDepth() {
        if (children.size() == 0) {
            return 0;
        } else {
            int ivals[] = new int[children.size()];
            for (int i = 0; i < children.size(); ++i) {
                TreeNode node = children.get(i);
                ivals[i] = node.getDepth();
            }
            return 1 + NumberUtils.max(ivals);
        }
    }

    public boolean isFirstChild() {
        return isFirstChild;
    }

    public boolean hasChild() {
        return !children.isEmpty();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Object o : values) {
            sb.append(o).append(",");
        }
        return sb.toString();
    }
}

package fi.videosambo.iptools;

import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlToTree {
	
    private Document document;
    private DefaultTreeModel treeModel;
    private JTree treeView;

    public XmlToTree(Document doc) {
        this.document = doc;
        treeModel = new DefaultTreeModel(buildTreeNode(document));
        treeView = new JTree(treeModel);
    }
    
    public void expandAllNodes() {
        if (treeView != null) {
            for (int i = 0; i < treeView.getRowCount(); i++) {
                treeView.expandRow(i);
            }
        }
    }
    
    public JTree show() {
        if (treeView != null)
            return treeView;
		return null;
    }
    
    private DefaultMutableTreeNode buildTreeNode(Node docNode) {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(
                docNode.getNodeName());

        if (docNode.hasAttributes()) {
            NamedNodeMap attributes = docNode.getAttributes();

            for (int j = 0; j < attributes.getLength(); j++) {
                String attr = attributes.item(j).toString();
                treeNode.add(new DefaultMutableTreeNode(attr));
            }
        }

        if (docNode.hasChildNodes()) {
            NodeList childNodes = docNode.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                short childNodeType = childNode.getNodeType();

                if (childNodeType == Node.ELEMENT_NODE) {
                    treeNode.add(buildTreeNode(childNode));
                } else if (childNodeType == Node.TEXT_NODE) {
                    String text = childNode.getTextContent().trim();
                    if (!text.equals("")) {
                        treeNode.add(new DefaultMutableTreeNode(text));
                    }
                } else if (childNodeType == Node.COMMENT_NODE) {
                    String comment = childNode.getNodeValue().trim();
                    treeNode.add(new DefaultMutableTreeNode(comment));
                }
            }
        }
        return treeNode;
    }
    
    
}

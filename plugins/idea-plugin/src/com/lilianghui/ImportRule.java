package com.lilianghui;

import com.lilianghui.action.ImportAction;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.JavaNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class ImportRule extends AbstractJavaRule {
    private ImportAction importAction;

    public ImportRule(ImportAction importAction) {
        this.importAction=importAction;
    }

    @Override
    public Object visit(JavaNode node, Object data) {
        node.childrenAccept(this, data);
        if (node.getImage() != null) {
            String image = node.getImage();
            if (node instanceof ASTClassOrInterfaceType) {
//                System.err.println("class=" + image);
                importAction.addName(image);
            } else if (node instanceof ASTName) {
//                    System.err.println("name=" + image);
                importAction.addName(((ASTName) node).jjtGetFirstToken().getImage());
            } else {
            }
            System.out.println(node.getImage() + "---" + node.getClass());//ClassOrInterfaceType //
        }
        return null;
    }

}

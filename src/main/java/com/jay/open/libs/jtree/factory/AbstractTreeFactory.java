package com.jay.open.libs.jtree.factory;

import com.jay.open.libs.jtree.Node;
import com.jay.open.libs.jtree.RootIdAware;
import com.jay.open.libs.jtree.Tree;
import com.jay.open.libs.jtree.TreeUtils;
import com.jay.open.libs.jtree.parsers.NodeParser;
import com.jay.open.libs.common.io.EntriesLoader;
import com.jay.open.libs.common.validate.Assert;
import com.sankuai.xm.eas.libs.etree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: suweijie
 * Date: 14-11-12
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractTreeFactory implements TreeFactory,RootIdAware {
    private Logger log = LoggerFactory.getLogger(AbstractTreeFactory.class);

    private long rootId = 0;

    protected EntriesLoader entriesLoader;
    protected NodeParser nodeParser;
    protected Class<? extends Tree> treeClazz;
    protected Class<? extends Node> nodeClazz;

    protected abstract EntriesLoader getEntriesLoader();

    protected abstract NodeParser getNodeParser(Class<? extends Node> nodeClazz);

    public AbstractTreeFactory(Class<? extends Tree> treeClazz,Class<? extends Node> nodeClazz){
        Assert.notNull(nodeClazz);
        Assert.notNull(treeClazz);
        this.nodeClazz = nodeClazz;
        this.treeClazz = treeClazz;

        entriesLoader = getEntriesLoader();
        nodeParser = getNodeParser(nodeClazz);

        Assert.notNull(entriesLoader);
        Assert.notNull(nodeParser);

        nodeParser.bindEntriesLoader(entriesLoader);
    }

    @Override
    public Tree buildTree() {
        Node root = nodeParser.parseTreeRoot(getRootId());
        Tree tree = null;
        try {
            tree = treeClazz.newInstance();
        } catch (Exception e) {
            log.error(e.getMessage()+"(Please support a non-params constructor)",e);
        }
        tree.setRoot(root);
        TreeUtils.validateTree(tree);
        return tree;
    }

    @Override
    public long getRootId() {
        return this.rootId;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setRootId(long rootId){
        this.rootId = rootId;
    }
}

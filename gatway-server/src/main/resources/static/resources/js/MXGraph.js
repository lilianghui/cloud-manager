var graph;
var nodes = [];
var editor;

$(function () {
    main();
    addMap();
})

function addMap() {
    var div = document.createElement('div');
    div.style.background = "round";//圆角
    div.style.height = "50px";
    div.style.width = "120px";
    div.style.border = "1px";
    div.style.left = '20px';
    div.style.background = "#f4f5f8";
    div.style.marginTop = "-10px";

    // 创建拖动源的预览
    var dragElt = document.createElement('div');
    dragElt.style.border = 'dashed black 1px';
    dragElt.style.width = '110px';
    dragElt.style.height = '50px';

    $("#sidebar").append(div);


    var ds = mxUtils.makeDraggable(div, graph, funct, dragElt, 0, 0, true, true);
    ds.setGuidesEnabled(true);
}

function funct(graph, evt, target,x,y) {
    //创建默认窗体
    var parent = graph.getDefaultParent();
    try {
        graph.getModel().beginUpdate();
        var mcell = graph.insertVertex(parent, null, "aa", x, y, 32, 32, 'start_image4gray;rounded=true;strokeColor=none;fillColor=yellow;');
        if (null != mcell) {
            // var nums = new Date().getTime();
            // mcell.node_type = node_type;
            // mcell.nodeID = node_type + nums;
            // mcell.value = label;
        }



      /*  var cell = new mxCell('测试CELL', new mxGeometry(0, 0, 120, 40));
        cell.vertex = true;
        var cells = graph.importCells([cell], x, y, target);

        if (cells != null && cells > 0)
        {
            graph.scrollCellToVisible(cells[0]);
            graph.setSelectionCells(cells);
        }
        */

        graph.stopEditing(false);
    } finally {
        graph.getModel().endUpdate();
    }
}

function main() {
    if (!mxClient.isBrowserSupported()) {
        // Displays an error message if the browser is not supported.
        mxUtils.error('Browser is not supported!', 200, false);
    }
    editor = new mxEditor();
    graph = editor.graph;
    graph.setConnectable(true);
    graph.setDropEnabled(true);//从工具栏拖动到目标细胞时细胞边界是否产生光圈
    graph.setTooltips(true);
    //点击节点连线 不生成新的节点出来 false true为生成新节点
    graph.connectionHandler.setCreateTarget(false);
    //重复连接
    graph.setMultigraph(false);
    //指定图表是否应允许将单元格放入其他单元格中或其他单元格中。
    graph.setSplitEnabled(true);
    //是否可以移动连线 重新连接其他cell，主要用来展现中使用
    //graph.setCellsLocked(true);
    //节点不可改变大小
    graph.setCellsResizable(true);
    /**
     *邓纯杰
     *创建连线
     **/
    //定义新连接的图标
    //mxConnectionHandler.prototype.connectImage = new mxImage('../view/pc/lc-view/lc-design/archive/grapheditor/images/connector.gif', 16, 16);
    //节点变化情况事件主要用于验证节点杰连线及节点
    graph.getSelectionModel().addListener(mxEvent.CHANGE, function (sender, evt) {
    });

    //移动元素触发事件
    // graph.addListener(mxEvent.CELLS_MOVED, function (sender, evt) {
    //
    // });
    //禁止连接线晃动即不可以离开节点
    graph.setAllowDanglingEdges(false);
    //允许连线的目标和源是同一元素
    graph.setAllowLoops(false);
    //导航线 显示细胞位置标尺
    mxGraphHandler.prototype.guidesEnabled = true;
    //自动导航目标
    mxEdgeHandler.prototype.snapToTerminals = true;
    //去锯齿效果
    mxRectangleShape.prototype.crisp = true;
    /**
     *邓纯杰
     *节点拖动事件包括移动 拖动 复制 粘贴（注意不包括在节点上生成新的节点）
     **/
    // mxGraph.prototype.moveCells = function (cells, dx, dy, clone, target, evt) {
    //     if (cells != null && (dx != 0 || dy != 0 || clone || target != null)) {
    //
    //     }
    //     //处理flow与Message转换验证
    //     return cells;
    // };

    /////////////////////////////边界事件节点只能在父元素边框1/2位置----开始/////////////////////
    graph.graphHandler.getDelta = function (me) {
        var point = mxUtils.convertPoint(this.graph.container, me.getX(), me.getY());
        var delta = new mxPoint(point.x - this.first.x, point.y - this.first.y);
        return delta;
    };
    graph.graphHandler.shouldRemoveCellsFromParent = function (parent, cells, evt) {
        if (cells.length == 1) {
            if (cells[0].node_type == 'errorStartEvent') {
                if (null != parent && typeof(parent.node_type) != "undefined" && parent.node_type == 'eventSubProcess') {
                    return false;
                }
            }
        } else {
            ////////////////判断一起拖拽的是否存在错误启动事件
            for (var i = 0; i < cells.length; i++) {
                if (cells[i].node_type == 'errorStartEvent') {
                    if (null != parent && typeof(parent.node_type) != "undefined" && parent.node_type == 'eventSubProcess') {
                        return false;
                    }
                }
            }
        }
        return cells.length > 0 && !cells[0].geometry.relative && mxGraphHandler.prototype.shouldRemoveCellsFromParent.apply(this, arguments);
    };

    graph.translateCell = function (cell, dx, dy) {

    };
    graph.isCellLocked = function (cell) {
        return false;
    };
    new mxRubberband(graph);
    /////////////////////////////边界事件节点只能在父元素边框1/2位置----结束/////////////////////

    mxGraph.prototype.constrainRelativeChildren = true;
    ////////////////////////////////子标签操作开始////////////////////////////
    // 禁止折叠图标  用于泳道及子标签
    graph.isCellFoldable = function (cell) {
        return false;
    }
    var secondLabelVisible = true;
    // 返回对于一个给定单元格的编号
    graph.getSecondLabel = function (cell) {
        if (!this.model.isEdge(cell)) {
            // 可能返回一个字符串
            return "ID=" + cell.id;
        }
        return null;
    };
    var relativeChildVerticesVisible = true;
    // 隐藏相对于子元素的顶点
    graph.isCellVisible = function (cell) {
        return !this.model.isVertex(cell) || cell.geometry == null ||
            !cell.geometry.relative ||
            cell.geometry.relative == relativeChildVerticesVisible;
    };
    //创建历史
    var history = new mxUndoManager();
    // 移动/调整大小已被重新绘制的元素
    ////////////////////////////////子标签操作结束////////////////////////////

    graph.flipEdge = function (edge) {
        if (edge != null) {
            var state = this.view.getState(edge);
            var style = (state != null) ? state.style : this.getCellStyle(edge);
            {
                var elbow = mxUtils.getValue(style, mxConstants.STYLE_ELBOW,
                    mxConstants.ELBOW_HORIZONTAL);
                var value = (elbow == mxConstants.ELBOW_HORIZONTAL) ?
                    mxConstants.ELBOW_VERTICAL : mxConstants.ELBOW_HORIZONTAL;
                this.setCellStyles(mxConstants.STYLE_ELBOW, value, [edge]);
            }
        }
    };
    //双击事件
    graph.addListener(mxEvent.DOUBLE_CLICK, function (sender, evt) {
        var cell = evt.getProperty('cell');
    });
    //单击事件
    graph.addListener(mxEvent.CLICK, function (sender, evt) {
        var cell = evt.getProperty('cell');
        /**
         if(typeof(cell) != "undefined" && null != cell && (cell.node_type == 'timerBoundaryEvent')){
			graph.setCellsResizable(false);
			return;
		}else{
			graph.setCellsResizable(true);
		}
         **/
    });
    //改变大小事件
    graph.addListener(mxEvent.CELLS_RESIZED, function (sender, evt) {
        var cells = evt.getProperty('cells');
        /**
         if(typeof(cells) != "undefined" && null != cells && (cells[0].node_type == 'timerBoundaryEvent')){
			msgTishi('时间边界不能改变大小');
			graph.setCellsResizable(false);
			return;
		}else{
			graph.setCellsResizable(true);
		}
         **/
    });
    //捕获任务节点的鼠标点击事件
    graph.addListener(mxEvent.CLICK, function (sender, evt) {
    });

    //初始化mxGraph图形面板
    var container = document.getElementById("container");
    container.style.position = 'absolute';
    container.style.overflow = 'auto';
    container.style.left = '0px';
    container.style.top = '0px';
    container.style.right = '0px';
    container.style.bottom = '0px';
    graph.init(container);
    if (mxClient.IS_GC || mxClient.IS_SF) {
        graph.container.style.background = '-webkit-gradient(linear, 0% 0%, 100% 0%, from(#FFFFFF), to(#FFFFFF))';
    } else if (mxClient.IS_NS) {
        graph.container.style.background = '-moz-linear-gradient(left, #FFFFFF, #FFFFFF)';
    } else if (mxClient.IS_IE) {
        graph.container.style.filter = 'progid:DXImageTransform.Microsoft.Gradient(' + 'StartColorStr=\'#FFFFFF\', EndColorStr=\'#FFFFFF\', GradientType=1)';
    }
}

var x = 120;

function append() {
    var parent = graph.getDefaultParent();
    graph.getModel().beginUpdate();
    try {
        x += 100;
        var v2 = graph.insertVertex(parent, null,
            'World!' + x, x, 150, 80, 30);
        var e1 = graph.insertEdge(parent, null, '', nodes[nodes.length - 1], v2);
        nodes.push(v2);
    }
    finally {
        // 更新显示
        graph.getModel().endUpdate();
    }
}

function toXML() {
    var enc = new mxCodec();
    var node = enc.encode(graph.getModel());
    console.log(mxUtils.getXml(node))
}
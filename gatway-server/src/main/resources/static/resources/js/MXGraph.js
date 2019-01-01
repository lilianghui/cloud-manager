var graph;
var nodes = [];
var historyManager;
var editor;

$(function () {
    main();
    initLeftPanel(graph);
    addMouse();
    $("#basicForm button[type='button']").click(global_event["parse"]);
})


function main() {
    if (!mxClient.isBrowserSupported()) {
        // Displays an error message if the browser is not supported.
        mxUtils.error('Browser is not supported!', 200, false);
    }
    // var config=mxUtils.load("../static/resources/mxgraph/editors/config/keyhandler-commons.xml").getDocumentElement();
    editor = new mxEditor();
    graph = editor.graph;
    var parent = graph.getDefaultParent();

    // graph.setConnectable(true);
    graph.setDropEnabled(true);//从工具栏拖动到目标细胞时细胞边界是否产生光圈
    graph.setTooltips(true);
    graph.setCellsEditable(true);
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
    // 允许移动 Vertex 的 Label
    graph.setVertexLabelsMovable(true);
    // graph.setCellsMovable(true);
    graph.setHtmlLabels(true);
// 连线的样式
    var style = graph.getStylesheet().getDefaultEdgeStyle();
    style[mxConstants.STYLE_ROUNDED] = true;//圆角连线
    style[mxConstants.STYLE_EDGE] = mxEdgeStyle.ElbowConnector; //自己拐弯的连线

    //*****************************************************************************************8

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
    //     return true;
    // });
    //禁止连接线晃动即不可以离开节点
    graph.setAllowDanglingEdges(false);
    //允许连线的目标和源是同一元素
    // graph.setAllowLoops(false);
    //导航线 显示细胞位置标尺
    mxGraphHandler.prototype.guidesEnabled = true;
    //自动导航目标
    mxEdgeHandler.prototype.snapToTerminals = true;
    //去锯齿效果
    mxRectangleShape.prototype.crisp = true;
    // mxGraphHandler.prototype.setMoveEnabled(false);//是否可以移动


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
        mxGraph.prototype.translateCell.apply(this, arguments);
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
    historyManager = new mxUndoManager();
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
        var key = "dblclick";
        if ($.isFunction(global_event[key])) {
            global_event[key](sender, evt);
        }
    });
    //单击事件

    graph.addListener(mxEvent.CLICK, function (sender, evt) {
        var key = "click";
        if ($.isFunction(global_event[key])) {
            global_event[key](sender, evt);
        }
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
    //*****************************************************************************************8


    //初始化mxGraph图形面板
    var container = document.getElementById("container");
    container.style.position = 'absolute';
    container.style.overflow = 'auto';
    container.style.left = '0px';
    container.style.top = '0px';
    container.style.right = '0px';
    container.style.bottom = '0px';


    container.style.background = 'url("../static/resources/images/grid.gif") repeat white';
    container.style.cursor = 'hand';

    graph.init(container);
    // if (mxClient.IS_GC || mxClient.IS_SF) {
    //     graph.container.style.background = '-webkit-gradient(linear, 0% 0%, 100% 0%, from(#FFFFFF), to(#FFFFFF))';
    // } else if (mxClient.IS_NS) {
    //     graph.container.style.background = '-moz-linear-gradient(left, #FFFFFF, #FFFFFF)';
    // } else if (mxClient.IS_IE) {
    //     graph.container.style.filter = 'progid:DXImageTransform.Microsoft.Gradient(' + 'StartColorStr=\'#FFFFFF\', EndColorStr=\'#FFFFFF\', GradientType=1)';
    // }


    $.contextMenu({
        selector: "#container",
        callback: function (key, options) {
            if ($.isFunction(global_event[key])) {
                global_event[key](key, options);
            }
        },
        items: {
            "undo": {name: "撤消", icon: "undo"},
            "redo": {name: "重做", icon: "redo"},
            "delete": {name: "删除", icon: "delete"},
            "sep1": "---------",
            "show": {name: "预览", icon: "add"}
        },
        events: {
            show: function (options) {
                $(this).addClass("dataTableHighlight");
                return true;
            },
            hide: function (options) {
                $(this).removeClass("dataTableHighlight");
                return true;
            }
        }
    });

    historyManager = new mxUndoManager();
    var undoHandler = function (sender, evt) {
        var changes = evt.getProperty('edit').changes;
        graph.setSelectionCells(graph.getSelectionCellsForChanges(changes));
    };

    historyManager.addListener(mxEvent.UNDO, undoHandler);
    historyManager.addListener(mxEvent.REDO, undoHandler);

    var listener = function (sender, evt) {
        historyManager.undoableEditHappened(evt.getProperty('edit'));
    };

    graph.getModel().addListener(mxEvent.UNDO, listener);
    graph.getView().addListener(mxEvent.UNDO, listener);

    var keyHandler = new mxKeyHandler(graph);
    keyHandler.enter = function () {
    };
    keyHandler.bindKey(8, function () {
        graph.foldCells(true);
    });
    keyHandler.bindKey(13, function () {
        graph.foldCells(false);
    });
    keyHandler.bindKey(33, function () {
        graph.exitGroup();
    });
    keyHandler.bindKey(34, function () {
        graph.enterGroup();
    });
    keyHandler.bindKey(36, function () {
        graph.home();
    });
    keyHandler.bindKey(35, function () {
        graph.refresh();
    });
    keyHandler.bindKey(KEY_MAP.LEFT, function () {
        graph.selectPreviousCell();
    });
    //UP
    keyHandler.bindKey(KEY_MAP.UP, function () {
        graph.selectParentCell();
    });
    keyHandler.bindKey(KEY_MAP.RIGHT, function () {
        graph.selectNextCell();
    });
    keyHandler.bindKey(KEY_MAP.DOWN, function () {
        graph.selectChildCell();
    });
    keyHandler.bindKey(KEY_MAP.DELETE, function () {
        graph.removeCells();
    });
    keyHandler.bindKey(107, function () {
        graph.zoomIn();
    });
    keyHandler.bindKey(109, function () {
        graph.zoomOut();
    });
    keyHandler.bindKey(113, function () {
        graph.startEditingAtCell();
    });
    keyHandler.bindControlKey(65, function () {
        graph.selectAll();
    });
    keyHandler.bindControlKey(89, function () {
        historyManager.redo();
    });
    keyHandler.bindControlKey(90, function () {
        historyManager.undo();
    });
    keyHandler.bindControlKey(88, function () {
        mxClipboard.cut(graph);
    });
    keyHandler.bindControlKey(67, function () {
        mxClipboard.copy(graph);
    });
    keyHandler.bindControlKey(86, function () {
        mxClipboard.paste(graph);
    });
    keyHandler.bindControlKey(71, function () {
        graph.setSelectionCell(graph.groupCells(null, 20));
    });
    keyHandler.bindControlKey(85, function () {
        graph.setSelectionCells(graph.ungroupCells());
    });


    // 设置自动扩大鼠标悬停
    graph.panningHandler.autoExpand = true;
    // 覆写右键单击事件
    graph.panningHandler.factoryMethod = function (menu, cell, evt) {
        menu.addItem('Item 1', null, function () {
            alert('Item 1');
        });

        menu.addItem('Item 2', null, function () {
            alert('Item 2');
        });

        menu.addSeparator();

        var submenu1 = menu.addItem('Submenu 1', null, null);

        menu.addItem('Subitem 1', null, function () {
            alert('Subitem 1');
        }, submenu1);
        menu.addItem('Subitem 1', null, function () {
            alert('Subitem 2');
        }, submenu1);
    };
}

var x = 120;

function append() {
    var parent = graph.getDefaultParent();
    graph.getModel().beginUpdate();
    try {
        x += 100;
        var v2 = graph.insertVertex(parent, null,
            '<div style="color: red;">World! </div>' + x, x, 150, 80, 30);
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

    //获取mxgraph拓扑图数据
    //var enc = new mxCodec(mxUtils.createXmlDocument());
    //var node1 = enc.encode(graph.getModel());
    //var mxgraphxml = mxUtils.getXml(node1);
    var enc = new mxCodec(mxUtils.createXmlDocument());
    var node = enc.encode(graph.getModel());
    var mxgraphxml = mxUtils.getPrettyXml(node);
    mxgraphxml = mxgraphxml.replace(/\"/g, "'");
    //mxgraphxml = encodeURIComponent(mxgraphxml);

    var xmlDoc = mxUtils.createXmlDocument();
    var root = xmlDoc.createElement('output');
    xmlDoc.appendChild(root);
    var xmlCanvas = new mxXmlCanvas2D(root);
    var imgExport = new mxImageExport();
    imgExport.drawState(graph.getView().getState(graph.model.root), xmlCanvas);
    var bounds = graph.getGraphBounds();
    var w = Math.round(bounds.x + bounds.width + 4);
    var h = Math.round(bounds.y + bounds.height + 4);
    var imgxml = mxUtils.getXml(root);
    //imgxml = "<output>"+imgxml+"</output>";
    //imgxml = encodeURIComponent(imgxml);
    // save_process(mxgraphxml,w,h,imgxml);
    $.ajax({
        url: "/modal",
        type: "POST",
        data: {graphXml: mxgraphxml},
        dataType: "json",
        success: function (data) {
            window.location.reload();
        }
    });
}

var perX;
var global_event = {
    "delete": function (key, options) {
        var v2 = graph.getSelectionCell();
        console.log(v2)
        graph.removeCells();
    },
    "undo": function () {
        historyManager.undo();
    },
    "redo": function () {
        historyManager.redo();
    },
    "show": function () {
        var url = editor.getUrlImage();
        if (url == null || mxClient.IS_LOCAL) {
            editor.execute('show');
        } else {
            var node = mxUtils.getViewXml(editor.graph, 1);
            var xml = mxUtils.getXml(node, '\n');
            mxUtils.submit(url, graph.postParameterName + '=' +
                encodeURIComponent(xml), document, '_blank');
        }
    },
    "dblclick": function (sender, evt) {

    },
    "parse": function () {
        var basicForm = $(this).closest("form");
        var cell = basicForm.data("cell");
        var row = cell.row;
        row.name = basicForm.find("[name='name']").val();
        row.type = basicForm.find("[name='type']").val();
        return false;
    },
    "click": function (sender, evt) {
        var cell = evt.getProperty('cell');
        var value = {};
        var basicForm = $("#basicForm");
        if (cell) {
            cell.row = cell.row || {};
            value = cell.row;
        }
        basicForm.data("cell", cell);
        basicForm.find("[name='name']").val(value.name);
        basicForm.find("[name='type']").val(value.type);
        /* var v2 = graph.getSelectionCell();
         if (!v2 || v2.edge) {
             return;
         }
         if (perX) {
             if (v2 && perX.id != v2.id) {
                 graph.insertEdge(parent, null, '', perX, v2);
             }
             perX = null;
         } else {
             perX = v2;
         }*/


        /**
         if(typeof(cell) != "undefined" && null != cell && (cell.node_type == 'timerBoundaryEvent')){
			graph.setCellsResizable(false);
			return;
		}else{
			graph.setCellsResizable(true);
		}
         **/
    }
}

var KEY_MAP = {
    LEFT: 37,
    UP: 38,
    RIGHT: 39,
    DOWN: 40,
    DELETE: 46,
}
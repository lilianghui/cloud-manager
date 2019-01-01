var p_data = [{
    name: "Excel输入",
    image: "i_userGroupManager.png",
    id: "e_input",
    width: 100,
    height: 100,
    sWidth: 50,
    sHeight: 50,
    type: "e_input"
}, {
    name: "Excel输出",
    image: "i_transDetail.png",
    id: "e_output",
    width: 100,
    height: 100,
    sWidth: 50,
    sHeight: 50,
    type: "e_output"
}, {
    name: "文本文件输入",
    image: "i_slaveCon_16.png",
    id: "t_input",
    width: 100,
    height: 100,
    sWidth: 50,
    sHeight: 50,
    type: "t_input"
}, {
    name: "文本文件输出",
    image: "i_refresh.png",
    id: "t_output",
    width: 100,
    height: 100,
    sWidth: 50,
    sHeight: 50,
    type: "t_output"
}];

var $element_tp = "<div class='tuyuan'><img class='pic'><span class='name'></span>  </div>";

function initLeftPanel(graph) {
    $.each(p_data, function (index, item) {
        var $element = $($element_tp);
        $element.find("span.name").html(item.name);
        $element.find("img.pic").attr("src", "../static/resources/images/" + item.image);

        // 创建拖动源的预览
        var $dragElt = $("<div>").css({});

        $("#sidebar").append($element);

        var func = (function (item) {
            function funct(graph, evt, target, x, y) {
                //创建默认窗体
                var parent = graph.getDefaultParent();
                try {
                    graph.getModel().beginUpdate();
                    var mcell = graph.insertVertex(parent, null, item.name, x, y, item.sWidth, item.sHeight, initType(item) + ';rounded=true;strokeColor=none;fillColor=yellow;size=12');
                    mcell.type = item.type;
                    mcell.row = {};
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

            return funct;
        })(item);
        var ds = mxUtils.makeDraggable($element.get(0), graph, func, $element.get(0), 0, 0, true, true);
        ds.setGuidesEnabled(true);
    })
}

function getStyle(item) {
    var name = "Ccc";
    // 声明一个object
    var style = {};
    // 克隆一个object
    style = mxUtils.clone(style);
    style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_LABEL;  // 不设置这个属性 背景图片不出来
    // 边框颜色
    // style[mxConstants.STYLE_STROKECOLOR] = '#999999';
    // 边框大小
    // style[mxConstants.STYLE_STROKEWIDTH] = 10;
    // 字体颜色
    style[mxConstants.STYLE_FONTCOLOR] = '#FFFF00';
    // 文字水平方式
    style[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_CENTER;
    // 文字垂直对齐
    style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_BOTTOM;
    // 字体大小
    style[mxConstants.STYLE_FONTSIZE] = 30;
    // 底图水平对齐
    style[mxConstants.STYLE_IMAGE_ALIGN] = mxConstants.ALIGN_CENTER;
    // 底图垂直对齐
    style[mxConstants.STYLE_IMAGE_VERTICAL_ALIGN] = mxConstants.ALIGN_CENTER;
    // 图片路径
    //style[mxConstants.STYLE_IMAGE] = 'images/icons48/gear.png';
    style[mxConstants.STYLE_IMAGE] = item.image;
    // 背景图片宽
    style[mxConstants.STYLE_IMAGE_WIDTH] = 50;
    // 背景图片高
    style[mxConstants.STYLE_IMAGE_HEIGHT] = 20;
    // 上间距设置
    // 即使下边定义了全局设置，但这里单独设置上边间距仍单独有效
    style[mxConstants.STYLE_SPACING_TOP] = 30;
    // 四边间距设置
    style[mxConstants.STYLE_SPACING] = 10;
    // 把定义好的样式object push到stylesheet
    graph.getStylesheet().putCellStyle(name, style);
    //样式使用
    return name;
}

var map_type = {};

function initType(item) {
    if (!map_type[item.id]) {
        //1-1.开始样式
        var start_style = new Object();
        start_style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_IMAGE;
        start_style[mxConstants.STYLE_FONTSIZE] = '8';
        start_style[mxConstants.STYLE_FONTCOLOR] = '#f5f5f5';
        start_style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
        start_style[mxConstants.STYLE_IMAGE] = "../static/resources/images/" + item.image;
        graph.getStylesheet().putCellStyle(item.id, start_style);
        map_type[item.id] = 1;
    }
    return item.id;
}
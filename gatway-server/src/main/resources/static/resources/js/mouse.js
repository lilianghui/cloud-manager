function addMouse() {
    graph.addMouseListener({
        currentState: null,
        currentIconSet: null,
        mouseDown: function (sender, me) {
            if (this.currentState != null) {
                this.dragLeave(me.getEvent(), this.currentState);
                this.currentState = null;
            }
        },
        mouseMove: function (sender, me) {
            if (this.currentState != null && (me.getState() == this.currentState ||
                me.getState() == null)) {
                var tol = iconTolerance;
                var tmp = new mxRectangle(me.getGraphX() - tol,
                    me.getGraphY() - tol, 2 * tol, 2 * tol);
                if (mxUtils.intersects(tmp, this.currentState)) {
                    return;
                }
            }
            var tmp = graph.view.getState(me.getCell());
            if (graph.isMouseDown || (tmp != null && !graph.getModel().isVertex(tmp.cell))) {
                tmp = null;
            }
            if (tmp != this.currentState) {
                if (this.currentState != null) {
                    this.dragLeave(me.getEvent(), this.currentState);
                }
                this.currentState = tmp;
                if (this.currentState != null) {
                    this.dragEnter(me.getEvent(), this.currentState);
                }
            }
        },
        mouseUp: function (sender, me) {
        },
        dragEnter: function (evt, state) {
            if (this.currentIconSet == null) {
                this.currentIconSet = new mxIconSet(state);
            }
        },
        dragLeave: function (evt, state) {
            if (this.currentIconSet != null) {
                this.currentIconSet.destroy();
                this.currentIconSet = null;
            }
        }
    });


}


///////////////////////定义鼠标移动到节点出现图标开始////////////////////////////
var iconTolerance = 20;

function mxIconSet(state) {
    var cell = state.cell;
    this.images = [];
    var graph = state.view.graph;
    var md = (mxClient.IS_TOUCH) ? 'touchstart' : 'mousedown';
    //1删除

    var img = $("<img>").attr({
        "title": "连线",
        "src": "../static/resources/images/i_quato.png",
    }).css({
        "position": "absolute",
        "cursor": "pointer",
        "width": "30px",
        "height": "30px",
        "left": (state.x + state.width + 10) + "px",
        "top": (state.y + 16 + 16 + 14) + "px"
    }).get(0);
    mxEvent.addListener(img, md, mxUtils.bind(this, function (evt) {
            var pt = mxUtils.convertPoint(graph.container, mxEvent.getClientX(evt), mxEvent.getClientY(evt));
            graph.connectionHandler.start(state, pt.x, pt.y);
            graph.isMouseDown = true;
            mxEvent.consume(evt);
        })
    );
    state.view.graph.container.appendChild(img);
    this.images.push(img);

};
mxIconSet.prototype.destroy = function () {
    if (this.images != null) {
        for (var i = 0; i < this.images.length; i++) {
            var img = this.images[i];
            img.parentNode.removeChild(img);
        }
    }
    this.images = null;
};

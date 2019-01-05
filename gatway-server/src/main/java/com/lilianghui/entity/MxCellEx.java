package com.lilianghui.entity;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MxCellEx extends mxCell {

    private String type;
    protected Object row;

    public MxCellEx() {
    }

    public MxCellEx(Object value) {
        super(value);
    }

    public MxCellEx(Object value, mxGeometry geometry, String style) {
        super(value, geometry, style);
    }

}

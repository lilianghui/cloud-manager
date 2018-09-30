package com.lilianghui.action;

import com.lilianghui.Context;
import com.lilianghui.ui.ImportDialog;

public interface Clicked {
    boolean mouseClicked(ImportDialog importDialog, Context context, int type);

    void refreshModel(Context context, ImportDialog importDialog, String text);
}

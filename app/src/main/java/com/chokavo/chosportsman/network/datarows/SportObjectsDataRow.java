
package com.chokavo.chosportsman.network.datarows;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SportObjectsDataRow extends BaseDataRow{

    @SerializedName("Cells")
    @Expose
    private com.chokavo.chosportsman.network.cells.SportObjectsMoscowCells Cells;

    /**
     * 
     * @return
     *     The Cells
     */
    public com.chokavo.chosportsman.network.cells.SportObjectsMoscowCells getCells() {
        return Cells;
    }

    /**
     * 
     * @param Cells
     *     The Cells
     */
    public void setCells(com.chokavo.chosportsman.network.cells.SportObjectsMoscowCells Cells) {
        this.Cells = Cells;
    }

}

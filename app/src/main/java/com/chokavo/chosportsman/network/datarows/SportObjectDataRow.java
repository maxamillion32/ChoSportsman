
package com.chokavo.chosportsman.network.datarows;

import com.chokavo.chosportsman.network.cells.SportObjectCells;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SportObjectDataRow extends BaseDataRow{

    @SerializedName("Cells")
    @Expose
    private SportObjectCells Cells;

    /**
     * 
     * @return
     *     The Cells
     */
    public SportObjectCells getCells() {
        return Cells;
    }

    /**
     * 
     * @param Cells
     *     The Cells
     */
    public void setCells(SportObjectCells Cells) {
        this.Cells = Cells;
    }

}

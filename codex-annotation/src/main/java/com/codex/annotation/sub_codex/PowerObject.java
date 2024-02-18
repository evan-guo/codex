package com.codex.annotation.sub_codex;

import lombok.Getter;
import lombok.Setter;

/**
 * 权限构建对象
 *
 * @author evan guo
 * @since 1.0.0
 */
@Setter
@Getter
public class PowerObject {

    private boolean add;
    private boolean delete;
    private boolean edit;
    private boolean query;
    private boolean export;
    private boolean importable;

    public PowerObject(Power power) {
        this.add = power.add();
        this.delete = power.delete();
        this.edit = power.edit();
        this.query = power.query();
        this.export = power.export();
        this.importable = power.importable();
    }

    public PowerObject() {

    }

}

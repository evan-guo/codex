package com.codex.core.util;

import com.codex.annotation.sub_codex.Power;
import com.codex.annotation.sub_codex.PowerObject;
import com.codex.core.exception.NoPowerException;
import com.codex.core.scan.CodexModel;

import java.util.function.Function;

/**
 * 权限校验工具
 *
 * @author evan guo
 */
public class PowerUtil {

    public static void powerLegal(CodexModel codexModel, Function<PowerObject, Boolean> function) {
        Power power = codexModel.getCodex().power();
        PowerObject powerObject = new PowerObject(power);
        if (!function.apply(powerObject)) {
            throw new NoPowerException("接口权限不足");
        }
    }

}

package com.codex.api.util;

import com.codex.api.annotation.Power;
import com.codex.api.annotation.PowerObject;
import com.codex.api.exception.NoPowerException;
import com.codex.api.scan.CodexModel;

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

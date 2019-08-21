package com.frc107.vanguard.core.utils.callbacks;

public interface ICallbackWithParamAndResult<P,R> {
    R call(P param);
}

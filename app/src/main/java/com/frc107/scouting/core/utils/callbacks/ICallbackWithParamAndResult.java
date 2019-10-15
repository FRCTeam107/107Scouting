package com.frc107.scouting.core.utils.callbacks;

public interface ICallbackWithParamAndResult<P,R> {
    R call(P param);
}

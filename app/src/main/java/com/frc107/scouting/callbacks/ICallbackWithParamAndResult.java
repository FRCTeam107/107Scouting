package com.frc107.scouting.callbacks;

public interface ICallbackWithParamAndResult<P,R> {
    R call(P param);
}

package com.miisteuhdiack.springexceldatasave.exceptions;

import com.miisteuhdiack.springexceldatasave.utils.ExceptionMessage;

public class NotFoundException extends CoreException {
    public NotFoundException() {
        super(ExceptionMessage.NOT_FOUND);
    }

}

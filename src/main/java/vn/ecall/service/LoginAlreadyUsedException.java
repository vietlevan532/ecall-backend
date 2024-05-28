package vn.ecall.service;

import vn.ecall.web.rest.errors.BadRequestAlertException;
import vn.ecall.web.rest.errors.ErrorConstants;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    public static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login name already used", "userManagement", "userexists");
    }
}

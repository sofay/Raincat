/*
 *
 * Copyright 2017-2018 549477611@qq.com(xiaoyu)
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.raincat.admin.controller;

import com.raincat.admin.dto.UserDTO;
import com.raincat.admin.service.LoginService;
import com.raincat.common.holder.httpclient.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LoginController.
 * @author xiaoyu(Myth)
 */
@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping("/login")
    public AjaxResponse login(final @RequestBody UserDTO userDTO) {
        final Boolean login = loginService.login(userDTO.getUserName(), userDTO.getPassword());
        return AjaxResponse.success(login);
    }

    @RequestMapping("/logout")
    public AjaxResponse logout() {
        return AjaxResponse.success(loginService.logout());
    }
    
}

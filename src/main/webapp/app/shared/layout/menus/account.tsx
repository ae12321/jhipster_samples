import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

const accountMenuItemsAuthenticated = () => (
  <>
    <MenuItem icon="wrench" to="/account/settings" data-cy="settings">
      設定
    </MenuItem>
    <MenuItem icon="lock" to="/account/password" data-cy="passwordItem">
      パスワード
    </MenuItem>
    <MenuItem icon="sign-out-alt" to="/logout" data-cy="logout">
      ログアウト
    </MenuItem>
  </>
);

const accountMenuItems = () => (
  <>
    <MenuItem id="login-item" icon="sign-in-alt" to="/login" data-cy="login">
      認証
    </MenuItem>
    <MenuItem icon="user-plus" to="/account/register" data-cy="register">
      登録
    </MenuItem>
  </>
);

export const AccountMenu = ({ isAuthenticated = false }) => (
  <NavDropdown icon="user" name="アカウント" id="account-menu" data-cy="accountMenu">
    {isAuthenticated ? accountMenuItemsAuthenticated() : accountMenuItems()}
  </NavDropdown>
);

export default AccountMenu;

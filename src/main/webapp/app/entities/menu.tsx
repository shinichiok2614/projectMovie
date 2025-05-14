import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/phim">
        <Translate contentKey="global.menu.entities.phim" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/suat-chieu">
        <Translate contentKey="global.menu.entities.suatChieu" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cum-rap">
        <Translate contentKey="global.menu.entities.cumRap" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/rap">
        <Translate contentKey="global.menu.entities.rap" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/phong">
        <Translate contentKey="global.menu.entities.phong" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ghe">
        <Translate contentKey="global.menu.entities.ghe" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/loai-ghe">
        <Translate contentKey="global.menu.entities.loaiGhe" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/bap-nuoc">
        <Translate contentKey="global.menu.entities.bapNuoc" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/danh-sach-ghe">
        <Translate contentKey="global.menu.entities.danhSachGhe" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/danh-sach-bap-nuoc">
        <Translate contentKey="global.menu.entities.danhSachBapNuoc" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ve">
        <Translate contentKey="global.menu.entities.ve" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;

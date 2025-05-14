import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './danh-sach-bap-nuoc.reducer';

export const DanhSachBapNuocDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const danhSachBapNuocEntity = useAppSelector(state => state.danhSachBapNuoc.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="danhSachBapNuocDetailsHeading">
          <Translate contentKey="projectMovieApp.danhSachBapNuoc.detail.title">DanhSachBapNuoc</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{danhSachBapNuocEntity.id}</dd>
          <dt>
            <span id="soDienThoai">
              <Translate contentKey="projectMovieApp.danhSachBapNuoc.soDienThoai">So Dien Thoai</Translate>
            </span>
          </dt>
          <dd>{danhSachBapNuocEntity.soDienThoai}</dd>
          <dt>
            <span id="tenBapNuoc">
              <Translate contentKey="projectMovieApp.danhSachBapNuoc.tenBapNuoc">Ten Bap Nuoc</Translate>
            </span>
          </dt>
          <dd>{danhSachBapNuocEntity.tenBapNuoc}</dd>
          <dt>
            <Translate contentKey="projectMovieApp.danhSachBapNuoc.ve">Ve</Translate>
          </dt>
          <dd>{danhSachBapNuocEntity.ve ? danhSachBapNuocEntity.ve.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/danh-sach-bap-nuoc" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/danh-sach-bap-nuoc/${danhSachBapNuocEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DanhSachBapNuocDetail;

import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './danh-sach-ghe.reducer';

export const DanhSachGheDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const danhSachGheEntity = useAppSelector(state => state.danhSachGhe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="danhSachGheDetailsHeading">
          <Translate contentKey="projectMovieApp.danhSachGhe.detail.title">DanhSachGhe</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{danhSachGheEntity.id}</dd>
          <dt>
            <span id="soDienThoai">
              <Translate contentKey="projectMovieApp.danhSachGhe.soDienThoai">So Dien Thoai</Translate>
            </span>
          </dt>
          <dd>{danhSachGheEntity.soDienThoai}</dd>
          <dt>
            <span id="tenGhe">
              <Translate contentKey="projectMovieApp.danhSachGhe.tenGhe">Ten Ghe</Translate>
            </span>
          </dt>
          <dd>{danhSachGheEntity.tenGhe}</dd>
        </dl>
        <Button tag={Link} to="/danh-sach-ghe" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/danh-sach-ghe/${danhSachGheEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DanhSachGheDetail;

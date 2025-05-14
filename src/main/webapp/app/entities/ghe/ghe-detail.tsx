import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ghe.reducer';

export const GheDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const gheEntity = useAppSelector(state => state.ghe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gheDetailsHeading">
          <Translate contentKey="projectMovieApp.ghe.detail.title">Ghe</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gheEntity.id}</dd>
          <dt>
            <span id="tenGhe">
              <Translate contentKey="projectMovieApp.ghe.tenGhe">Ten Ghe</Translate>
            </span>
          </dt>
          <dd>{gheEntity.tenGhe}</dd>
          <dt>
            <span id="tinhTrang">
              <Translate contentKey="projectMovieApp.ghe.tinhTrang">Tinh Trang</Translate>
            </span>
          </dt>
          <dd>{gheEntity.tinhTrang}</dd>
          <dt>
            <Translate contentKey="projectMovieApp.ghe.phong">Phong</Translate>
          </dt>
          <dd>{gheEntity.phong ? gheEntity.phong.id : ''}</dd>
          <dt>
            <Translate contentKey="projectMovieApp.ghe.ve">Ve</Translate>
          </dt>
          <dd>{gheEntity.ve ? gheEntity.ve.id : ''}</dd>
          <dt>
            <Translate contentKey="projectMovieApp.ghe.loaiGhe">Loai Ghe</Translate>
          </dt>
          <dd>{gheEntity.loaiGhe ? gheEntity.loaiGhe.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ghe" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ghe/${gheEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GheDetail;

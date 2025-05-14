import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ve.reducer';

export const VeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const veEntity = useAppSelector(state => state.ve.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="veDetailsHeading">
          <Translate contentKey="projectMovieApp.ve.detail.title">Ve</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{veEntity.id}</dd>
          <dt>
            <span id="soDienThoai">
              <Translate contentKey="projectMovieApp.ve.soDienThoai">So Dien Thoai</Translate>
            </span>
          </dt>
          <dd>{veEntity.soDienThoai}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="projectMovieApp.ve.email">Email</Translate>
            </span>
          </dt>
          <dd>{veEntity.email}</dd>
          <dt>
            <span id="giaTien">
              <Translate contentKey="projectMovieApp.ve.giaTien">Gia Tien</Translate>
            </span>
          </dt>
          <dd>{veEntity.giaTien}</dd>
          <dt>
            <span id="tinhTrang">
              <Translate contentKey="projectMovieApp.ve.tinhTrang">Tinh Trang</Translate>
            </span>
          </dt>
          <dd>{veEntity.tinhTrang}</dd>
          <dt>
            <Translate contentKey="projectMovieApp.ve.suatChieu">Suat Chieu</Translate>
          </dt>
          <dd>{veEntity.suatChieu ? veEntity.suatChieu.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ve" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ve/${veEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VeDetail;

import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './loai-ghe.reducer';

export const LoaiGheDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const loaiGheEntity = useAppSelector(state => state.loaiGhe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="loaiGheDetailsHeading">
          <Translate contentKey="projectMovieApp.loaiGhe.detail.title">LoaiGhe</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{loaiGheEntity.id}</dd>
          <dt>
            <span id="tenLoai">
              <Translate contentKey="projectMovieApp.loaiGhe.tenLoai">Ten Loai</Translate>
            </span>
          </dt>
          <dd>{loaiGheEntity.tenLoai}</dd>
          <dt>
            <span id="giaTien">
              <Translate contentKey="projectMovieApp.loaiGhe.giaTien">Gia Tien</Translate>
            </span>
          </dt>
          <dd>{loaiGheEntity.giaTien}</dd>
        </dl>
        <Button tag={Link} to="/loai-ghe" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/loai-ghe/${loaiGheEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LoaiGheDetail;

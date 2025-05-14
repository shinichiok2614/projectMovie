import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './phong.reducer';

export const PhongDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const phongEntity = useAppSelector(state => state.phong.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="phongDetailsHeading">
          <Translate contentKey="projectMovieApp.phong.detail.title">Phong</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{phongEntity.id}</dd>
          <dt>
            <span id="tenPhong">
              <Translate contentKey="projectMovieApp.phong.tenPhong">Ten Phong</Translate>
            </span>
          </dt>
          <dd>{phongEntity.tenPhong}</dd>
          <dt>
            <Translate contentKey="projectMovieApp.phong.rap">Rap</Translate>
          </dt>
          <dd>{phongEntity.rap ? phongEntity.rap.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/phong" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/phong/${phongEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PhongDetail;

import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './rap.reducer';

export const RapDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const rapEntity = useAppSelector(state => state.rap.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rapDetailsHeading">
          <Translate contentKey="projectMovieApp.rap.detail.title">Rap</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rapEntity.id}</dd>
          <dt>
            <span id="tenRap">
              <Translate contentKey="projectMovieApp.rap.tenRap">Ten Rap</Translate>
            </span>
          </dt>
          <dd>{rapEntity.tenRap}</dd>
          <dt>
            <span id="diaChi">
              <Translate contentKey="projectMovieApp.rap.diaChi">Dia Chi</Translate>
            </span>
          </dt>
          <dd>{rapEntity.diaChi}</dd>
          <dt>
            <span id="thanhPho">
              <Translate contentKey="projectMovieApp.rap.thanhPho">Thanh Pho</Translate>
            </span>
          </dt>
          <dd>{rapEntity.thanhPho}</dd>
          <dt>
            <Translate contentKey="projectMovieApp.rap.cumRap">Cum Rap</Translate>
          </dt>
          <dd>{rapEntity.cumRap ? rapEntity.cumRap.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/rap" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rap/${rapEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RapDetail;

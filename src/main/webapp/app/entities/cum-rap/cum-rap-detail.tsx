import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cum-rap.reducer';

export const CumRapDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cumRapEntity = useAppSelector(state => state.cumRap.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cumRapDetailsHeading">
          <Translate contentKey="projectMovieApp.cumRap.detail.title">CumRap</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cumRapEntity.id}</dd>
          <dt>
            <span id="tenCumRap">
              <Translate contentKey="projectMovieApp.cumRap.tenCumRap">Ten Cum Rap</Translate>
            </span>
          </dt>
          <dd>{cumRapEntity.tenCumRap}</dd>
          <dt>
            <span id="logo">
              <Translate contentKey="projectMovieApp.cumRap.logo">Logo</Translate>
            </span>
          </dt>
          <dd>
            {cumRapEntity.logo ? (
              <div>
                {cumRapEntity.logoContentType ? (
                  <a onClick={openFile(cumRapEntity.logoContentType, cumRapEntity.logo)}>
                    <img src={`data:${cumRapEntity.logoContentType};base64,${cumRapEntity.logo}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {cumRapEntity.logoContentType}, {byteSize(cumRapEntity.logo)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/cum-rap" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cum-rap/${cumRapEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CumRapDetail;

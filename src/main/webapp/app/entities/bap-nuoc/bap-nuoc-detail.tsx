import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bap-nuoc.reducer';

export const BapNuocDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bapNuocEntity = useAppSelector(state => state.bapNuoc.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bapNuocDetailsHeading">
          <Translate contentKey="projectMovieApp.bapNuoc.detail.title">BapNuoc</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bapNuocEntity.id}</dd>
          <dt>
            <span id="tenBapNuoc">
              <Translate contentKey="projectMovieApp.bapNuoc.tenBapNuoc">Ten Bap Nuoc</Translate>
            </span>
          </dt>
          <dd>{bapNuocEntity.tenBapNuoc}</dd>
          <dt>
            <span id="logo">
              <Translate contentKey="projectMovieApp.bapNuoc.logo">Logo</Translate>
            </span>
          </dt>
          <dd>
            {bapNuocEntity.logo ? (
              <div>
                {bapNuocEntity.logoContentType ? (
                  <a onClick={openFile(bapNuocEntity.logoContentType, bapNuocEntity.logo)}>
                    <img src={`data:${bapNuocEntity.logoContentType};base64,${bapNuocEntity.logo}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {bapNuocEntity.logoContentType}, {byteSize(bapNuocEntity.logo)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="giaTien">
              <Translate contentKey="projectMovieApp.bapNuoc.giaTien">Gia Tien</Translate>
            </span>
          </dt>
          <dd>{bapNuocEntity.giaTien}</dd>
        </dl>
        <Button tag={Link} to="/bap-nuoc" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bap-nuoc/${bapNuocEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BapNuocDetail;
